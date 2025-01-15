package http

import global.Variables
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.SerializationException
import models.PagedListModel
import models.PertamaGeneralApiResponse
import models.account.APITokenModel
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
import models.transaction.Form1770FinalIncomeUmkm2023BusinessRequestModel
import models.transaction.Form1770FinalIncomeUmkm2023ResponseApiModel
import models.transaction.Form1770FinalIncomeUmkm2023SummaryRequestModel
import models.transaction.Form1770HdRequestApiModel
import models.transaction.Form1770HdResponseApiModel
import models.transaction.FormDebtRequestApiModel
import models.transaction.FormDebtResponseApiModel
import models.transaction.FormDependentRequestApiModel
import models.transaction.FormDependentResponseApiModel
import models.transaction.FormFinalIncomeARequestApiModel
import models.transaction.FormFinalIncomeBRequestApiModel
import models.transaction.FormFinalIncomeCRequestApiModel
import models.transaction.FormFinalIncomeDRequestApiModel
import models.transaction.FormFinalIncomeERequestApiModel
import models.transaction.FormFinalIncomeResponseApiModel
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
import models.transaction.SPTSummaryResponseApiModel
import util.NetworkError
import util.Result

class Interfaces(private val httpClient: HttpClient) {
	
	suspend fun ApiToken (userKey: String, userSecret: String, body: String): Result<APITokenModel, NetworkError> {
		val response = try {
			httpClient.post(
				urlString = "${Variables.PertamaApiBaseUrl}Token"
			) {
				header("apikey", userKey)
				header("apisecret", userSecret)
				setBody(body)
				contentType(ContentType.Application.FormUrlEncoded)
			}
		} catch (e: UnresolvedAddressException) {
			return Result.Error(NetworkError.NO_INTERNET)
		} catch (e: SerializationException) {
			return Result.Error(NetworkError.SERIALIZATION)
		}
		
		return when (response.status.value) {
			200 -> {
				val responseBody = response.body<APITokenModel>()
				Result.Success(responseBody)
			}
			401 -> Result.Error(NetworkError.UNAUTHORIZED)
			409 -> Result.Error(NetworkError.CONFLICT)
			408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
			413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
			in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
			else -> Result.Error(NetworkError.UNKNOWN)
		}
	}
	
	suspend fun getSptHdList(apiToken: String, query: HashMap<String, String>? = HashMap()) : Result<PagedListModel<Form1770HdResponseApiModel>, NetworkError> {
		val response = try {
			httpClient.get (
				urlString = "${Variables.PertamaApiBaseUrl}api/SPTTahunanOP/GetListSptHD"
			) {
				header("Authorization", apiToken)
				url {
					query?.forEach { (key, value) ->
						encodedParameters.append(key, value)
					}
				}
				contentType(ContentType.Application.Json)
			}
		} catch (e: UnresolvedAddressException) {
			return Result.Error(NetworkError.NO_INTERNET)
		} catch (e: SerializationException) {
			return Result.Error(NetworkError.SERIALIZATION)
		}
		
		return when (response.status.value) {
			200 -> {
				val responseBody = response.body<PagedListModel<Form1770HdResponseApiModel>>()
				Result.Success(responseBody)
			}
			401 -> Result.Error(NetworkError.UNAUTHORIZED)
			409 -> Result.Error(NetworkError.CONFLICT)
			408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
			413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
			in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
			else -> Result.Error(NetworkError.UNKNOWN)
		}
	}
	
	suspend fun getSptRevMax(apiToken: String, year: String) : Result<Form1770HdResponseApiModel, NetworkError> {
		val response = try {
			httpClient.get (
				urlString = "${Variables.PertamaApiBaseUrl}api/SPTTahunanOP/GetSptRevMax"
			) {
				header("Authorization", apiToken)
				url {
					encodedParameters.append("taxYear", year)
				}
				contentType(ContentType.Application.Json)
			}
		} catch (e: UnresolvedAddressException) {
			return Result.Error(NetworkError.NO_INTERNET)
		} catch (e: SerializationException) {
			return Result.Error(NetworkError.SERIALIZATION)
		}
		
		return when (response.status.value) {
			200 -> {
				val responseBody = response.body<Form1770HdResponseApiModel>()
				Result.Success(responseBody)
			}
			401 -> Result.Error(NetworkError.UNAUTHORIZED)
			409 -> Result.Error(NetworkError.CONFLICT)
			408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
			413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
			in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
			else -> Result.Error(NetworkError.UNKNOWN)
		}
	}
	
	suspend fun getSptHdById(apiToken: String, entityId: Int) : Result<Form1770HdResponseApiModel, NetworkError> {
		val response = try {
			httpClient.get (
				urlString = "${Variables.PertamaApiBaseUrl}api/SPTTahunanOP/GetSptHd"
			) {
				header("Authorization", apiToken)
				url {
					encodedParameters.append("hdId", entityId.toString())
				}
				contentType(ContentType.Application.Json)
			}
		} catch (e: UnresolvedAddressException) {
			return Result.Error(NetworkError.NO_INTERNET)
		} catch (e: SerializationException) {
			return Result.Error(NetworkError.SERIALIZATION)
		}
		
		return when (response.status.value) {
			200 -> {
				val responseBody = response.body<Form1770HdResponseApiModel>()
				Result.Success(responseBody)
			}
			401 -> Result.Error(NetworkError.UNAUTHORIZED)
			409 -> Result.Error(NetworkError.CONFLICT)
			408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
			413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
			in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
			else -> Result.Error(NetworkError.UNKNOWN)
		}
	}
	
	suspend fun initInputForm1770(apiToken: String, request: Form1770HdRequestApiModel) : Result<PertamaGeneralApiResponse, NetworkError> {
		val response = try {
			httpClient.post  (
				urlString = "${Variables.PertamaApiBaseUrl}api/SPTTahunanOP/InitInputForm1770"
			) {
				header("Authorization", apiToken)
				setBody(request)
				contentType(ContentType.Application.Json)
			}
		} catch (e: UnresolvedAddressException) {
			return Result.Error(NetworkError.NO_INTERNET)
		} catch (e: SerializationException) {
			return Result.Error(NetworkError.SERIALIZATION)
		}
		
		return when (response.status.value) {
			200 -> {
				val body = response.body<PertamaGeneralApiResponse>()
				Result.Success(body)
			}
			401 -> Result.Error(NetworkError.UNAUTHORIZED)
			409 -> Result.Error(NetworkError.CONFLICT)
			408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
			413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
			in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
			else -> Result.Error(NetworkError.UNKNOWN)
		}
	}
	
	suspend fun getCityList(apiToken: String, query: HashMap<String, String>? = HashMap()) : Result<PagedListModel<CityModel>, NetworkError> {
		val response = try {
			httpClient.get (
				urlString = "${Variables.PertamaApiBaseUrl}api/SPTTahunanOP/GetCity"
			) {
				header("Authorization", apiToken)
				url {
					query?.forEach { (key, value) ->
						encodedParameters.append(key, value)
					}
				}
				contentType(ContentType.Application.Json)
			}
		} catch (e: UnresolvedAddressException) {
			return Result.Error(NetworkError.NO_INTERNET)
		} catch (e: SerializationException) {
			return Result.Error(NetworkError.SERIALIZATION)
		}
		
		return when (response.status.value) {
			200 -> {
				val responseBody = response.body<PagedListModel<CityModel>>()
				Result.Success(responseBody)
			}
			401 -> Result.Error(NetworkError.UNAUTHORIZED)
			409 -> Result.Error(NetworkError.CONFLICT)
			408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
			413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
			in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
			else -> Result.Error(NetworkError.UNKNOWN)
		}
	}
	
