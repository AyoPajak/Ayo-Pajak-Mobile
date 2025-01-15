package models.transaction

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import models.master.CityModel
import models.master.KluModel

@Serializable
data class FormFinalIncomeResponseApiModel (
	@SerialName("Id") val Id: Int,
	@SerialName("Tr1770HdId") val Tr1770HdId: Int,
	@SerialName("IncomeTypeE") val IncomeTypeE: Int,
	@SerialName("IncomeIDR") val IncomeIDR: Double, //TODO("BigDecimal")
	@SerialName("TaxPayableIDR") val TaxPayableIDR: Double?, //TODO("BigDecimal")
	@SerialName("Description") val Description: String?,
	
	@SerialName("SellPriceIDR") val SellPriceIDR: Double?, //TODO("BigDecimal")
	@SerialName("City") val City: CityModel?,
	@SerialName("KLU") val KLU: KluModel?,
	@SerialName("BrutoCirculations") val BrutoCirculations: List<BrutoCirculationResponseApiModel>?,
	
	@SerialName("NPWP") val NPWP: String?,
	@SerialName("KPPLocation") val KPPLocation: String?,
	@SerialName("BusinessAddress") val BusinessAddress: String?,
	@SerialName("RentStartDate") val RentStartDate: String?,  //TODO("LocalDate")
	@SerialName("RentEndDate") val RentEndDate: String?,  //TODO("LocalDate")
	
	@SerialName("Wealth") val Wealth: FormWealthResponseApiModel?,
)

@Serializable
data class Form1770FinalIncomeUmkm2023ResponseApiModel(
	@SerialName("BusinessList") val BusinessList: List<Form1770FinalIncomeUmkm2023BusinessModel>,
	@SerialName("Summary") val Summary: Form1770FinalIncomeUmkm2023SummaryModel
)