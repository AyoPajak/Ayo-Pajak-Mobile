package SPT

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import global.PreferencesKey
import global.PreferencesKey.Companion.PertamaUserApiToken
import global.PreferencesKey.Companion.UserApiKey
import global.PreferencesKey.Companion.UserApiSecret
import global.QueryKeyword
import global.TaxStatus
import global.Variables
import http.Account
import http.Interfaces
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import models.ApiODataQueryModel
import models.ODataFilterType
import models.ODataQueryOrderDirection
import models.transaction.Form1770HdResponseApiModel
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
	
	private fun getUserApiToken(scope: CoroutineScope, requestNew: Boolean = false) : String {
		var apiToken = runBlocking {
			prefs.data.first()[stringPreferencesKey(PertamaUserApiToken)].toString()
		}
		
		if (requestNew || apiToken.isBlank()) {
//			apiToken = requestUserApiToken(scope)
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
		
		val apiToken = requestUserApiToken(scope)
		if (apiToken.isBlank()) {
			println("Fail: Api token is null")
			return sptList
		}
		
		if (!query.OrderByList.any())
		{
			query.OrderByList[Form1770HdResponseApiModel::Id.name] = ODataQueryOrderDirection.DESCENDING.value
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
}