	suspend fun getJobList(apiToken: String, query: HashMap<String, String>? = HashMap()) : Result<PagedListModel<JobModel>, NetworkError> {
		val response = try {
			httpClient.get (
				urlString = "${Variables.PertamaApiBaseUrl}api/SPTTahunanOP/GetJob"
			) {
				header("Authorization", apiToken)
				url {
					query?.forEach { (key, value) ->
						encodedParameters.append(key, value)
					}
				}
				contentType(ContentType.Application.Json)
			}
		} catch (e: UnresolvedAddressException) {
			return Result.Error(NetworkError.NO_INTERNET)
		} catch (e: SerializationException) {
			return Result.Error(NetworkError.SERIALIZATION)
		}
		
		return when (response.status.value) {
			200 -> {
				val responseBody = response.body<PagedListModel<JobModel>>()
				Result.Success(responseBody)
			}
			401 -> Result.Error(NetworkError.UNAUTHORIZED)
			409 -> Result.Error(NetworkError.CONFLICT)
			408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
			413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
			in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
			else -> Result.Error(NetworkError.UNKNOWN)
		}
	}
	
	suspend fun getKluList(apiToken: String, query: HashMap<String, String>? = HashMap(), taxYear: Int = Clock.System.now().toLocalDateTime(
		TimeZone.currentSystemDefault()).year) : Result<PagedListModel<KluModel>, NetworkError> {
		val response = try {
			httpClient.get (
				urlString = "${Variables.PertamaApiBaseUrl}api/SPTTahunanOP/GetKLU"
			) {
				header("Authorization", apiToken)
				url {
					query?.forEach { (key, value) ->
						encodedParameters.append(key, value)
					}
				}
				url {
					encodedParameters.append("taxYear", taxYear.toString())
				}
				contentType(ContentType.Application.Json)
			}
		} catch (e: UnresolvedAddressException) {
			return Result.Error(NetworkError.NO_INTERNET)
		} catch (e: SerializationException) {
			return Result.Error(NetworkError.SERIALIZATION)
		}
		
		return when (response.status.value) {
			200 -> {
				val responseBody = response.body<PagedListModel<KluModel>>()
				Result.Success(responseBody)
			}
			401 -> Result.Error(NetworkError.UNAUTHORIZED)
			409 -> Result.Error(NetworkError.CONFLICT)
			408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
			413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
			in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
			else -> Result.Error(NetworkError.UNKNOWN)
		}
	}
	
	suspend fun saveIdentity(apiToken: String, request: FormIdentityRequestApiModel) : Result<PertamaGeneralApiResponse, NetworkError> {
		val response = try {
			httpClient.post  (
				urlString = "${Variables.PertamaApiBaseUrl}api/SPTTahunanOP/SaveIdentity"
			) {
				header("Authorization", apiToken)
				setBody(request)
				contentType(ContentType.Application.Json)
			}
		} catch (e: UnresolvedAddressException) {
			return Result.Error(NetworkError.NO_INTERNET)
		} catch (e: SerializationException) {
			return Result.Error(NetworkError.SERIALIZATION)
		}
		
		return when (response.status.value) {
			200 -> {
				val body = response.body<PertamaGeneralApiResponse>()
				Result.Success(body)
			}
			401 -> Result.Error(NetworkError.UNAUTHORIZED)
			409 -> Result.Error(NetworkError.CONFLICT)
			408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
			413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
			in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
			else -> Result.Error(NetworkError.UNKNOWN)
		}
	}
	
	suspend fun getIdentityById(apiToken: String, sptHd: Int): Result<FormIdentityResponseApiModel, NetworkError> {
		val response = try {
			httpClient.get(
				urlString = "${Variables.PertamaApiBaseUrl}api/SPTTahunanOP/GetIdentityData"
			) {
				header("Authorization", apiToken)
				url {
					encodedParameters.append("hdId", sptHd.toString())
				}
				contentType(ContentType.Application.Json)
			}
		} catch (e: UnresolvedAddressException) {
			return Result.Error(NetworkError.NO_INTERNET)
		} catch (e: SerializationException) {
			return Result.Error(NetworkError.SERIALIZATION)
		}
		
		return when (response.status.value) {
			200 -> {
				val body = response.body<FormIdentityResponseApiModel>()
				Result.Success(body)
			}
			401 -> Result.Error(NetworkError.UNAUTHORIZED)
			409 -> Result.Error(NetworkError.CONFLICT)
			408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
			413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
			in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
			else -> Result.Error(NetworkError.UNKNOWN)
		}
	}
	
	suspend fun getDependentList(apiToken: String, query: HashMap<String, String>? = HashMap(), sptId: Int) : Result<PagedListModel<FormDependentResponseApiModel>, NetworkError> {
		val response = try {
			httpClient.get (
				urlString = "${Variables.PertamaApiBaseUrl}api/SPTTahunanOP/GetDependentData"
			) {
				header("Authorization", apiToken)
				url {
					encodedParameters.append("hdId", sptId.toString())
				}
				url {
					query?.forEach { (key, value) ->
						encodedParameters.append(key, value)
					}
				}
				contentType(ContentType.Application.Json)
			}
		} catch (e: UnresolvedAddressException) {
			return Result.Error(NetworkError.NO_INTERNET)
		} catch (e: SerializationException) {
			return Result.Error(NetworkError.SERIALIZATION)
		}
		
		return when (response.status.value) {
			200 -> {
				val responseBody = response.body<PagedListModel<FormDependentResponseApiModel>>()
				Result.Success(responseBody)
			}
			401 -> Result.Error(NetworkError.UNAUTHORIZED)
			409 -> Result.Error(NetworkError.CONFLICT)
			408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
			413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
			in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
			else -> Result.Error(NetworkError.UNKNOWN)
		}
	}
	
	suspend fun getDependentById(apiToken: String, id: Int): Result<FormDependentResponseApiModel, NetworkError> {
		val response = try {
			httpClient.get(
				urlString = "${Variables.PertamaApiBaseUrl}api/SPTTahunanOP/GetDependentDataById"
			) {
				header("Authorization", apiToken)
				url {
					encodedParameters.append("id", id.toString())
				}
				contentType(ContentType.Application.Json)
			}
		} catch (e: UnresolvedAddressException) {
			return Result.Error(NetworkError.NO_INTERNET)
		} catch (e: SerializationException) {
			return Result.Error(NetworkError.SERIALIZATION)
		}
		
		return when (response.status.value) {
			200 -> {
				val body = response.body<FormDependentResponseApiModel>()
				Result.Success(body)
			}
			401 -> Result.Error(NetworkError.UNAUTHORIZED)
			409 -> Result.Error(NetworkError.CONFLICT)
			408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
			413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
			in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
			else -> Result.Error(NetworkError.UNKNOWN)
		}
	}
	
	suspend fun getFamilyRel(apiToken: String): Result<PagedListModel<FamilyRelModel>, NetworkError> {
		val response = try {
			httpClient.get(
				urlString = "${Variables.PertamaApiBaseUrl}api/SPTTahunanOP/GetFamilyRel"
			) {
				header("Authorization", apiToken)
				contentType(ContentType.Application.Json)
			}
		} catch (e: UnresolvedAddressException) {
			return Result.Error(NetworkError.NO_INTERNET)
		} catch (e: SerializationException) {
			return Result.Error(NetworkError.SERIALIZATION)
		}
		
		return when (response.status.value) {
			200 -> {
				val body = response.body<PagedListModel<FamilyRelModel>>()
				Result.Success(body)
			}
			401 -> Result.Error(NetworkError.UNAUTHORIZED)
			409 -> Result.Error(NetworkError.CONFLICT)
			408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
			413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
			in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
			else -> Result.Error(NetworkError.UNKNOWN)
		}
	}
	
