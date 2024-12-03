package models.account

import global.Gender
import global.MaritalStatus
import global.TaxPayerType
import global.TaxStatus
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import viewmodels.UserProfileModel

@Serializable
data class ClientProfileResponseApiModel (
	@SerialName("TaxPayerTypeE")val TaxPayerTypeE: Int,
	@SerialName("WPNIK")val WPNIK: String?,
	@SerialName("WPName")val WPName: String?,
	@SerialName("GenderE")val GenderE: String?,
	@SerialName("MaritalStatusE")val MaritalStatusE: Int?,
	@SerialName("TaxStatusE")val TaxStatusE: String?,
	@SerialName("SpouseNPWP")val SpouseNPWP: String?,
	@SerialName("JobId")val JobId: Int?,
	@SerialName("KluId")val KluId: Int?,
	@SerialName("Address")val Address: String,
	@SerialName("CityId")val CityId: Int,
	@SerialName("TelephoneNo")val TelephoneNo: String,
	@SerialName("RegisterName")val RegisterName: String,
	@SerialName("JobTitle")val JobTitle: String,
	@SerialName("NPWP")val NPWP: String?,
	@SerialName("NPWP_New")val NPWP_New: String?,
	@SerialName("NITKU")val NITKU: String?,
	@SerialName("EFINNo")val EFINNo: String,
	@SerialName("UserGuid")val UserGuid: String?,
	@SerialName("ECertEFilingExpiryDate")val ECertEFilingExpiryDate: String?,
	@SerialName("ECertEFakturExpiryDate")val ECertEFakturExpiryDate: String?
) {
	fun toDataModel(): UserProfileModel {
		val result = UserProfileModel()
		result.TaxPayerTypeE = TaxPayerTypeE ?: TaxPayerType.Personal.value
		result.WPName = WPName ?: ""
		result.WPNIK = WPNIK ?: ""
		result.NPWP = NPWP ?: ""
		result.GenderE = GenderE ?: Gender.Male.value
		result.MaritalStatusE = MaritalStatusE ?: MaritalStatus.NotMarried.value
		result.TaxStatusE = TaxStatusE ?: TaxStatus.KepalaKeluarga.value
		result.SpouseNPWP = SpouseNPWP
		result.JobId = JobId ?: 0
		result.KluId = KluId ?: 0
		result.CityId = CityId ?: 0
		result.Address = Address ?: ""
		result.TelephoneNo = TelephoneNo ?: ""
		result.RegisterName = RegisterName ?: ""
		result.JobTitle = JobTitle ?: ""
		result.EFINNo = EFINNo
		result.UserGuid = UserGuid ?: ""
		result.ECertEFilingExpiryDate = ECertEFilingExpiryDate
		result.ECertEFakturExpiryDate = ECertEFakturExpiryDate
		
		return result
	}
}