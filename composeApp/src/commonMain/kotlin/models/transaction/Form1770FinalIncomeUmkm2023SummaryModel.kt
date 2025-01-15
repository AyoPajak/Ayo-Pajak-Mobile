package models.transaction

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Form1770FinalIncomeUmkm2023SummaryModel(
	@SerialName("Tr1770IncomeId") val Tr1770IncomeId: Int?,
	@SerialName("Sum_1") val Sum_1: Double, //TODO("BigDecimal")
	@SerialName("Sum_2") val Sum_2: Double,
	@SerialName("Sum_3") val Sum_3: Double,
	@SerialName("Sum_4") val Sum_4: Double,
	@SerialName("Sum_5") val Sum_5: Double,
	@SerialName("Sum_6") val Sum_6: Double,
	@SerialName("Sum_7") val Sum_7: Double,
	@SerialName("Sum_8") val Sum_8: Double,
	@SerialName("Sum_9") val Sum_9: Double,
	@SerialName("Sum_10") val Sum_10: Double,
	@SerialName("Sum_11") val Sum_11: Double,
	@SerialName("Sum_12") val Sum_12: Double,
	@SerialName("SumTotal") val SumTotal: Double,
	@SerialName("Accumulate_1") val Accumulate_1: Double,
	@SerialName("Accumulate_2") val Accumulate_2: Double,
	@SerialName("Accumulate_3") val Accumulate_3: Double,
	@SerialName("Accumulate_4") val Accumulate_4: Double,
	@SerialName("Accumulate_5") val Accumulate_5: Double,
	@SerialName("Accumulate_6") val Accumulate_6: Double,
	@SerialName("Accumulate_7") val Accumulate_7: Double,
	@SerialName("Accumulate_8") val Accumulate_8: Double,
	@SerialName("Accumulate_9") val Accumulate_9: Double,
	@SerialName("Accumulate_10") val Accumulate_10: Double,
	@SerialName("Accumulate_11") val Accumulate_11: Double,
	@SerialName("Accumulate_12") val Accumulate_12: Double,
	@SerialName("PKP_1") val PKP_1: Double,
	@SerialName("PKP_2") val PKP_2: Double,
	@SerialName("PKP_3") val PKP_3: Double,
	@SerialName("PKP_4") val PKP_4: Double,
	@SerialName("PKP_5") val PKP_5: Double,
	@SerialName("PKP_6") val PKP_6: Double,
	@SerialName("PKP_7") val PKP_7: Double,
	@SerialName("PKP_8") val PKP_8: Double,
	@SerialName("PKP_9") val PKP_9: Double,
	@SerialName("PKP_10") val PKP_10: Double,
	@SerialName("PKP_11") val PKP_11: Double,
	@SerialName("PKP_12") val PKP_12: Double,
	@SerialName("PKPTotal") val PKPTotal: Double,
	@SerialName("PPhFinal_1") val PPhFinal_1: Double,
	@SerialName("PPhFinal_2") val PPhFinal_2: Double,
	@SerialName("PPhFinal_3") val PPhFinal_3: Double,
	@SerialName("PPhFinal_4") val PPhFinal_4: Double,
	@SerialName("PPhFinal_5") val PPhFinal_5: Double,
	@SerialName("PPhFinal_6") val PPhFinal_6: Double,
	@SerialName("PPhFinal_7") val PPhFinal_7: Double,
	@SerialName("PPhFinal_8") val PPhFinal_8: Double,
	@SerialName("PPhFinal_9") val PPhFinal_9: Double,
	@SerialName("PPhFinal_10") val PPhFinal_10: Double,
	@SerialName("PPhFinal_11") val PPhFinal_11: Double,
	@SerialName("PPhFinal_12") val PPhFinal_12: Double,
	@SerialName("PPhFinalTotal") val PPhFinalTotal: Double,
	@SerialName("PPhSelfPaid_1") val PPhSelfPaid_1: Double,
	@SerialName("PPhSelfPaid_2") val PPhSelfPaid_2: Double,
	@SerialName("PPhSelfPaid_3") val PPhSelfPaid_3: Double,
	@SerialName("PPhSelfPaid_4") val PPhSelfPaid_4: Double,
	@SerialName("PPhSelfPaid_5") val PPhSelfPaid_5: Double,
	@SerialName("PPhSelfPaid_6") val PPhSelfPaid_6: Double,
	@SerialName("PPhSelfPaid_7") val PPhSelfPaid_7: Double,
	@SerialName("PPhSelfPaid_8") val PPhSelfPaid_8: Double,
	@SerialName("PPhSelfPaid_9") val PPhSelfPaid_9: Double,
	@SerialName("PPhSelfPaid_10") val PPhSelfPaid_10: Double,
	@SerialName("PPhSelfPaid_11") val PPhSelfPaid_11: Double,
	@SerialName("PPhSelfPaid_12") val PPhSelfPaid_12: Double,
	@SerialName("PPhSelfPaidTotal") val PPhSelfPaidTotal: Double,
	@SerialName("PPhDeducted_1") val PPhDeducted_1: Double,
	@SerialName("PPhDeducted_2") val PPhDeducted_2: Double,
	@SerialName("PPhDeducted_3") val PPhDeducted_3: Double,
	@SerialName("PPhDeducted_4") val PPhDeducted_4: Double,
	@SerialName("PPhDeducted_5") val PPhDeducted_5: Double,
	@SerialName("PPhDeducted_6") val PPhDeducted_6: Double,
	@SerialName("PPhDeducted_7") val PPhDeducted_7: Double,
	@SerialName("PPhDeducted_8") val PPhDeducted_8: Double,
	@SerialName("PPhDeducted_9") val PPhDeducted_9: Double,
	@SerialName("PPhDeducted_10") val PPhDeducted_10: Double,
	@SerialName("PPhDeducted_11") val PPhDeducted_11: Double,
	@SerialName("PPhDeducted_12") val PPhDeducted_12: Double,
	@SerialName("PPhDeductedTotal") val PPhDeductedTotal: Double,
	@SerialName("PPhDifference_1") val PPhDifference_1: Double,
	@SerialName("PPhDifference_2") val PPhDifference_2: Double,
	@SerialName("PPhDifference_3") val PPhDifference_3: Double,
	@SerialName("PPhDifference_4") val PPhDifference_4: Double,
	@SerialName("PPhDifference_5") val PPhDifference_5: Double,
	@SerialName("PPhDifference_6") val PPhDifference_6: Double,
	@SerialName("PPhDifference_7") val PPhDifference_7: Double,
	@SerialName("PPhDifference_8") val PPhDifference_8: Double,
	@SerialName("PPhDifference_9") val PPhDifference_9: Double,
	@SerialName("PPhDifference_10") val PPhDifference_10: Double,
	@SerialName("PPhDifference_11") val PPhDifference_11: Double,
	@SerialName("PPhDifference_12") val PPhDifference_12: Double,
	@SerialName("PPhDifferenceTotal") val PPhDifferenceTotal: Double
)

