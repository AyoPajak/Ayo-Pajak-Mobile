package global

import androidx.compose.ui.text.intl.Locale

interface IBaseEnum {
	var resourceSuffix: String
	override fun toString() : String
}

interface IBaseEnumAdapter<T> {
	fun toSpinnerAdapterList(): Map<T, String> = HashMap()
//	fun toAutoCompleteAdapterList(): List<AutoCompleteTextViewItemViewModel<T>>
}

enum class QueryKeyword(val value: String) {
	TOP("\$top"),
	SKIP("\$skip"),
	COUNT("\$count"),
	FILTER("\$filter"),
	ORDER_BY("\$orderby")
}

enum class ApiTokenType(val value: Int) {
	TaxpediaToken(1),
	AyopajakUserToken(2),
	PertamaUserToken(3)
}

enum class PertamaSptFillingStep(val value: Int) {
	Identity(10),
	Dependent(20),
	Asset(30),
	Liability(40),
	FinalIncome(110),
	NonTaxedIncome(120),
	TaxCredit(210),
	IncomeBookKeep(310),
	IncomeNonFinal(320),
	IncomeNetJob(330),
	IncomeNetOther(340),
	IncomeSpousePHMT(410),
	OtherDetail(430),
	TaxPaymentSlip(450),
	Additional(470),
	Confirm(999),
}

enum class TaxPayerType(val value: Int) {
	Personal(1),
	Corporate(3)
}

enum class Gender(val value: String) {
	Male("M"),
	Female("F")
}

enum class MaritalStatus(val value: Int) {
	NotMarried(1),
	Married(3)
}

enum class TaxStatus(val value: String) {
	KepalaKeluarga("KK"),
	HidupBerpisah("HB"),
	PisahHarta("PH"),
	MemilihTerpisahKewajibanPajak("MT")
}

enum class SptType(val value: String) {
	SPT_1770("1770"),
	SPT_1770S("1770S"),
	SPT_1770SS("1770SS")
}

enum class IncomeSource {
	WORKING,
	BUSINESS
}

enum class AccountRecordType(val value: Int) {
	BOOKKEEPING(1),
	RECORD(5),
}

enum class CityCategory(val value: Int) {
	MAIN_CAPITALS(1),
	OTHER_CAPITALS(5),
	OTHER_AREAS(10)
}

enum class CurrRateEntryMode(val value: Int) {
	TaxCurrRate(1),
	Manual(5)
}

enum class AssetApiSaveCode {
	A,
	B,
	C,
	D,
	E,
	F,
	G,
	H,
	I,
	J,
	K,
	L
}

enum class AssetCode(val value: String) : IBaseEnum {
	UANG_TUNAI("011"),
	TABUNGAN("012"),
	GIRO("013"),
	DEPOSITO("014"),
	SETARA_KAS_LAINNYA("019"),
	PIUTANG("021"),
	PIUTANG_AFILIASI("022"),
	PERSEDIAAN_USAHA("023"),
	PIUTANG_LAINNYA("029"),
	SAHAM_YANG_DIBELI_UNTUK_DIJUAL_KEMBALI("031"),
	SAHAM("032"),
	OBLIGASI_PERUSAHAAN("033"),
	OBLIGASI_PEMERINTAH_INDONESIA("034"),
	SURAT_UTANG_LAINNYA("035"),
	REKSADANA("036"),
	INSTRUMEN_DERIVATIF("037"),
	PENYERTAAN_MODAL_DALAM_PERUSAHAAN_LAIN_YANG_TIDAK_ATAS_SAHAM("038"),
	INVESTASI_LAINNYA("039"),
	SEPEDA("041"),
	SEPEDA_MOTOR("042"),
	MOBIL("043"),
	ALAT_TRANSPORTASI_LAINNYA("049"),
	LOGAM_MULIA("051"),
	BATU_MULIA("052"),
	BARANG_SENI_DAN_ANTIK("053"),
	KAPAL_PESIAR_PESAWAT_TERBANG_HELIKOPTER_JETSKI_PERALATAN_OLAH_RAGA_KHUSUS("054"),
	PERALATAN_ELEKTRONIK_FURINITURE("055"),
	HARTA_BERGERAK_LAINNYA("059"),
	TANAH_DAN_ATAU_BANGUNAN_UNTUK_TEMPAT_TINGGAL("061"),
	TANAH_DAN_ATAU_BANGUNAN_UNTUK_USAHA("062"),
	TANAH_ATAU_LAHAN_UNTUK_USAHA("063"),
	HARTA_TIDAK_GERAK_LAINNYA("069"),
	PATEN("071"),
	ROYALTI("072"),
	MEREK_DAGANG("073"),
	HARTA_TIDAK_BERWUJUD_LAINNYA("079");
	
