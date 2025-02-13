package models.transaction

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FormAdditionalRequestApiModel (
	@SerialName("Tr1770HdId") val Tr1770HdId: Int,
	
	@SerialName("TaxPaidDate") val TaxPaidDate: String?,
	@SerialName("OverpaidRequestE") val OverpaidRequestE: Int?,
	@SerialName("Art25InstallmentBaseE") val Art25InstallmentBaseE: Int,
	@SerialName("Art25Installment_IDR") val Art25Installment_IDR: Long, //TODO("BigDecimal")
	@SerialName("ReportDate") val ReportDate: String,
	@SerialName("AuthorityE") val AuthorityE: Int,
	@SerialName("ReporterName") val ReporterName: String,
	@SerialName("ReporterNPWP") val ReporterNPWP: String,
	
	@SerialName("InclLampH") val InclLampH: Boolean,
	@SerialName("LampHName") val LampHName: String?,
	@SerialName("InclLampL") val InclLampL: Boolean,
	@SerialName("LampLName") val LampLName: String?
)