	suspend fun saveDependent(apiToken: String, request: FormDependentRequestApiModel) : Result<PertamaGeneralApiResponse, NetworkError> {
		val response = try {
			httpClient.post  (
				urlString = "${Variables.PertamaApiBaseUrl}api/SPTTahunanOP/SaveDependent"
			) {
				header("Authorization", apiToken)
				setBody(request)
				contentType(ContentType.Application.Json)
			}
		} catch (e: UnresolvedAddressException) {
			return Result.Error(NetworkError.NO_INTERNET)
		} catch (e: SerializationException) {
			return Result.Error(NetworkError.SERIALIZATION)
		}
		
		return when (response.status.value) {
			200 -> {
				val body = response.body<PertamaGeneralApiResponse>()
				Result.Success(body)
			}
			401 -> Result.Error(NetworkError.UNAUTHORIZED)
			409 -> Result.Error(NetworkError.CONFLICT)
			408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
			413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
			in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
			else -> Result.Error(NetworkError.UNKNOWN)
		}
	}
	
	suspend fun deleteSpt(apiToken: String, sptId: Int): Result<PertamaGeneralApiResponse, NetworkError> {
		val response = try {
			httpClient.post  (
				urlString = "${Variables.PertamaApiBaseUrl}api/SPTTahunanOP/DeleteSPT"
			) {
				header("Authorization", apiToken)
				url {
					encodedParameters.append("key", sptId.toString())
				}
				contentType(ContentType.Application.Json)
			}
		} catch (e: UnresolvedAddressException) {
			return Result.Error(NetworkError.NO_INTERNET)
		} catch (e: SerializationException) {
			return Result.Error(NetworkError.SERIALIZATION)
		}
		
		return when (response.status.value) {
			200 -> {
				val body = response.body<PertamaGeneralApiResponse>()
				Result.Success(body)
			}
			401 -> Result.Error(NetworkError.UNAUTHORIZED)
			409 -> Result.Error(NetworkError.CONFLICT)
			408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
			413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
			in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
			else -> Result.Error(NetworkError.UNKNOWN)
		}
	}
	
	suspend fun deleteDependent(apiToken: String, sptId: Int): Result<PertamaGeneralApiResponse, NetworkError> {
		val response = try {
			httpClient.post  (
				urlString = "${Variables.PertamaApiBaseUrl}api/SPTTahunanOP/DeleteDependent"
			) {
				header("Authorization", apiToken)
				url {
					encodedParameters.append("key", sptId.toString())
				}
				contentType(ContentType.Application.Json)
			}
		} catch (e: UnresolvedAddressException) {
			return Result.Error(NetworkError.NO_INTERNET)
		} catch (e: SerializationException) {
			return Result.Error(NetworkError.SERIALIZATION)
		}
		
		return when (response.status.value) {
			200 -> {
				val body = response.body<PertamaGeneralApiResponse>()
				Result.Success(body)
			}
			401 -> Result.Error(NetworkError.UNAUTHORIZED)
			409 -> Result.Error(NetworkError.CONFLICT)
			408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
			413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
			in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
			else -> Result.Error(NetworkError.UNKNOWN)
		}
	}
	
	//Wealth
	suspend fun getWealthSummaryPerCode(apiToken: String, hdId: String) : Result<PagedListModel<AssetGroupResponseApiModel>, NetworkError> {
		val response = try {
			httpClient.get (
				urlString = "${Variables.PertamaApiBaseUrl}api/SPTTahunanOP/GetWealthSummaryPerCode"
			) {
				header("Authorization", apiToken)
				url {
					encodedParameters.append("hdId", hdId)
				}
				contentType(ContentType.Application.Json)
			}
		} catch (e: UnresolvedAddressException) {
			return Result.Error(NetworkError.NO_INTERNET)
		} catch (e: SerializationException) {
			return Result.Error(NetworkError.SERIALIZATION)
		}
		
		return when (response.status.value) {
			200 -> {
				val responseBody = response.body<PagedListModel<AssetGroupResponseApiModel>>()
				Result.Success(responseBody)
			}
			401 -> Result.Error(NetworkError.UNAUTHORIZED)
			409 -> Result.Error(NetworkError.CONFLICT)
			408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
			413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
			in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
			else -> Result.Error(NetworkError.UNKNOWN)
		}
	}
	
	suspend fun getWealthTotal(apiToken: String, hdId: String, wealthTypeId: String? = null): Result<Double, NetworkError> {
		val response = try {
			httpClient.get (
				urlString = "${Variables.PertamaApiBaseUrl}api/SPTTahunanOP/GetWealthTotal"
			) {
				header("Authorization", apiToken)
				url {
					encodedParameters.append("hdId", hdId)
				}
				if(!wealthTypeId.isNullOrBlank()) {
					url {encodedParameters.append("wealthTypeId", wealthTypeId)}
				}
				
				contentType(ContentType.Application.Json)
			}
		} catch (e: UnresolvedAddressException) {
			return Result.Error(NetworkError.NO_INTERNET)
		} catch (e: SerializationException) {
			return Result.Error(NetworkError.SERIALIZATION)
		}
		
		return when (response.status.value) {
			200 -> {
				val responseBody = response.body<Double>()
				Result.Success(responseBody)
			}
			401 -> Result.Error(NetworkError.UNAUTHORIZED)
			409 -> Result.Error(NetworkError.CONFLICT)
			408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
			413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
			in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
			else -> Result.Error(NetworkError.UNKNOWN)
		}
	}
	
	suspend fun getWealthData(apiToken: String, hdId: String, wealthTypeId: String? = null): Result<PagedListModel<FormWealthResponseApiModel>, NetworkError> {
		val response = try {
			httpClient.get (
				urlString = "${Variables.PertamaApiBaseUrl}api/SPTTahunanOP/GetWealthData"
			) {
				header("Authorization", apiToken)
				url {
					encodedParameters.append("hdId", hdId)
				}
				if(!wealthTypeId.isNullOrBlank()) {
					url {encodedParameters.append("wealthTypeId", wealthTypeId)}
				}
				
				contentType(ContentType.Application.Json)
			}
		} catch (e: UnresolvedAddressException) {
			return Result.Error(NetworkError.NO_INTERNET)
		} catch (e: SerializationException) {
			return Result.Error(NetworkError.SERIALIZATION)
		}
		
		return when (response.status.value) {
			200 -> {
				val responseBody = response.body<PagedListModel<FormWealthResponseApiModel>>()
				Result.Success(responseBody)
			}
			401 -> Result.Error(NetworkError.UNAUTHORIZED)
			409 -> Result.Error(NetworkError.CONFLICT)
			408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
			413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
			in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
			else -> Result.Error(NetworkError.UNKNOWN)
		}
	}
	
	suspend fun getWealthDataById(apiToken: String, id: String): Result<FormWealthResponseApiModel, NetworkError> {
		val response = try {
			httpClient.get (
				urlString = "${Variables.PertamaApiBaseUrl}api/SPTTahunanOP/GetWealthDataById"
			) {
				header("Authorization", apiToken)
				url {
					encodedParameters.append("id", id)
				}
				
				contentType(ContentType.Application.Json)
			}
		} catch (e: UnresolvedAddressException) {
			return Result.Error(NetworkError.NO_INTERNET)
		} catch (e: SerializationException) {
			return Result.Error(NetworkError.SERIALIZATION)
		}
		
		return when (response.status.value) {
			200 -> {
				val responseBody = response.body<FormWealthResponseApiModel>()
				Result.Success(responseBody)
			}
			401 -> Result.Error(NetworkError.UNAUTHORIZED)
			409 -> Result.Error(NetworkError.CONFLICT)
			408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
			413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
			in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
			else -> Result.Error(NetworkError.UNKNOWN)
		}
	}
	
