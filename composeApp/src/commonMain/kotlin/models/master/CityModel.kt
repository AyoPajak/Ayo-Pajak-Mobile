package models.master

import global.AccountRecordType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CityModel(
	@SerialName("Id") val Id: Int,
	@SerialName("ProvinceName") val ProvinceName: String,
	@SerialName("CityName") val CityName: String,
	@SerialName("CityCtgE") val CityCtgE: Int
)