	override var resourceSuffix: String = "asset_code_"
	
//	override fun toString(): String {
//		val applicationContext: Context = TaxpediaApplication.applicationContext()
//
//		return applicationContext.resources.getString(applicationContext.resources.getIdentifier("asset_code_${this.value}", "string", applicationContext.packageName))
//	}
//
//	companion object: IBaseEnumAdapter<String>
//	{
//		override fun toSpinnerAdapterList(): Map<String, String>
//		{
//			return values().associate {
//				it.value to it.toString()
//			}
//		}
//
//		override fun toAutoCompleteAdapterList(): List<AutoCompleteTextViewItemViewModel<String>>
//		{
//			return values().map {
//				AutoCompleteTextViewItemViewModel(it.value, it.toString())
//			}
//		}
//	}
}

enum class TaxArticle(val value: Int)
{
	Pasal25(1),
	PP46y2013(3),
	PP46y2013_PP23y2018(5),
	PP23y2018(7),
}

enum class TaxType(val value: Int) : IBaseEnum {
	Pasal21(1),
	Pasal22(3),
	Pasal23(5),
	Pasal24(7),
	Pasal26(9),
	DTP(99);
	
	override var resourceSuffix: String = "tax_type_"
	
	companion object {
		// Method to get the enum name by its value
		fun fromValue(value: Int): String? {
			when (value) {
				1 -> { return "Pasal 21" }
				3 -> { return "Pasal 22" }
				5 -> { return "Pasal 23" }
				7 -> { return "Pasal 24" }
				9 -> { return "Pasal 26" }
				99 -> { return "DTP (Ditanggung Pemerintah)" }
				
				else -> { return null }
			}
		}
	}
	
//	override fun toString(): String {
//		val applicationContext: Context = TaxpediaApplication.applicationContext()
//
//		return applicationContext.resources.getString(applicationContext.resources.getIdentifier("${resourceSuffix}${this.value}", "string", applicationContext.packageName))
//	}
//
//	companion object: IBaseEnumAdapter<Int>
//	{
//		override fun toSpinnerAdapterList(): Map<Int, String>
//		{
//			return values().associate {
//				it.value to it.toString()
//			}
//		}
//
//		override fun toAutoCompleteAdapterList(): List<AutoCompleteTextViewItemViewModel<Int>>
//		{
//			return values().map {
//				AutoCompleteTextViewItemViewModel(it.value, it.toString())
//			}
//		}
//	}
}

enum class DepositType(val value: Int) : IBaseEnum {
	MasaPPhPasal25OP(100),
	MasaPPhPasal25OP_T(101),
	BayarSKP_PPhPasal25OP(119),
	Tahunan_PPhOP(200),
	STP_PPhOP(300),
	SKPKB_PPhOP(310),
	SKPKBT_PPhOP(320),
	BayarSKP_SKK_PB(390);
	
