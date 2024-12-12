package models.master

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class KluModel (
	@SerialName("Id") val Id: Int,
	@SerialName("TaxYear") val TaxYear: String,
	@SerialName("KLUCode") val KLUCode: String,
	@SerialName("KLUName") val KLUName: String,
	@SerialName("N1A") val N1A: Double,
	@SerialName("N1B") val N1B: Double,
	@SerialName("N1C") val N1C: Double,
	@SerialName("N2A") val N2A: Double,
	@SerialName("N2B") val N2B: Double,
	@SerialName("N2C") val N2C: Double,
	@SerialName("N3A") val N3A: Double,
	@SerialName("N3B") val N3B: Double,
	@SerialName("N3C") val N3C: Double,
	@SerialName("Description") val Description: String
)