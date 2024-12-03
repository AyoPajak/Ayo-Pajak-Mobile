package viewmodels

import global.Gender
import global.MaritalStatus
import global.PreferencesKey.Companion.EFINNo
import global.TaxPayerType
import global.TaxStatus
import kotlinx.datetime.LocalDateTime

class UserProfileModel {
	var TaxPayerTypeE: Int = TaxPayerType.Personal.value
	var WPName: String = ""
	var WPNIK: String = ""
	var NPWP: String = ""
	var GenderE: String = Gender.Male.value
	var MaritalStatusE: Int = MaritalStatus.NotMarried.value
	var TaxStatusE: String = TaxStatus.KepalaKeluarga.value
	var SpouseNPWP: String? = null
	var JobId: Int = 0
	var KluId: Int = 0
	var CityId: Int = 0
	var Address: String = ""
	var TelephoneNo: String = ""
	var RegisterName: String = ""
	var JobTitle: String = "Pribadi"
	var EFINNo: String? = null
	var UserGuid: String = ""
	var ECertEFilingExpiryDate: String? = null
	var ECertEFakturExpiryDate: String? = null
	
//	fun toSetupApiRequestModel() = UserProfileSetupRequestApiModel(
//		TaxPayerTypeE = TaxPayerTypeE,
//		WPName = WPName,
//		WPNIK = WPNIK,
//		NPWP = NPWP,
//		GenderE = GenderE,
//		MaritalStatusE = MaritalStatusE,
//		TaxStatusE = TaxStatusE,
//		SpouseNPWP = SpouseNPWP,
//		JobId = JobId,
//		KluId = KluId,
//		CityId = CityId,
//		Address = Address,
//		TelephoneNo = TelephoneNo,
//		RegisterName = RegisterName,
//		JobTitle = JobTitle
//	)
//	fun toEditApiRequestModel() = UserProfileEditRequestApiModel(
//		MaritalStatusE = MaritalStatusE,
//		TaxStatusE = TaxStatusE,
//		Address = Address,
//		CityId = CityId,
//		JobId = JobId,
//		KluId = KluId,
//		TelephoneNo = TelephoneNo,
//		RegisterName = RegisterName,
//		JobTitle = JobTitle,
//		SpouseNPWP = SpouseNPWP
//	)
	fun clone() : UserProfileModel {
		val result = UserProfileModel()
		result.TaxPayerTypeE = TaxPayerTypeE
		result.WPName = WPName
		result.WPNIK = WPNIK
		result.NPWP = NPWP
		result.GenderE = GenderE
		result.MaritalStatusE = MaritalStatusE
		result.TaxStatusE = TaxStatusE
		result.SpouseNPWP = SpouseNPWP
		result.JobId = JobId
		result.KluId = KluId
		result.CityId = CityId
		result.Address = Address
		result.TelephoneNo = TelephoneNo
		result.RegisterName = RegisterName
		result.EFINNo = EFINNo
		result.ECertEFilingExpiryDate = ECertEFilingExpiryDate
		result.ECertEFakturExpiryDate = ECertEFakturExpiryDate
		return result
	}
}

//class UserProfileViewModel(savedStateHandle: SavedStateHandle) : BaseViewModel<UserProfileModel>(
//	false,
//	UserProfileViewModel::class.simpleName ?: "UserProfileViewModel",
//	savedStateHandle
//) {
//	var cityList: List<CityModel> = listOf()
//	var jobList: List<JobModel> = listOf()
//	var kluList: List<KluModel> = listOf()
//}