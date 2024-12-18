package SPT

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import global.JobName
import global.KluCode
import global.PreferencesKey
import global.PreferencesKey.Companion.PertamaUserApiToken
import global.PreferencesKey.Companion.UserApiKey
import global.PreferencesKey.Companion.UserApiSecret
import global.QueryKeyword
import global.TaxStatus
import global.Variables
import http.Account
import http.Interfaces
import io.ktor.util.reflect.Type
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import models.ApiODataQueryModel
import models.ODataFilterType
import models.ODataQueryOrderDirection
import models.PertamaGeneralApiResponse
import models.ReturnStatus
import models.master.CityModel
import models.master.FamilyRelModel
import models.master.JobModel
import models.master.KluModel
import models.transaction.Form1770HdRequestApiModel
import models.transaction.Form1770HdResponseApiModel
import models.transaction.FormDependentRequestApiModel
import models.transaction.FormDependentResponseApiModel
import models.transaction.FormIdentityRequestApiModel
import models.transaction.FormIdentityResponseApiModel
import util.onError
import util.onSuccess
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SPTManager(val prefs: DataStore<Preferences>, val client: Account, val sptPertamaClient: Interfaces) {
	
	companion object {
		private const val ODataMaxTop: Int = 100
		
		fun isPHMTTaxStatus(taxStatusE: String) : Boolean {
			return taxStatusE.equals(TaxStatus.PisahHarta.value, true) || taxStatusE.equals(TaxStatus.MemilihTerpisahKewajibanPajak.value, true)
		}
	}
	
	private suspend fun getUserApiToken(scope: CoroutineScope, requestNew: Boolean = false) : String {
		var apiToken = runBlocking {
			prefs.data.first()[stringPreferencesKey(PertamaUserApiToken)].toString()
		}
		
		if (requestNew || apiToken.isBlank()) {
			apiToken = requestUserApiToken(scope)
		}
		
		return apiToken
	}
	
	suspend fun requestUserApiToken(scope: CoroutineScope, updatePreference: Boolean = true) : String {
		var apiToken = ""
		var userKey = ""
		var userSecret = ""
		
		runBlocking {
			val prefs = prefs.data.first()
			userKey = prefs[stringPreferencesKey(UserApiKey)].toString()
			userSecret = prefs[stringPreferencesKey(UserApiSecret)].toString()
		}
		
		if (userKey.isBlank() || userSecret.isBlank())
			return apiToken
		
		try {
			return suspendCoroutine { cont ->
				scope.launch {
					sptPertamaClient.ApiToken(userKey, userSecret, "grant_type=password")
						.onSuccess {
							apiToken = it.AccessToken
							if (updatePreference && apiToken.isNotBlank()) {
								prefs.edit { dataStore ->
									dataStore[stringPreferencesKey(PertamaUserApiToken)] = it.AccessToken
								}
								apiToken = it.AccessToken
								cont.resume(apiToken)
							}
						}
						.onError {
							println(it.name)
						}
				}
			}
		}
		catch (ex: Exception)
		{
			println(ex.message)
		}
		
		return apiToken
	}
	
	suspend fun getSptHdList(scope: CoroutineScope, query: ApiODataQueryModel) : List<Form1770HdResponseApiModel> {
		var sptList: List<Form1770HdResponseApiModel> = ArrayList()
		
		val apiToken = getUserApiToken(scope, true)
		if (apiToken.isBlank()) {
			println("Fail: Api token is null")
			return sptList
		}
		
		if (!query.OrderByList.any())
		{
			query.OrderByList[Form1770HdResponseApiModel::TaxYear.name] = ODataQueryOrderDirection.DESCENDING.value
			query.OrderByList[Form1770HdResponseApiModel::CorrectionSeq.name] = ODataQueryOrderDirection.DESCENDING.value
		}
		val queryMap = mapQueryModel(query)
		
		return suspendCoroutine { cont ->
			scope.launch {
				sptPertamaClient.getSptHdList("Bearer $apiToken", queryMap)
					.onSuccess {
						sptList = it.Items ?: listOf()
						cont.resume(sptList)
					}
					.onError {
						println(it.name)
					}
			}
		}
	}
	
	private fun mapQueryModel(query: ApiODataQueryModel, overrideMaxTop: Boolean = false): HashMap<String, String> {
		val result: HashMap<String, String> = HashMap()
		if (!query.FilterModel?.value.isNullOrBlank() && query.FilterModel?.fields?.any() == true)
		{
			val filterModelObj = query.FilterModel!!
			var filterModelText = ""
			if (filterModelObj.filterType == ODataFilterType.CONTAIN)
				filterModelText = filterModelObj.fields.joinToString(separator = " or ") {
					"${ODataFilterType.CONTAIN.value}(${it}, '${filterModelObj.value}')"
				}
			else if (filterModelObj.filterType == ODataFilterType.EQUAL)
				filterModelText = filterModelObj.fields.joinToString(separator = " or ") {
					"$it ${ODataFilterType.EQUAL.value} '${filterModelObj.value}'"
				}
			
			if (filterModelText.isNotBlank())
				result[QueryKeyword.FILTER.value] = filterModelText
		}
		if (query.ItemCountPerPage > 0)
		{
			result[QueryKeyword.TOP.value] = if (!overrideMaxTop) query.ItemCountPerPage.coerceAtMost(ODataMaxTop).toString() else query.ItemCountPerPage.toString()
			
			if (query.PageNum > 0) {
				val skipCount = query.ItemCountPerPage * (query.PageNum - 1)
				result[QueryKeyword.SKIP.value] = skipCount.toString()
			}
		}
		if (query.OrderByList.any()) {
			result[QueryKeyword.ORDER_BY.value] = query.OrderByList.map { "${it.key} ${it.value}" }.joinToString(separator = ", ")
		}
		
		result[QueryKeyword.COUNT.value] = "true"
		
		return result
	}
	
	suspend fun getLatestSPT(scope: CoroutineScope, year: String = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).year.toString()) : Form1770HdResponseApiModel
	{
		var result: Form1770HdResponseApiModel
		val apiToken = getUserApiToken(scope, true)
		if (apiToken.isBlank()) {
			println("Api token is null")
		}
				
		return suspendCoroutine { cont ->
			scope.launch {
				sptPertamaClient.getSptRevMax("Bearer $apiToken", year)
					.onSuccess {
						result = it
						cont.resume(result)
					}
					.onError {
						println(it.name)
					}
			}
		}
	}
	
	suspend fun getSptHdById(scope: CoroutineScope, entityId: Int) : Form1770HdResponseApiModel? {
		var result: Form1770HdResponseApiModel?
		
		val apiToken = getUserApiToken(scope, true)
		if (apiToken.isBlank()) {
			println("Api token is null")
			return null
		}
		
		return suspendCoroutine { cont ->
			scope.launch {
				sptPertamaClient.getSptHdById("Bearer $apiToken", entityId)
					.onSuccess {
						result = it
						cont.resume(result)
					}
					.onError {
						println(it.name)
					}
			}
		}
	}
	
	suspend fun createSpt(scope: CoroutineScope, body: Form1770HdRequestApiModel) : PertamaGeneralApiResponse? {
		var result: PertamaGeneralApiResponse?
		
		val apiToken = getUserApiToken(scope, true)
		if (apiToken.isBlank()) {
			println("Api token is null")
			return null
		}
		
		return suspendCoroutine { cont ->
			scope.launch {
				sptPertamaClient.initInputForm1770("Bearer $apiToken", body)
					.onSuccess {
						result = it
						cont.resume(result)
					}
					.onError {
						println(it.name)
					}
			}
		}
	}
	
	suspend fun getCityList(scope: CoroutineScope): List<CityModel> {
		var cityList: List<CityModel> = ArrayList()
		
		val apiToken = getUserApiToken(scope, true)
		if (apiToken.isBlank()) {
			println("Api token is null")
			return cityList
		}
		
		return suspendCoroutine { cont ->
			scope.launch {
				sptPertamaClient.getCityList("Bearer $apiToken")
					.onSuccess {
						cityList = it.Items ?: listOf()
						cont.resume(cityList)
					}
					.onError {
						println(it.name)
					}
			}
		}
	}
	
	suspend fun getJobList(scope: CoroutineScope): List<JobModel> {
		var jobList: List<JobModel> = ArrayList()
		
		val apiToken = getUserApiToken(scope, true)
		if (apiToken.isBlank()) {
			println("Api token is null")
			return jobList
		}
		
		return suspendCoroutine { cont ->
			scope.launch {
				sptPertamaClient.getJobList("Bearer $apiToken")
					.onSuccess {
						jobList = it.Items ?: listOf()
						cont.resume(jobList)
					}
					.onError {
						println(it.name)
					}
			}
		}
	}
	
	suspend fun getKluList(scope: CoroutineScope, query: ApiODataQueryModel, taxYear: Int = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).year): List<KluModel> {
		var kluList: List<KluModel> = ArrayList()
		
		val apiToken = getUserApiToken(scope, true)
		if (apiToken.isBlank()) {
			println("Api token is null")
			return kluList
		}
		
		val queryMap = mapQueryModel(query)
		
		return suspendCoroutine { cont ->
			scope.launch {
				sptPertamaClient.getKluList("Bearer $apiToken", queryMap, taxYear)
					.onSuccess {
						kluList = it.Items ?: listOf()
						cont.resume(kluList)
					}
					.onError {
						println(it.name)
					}
			}
		}
	}
	
	fun getJobKluMap() : Map<JobName, List<String>> {
		val jobKluMap: HashMap<JobName, List<String>> = hashMapOf()
		jobKluMap[JobName.Pegawai] = listOf(KluCode.PNS.value, KluCode.PegawaiBUMN.value, KluCode.PegawaiSwasta.value, KluCode.Pensiunan.value)
		jobKluMap[JobName.Mahasiswa] = listOf(KluCode.JasaPeroranganLainnya.value)
		jobKluMap[JobName.Pelajar] = listOf(KluCode.JasaPeroranganLainnya.value)
		
		return jobKluMap
	}
	
	suspend fun saveIdentity(scope: CoroutineScope, body: FormIdentityRequestApiModel): ReturnStatus {
		val result = ReturnStatus()
		
		val apiToken = getUserApiToken(scope, true)
		if (apiToken.isBlank()) {
			println("Api token is null")
			result.SetError("Api token is null")
			return result
		}
		
		return suspendCoroutine { cont ->
			scope.launch {
				sptPertamaClient.saveIdentity("Bearer $apiToken", body)
					.onSuccess {
						cont.resume(result)
					}
					.onError {
						println(it.name)
					}
			}
		}
	}
	
	suspend fun getIdentityData(scope: CoroutineScope, sptHd: Int): FormIdentityResponseApiModel? {
		var result: FormIdentityResponseApiModel? = null
		
		val apiToken = getUserApiToken(scope, true)
		if (apiToken.isBlank()) {
			println("Api token is null")
			return result
		}
		
		return suspendCoroutine { cont ->
			scope.launch {
				sptPertamaClient.getIdentityById("Bearer $apiToken", sptHd)
					.onSuccess {
						result = it
						cont.resume(result)
					}
					.onError {
						println(it.name)
					}
			}
		}
	}
	
	suspend fun getDependentList(scope: CoroutineScope, sptId: Int, query: ApiODataQueryModel) : List<FormDependentResponseApiModel>? {
		var dependentList : List<FormDependentResponseApiModel>? = null
		val apiToken = getUserApiToken(scope, true)
		if (apiToken.isBlank()) {
			println("Fail: Api token is null")
			return null
		}
		
		val queryMap = mapQueryModel(query)
		
		return suspendCoroutine { cont ->
			scope.launch {
				sptPertamaClient.getDependentList("Bearer $apiToken", queryMap, sptId)
					.onSuccess {
						dependentList = it.Items ?: listOf()
						cont.resume(dependentList)
					}
					.onError {
						println(it.name)
					}
			}
		}
	}
	
	suspend fun getDependentById(scope: CoroutineScope, id: Int) : FormDependentResponseApiModel? {
		var data: FormDependentResponseApiModel?
		val apiToken = getUserApiToken(scope, true)
		if (apiToken.isBlank()) {
			println("Api token is null")
			return null
		}
		
		return suspendCoroutine { cont ->
			scope.launch {
				sptPertamaClient.getDependentById("Bearer $apiToken", id)
					.onSuccess {
						data = it
						cont.resume(data)
					}
					.onError {
						println(it.name)
					}
			}
		}
	}
	
	suspend fun getFamilyRel(scope: CoroutineScope): List<FamilyRelModel> {
		var familyRelList: List<FamilyRelModel> = ArrayList()
		
		val apiToken = getUserApiToken(scope, true)
		if (apiToken.isBlank()) {
			println("Api token is null")
			return familyRelList
		}
		
		return suspendCoroutine { cont ->
			scope.launch {
				sptPertamaClient.getFamilyRel("Bearer $apiToken")
					.onSuccess {
						familyRelList = it.Items ?: listOf()
						cont.resume(familyRelList)
					}
					.onError {
						println(it.name)
					}
			}
		}
	}
	
	suspend fun saveDependent(scope: CoroutineScope, body: FormDependentRequestApiModel): ReturnStatus {
		val result = ReturnStatus()
		
		val apiToken = getUserApiToken(scope, true)
		if (apiToken.isBlank()) {
			println("Api token is null")
			result.SetError("Api token is null")
			return result
		}
		
		return suspendCoroutine { cont ->
			scope.launch {
				sptPertamaClient.saveDependent("Bearer $apiToken", body)
					.onSuccess {
						println(result.Message)
						cont.resume(result)
					}
					.onError {
						println(it.name)
					}
			}
		}
	}
	
	suspend fun deleteSpt(scope: CoroutineScope, id: Int): ReturnStatus {
		val result = ReturnStatus()
		
		val apiToken = getUserApiToken(scope, true)
		if (apiToken.isBlank()) {
			println("Api token is null")
			result.SetError("Api token is null")
			return result
		}
		
		return suspendCoroutine { cont ->
			scope.launch {
				sptPertamaClient.deleteSpt("Bearer $apiToken", id)
					.onSuccess {
						cont.resume(result)
					}
					.onError {
						println(it.name)
					}
			}
		}
	}
	
	suspend fun deleteDependent(scope: CoroutineScope, id: Int): ReturnStatus {
		val result = ReturnStatus()
		
		val apiToken = getUserApiToken(scope, true)
		if (apiToken.isBlank()) {
			println("Api token is null")
			result.SetError("Api token is null")
			return result
		}
		
		return suspendCoroutine { cont ->
			scope.launch {
				sptPertamaClient.deleteDependent("Bearer $apiToken", id)
					.onSuccess {
						cont.resume(result)
					}
					.onError {
						println(it.name)
					}
			}
		}
	}
}