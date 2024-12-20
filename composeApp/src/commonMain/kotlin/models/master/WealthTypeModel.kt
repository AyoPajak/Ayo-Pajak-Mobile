package models.master

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WealthTypeModel (
	@SerialName("Id") val Id: Int,
	@SerialName("WealthTypeCode") val WealthTypeCode: String,
	@SerialName("WealthTypeName") val WealthTypeName: String
)