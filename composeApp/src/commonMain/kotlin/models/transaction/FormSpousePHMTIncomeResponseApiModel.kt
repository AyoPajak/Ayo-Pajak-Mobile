package models.transaction

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FormSpousePHMTIncomeResponseApiModel (
	@SerialName("Tr1770HdId") val Tr1770HdId: Int,
	@SerialName("OnlyHasFinalIncomeSelf") val OnlyHasFinalIncomeSelf: Boolean,
	@SerialName("OnlyHasFinalIncomeSpouse") val OnlyHasFinalIncomeSpouse: Boolean,
	
	@SerialName("NonFinalIncomeIDR") val NonFinalIncomeIDR: Double, //TODO("BigDecimal")
	@SerialName("JobNettIncomeIDR") val JobNettIncomeIDR: Double, //TODO("BigDecimal")
	@SerialName("OtherNettIncomeIDR") val OtherNettIncomeIDR: Double, //TODO("BigDecimal")
	@SerialName("OverseasNettIncomeIDR") val OverseasNettIncomeIDR: Double, //TODO("BigDecimal")
	@SerialName("ReligiousDonationIDR") val ReligiousDonationIDR: Double, //TODO("BigDecimal")
	@SerialName("SubTotalA_IDR") val SubTotalA_IDR: Double, //TODO("BigDecimal")
	@SerialName("LossCompensationIDR") val LossCompensationIDR: Double, //TODO("BigDecimal")
	@SerialName("TotalA_IDR") val TotalA_IDR: Double, //TODO("BigDecimal")
)