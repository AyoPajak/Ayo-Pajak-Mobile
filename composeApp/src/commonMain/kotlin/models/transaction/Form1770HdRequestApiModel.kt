package models.transaction

import global.AccountRecordType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Form1770HdRequestApiModel (
	@SerialName("TaxYear") val TaxYear: Int,
	@SerialName("SPTType") val SPTType: String,
	@SerialName("CorrectionSeq") val CorrectionSeq: Int,
	@SerialName("AccountRecordE") val AccountRecordE: Int? = AccountRecordType.RECORD.value,
	@SerialName("SkipCorrectionSeq") val SkipCorrectionSeq: Boolean = false,
)