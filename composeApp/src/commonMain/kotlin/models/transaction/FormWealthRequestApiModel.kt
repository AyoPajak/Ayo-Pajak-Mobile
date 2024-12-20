package models.transaction

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FormWealthARequestApiModel (
	@SerialName("Id") val Id: Int,
	@SerialName("Tr1770HdId") val Tr1770HdId: Int,
	@SerialName("WealthTypeId") val WealthTypeId: Int,
	@SerialName("AcquisitionYear") val AcquisitionYear: Int,
	@SerialName("CurrencyId") val CurrencyId: Int,
	@SerialName("CurrencyAmount") val CurrencyAmount: Long,
	@SerialName("CurrRateEntryModeE") val CurrRateEntryModeE: Int?,
	@SerialName("CurrRateIDR") val CurrRateIDR: Long?,
	@SerialName("IsOverseas") val IsOverseas: Boolean,
	@SerialName("Description") val Description: String?,
)

@Serializable
data class FormWealthBRequestApiModel (
	@SerialName("Id") val Id: Int,
	@SerialName("Tr1770HdId") val Tr1770HdId: Int,
	@SerialName("WealthTypeId") val WealthTypeId: Int,
	@SerialName("AcquisitionYear") val AcquisitionYear: Int,
	@SerialName("CurrencyId") val CurrencyId: Int,
	@SerialName("CurrencyAmount") val CurrencyAmount: Long,
	@SerialName("CurrRateEntryModeE") val CurrRateEntryModeE: Int?,
	@SerialName("CurrRateIDR") val CurrRateIDR: Long?,
	@SerialName("IsOverseas") val IsOverseas: Boolean,
	@SerialName("StorageName") val StorageName: String?,
	@SerialName("StorageAccountNo") val StorageAccountNo: String?,
	@SerialName("StorageAddress") val StorageAddress: String?,
)

@Serializable
data class FormWealthCRequestApiModel (
	@SerialName("Id") val Id: Int,
	@SerialName("Tr1770HdId") val Tr1770HdId: Int,
	@SerialName("WealthTypeId") val WealthTypeId: Int,
	@SerialName("AcquisitionYear") val AcquisitionYear: Int,
	@SerialName("CurrencyId") val CurrencyId: Int,
	@SerialName("CurrencyAmount") val CurrencyAmount: Long,
	@SerialName("CurrRateEntryModeE") val CurrRateEntryModeE: Int?,
	@SerialName("CurrRateIDR") val CurrRateIDR: Long?,
	@SerialName("IsOverseas") val IsOverseas: Boolean,
	@SerialName("StartDate") val StartDate: String?,
	@SerialName("InterestRatePerc") val InterestRatePerc: Long?,
	@SerialName("DueDate") val DueDate: String?,
	@SerialName("IsARO") val IsARO: Boolean,
	@SerialName("StorageName") val StorageName: String?,
	@SerialName("BilyetNo") val BilyetNo: String?,
	@SerialName("StorageAddress") val StorageAddress: String?,
)

@Serializable
data class FormWealthDRequestApiModel (
	@SerialName("Id") val Id: Int,
	@SerialName("Tr1770HdId") val Tr1770HdId: Int,
	@SerialName("WealthTypeId") val WealthTypeId: Int,
	@SerialName("AcquisitionYear") val AcquisitionYear: Int,
	@SerialName("CurrencyId") val CurrencyId: Int,
	@SerialName("CurrencyAmount") val CurrencyAmount: Long,
	@SerialName("CurrRateEntryModeE") val CurrRateEntryModeE: Int?,
	@SerialName("CurrRateIDR") val CurrRateIDR: Long?,
	@SerialName("IsOverseas") val IsOverseas: Boolean,
	@SerialName("StartDate") val StartDate: String?,
	@SerialName("InterestRatePerc") val InterestRatePerc: Long?,
	@SerialName("DueDate") val DueDate: String?,
	@SerialName("InterestTypeId") val InterestTypeId: Int,
	@SerialName("DebitorName") val DebitorName: String?,
	@SerialName("Relation") val Relation: String?,
	@SerialName("DebitorAddress") val DebitorAddress: String?,
	@SerialName("InterestAmountIDR") val InterestAmountIDR: Long?,
)

@Serializable
data class FormWealthERequestApiModel (
	@SerialName("Id") val Id: Int,
	@SerialName("Tr1770HdId") val Tr1770HdId: Int,
	@SerialName("WealthTypeId") val WealthTypeId: Int,
	@SerialName("AcquisitionYear") val AcquisitionYear: Int,
	@SerialName("InventoryName") val InventoryName: String?,
	@SerialName("InventoryQty") val InventoryQty: Long?,
	@SerialName("InventoryPriceIDR") val InventoryPriceIDR: Long?,
	@SerialName("IsOverseas") val IsOverseas: Boolean,
	@SerialName("Description") val Description: String?
)

