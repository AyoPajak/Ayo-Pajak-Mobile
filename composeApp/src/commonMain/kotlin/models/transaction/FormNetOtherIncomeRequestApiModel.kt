package models.transaction

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FormNetOtherIncomeARequestApiModel (
	@SerialName("Id") val Id: Int,
	@SerialName("Tr1770HdId") val Tr1770HdId: Int,
	@SerialName("DomesticNetIncomeE") val DomesticNetIncomeE: Int,
	@SerialName("NettIncomeIDR") val NettIncomeIDR: Long, //TODO("BigDecimal")
	@SerialName("Description") val Description: String?
)

@Serializable
data class FormNetOtherIncomeBRequestApiModel (
	@SerialName("Id") val Id: Int,
	@SerialName("Tr1770HdId") val Tr1770HdId: Int,
	@SerialName("Tr1770WealthId") val Tr1770WealthId: Int?,
	@SerialName("SellPriceIDR") val SellPriceIDR: Long, //TODO("BigDecimal")
	@SerialName("NettIncomeIDR") val NettIncomeIDR: Long, //TODO("BigDecimal")
	@SerialName("Description") val Description: String?
)

@Serializable
data class FormNetOtherIncomeCRequestApiModel (
	@SerialName("Id") val Id: Int,
	@SerialName("Tr1770HdId") val Tr1770HdId: Int,
	@SerialName("Tr1770WealthId") val Tr1770WealthId: Int?,
	@SerialName("NettIncomeIDR") val NettIncomeIDR: Long, //TODO("BigDecimal")
	@SerialName("RentStartDate") val RentStartDate: String?,
	@SerialName("RentEndDate") val RentEndDate: String?,
	@SerialName("Description") val Description: String?
)

@Serializable
data class FormNetOtherIncomeDRequestApiModel (
	@SerialName("Id") val Id: Int,
	@SerialName("Tr1770HdId") val Tr1770HdId: Int,
	@SerialName("Tr1770WealthId") val Tr1770WealthId: Int?,
	@SerialName("EmployerNameAddr") val EmployerNameAddr: String?,
	@SerialName("SellPriceIDR") val SellPriceIDR: Long, //TODO("BigDecimal")
	@SerialName("NettIncomeIDR") val NettIncomeIDR: Long, //TODO("BigDecimal")
	@SerialName("Description") val Description: String?
)

@Serializable
data class FormNetOtherIncomeERequestApiModel (
	@SerialName("Id") val Id: Int,
	@SerialName("Tr1770HdId") val Tr1770HdId: Int,
	@SerialName("Tr1770WealthId") val Tr1770WealthId: Int?,
	@SerialName("EmployerNameAddr") val EmployerNameAddr: String?,
	@SerialName("OverseasIncomeType") val OverseasIncomeType: String?,
	@SerialName("NettIncomeIDR") val NettIncomeIDR: Long, //TODO("BigDecimal")
	@SerialName("Description") val Description: String?
)

@Serializable
data class FormNetOtherIncomeFRequestApiModel (
	@SerialName("Id") val Id: Int,
	@SerialName("Tr1770HdId") val Tr1770HdId: Int,
	@SerialName("Tr1770WealthId") val Tr1770WealthId: Int?,
	@SerialName("EmployerNameAddr") val EmployerNameAddr: String?,
	@SerialName("NettIncomeIDR") val NettIncomeIDR: Long, //TODO("BigDecimal")
	@SerialName("RentStartDate") val RentStartDate: String?,
	@SerialName("RentEndDate") val RentEndDate: String?,
	@SerialName("Description") val Description: String?
)