	suspend fun getWealthType(apiToken: String): Result<PagedListModel<WealthTypeModel>, NetworkError> {
		val response = try {
			httpClient.get (
				urlString = "${Variables.PertamaApiBaseUrl}api/SPTTahunanOP/GetWealthType"
			) {
				header("Authorization", apiToken)
				
				contentType(ContentType.Application.Json)
			}
		} catch (e: UnresolvedAddressException) {
			return Result.Error(NetworkError.NO_INTERNET)
		} catch (e: SerializationException) {
			return Result.Error(NetworkError.SERIALIZATION)
		}
		
		return when (response.status.value) {
			200 -> {
				val responseBody = response.body<PagedListModel<WealthTypeModel>>()
				Result.Success(responseBody)
			}
			401 -> Result.Error(NetworkError.UNAUTHORIZED)
			409 -> Result.Error(NetworkError.CONFLICT)
			408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
			413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
			in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
			else -> Result.Error(NetworkError.UNKNOWN)
		}
	}
	
	suspend fun getCurrency(apiToken: String): Result<PagedListModel<CurrencyModel>, NetworkError> {
		val response = try {
			httpClient.get (
				urlString = "${Variables.PertamaApiBaseUrl}api/SPTTahunanOP/GetCurrency"
			) {
				header("Authorization", apiToken)
				
				contentType(ContentType.Application.Json)
			}
		} catch (e: UnresolvedAddressException) {
			return Result.Error(NetworkError.NO_INTERNET)
		} catch (e: SerializationException) {
			return Result.Error(NetworkError.SERIALIZATION)
		}
		
		return when (response.status.value) {
			200 -> {
				val responseBody = response.body<PagedListModel<CurrencyModel>>()
				Result.Success(responseBody)
			}
			401 -> Result.Error(NetworkError.UNAUTHORIZED)
			409 -> Result.Error(NetworkError.CONFLICT)
			408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
			413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
			in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
			else -> Result.Error(NetworkError.UNKNOWN)
		}
	}
	
	suspend fun deleteWealth(apiToken: String, id: String): Result<PertamaGeneralApiResponse, NetworkError> {
		val response = try {
			httpClient.post  (
				urlString = "${Variables.PertamaApiBaseUrl}api/SPTTahunanOP/DeleteWealth"
			) {
				header("Authorization", apiToken)
				url {
					encodedParameters.append("key", id)
				}
				contentType(ContentType.Application.Json)
			}
		} catch (e: UnresolvedAddressException) {
			return Result.Error(NetworkError.NO_INTERNET)
		} catch (e: SerializationException) {
			return Result.Error(NetworkError.SERIALIZATION)
		}
		
		return when (response.status.value) {
			200 -> {
				val body = response.body<PertamaGeneralApiResponse>()
				Result.Success(body)
			}
			401 -> Result.Error(NetworkError.UNAUTHORIZED)
			409 -> Result.Error(NetworkError.CONFLICT)
			408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
			413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
			in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
			else -> Result.Error(NetworkError.UNKNOWN)
		}
	}
	
	suspend fun getCurrencyRateByYear(apiToken: String, year: String): Result<PagedListModel<CurrencyRateModel>, NetworkError> {
		val response = try {
			httpClient.post  (
				urlString = "${Variables.PertamaApiBaseUrl}api/SPTTahunanOP/GetCurrencyRateByYear"
			) {
				header("Authorization", apiToken)
				url {
					encodedParameters.append("key", year)
				}
				contentType(ContentType.Application.Json)
			}
		} catch (e: UnresolvedAddressException) {
			return Result.Error(NetworkError.NO_INTERNET)
		} catch (e: SerializationException) {
			return Result.Error(NetworkError.SERIALIZATION)
		}
		
		return when (response.status.value) {
			200 -> {
				val body = response.body<PagedListModel<CurrencyRateModel>>()
				Result.Success(body)
			}
			401 -> Result.Error(NetworkError.UNAUTHORIZED)
			409 -> Result.Error(NetworkError.CONFLICT)
			408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
			413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
			in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
			else -> Result.Error(NetworkError.UNKNOWN)
		}
	}
	
	suspend fun getInterestType(apiToken: String): Result<PagedListModel<InterestTypeModel>, NetworkError> {
		val response = try {
			httpClient.get (
				urlString = "${Variables.PertamaApiBaseUrl}api/SPTTahunanOP/GetInterestType"
			) {
				header("Authorization", apiToken)
				
				contentType(ContentType.Application.Json)
			}
		} catch (e: UnresolvedAddressException) {
			return Result.Error(NetworkError.NO_INTERNET)
		} catch (e: SerializationException) {
			return Result.Error(NetworkError.SERIALIZATION)
		}
		
		return when (response.status.value) {
			200 -> {
				val responseBody = response.body<PagedListModel<InterestTypeModel>>()
				Result.Success(responseBody)
			}
			401 -> Result.Error(NetworkError.UNAUTHORIZED)
			409 -> Result.Error(NetworkError.CONFLICT)
			408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
			413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
			in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
			else -> Result.Error(NetworkError.UNKNOWN)
		}
	}
	
	suspend fun saveWealthA(apiToken: String, request: FormWealthARequestApiModel) : Result<PertamaGeneralApiResponse, NetworkError> {
		val response = try {
			httpClient.post  (
				urlString = "${Variables.PertamaApiBaseUrl}api/SPTTahunanOP/SaveWealthA"
			) {
				header("Authorization", apiToken)
				setBody(request)
				contentType(ContentType.Application.Json)
			}
		} catch (e: UnresolvedAddressException) {
			return Result.Error(NetworkError.NO_INTERNET)
		} catch (e: SerializationException) {
			return Result.Error(NetworkError.SERIALIZATION)
		}
		
		return when (response.status.value) {
			200 -> {
				val body = response.body<PertamaGeneralApiResponse>()
				println(body)
				Result.Success(body)
			}
			401 -> Result.Error(NetworkError.UNAUTHORIZED)
			409 -> Result.Error(NetworkError.CONFLICT)
			408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
			413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
			in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
			else -> Result.Error(NetworkError.UNKNOWN)
		}
	}
	
	suspend fun saveWealthB(apiToken: String, request: FormWealthBRequestApiModel) : Result<PertamaGeneralApiResponse, NetworkError> {
		val response = try {
			httpClient.post  (
				urlString = "${Variables.PertamaApiBaseUrl}api/SPTTahunanOP/SaveWealthB"
			) {
				header("Authorization", apiToken)
				setBody(request)
				contentType(ContentType.Application.Json)
			}
		} catch (e: UnresolvedAddressException) {
			return Result.Error(NetworkError.NO_INTERNET)
		} catch (e: SerializationException) {
			return Result.Error(NetworkError.SERIALIZATION)
		}
		
		return when (response.status.value) {
			200 -> {
				val body = response.body<PertamaGeneralApiResponse>()
				println(body)
				Result.Success(body)
			}
			401 -> Result.Error(NetworkError.UNAUTHORIZED)
			409 -> Result.Error(NetworkError.CONFLICT)
			408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
			413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
			in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
			else -> Result.Error(NetworkError.UNKNOWN)
		}
	}
	
