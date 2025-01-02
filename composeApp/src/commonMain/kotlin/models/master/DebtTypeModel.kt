package models.master

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DebtTypeModel (
	@SerialName("Id") val Id: Int,
	@SerialName("DebtTypeCode") val DebtTypeCode: String,
	@SerialName("DebtTypeName") val DebtTypeName: String
)