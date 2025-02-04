package models.transaction

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import models.master.CityModel
import models.master.KluModel

@Serializable
data class FormNonFinalIncomeResponseApiModel(
	@SerialName("Id") val Id: Int,
	@SerialName("Tr1770HdId") val Tr1770HdId: Int,
	@SerialName("Seq") val Seq: Int,
	@SerialName("BusinessTypeE") val BusinessTypeE: Int,
	
	@SerialName("City") val City: CityModel,
	@SerialName("KLU") val KLU: KluModel?,
	@SerialName("TaxNormPerc") val TaxNormPerc: Double, //TODO(BigDecimal)
	@SerialName("IsPayingPPhPasal25") val IsPayingPPhPasal25: Boolean,
	@SerialName("NPWP") val NPWP: String?,
	@SerialName("KPPLocation") val KPPLocation: String?,
	@SerialName("BusinessAddress") val BusinessAddress: String?,
	
	@SerialName("BrutoCirculations") val BrutoCirculations: List<BrutoCirculationResponseApiModel>?,
	@SerialName("BusinessCirculationIDR") val BusinessCirculationIDR: Double, //TODO(BigDecimal)
	@SerialName("NettIncomeIDR") val NettIncomeIDR: Double //TODO(BigDecimal)
 )
