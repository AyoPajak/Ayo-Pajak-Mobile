package models.transaction

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FormNonTaxedIncomeRequestApiModel (
	@SerialName("Id") val Id: Int,
	@SerialName("Tr1770HdId") val Tr1770HdId: Int,
	@SerialName("IncomeTypeE") val IncomeTypeE: Int,
	@SerialName("IncomeIDR") val IncomeIDR: Long, //TODO("BigDecimal")
	@SerialName("EmployerName") val EmployerName: String?,
	@SerialName("Description") val Description: String?
)