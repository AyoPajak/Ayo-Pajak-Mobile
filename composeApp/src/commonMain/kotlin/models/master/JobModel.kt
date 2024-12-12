package models.master

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JobModel(
	@SerialName("Id") val Id: Int,
	@SerialName("JobCode") val JobCode: String,
	@SerialName("JobName") val JobName: String,
)