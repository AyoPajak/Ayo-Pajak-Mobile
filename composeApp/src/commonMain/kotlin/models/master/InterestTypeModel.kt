package models.master

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InterestTypeModel (
	@SerialName("Id") val Id: Int,
	@SerialName("Code") val Code: String,
	@SerialName("Name") val Name: String
)