	override var resourceSuffix: String = "deposit_type_"
	
//	override fun toString(): String {
//		val applicationContext: Context = TaxpediaApplication.applicationContext()
//
//		return applicationContext.resources.getString(applicationContext.resources.getIdentifier("${resourceSuffix}${this.value}", "string", applicationContext.packageName))
//	}
//
//	companion object: IBaseEnumAdapter<Int>
//	{
//		override fun toSpinnerAdapterList(): Map<Int, String>
//		{
//			return values().associate {
//				it.value to it.toString()
//			}
//		}
//
//		override fun toAutoCompleteAdapterList(): List<AutoCompleteTextViewItemViewModel<Int>>
//		{
//			return values().map {
//				AutoCompleteTextViewItemViewModel(it.value, it.toString())
//			}
//		}
//	}
}

enum class AuditOpinion(val value: Int) {
	REASONABLE(1),
	REASONABLEWEXCEPTION(2),
	UNREASONABLE(3),
	NOOPINION(4);
	
	var resourceSuffix: String = "audit_opinion_"
	
	companion object{
		fun fromValue(value: Int): String? {
			return when (value) {
				1 -> {
					"Wajar Tanpa Pengecualian"
				}
				
				2 -> {
					"Wajar Dengan Pengecualian"
				}
				
				3 -> {
					"Tidak Wajar"
				}
				
				4 -> {
					"Tidak Ada Opini"
				}
				
				else -> {
					null
				}
			}
		}
	}
	
//	override fun toString(): String {
//		val applicationContext: Context = TaxpediaApplication.applicationContext()
//
//		return applicationContext.resources.getString(applicationContext.resources.getIdentifier("${resourceSuffix}${this.value}", "string", applicationContext.packageName))
//	}
}

enum class IncomeGroup(val value: Int) {
	FINAL_INCOME(10),
	NONFINAL_INCOME_RECORD(20),
	NONFINAL_INCOME_BOOKKEEP(25),
	NETJOB(30),
	NETOTHER_DOMESTIC(40),
	NETOTHER_OVERSEA(50),
	NONTAXED(60);
	
	var resourceSuffix: String = "income_group_"
	
//	override fun toString(): String {
//		val applicationContext: Context = TaxpediaApplication.applicationContext()
//
//		return applicationContext.resources.getString(applicationContext.resources.getIdentifier("${resourceSuffix}${this.value}", "string", applicationContext.packageName))
//	}
}

enum class FinalIncomeType(val value: Int) : IBaseEnum {
	BUNGA_DEPOSITO_TABUNGAN_DISKONTO_SUN(10),
	BUNGA_DISKONTO_OBLIGASI(20),
	SAHAM_BE(30),
	HADIAH_UNDIAN(40),
	PESANGON_THT_PENSIUN(50),
	HONORARIUM_APBN_APBD(60),
	ALIH_HAK_TANAH_BANGUNAN(70),
	BANGUNAN_GUNA_SERAH(80),
	SEWA_TANAH_BANGUNAN(90),
	JASA_KONSTRUKSI(100),
	AGEN_BBM(110),
	BUNGA_KOPERASI(120),
	DIVIDEN(140),
	PENGHASILAN_ISTRI_DARI_1_PEMBERI_KERJA(150),
	PENGHASILAN_LAIN(160);
	
	override var resourceSuffix: String = "final_income_"
	
	companion object {
		// Method to get the enum name by its value
		fun fromValue(value: Int): String? {
			when (value) {
				10 -> { return "Bunga Deposito, Tabungan, Diskonto SBI, Surat Berharga Negara" }
				20 -> { return "Bunga/Diskonto Obligasi" }
				30 -> { return "Penjualan Saham di Bursa Efek" }
				40 -> { return "Hadiah Undian" }
				50 -> { return "Pesangon, Tunjangan Hari Tua, dan Tebusan Pensiun"}
				60 -> { return "Honorarium Atas Beban APBN/APBD" }
				70 -> { return "Pengalihan Hak Atas Tanah dan/atau Bangunan" }
				80 -> { return "Bangunan Guna Serah" }
				90 -> { return "Sewa Atas Tanah dan/atau Bangunan" }
				100 -> { return "Usaha Jasa Konstruksi" }
				110 -> { return "Penyalur / Dealer / Agen Produk BBM" }
				120 -> { return "Bunga Koperasi" }
				140 -> { return "Dividen"}
				150 -> { return "Penghasilan Isteri Bekerja" }
				160 -> { return "Penghasilan Lain" }
				else -> { return null }
			}
		}
		
		fun formTypeFromValue(value: Int): String? {
			return when (value) {
				10, 20, 30, 80, 120, 140 -> {
					"A"
				}
				
				40, 50, 60, 100, 110, 150 -> {
					"B"
				}
				
				70 -> {
					"C"
				}
				
				160 -> {
					"D"
				}
				
				90 -> {
					"E"
				}
				
				else -> {
					null
				}
			}
		}
	}
}

