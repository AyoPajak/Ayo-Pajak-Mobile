package http

import global.Variables
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.parametersOf
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.SerializationException
import models.PagedListModel
import models.PertamaGeneralApiResponse
import models.account.APITokenModel
import models.master.CityModel
import models.master.JobModel
import models.master.KluModel
import models.transaction.Form1770HdRequestApiModel
import models.transaction.Form1770HdResponseApiModel
import models.transaction.FormIdentityRequestApiModel
import models.transaction.FormIdentityResponseApiModel
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
}