	suspend fun saveWealthC(apiToken: String, request: FormWealthCRequestApiModel) : Result<PertamaGeneralApiResponse, NetworkError> {
		val response = try {
			httpClient.post  (
				urlString = "${Variables.PertamaApiBaseUrl}api/SPTTahunanOP/SaveWealthC"
			) {
				header("Authorization", apiToken)
				setBody(request)
				contentType(ContentType.Application.Json)
			}
		} catch (e: UnresolvedAddressException) {
			return Result.Error(NetworkError.NO_INTERNET)
		} catch (e: SerializationException) {
			return Result.Error(NetworkError.SERIALIZATION)
		}
		
		return when (response.status.value) {
			200 -> {
				val body = response.body<PertamaGeneralApiResponse>()
				println(body)
				Result.Success(body)
			}
			401 -> Result.Error(NetworkError.UNAUTHORIZED)
			409 -> Result.Error(NetworkError.CONFLICT)
			408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
			413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
			in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
			else -> Result.Error(NetworkError.UNKNOWN)
		}
	}
	
	suspend fun saveWealthD(apiToken: String, request: FormWealthDRequestApiModel) : Result<PertamaGeneralApiResponse, NetworkError> {
		val response = try {
			httpClient.post  (
				urlString = "${Variables.PertamaApiBaseUrl}api/SPTTahunanOP/SaveWealthD"
			) {
				header("Authorization", apiToken)
				setBody(request)
				contentType(ContentType.Application.Json)
			}
		} catch (e: UnresolvedAddressException) {
			return Result.Error(NetworkError.NO_INTERNET)
		} catch (e: SerializationException) {
			return Result.Error(NetworkError.SERIALIZATION)
		}
		
		return when (response.status.value) {
			200 -> {
				val body = response.body<PertamaGeneralApiResponse>()
				println(body)
				Result.Success(body)
			}
			401 -> Result.Error(NetworkError.UNAUTHORIZED)
			409 -> Result.Error(NetworkError.CONFLICT)
			408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
			413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
			in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
			else -> Result.Error(NetworkError.UNKNOWN)
		}
	}
	
	suspend fun saveWealthE(apiToken: String, request: FormWealthERequestApiModel) : Result<PertamaGeneralApiResponse, NetworkError> {
		val response = try {
			httpClient.post  (
				urlString = "${Variables.PertamaApiBaseUrl}api/SPTTahunanOP/SaveWealthE"
			) {
				header("Authorization", apiToken)
				setBody(request)
				contentType(ContentType.Application.Json)
			}
		} catch (e: UnresolvedAddressException) {
			return Result.Error(NetworkError.NO_INTERNET)
		} catch (e: SerializationException) {
			return Result.Error(NetworkError.SERIALIZATION)
		}
		
		return when (response.status.value) {
			200 -> {
				val body = response.body<PertamaGeneralApiResponse>()
				println(body)
				Result.Success(body)
			}
			401 -> Result.Error(NetworkError.UNAUTHORIZED)
			409 -> Result.Error(NetworkError.CONFLICT)
			408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
			413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
			in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
			else -> Result.Error(NetworkError.UNKNOWN)
		}
	}
	
	suspend fun saveWealthF(apiToken: String, request: FormWealthFRequestApiModel) : Result<PertamaGeneralApiResponse, NetworkError> {
		val response = try {
			httpClient.post  (
				urlString = "${Variables.PertamaApiBaseUrl}api/SPTTahunanOP/SaveWealthF"
			) {
				header("Authorization", apiToken)
				setBody(request)
				contentType(ContentType.Application.Json)
			}
		} catch (e: UnresolvedAddressException) {
			return Result.Error(NetworkError.NO_INTERNET)
		} catch (e: SerializationException) {
			return Result.Error(NetworkError.SERIALIZATION)
		}
		
		return when (response.status.value) {
			200 -> {
				val body = response.body<PertamaGeneralApiResponse>()
				println(body)
				Result.Success(body)
			}
			401 -> Result.Error(NetworkError.UNAUTHORIZED)
			409 -> Result.Error(NetworkError.CONFLICT)
			408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
			413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
			in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
			else -> Result.Error(NetworkError.UNKNOWN)
		}
	}
	
	suspend fun saveWealthG(apiToken: String, request: FormWealthGRequestApiModel) : Result<PertamaGeneralApiResponse, NetworkError> {
		val response = try {
			httpClient.post  (
				urlString = "${Variables.PertamaApiBaseUrl}api/SPTTahunanOP/SaveWealthG"
			) {
				header("Authorization", apiToken)
				setBody(request)
				contentType(ContentType.Application.Json)
			}
		} catch (e: UnresolvedAddressException) {
			return Result.Error(NetworkError.NO_INTERNET)
		} catch (e: SerializationException) {
			return Result.Error(NetworkError.SERIALIZATION)
		}
		
		return when (response.status.value) {
			200 -> {
				val body = response.body<PertamaGeneralApiResponse>()
				println(body)
				Result.Success(body)
			}
			401 -> Result.Error(NetworkError.UNAUTHORIZED)
			409 -> Result.Error(NetworkError.CONFLICT)
			408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
			413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
			in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
			else -> Result.Error(NetworkError.UNKNOWN)
		}
	}
	
	suspend fun saveWealthH(apiToken: String, request: FormWealthHRequestApiModel) : Result<PertamaGeneralApiResponse, NetworkError> {
		val response = try {
			httpClient.post  (
				urlString = "${Variables.PertamaApiBaseUrl}api/SPTTahunanOP/SaveWealthH"
			) {
				header("Authorization", apiToken)
				setBody(request)
				contentType(ContentType.Application.Json)
			}
		} catch (e: UnresolvedAddressException) {
			return Result.Error(NetworkError.NO_INTERNET)
		} catch (e: SerializationException) {
			return Result.Error(NetworkError.SERIALIZATION)
		}
		
		return when (response.status.value) {
			200 -> {
				val body = response.body<PertamaGeneralApiResponse>()
				println(body)
				Result.Success(body)
			}
			401 -> Result.Error(NetworkError.UNAUTHORIZED)
			409 -> Result.Error(NetworkError.CONFLICT)
			408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
			413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
			in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
			else -> Result.Error(NetworkError.UNKNOWN)
		}
	}
	
	suspend fun saveWealthI(apiToken: String, request: FormWealthIRequestApiModel) : Result<PertamaGeneralApiResponse, NetworkError> {
		val response = try {
			httpClient.post  (
				urlString = "${Variables.PertamaApiBaseUrl}api/SPTTahunanOP/SaveWealthI"
			) {
				header("Authorization", apiToken)
				setBody(request)
				contentType(ContentType.Application.Json)
			}
		} catch (e: UnresolvedAddressException) {
			return Result.Error(NetworkError.NO_INTERNET)
		} catch (e: SerializationException) {
			return Result.Error(NetworkError.SERIALIZATION)
		}
		
		return when (response.status.value) {
			200 -> {
				val body = response.body<PertamaGeneralApiResponse>()
				println(body)
				Result.Success(body)
			}
			401 -> Result.Error(NetworkError.UNAUTHORIZED)
			409 -> Result.Error(NetworkError.CONFLICT)
			408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
			413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
			in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
			else -> Result.Error(NetworkError.UNKNOWN)
		}
	}
	
