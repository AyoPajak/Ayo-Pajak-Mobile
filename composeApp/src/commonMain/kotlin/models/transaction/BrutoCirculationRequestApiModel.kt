package models.transaction

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BrutoCirculationRequestApiModel (
	@SerialName("Id") val Id: Int?,
	@SerialName("Tr1770IncomeId") val Tr1770IncomeId: Int?,
	@SerialName("Period") val Period: String,
	@SerialName("IncomeIDR") val IncomeIDR: Long, //TODO("BigDecimal")
	@SerialName("TaxPayableIDR") val TaxPayableIDR: Long?, //TODO("BigDecimal")
)