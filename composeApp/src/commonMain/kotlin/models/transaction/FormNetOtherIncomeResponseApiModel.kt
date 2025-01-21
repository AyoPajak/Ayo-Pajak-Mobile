package models.transaction

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FormNetOtherIncomeResponseApiModel(
	@SerialName("Id") val Id: Int,
	@SerialName("Tr1770HdId") val Tr1770HdId: Int,
	@SerialName("Seq") val Seq: Int,
	@SerialName("DomesticNetIncomeE") val IncomeTypeE: Int,
	@SerialName("NettIncomeIDR") val NettIncomeIDR: Double,
	@SerialName("Description") val Description: String?,
	
	@SerialName("SellPriceIDR") val SellPriceIDR: Double?,
	@SerialName("RentStartDate") val RentStartDate: String?, //TODO("LocalDate")
	@SerialName("RentEndDate") val RentEndDate: String?, //TODO("LocalDate")
	@SerialName("EmployerNameAddr") val EmployerNameAddr: String?,
	@SerialName("OverseasIncomeType") val OverseasIncomeType: String?,
	
	@SerialName("Wealth") val Wealth: FormWealthResponseApiModel?,
	@SerialName("TaxCredit") val TaxCredit: FormTaxCreditResponseApiModel?
)
