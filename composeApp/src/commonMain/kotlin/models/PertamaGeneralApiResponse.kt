package models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.modules.SerializersModule
import models.account.ApiAuthenticationModel

@Serializable
data class PertamaGeneralApiResponse(
    @SerialName("ErrorCode") val ErrorCode: Int,
    @SerialName("Message") val Message: String?,
    @SerialName("Data") val Data: JsonElement?
)