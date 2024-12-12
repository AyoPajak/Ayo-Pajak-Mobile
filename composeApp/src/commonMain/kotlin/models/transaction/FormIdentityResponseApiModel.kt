package models.transaction

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import models.master.CityModel
import models.master.JobModel
import models.master.KluModel

@Serializable
data class FormIdentityResponseApiModel (
	@SerialName("NPWP") val NPWP: String,
	@SerialName("WPName") val WPName: String,
	@SerialName("WPNIK") val WPNIK: String,
	@SerialName("GenderE") val GenderE: String,
	@SerialName("City") val City: CityModel?,
	@SerialName("Job") val Job: JobModel?,
	@SerialName("KLU") val KLU: KluModel?,
	@SerialName("TelephoneNo") val TelephoneNo: String,
	@SerialName("HandphoneNo") val HandphoneNo: String?,
	@SerialName("MaritalStatusE") val MaritalStatusE: Int,
	@SerialName("TaxStatusE") val TaxStatusE: String,
	
	@SerialName("Tr1770HdId") val Tr1770HdId: Int,
	@SerialName("AccountRecordE") val AccountRecordE: Int,
	@SerialName("DependentCount") val DependentCount: Int,
	@SerialName("IsWifeOwnBusiness") val IsWifeOwnBusiness: Boolean,
	@SerialName("SpouseNPWP") val SpouseNPWP: String?,
	@SerialName("SpouseName") val SpouseName: String?,
	@SerialName("SpouseDependentCount") val SpouseDependentCount: Int?
)