@Serializable
data class FormWealthFRequestApiModel (
	@SerialName("Id") val Id: Int,
	@SerialName("Tr1770HdId") val Tr1770HdId: Int,
	@SerialName("WealthTypeId") val WealthTypeId: Int,
	@SerialName("AcquisitionYear") val AcquisitionYear: Int,
	@SerialName("SharesUnitAmount") val SharesUnitAmount: Int?,
	@SerialName("SharesUnitPrice") val SharesUnitPrice: Long?,
	@SerialName("CurrencyId") val CurrencyId: Int,
	@SerialName("CurrRateEntryModeE") val CurrRateEntryModeE: Int?,
	@SerialName("CurrRateIDR") val CurrRateIDR: Long?,
	@SerialName("IsOverseas") val IsOverseas: Boolean,
	@SerialName("CompanyName") val CompanyName: String?,
	@SerialName("CompanyNPWP") val CompanyNPWP: String?,
	@SerialName("Description") val Description: String?,
)

@Serializable
data class FormWealthGRequestApiModel (
	@SerialName("Id") val Id: Int,
	@SerialName("Tr1770HdId") val Tr1770HdId: Int,
	@SerialName("WealthTypeId") val WealthTypeId: Int,
	@SerialName("AcquisitionYear") val AcquisitionYear: Int,
	@SerialName("CurrencyAmountIDR") val CurrencyAmountIDR: Long,
	@SerialName("IsOverseas") val IsOverseas: Boolean,
	@SerialName("CVName") val CVName: String?,
	@SerialName("CVNPWP") val CVNPWP: String?,
	@SerialName("CVAddress") val CVAddress: String?,
	@SerialName("Description") val Description: String?,
)

@Serializable
data class FormWealthHRequestApiModel (
	@SerialName("Id") val Id: Int,
	@SerialName("Tr1770HdId") val Tr1770HdId: Int,
	@SerialName("WealthTypeId") val WealthTypeId: Int,
	@SerialName("AcquisitionYear") val AcquisitionYear: Int,
	@SerialName("CurrencyAmountIDR") val CurrencyAmountIDR: Long,
	@SerialName("IsOverseas") val IsOverseas: Boolean,
	@SerialName("BrandName") val BrandName: String?,
	@SerialName("ProductType") val ProductType: String?,
	@SerialName("ProductionYear") val ProductionYear: Int?,
	@SerialName("Description") val Description: String?,
)

@Serializable
data class FormWealthIRequestApiModel (
	@SerialName("Id") val Id: Int,
	@SerialName("Tr1770HdId") val Tr1770HdId: Int,
	@SerialName("WealthTypeId") val WealthTypeId: Int,
	@SerialName("AcquisitionYear") val AcquisitionYear: Int,
	@SerialName("CurrencyAmountIDR") val CurrencyAmountIDR: Long,
	@SerialName("IsOverseas") val IsOverseas: Boolean,
	@SerialName("JewerlyType") val JewerlyType: String?,
	@SerialName("ProductType") val ProductType: String?,
	@SerialName("UnitName") val UnitName: String?,
	@SerialName("OwnerNo") val OwnerNo: String?,
)

@Serializable
data class FormWealthJRequestApiModel (
	@SerialName("Id") val Id: Int,
	@SerialName("Tr1770HdId") val Tr1770HdId: Int,
	@SerialName("WealthTypeId") val WealthTypeId: Int,
	@SerialName("AcquisitionYear") val AcquisitionYear: Int,
	@SerialName("CurrencyAmountIDR") val CurrencyAmountIDR: Long,
	@SerialName("IsOverseas") val IsOverseas: Boolean,
	@SerialName("Location") val Location: String?,
	@SerialName("NOP") val NOP: String?,
	@SerialName("Area") val Area: String?,
	@SerialName("OwnerNo") val OwnerNo: String?,
)

@Serializable
data class FormWealthKRequestApiModel (
	@SerialName("Id") val Id: Int,
	@SerialName("Tr1770HdId") val Tr1770HdId: Int,
	@SerialName("WealthTypeId") val WealthTypeId: Int,
	@SerialName("AcquisitionYear") val AcquisitionYear: Int,
	@SerialName("CurrencyAmountIDR") val CurrencyAmountIDR: Long,
	@SerialName("IsOverseas") val IsOverseas: Boolean,
	@SerialName("PatenName") val PatenName: String?,
	@SerialName("PatenNo") val PatenNo: String?,
	@SerialName("PatenStartDate") val PatenStartDate: String?,
	@SerialName("PatenDueDate") val PatenDueDate: String?,
	@SerialName("Description") val Description: String?,
)

@Serializable
data class FormWealthLRequestApiModel (
	@SerialName("Id") val Id: Int,
	@SerialName("Tr1770HdId") val Tr1770HdId: Int,
	@SerialName("WealthTypeId") val WealthTypeId: Int,
	@SerialName("AcquisitionYear") val AcquisitionYear: Int,
	@SerialName("CurrencyId") val CurrencyId: Int,
	@SerialName("CurrencyAmount") val CurrencyAmount: Long,
	@SerialName("CurrRateEntryModeE") val CurrRateEntryModeE: Int?,
	@SerialName("CurrRateIDR") val CurrRateIDR: Long?,
	@SerialName("IsOverseas") val IsOverseas: Boolean,
	@SerialName("StorageName") val StorageName: String?,
	@SerialName("PolisNo") val PolisNo: String?,
	@SerialName("StorageAddress") val StorageAddress: String?,
)