enum class NonTaxedIncomeType(val value: Int) : IBaseEnum {
	SUMBANGAN_HIBAH(310),
	WARISAN(320),
	LABA_CV_NONSAHAM(330),
	KLAIM_ASURANSI(340),
	BEASISWA(350),
	PENGHASILAN_LAIN_NONPAJAK(360);
	
	override var resourceSuffix: String = "non_taxed_income_"
	
	companion object {
		// Method to get the enum name by its value
		fun fromValue(value: Int): String? {
			return when (value) {
				310 -> {
					"Bantuan / Sumbangan / Hibah"
				}
				
				320 -> {
					"Warisan"
				}
				
				330 -> {
					"Bagian Laba Anggota Perseroan Komanditer Tidak Atas Saham, Persekutuan, Perkumpulan, Firma, Kongsi"
				}
				
				340 -> {
					"Klaim Asuransi Kesehatan, Kecelakaan, Jiwa, Dwiguna, Beasiswa"
				}
				
				350 -> {
					"Beasiswa"
				}
				
				360 -> {
					"Penghasilan Lain Yang Tidak Termasuk Objek Pajak"
				}
				
				else -> {
					null
				}
			}
		}
	}
	
//	override fun toString(): String {
//		val applicationContext: Context = TaxpediaApplication.applicationContext()
//
//		return applicationContext.resources.getString(applicationContext.resources.getIdentifier("${resourceSuffix}${this.value}", "string", applicationContext.packageName))
//	}
//
//	companion object: IBaseEnumAdapter<Int>
//	{
//		override fun toSpinnerAdapterList(): Map<Int, String>
//		{
//			return values().associate {
//				it.value to it.toString()
//			}
//		}
//
//		override fun toAutoCompleteAdapterList(): List<AutoCompleteTextViewItemViewModel<Int>>
//		{
//			return values().map {
//				AutoCompleteTextViewItemViewModel(it.value, it.toString())
//			}
//		}
//	}
}

enum class OtherDetailIncomeType(val value: Int) : IBaseEnum {
	PENGHASILAN_NETO_SUAMI_ISTRI(510);
	
	override var resourceSuffix: String = "other_detail_income_"
	
//	override fun toString(): String {
//		val applicationContext: Context = TaxpediaApplication.applicationContext()
//
//		return applicationContext.resources.getString(applicationContext.resources.getIdentifier("${resourceSuffix}${this.value}", "string", applicationContext.packageName))
//	}
//
//	companion object: IBaseEnumAdapter<Int>
//	{
//		override fun toSpinnerAdapterList(): Map<Int, String>
//		{
//			return values().associate {
//				it.value to it.toString()
//			}
//		}
//
//		override fun toAutoCompleteAdapterList(): List<AutoCompleteTextViewItemViewModel<Int>>
//		{
//			return values().map {
//				AutoCompleteTextViewItemViewModel(it.value, it.toString())
//			}
//		}
//	}
}

enum class NonFinalIncomeType(val value: Int) : IBaseEnum {
	DAGANG(5),
	INDUSTRI(10),
	JASA(15),
	PEKERJAAN_BEBAS(20),
	USAHA_LAINNYA(99);
	
	override var resourceSuffix: String = "non_final_income_"
	
