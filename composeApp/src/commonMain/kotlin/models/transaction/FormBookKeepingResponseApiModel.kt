package models.transaction

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FormBookKeepingResponseApiModel(
	@SerialName("Tr1770HdId") val Tr1770HdId: Int,
	@SerialName("IsAudited") val IsAudited: Boolean,
	
	@SerialName("AuditOpinionE") val AuditOpinionE: Int?,
	@SerialName("PubAccName") val PubAccName: String?,
	@SerialName("PubAccNPWP") val PubAccNPWP: String?,
	@SerialName("PubAccOfficeName") val PubAccOfficeName: String?,
	@SerialName("PubAccOfficeNPWP") val PubAccOfficeNPWP: String?,
	@SerialName("TaxConsultantName") val TaxConsultantName: String?,
	@SerialName("TaxConsultantNPWP") val TaxConsultantNPWP: String?,
	@SerialName("TaxConsultantOfficeName") val TaxConsultantOfficeName: String?,
	@SerialName("TaxConsultantOfficeNPWP") val TaxConsultantOfficeNPWP: String?,
	
	@SerialName("BusinessCirculationIDR") val BusinessCirculationIDR: Double, //TODO("BigDecimal")
	@SerialName("HPPIDR") val HPPIDR: Double, //TODO("BigDecimal")
	@SerialName("ProfitLossIDR") val ProfitLossIDR: Double, //TODO("BigDecimal")
	@SerialName("BusinessCostIDR") val BusinessCostIDR: Double, //TODO("BigDecimal")
	@SerialName("NettIncomeIDR") val NettIncomeIDR: Double, //TODO("BigDecimal")
	@SerialName("P2A_IDR") val P2A_IDR: Double, //TODO("BigDecimal")
	@SerialName("P2B_IDR") val P2B_IDR: Double, //TODO("BigDecimal")
	@SerialName("P2C_IDR") val P2C_IDR: Double, //TODO("BigDecimal")
	@SerialName("P2D_IDR") val P2D_IDR: Double, //TODO("BigDecimal")
	@SerialName("P2E_IDR") val P2E_IDR: Double, //TODO("BigDecimal")
	@SerialName("P2F_IDR") val P2F_IDR: Double, //TODO("BigDecimal")
	@SerialName("P2G_IDR") val P2G_IDR: Double, //TODO("BigDecimal")
	@SerialName("P2H_IDR") val P2H_IDR: Double, //TODO("BigDecimal")
	@SerialName("P2I_IDR") val P2I_IDR: Double, //TODO("BigDecimal")
	@SerialName("P2J_IDR") val P2J_IDR: Double, //TODO("BigDecimal")
	@SerialName("P2K_IDR") val P2K_IDR: Double, //TODO("BigDecimal")
	@SerialName("P2L_IDR") val P2L_IDR: Double, //TODO("BigDecimal")
	@SerialName("P3A_IDR") val P3A_IDR: Double, //TODO("BigDecimal")
	@SerialName("P3B_IDR") val P3B_IDR: Double, //TODO("BigDecimal")
	@SerialName("P3C_IDR") val P3C_IDR: Double, //TODO("BigDecimal")
	@SerialName("P3D_IDR") val P3D_IDR: Double, //TODO("BigDecimal")
	@SerialName("Final_IDR") val Final_IDR: Double, //TODO("BigDecimal")
	@SerialName("LossCompensationIDR") val LossCompensationIDR: Double //TODO("BigDecimal")
)
