package models.transaction

import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import models.master.FamilyRelModel
import models.master.JobModel

@Serializable
data class FormDependentResponseApiModel (
	@SerialName("Id") val Id: Int,
	@SerialName("Tr1770HdId") val Tr1770HdId: Int,
	@SerialName("Seq") val Seq: Int,
	@SerialName("DependentName") val DependentName: String,
	@SerialName("DependentNIK") val DependentNIK: String?,
	@SerialName("FamilyRel") val FamilyRel: FamilyRelModel,
	@SerialName("Job") val Job: JobModel,
	@SerialName("BirthDate") val BirthDate: String, //TODO("Should be LocalDateTime")
	@SerialName("DependentFee") val DependentFee: Double, //TODO("Should be BigDecimal")
	@SerialName("SchoolFee") val SchoolFee: Double?, //TODO("Should be BigDecimal?")
)