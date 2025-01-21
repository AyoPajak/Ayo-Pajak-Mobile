package models.transaction

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FormTaxCreditResponseApiModel(
	@SerialName("Id") val Id: Int,
	@SerialName("Tr1770HdId") val Tr1770HdId: Int,
	@SerialName("Seq") val Seq: Int,
	@SerialName("TaxTypeE") val TaxTypeE: Int,
	@SerialName("WithholderNPWP") val WithholderNPWP: String,
	@SerialName("WithholderName") val WithholderName: String,
	@SerialName("WithholdingTaxNo") val WithholdingTaxNo: String,
	@SerialName("WithholdingDate") val WithholdingDate: String, //TODO("LocalDate")
	@SerialName("WithholdingTaxAmountIDR") val WithholdingTaxAmountIDR: Double, //TODO("BigDecimal")
	@SerialName("TaxCreditIDR") val TaxCreditIDR: Double, //TODO("BigDecimal")
	@SerialName("NetJob") val NetJob: FormIncomeNetJobResponseApiModel?,
	@SerialName("NetOther") val NetOther: FormNetOtherIncomeResponseApiModel?
)
