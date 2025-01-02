package models.transaction

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import models.master.DebtTypeModel

@Serializable
data class FormDebtResponseApiModel (
	@SerialName("Id") val Id: Int,
	@SerialName("Tr1770HdId") val Tr1770HdId: Int,
	@SerialName("Seq") val Seq: Int,
	@SerialName("DebtType") val DebtType: DebtTypeModel,
	@SerialName("LenderName") val LenderName: String,
	@SerialName("LenderAddress") val LenderAddress: String,
	@SerialName("DebtYear") val DebtYear: String,
	@SerialName("Principal_IDR") val Principal_IDR: Double, //TODO("BigDecimal")
	@SerialName("MonInstallment_IDR") val MonInstallment_IDR: Double?, //TODO("BigDecimal")
	@SerialName("Outstanding_IDR") val Outstanding_IDR: Double, //TODO("BigDecimal")
)