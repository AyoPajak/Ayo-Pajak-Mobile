package models.transaction

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import models.master.CurrencyModel
import models.master.InterestTypeModel
import models.master.WealthTypeModel

@Serializable
data class FormWealthResponseApiModel (
	@SerialName("Id") val Id: Int,
	@SerialName("Tr1770HdId") val Tr1770HdId: Int,
	@SerialName("Seq") val Seq: Int,
	@SerialName("Wealth") val WealthType: WealthTypeModel,
	@SerialName("AcquisitionYear") val AcquisitionYear: String,
	@SerialName("Currency") val Currency: CurrencyModel?,
	@SerialName("CurrencyAmount") val CurrencyAmount: Double?,
	@SerialName("CurrRateEntryMode") val CurrRateEntryMode: Int?,
	@SerialName("CurrRateIDR") val CurrRateIDR: Double?,
	@SerialName("CurrencyAmountIDR") val CurrencyAmountIDR: Double,
	@SerialName("IsOverseas") val IsOverseas: Boolean?,
	@SerialName("Description") val Description: String?,
	
	@SerialName("StorageName") val StorageName: String?,
	@SerialName("StorageAccountNo") val StorageAccountNo: String?,
	@SerialName("StorageAddress") val StorageAddress: String?,
	@SerialName("StartDate") val StartDate: String?,
	@SerialName("DueDate") val DueDate: String?,
	@SerialName("InterestRatePerc") val InterestRatePerc: Double?,
	@SerialName("IsARO") val IsARO: Boolean?,
	@SerialName("BilyetNo") val BilyetNo: String?,
	
	@SerialName("Interest") val InterestType: InterestTypeModel?,
	
	@SerialName("DebitorName") val DebitorName: String?,
	@SerialName("Relation") val Relation: String?,
	@SerialName("DebitorAddress") val DebitorAddress: String?,
	@SerialName("InterestAmountIDR") val InterestAmountIDR: Double?,
	
	@SerialName("InventoryName") val InventoryName: String?,
	@SerialName("InventoryQty") val InventoryQty: Double?,
	@SerialName("InventoryPriceIDR") val InventoryPriceIDR: Double?,
	
	@SerialName("SharesUnitAmount") val SharesUnitAmount: Int?,
	@SerialName("SharesUnitPrice") val SharesUnitPrice: Double?,
	@SerialName("CompanyName") val CompanyName: String?,
	@SerialName("CompanyNPWP") val CompanyNPWP: String?,
	@SerialName("CVName") val CVName: String?,
	@SerialName("CVNPWP") val CVNPWP: String?,
	@SerialName("CVAddress") val CVAddress: String?,
	
	@SerialName("BrandName") val BrandName: String?,
	@SerialName("ProductType") val ProductType: String?,
	@SerialName("ProductionYear") val ProductionYear: String?,
	@SerialName("JewerlyType") val JewerlyType: String?,
	@SerialName("UnitName") val UnitName: String?,
	
	@SerialName("OwnerNo") val OwnerNo: String?,
	@SerialName("Location") val Location: String?,
	@SerialName("NOP") val NOP: String?,
	@SerialName("Area") val Area: String?,
	
	@SerialName("PatenName") val PatenName: String?,
	@SerialName("PatenNo") val PatenNo: String?,
	@SerialName("PatenStartDate") val PatenStartDate: String?,
	@SerialName("PatenDueDate") val PatenDueDate: String?,
	
	@SerialName("PolisNo") val PolisNo: String?,
	@SerialName("MainDesc") val MainDesc: String?,
)