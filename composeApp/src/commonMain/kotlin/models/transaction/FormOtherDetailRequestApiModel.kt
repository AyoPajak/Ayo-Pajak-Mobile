package models.transaction

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FormOtherDetailRequestApiModel (
	@SerialName("Tr1770HdId") val Tr1770HdId: Int,
	@SerialName("SelfLivingCostIDR") val SelfLivingCostIDR: Long?, //TODO("BigDecimal")
	@SerialName("ReligiousDonationIDR") val ReligiousDonationIDR: Long?, //TODO("BigDecimal")
	@SerialName("Article24DiffIDR") val Article24DiffIDR: Long?, //TODO("BigDecimal")
	@SerialName("MonthlyPPhPasal25Paid_IDR") val MonthlyPPhPasal25Paid_IDR: Long?, //TODO("BigDecimal")
	@SerialName("STPPasal25Paid_IDR") val STPPasal25Paid_IDR: Long?, //TODO("BigDecimal")
)
