package models.transaction

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@kotlinx.serialization.Serializable
data class FormTaxCreditARequestApiModel (
	@SerialName("Id") val Id: Int,
	@SerialName("Tr1770HdId") val Tr1770HdId: Int,
	@SerialName("TaxTypeE") val TaxTypeE: Int,
	@SerialName("Tr1770IncomeNetJobId") var Tr1770IncomeNetJobId: Int?,
	@SerialName("WithholderNPWP") val WithholderNPWP: String,
	@SerialName("WithholderName") val WithholderName: String,
	@SerialName("WithholdingTaxNo") val WithholdingTaxNo: String,
	@SerialName("WithholdingDate") val WithholdingDate: String, //TODO("LocalDate")
	@SerialName("WithholdingTaxAmountIDR") val WithholdingTaxAmountIDR: Long, //TODO("BigDecimal")
)

@Serializable
data class FormTaxCreditBRequestApiModel (
	@SerialName("Id") val Id: Int,
	@SerialName("Tr1770HdId") val Tr1770HdId: Int,
	@SerialName("Tr1770IncomeNetOtherId") val Tr1770IncomeNetOtherId: Int?,
	@SerialName("WithholderNPWP") val WithholderNPWP: String,
	@SerialName("WithholderName") val WithholderName: String,
	@SerialName("WithholdingTaxNo") val WithholdingTaxNo: String,
	@SerialName("WithholdingDate") val WithholdingDate: String, //TODO("LocalDate")
	@SerialName("WithholdingTaxAmountIDR") val WithholdingTaxAmountIDR: Long, //TODO("BigDecimal")
)
