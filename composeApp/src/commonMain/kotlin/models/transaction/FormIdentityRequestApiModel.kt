package models.transaction

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FormIdentityRequestApiModel (
	@SerialName("Tr1770HdId") val Tr1770HdId: Int,
	@SerialName("AccountRecordE") val AccountRecordE: Int,
	@SerialName("CityId") val CityId: Int,
	@SerialName("JobId") val JobId: Int,
	@SerialName("KLUId") val KLUId: Int,
	@SerialName("TelephoneNo") val TelephoneNo: String,
	@SerialName("HandphoneNo") val HandphoneNo: String?,
	@SerialName("MaritalStatusE") val MaritalStatusE: Int,
	@SerialName("TaxStatusE") val TaxStatusE: String,
	
	@SerialName("DependentCount") val DependentCount: Int,
	@SerialName("IsWifeOwnBusiness") val IsWifeOwnBusiness: Boolean,
	@SerialName("SpouseNPWP") val SpouseNPWP: String?,
	@SerialName("SpouseName") val SpouseName: String?,
	@SerialName("SpouseDependentCount") val SpouseDependentCount: Int?
)