package screens.SPT

import SPT.SPTManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import ayopajakmobile.composeapp.generated.resources.Res
import ayopajakmobile.composeapp.generated.resources.arrow_back
import ayopajakmobile.composeapp.generated.resources.icon_tripledot_black
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import global.AssetApiSaveCode
import global.AssetCode
import global.Colors
import global.CurrRateEntryMode
import global.universalUIComponents.loadingPopupBox
import http.Account
import http.Interfaces
import models.transaction.Form1770HdResponseApiModel
import models.transaction.FormWealthResponseApiModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource
import util.BigDeciToString

class AssetFormScreen(val id: Int, val sptHdId: Int, val client: Account, val sptPertamaClient: Interfaces, val prefs: DataStore<Preferences>): Screen {
	
	val sptManager = SPTManager(prefs, client, sptPertamaClient)
	
	@Composable
	override fun Content() {
	
		val scope = rememberCoroutineScope()
		val navigator = LocalNavigator.currentOrThrow
		
		var showDeletePopup by remember { mutableStateOf(false) }
		
		var acquisitionYear by remember { mutableStateOf(0) }
		var currencyId by remember { mutableStateOf(0) }
		var currencyAmount by remember { mutableStateOf(0L) }
		var currRateEntryModeE by remember { mutableStateOf(CurrRateEntryMode.TaxCurrRate.value) }
		var currRateIDR by remember { mutableStateOf(0L) }
		var isOverseas by remember { mutableStateOf(false) }
		
		var description by remember { mutableStateOf("") }
		
		var storageName by remember { mutableStateOf("") }
		var storageAccountNo by remember { mutableStateOf("") }
		var storageAddress by remember { mutableStateOf("") }
		
		var startDate by remember { mutableStateOf("") }
		var interestRatePerc by remember { mutableStateOf(0L) }
		var dueDate by remember { mutableStateOf("") }
		var isARO by remember { mutableStateOf(false) }
		var bilyetNo by remember { mutableStateOf("") }
		
		var interestTypeId by remember { mutableStateOf(0) }
		var debitorName by remember { mutableStateOf("") }
		var relation by remember { mutableStateOf("") }
		var debitorAddress by remember { mutableStateOf("") }
		var interestAmmount by remember { mutableStateOf(0L) }
		
		var isReady by remember { mutableStateOf(false) }
		var formCategory by remember { mutableStateOf("") }
		
		var inventoryName by remember { mutableStateOf("") }
		var inventoryQty by remember { mutableStateOf(0L) }
		var inventoryPriceIDR by remember { mutableStateOf(0L) }
		
		var sharesUnitAmount by remember { mutableStateOf(0) }
		var sharesUnitPrice by remember { mutableStateOf(0L) }
		var companyName by remember { mutableStateOf("") }
		var companyNPWP by remember { mutableStateOf("") }
		
		var cvName by remember { mutableStateOf("") }
		var cvNPWP by remember { mutableStateOf("") }
		var cvAddress by remember { mutableStateOf("") }
		
		var brandName by remember { mutableStateOf("") }
		var productType by remember { mutableStateOf("") }
		var productionYear by remember { mutableStateOf(0) }
		
		var jewelryType by remember { mutableStateOf("") }
		var unitName by remember { mutableStateOf("") }
		var ownerNo by remember { mutableStateOf("") }
		
		var location by remember { mutableStateOf("") }
		var nop by remember { mutableStateOf("") }
		var area by remember { mutableStateOf("") }
		
		var patenName by remember { mutableStateOf("") }
		var patenNo by remember { mutableStateOf("") }
		var patenStartDate by remember { mutableStateOf("") }
		var patenDueDate by remember { mutableStateOf("") }
		
		var polisNo by remember { mutableStateOf("") }
		
		LaunchedEffect(null) {
			if(id != 0) {
				val oldAssetData = sptManager.getWealthDataById(scope, id.toString())
				
				if (oldAssetData != null) {
					when (oldAssetData.WealthType.WealthTypeCode) {
						AssetCode.UANG_TUNAI.value -> {
							formCategory = AssetApiSaveCode.A.toString()
							
							acquisitionYear = oldAssetData.AcquisitionYear.toInt()
							currencyId = oldAssetData.Currency!!.Id
							currencyAmount = BigDeciToString(oldAssetData.CurrencyAmount.toString()).toLong()
							currRateEntryModeE = oldAssetData.CurrRateEntryMode ?: CurrRateEntryMode.TaxCurrRate.value
							currRateIDR = BigDeciToString(oldAssetData.CurrRateIDR.toString()).toLong()
							isOverseas = oldAssetData.IsOverseas ?: false
							
							description = oldAssetData.Description ?: ""
						}
						AssetCode.TABUNGAN.value, AssetCode.GIRO.value -> {
							formCategory = AssetApiSaveCode.B.toString()
							
							acquisitionYear = oldAssetData.AcquisitionYear.toInt()
							currencyId = oldAssetData.Currency!!.Id
							currencyAmount = BigDeciToString(oldAssetData.CurrencyAmount.toString()).toLong()
							currRateEntryModeE = oldAssetData.CurrRateEntryMode ?: CurrRateEntryMode.TaxCurrRate.value
							currRateIDR = BigDeciToString(oldAssetData.CurrRateIDR.toString()).toLong()
							isOverseas = oldAssetData.IsOverseas ?: false
							
							storageName = oldAssetData.StorageName ?: ""
							storageAccountNo = oldAssetData.StorageAccountNo ?: ""
							storageAddress = oldAssetData.StorageAddress ?: ""
						}
						AssetCode.DEPOSITO.value -> {
							formCategory = AssetApiSaveCode.C.toString()
							
							acquisitionYear = oldAssetData.AcquisitionYear.toInt()
							currencyId = oldAssetData.Currency!!.Id
							currencyAmount = BigDeciToString(oldAssetData.CurrencyAmount.toString()).toLong()
							currRateEntryModeE = oldAssetData.CurrRateEntryMode ?: CurrRateEntryMode.TaxCurrRate.value
							currRateIDR = BigDeciToString(oldAssetData.CurrRateIDR.toString()).toLong()
							isOverseas = oldAssetData.IsOverseas ?: false
							
							startDate = oldAssetData.StartDate ?: ""
							interestRatePerc = BigDeciToString(oldAssetData.InterestRatePerc.toString()).toLong()
							dueDate = oldAssetData.DueDate ?: ""
							isARO = oldAssetData.IsARO ?: false
							bilyetNo = oldAssetData.BilyetNo ?: ""
							
							storageName = oldAssetData.StorageName ?: ""
							storageAddress = oldAssetData.StorageAddress ?: ""
						}
						AssetCode.PIUTANG.value,
						AssetCode.PIUTANG_AFILIASI.value,
						AssetCode.PIUTANG_LAINNYA.value,
						AssetCode.OBLIGASI_PERUSAHAAN.value,
						AssetCode.OBLIGASI_PEMERINTAH_INDONESIA.value,
						AssetCode.SURAT_UTANG_LAINNYA.value,
						AssetCode.INSTRUMEN_DERIVATIF.value -> {
							formCategory = AssetApiSaveCode.D.toString()
							
							acquisitionYear = oldAssetData.AcquisitionYear.toInt()
							currencyId = oldAssetData.Currency!!.Id
							currencyAmount = BigDeciToString(oldAssetData.CurrencyAmount.toString()).toLong()
							currRateEntryModeE = oldAssetData.CurrRateEntryMode ?: CurrRateEntryMode.TaxCurrRate.value
							currRateIDR = BigDeciToString(oldAssetData.CurrRateIDR.toString()).toLong()
							isOverseas = oldAssetData.IsOverseas ?: false
							
							startDate = oldAssetData.StartDate ?: ""
							interestRatePerc = BigDeciToString(oldAssetData.InterestRatePerc.toString()).toLong()
							dueDate = oldAssetData.DueDate ?: ""
							
							interestTypeId = oldAssetData.InterestType!!.Id
							debitorName = oldAssetData.DebitorName ?: ""
							relation = oldAssetData.Relation ?: ""
							debitorAddress = oldAssetData.DebitorAddress ?: ""
							interestAmmount = BigDeciToString(oldAssetData.InterestAmountIDR.toString()).toLong()
						}
						AssetCode.PERSEDIAAN_USAHA.value -> {
							formCategory = AssetApiSaveCode.E.toString()
							
							acquisitionYear = oldAssetData.AcquisitionYear.toInt()
							
							isOverseas = oldAssetData.IsOverseas ?: false
							
							description = oldAssetData.Description ?: ""
							
							inventoryName = oldAssetData.InventoryName ?: ""
							inventoryQty = BigDeciToString(oldAssetData.InventoryQty.toString()).toLong()
							inventoryPriceIDR = BigDeciToString(oldAssetData.InventoryPriceIDR.toString()).toLong()
						}
						AssetCode.SAHAM_YANG_DIBELI_UNTUK_DIJUAL_KEMBALI.value,
						AssetCode.SAHAM.value -> {
							formCategory = AssetApiSaveCode.F.toString()
							
							acquisitionYear = oldAssetData.AcquisitionYear.toInt()
							currencyId = oldAssetData.Currency!!.Id
							currRateEntryModeE = oldAssetData.CurrRateEntryMode ?: CurrRateEntryMode.TaxCurrRate.value
							currRateIDR = BigDeciToString(oldAssetData.CurrRateIDR.toString()).toLong()
							isOverseas = oldAssetData.IsOverseas ?: false
							
							sharesUnitAmount = oldAssetData.SharesUnitAmount ?: 0
							sharesUnitPrice = BigDeciToString(oldAssetData.SharesUnitPrice.toString()).toLong()
							
							companyName = oldAssetData.CompanyName ?: ""
							companyNPWP = oldAssetData.CompanyNPWP ?: ""
							
							description = oldAssetData.Description ?: ""
						}
						AssetCode.PENYERTAAN_MODAL_DALAM_PERUSAHAAN_LAIN_YANG_TIDAK_ATAS_SAHAM.value,
						AssetCode.INVESTASI_LAINNYA.value -> {
							formCategory = AssetApiSaveCode.G.toString()
							
							acquisitionYear = oldAssetData.AcquisitionYear.toInt()
							currencyAmount = BigDeciToString(oldAssetData.CurrencyAmountIDR.toString()).toLong()
							isOverseas = oldAssetData.IsOverseas ?: false
							
							cvName = oldAssetData.CVName ?: ""
							cvNPWP = oldAssetData.CVNPWP ?: ""
							cvAddress = oldAssetData.CVAddress ?: ""
							
							description = oldAssetData.Description ?: ""
						}
						AssetCode.SEPEDA.value,
						AssetCode.SEPEDA_MOTOR.value,
						AssetCode.MOBIL.value,
						AssetCode.ALAT_TRANSPORTASI_LAINNYA.value,
						AssetCode.KAPAL_PESIAR_PESAWAT_TERBANG_HELIKOPTER_JETSKI_PERALATAN_OLAH_RAGA_KHUSUS.value,
						AssetCode.PERALATAN_ELEKTRONIK_FURINITURE.value,
						AssetCode.HARTA_BERGERAK_LAINNYA.value-> {
							formCategory = AssetApiSaveCode.H.toString()
							
							acquisitionYear = oldAssetData.AcquisitionYear.toInt()
							currencyAmount = BigDeciToString(oldAssetData.CurrencyAmountIDR.toString()).toLong()
							isOverseas = oldAssetData.IsOverseas ?: false
							
							brandName = oldAssetData.BrandName ?: ""
							productType = oldAssetData.ProductType ?: ""
							productionYear = oldAssetData.ProductionYear?.toInt() ?: 0
							
							description = oldAssetData.Description ?: ""
						}
						AssetCode.LOGAM_MULIA.value,
						AssetCode.BATU_MULIA.value,
						AssetCode.BARANG_SENI_DAN_ANTIK.value -> {
							formCategory = AssetApiSaveCode.I.toString()
							
							acquisitionYear = oldAssetData.AcquisitionYear.toInt()
							currencyAmount = BigDeciToString(oldAssetData.CurrencyAmountIDR.toString()).toLong()
							isOverseas = oldAssetData.IsOverseas ?: false
							
							jewelryType = oldAssetData.JewerlyType ?: ""
							productType = oldAssetData.ProductType ?: ""
							unitName = oldAssetData.UnitName ?: ""
							ownerNo = oldAssetData.OwnerNo ?: ""
						}
						AssetCode.TANAH_DAN_ATAU_BANGUNAN_UNTUK_TEMPAT_TINGGAL.value,
						AssetCode.TANAH_DAN_ATAU_BANGUNAN_UNTUK_USAHA.value,
						AssetCode.TANAH_ATAU_LAHAN_UNTUK_USAHA.value,
						AssetCode.HARTA_TIDAK_GERAK_LAINNYA.value-> {
							formCategory = AssetApiSaveCode.J.toString()
							
							acquisitionYear = oldAssetData.AcquisitionYear.toInt()
							currencyAmount = BigDeciToString(oldAssetData.CurrencyAmountIDR.toString()).toLong()
							isOverseas = oldAssetData.IsOverseas ?: false
							
							location = oldAssetData.Location ?: ""
							nop = oldAssetData.NOP ?: ""
							area = oldAssetData.Area ?: ""
							ownerNo = oldAssetData.OwnerNo ?: ""
						}
						AssetCode.PATEN.value,
						AssetCode.ROYALTI.value,
						AssetCode.MEREK_DAGANG.value,
						AssetCode.HARTA_TIDAK_BERWUJUD_LAINNYA.value-> {
							formCategory = AssetApiSaveCode.K.toString()
							
							acquisitionYear = oldAssetData.AcquisitionYear.toInt()
							currencyAmount = BigDeciToString(oldAssetData.CurrencyAmountIDR.toString()).toLong()
							isOverseas = oldAssetData.IsOverseas ?: false
							
							patenName = oldAssetData.PatenName ?: ""
							patenNo = oldAssetData.PatenNo ?: ""
							patenStartDate = oldAssetData.PatenStartDate ?: ""
							patenDueDate = oldAssetData.PatenDueDate ?: ""
							
							description = oldAssetData.Description ?: ""
						}
						AssetCode.SETARA_KAS_LAINNYA.value,
						AssetCode.REKSADANA.value-> {
							formCategory = AssetApiSaveCode.L.toString()
							
							acquisitionYear = oldAssetData.AcquisitionYear.toInt()
							currencyId = oldAssetData.Currency!!.Id
							currencyAmount = BigDeciToString(oldAssetData.CurrencyAmount.toString()).toLong()
							currRateEntryModeE = oldAssetData.CurrRateEntryMode ?: CurrRateEntryMode.TaxCurrRate.value
							currRateIDR = BigDeciToString(oldAssetData.CurrRateIDR.toString()).toLong()
							isOverseas = oldAssetData.IsOverseas ?: false
							
							storageName = oldAssetData.StorageName ?: ""
							polisNo = oldAssetData.PolisNo ?: ""
							storageAddress = oldAssetData.StorageAddress ?: ""
						}
					}
					
					println(formCategory)
				}
				else navigator.pop()
			}
			
			isReady = true
		}
		
		loadingPopupBox(!isReady)
		
		Column(
			modifier = Modifier.fillMaxSize().background(Colors().panel)
		) {
			LazyColumn(
				modifier = Modifier.fillMaxSize()
			) {
				item {
					Row(
						modifier = Modifier.fillMaxWidth(),
						verticalAlignment = Alignment.CenterVertically
					) {
						IconButton(
							modifier = Modifier
								.padding(horizontal = 16.dp)
								.padding(vertical = 24.dp)
								.clip(CircleShape)
								.border(1.dp, Color.LightGray, CircleShape)
								.background(Colors().panel)
								.align(Alignment.CenterVertically),
							onClick = {
								navigator.pop()
							}
						) {
							Icon(
								modifier = Modifier.scale(1.2f),
								imageVector = vectorResource(Res.drawable.arrow_back), contentDescription = null
							)
						}
						
						Text(
							text = if (id == 0) "Tambah Tanggungan" else "Edit Tanggungan",
							fontWeight = FontWeight.Bold,
							fontSize = 16.sp,
							color = Colors().textBlack,
							modifier = Modifier.align(Alignment.CenterVertically).padding(end = 120.dp)
						)
						
						if (id != 0) {
							Image(
								painter = painterResource(Res.drawable.icon_tripledot_black),
								null,
								modifier = Modifier
									.padding(end = 16.dp)
									.clickable(true, onClick = {
										showDeletePopup = true
									})
							)
						}
					}
				}
			}
		}
	}
}