	companion object {
		// Method to get the enum name by its value
		fun fromValue(value: Int): String? {
			return when (value) {
				5 -> {
					"Dagang"
				}
				10 -> {
					"Industri"
				}
				15 -> {
					"Jasa"
				}
				20 -> {
					"Pekerjaan Bebas"
				}
				99 -> {
					"Usaha Lainnya"
				}
				
				else -> {
					null
				}
			}
		}
	}
	
//	override fun toString(): String {
//		val applicationContext: Context = TaxpediaApplication.applicationContext()
//
//		return applicationContext.resources.getString(applicationContext.resources.getIdentifier("${resourceSuffix}${this.value}", "string", applicationContext.packageName))
//	}
//
//	companion object: IBaseEnumAdapter<Int>
//	{
//		override fun toSpinnerAdapterList(): Map<Int, String>
//		{
//			return values().associate {
//				it.value to it.toString()
//			}
//		}
//
//		override fun toAutoCompleteAdapterList(): List<AutoCompleteTextViewItemViewModel<Int>>
//		{
//			return values().map {
//				AutoCompleteTextViewItemViewModel(it.value, it.toString())
//			}
//		}
//	}
}

enum class DomesticNetIncomeType(val value: Int) : IBaseEnum {
	INTEREST(5),
	ROYALTY(10),
	RENT(15),
	AWARD_AND_GIFT(20),
	WEALTH_SALE_PROFIT(25),
	OTHER(99);
	
	override var resourceSuffix: String = "domestic_net_income_"
	
	companion object {
		// Method to get the enum name by its value
		fun fromValue(value: Int): String {
			return when (value) {
				5 -> {
					"Bunga"
				}
				10 -> {
					"Royalti"
				}
				15 -> {
					"Sewa"
				}
				20 -> {
					"Penghargaan dan Hadiah"
				}
				25 -> {
					"Keuntungan dari Penjualan/Pengalihan Harta"
				}
				99 -> {
					"Penghasilan Lainnya"
				}
				else -> {
					"-"
				}
			}
		}
		
		fun formTypeFromValue(value: Int): String? {
			return when (value) {
				5, 10, 20, 99 -> {
					"A"
				}
				
				25 -> {
					"B"
				}
				
				15 -> {
					"C"
				}
				
				else -> {
					null
				}
			}
		}
	}
	
	
	
//	override fun toString(): String {
//		val applicationContext: Context = TaxpediaApplication.applicationContext()
//
//		return applicationContext.resources.getString(applicationContext.resources.getIdentifier("${resourceSuffix}${this.value}", "string", applicationContext.packageName))
//	}
//
//	companion object: IBaseEnumAdapter<Int>
//	{
//		override fun toSpinnerAdapterList(): Map<Int, String>
//		{
//			return values().associate {
//				it.value to it.toString()
//			}
//		}
//
//		override fun toAutoCompleteAdapterList(): List<AutoCompleteTextViewItemViewModel<Int>>
//		{
//			return values().map {
//				AutoCompleteTextViewItemViewModel(it.value, it.toString())
//			}
//		}
//	}
}

enum class OverseasNetIncomeType(val value: Int) : IBaseEnum {
	RENT(215),
	WEALTH_SALE_PROFIT(225),
	OTHER(299);
	
	override var resourceSuffix: String = "overseas_net_income_"
	
