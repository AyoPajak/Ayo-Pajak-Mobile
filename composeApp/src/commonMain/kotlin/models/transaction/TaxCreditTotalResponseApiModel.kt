package models.transaction

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TaxCreditTotalResponseApiModel (
	@SerialName("WithholdingTaxAmountIDR") val WithholdingTaxAmountIDR: Double, //TODO("BigDecimal")
	@SerialName("TaxCreditIDR") val TaxCreditIDR: Double //TODO("BigDecimal")
)