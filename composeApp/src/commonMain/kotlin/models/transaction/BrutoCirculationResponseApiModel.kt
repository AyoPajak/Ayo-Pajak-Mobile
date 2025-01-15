package models.transaction

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BrutoCirculationResponseApiModel (
	@SerialName("Id") val Id: Int,
	@SerialName("Tr1770IncomeId") val Tr1770IncomeId: Int,
	@SerialName("Period") val Period: String,
	@SerialName("IncomeIDR") val IncomeIDR: Double, //TODO("BigDecimal")
	@SerialName("TaxPayableIDR") val TaxPayableIDR: Double, //TODO("BigDecimal")
)