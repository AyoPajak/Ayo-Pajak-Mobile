package models.transaction

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AssetGroupResponseApiModel (
	@SerialName("WealthTypeId") val WealthTypeId: Int,
	@SerialName("WealthCode") val AssetCode: String,
	@SerialName("WealthName") val AssetName: String,
	@SerialName("DataCount") val DataCount: Int,
	@SerialName("Total") val Total: Double
)