	suspend fun saveWealthJ(apiToken: String, request: FormWealthJRequestApiModel) : Result<PertamaGeneralApiResponse, NetworkError> {
		val response = try {
			httpClient.post  (
				urlString = "${Variables.PertamaApiBaseUrl}api/SPTTahunanOP/SaveWealthJ"
			) {
				header("Authorization", apiToken)
				setBody(request)
				contentType(ContentType.Application.Json)
			}
		} catch (e: UnresolvedAddressException) {
			return Result.Error(NetworkError.NO_INTERNET)
		} catch (e: SerializationException) {
			return Result.Error(NetworkError.SERIALIZATION)
		}
		
		return when (response.status.value) {
			200 -> {
				val body = response.body<PertamaGeneralApiResponse>()
				println(body)
				Result.Success(body)
			}
			401 -> Result.Error(NetworkError.UNAUTHORIZED)
			409 -> Result.Error(NetworkError.CONFLICT)
			408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
			413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
			in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
			else -> Result.Error(NetworkError.UNKNOWN)
		}
	}
	
	suspend fun saveWealthK(apiToken: String, request: FormWealthKRequestApiModel) : Result<PertamaGeneralApiResponse, NetworkError> {
		val response = try {
			httpClient.post  (
				urlString = "${Variables.PertamaApiBaseUrl}api/SPTTahunanOP/SaveWealthK"
			) {
				header("Authorization", apiToken)
				setBody(request)
				contentType(ContentType.Application.Json)
			}
		} catch (e: UnresolvedAddressException) {
			return Result.Error(NetworkError.NO_INTERNET)
		} catch (e: SerializationException) {
			return Result.Error(NetworkError.SERIALIZATION)
		}
		
		return when (response.status.value) {
			200 -> {
				val body = response.body<PertamaGeneralApiResponse>()
				println(body)
				Result.Success(body)
			}
			401 -> Result.Error(NetworkError.UNAUTHORIZED)
			409 -> Result.Error(NetworkError.CONFLICT)
			408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
			413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
			in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
			else -> Result.Error(NetworkError.UNKNOWN)
		}
	}
	
	suspend fun saveWealthL(apiToken: String, request: FormWealthLRequestApiModel) : Result<PertamaGeneralApiResponse, NetworkError> {
		val response = try {
			httpClient.post  (
				urlString = "${Variables.PertamaApiBaseUrl}api/SPTTahunanOP/SaveWealthL"
			) {
				header("Authorization", apiToken)
				setBody(request)
				contentType(ContentType.Application.Json)
			}
		} catch (e: UnresolvedAddressException) {
			return Result.Error(NetworkError.NO_INTERNET)
		} catch (e: SerializationException) {
			return Result.Error(NetworkError.SERIALIZATION)
		}
		
		return when (response.status.value) {
			200 -> {
				val body = response.body<PertamaGeneralApiResponse>()
				println(body)
				Result.Success(body)
			}
			401 -> Result.Error(NetworkError.UNAUTHORIZED)
			409 -> Result.Error(NetworkError.CONFLICT)
			408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
			413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
			in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
			else -> Result.Error(NetworkError.UNKNOWN)
		}
	}
	
	//Debt
	suspend fun getDebtTotal(apiToken: String, hdId: String): Result<Double, NetworkError> {
		val response = try {
			httpClient.get (
				urlString = "${Variables.PertamaApiBaseUrl}api/SPTTahunanOP/GetDebtTotal"
			) {
				header("Authorization", apiToken)
				url {
					encodedParameters.append("hdId", hdId)
				}
				contentType(ContentType.Application.Json)
			}
		} catch (e: UnresolvedAddressException) {
			return Result.Error(NetworkError.NO_INTERNET)
		} catch (e: SerializationException) {
			return Result.Error(NetworkError.SERIALIZATION)
		}
		
		return when (response.status.value) {
			200 -> {
				val responseBody = response.body<Double>()
				Result.Success(responseBody)
			}
			401 -> Result.Error(NetworkError.UNAUTHORIZED)
			409 -> Result.Error(NetworkError.CONFLICT)
			408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
			413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
			in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
			else -> Result.Error(NetworkError.UNKNOWN)
		}
	}
	
	suspend fun getDebtData(apiToken: String, hdId: String, debtTypeId: String? = null): Result<PagedListModel<FormDebtResponseApiModel>, NetworkError> {
		val response = try {
			httpClient.get (
				urlString = "${Variables.PertamaApiBaseUrl}api/SPTTahunanOP/GetDebtData"
			) {
				header("Authorization", apiToken)
				url {
					encodedParameters.append("hdId", hdId)
				}
				if(!debtTypeId.isNullOrBlank()) {
					url {encodedParameters.append("debtTypeId", debtTypeId)}
				}
				
				contentType(ContentType.Application.Json)
			}
		} catch (e: UnresolvedAddressException) {
			return Result.Error(NetworkError.NO_INTERNET)
		} catch (e: SerializationException) {
			return Result.Error(NetworkError.SERIALIZATION)
		}
		
		return when (response.status.value) {
			200 -> {
				val responseBody = response.body<PagedListModel<FormDebtResponseApiModel>>()
				Result.Success(responseBody)
			}
			401 -> Result.Error(NetworkError.UNAUTHORIZED)
			409 -> Result.Error(NetworkError.CONFLICT)
			408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
			413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
			in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
			else -> Result.Error(NetworkError.UNKNOWN)
		}
	}
	
	suspend fun getDebtDataById(apiToken: String, id: String): Result<FormDebtResponseApiModel, NetworkError> {
		val response = try {
			httpClient.get (
				urlString = "${Variables.PertamaApiBaseUrl}api/SPTTahunanOP/GetDebtDataById"
			) {
				header("Authorization", apiToken)
				url {
					encodedParameters.append("id", id)
				}
				
				contentType(ContentType.Application.Json)
			}
		} catch (e: UnresolvedAddressException) {
			return Result.Error(NetworkError.NO_INTERNET)
		} catch (e: SerializationException) {
			return Result.Error(NetworkError.SERIALIZATION)
		}
		
		return when (response.status.value) {
			200 -> {
				val responseBody = response.body<FormDebtResponseApiModel>()
				Result.Success(responseBody)
			}
			401 -> Result.Error(NetworkError.UNAUTHORIZED)
			409 -> Result.Error(NetworkError.CONFLICT)
			408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
			413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
			in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
			else -> Result.Error(NetworkError.UNKNOWN)
		}
	}
	
	suspend fun getDebtType(apiToken: String): Result<PagedListModel<DebtTypeModel>, NetworkError> {
		val response = try {
			httpClient.get (
				urlString = "${Variables.PertamaApiBaseUrl}api/SPTTahunanOP/GetDebtType"
			) {
				header("Authorization", apiToken)
				
				contentType(ContentType.Application.Json)
			}
		} catch (e: UnresolvedAddressException) {
			return Result.Error(NetworkError.NO_INTERNET)
		} catch (e: SerializationException) {
			return Result.Error(NetworkError.SERIALIZATION)
		}
		
		return when (response.status.value) {
			200 -> {
				val responseBody = response.body<PagedListModel<DebtTypeModel>>()
				Result.Success(responseBody)
			}
			401 -> Result.Error(NetworkError.UNAUTHORIZED)
			409 -> Result.Error(NetworkError.CONFLICT)
			408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
			413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
			in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
			else -> Result.Error(NetworkError.UNKNOWN)
		}
	}
	
