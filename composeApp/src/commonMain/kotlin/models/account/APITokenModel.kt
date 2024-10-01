package models.account

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDateTime

@Serializable
data class APITokenModel (
    @SerialName("access_token")val AccessToken: String,
    @SerialName("token_type")val TokenType: String,
    @SerialName("expires_in")val ExpiresIn: Long,
    @SerialName(".issued")val Issued: String,
    @SerialName(".expires")val Expires: String
)

@Serializable
data class ApiAuthenticationModel (
    @SerialName("ApiKey")val ApiKey: String,
    @SerialName("ApiSecret")val ApiSecret: String
)