package models.transaction

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class FormFinalIncomeARequestApiModel (
	@SerialName("Id") val Id: Int,
	@SerialName("Tr1770HdId") val Tr1770HdId: Int,
	@SerialName("IncomeTypeE") val IncomeTypeE: Int,
	@SerialName("Tr1770WealthId") val Tr1770WealthId: Int?,
	@SerialName("IncomeIDR") val IncomeIDR: Long, //TODO("BigDecimal")
	@SerialName("TaxPayableIDR") val TaxPayableIDR: Long, //TODO("BigDecimal")
	@SerialName("RealizedIDR") val RealizedIDR: Long?, //TODO("BigDecimal")
	@SerialName("Description") val Description: String?
)

@kotlinx.serialization.Serializable
data class FormFinalIncomeBRequestApiModel (
	@SerialName("Id") val Id: Int,
	@SerialName("Tr1770HdId") val Tr1770HdId: Int,
	@SerialName("IncomeTypeE") val IncomeTypeE: Int,
	@SerialName("IncomeIDR") val IncomeIDR: Long, //TODO("BigDecimal")
	@SerialName("TaxPayableIDR") val TaxPayableIDR: Long, //TODO("BigDecimal")
	@SerialName("Description") val Description: String?
)

@kotlinx.serialization.Serializable
data class FormFinalIncomeCRequestApiModel (
	@SerialName("Id") val Id: Int,
	@SerialName("Tr1770HdId") val Tr1770HdId: Int,
	@SerialName("Tr1770WealthId") val Tr1770WealthId: Int?,
	@SerialName("SellPriceIDR") val SellPriceIDR: Long, //TODO("BigDecimal")
	@SerialName("IncomeIDR") val IncomeIDR: Long, //TODO("BigDecimal")
	@SerialName("TaxPayableIDR") val TaxPayableIDR: Long, //TODO("BigDecimal")
	@SerialName("Description") val Description: String?
)

@kotlinx.serialization.Serializable
data class FormFinalIncomeDRequestApiModel (
	@SerialName("Id") val Id: Int,
	@SerialName("Tr1770HdId") val Tr1770HdId: Int,
	@SerialName("CityId") val CityId: Int,
	@SerialName("KLUId") val KLUId: Int,
	@SerialName("NPWP") val NPWP: String,
	@SerialName("KPPLocation") val KPPLocation: String,
	@SerialName("UseUUHPP") val UseUUHPP: Boolean,
	@SerialName("BusinessAddress") val BusinessAddress: String,
	@SerialName("BrutoCirculations") val BrutoCirculations: List<BrutoCirculationRequestApiModel>,
)

@kotlinx.serialization.Serializable
data class FormFinalIncomeERequestApiModel (
	@SerialName("Id") val Id: Int,
	@SerialName("Tr1770HdId") val Tr1770HdId: Int,
	@SerialName("Tr1770WealthId") val Tr1770WealthId: Int?,
	@SerialName("IncomeIDR") val IncomeIDR: Long, //TODO("BigDecimal")
	@SerialName("TaxPayableIDR") val TaxPayableIDR: Long, //TODO("BigDecimal")
	@SerialName("RentStartDate") val RentStartDate: String?,
	@SerialName("RentEndDate") val RentEndDate: String?,
	@SerialName("Description") val Description: String?
)