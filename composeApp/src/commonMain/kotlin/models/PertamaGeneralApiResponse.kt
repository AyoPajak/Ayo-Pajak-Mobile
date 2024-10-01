package models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PertamaGeneralApiResponse (
    @SerialName("ErrorCode") val ErrorCode: Int,
    @SerialName("Message") val Message: String?
)
