package models.transaction

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FormIncomeNetJobRequestApiModel (
	@SerialName("Id") val Id: Int,
	@SerialName("Tr1770HdId") val Tr1770HdId: Int,
	@SerialName("EmployerName") val EmployerName: String,
	@SerialName("EmployerNPWP") val EmployerNPWP: String,
	
	@SerialName("GrossIncomeIDR") val GrossIncomeIDR: Long, //TODO("BigDecimal")
	@SerialName("DeductionIDR") val DeductionIDR: Long, //TODO("BigDecimal")
)
