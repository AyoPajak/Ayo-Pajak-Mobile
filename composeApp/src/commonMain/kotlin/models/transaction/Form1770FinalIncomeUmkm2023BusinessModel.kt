package models.transaction

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Form1770FinalIncomeUmkm2023BusinessModel(
	@SerialName("Id") val Id: Int,
	@SerialName("Tr1770HdId") val Tr1770HdId: Int?,
	@SerialName("Tr1770IncomeId") val Tr1770IncomeId: Int?,
	@SerialName("NPWP") val NPWP: String?,
	@SerialName("BusinessName") val BusinessName: String?,
	@SerialName("BusinessAddress") val BusinessAddress: String?,
	@SerialName("Kelurahan") val Kelurahan: String?,
	@SerialName("Kecamatan") val Kecamatan: String?,
	@SerialName("Provinsi") val Provinsi: String?,
	@SerialName("CityId") val CityId: Int,
	@SerialName("CityStr") val CityStr: String?,
	@SerialName("KLUId") val KLUId: Int,
	@SerialName("KLUStr") val KLUStr: String?,
	@SerialName("Bruto_1") val Bruto_1: Double, //TODO("BigDecimal")
	@SerialName("Bruto_2") val Bruto_2: Double,
	@SerialName("Bruto_3") val Bruto_3: Double,
	@SerialName("Bruto_4") val Bruto_4: Double,
	@SerialName("Bruto_5") val Bruto_5: Double,
	@SerialName("Bruto_6") val Bruto_6: Double,
	@SerialName("Bruto_7") val Bruto_7: Double,
	@SerialName("Bruto_8") val Bruto_8: Double,
	@SerialName("Bruto_9") val Bruto_9: Double,
	@SerialName("Bruto_10") val Bruto_10: Double,
	@SerialName("Bruto_11") val Bruto_11: Double,
	@SerialName("Bruto_12") val Bruto_12: Double,
	@SerialName("Total") val Total: Double
)

@Serializable
data class Form1770FinalIncomeUmkm2023BusinessRequestModel(
	@SerialName("Id") val Id: Int,
	@SerialName("Tr1770HdId") val Tr1770HdId: Int?,
	@SerialName("Tr1770IncomeId") val Tr1770IncomeId: Int?,
	@SerialName("NPWP") val NPWP: String?,
	@SerialName("BusinessName") val BusinessName: String?,
	@SerialName("BusinessAddress") val BusinessAddress: String?,
	@SerialName("Kelurahan") val Kelurahan: String?,
	@SerialName("Kecamatan") val Kecamatan: String?,
	@SerialName("Provinsi") val Provinsi: String?,
	@SerialName("CityId") val CityId: Int,
	@SerialName("CityStr") val CityStr: String?,
	@SerialName("KLUId") val KLUId: Int,
	@SerialName("KLUStr") val KLUStr: String?,
	@SerialName("Bruto_1") val Bruto_1: Long, //TODO("BigDecimal")
	@SerialName("Bruto_2") val Bruto_2: Long,
	@SerialName("Bruto_3") val Bruto_3: Long,
	@SerialName("Bruto_4") val Bruto_4: Long,
	@SerialName("Bruto_5") val Bruto_5: Long,
	@SerialName("Bruto_6") val Bruto_6: Long,
	@SerialName("Bruto_7") val Bruto_7: Long,
	@SerialName("Bruto_8") val Bruto_8: Long,
	@SerialName("Bruto_9") val Bruto_9: Long,
	@SerialName("Bruto_10") val Bruto_10: Long,
	@SerialName("Bruto_11") val Bruto_11: Long,
	@SerialName("Bruto_12") val Bruto_12: Long,
	@SerialName("Total") val Total: Long
)