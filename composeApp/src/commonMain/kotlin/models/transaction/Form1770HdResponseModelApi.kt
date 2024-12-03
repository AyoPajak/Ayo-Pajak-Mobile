package models.transaction

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Form1770HdResponseApiModel (
	@SerialName("Id") val Id: Int,
	@SerialName("SPTType") val SPTType: String,
	@SerialName("TaxYear") val TaxYear: String,
	@SerialName("StartPeriod") val StartPeriod: String,
	@SerialName("EndPeriod") val EndPeriod: String,
	@SerialName("CorrectionSeq") val CorrectionSeq: Int,
	@SerialName("SPTFileName") val SPTFileName: String,
	@SerialName("LastStep") val LastStep: Int,
	@SerialName("TaxPaymentStateE") val TaxPaymentStateE: Int?,
	@SerialName("TaxPayable") val TaxPayable: Double?, //TODO(Should be BigDecimal?)
	@SerialName("SSPAmount") val SSPAmount: Double?, //TODO(Should be BigDecimal?)
	@SerialName("TaxReportStatusE") val TaxReportStatusE: Int?,
	@SerialName("ReportDesc") val ReportDesc: String?,
	@SerialName("ReportDate") val ReportDate: String?, //TODO(Should be LocalDateTime?)
	@SerialName("EFilingId") val EFilingId: Int?,
)