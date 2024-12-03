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
import kotlinx.serialization.SerializationException
import models.PagedListModel
import models.account.APITokenModel
import models.transaction.Form1770HdResponseApiModel
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
}