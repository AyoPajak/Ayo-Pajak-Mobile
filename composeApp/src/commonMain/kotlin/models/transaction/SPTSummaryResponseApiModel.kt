package models.transaction

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SPTSummaryResponseApiModel (
	@SerialName("IncomeTotal") val IncomeTotal: Double, //TODO("BigDecimal")
	@SerialName("WealthTotal") val WealthTotal: Double, //TODO("BigDecimal")
	@SerialName("DebtTotal") val DebtTotal: Double //TODO("BigDecimal")
)