@Serializable
data class Form1770FinalIncomeUmkm2023SummaryRequestModel(
	@SerialName("Tr1770IncomeId") val Tr1770IncomeId: Int?,
	@SerialName("PPhSelfPaid_1") val PPhSelfPaid_1: Long, //TODO("Long")
	@SerialName("PPhSelfPaid_2") val PPhSelfPaid_2: Long,
	@SerialName("PPhSelfPaid_3") val PPhSelfPaid_3: Long,
	@SerialName("PPhSelfPaid_4") val PPhSelfPaid_4: Long,
	@SerialName("PPhSelfPaid_5") val PPhSelfPaid_5: Long,
	@SerialName("PPhSelfPaid_6") val PPhSelfPaid_6: Long,
	@SerialName("PPhSelfPaid_7") val PPhSelfPaid_7: Long,
	@SerialName("PPhSelfPaid_8") val PPhSelfPaid_8: Long,
	@SerialName("PPhSelfPaid_9") val PPhSelfPaid_9: Long,
	@SerialName("PPhSelfPaid_10") val PPhSelfPaid_10: Long,
	@SerialName("PPhSelfPaid_11") val PPhSelfPaid_11: Long,
	@SerialName("PPhSelfPaid_12") val PPhSelfPaid_12: Long,
	@SerialName("PPhDeducted_1") val PPhDeducted_1: Long,
	@SerialName("PPhDeducted_2") val PPhDeducted_2: Long,
	@SerialName("PPhDeducted_3") val PPhDeducted_3: Long,
	@SerialName("PPhDeducted_4") val PPhDeducted_4: Long,
	@SerialName("PPhDeducted_5") val PPhDeducted_5: Long,
	@SerialName("PPhDeducted_6") val PPhDeducted_6: Long,
	@SerialName("PPhDeducted_7") val PPhDeducted_7: Long,
	@SerialName("PPhDeducted_8") val PPhDeducted_8: Long,
	@SerialName("PPhDeducted_9") val PPhDeducted_9: Long,
	@SerialName("PPhDeducted_10") val PPhDeducted_10: Long,
	@SerialName("PPhDeducted_11") val PPhDeducted_11: Long,
	@SerialName("PPhDeducted_12") val PPhDeducted_12: Long
)
