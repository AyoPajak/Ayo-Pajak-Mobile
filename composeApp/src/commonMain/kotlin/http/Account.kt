package http

import global.Variables
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import models.LoginRequest
import models.PertamaGeneralApiResponse
import models.ReturnStatus
import models.account.APITokenModel
import models.account.ApiAuthenticationModel
import models.account.ClientProfileResponseApiModel
import util.NetworkError
import util.Result

class Account (private val httpClient: HttpClient) {
    suspend fun Login (request: LoginRequest, apiToken: String): Result<PertamaGeneralApiResponse, NetworkError> {
        val response = try {
            httpClient.post  (
                urlString = "${Variables.AyoPajakApiBaseUrl}api/account/Client/VerifyLogin"
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
    
    suspend fun GetAuthentication (authorization: String, email: String): Result<PertamaGeneralApiResponse, NetworkError> {
        val response = try {
            httpClient.get(
                urlString = "${Variables.AyoPajakApiBaseUrl}api/account/Client/GetAuthentication"
            ) {
                header("Authorization", authorization)
                url {
                    encodedParameters.append("email", email)
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
                val responseBody = response.body<PertamaGeneralApiResponse>()
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

    suspend fun ApiToken (userKey: String, userSecret: String, body: String): Result<APITokenModel, NetworkError> {
        val response = try {
            httpClient.post(
                urlString = "${Variables.AyoPajakApiBaseUrl}Token"
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
    
    suspend fun getUserProfile(apiToken: String) : Result<ClientProfileResponseApiModel, NetworkError> {
        val response = try {
            httpClient.get (
                urlString = "${Variables.AyoPajakApiBaseUrl}api/account/ClientProfile"
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
                val responseBody = response.body<ClientProfileResponseApiModel>()
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