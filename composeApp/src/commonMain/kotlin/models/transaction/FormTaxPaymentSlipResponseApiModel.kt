package models.transaction

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FormTaxPaymentSlipResponseApiModel (
	@SerialName("Id") val Id: Int,
	@SerialName("Tr1770HdId") val Tr1770HdId: Int,
	
	@SerialName("KAPCode") val KAPCode: String,
	@SerialName("DepositTypeE") val DepositTypeE: Int,
	@SerialName("SSPDate") val SSPDate: String, //TODO("LocalDate")
	@SerialName("SSPAmount") val SSPAmount: Double, //TODO("BigDecimal")
	@SerialName("NTPN") val NTPN: String?,
	
	@SerialName("RefAyoPajakEBillingId") val RefAyoPajakEBillingId: Long?,
	@SerialName("BillingCode") val BillingCode: String?,
	@SerialName("BillingExpiryDate") val BillingExpiryDate: String?, //TODO("LocalDateTime")
)
