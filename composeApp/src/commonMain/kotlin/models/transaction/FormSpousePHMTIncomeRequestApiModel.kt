package models.transaction

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FormSpousePHMTIncomeRequestApiModel (
	@SerialName("Tr1770HdId") val Tr1770HdId: Int,
	@SerialName("OnlyHasFinalIncomeSelf") val OnlyHasFinalIncomeSelf: Boolean,
	@SerialName("OnlyHasFinalIncomeSpouse") val OnlyHasFinalIncomeSpouse: Boolean,
	
	@SerialName("NonFinalIncomeIDR") val NonFinalIncomeIDR: Long, //TODO("BigDecimal")
	@SerialName("JobNettIncomeIDR") val JobNettIncomeIDR: Long, //TODO("BigDecimal")
	@SerialName("OtherNettIncomeIDR") val OtherNettIncomeIDR: Long, //TODO("BigDecimal")
	@SerialName("OverseasNettIncomeIDR") val OverseasNettIncomeIDR: Long, //TODO("BigDecimal")
	@SerialName("ReligiousDonationIDR") val ReligiousDonationIDR: Long, //TODO("BigDecimal")
	@SerialName("LossCompensationIDR") val LossCompensationIDR: Long, //TODO("BigDecimal")
)
