package models

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val Email: String,
    val Password: String
)