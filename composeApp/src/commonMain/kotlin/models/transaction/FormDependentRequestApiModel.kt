package models.transaction

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FormDependentRequestApiModel (
	@SerialName("Id") val Id: Int,
	@SerialName("Tr1770HdId") val Tr1770HdId: Int,
	@SerialName("DependentName") val DependentName: String,
	@SerialName("DependentNIK") val DependentNIK: String,
	@SerialName("FamilyRelId") val FamilyRelId: Int,
	@SerialName("JobId") val JobId: Int,
	@SerialName("BirthDate") val BirthDate: String,
	@SerialName("DependentFee") val DependentFee: Long,
	@SerialName("SchoolFee") val SchoolFee: Long,
)