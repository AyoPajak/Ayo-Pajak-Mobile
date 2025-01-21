package models.transaction

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FormIncomeNetJobResponseApiModel(
	@SerialName("Id") val Id: Int,
	@SerialName("Tr1770HdId") val Tr1770HdId: Int,
	@SerialName("Seq") val Seq: Int,
	@SerialName("EmployerName") val EmployerName: String,
	@SerialName("EmployerNPWP") val EmployerNPWP: String,
	@SerialName("GrossIncomeIDR") val GrossIncomeIDR: Double,
	@SerialName("DeductionIDR") val DeductionIDR: Double,
	@SerialName("NettIncomeIDR") val NettIncomeIDR: Double,
	
	@SerialName("TaxCredit") val TaxCredit: FormTaxCreditResponseApiModel?
)
