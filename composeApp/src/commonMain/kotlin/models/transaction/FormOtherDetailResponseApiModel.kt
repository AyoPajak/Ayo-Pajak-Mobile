package models.transaction

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FormOtherDetailResponseApiModel (
	@SerialName("Tr1770HdId") val Tr1770HdId: Int,
	@SerialName("SelfLivingCostIDR") val SelfLivingCostIDR: Double?, //TODO("BigDecimal")
	@SerialName("ReligiousDonationIDR") val ReligiousDonationIDR: Double?, //TODO("BigDecimal")
	@SerialName("Article24DiffIDR") val Article24DiffIDR: Double?, //TODO("BigDecimal")
	@SerialName("MonthlyPPhPasal25Paid_IDR") val MonthlyPPhPasal25Paid_IDR: Double?, //TODO("BigDecimal")
	@SerialName("STPPasal25Paid_IDR") val STPPasal25Paid_IDR: Double?, //TODO("BigDecimal")
)
