package SPT

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import global.JobName
import global.KluCode
import global.PreferencesKey.Companion.PertamaUserApiToken
import global.PreferencesKey.Companion.UserApiKey
import global.PreferencesKey.Companion.UserApiSecret
import global.QueryKeyword
import global.TaxStatus
import http.Account
import http.Interfaces
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import models.ApiODataQueryModel
import models.ODataFilterType
import models.ODataQueryOrderDirection
import models.PertamaGeneralApiResponse
import models.ReturnStatus
import models.master.CityModel
import models.master.CurrencyModel
import models.master.CurrencyRateModel
import models.master.DebtTypeModel
import models.master.FamilyRelModel
import models.master.InterestTypeModel
import models.master.JobModel
import models.master.KluModel
import models.master.WealthTypeModel
import models.transaction.AssetGroupResponseApiModel
import models.transaction.Form1770HdRequestApiModel
import models.transaction.Form1770HdResponseApiModel
import models.transaction.FormDebtRequestApiModel
import models.transaction.FormDebtResponseApiModel
import models.transaction.FormDependentRequestApiModel
import models.transaction.FormDependentResponseApiModel
import models.transaction.FormIdentityRequestApiModel
import models.transaction.FormIdentityResponseApiModel
import models.transaction.FormWealthARequestApiModel
import models.transaction.FormWealthBRequestApiModel
import models.transaction.FormWealthCRequestApiModel
import models.transaction.FormWealthDRequestApiModel
import models.transaction.FormWealthERequestApiModel
import models.transaction.FormWealthFRequestApiModel
import models.transaction.FormWealthGRequestApiModel
import models.transaction.FormWealthHRequestApiModel
import models.transaction.FormWealthIRequestApiModel
import models.transaction.FormWealthJRequestApiModel
import models.transaction.FormWealthKRequestApiModel
import models.transaction.FormWealthLRequestApiModel
import models.transaction.FormWealthResponseApiModel
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
						cont.resume(result)
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
						cont.resume(result)
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
						cont.resume(dependentList)
					}
			}
		}
	}
	
	suspend fun getDependentById(scope: CoroutineScope, id: Int) : FormDependentResponseApiModel? {
		var data: FormDependentResponseApiModel? = null
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
						cont.resume(data)
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
						cont.resume(familyRelList)
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
						cont.resume(result)
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
						cont.resume(result)
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
						cont.resume(result)
					}
			}
		}
	}
	
	//Wealth
	suspend fun getWealthSummaryByType(scope: CoroutineScope, hdId: String) : List<AssetGroupResponseApiModel> {
		var wealthList: List<AssetGroupResponseApiModel> = ArrayList()
		
		val apiToken = getUserApiToken(scope, true)
		if (apiToken.isBlank()) {
			println("Fail: Api token is null")
			return wealthList
		}
		
		return suspendCoroutine { cont ->
			scope.launch {
				sptPertamaClient.getWealthSummaryPerCode("Bearer $apiToken", hdId)
					.onSuccess {
						wealthList = it.Items ?: listOf()
						cont.resume(wealthList)
					}
					.onError {
						println(it.name)
						cont.resume(wealthList)
					}
			}
		}
	}
	
	suspend fun getWealthTotal(scope: CoroutineScope, hdId: String, wealthTypeId: String? = null) : Double {
		var total = 0.0
		
		val apiToken = getUserApiToken(scope, true)
		if (apiToken.isBlank()) {
			println("Fail: Api token is null")
			return total
		}
		
		return suspendCoroutine { cont ->
			scope.launch {
				sptPertamaClient.getWealthTotal("Bearer $apiToken", hdId, wealthTypeId)
					.onSuccess {
						total = it
						cont.resume(total)
					}
					.onError {
						println(it.name)
						cont.resume(total)
					}
			}
		}
	}
	
	suspend fun getWealthData(scope: CoroutineScope, hdId: String, wealthTypeId: String? = null): List<FormWealthResponseApiModel> {
		var wealthData: List<FormWealthResponseApiModel> = ArrayList()
		
		val apiToken = getUserApiToken(scope, true)
		if (apiToken.isBlank()) {
			println("Fail: Api token is null")
			return wealthData
		}
		
		return suspendCoroutine { cont ->
			scope.launch {
				sptPertamaClient.getWealthData("Bearer $apiToken", hdId, wealthTypeId)
					.onSuccess {
						wealthData = it.Items ?: listOf()
						cont.resume(wealthData)
					}
					.onError {
						println(it.name)
						cont.resume(wealthData)
					}
			}
		}
	}
	
	suspend fun getWealthDataById(scope: CoroutineScope, id: String): FormWealthResponseApiModel? {
		var wealthData: FormWealthResponseApiModel? = null
		
		val apiToken = getUserApiToken(scope, true)
		if (apiToken.isBlank()) {
			println("Fail: Api token is null")
			return wealthData
		}
		
		return suspendCoroutine { cont ->
			scope.launch {
				sptPertamaClient.getWealthDataById("Bearer $apiToken", id)
					.onSuccess {
						wealthData = it
						cont.resume(wealthData)
					}
					.onError {
						println(it.name)
						cont.resume(wealthData)
					}
			}
		}
	}
	
	suspend fun getWealthTypeList(scope: CoroutineScope): List<WealthTypeModel>? {
		var wealthTypeList: List<WealthTypeModel>? = null
		
		val apiToken = getUserApiToken(scope, true)
		if (apiToken.isBlank()) {
			println("Fail: Api token is null")
			return wealthTypeList
		}
		
		return suspendCoroutine { cont ->
			scope.launch {
				sptPertamaClient.getWealthType("Bearer $apiToken")
					.onSuccess {
						wealthTypeList = it.Items ?: listOf()
						cont.resume(wealthTypeList)
					}
					.onError {
						println(it.name)
						cont.resume(wealthTypeList)
					}
			}
		}
	}
	
	suspend fun getCurrencyList(scope: CoroutineScope): List<CurrencyModel>? {
		var currencyList: List<CurrencyModel>? = null
		
		val apiToken = getUserApiToken(scope, true)
		if (apiToken.isBlank()) {
			println("Fail: Api token is null")
			return currencyList
		}
		
		return suspendCoroutine { cont ->
			scope.launch {
				sptPertamaClient.getCurrency("Bearer $apiToken")
					.onSuccess {
						currencyList = it.Items ?: listOf()
						cont.resume(currencyList)
					}
					.onError {
						println(it.name)
						cont.resume(currencyList)
					}
			}
		}
	}
	
	suspend fun deleteWealth(scope: CoroutineScope, id: String): ReturnStatus {
		val result = ReturnStatus()
		
		val apiToken = getUserApiToken(scope, true)
		if (apiToken.isBlank()) {
			println("Api token is null")
			result.SetError("Api token is null")
			return result
		}
		
		return suspendCoroutine { cont ->
			scope.launch {
				sptPertamaClient.deleteWealth("Bearer $apiToken", id)
					.onSuccess {
						cont.resume(result)
					}
					.onError {
						println(it.name)
						cont.resume(result)
					}
			}
		}
	}
	
	suspend fun getCurrencyRate(scope: CoroutineScope, year: String): List<CurrencyRateModel>? {
		var currencyRateList: List<CurrencyRateModel>? = null
		
		val apiToken = getUserApiToken(scope, true)
		if (apiToken.isBlank()) {
			println("Fail: Api token is null")
			return currencyRateList
		}
		
		return suspendCoroutine { cont ->
			scope.launch {
				sptPertamaClient.getCurrencyRateByYear("Bearer $apiToken", year)
					.onSuccess {
						currencyRateList = it.Items ?: listOf()
						cont.resume(currencyRateList)
					}
					.onError {
						println(it.name)
						cont.resume(currencyRateList)
					}
			}
		}
	}
	
	suspend fun getInterestTypeList(scope: CoroutineScope): List<InterestTypeModel>? {
		var interestTypeList: List<InterestTypeModel>? = null
		
		val apiToken = getUserApiToken(scope, true)
		if (apiToken.isBlank()) {
			println("Fail: Api token is null")
			return interestTypeList
		}
		
		return suspendCoroutine { cont ->
			scope.launch {
				sptPertamaClient.getInterestType("Bearer $apiToken")
					.onSuccess {
						interestTypeList = it.Items ?: listOf()
						cont.resume(interestTypeList)
					}
					.onError {
						println(it.name)
						cont.resume(interestTypeList)
					}
			}
		}
	}
	
	suspend fun saveWealthA(scope: CoroutineScope, body: FormWealthARequestApiModel): ReturnStatus {
		val result = ReturnStatus()
		
		val apiToken = getUserApiToken(scope, true)
		if (apiToken.isBlank()) {
			println("Api token is null")
			result.SetError("Api token is null")
			return result
		}
		
		return suspendCoroutine { cont ->
			scope.launch {
				sptPertamaClient.saveWealthA("Bearer $apiToken", body)
					.onSuccess {
						println(result.Message)
						cont.resume(result)
					}
					.onError {
						println(it.name)
						cont.resume(result)
					}
			}
		}
	}
	
	suspend fun saveWealthB(scope: CoroutineScope, body: FormWealthBRequestApiModel): ReturnStatus {
		val result = ReturnStatus()
		
		val apiToken = getUserApiToken(scope, true)
		if (apiToken.isBlank()) {
			println("Api token is null")
			result.SetError("Api token is null")
			return result
		}
		
		return suspendCoroutine { cont ->
			scope.launch {
				sptPertamaClient.saveWealthB("Bearer $apiToken", body)
					.onSuccess {
						println(result.Message)
						cont.resume(result)
					}
					.onError {
						println(it.name)
						cont.resume(result)
					}
			}
		}
	}
	
	suspend fun saveWealthC(scope: CoroutineScope, body: FormWealthCRequestApiModel): ReturnStatus {
		val result = ReturnStatus()
		
		val apiToken = getUserApiToken(scope, true)
		if (apiToken.isBlank()) {
			println("Api token is null")
			result.SetError("Api token is null")
			return result
		}
		
		return suspendCoroutine { cont ->
			scope.launch {
				sptPertamaClient.saveWealthC("Bearer $apiToken", body)
					.onSuccess {
						println(result.Message)
						cont.resume(result)
					}
					.onError {
						println(it.name)
						cont.resume(result)
					}
			}
		}
	}
	
	suspend fun saveWealthD(scope: CoroutineScope, body: FormWealthDRequestApiModel): ReturnStatus {
		val result = ReturnStatus()
		
		val apiToken = getUserApiToken(scope, true)
		if (apiToken.isBlank()) {
			println("Api token is null")
			result.SetError("Api token is null")
			return result
		}
		
		return suspendCoroutine { cont ->
			scope.launch {
				sptPertamaClient.saveWealthD("Bearer $apiToken", body)
					.onSuccess {
						println(result.Message)
						cont.resume(result)
					}
					.onError {
						println(it.name)
						cont.resume(result)
					}
			}
		}
	}
	
	suspend fun saveWealthE(scope: CoroutineScope, body: FormWealthERequestApiModel): ReturnStatus {
		val result = ReturnStatus()
		
		val apiToken = getUserApiToken(scope, true)
		if (apiToken.isBlank()) {
			println("Api token is null")
			result.SetError("Api token is null")
			return result
		}
		
		return suspendCoroutine { cont ->
			scope.launch {
				sptPertamaClient.saveWealthE("Bearer $apiToken", body)
					.onSuccess {
						println(result.Message)
						cont.resume(result)
					}
					.onError {
						println(it.name)
						cont.resume(result)
					}
			}
		}
	}
	
	suspend fun saveWealthF(scope: CoroutineScope, body: FormWealthFRequestApiModel): ReturnStatus {
		val result = ReturnStatus()
		
		val apiToken = getUserApiToken(scope, true)
		if (apiToken.isBlank()) {
			println("Api token is null")
			result.SetError("Api token is null")
			return result
		}
		
		return suspendCoroutine { cont ->
			scope.launch {
				sptPertamaClient.saveWealthF("Bearer $apiToken", body)
					.onSuccess {
						println(result.Message)
						cont.resume(result)
					}
					.onError {
						println(it.name)
						cont.resume(result)
					}
			}
		}
	}
	
	suspend fun saveWealthG(scope: CoroutineScope, body: FormWealthGRequestApiModel): ReturnStatus {
		val result = ReturnStatus()
		
		val apiToken = getUserApiToken(scope, true)
		if (apiToken.isBlank()) {
			println("Api token is null")
			result.SetError("Api token is null")
			return result
		}
		
		return suspendCoroutine { cont ->
			scope.launch {
				sptPertamaClient.saveWealthG("Bearer $apiToken", body)
					.onSuccess {
						println(result.Message)
						cont.resume(result)
					}
					.onError {
						println(it.name)
						cont.resume(result)
					}
			}
		}
	}
	
	suspend fun saveWealthH(scope: CoroutineScope, body: FormWealthHRequestApiModel): ReturnStatus {
		val result = ReturnStatus()
		
		val apiToken = getUserApiToken(scope, true)
		if (apiToken.isBlank()) {
			println("Api token is null")
			result.SetError("Api token is null")
			return result
		}
		
		return suspendCoroutine { cont ->
			scope.launch {
				sptPertamaClient.saveWealthH("Bearer $apiToken", body)
					.onSuccess {
						println(result.Message)
						cont.resume(result)
					}
					.onError {
						println(it.name)
						cont.resume(result)
					}
			}
		}
	}
	
	suspend fun saveWealthI(scope: CoroutineScope, body: FormWealthIRequestApiModel): ReturnStatus {
		val result = ReturnStatus()
		
		val apiToken = getUserApiToken(scope, true)
		if (apiToken.isBlank()) {
			println("Api token is null")
			result.SetError("Api token is null")
			return result
		}
		
		return suspendCoroutine { cont ->
			scope.launch {
				sptPertamaClient.saveWealthI("Bearer $apiToken", body)
					.onSuccess {
						println(result.Message)
						cont.resume(result)
					}
					.onError {
						println(it.name)
						cont.resume(result)
					}
			}
		}
	}
	
	suspend fun saveWealthJ(scope: CoroutineScope, body: FormWealthJRequestApiModel): ReturnStatus {
		val result = ReturnStatus()
		
		val apiToken = getUserApiToken(scope, true)
		if (apiToken.isBlank()) {
			println("Api token is null")
			result.SetError("Api token is null")
			return result
		}
		
		return suspendCoroutine { cont ->
			scope.launch {
				sptPertamaClient.saveWealthJ("Bearer $apiToken", body)
					.onSuccess {
						println(result.Message)
						cont.resume(result)
					}
					.onError {
						println(it.name)
						cont.resume(result)
					}
			}
		}
	}
	
	suspend fun saveWealthK(scope: CoroutineScope, body: FormWealthKRequestApiModel): ReturnStatus {
		val result = ReturnStatus()
		
		val apiToken = getUserApiToken(scope, true)
		if (apiToken.isBlank()) {
			println("Api token is null")
			result.SetError("Api token is null")
			return result
		}
		
		return suspendCoroutine { cont ->
			scope.launch {
				sptPertamaClient.saveWealthK("Bearer $apiToken", body)
					.onSuccess {
						println(result.Message)
						cont.resume(result)
					}
					.onError {
						println(it.name)
						cont.resume(result)
					}
			}
		}
	}
	
	suspend fun saveWealthL(scope: CoroutineScope, body: FormWealthLRequestApiModel): ReturnStatus {
		val result = ReturnStatus()
		
		val apiToken = getUserApiToken(scope, true)
		if (apiToken.isBlank()) {
			println("Api token is null")
			result.SetError("Api token is null")
			return result
		}
		
		return suspendCoroutine { cont ->
			scope.launch {
				sptPertamaClient.saveWealthL("Bearer $apiToken", body)
					.onSuccess {
						println(result.Message)
						cont.resume(result)
					}
					.onError {
						println(it.name)
						cont.resume(result)
					}
			}
		}
	}
	
	//Debt
	suspend fun getDebtTotal(scope: CoroutineScope, hdId: String) : Double {
		var total = 0.0
		
		val apiToken = getUserApiToken(scope, true)
		if (apiToken.isBlank()) {
			println("Fail: Api token is null")
			return total
		}
		
		return suspendCoroutine { cont ->
			scope.launch {
				sptPertamaClient.getDebtTotal("Bearer $apiToken", hdId)
					.onSuccess {
						total = it
						cont.resume(total)
					}
					.onError {
						println(it.name)
						cont.resume(total)
					}
			}
		}
	}
	
	suspend fun getDebtData(scope: CoroutineScope, hdId: String, debtTypeId: String? = null): List<FormDebtResponseApiModel> {
		var debtData: List<FormDebtResponseApiModel> = ArrayList()
		
		val apiToken = getUserApiToken(scope, true)
		if (apiToken.isBlank()) {
			println("Fail: Api token is null")
			return debtData
		}
		
		return suspendCoroutine { cont ->
			scope.launch {
				sptPertamaClient.getDebtData("Bearer $apiToken", hdId, debtTypeId)
					.onSuccess {
						debtData = it.Items ?: listOf()
						cont.resume(debtData)
					}
					.onError {
						println(it.name)
						cont.resume(debtData)
					}
			}
		}
	}
	
	suspend fun getDebtDataById(scope: CoroutineScope, id: String): FormDebtResponseApiModel? {
		var debtData: FormDebtResponseApiModel? = null
		
		val apiToken = getUserApiToken(scope, true)
		if (apiToken.isBlank()) {
			println("Fail: Api token is null")
			return debtData
		}
		
		return suspendCoroutine { cont ->
			scope.launch {
				sptPertamaClient.getDebtDataById("Bearer $apiToken", id)
					.onSuccess {
						debtData = it
						cont.resume(debtData)
					}
					.onError {
						println(it.name)
						cont.resume(debtData)
					}
			}
		}
	}
	
	suspend fun getDebtTypeList(scope: CoroutineScope): List<DebtTypeModel>? {
		var debtTypeList: List<DebtTypeModel>? = null
		
		val apiToken = getUserApiToken(scope, true)
		if (apiToken.isBlank()) {
			println("Fail: Api token is null")
			return debtTypeList
		}
		
		return suspendCoroutine { cont ->
			scope.launch {
				sptPertamaClient.getDebtType("Bearer $apiToken")
					.onSuccess {
						debtTypeList = it.Items ?: listOf()
						cont.resume(debtTypeList)
					}
					.onError {
						println(it.name)
						cont.resume(debtTypeList)
					}
			}
		}
	}
	
	suspend fun deleteDebt(scope: CoroutineScope, id: String): ReturnStatus {
		val result = ReturnStatus()
		
		val apiToken = getUserApiToken(scope, true)
		if (apiToken.isBlank()) {
			println("Api token is null")
			result.SetError("Api token is null")
			return result
		}
		
		return suspendCoroutine { cont ->
			scope.launch {
				sptPertamaClient.deleteDebt("Bearer $apiToken", id)
					.onSuccess {
						cont.resume(result)
					}
					.onError {
						println(it.name)
						cont.resume(result)
					}
			}
		}
	}
	
	suspend fun saveDebt(scope: CoroutineScope, body: FormDebtRequestApiModel): ReturnStatus {
		val result = ReturnStatus()
		
		val apiToken = getUserApiToken(scope, true)
		if (apiToken.isBlank()) {
			println("Api token is null")
			result.SetError("Api token is null")
			return result
		}
		
		return suspendCoroutine { cont ->
			scope.launch {
				sptPertamaClient.saveDebt("Bearer $apiToken", body)
					.onSuccess {
						println(result.Message)
						cont.resume(result)
					}
					.onError {
						println(it.name)
						cont.resume(result)
					}
			}
		}
	}
}