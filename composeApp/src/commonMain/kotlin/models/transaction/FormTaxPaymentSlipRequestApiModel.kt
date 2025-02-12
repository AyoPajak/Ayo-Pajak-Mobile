package models.transaction

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FormTaxPaymentSlipRequestApiModel (
	@SerialName("Id") val Id: Int,
	@SerialName("Tr1770HdId") val Tr1770HdId: Int,
	
	@SerialName("DepositTypeE") val DepositTypeE: Int,
	@SerialName("SSPDate") val SSPDate: String,
	@SerialName("SSPAmount") val SSPAmount: Long, //TODO("BigDecimal")
	@SerialName("NTPN") val NTPN: String?
)