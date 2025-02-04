package models.transaction

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FormNonFinalIncomeRequestApiModel (
	@SerialName("Id") val Id: Int,
	@SerialName("Tr1770HdId") val Tr1770HdId: Int,
	@SerialName("BusinessTypeE") val BusinessTypeE: Int,
	@SerialName("CityId") val CityId: Int,
	@SerialName("KluId") val KluId: Int,
	@SerialName("IsPayingPPhPasal25") val IsPayingPPhPasal25: Boolean,
	@SerialName("NPWP") val NPWP: String?,
	@SerialName("KPPLocation") val KPPLocation: String?,
	@SerialName("BusinessAddress") val BusinessAddress: String?,
	@SerialName("BrutoCirculations") val BrutoCirculations: List<BrutoCirculationRequestApiModel>,
)