	suspend fun deleteDebt(apiToken: String, id: String): Result<PertamaGeneralApiResponse, NetworkError> {
		val response = try {
			httpClient.post  (
				urlString = "${Variables.PertamaApiBaseUrl}api/SPTTahunanOP/DeleteDebt"
			) {
				header("Authorization", apiToken)
				url {
					encodedParameters.append("key", id)
				}
				contentType(ContentType.Application.Json)
			}
		} catch (e: UnresolvedAddressException) {
			return Result.Error(NetworkError.NO_INTERNET)
		} catch (e: SerializationException) {
			return Result.Error(NetworkError.SERIALIZATION)
		}
		
		return when (response.status.value) {
			200 -> {
				val body = response.body<PertamaGeneralApiResponse>()
				Result.Success(body)
			}
			401 -> Result.Error(NetworkError.UNAUTHORIZED)
			409 -> Result.Error(NetworkError.CONFLICT)
			408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
			413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
			in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
			else -> Result.Error(NetworkError.UNKNOWN)
		}
	}
	
	suspend fun saveDebt(apiToken: String, request: FormDebtRequestApiModel) : Result<PertamaGeneralApiResponse, NetworkError> {
		val response = try {
			httpClient.post  (
				urlString = "${Variables.PertamaApiBaseUrl}api/SPTTahunanOP/SaveDebt"
			) {
				header("Authorization", apiToken)
				setBody(request)
				contentType(ContentType.Application.Json)
			}
		} catch (e: UnresolvedAddressException) {
			return Result.Error(NetworkError.NO_INTERNET)
		} catch (e: SerializationException) {
			return Result.Error(NetworkError.SERIALIZATION)
		}
		
		return when (response.status.value) {
			200 -> {
				val body = response.body<PertamaGeneralApiResponse>()
				println(body)
				Result.Success(body)
			}
			401 -> Result.Error(NetworkError.UNAUTHORIZED)
			409 -> Result.Error(NetworkError.CONFLICT)
			408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
			413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
			in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
			else -> Result.Error(NetworkError.UNKNOWN)
		}
	}
	
	//Final Income
	suspend fun getSPTIncomeSummary(apiToken: String, hdId: String, incomeGroupE: String, incomeTypeE: String): Result<SPTSummaryResponseApiModel, NetworkError> {
		val response = try {
			httpClient.get (
				urlString = "${Variables.PertamaApiBaseUrl}api/SPTTahunanOP/GetSPTIncomeSummary"
			) {
				header("Authorization", apiToken)
				url {
					encodedParameters.append("hdId", hdId)
					encodedParameters.append("incomeGroupE", incomeGroupE)
					encodedParameters.append("incomeTypeE", incomeTypeE)
				}
				
				contentType(ContentType.Application.Json)
			}
		} catch (e: UnresolvedAddressException) {
			return Result.Error(NetworkError.NO_INTERNET)
		} catch (e: SerializationException) {
			return Result.Error(NetworkError.SERIALIZATION)
		}
		
		return when (response.status.value) {
			200 -> {
				val responseBody = response.body<SPTSummaryResponseApiModel>()
				Result.Success(responseBody)
			}
			401 -> Result.Error(NetworkError.UNAUTHORIZED)
			409 -> Result.Error(NetworkError.CONFLICT)
			408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
			413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
			in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
			else -> Result.Error(NetworkError.UNKNOWN)
		}
	}
	
	suspend fun getFinalIncomeData(apiToken: String, hdId: String, incomeTypeE: String? = null): Result<PagedListModel<FormFinalIncomeResponseApiModel>, NetworkError> {
		val response = try {
			httpClient.get (
				urlString = "${Variables.PertamaApiBaseUrl}api/SPTTahunanOP/GetFinalIncomeData"
			) {
				header("Authorization", apiToken)
				url {
					encodedParameters.append("hdId", hdId)
				}
				if(!incomeTypeE.isNullOrBlank()) {
					url {encodedParameters.append("incomeTypeE", incomeTypeE)}
				}
				
				contentType(ContentType.Application.Json)
			}
		} catch (e: UnresolvedAddressException) {
			return Result.Error(NetworkError.NO_INTERNET)
		} catch (e: SerializationException) {
			return Result.Error(NetworkError.SERIALIZATION)
		}
		
		return when (response.status.value) {
			200 -> {
				val responseBody = response.body<PagedListModel<FormFinalIncomeResponseApiModel>>()
				Result.Success(responseBody)
			}
			401 -> Result.Error(NetworkError.UNAUTHORIZED)
			409 -> Result.Error(NetworkError.CONFLICT)
			408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
			413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
			in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
			else -> Result.Error(NetworkError.UNKNOWN)
		}
	}
	
	suspend fun getFinalIncomeDataById(apiToken: String, id: String): Result<FormFinalIncomeResponseApiModel, NetworkError> {
		val response = try {
			httpClient.get (
				urlString = "${Variables.PertamaApiBaseUrl}api/SPTTahunanOP/GetFinalIncomeDataById"
			) {
				header("Authorization", apiToken)
				url {
					encodedParameters.append("id", id)
				}
				
				contentType(ContentType.Application.Json)
			}
		} catch (e: UnresolvedAddressException) {
			return Result.Error(NetworkError.NO_INTERNET)
		} catch (e: SerializationException) {
			return Result.Error(NetworkError.SERIALIZATION)
		}
		
		return when (response.status.value) {
			200 -> {
				val responseBody = response.body<FormFinalIncomeResponseApiModel>()
				Result.Success(responseBody)
			}
			401 -> Result.Error(NetworkError.UNAUTHORIZED)
			409 -> Result.Error(NetworkError.CONFLICT)
			408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
			413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
			in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
			else -> Result.Error(NetworkError.UNKNOWN)
		}
	}
	
	suspend fun deleteIncome(apiToken: String, id: String): Result<PertamaGeneralApiResponse, NetworkError> {
		val response = try {
			httpClient.post  (
				urlString = "${Variables.PertamaApiBaseUrl}api/SPTTahunanOP/DeleteIncome"
			) {
				header("Authorization", apiToken)
				url {
					encodedParameters.append("key", id)
				}
				contentType(ContentType.Application.Json)
			}
		} catch (e: UnresolvedAddressException) {
			return Result.Error(NetworkError.NO_INTERNET)
		} catch (e: SerializationException) {
			return Result.Error(NetworkError.SERIALIZATION)
		}
		
		return when (response.status.value) {
			200 -> {
				val body = response.body<PertamaGeneralApiResponse>()
				Result.Success(body)
			}
			401 -> Result.Error(NetworkError.UNAUTHORIZED)
			409 -> Result.Error(NetworkError.CONFLICT)
			408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
			413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
			in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
			else -> Result.Error(NetworkError.UNKNOWN)
		}
	}
	
	suspend fun saveFinalIncomeA(apiToken: String, request: FormFinalIncomeARequestApiModel) : Result<PertamaGeneralApiResponse, NetworkError> {
		val response = try {
			httpClient.post  (
				urlString = "${Variables.PertamaApiBaseUrl}api/SPTTahunanOP/SaveFinalIncomeA"
			) {
				header("Authorization", apiToken)
				setBody(request)
				contentType(ContentType.Application.Json)
			}
		} catch (e: UnresolvedAddressException) {
			return Result.Error(NetworkError.NO_INTERNET)
		} catch (e: SerializationException) {
			return Result.Error(NetworkError.SERIALIZATION)
		}
		
		return when (response.status.value) {
			200 -> {
				val body = response.body<PertamaGeneralApiResponse>()
				println(body)
				Result.Success(body)
			}
			401 -> Result.Error(NetworkError.UNAUTHORIZED)
			409 -> Result.Error(NetworkError.CONFLICT)
			408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
			413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
			in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
			else -> Result.Error(NetworkError.UNKNOWN)
		}
	}
	
