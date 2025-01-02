package models.master

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class CurrencyRateModel (
	@SerialName("Currency") val Currency: CurrencyModel,
	@SerialName("IDRRate") val IDRRate: Double //TODO(BIGDECIMAL)
)