package models.transaction

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FormDebtRequestApiModel (
	@SerialName("Id") val Id: Int,
	@SerialName("Tr1770HdId") val Tr1770HdId: Int,
	@SerialName("DebtTypeCode") val DebtTypeCode: String,
	@SerialName("LenderName") val LenderName: String,
	@SerialName("LenderAddress") val LenderAddress: String,
	@SerialName("DebtYear") val DebtYear: Int,
	@SerialName("Principal_IDR") val Principal_IDR: Long,
	@SerialName("MonInstallment_IDR") val MonInstallment_IDR: Long,
	@SerialName("Outstanding_IDR") val Outstanding_IDR: Long,
)