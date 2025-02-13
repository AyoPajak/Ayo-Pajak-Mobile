package models.transaction

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FormAdditionalResponseApiModel (
	@SerialName("Tr1770HdId") val Tr1770HdId: Int,
	@SerialName("FinalTaxPaymentStateE") val FinalTaxPaymentStateE: Int?,
	
	@SerialName("TaxAmount") val TaxAmount: Double, //TODO("BigDecimal")
	@SerialName("SSPAmount") val SSPAmount: Double, //TODO("BigDecimal")
	@SerialName("TaxPaidDate") val TaxPaidDate: String?, //TODO("LocalDate")
	@SerialName("OverpaidRequestE") val OverpaidRequestE: Int?,
	@SerialName("Art25InstallmentBaseE") val Art25InstallmentBaseE: Int,
	@SerialName("Art25Installment_IDR") val Art25Installment_IDR: Double, //TODO("BigDecimal")
	@SerialName("ReportDate") val ReportDate: String?, //TODO("LocalDate")
	@SerialName("AuthorityE") val AuthorityE: Int,
	@SerialName("ReporterName") val ReporterName: String,
	@SerialName("ReporterNPWP") val ReporterNPWP: String,
	
	@SerialName("InclLampA") val InclLampA: Boolean,
	@SerialName("InclLampB") val InclLampB: Boolean,
	@SerialName("InclLampC") val InclLampC: Boolean,
	@SerialName("InclLampD") val InclLampD: Boolean,
	@SerialName("InclLampE") val InclLampE: Boolean,
	@SerialName("InclLampF") val InclLampF: Boolean,
	@SerialName("LampFCount") val LampFCount: Int?,
	@SerialName("InclLampG") val InclLampG: Boolean,
	@SerialName("InclLampH") val InclLampH: Boolean,
	@SerialName("LampHName") val LampHName: String?,
	@SerialName("InclLampI") val InclLampI: Boolean,
	@SerialName("InclLampJ") val InclLampJ: Boolean,
	@SerialName("InclLampK") val InclLampK: Boolean,
	@SerialName("InclLampL") val InclLampL: Boolean,
	@SerialName("LampLName") val LampLName: String?
)