	companion object {
		// Method to get the enum name by its value
		fun fromValue(value: Int): String {
			return when (value) {
				215 -> {
					"Penyewaan Aset (Luar Negeri)"
				}
				225 -> {
					"Keuntungan dari Penjualan/Pengalihan Harta (Luar Negeri)"
				}
				299 -> {
					"Penghasilan Lainnya (Luar Negeri)"
				}
				
				else -> {
					"-"
				}
			}
		}
		
		fun formTypeFromValue(value: Int): String? {
			return when (value) {
				225 -> {
					"D"
				}
				
				299 -> {
					"E"
				}
				
				215 -> {
					"F"
				}
				
				else -> {
					null
				}
			}
		}
	}
	
//	override fun toString(): String {
//		val applicationContext: Context = TaxpediaApplication.applicationContext()
//
//		return applicationContext.resources.getString(applicationContext.resources.getIdentifier("${resourceSuffix}${this.value}", "string", applicationContext.packageName))
//	}
//
//	companion object: IBaseEnumAdapter<Int>
//	{
//		override fun toSpinnerAdapterList(): Map<Int, String>
//		{
//			return values().associate {
//				it.value to it.toString()
//			}
//		}
//
//		override fun toAutoCompleteAdapterList(): List<AutoCompleteTextViewItemViewModel<Int>>
//		{
//			return values().map {
//				AutoCompleteTextViewItemViewModel(it.value, it.toString())
//			}
//		}
//	}
}

enum class InterestType(val value: String) : IBaseEnum {
	FLAT("FLAT"),
	EFEK("EFEK"),
	ANUITAS("ANUITAS"),
	NOINT("NOINT");
	
	override var resourceSuffix: String = "interest_type_"
	
//	override fun toString(): String {
//		val applicationContext: Context = TaxpediaApplication.applicationContext()
//
//		return applicationContext.resources.getString(applicationContext.resources.getIdentifier("${resourceSuffix}${this.value.lowercase(
//			Locale.ROOT)}", "string", applicationContext.packageName))
//	}
//
//	companion object: IBaseEnumAdapter<String>
//	{
//		override fun toSpinnerAdapterList(): Map<String, String>
//		{
//			return values().associate {
//				it.value to it.toString()
//			}
//		}
//
//		override fun toAutoCompleteAdapterList(): List<AutoCompleteTextViewItemViewModel<String>>
//		{
//			return values().map {
//				AutoCompleteTextViewItemViewModel(it.value, it.toString())
//			}
//		}
//	}
}

enum class LiabilityType(val value: Int) : IBaseEnum {
	BANK(101),
	CREDIT_CARD(102),
	AFFILIATE(103),
	OTHER(109);
	
	override var resourceSuffix: String = "debt_type_"
	
//	override fun toString(): String {
//		val applicationContext: Context = TaxpediaApplication.applicationContext()
//
//		return applicationContext.resources.getString(applicationContext.resources.getIdentifier("${resourceSuffix}${this.value}", "string", applicationContext.packageName))
//	}
//
//	companion object: IBaseEnumAdapter<Int>
//	{
//		override fun toSpinnerAdapterList(): Map<Int, String>
//		{
//			return values().associate {
//				it.value to it.toString()
//			}
//		}
//
//		override fun toAutoCompleteAdapterList(): List<AutoCompleteTextViewItemViewModel<Int>>
//		{
//			return values().map {
//				AutoCompleteTextViewItemViewModel(it.value, it.toString())
//			}
//		}
//	}
}

enum class JobName(val value: String) {
	Balita("Balita"),
	Pelajar("Pelajar"),
	Mahasiswa("Mahasiswa"),
	Pegawai("Pegawai"),
	PekerjaBebas("Pekerja Bebas"),
	Wiraswasta("Wiraswasta"),
	IbuRumahTangga("Ibu Rumah Tangga"),
	Lainlain("Lain-lain"),
}

enum class KluCode(val value: String) {
	PNS("96301"),
	PegawaiBUMN("96303"),
	PegawaiSwasta("96304"),
	Pensiunan("96305"),
	JasaPeroranganLainnya("96999"),
}

enum class Art25InstallmentBase(val value: Int) : IBaseEnum {
	None(0),
	TaxPayment1Per12(1),
	WP_OPPT(3),
	SeparatedAttachment(5);
	