	suspend fun saveFinalIncomeB(apiToken: String, request: FormFinalIncomeBRequestApiModel) : Result<PertamaGeneralApiResponse, NetworkError> {
		val response = try {
			httpClient.post  (
				urlString = "${Variables.PertamaApiBaseUrl}api/SPTTahunanOP/SaveFinalIncomeB"
			) {
				header("Authorization", apiToken)
				setBody(request)
				contentType(ContentType.Application.Json)
			}
		} catch (e: UnresolvedAddressException) {
			return Result.Error(NetworkError.NO_INTERNET)
		} catch (e: SerializationException) {
			return Result.Error(NetworkError.SERIALIZATION)
		}
		
		return when (response.status.value) {
			200 -> {
				val body = response.body<PertamaGeneralApiResponse>()
				println(body)
				Result.Success(body)
			}
			401 -> Result.Error(NetworkError.UNAUTHORIZED)
			409 -> Result.Error(NetworkError.CONFLICT)
			408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
			413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
			in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
			else -> Result.Error(NetworkError.UNKNOWN)
		}
	}
	
	suspend fun saveFinalIncomeC(apiToken: String, request: FormFinalIncomeCRequestApiModel) : Result<PertamaGeneralApiResponse, NetworkError> {
		val response = try {
			httpClient.post  (
				urlString = "${Variables.PertamaApiBaseUrl}api/SPTTahunanOP/SaveFinalIncomeC"
			) {
				header("Authorization", apiToken)
				setBody(request)
				contentType(ContentType.Application.Json)
			}
		} catch (e: UnresolvedAddressException) {
			return Result.Error(NetworkError.NO_INTERNET)
		} catch (e: SerializationException) {
			return Result.Error(NetworkError.SERIALIZATION)
		}
		
		return when (response.status.value) {
			200 -> {
				val body = response.body<PertamaGeneralApiResponse>()
				println(body)
				Result.Success(body)
			}
			401 -> Result.Error(NetworkError.UNAUTHORIZED)
			409 -> Result.Error(NetworkError.CONFLICT)
			408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
			413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
			in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
			else -> Result.Error(NetworkError.UNKNOWN)
		}
	}
	
	suspend fun saveFinalIncomeD(apiToken: String, request: FormFinalIncomeDRequestApiModel) : Result<PertamaGeneralApiResponse, NetworkError> {
		val response = try {
			httpClient.post  (
				urlString = "${Variables.PertamaApiBaseUrl}api/SPTTahunanOP/SaveFinalIncomeD"
			) {
				header("Authorization", apiToken)
				setBody(request)
				contentType(ContentType.Application.Json)
			}
		} catch (e: UnresolvedAddressException) {
			return Result.Error(NetworkError.NO_INTERNET)
		} catch (e: SerializationException) {
			return Result.Error(NetworkError.SERIALIZATION)
		}
		
		return when (response.status.value) {
			200 -> {
				val body = response.body<PertamaGeneralApiResponse>()
				println(body)
				Result.Success(body)
			}
			401 -> Result.Error(NetworkError.UNAUTHORIZED)
			409 -> Result.Error(NetworkError.CONFLICT)
			408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
			413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
			in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
			else -> Result.Error(NetworkError.UNKNOWN)
		}
	}
	
	suspend fun saveFinalIncomeE(apiToken: String, request: FormFinalIncomeERequestApiModel) : Result<PertamaGeneralApiResponse, NetworkError> {
		val response = try {
			httpClient.post  (
				urlString = "${Variables.PertamaApiBaseUrl}api/SPTTahunanOP/SaveFinalIncomeE"
			) {
				header("Authorization", apiToken)
				setBody(request)
				contentType(ContentType.Application.Json)
			}
		} catch (e: UnresolvedAddressException) {
			return Result.Error(NetworkError.NO_INTERNET)
		} catch (e: SerializationException) {
			return Result.Error(NetworkError.SERIALIZATION)
		}
		
		return when (response.status.value) {
			200 -> {
				val body = response.body<PertamaGeneralApiResponse>()
				println(body)
				Result.Success(body)
			}
			401 -> Result.Error(NetworkError.UNAUTHORIZED)
			409 -> Result.Error(NetworkError.CONFLICT)
			408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
			413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
			in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
			else -> Result.Error(NetworkError.UNKNOWN)
		}
	}
	
	//UMKM
	suspend fun saveFinalIncomeUmkm2023Business(apiToken: String, request: Form1770FinalIncomeUmkm2023BusinessRequestModel) : Result<PertamaGeneralApiResponse, NetworkError> {
		val response = try {
			httpClient.post  (
				urlString = "${Variables.PertamaApiBaseUrl}api/SPTTahunanOP/SaveFinalIncomeUmkm2023Business"
			) {
				header("Authorization", apiToken)
				setBody(request)
				contentType(ContentType.Application.Json)
			}
		} catch (e: UnresolvedAddressException) {
			return Result.Error(NetworkError.NO_INTERNET)
		} catch (e: SerializationException) {
			return Result.Error(NetworkError.SERIALIZATION)
		}
		
		return when (response.status.value) {
			200 -> {
				val body = response.body<PertamaGeneralApiResponse>()
				println(body)
				Result.Success(body)
			}
			401 -> Result.Error(NetworkError.UNAUTHORIZED)
			409 -> Result.Error(NetworkError.CONFLICT)
			408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
			413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
			in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
			else -> Result.Error(NetworkError.UNKNOWN)
		}
	}
	
	suspend fun getFinalIncomeUMKMByHdId(apiToken: String, id: String): Result<Form1770FinalIncomeUmkm2023ResponseApiModel, NetworkError> {
		val response = try {
			httpClient.get (
				urlString = "${Variables.PertamaApiBaseUrl}api/SPTTahunanOP/GetFinalIncomeUMKMByHdId"
			) {
				header("Authorization", apiToken)
				url {
					encodedParameters.append("HdId", id)
				}
				
				contentType(ContentType.Application.Json)
			}
		} catch (e: UnresolvedAddressException) {
			return Result.Error(NetworkError.NO_INTERNET)
		} catch (e: SerializationException) {
			return Result.Error(NetworkError.SERIALIZATION)
		}
		
		return when (response.status.value) {
			200 -> {
				val responseBody = response.body<Form1770FinalIncomeUmkm2023ResponseApiModel>()
				Result.Success(responseBody)
			}
			401 -> Result.Error(NetworkError.UNAUTHORIZED)
			409 -> Result.Error(NetworkError.CONFLICT)
			408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
			413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
			in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
			else -> Result.Error(NetworkError.UNKNOWN)
		}
	}
	
	suspend fun saveFinalIncomeUmkm2023Summary(apiToken: String, request: Form1770FinalIncomeUmkm2023SummaryRequestModel) : Result<PertamaGeneralApiResponse, NetworkError> {
		val response = try {
			httpClient.post  (
				urlString = "${Variables.PertamaApiBaseUrl}api/SPTTahunanOP/SaveFinalIncomeUmkm2023Summary"
			) {
				header("Authorization", apiToken)
				setBody(request)
				contentType(ContentType.Application.Json)
			}
		} catch (e: UnresolvedAddressException) {
			return Result.Error(NetworkError.NO_INTERNET)
		} catch (e: SerializationException) {
			return Result.Error(NetworkError.SERIALIZATION)
		}
		
		return when (response.status.value) {
			200 -> {
				val body = response.body<PertamaGeneralApiResponse>()
				println(body)
				Result.Success(body)
			}
			401 -> Result.Error(NetworkError.UNAUTHORIZED)
			409 -> Result.Error(NetworkError.CONFLICT)
			408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
			413 -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
			in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
			else -> Result.Error(NetworkError.UNKNOWN)
		}
	}
}