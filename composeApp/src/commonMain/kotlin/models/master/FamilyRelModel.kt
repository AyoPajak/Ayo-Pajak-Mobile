package models.master

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FamilyRelModel (
	@SerialName("Id") val Id: Int,
	@SerialName("FamilyRelName") val FamilyRelName: String,
//	@SerialName("FamilyRelName_DJP") val FamilyRelName_DJP: String?
)