	override var resourceSuffix: String = "art25_installment_base_"
	
//	override fun toString(): String {
//		val applicationContext: Context = TaxpediaApplication.applicationContext()
//
//		return applicationContext.resources.getString(applicationContext.resources.getIdentifier("${resourceSuffix}${this.value}", "string", applicationContext.packageName))
//	}
//
//	companion object: IBaseEnumAdapter<Int>
//	{
//		override fun toSpinnerAdapterList(): Map<Int, String>
//		{
//			return values().associate {
//				it.value to it.toString()
//			}
//		}
//
//		override fun toAutoCompleteAdapterList(): List<AutoCompleteTextViewItemViewModel<Int>>
//		{
//			return values().map {
//				AutoCompleteTextViewItemViewModel(it.value, it.toString())
//			}
//		}
//	}
}

enum class OverpaidRequest(val value: Int) : IBaseEnum {
	Restitution(1),
	ReturnedWithArt17C(5),
	ReturnedWithArt17D(7);
	
	override var resourceSuffix: String = "overpaid_request_"
	
//	override fun toString(): String {
//		val applicationContext: Context = TaxpediaApplication.applicationContext()
//
//		return applicationContext.resources.getString(applicationContext.resources.getIdentifier("${resourceSuffix}${this.value}", "string", applicationContext.packageName))
//	}
//
//	companion object: IBaseEnumAdapter<Int>
//	{
//		override fun toSpinnerAdapterList(): Map<Int, String>
//		{
//			return values().associate {
//				it.value to it.toString()
//			}
//		}
//
//		override fun toAutoCompleteAdapterList(): List<AutoCompleteTextViewItemViewModel<Int>>
//		{
//			return values().map {
//				AutoCompleteTextViewItemViewModel(it.value, it.toString())
//			}
//		}
//	}
}

enum class Authority(val value: Int) : IBaseEnum {
	Self(1),
	Representative(3);
	
	override var resourceSuffix: String = "authority_"
	
//	override fun toString(): String {
//		val applicationContext: Context = TaxpediaApplication.applicationContext()
//
//		return applicationContext.resources.getString(applicationContext.resources.getIdentifier("${resourceSuffix}${this.value}", "string", applicationContext.packageName))
//	}
//
//	companion object: IBaseEnumAdapter<Int>
//	{
//		override fun toSpinnerAdapterList(): Map<Int, String>
//		{
//			return values().associate {
//				it.value to it.toString()
//			}
//		}
//
//		override fun toAutoCompleteAdapterList(): List<AutoCompleteTextViewItemViewModel<Int>>
//		{
//			return values().map {
//				AutoCompleteTextViewItemViewModel(it.value, it.toString())
//			}
//		}
//	}
}

//enum class TaxPaymentState(val value: Int) : IBaseEnum {
//	UnderPaid(1),
//	OverPaid(3);
//
//	override var resourceSuffix: String = "tax_payment_state_"
//
//	override fun toString(): String {
//		val applicationContext: Context = TaxpediaApplication.applicationContext()
//
//		return applicationContext.resources.getString(applicationContext.resources.getIdentifier("${resourceSuffix}${this.value}", "string", applicationContext.packageName))
//	}
//
//	companion object: IBaseEnumAdapter<Int>
//	{
//		override fun toSpinnerAdapterList(): Map<Int, String>
//		{
//			return values().associate {
//				it.value to it.toString()
//			}
//		}
//
//		override fun toAutoCompleteAdapterList(): List<AutoCompleteTextViewItemViewModel<Int>>
//		{
//			return values().map {
//				AutoCompleteTextViewItemViewModel(it.value, it.toString())
//			}
//		}
//	}
//}

//enum class TaxReportStatus(val value: Int) {
//	Processing(1),
//	WaitingForBPE(3),
//	Approved(5),
//	Failed(99);
//
//	var resourceSuffix: String = "tax_report_status_"
//
//
//	override fun toString(): String {
//		val applicationContext: Context = TaxpediaApplication.applicationContext()
//
//		return applicationContext.resources.getString(applicationContext.resources.getIdentifier("${resourceSuffix}${this.value}", "string", applicationContext.packageName))
//	}
//}