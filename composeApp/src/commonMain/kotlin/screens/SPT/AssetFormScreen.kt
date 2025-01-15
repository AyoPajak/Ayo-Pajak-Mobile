package screens.SPT

import SPT.SPTManager
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults.textFieldColors
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import ayopajakmobile.composeapp.generated.resources.Icon_Dropdown_Arrow
import ayopajakmobile.composeapp.generated.resources.Res
import ayopajakmobile.composeapp.generated.resources.arrow_back
import ayopajakmobile.composeapp.generated.resources.icon_calendar
import ayopajakmobile.composeapp.generated.resources.icon_clear
import ayopajakmobile.composeapp.generated.resources.icon_search
import ayopajakmobile.composeapp.generated.resources.icon_tripledot_black
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import global.AssetApiSaveCode
import global.AssetCode
import global.Colors
import global.CurrRateEntryMode
import global.Gender
import global.universalUIComponents.loadingPopupBox
import global.universalUIComponents.popUpBox
import http.Account
import http.Interfaces
import kotlinx.coroutines.launch
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import models.master.CurrencyModel
import models.master.CurrencyRateModel
import models.master.InterestTypeModel
import models.master.WealthTypeModel
import models.transaction.Form1770HdResponseApiModel
import models.transaction.FormDependentRequestApiModel
import models.transaction.FormWealthARequestApiModel
import models.transaction.FormWealthBRequestApiModel
import models.transaction.FormWealthCRequestApiModel
import models.transaction.FormWealthDRequestApiModel
import models.transaction.FormWealthFRequestApiModel
import models.transaction.FormWealthGRequestApiModel
import models.transaction.FormWealthHRequestApiModel
import models.transaction.FormWealthIRequestApiModel
import models.transaction.FormWealthJRequestApiModel
import models.transaction.FormWealthKRequestApiModel
import models.transaction.FormWealthLRequestApiModel
import network.chaintech.kmp_date_time_picker.ui.datepicker.WheelDatePickerComponent.WheelDatePicker
import network.chaintech.kmp_date_time_picker.utils.now
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource
import util.BigDeciToLong
import util.BigDeciToString
import util.CurrencyFormatter
import util.DateFormatter

class AssetFormScreen(val id: Int, val sptHd: Form1770HdResponseApiModel?, val client: Account, val sptPertamaClient: Interfaces, val prefs: DataStore<Preferences>): Screen {
	
	private val sptManager = SPTManager(prefs, client, sptPertamaClient)
	
	@Composable
	override fun Content() {
	
		val scope = rememberCoroutineScope()
		val navigator = LocalNavigator.currentOrThrow
		
		var showWealthTypePopup by remember { mutableStateOf(false) }
		var wealthTypeSearchText by remember { mutableStateOf("")}
		
		var showDeletePopup by remember { mutableStateOf(false) }
		var showConfirmPopup by remember { mutableStateOf(false) }
		
		var showCurrencyPopup by remember { mutableStateOf(false) }
		var currencSearchText by remember { mutableStateOf("") }
		
		var showStartDatePopup by remember { mutableStateOf(false) }
		var showDueDatePopup by remember { mutableStateOf(false) }
		
		var showPatenStartPopup by remember { mutableStateOf(false) }
		var showPatenEndPopup by remember { mutableStateOf(false) }
		
		var showInterestPopup by remember { mutableStateOf(false) }
		var interestSearchText by remember { mutableStateOf("") }
		
		var wealthTypeList by remember { mutableStateOf<List<WealthTypeModel>>(ArrayList()) }
		var selectedWealthType by remember { mutableStateOf("") }
		var wealthTypeId by remember { mutableStateOf(0) }
		
		var currencyList by remember { mutableStateOf<List<CurrencyModel>>(emptyList()) }
		var selectedCurrency by remember { mutableStateOf("(IDR) Indonesian rupiah") }
		
		var currencyRateList by remember { mutableStateOf<List<CurrencyRateModel>>(emptyList()) }
		
		var interestTypeList by remember { mutableStateOf<List<InterestTypeModel>>(emptyList()) }
		var selectedInterest by remember { mutableStateOf("") }
		
		var isReady by remember { mutableStateOf(false) }
		var formCategory by remember { mutableStateOf("") }
		
		var acquisitionYear by remember { mutableStateOf("") }
		var isOverseas by remember { mutableStateOf(false) }
		
		var currencyAmountEntry by remember { mutableStateOf("") }
		var currencyId by remember { mutableStateOf(61) }
		var currencyAmount by remember { mutableStateOf("") }
		var currRateEntryModeE by remember { mutableStateOf(CurrRateEntryMode.Manual.value) }
		var currRateIDR by remember { mutableStateOf("1") }
		
		var description by remember { mutableStateOf("") }
		
		var storageName by remember { mutableStateOf("") }
		var storageAccountNo by remember { mutableStateOf("") }
		var storageAddress by remember { mutableStateOf("") }
		
		var startDate by remember { mutableStateOf("") }
		var interestRatePerc by remember { mutableStateOf("0") }
		var dueDate by remember { mutableStateOf("") }
		var isARO by remember { mutableStateOf(false) }
		var bilyetNo by remember { mutableStateOf("") }
		
		var interestTypeId by remember { mutableStateOf<Int?>(null) }
		var debitorName by remember { mutableStateOf("") }
		var relation by remember { mutableStateOf("") }
		var debitorAddress by remember { mutableStateOf("") }
		var interestAmmount by remember { mutableStateOf("") }
		
		var inventoryName by remember { mutableStateOf("") }
		var inventoryQty by remember { mutableStateOf(0L) }
		var inventoryPriceIDR by remember { mutableStateOf(0L) }
		
		var sharesUnitAmount by remember { mutableStateOf("0") }
		var sharesUnitPrice by remember { mutableStateOf("") }
		var companyName by remember { mutableStateOf("") }
		var companyNPWP by remember { mutableStateOf("") }
		
		var cvName by remember { mutableStateOf("") }
		var cvNPWP by remember { mutableStateOf("") }
		var cvAddress by remember { mutableStateOf("") }
		
		var brandName by remember { mutableStateOf("") }
		var productType by remember { mutableStateOf("") }
		var productionYear by remember { mutableStateOf("") }
		
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
			wealthTypeList = sptManager.getWealthTypeList(scope) ?: listOf()
			currencyList = sptManager.getCurrencyList(scope) ?: listOf()
			currencyRateList = sptManager.getCurrencyRate(scope, sptHd!!.TaxYear) ?: listOf()
			interestTypeList = sptManager.getInterestTypeList(scope) ?: listOf()
			
			if(id != 0) {
				val oldAssetData = sptManager.getWealthDataById(scope, id.toString())
				println(oldAssetData)
				
				if (oldAssetData != null) {
					wealthTypeId = oldAssetData.Id
					selectedWealthType = "${oldAssetData.WealthType.WealthTypeCode} - ${oldAssetData.WealthType.WealthTypeName}"
					
					when (oldAssetData.WealthType.WealthTypeCode) {
						//A
						AssetCode.UANG_TUNAI.value -> {
							formCategory = AssetApiSaveCode.A.toString()
							
							acquisitionYear = oldAssetData.AcquisitionYear
							currencyId = oldAssetData.Currency!!.Id
							currencyAmount = BigDeciToString(oldAssetData.CurrencyAmount.toString())
							currRateEntryModeE = oldAssetData.CurrRateEntryMode ?: CurrRateEntryMode.TaxCurrRate.value
							currRateIDR = BigDeciToString(oldAssetData.CurrRateIDR.toString())
							isOverseas = oldAssetData.IsOverseas ?: false
							
							description = oldAssetData.Description ?: ""
							
							currencyAmountEntry = (currencyAmount.toLong() / currRateIDR.toLong()).toString()
						}
						
						//B
						AssetCode.TABUNGAN.value, AssetCode.GIRO.value -> {
							formCategory = AssetApiSaveCode.B.toString()
							
							acquisitionYear = oldAssetData.AcquisitionYear
							currencyId = oldAssetData.Currency!!.Id
							currencyAmount = BigDeciToString(oldAssetData.CurrencyAmount.toString())
							currRateEntryModeE = oldAssetData.CurrRateEntryMode ?: CurrRateEntryMode.TaxCurrRate.value
							currRateIDR = BigDeciToString(oldAssetData.CurrRateIDR.toString())
							isOverseas = oldAssetData.IsOverseas ?: false
							
							storageName = oldAssetData.StorageName ?: ""
							storageAccountNo = oldAssetData.StorageAccountNo ?: ""
							storageAddress = oldAssetData.StorageAddress ?: ""
							
							currencyAmountEntry = (currencyAmount.toLong() / currRateIDR.toLong()).toString()
						}
						
						//C
						AssetCode.DEPOSITO.value -> {
							formCategory = AssetApiSaveCode.C.toString()
							
							acquisitionYear = oldAssetData.AcquisitionYear
							currencyId = oldAssetData.Currency!!.Id
							currencyAmount = BigDeciToString(oldAssetData.CurrencyAmount.toString())
							currRateEntryModeE = oldAssetData.CurrRateEntryMode ?: CurrRateEntryMode.TaxCurrRate.value
							currRateIDR = BigDeciToString(oldAssetData.CurrRateIDR.toString())
							isOverseas = oldAssetData.IsOverseas ?: false
							
							startDate = oldAssetData.StartDate ?: ""
							interestRatePerc = BigDeciToString(oldAssetData.InterestRatePerc.toString())
							dueDate = oldAssetData.DueDate ?: ""
							isARO = oldAssetData.IsARO ?: false
							bilyetNo = oldAssetData.BilyetNo ?: ""
							
							storageName = oldAssetData.StorageName ?: ""
							storageAddress = oldAssetData.StorageAddress ?: ""
							
							currencyAmountEntry = (currencyAmount.toLong() / currRateIDR.toLong()).toString()
						}
						
						//D
						AssetCode.PIUTANG.value,
						AssetCode.PIUTANG_AFILIASI.value,
						AssetCode.PIUTANG_LAINNYA.value,
						AssetCode.OBLIGASI_PERUSAHAAN.value,
						AssetCode.OBLIGASI_PEMERINTAH_INDONESIA.value,
						AssetCode.SURAT_UTANG_LAINNYA.value,
						AssetCode.INSTRUMEN_DERIVATIF.value -> {
							formCategory = AssetApiSaveCode.D.toString()
							
							acquisitionYear = oldAssetData.AcquisitionYear
							currencyId = oldAssetData.Currency!!.Id
							currencyAmount = BigDeciToString(oldAssetData.CurrencyAmount.toString())
							currRateEntryModeE = oldAssetData.CurrRateEntryMode ?: CurrRateEntryMode.TaxCurrRate.value
							currRateIDR = BigDeciToString(oldAssetData.CurrRateIDR.toString())
							isOverseas = oldAssetData.IsOverseas ?: false
							
							startDate = oldAssetData.StartDate ?: ""
							interestRatePerc = BigDeciToString(oldAssetData.InterestRatePerc.toString())
							dueDate = oldAssetData.DueDate ?: ""
							
							interestTypeId = oldAssetData.InterestType?.Id
							debitorName = oldAssetData.DebitorName ?: ""
							relation = oldAssetData.Relation ?: ""
							debitorAddress = oldAssetData.DebitorAddress ?: ""
							interestAmmount = BigDeciToString(oldAssetData.InterestAmountIDR.toString())
						}
						
						//E
						AssetCode.PERSEDIAAN_USAHA.value -> {
							formCategory = AssetApiSaveCode.E.toString()
							
							acquisitionYear = oldAssetData.AcquisitionYear
							
							isOverseas = oldAssetData.IsOverseas ?: false
							
							description = oldAssetData.Description ?: ""
							
							inventoryName = oldAssetData.InventoryName ?: ""
							inventoryQty = BigDeciToString(oldAssetData.InventoryQty.toString()).toLong()
							inventoryPriceIDR = BigDeciToString(oldAssetData.InventoryPriceIDR.toString()).toLong()
						}
						
						//F
						AssetCode.SAHAM_YANG_DIBELI_UNTUK_DIJUAL_KEMBALI.value,
						AssetCode.SAHAM.value -> {
							formCategory = AssetApiSaveCode.F.toString()
							
							acquisitionYear = oldAssetData.AcquisitionYear
							currencyId = oldAssetData.Currency!!.Id
							currRateEntryModeE = oldAssetData.CurrRateEntryMode ?: CurrRateEntryMode.TaxCurrRate.value
							currRateIDR = BigDeciToString(oldAssetData.CurrRateIDR.toString())
							isOverseas = oldAssetData.IsOverseas ?: false
							
							sharesUnitAmount = (oldAssetData.SharesUnitAmount ?: 0).toString()
							sharesUnitPrice = BigDeciToString(oldAssetData.SharesUnitPrice.toString())
							
							companyName = oldAssetData.CompanyName ?: ""
							companyNPWP = oldAssetData.CompanyNPWP ?: ""
							
							description = oldAssetData.Description ?: ""
						}
						
						//G
						AssetCode.PENYERTAAN_MODAL_DALAM_PERUSAHAAN_LAIN_YANG_TIDAK_ATAS_SAHAM.value,
						AssetCode.INVESTASI_LAINNYA.value -> {
							formCategory = AssetApiSaveCode.G.toString()
							
							acquisitionYear = oldAssetData.AcquisitionYear
							currencyAmount = BigDeciToString(oldAssetData.CurrencyAmountIDR.toString())
							isOverseas = oldAssetData.IsOverseas ?: false
							
							cvName = oldAssetData.CVName ?: ""
							cvNPWP = oldAssetData.CVNPWP ?: ""
							cvAddress = oldAssetData.CVAddress ?: ""
							
							description = oldAssetData.Description ?: ""
						}
						
						//H
						AssetCode.SEPEDA.value,
						AssetCode.SEPEDA_MOTOR.value,
						AssetCode.MOBIL.value,
						AssetCode.ALAT_TRANSPORTASI_LAINNYA.value,
						AssetCode.KAPAL_PESIAR_PESAWAT_TERBANG_HELIKOPTER_JETSKI_PERALATAN_OLAH_RAGA_KHUSUS.value,
						AssetCode.PERALATAN_ELEKTRONIK_FURINITURE.value,
						AssetCode.HARTA_BERGERAK_LAINNYA.value-> {
							formCategory = AssetApiSaveCode.H.toString()
							
							acquisitionYear = oldAssetData.AcquisitionYear
							currencyAmount = BigDeciToString(oldAssetData.CurrencyAmountIDR.toString())
							isOverseas = oldAssetData.IsOverseas ?: false
							
							brandName = oldAssetData.BrandName ?: ""
							productType = oldAssetData.ProductType ?: ""
							productionYear = oldAssetData.ProductionYear ?: ""
							
							description = oldAssetData.Description ?: ""
						}
						
						//I
						AssetCode.LOGAM_MULIA.value,
						AssetCode.BATU_MULIA.value,
						AssetCode.BARANG_SENI_DAN_ANTIK.value -> {
							formCategory = AssetApiSaveCode.I.toString()
							
							acquisitionYear = oldAssetData.AcquisitionYear
							currencyAmount = BigDeciToString(oldAssetData.CurrencyAmountIDR.toString())
							isOverseas = oldAssetData.IsOverseas ?: false
							
							jewelryType = oldAssetData.JewerlyType ?: ""
							productType = oldAssetData.ProductType ?: ""
							unitName = oldAssetData.UnitName ?: ""
							ownerNo = oldAssetData.OwnerNo ?: ""
						}
						
						//J
						AssetCode.TANAH_DAN_ATAU_BANGUNAN_UNTUK_TEMPAT_TINGGAL.value,
						AssetCode.TANAH_DAN_ATAU_BANGUNAN_UNTUK_USAHA.value,
						AssetCode.TANAH_ATAU_LAHAN_UNTUK_USAHA.value,
						AssetCode.HARTA_TIDAK_GERAK_LAINNYA.value-> {
							formCategory = AssetApiSaveCode.J.toString()
							
							acquisitionYear = oldAssetData.AcquisitionYear
							currencyAmount = BigDeciToString(oldAssetData.CurrencyAmountIDR.toString())
							isOverseas = oldAssetData.IsOverseas ?: false
							
							location = oldAssetData.Location ?: ""
							nop = oldAssetData.NOP ?: ""
							area = oldAssetData.Area ?: ""
							ownerNo = oldAssetData.OwnerNo ?: ""
						}
						
						//K
						AssetCode.PATEN.value,
						AssetCode.ROYALTI.value,
						AssetCode.MEREK_DAGANG.value,
						AssetCode.HARTA_TIDAK_BERWUJUD_LAINNYA.value-> {
							formCategory = AssetApiSaveCode.K.toString()
							
							acquisitionYear = oldAssetData.AcquisitionYear
							currencyAmount = BigDeciToString(oldAssetData.CurrencyAmountIDR.toString())
							isOverseas = oldAssetData.IsOverseas ?: false
							
							patenName = oldAssetData.PatenName ?: ""
							patenNo = oldAssetData.PatenNo ?: ""
							patenStartDate = oldAssetData.PatenStartDate ?: ""
							patenDueDate = oldAssetData.PatenDueDate ?: ""
							
							description = oldAssetData.Description ?: ""
						}
						
						//L
						AssetCode.SETARA_KAS_LAINNYA.value,
						AssetCode.REKSADANA.value-> {
							formCategory = AssetApiSaveCode.L.toString()
							
							acquisitionYear = oldAssetData.AcquisitionYear
							currencyId = oldAssetData.Currency!!.Id
							currencyAmount = BigDeciToString(oldAssetData.CurrencyAmount.toString())
							currRateEntryModeE = oldAssetData.CurrRateEntryMode ?: CurrRateEntryMode.TaxCurrRate.value
							currRateIDR = BigDeciToString(oldAssetData.CurrRateIDR.toString())
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
		
		//Functions
		fun updateTotal(entry: Long = 0L, rate: Long = 1L): Long {
			currencyAmount = (entry * rate).toString()
			return currencyAmount.toLong()
		}
		fun checkRateAvailable(): Boolean {
			currencyRateList.forEach {
				if (it.Currency.Id == currencyId) {
					return true
				}
			}
			
			return false
		}
		fun getSelectedRate(): Long {
			currencyRateList.forEach {
				if (it.Currency.Id == currencyId) {
					return BigDeciToLong(it.IDRRate.toString())
				}
			}
			return 1L
		}
		
		//Text Fields
		//Acquisition Year
		@Composable
		fun acquisitionYearTextField() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top= 8.dp),
				text = "Tahun Perolehan",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			TextField(
				enabled = true,
				modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
					.padding(horizontal = 16.dp)
					.border(
						border = BorderStroke(1.dp, Colors().textDarkGrey),
						shape = RoundedCornerShape(4.dp)
					),
				placeholder = {
					Text(
						text = "1980",
						fontSize = 16.sp,
						color = Colors().textDarkGrey
					)
				},
				value = acquisitionYear,
				onValueChange = {
					acquisitionYear = it
				},
				singleLine = true,
				colors = textFieldColors(
					backgroundColor = Color.White,
					focusedIndicatorColor = Color.Transparent,
					unfocusedIndicatorColor = Color.Transparent,
					disabledIndicatorColor = Colors().slate20
				),
				keyboardOptions = KeyboardOptions(
					keyboardType = KeyboardType.Number,
					imeAction = ImeAction.Next
				)
			)
		}
		
		//Currency Selector
		@Composable
		fun currencySelector() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Mata Uang",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			
			Box(
				modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
					.background(Color.White)
					.border(1.dp, Colors().textDarkGrey, RoundedCornerShape(4.dp))
					.clickable(true, onClick = {
						showCurrencyPopup = true
					})
			) {
				Row (
					modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
					horizontalArrangement = Arrangement.SpaceBetween,
					verticalAlignment = Alignment.CenterVertically
				){
					Text(
						text = if(selectedCurrency == "") "(IDR) Indonesian rupiah" else selectedCurrency,
						fontSize = 14.sp,
						color = if(selectedCurrency == "") Colors().textDarkGrey else Colors().textBlack
					)
					Image(painterResource(Res.drawable.Icon_Dropdown_Arrow), null, modifier = Modifier.size(24.dp))
				}
			}
		}
		
		@Composable
		fun currencyEntryTextField() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Nilai Mata Uang",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			TextField(
				enabled = true,
				modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
					.padding(horizontal = 16.dp)
					.border(
						border = BorderStroke(1.dp, Colors().textDarkGrey),
						shape = RoundedCornerShape(4.dp)
					),
				placeholder = {
					Text(
						text = "0",
						fontSize = 16.sp,
						color = Colors().textDarkGrey
					)
				},
				value = currencyAmountEntry,
				onValueChange = {
					currencyAmountEntry = it
				},
				singleLine = true,
				colors = textFieldColors(
					backgroundColor = Color.White,
					focusedIndicatorColor = Color.Transparent,
					unfocusedIndicatorColor = Color.Transparent,
					disabledIndicatorColor = Colors().slate20
				),
				keyboardOptions = KeyboardOptions(
					keyboardType = KeyboardType.Number,
					imeAction = ImeAction.Next
				)
			)
		}
		
		@Composable
		fun currRateEntryModeCheckbox() {
			Column(
				modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
			) {
				Text(
					text = "Jenis Nilai Tukar Mata Uang",
					fontSize = 12.sp,
					color = Color.Black,
					fontWeight = FontWeight.Bold
				)
				Row(modifier = Modifier.selectableGroup().fillMaxWidth()){
					Row(modifier = Modifier.weight(.5f), verticalAlignment = Alignment.CenterVertically) {
						RadioButton(
							enabled = checkRateAvailable(),
							selected = currRateEntryModeE == CurrRateEntryMode.TaxCurrRate.value,
							onClick = { currRateEntryModeE = CurrRateEntryMode.TaxCurrRate.value },
							colors = RadioButtonDefaults.colors(selectedColor = Colors().buttonActive)
						)
						Text(
							text = "Kurs Tengah BI (akhir tahun)",
							fontSize = 14.sp,
							color = if(checkRateAvailable()) Colors().textBlack else Colors().textDarkGrey,
							modifier = Modifier.clickable(checkRateAvailable(), onClick = {
								currRateEntryModeE = CurrRateEntryMode.TaxCurrRate.value
							})
						)
					}
					Row(modifier = Modifier.weight(.5f), verticalAlignment = Alignment.CenterVertically) {
						RadioButton(
							enabled = !checkRateAvailable(),
							selected = currRateEntryModeE == CurrRateEntryMode.Manual.value,
							onClick = { currRateEntryModeE = CurrRateEntryMode.Manual.value },
							colors = RadioButtonDefaults.colors(selectedColor = Colors().buttonActive)
						)
						Text(
							text = "Manual",
							fontSize = 14.sp,
							color = if(!checkRateAvailable()) Colors().textBlack else Colors().textDarkGrey,
							modifier = Modifier.clickable(!checkRateAvailable(), onClick = {
								currRateEntryModeE = CurrRateEntryMode.Manual.value
							})
						)
					}
				}
			}
		}
		
		@Composable
		fun currencyRateTextField() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Nilai Kurs",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			TextField(
				enabled = currRateEntryModeE == CurrRateEntryMode.Manual.value,
				modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
					.padding(horizontal = 16.dp)
					.border(
						border = BorderStroke(1.dp, Colors().textDarkGrey),
						shape = RoundedCornerShape(4.dp)
					),
				placeholder = {
					Text(
						text = "1",
						fontSize = 16.sp,
						color = Colors().textDarkGrey
					)
				},
				value = currRateIDR,
				onValueChange = {
					currRateIDR = it
				},
				singleLine = true,
				colors = textFieldColors(
					backgroundColor = Color.White,
					focusedIndicatorColor = Color.Transparent,
					unfocusedIndicatorColor = Color.Transparent,
					disabledIndicatorColor = Colors().slate20
				),
				keyboardOptions = KeyboardOptions(
					keyboardType = KeyboardType.Number,
					imeAction = ImeAction.Next
				)
			)
		}
		
		@Composable
		fun currencyAmountTextField() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Total",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			Box(
				modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
					.background(Colors().slate20, RoundedCornerShape(4.dp))
					.border(1.dp, Colors().textDarkGrey, RoundedCornerShape(4.dp))
			) {
				Row (
					modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
					horizontalArrangement = Arrangement.SpaceBetween,
					verticalAlignment = Alignment.CenterVertically
				){
					Text(
						text = if(currencyAmountEntry.isNotBlank() && currRateIDR.isNotBlank() && currRateIDR != "0") "Rp ${CurrencyFormatter(BigDeciToString(updateTotal(currencyAmountEntry.toLong(), currRateIDR.toLong()).toString()))}" else "Rp 0",
						fontSize = 14.sp,
						color = Colors().textDarkGrey
					)
				}
			}
		}
		
		@Composable
		fun description() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Keterangan",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			TextField(
				enabled = true,
				modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
					.padding(horizontal = 16.dp)
					.border(
						border = BorderStroke(1.dp, Colors().textDarkGrey),
						shape = RoundedCornerShape(4.dp)
					),
				placeholder = {
					Text(
						text = "Keterangan",
						fontSize = 16.sp,
						color = Colors().textDarkGrey
					)
				},
				value = description,
				onValueChange = {
					if(it.length <= 255) { description = it }
				},
				singleLine = true,
				colors = textFieldColors(
					backgroundColor = Color.White,
					focusedIndicatorColor = Color.Transparent,
					unfocusedIndicatorColor = Color.Transparent,
					disabledIndicatorColor = Colors().slate20
				),
				keyboardOptions = KeyboardOptions(
					keyboardType = KeyboardType.Text,
					imeAction = ImeAction.Next
				)
			)
		}
		
		@Composable
		fun storageNameTextField() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Nama Bank / Lokasi Penyimpanan",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			TextField(
				enabled = true,
				modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
					.padding(horizontal = 16.dp)
					.border(
						border = BorderStroke(1.dp, Colors().textDarkGrey),
						shape = RoundedCornerShape(4.dp)
					),
				placeholder = {
					Text(
						text = "Nama Bank",
						fontSize = 16.sp,
						color = Colors().textDarkGrey
					)
				},
				value = storageName,
				onValueChange = {
					if(it.length <= 255) { storageName = it }
				},
				singleLine = true,
				colors = textFieldColors(
					backgroundColor = Color.White,
					focusedIndicatorColor = Color.Transparent,
					unfocusedIndicatorColor = Color.Transparent,
					disabledIndicatorColor = Colors().slate20
				),
				keyboardOptions = KeyboardOptions(
					keyboardType = KeyboardType.Text,
					imeAction = ImeAction.Next
				)
			)
		}
		
		@Composable
		fun storageAccountNoTextField() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "No. Rekening / BG / Polis",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			TextField(
				enabled = true,
				modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
					.padding(horizontal = 16.dp)
					.border(
						border = BorderStroke(1.dp, Colors().textDarkGrey),
						shape = RoundedCornerShape(4.dp)
					),
				placeholder = {
					Text(
						text = "0000000000",
						fontSize = 16.sp,
						color = Colors().textDarkGrey
					)
				},
				value = storageAccountNo,
				onValueChange = {
					if(it.length <= 100) { storageAccountNo = it }
				},
				singleLine = true,
				colors = textFieldColors(
					backgroundColor = Color.White,
					focusedIndicatorColor = Color.Transparent,
					unfocusedIndicatorColor = Color.Transparent,
					disabledIndicatorColor = Colors().slate20
				),
				keyboardOptions = KeyboardOptions(
					keyboardType = KeyboardType.Number,
					imeAction = ImeAction.Next
				)
			)
		}
		
		@Composable
		fun storageAddressTextField() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Alamat Penyimpanan",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			TextField(
				enabled = true,
				modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
					.padding(horizontal = 16.dp)
					.border(
						border = BorderStroke(1.dp, Colors().textDarkGrey),
						shape = RoundedCornerShape(4.dp)
					),
				placeholder = {
					Text(
						text = "Jalan Hidup Bahagia",
						fontSize = 16.sp,
						color = Colors().textDarkGrey
					)
				},
				value = storageAddress,
				onValueChange = {
					if(it.length <= 255) { storageAddress = it }
				},
				singleLine = true,
				colors = textFieldColors(
					backgroundColor = Color.White,
					focusedIndicatorColor = Color.Transparent,
					unfocusedIndicatorColor = Color.Transparent,
					disabledIndicatorColor = Colors().slate20
				),
				keyboardOptions = KeyboardOptions(
					keyboardType = KeyboardType.Text,
					imeAction = ImeAction.Next
				)
			)
		}
		
		@Composable
		fun startDatePicker() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Tanggal Mulai",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			Box(
				modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
//							.background(Colors().slate20, RoundedCornerShape(4.dp))
					.border(1.dp, Colors().textDarkGrey, RoundedCornerShape(4.dp))
					.clickable(true, onClick = {
						showStartDatePopup = true
					})
			) {
				Row (
					modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
					horizontalArrangement = Arrangement.SpaceBetween,
					verticalAlignment = Alignment.CenterVertically
				){
					Text(
						text = if(startDate == "") "Pilih tanggal mulai" else DateFormatter(startDate),
						fontSize = 14.sp,
						color = if(startDate == "") Colors().textDarkGrey else Colors().textBlack
					)
					Image(painterResource(Res.drawable.icon_calendar), null, modifier = Modifier.size(24.dp))
				}
			}
		}
		
		@Composable
		fun dueDatePicker() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Tanggal Jatuh Tempo",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			Box(
				modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
//							.background(Colors().slate20, RoundedCornerShape(4.dp))
					.border(1.dp, Colors().textDarkGrey, RoundedCornerShape(4.dp))
					.clickable(true, onClick = {
						showDueDatePopup = true
					})
			) {
				Row (
					modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
					horizontalArrangement = Arrangement.SpaceBetween,
					verticalAlignment = Alignment.CenterVertically
				){
					Text(
						text = if(dueDate == "") "Pilih tanggal jatuh tempo" else DateFormatter(dueDate),
						fontSize = 14.sp,
						color = if(dueDate == "") Colors().textDarkGrey else Colors().textBlack
					)
					Image(painterResource(Res.drawable.icon_calendar), null, modifier = Modifier.size(24.dp))
				}
			}
		}
		
		@Composable
		fun interestRatePercTextField() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Suku Bunga (%)",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			TextField(
				enabled = true,
				modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
					.padding(horizontal = 16.dp)
					.border(
						border = BorderStroke(1.dp, Colors().textDarkGrey),
						shape = RoundedCornerShape(4.dp)
					),
				placeholder = {
					Text(
						text = "%",
						fontSize = 16.sp,
						color = Colors().textDarkGrey
					)
				},
				value = interestRatePerc,
				onValueChange = {
					if(it.length <= 32) { interestRatePerc = it }
				},
				singleLine = true,
				colors = textFieldColors(
					backgroundColor = Color.White,
					focusedIndicatorColor = Color.Transparent,
					unfocusedIndicatorColor = Color.Transparent,
					disabledIndicatorColor = Colors().slate20
				),
				keyboardOptions = KeyboardOptions(
					keyboardType = KeyboardType.Number,
					imeAction = ImeAction.Next
				)
			)
		}
		
		@Composable
		fun isAROCheckbox() {
			Row(
				modifier = Modifier.fillMaxWidth(),
				verticalAlignment = Alignment.CenterVertically
			) {
				Checkbox(
					modifier = Modifier.padding(start = 8.dp),
					enabled = true,
					checked = isARO,
					onCheckedChange = { isARO = it },
					colors = CheckboxDefaults.colors(checkedColor = Colors().buttonActive)
				)
				Text(
					text = "Pakai ARO (Automatic Roll Over)",
					fontSize = 14.sp,
					color = Colors().textBlack,
					modifier = Modifier.clickable(true, onClick = {
						isARO = !isARO
					})
				)
			}
		}
		
		@Composable
		fun bilyetNoTextField() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "No. Bilyet",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			TextField(
				enabled = true,
				modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
					.padding(horizontal = 16.dp)
					.border(
						border = BorderStroke(1.dp, Colors().textDarkGrey),
						shape = RoundedCornerShape(4.dp)
					),
				placeholder = {
					Text(
						text = "",
						fontSize = 16.sp,
						color = Colors().textDarkGrey
					)
				},
				value = bilyetNo,
				onValueChange = {
					if(it.length <= 100) { bilyetNo = it }
				},
				singleLine = true,
				colors = textFieldColors(
					backgroundColor = Color.White,
					focusedIndicatorColor = Color.Transparent,
					unfocusedIndicatorColor = Color.Transparent,
					disabledIndicatorColor = Colors().slate20
				),
				keyboardOptions = KeyboardOptions(
					keyboardType = KeyboardType.Number,
					imeAction = ImeAction.Next
				)
			)
		}
		
		@Composable
		fun interestTypeSelector() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Jenis Bunga",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			
			Box(
				modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
					.background(Color.White)
					.border(1.dp, Colors().textDarkGrey, RoundedCornerShape(4.dp))
					.clickable(true, onClick = {
						showInterestPopup = true
					})
			) {
				Row (
					modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
					horizontalArrangement = Arrangement.SpaceBetween,
					verticalAlignment = Alignment.CenterVertically
				){
					Text(
						text = if(selectedInterest == "") "Anuitas" else selectedInterest,
						fontSize = 14.sp,
						color = if(selectedInterest == "") Colors().textDarkGrey else Colors().textBlack
					)
					Image(painterResource(Res.drawable.Icon_Dropdown_Arrow), null, modifier = Modifier.size(24.dp))
				}
			}
		}
		
		@Composable
		fun debitorNameTextField() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Nama Debitur",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			TextField(
				enabled = true,
				modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
					.padding(horizontal = 16.dp)
					.border(
						border = BorderStroke(1.dp, Colors().textDarkGrey),
						shape = RoundedCornerShape(4.dp)
					),
				placeholder = {
					Text(
						text = "Nama Lengkap",
						fontSize = 16.sp,
						color = Colors().textDarkGrey
					)
				},
				value = debitorName,
				onValueChange = {
					if(it.length <= 100) { debitorName = it }
				},
				singleLine = true,
				colors = textFieldColors(
					backgroundColor = Color.White,
					focusedIndicatorColor = Color.Transparent,
					unfocusedIndicatorColor = Color.Transparent,
					disabledIndicatorColor = Colors().slate20
				),
				keyboardOptions = KeyboardOptions(
					keyboardType = KeyboardType.Text,
					imeAction = ImeAction.Next
				)
			)
		}
		
		@Composable
		fun relationTextField() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Hubungan",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			TextField(
				enabled = true,
				modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
					.padding(horizontal = 16.dp)
					.border(
						border = BorderStroke(1.dp, Colors().textDarkGrey),
						shape = RoundedCornerShape(4.dp)
					),
				placeholder = {
					Text(
						text = "Teman",
						fontSize = 16.sp,
						color = Colors().textDarkGrey
					)
				},
				value = relation,
				onValueChange = {
					if(it.length <= 100) { relation = it }
				},
				singleLine = true,
				colors = textFieldColors(
					backgroundColor = Color.White,
					focusedIndicatorColor = Color.Transparent,
					unfocusedIndicatorColor = Color.Transparent,
					disabledIndicatorColor = Colors().slate20
				),
				keyboardOptions = KeyboardOptions(
					keyboardType = KeyboardType.Text,
					imeAction = ImeAction.Next
				)
			)
		}
		
		@Composable
		fun debitorAddressTextField() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Alamat Debitur",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			TextField(
				enabled = true,
				modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
					.padding(horizontal = 16.dp)
					.border(
						border = BorderStroke(1.dp, Colors().textDarkGrey),
						shape = RoundedCornerShape(4.dp)
					),
				placeholder = {
					Text(
						text = "Jalan Hidup Bahagia",
						fontSize = 16.sp,
						color = Colors().textDarkGrey
					)
				},
				value = debitorAddress,
				onValueChange = {
					if(it.length <= 255) { debitorAddress = it }
				},
				singleLine = true,
				colors = textFieldColors(
					backgroundColor = Color.White,
					focusedIndicatorColor = Color.Transparent,
					unfocusedIndicatorColor = Color.Transparent,
					disabledIndicatorColor = Colors().slate20
				),
				keyboardOptions = KeyboardOptions(
					keyboardType = KeyboardType.Text,
					imeAction = ImeAction.Next
				)
			)
		}
		
		@Composable
		fun interestAmmountTextField() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Penghasilan Bunga (Rupiah)",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			TextField(
				enabled = true,
				modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
					.padding(horizontal = 16.dp)
					.border(
						border = BorderStroke(1.dp, Colors().textDarkGrey),
						shape = RoundedCornerShape(4.dp)
					),
				placeholder = {
					Text(
						text = "0",
						fontSize = 16.sp,
						color = Colors().textDarkGrey
					)
				},
				value = interestAmmount,
				onValueChange = {
					interestAmmount = it
				},
				singleLine = true,
				colors = textFieldColors(
					backgroundColor = Color.White,
					focusedIndicatorColor = Color.Transparent,
					unfocusedIndicatorColor = Color.Transparent,
					disabledIndicatorColor = Colors().slate20
				),
				keyboardOptions = KeyboardOptions(
					keyboardType = KeyboardType.Number,
					imeAction = ImeAction.Next
				)
			)
		}
		
		@Composable
		fun sharesUnitAmountTextField() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Jumlah Lembar Saham",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			TextField(
				enabled = true,
				modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
					.padding(horizontal = 16.dp)
					.border(
						border = BorderStroke(1.dp, Colors().textDarkGrey),
						shape = RoundedCornerShape(4.dp)
					),
				placeholder = {
					Text(
						text = "0",
						fontSize = 16.sp,
						color = Colors().textDarkGrey
					)
				},
				value = sharesUnitAmount,
				onValueChange = {
					sharesUnitAmount = it
				},
				singleLine = true,
				colors = textFieldColors(
					backgroundColor = Color.White,
					focusedIndicatorColor = Color.Transparent,
					unfocusedIndicatorColor = Color.Transparent,
					disabledIndicatorColor = Colors().slate20
				),
				keyboardOptions = KeyboardOptions(
					keyboardType = KeyboardType.Number,
					imeAction = ImeAction.Next
				)
			)
		}
		
		@Composable
		fun sharesUnitPriceTextField() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Harga per Lembar",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			TextField(
				enabled = true,
				modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
					.padding(horizontal = 16.dp)
					.border(
						border = BorderStroke(1.dp, Colors().textDarkGrey),
						shape = RoundedCornerShape(4.dp)
					),
				placeholder = {
					Text(
						text = "0",
						fontSize = 16.sp,
						color = Colors().textDarkGrey
					)
				},
				value = sharesUnitPrice,
				onValueChange = {
					sharesUnitPrice = it
				},
				singleLine = true,
				colors = textFieldColors(
					backgroundColor = Color.White,
					focusedIndicatorColor = Color.Transparent,
					unfocusedIndicatorColor = Color.Transparent,
					disabledIndicatorColor = Colors().slate20
				),
				keyboardOptions = KeyboardOptions(
					keyboardType = KeyboardType.Number,
					imeAction = ImeAction.Next
				)
			)
		}
		
		@Composable
		fun companyNameTextField() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Nama Perusahaan",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			TextField(
				enabled = true,
				modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
					.padding(horizontal = 16.dp)
					.border(
						border = BorderStroke(1.dp, Colors().textDarkGrey),
						shape = RoundedCornerShape(4.dp)
					),
				placeholder = {
					Text(
						text = "PT. ABC",
						fontSize = 16.sp,
						color = Colors().textDarkGrey
					)
				},
				value = companyName,
				onValueChange = {
					if(it.length <= 100) { companyName = it }
				},
				singleLine = true,
				colors = textFieldColors(
					backgroundColor = Color.White,
					focusedIndicatorColor = Color.Transparent,
					unfocusedIndicatorColor = Color.Transparent,
					disabledIndicatorColor = Colors().slate20
				),
				keyboardOptions = KeyboardOptions(
					keyboardType = KeyboardType.Text,
					imeAction = ImeAction.Next
				)
			)
		}
		
		@Composable
		fun companyNPWPTextField() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "NPWP Perusahaan",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			TextField(
				enabled = true,
				modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
					.padding(horizontal = 16.dp)
					.border(
						border = BorderStroke(1.dp, Colors().textDarkGrey),
						shape = RoundedCornerShape(4.dp)
					),
				placeholder = {
					Text(
						text = "PT. ABC",
						fontSize = 16.sp,
						color = Colors().textDarkGrey
					)
				},
				value = companyNPWP,
				onValueChange = {
					if(it.length <= 100) { companyNPWP = it }
				},
				singleLine = true,
				colors = textFieldColors(
					backgroundColor = Color.White,
					focusedIndicatorColor = Color.Transparent,
					unfocusedIndicatorColor = Color.Transparent,
					disabledIndicatorColor = Colors().slate20
				),
				keyboardOptions = KeyboardOptions(
					keyboardType = KeyboardType.Text,
					imeAction = ImeAction.Next
				)
			)
		}
		
		@Composable
		fun acquisitionPriceTextField() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Harga Perolehan",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			TextField(
				enabled = true,
				modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
					.padding(horizontal = 16.dp)
					.border(
						border = BorderStroke(1.dp, Colors().textDarkGrey),
						shape = RoundedCornerShape(4.dp)
					),
				placeholder = {
					Text(
						text = "0",
						fontSize = 16.sp,
						color = Colors().textDarkGrey
					)
				},
				value = currencyAmount,
				onValueChange = {
					currencyAmount = it
				},
				singleLine = true,
				colors = textFieldColors(
					backgroundColor = Color.White,
					focusedIndicatorColor = Color.Transparent,
					unfocusedIndicatorColor = Color.Transparent,
					disabledIndicatorColor = Colors().slate20
				),
				keyboardOptions = KeyboardOptions(
					keyboardType = KeyboardType.Number,
					imeAction = ImeAction.Next
				)
			)
		}
		
		@Composable
		fun cvNameTextField() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Nama CV / Firma / dsb",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			TextField(
				enabled = true,
				modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
					.padding(horizontal = 16.dp)
					.border(
						border = BorderStroke(1.dp, Colors().textDarkGrey),
						shape = RoundedCornerShape(4.dp)
					),
				placeholder = {
					Text(
						text = "Nama CV / Firma / dsb",
						fontSize = 16.sp,
						color = Colors().textDarkGrey
					)
				},
				value = cvName,
				onValueChange = {
					if(it.length <= 100) { cvName = it }
				},
				singleLine = true,
				colors = textFieldColors(
					backgroundColor = Color.White,
					focusedIndicatorColor = Color.Transparent,
					unfocusedIndicatorColor = Color.Transparent,
					disabledIndicatorColor = Colors().slate20
				),
				keyboardOptions = KeyboardOptions(
					keyboardType = KeyboardType.Text,
					imeAction = ImeAction.Next
				)
			)
		}
		
		@Composable
		fun cvNPWPTextField() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "NPWP CV / Firma / dsb",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			TextField(
				enabled = true,
				modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
					.padding(horizontal = 16.dp)
					.border(
						border = BorderStroke(1.dp, Colors().textDarkGrey),
						shape = RoundedCornerShape(4.dp)
					),
				placeholder = {
					Text(
						text = "NPWP CV / Firma / dsb",
						fontSize = 16.sp,
						color = Colors().textDarkGrey
					)
				},
				value = cvNPWP,
				onValueChange = {
					if(it.length <= 100) { cvNPWP = it }
				},
				singleLine = true,
				colors = textFieldColors(
					backgroundColor = Color.White,
					focusedIndicatorColor = Color.Transparent,
					unfocusedIndicatorColor = Color.Transparent,
					disabledIndicatorColor = Colors().slate20
				),
				keyboardOptions = KeyboardOptions(
					keyboardType = KeyboardType.Text,
					imeAction = ImeAction.Next
				)
			)
		}
		
		@Composable
		fun cvAddressTextField() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Alamat CV / Firma / dsb",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			TextField(
				enabled = true,
				modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
					.padding(horizontal = 16.dp)
					.border(
						border = BorderStroke(1.dp, Colors().textDarkGrey),
						shape = RoundedCornerShape(4.dp)
					),
				placeholder = {
					Text(
						text = "Jalan Hidup Bahagia",
						fontSize = 16.sp,
						color = Colors().textDarkGrey
					)
				},
				value = cvAddress,
				onValueChange = {
					if(it.length <= 255) { cvAddress = it }
				},
				singleLine = true,
				colors = textFieldColors(
					backgroundColor = Color.White,
					focusedIndicatorColor = Color.Transparent,
					unfocusedIndicatorColor = Color.Transparent,
					disabledIndicatorColor = Colors().slate20
				),
				keyboardOptions = KeyboardOptions(
					keyboardType = KeyboardType.Text,
					imeAction = ImeAction.Next
				)
			)
		}
		
		@Composable
		fun brandNameTextField() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Merk",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			TextField(
				enabled = true,
				modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
					.padding(horizontal = 16.dp)
					.border(
						border = BorderStroke(1.dp, Colors().textDarkGrey),
						shape = RoundedCornerShape(4.dp)
					),
				placeholder = {
					Text(
						text = "Merk",
						fontSize = 16.sp,
						color = Colors().textDarkGrey
					)
				},
				value = brandName,
				onValueChange = {
					if(it.length <= 100) { brandName = it }
				},
				singleLine = true,
				colors = textFieldColors(
					backgroundColor = Color.White,
					focusedIndicatorColor = Color.Transparent,
					unfocusedIndicatorColor = Color.Transparent,
					disabledIndicatorColor = Colors().slate20
				),
				keyboardOptions = KeyboardOptions(
					keyboardType = KeyboardType.Text,
					imeAction = ImeAction.Next
				)
			)
		}
		
		@Composable
		fun productTypeTextField() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Tipe",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			TextField(
				enabled = true,
				modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
					.padding(horizontal = 16.dp)
					.border(
						border = BorderStroke(1.dp, Colors().textDarkGrey),
						shape = RoundedCornerShape(4.dp)
					),
				placeholder = {
					Text(
						text = "Tipe Produk",
						fontSize = 16.sp,
						color = Colors().textDarkGrey
					)
				},
				value = productType,
				onValueChange = {
					if(it.length <= 100) { productType = it }
				},
				singleLine = true,
				colors = textFieldColors(
					backgroundColor = Color.White,
					focusedIndicatorColor = Color.Transparent,
					unfocusedIndicatorColor = Color.Transparent,
					disabledIndicatorColor = Colors().slate20
				),
				keyboardOptions = KeyboardOptions(
					keyboardType = KeyboardType.Text,
					imeAction = ImeAction.Next
				)
			)
		}
		
		@Composable
		fun productionYearTextField() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Tahun Pembuatan",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			TextField(
				enabled = true,
				modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
					.padding(horizontal = 16.dp)
					.border(
						border = BorderStroke(1.dp, Colors().textDarkGrey),
						shape = RoundedCornerShape(4.dp)
					),
				placeholder = {
					Text(
						text = "1980",
						fontSize = 16.sp,
						color = Colors().textDarkGrey
					)
				},
				value = productionYear,
				onValueChange = {
					productionYear = it
				},
				singleLine = true,
				colors = textFieldColors(
					backgroundColor = Color.White,
					focusedIndicatorColor = Color.Transparent,
					unfocusedIndicatorColor = Color.Transparent,
					disabledIndicatorColor = Colors().slate20
				),
				keyboardOptions = KeyboardOptions(
					keyboardType = KeyboardType.Text,
					imeAction = ImeAction.Next
				)
			)
		}
		
		@Composable
		fun jewelryTypeTextField() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Jenis",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			TextField(
				enabled = true,
				modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
					.padding(horizontal = 16.dp)
					.border(
						border = BorderStroke(1.dp, Colors().textDarkGrey),
						shape = RoundedCornerShape(4.dp)
					),
				placeholder = {
					Text(
						text = "Emas 24 Karat / Lukisan / dsb",
						fontSize = 16.sp,
						color = Colors().textDarkGrey
					)
				},
				value = jewelryType,
				onValueChange = {
					if(it.length <= 100) { jewelryType = it }
				},
				singleLine = true,
				colors = textFieldColors(
					backgroundColor = Color.White,
					focusedIndicatorColor = Color.Transparent,
					unfocusedIndicatorColor = Color.Transparent,
					disabledIndicatorColor = Colors().slate20
				),
				keyboardOptions = KeyboardOptions(
					keyboardType = KeyboardType.Text,
					imeAction = ImeAction.Next
				)
			)
		}
		
		@Composable
		fun unitNameTextField() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Satuan",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			TextField(
				enabled = true,
				modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
					.padding(horizontal = 16.dp)
					.border(
						border = BorderStroke(1.dp, Colors().textDarkGrey),
						shape = RoundedCornerShape(4.dp)
					),
				placeholder = {
					Text(
						text = "1 buah / Kg",
						fontSize = 16.sp,
						color = Colors().textDarkGrey
					)
				},
				value = unitName,
				onValueChange = {
					if(it.length <= 100) { unitName = it }
				},
				singleLine = true,
				colors = textFieldColors(
					backgroundColor = Color.White,
					focusedIndicatorColor = Color.Transparent,
					unfocusedIndicatorColor = Color.Transparent,
					disabledIndicatorColor = Colors().slate20
				),
				keyboardOptions = KeyboardOptions(
					keyboardType = KeyboardType.Text,
					imeAction = ImeAction.Next
				)
			)
		}
		
		@Composable
		fun ownerNoTextField() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "No. Kepemilikan",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			TextField(
				enabled = true,
				modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
					.padding(horizontal = 16.dp)
					.border(
						border = BorderStroke(1.dp, Colors().textDarkGrey),
						shape = RoundedCornerShape(4.dp)
					),
				placeholder = {
					Text(
						text = "00.00.00.0000.00.00",
						fontSize = 16.sp,
						color = Colors().textDarkGrey
					)
				},
				value = ownerNo,
				onValueChange = {
					if(it.length <= 100) { ownerNo = it }
				},
				singleLine = true,
				colors = textFieldColors(
					backgroundColor = Color.White,
					focusedIndicatorColor = Color.Transparent,
					unfocusedIndicatorColor = Color.Transparent,
					disabledIndicatorColor = Colors().slate20
				),
				keyboardOptions = KeyboardOptions(
					keyboardType = KeyboardType.Text,
					imeAction = ImeAction.Next
				)
			)
		}
		
		@Composable
		fun locationTextField() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Lokasi",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			TextField(
				enabled = true,
				modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
					.padding(horizontal = 16.dp)
					.border(
						border = BorderStroke(1.dp, Colors().textDarkGrey),
						shape = RoundedCornerShape(4.dp)
					),
				placeholder = {
					Text(
						text = "Perumahan X Jakarta",
						fontSize = 16.sp,
						color = Colors().textDarkGrey
					)
				},
				value = location,
				onValueChange = {
					if(it.length <= 255) { location = it }
				},
				singleLine = true,
				colors = textFieldColors(
					backgroundColor = Color.White,
					focusedIndicatorColor = Color.Transparent,
					unfocusedIndicatorColor = Color.Transparent,
					disabledIndicatorColor = Colors().slate20
				),
				keyboardOptions = KeyboardOptions(
					keyboardType = KeyboardType.Text,
					imeAction = ImeAction.Next
				)
			)
		}
		
		@Composable
		fun nopTextField() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "NOP",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			TextField(
				enabled = true,
				modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
					.padding(horizontal = 16.dp)
					.border(
						border = BorderStroke(1.dp, Colors().textDarkGrey),
						shape = RoundedCornerShape(4.dp)
					),
				placeholder = {
					Text(
						text = "0000000000",
						fontSize = 16.sp,
						color = Colors().textDarkGrey
					)
				},
				value = nop,
				onValueChange = {
					if(it.length <= 50) { nop = it }
				},
				singleLine = true,
				colors = textFieldColors(
					backgroundColor = Color.White,
					focusedIndicatorColor = Color.Transparent,
					unfocusedIndicatorColor = Color.Transparent,
					disabledIndicatorColor = Colors().slate20
				),
				keyboardOptions = KeyboardOptions(
					keyboardType = KeyboardType.Text,
					imeAction = ImeAction.Next
				)
			)
		}
		
		@Composable
		fun areaTextField() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Luas",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			TextField(
				enabled = true,
				modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
					.padding(horizontal = 16.dp)
					.border(
						border = BorderStroke(1.dp, Colors().textDarkGrey),
						shape = RoundedCornerShape(4.dp)
					),
				placeholder = {
					Text(
						text = "1200 m2",
						fontSize = 16.sp,
						color = Colors().textDarkGrey
					)
				},
				value = area,
				onValueChange = {
					if(it.length <= 30) { area = it }
				},
				singleLine = true,
				colors = textFieldColors(
					backgroundColor = Color.White,
					focusedIndicatorColor = Color.Transparent,
					unfocusedIndicatorColor = Color.Transparent,
					disabledIndicatorColor = Colors().slate20
				),
				keyboardOptions = KeyboardOptions(
					keyboardType = KeyboardType.Text,
					imeAction = ImeAction.Next
				)
			)
		}
		
		@Composable
		fun patenNameTextField() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Nama Paten / Royalti / Merek Dagang",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			TextField(
				enabled = true,
				modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
					.padding(horizontal = 16.dp)
					.border(
						border = BorderStroke(1.dp, Colors().textDarkGrey),
						shape = RoundedCornerShape(4.dp)
					),
				placeholder = {
					Text(
						text = "Merek ABC",
						fontSize = 16.sp,
						color = Colors().textDarkGrey
					)
				},
				value = patenName,
				onValueChange = {
					if(it.length <= 100) { patenName = it }
				},
				singleLine = true,
				colors = textFieldColors(
					backgroundColor = Color.White,
					focusedIndicatorColor = Color.Transparent,
					unfocusedIndicatorColor = Color.Transparent,
					disabledIndicatorColor = Colors().slate20
				),
				keyboardOptions = KeyboardOptions(
					keyboardType = KeyboardType.Text,
					imeAction = ImeAction.Next
				)
			)
		}
		
		@Composable
		fun patenNoTextField() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Nomor Paten / Royalti / Merek Dagang",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			TextField(
				enabled = true,
				modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
					.padding(horizontal = 16.dp)
					.border(
						border = BorderStroke(1.dp, Colors().textDarkGrey),
						shape = RoundedCornerShape(4.dp)
					),
				placeholder = {
					Text(
						text = "00000000",
						fontSize = 16.sp,
						color = Colors().textDarkGrey
					)
				},
				value = patenNo,
				onValueChange = {
					if(it.length <= 75) { patenNo = it }
				},
				singleLine = true,
				colors = textFieldColors(
					backgroundColor = Color.White,
					focusedIndicatorColor = Color.Transparent,
					unfocusedIndicatorColor = Color.Transparent,
					disabledIndicatorColor = Colors().slate20
				),
				keyboardOptions = KeyboardOptions(
					keyboardType = KeyboardType.Text,
					imeAction = ImeAction.Next
				)
			)
		}
		
		@Composable
		fun patenStartDatePicker() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Tanggal Berlaku",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			Box(
				modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
//							.background(Colors().slate20, RoundedCornerShape(4.dp))
					.border(1.dp, Colors().textDarkGrey, RoundedCornerShape(4.dp))
					.clickable(true, onClick = {
						showPatenStartPopup = true
					})
			) {
				Row (
					modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
					horizontalArrangement = Arrangement.SpaceBetween,
					verticalAlignment = Alignment.CenterVertically
				){
					Text(
						text = if(patenStartDate == "") "Pilih tanggal Berlaku" else DateFormatter(patenStartDate),
						fontSize = 14.sp,
						color = if(patenStartDate == "") Colors().textDarkGrey else Colors().textBlack
					)
					Image(painterResource(Res.drawable.icon_calendar), null, modifier = Modifier.size(24.dp))
				}
			}
		}
		
		@Composable
		fun patenDueDatePicker() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Tanggal Kadaluarsa",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			Box(
				modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
//							.background(Colors().slate20, RoundedCornerShape(4.dp))
					.border(1.dp, Colors().textDarkGrey, RoundedCornerShape(4.dp))
					.clickable(true, onClick = {
						showPatenEndPopup = true
					})
			) {
				Row (
					modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
					horizontalArrangement = Arrangement.SpaceBetween,
					verticalAlignment = Alignment.CenterVertically
				){
					Text(
						text = if(patenDueDate == "") "Pilih tanggal kadaluarsa" else DateFormatter(patenDueDate),
						fontSize = 14.sp,
						color = if(patenDueDate == "") Colors().textDarkGrey else Colors().textBlack
					)
					Image(painterResource(Res.drawable.icon_calendar), null, modifier = Modifier.size(24.dp))
				}
			}
		}
		
		@Composable
		fun polisNoTextField() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "No. Rekening / BG / Polis",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			TextField(
				enabled = true,
				modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
					.padding(horizontal = 16.dp)
					.border(
						border = BorderStroke(1.dp, Colors().textDarkGrey),
						shape = RoundedCornerShape(4.dp)
					),
				placeholder = {
					Text(
						text = "0000000000",
						fontSize = 16.sp,
						color = Colors().textDarkGrey
					)
				},
				value = polisNo,
				onValueChange = {
					if(it.length <= 100) { polisNo = it }
				},
				singleLine = true,
				colors = textFieldColors(
					backgroundColor = Color.White,
					focusedIndicatorColor = Color.Transparent,
					unfocusedIndicatorColor = Color.Transparent,
					disabledIndicatorColor = Colors().slate20
				),
				keyboardOptions = KeyboardOptions(
					keyboardType = KeyboardType.Text,
					imeAction = ImeAction.Next
				)
			)
		}
		
		//Form
		Column(
			modifier = Modifier.fillMaxSize().background(Colors().panel)
		) {
			LazyColumn(
				modifier = Modifier.fillMaxSize()
			) {
				//Top Bar
				item{
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
						
						Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
							Text(
								text = if (id == 0) "Tambah Harta" else "Edit Harta",
								fontWeight = FontWeight.Bold,
								fontSize = 16.sp,
								color = Colors().textBlack,
								modifier = Modifier.align(Alignment.CenterVertically)
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
				
				//Select Wealth Type
				item {
					Text(
						modifier = Modifier.padding(horizontal = 16.dp).padding(top = 18.dp),
						text = "Jenis Harta",
						fontSize = 12.sp,
						color = Color.Black,
						fontWeight = FontWeight.Bold
					)
					
					Box(
						modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 8.dp)
							.background(if(id == 0) Color.White else Colors().slate20, RoundedCornerShape(4.dp))
							.border(1.dp, Colors().textDarkGrey, RoundedCornerShape(4.dp))
							.clickable(id == 0, onClick = {
								showWealthTypePopup = true
							})
					) {
						Row (
							modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
							horizontalArrangement = Arrangement.SpaceBetween,
							verticalAlignment = Alignment.CenterVertically
						){
							Text(
								text = if(selectedWealthType == "") "Pilih Jenis Harta" else selectedWealthType,
								fontSize = 14.sp,
								color = if(selectedWealthType == "" || id != 0) Colors().textDarkGrey else Colors().textBlack
							)
							Image(painterResource(Res.drawable.Icon_Dropdown_Arrow), null, modifier = Modifier.size(24.dp))
						}
					}
				}
				
				//Acquisition Year
				item {
					acquisitionYearTextField()
				}
				
				//Is Overseas Checkbox
				item {
					Row(
						modifier = Modifier.fillMaxWidth(),
						verticalAlignment = Alignment.CenterVertically
					) {
						Checkbox(
							modifier = Modifier.padding(start = 8.dp),
							enabled = true,
							checked = isOverseas,
							onCheckedChange = { isOverseas = it },
							colors = CheckboxDefaults.colors(checkedColor = Colors().buttonActive)
						)
						Text(
							text = "Berlokasi di luar negeri",
							fontSize = 14.sp,
							color = Colors().textBlack,
							modifier = Modifier.clickable(true, onClick = {
								isOverseas = !isOverseas
							})
						)
					}
				}
				
				when(formCategory) {
					//Form A
					AssetApiSaveCode.A.toString() -> {
						
						//Currency Selector
						item {
							currencySelector()
						}
						//Entry Amount
						item {
							currencyEntryTextField()
						}
						//Currency Rate Entry Mode Enum Checkbox
						if(selectedCurrency != "(IDR) Indonesian rupiah") {
							item {
								currRateEntryModeCheckbox()
							}
							
							//Currency Rate
							item {
								currencyRateTextField()
							}
						}
						//Total
						item {
							currencyAmountTextField()
						}
						//Description
						item {
							description()
						}
						//Submit Button
						item {
							Button(
								enabled = if(acquisitionYear.isNotBlank() && currencyAmountEntry.isNotBlank() && currRateIDR.isNotBlank()) true else false,
								modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 22.dp),
								colors = buttonColors(backgroundColor = Colors().buttonActive, contentColor = Color.White),
								onClick = {
									currencyAmount = currencyAmount.replace("Rp. ", "")
									currencyAmount = currencyAmount.replace(",", "")
									currRateIDR = currRateIDR.replace("Rp. ", "")
									currRateIDR = currRateIDR.replace(",", "")
									
									val dataModel = FormWealthARequestApiModel(
										Id = id,
										Tr1770HdId = sptHd?.Id ?: 0,
										WealthTypeId = wealthTypeId,
										AcquisitionYear = acquisitionYear.toInt(),
										CurrencyId = currencyId,
										CurrencyAmount = currencyAmount.toLong(),
										CurrRateEntryModeE = currRateEntryModeE,
										CurrRateIDR = currRateIDR.ifBlank { null }?.toLong(),
										Description = description.ifBlank { null },
										IsOverseas = isOverseas
									)
									
									println(dataModel)
									
									scope.launch {
										isReady = false
										sptManager.saveWealthA(scope, dataModel)
										navigator.pop()
									}
								}
							) {
								Text(
									modifier = Modifier.padding(vertical = 6.dp),
									text = "Simpan",
									fontSize = 16.sp,
									fontWeight = FontWeight.Bold
								)
							}
						}
					}
					
					//Form B
					AssetApiSaveCode.B.toString() -> {
						//Currency Selector
						item {
							currencySelector()
						}
						//Entry Amount
						item {
							currencyEntryTextField()
						}
						//Currency Rate Entry Mode Enum Checkbox
						if(selectedCurrency != "(IDR) Indonesian rupiah") {
							item {
								currRateEntryModeCheckbox()
							}
							
							//Currency Rate
							item {
								currencyRateTextField()
							}
						}
						//Total
						item {
							currencyAmountTextField()
						}
						//Storage Name
						item {
							storageNameTextField()
						}
						//Storage Account No.
						item {
							storageAccountNoTextField()
						}
						//Storage Address
						item {
							storageAddressTextField()
						}
						//Submit Button
						item {
							Button(
								enabled = if(acquisitionYear.isNotBlank() && currencyAmountEntry.isNotBlank() && currRateIDR.isNotBlank() && storageName.isNotBlank()) true else false,
								modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 22.dp),
								colors = buttonColors(backgroundColor = Colors().buttonActive, contentColor = Color.White),
								onClick = {
									currencyAmount = currencyAmount.replace("Rp. ", "")
									currencyAmount = currencyAmount.replace(",", "")
									currRateIDR = currRateIDR.replace("Rp. ", "")
									currRateIDR = currRateIDR.replace(",", "")
									
									val dataModel = FormWealthBRequestApiModel(
										Id = id,
										Tr1770HdId = sptHd?.Id ?: 0,
										WealthTypeId = wealthTypeId,
										AcquisitionYear = acquisitionYear.toInt(),
										CurrencyId = currencyId,
										CurrencyAmount = currencyAmount.toLong(),
										CurrRateEntryModeE = currRateEntryModeE,
										CurrRateIDR = currRateIDR.ifBlank { null }?.toLong(),
										IsOverseas = isOverseas,
										StorageName = storageName.ifBlank { null },
										StorageAccountNo = storageAccountNo.ifBlank { null },
										StorageAddress = storageAddress.ifBlank { null }
									)
									
									println(dataModel)
									
									scope.launch {
										isReady = false
										sptManager.saveWealthB(scope, dataModel)
										navigator.pop()
									}
								}
							) {
								Text(
									modifier = Modifier.padding(vertical = 6.dp),
									text = "Simpan",
									fontSize = 16.sp,
									fontWeight = FontWeight.Bold
								)
							}
						}
					}
					
					//Form C
					AssetApiSaveCode.C.toString() -> {
						//Currency Selector
						item {
							currencySelector()
						}
						//Entry Amount
						item {
							currencyEntryTextField()
						}
						//Currency Rate Entry Mode Enum Checkbox
						if(selectedCurrency != "(IDR) Indonesian rupiah") {
							item {
								currRateEntryModeCheckbox()
							}
							
							//Currency Rate
							item {
								currencyRateTextField()
							}
						}
						//Total
						item {
							currencyAmountTextField()
						}
						//Start Date
						item {
							startDatePicker()
						}
						//Interest Rate Percentile
						item {
						 interestRatePercTextField()
						}
						//Due Date
						item {
							dueDatePicker()
						}
						//Is ARO
						item {
							isAROCheckbox()
						}
						//Storage Name
						item {
							storageNameTextField()
						}
						//Bilyet No.
						item {
							bilyetNoTextField()
						}
						//Storage Address
						item {
							storageAddressTextField()
						}
						//Submit Button
						item {
							Button(
								enabled = if(acquisitionYear.isNotBlank() && currencyAmountEntry.isNotBlank() && currRateIDR.isNotBlank() && storageName.isNotBlank()) true else false,
								modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 22.dp),
								colors = buttonColors(backgroundColor = Colors().buttonActive, contentColor = Color.White),
								onClick = {
									currencyAmount = currencyAmount.replace("Rp. ", "")
									currencyAmount = currencyAmount.replace(",", "")
									currRateIDR = currRateIDR.replace("Rp. ", "")
									currRateIDR = currRateIDR.replace(",", "")
									
									val dataModel = FormWealthCRequestApiModel(
										Id = id,
										Tr1770HdId = sptHd?.Id ?: 0,
										WealthTypeId = wealthTypeId,
										AcquisitionYear = acquisitionYear.toInt(),
										CurrencyId = currencyId,
										CurrencyAmount = currencyAmount.toLong(),
										CurrRateEntryModeE = currRateEntryModeE,
										CurrRateIDR = currRateIDR.ifBlank { null }?.toLong(),
										IsOverseas = isOverseas,
										StartDate = startDate.ifBlank { null },
										InterestRatePerc = interestRatePerc.ifBlank { null }?.toLong(),
										DueDate = dueDate.ifBlank { null },
										IsARO = isARO,
										StorageName = storageName.ifBlank { null },
										BilyetNo = bilyetNo.ifBlank { null },
										StorageAddress = storageAddress.ifBlank { null }
									)
									
									println(dataModel)
									
									scope.launch {
										isReady = false
										sptManager.saveWealthC(scope, dataModel)
										navigator.pop()
									}
								}
							) {
								Text(
									modifier = Modifier.padding(vertical = 6.dp),
									text = "Simpan",
									fontSize = 16.sp,
									fontWeight = FontWeight.Bold
								)
							}
						}
					}
					
					//Form D
					AssetApiSaveCode.D.toString() -> {
						//Currency Selector
						item {
							currencySelector()
						}
						//Entry Amount
						item {
							currencyEntryTextField()
						}
						//Currency Rate Entry Mode Enum Checkbox
						if(selectedCurrency != "(IDR) Indonesian rupiah") {
							item {
								currRateEntryModeCheckbox()
							}
							
							//Currency Rate
							item {
								currencyRateTextField()
							}
						}
						//Total
						item {
							currencyAmountTextField()
						}
						//Start Date
						item {
							startDatePicker()
						}
						//Interest Rate Percentile
						item {
							interestRatePercTextField()
						}
						//Due Date
						item {
							dueDatePicker()
						}
						//Interest Type
						item {
							interestTypeSelector()
						}
						//Debitor Name
						item {
							debitorNameTextField()
						}
						//Relation
						item {
							relationTextField()
						}
						//Debitor Address
						item {
							debitorAddressTextField()
						}
						//Interest Ammount
						item {
							interestAmmountTextField()
						}
						//Submit Button
						item {
							Button(
								enabled = if(acquisitionYear.isNotBlank() && currencyAmountEntry.isNotBlank() && currRateIDR.isNotBlank() && debitorName.isNotBlank()) true else false,
								modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 22.dp),
								colors = buttonColors(backgroundColor = Colors().buttonActive, contentColor = Color.White),
								onClick = {
									currencyAmount = currencyAmount.replace("Rp. ", "")
									currencyAmount = currencyAmount.replace(",", "")
									currRateIDR = currRateIDR.replace("Rp. ", "")
									currRateIDR = currRateIDR.replace(",", "")
									interestAmmount = interestAmmount.replace("Rp. ", "")
									interestAmmount = interestAmmount.replace(",", "")
									
									val dataModel = FormWealthDRequestApiModel(
										Id = id,
										Tr1770HdId = sptHd?.Id ?: 0,
										WealthTypeId = wealthTypeId,
										AcquisitionYear = acquisitionYear.toInt(),
										CurrencyId = currencyId,
										CurrencyAmount = currencyAmount.toLong(),
										CurrRateEntryModeE = currRateEntryModeE,
										CurrRateIDR = currRateIDR.ifBlank { null }?.toLong(),
										IsOverseas = isOverseas,
										StartDate = startDate.ifBlank { null },
										InterestRatePerc = interestRatePerc.ifBlank { null }?.toLong(),
										DueDate = dueDate.ifBlank { null },
										InterestTypeId = interestTypeId ?: 0,
										DebitorName = debitorName.ifBlank { null },
										Relation = relation.ifBlank { null },
										DebitorAddress = debitorAddress.ifBlank { null },
										InterestAmountIDR = interestAmmount.ifBlank { null }?.toLong()
									)
									
									println(dataModel)
									
									scope.launch {
										isReady = false
										sptManager.saveWealthD(scope, dataModel)
										navigator.pop()
									}
								}
							) {
								Text(
									modifier = Modifier.padding(vertical = 6.dp),
									text = "Simpan",
									fontSize = 16.sp,
									fontWeight = FontWeight.Bold
								)
							}
						}
					}
					
					//Form E
					AssetApiSaveCode.E.toString() -> {
					
					}
					
					//Form F
					AssetApiSaveCode.F.toString() -> {
						//Shares Unit Amount
						item {
							sharesUnitAmountTextField()
						}
						
						//Shares Unit Price
						item {
							sharesUnitPriceTextField()
						}
						
						//Currency Selector
						item {
							currencySelector()
						}
						
						//Currency Rate Entry Mode Enum Checkbox
						if(selectedCurrency != "(IDR) Indonesian rupiah") {
							item {
								currRateEntryModeCheckbox()
							}
							
							//Currency Rate
							item {
								currencyRateTextField()
							}
						}
						
						//Total
						item {
							Text(
								modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
								text = "Total",
								fontSize = 12.sp,
								color = Color.Black,
								fontWeight = FontWeight.Bold
							)
							Box(
								modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
									.background(Colors().slate20, RoundedCornerShape(4.dp))
									.border(1.dp, Colors().textDarkGrey, RoundedCornerShape(4.dp))
							) {
								Row (
									modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
									horizontalArrangement = Arrangement.SpaceBetween,
									verticalAlignment = Alignment.CenterVertically
								){
									Text(
										text = if(sharesUnitPrice.isNotBlank() && currRateIDR.isNotBlank() && currRateIDR != "0") "Rp ${CurrencyFormatter(BigDeciToString(updateTotal(sharesUnitPrice.toLong(), currRateIDR.toLong()).toString()))}" else "Rp 0",
										fontSize = 14.sp,
										color = Colors().textDarkGrey
									)
								}
							}
						}
						
						//Company Name
						item {
							companyNameTextField()
						}
						
						//Company NPWP
						item {
							companyNPWPTextField()
						}
						
						//Description
						item {
							description()
						}
						
						//Submit Button
						item {
							Button(
								enabled = if(acquisitionYear.isNotBlank() && sharesUnitAmount.isNotBlank() && sharesUnitPrice.isNotBlank() && currRateIDR.isNotBlank() && companyName.isNotBlank()) true else false,
								modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 22.dp),
								colors = buttonColors(backgroundColor = Colors().buttonActive, contentColor = Color.White),
								onClick = {
									currRateIDR = currRateIDR.replace("Rp. ", "")
									currRateIDR = currRateIDR.replace(",", "")
									sharesUnitPrice = currRateIDR.replace("Rp. ", "")
									sharesUnitPrice = currRateIDR.replace(",", "")
									
									val dataModel = FormWealthFRequestApiModel(
										Id = id,
										Tr1770HdId = sptHd?.Id ?: 0,
										WealthTypeId = wealthTypeId,
										AcquisitionYear = acquisitionYear.toInt(),
										SharesUnitAmount = sharesUnitAmount.toInt(),
										SharesUnitPrice = sharesUnitPrice.toLong(),
										CurrencyId = currencyId,
										CurrRateEntryModeE = currRateEntryModeE,
										CurrRateIDR = currRateIDR.ifBlank { null }?.toLong(),
										IsOverseas = isOverseas,
										CompanyName = companyName.ifBlank { null },
										CompanyNPWP = companyNPWP.ifBlank { null },
										Description = description.ifBlank { null }
									)
									
									println(dataModel)
									
									scope.launch {
										isReady = false
										sptManager.saveWealthF(scope, dataModel)
										navigator.pop()
									}
								}
							) {
								Text(
									modifier = Modifier.padding(vertical = 6.dp),
									text = "Simpan",
									fontSize = 16.sp,
									fontWeight = FontWeight.Bold
								)
							}
						}
					}
					
					//Form G
					AssetApiSaveCode.G.toString() -> {
						//Acquisition Price
						item {
							acquisitionPriceTextField()
						}
						
						//CV Name
						item {
							cvNameTextField()
						}
						
						//CV NPWP
						item {
							cvNPWPTextField()
						}
						
						//CV Address
						item {
							cvAddressTextField()
						}
						
						//Description
						item {
							description()
						}
						
						//Submit Button
						item {
							Button(
								enabled = if(acquisitionYear.isNotBlank() && currencyAmount.isNotBlank() && cvName.isNotBlank()) true else false,
								modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 22.dp),
								colors = buttonColors(backgroundColor = Colors().buttonActive, contentColor = Color.White),
								onClick = {
									currencyAmount = currencyAmount.replace("Rp. ", "")
									currencyAmount = currencyAmount.replace(",", "")
									
									val dataModel = FormWealthGRequestApiModel(
										Id = id,
										Tr1770HdId = sptHd?.Id ?: 0,
										WealthTypeId = wealthTypeId,
										AcquisitionYear = acquisitionYear.toInt(),
										CurrencyAmountIDR = currencyAmount.toLong(),
										IsOverseas = isOverseas,
										CVName = cvName.ifBlank { null },
										CVNPWP = cvNPWP.ifBlank { null },
										CVAddress = cvAddress.ifBlank { null },
										Description = description.ifBlank { null }
									)
									
									println(dataModel)
									
									scope.launch {
										isReady = false
										sptManager.saveWealthG(scope, dataModel)
										navigator.pop()
									}
								}
							) {
								Text(
									modifier = Modifier.padding(vertical = 6.dp),
									text = "Simpan",
									fontSize = 16.sp,
									fontWeight = FontWeight.Bold
								)
							}
						}
					}
					
					//Form H
					AssetApiSaveCode.H.toString() -> {
						//Acquisition Price
						item {
							acquisitionPriceTextField()
						}
						
						//Brand Name
						item {
							brandNameTextField()
						}
						
						//Product Type
						item {
							productTypeTextField()
						}
						
						//Production Year
						item {
							productionYearTextField()
						}
						
						//Description
						item {
							description()
						}
						
						//Submit Button
						item {
							Button(
								enabled = if(acquisitionYear.isNotBlank() && currencyAmount.isNotBlank() && brandName.isNotBlank()) true else false,
								modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 22.dp),
								colors = buttonColors(backgroundColor = Colors().buttonActive, contentColor = Color.White),
								onClick = {
									currencyAmount = currencyAmount.replace("Rp. ", "")
									currencyAmount = currencyAmount.replace(",", "")
									
									val dataModel = FormWealthHRequestApiModel(
										Id = id,
										Tr1770HdId = sptHd?.Id ?: 0,
										WealthTypeId = wealthTypeId,
										AcquisitionYear = acquisitionYear.toInt(),
										CurrencyAmountIDR = currencyAmount.toLong(),
										IsOverseas = isOverseas,
										BrandName = brandName,
										ProductType = productType.ifBlank { null },
										ProductionYear = productionYear.ifBlank { null }?.toInt(),
										Description = description.ifBlank { null }
									)
									
									println(dataModel)
									
									scope.launch {
										isReady = false
										sptManager.saveWealthH(scope, dataModel)
										navigator.pop()
									}
								}
							) {
								Text(
									modifier = Modifier.padding(vertical = 6.dp),
									text = "Simpan",
									fontSize = 16.sp,
									fontWeight = FontWeight.Bold
								)
							}
						}
					}
					
					//Form I
					AssetApiSaveCode.I.toString() -> {
						//Acquisition Price
						item {
							acquisitionPriceTextField()
						}
						
						//Jewelry Type
						item {
							jewelryTypeTextField()
						}
						
						//Unit Name
						item {
							unitNameTextField()
						}
						
						//Owner No
						item {
							ownerNoTextField()
						}
						
						//Submit Button
						item {
							Button(
								enabled = if(acquisitionYear.isNotBlank() && currencyAmount.isNotBlank() && jewelryType.isNotBlank()) true else false,
								modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 22.dp),
								colors = buttonColors(backgroundColor = Colors().buttonActive, contentColor = Color.White),
								onClick = {
									currencyAmount = currencyAmount.replace("Rp. ", "")
									currencyAmount = currencyAmount.replace(",", "")
									
									val dataModel = FormWealthIRequestApiModel(
										Id = id,
										Tr1770HdId = sptHd?.Id ?: 0,
										WealthTypeId = wealthTypeId,
										AcquisitionYear = acquisitionYear.toInt(),
										CurrencyAmountIDR = currencyAmount.toLong(),
										IsOverseas = isOverseas,
										JewerlyType = jewelryType,
										ProductType = productType.ifBlank { null },
										UnitName = unitName.ifBlank { null },
										OwnerNo = ownerNo.ifBlank { null }
									)
									
									println(dataModel)
									
									scope.launch {
										isReady = false
										sptManager.saveWealthI(scope, dataModel)
										navigator.pop()
									}
								}
							) {
								Text(
									modifier = Modifier.padding(vertical = 6.dp),
									text = "Simpan",
									fontSize = 16.sp,
									fontWeight = FontWeight.Bold
								)
							}
						}
					}
					
					//Form J
					AssetApiSaveCode.J.toString() -> {
						//Acquisition Price
						item {
							acquisitionPriceTextField()
						}
						
						//Location
						item {
							locationTextField()
						}
						
						//NOP
						item {
							nopTextField()
						}
						
						//Area
						item {
							areaTextField()
						}
						
						//Owner No
						item {
							ownerNoTextField()
						}
						
						//Submit Button
						item {
							Button(
								enabled = if(acquisitionYear.isNotBlank() && currencyAmount.isNotBlank() && location.isNotBlank()) true else false,
								modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 22.dp),
								colors = buttonColors(backgroundColor = Colors().buttonActive, contentColor = Color.White),
								onClick = {
									currencyAmount = currencyAmount.replace("Rp. ", "")
									currencyAmount = currencyAmount.replace(",", "")
									
									val dataModel = FormWealthJRequestApiModel(
										Id = id,
										Tr1770HdId = sptHd?.Id ?: 0,
										WealthTypeId = wealthTypeId,
										AcquisitionYear = acquisitionYear.toInt(),
										CurrencyAmountIDR = currencyAmount.toLong(),
										IsOverseas = isOverseas,
										Location = location,
										NOP = nop.ifBlank { null },
										Area = area.ifBlank { null },
										OwnerNo = ownerNo.ifBlank { null }
									)
									
									println(dataModel)
									
									scope.launch {
										isReady = false
										sptManager.saveWealthJ(scope, dataModel)
										navigator.pop()
									}
								}
							) {
								Text(
									modifier = Modifier.padding(vertical = 6.dp),
									text = "Simpan",
									fontSize = 16.sp,
									fontWeight = FontWeight.Bold
								)
							}
						}
					}
					
					//Form K
					AssetApiSaveCode.K.toString() -> {
						//Acquisition Price
						item {
							acquisitionPriceTextField()
						}
						
						//Paten Name
						item {
							patenNameTextField()
						}
						
						//Paten No
						item {
							patenNoTextField()
						}
						
						//Paten Start Date
						item {
							patenStartDatePicker()
						}
						
						//Paten Due Date
						item {
							patenDueDatePicker()
						}
						
						//Submit Button
						item {
							Button(
								enabled = if(acquisitionYear.isNotBlank() && currencyAmount.isNotBlank() && patenName.isNotBlank()) true else false,
								modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 22.dp),
								colors = buttonColors(backgroundColor = Colors().buttonActive, contentColor = Color.White),
								onClick = {
									currencyAmount = currencyAmount.replace("Rp. ", "")
									currencyAmount = currencyAmount.replace(",", "")
									
									val dataModel = FormWealthKRequestApiModel(
										Id = id,
										Tr1770HdId = sptHd?.Id ?: 0,
										WealthTypeId = wealthTypeId,
										AcquisitionYear = acquisitionYear.toInt(),
										CurrencyAmountIDR = currencyAmount.toLong(),
										IsOverseas = isOverseas,
										PatenName = patenName,
										PatenNo = patenNo.ifBlank { null },
										PatenStartDate = patenStartDate.ifBlank { null },
										PatenDueDate = patenDueDate.ifBlank { null },
										Description = description.ifBlank { null }
									)
									
									println(dataModel)
									
									scope.launch {
										isReady = false
										sptManager.saveWealthK(scope, dataModel)
										navigator.pop()
									}
								}
							) {
								Text(
									modifier = Modifier.padding(vertical = 6.dp),
									text = "Simpan",
									fontSize = 16.sp,
									fontWeight = FontWeight.Bold
								)
							}
						}
					}
					
					//Form L
					AssetApiSaveCode.L.toString() -> {
						//Currency Selector
						item {
							currencySelector()
						}
						
						//Entry Amount
						item {
							currencyEntryTextField()
						}
						
						//Currency Rate Entry Mode Enum Checkbox
						if(selectedCurrency != "(IDR) Indonesian rupiah") {
							item {
								currRateEntryModeCheckbox()
							}
							
							//Currency Rate
							item {
								currencyRateTextField()
							}
						}
						
						//Total
						item {
							currencyAmountTextField()
						}
						
						//Storage Name
						item {
							storageNameTextField()
						}
						
						//Polis No
						item {
							polisNoTextField()
						}
						
						//Storage Address
						item {
							storageAddressTextField()
						}
						
						//Submit Button
						item {
							Button(
								enabled = if(acquisitionYear.isNotBlank() && currencyAmountEntry.isNotBlank() && currRateIDR.isNotBlank() && storageName.isNotBlank()) true else false,
								modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 22.dp),
								colors = buttonColors(backgroundColor = Colors().buttonActive, contentColor = Color.White),
								onClick = {
									currencyAmount = currencyAmount.replace("Rp. ", "")
									currencyAmount = currencyAmount.replace(",", "")
									currRateIDR = currRateIDR.replace("Rp. ", "")
									currRateIDR = currRateIDR.replace(",", "")
									
									val dataModel = FormWealthLRequestApiModel(
										Id = id,
										Tr1770HdId = sptHd?.Id ?: 0,
										WealthTypeId = wealthTypeId,
										AcquisitionYear = acquisitionYear.toInt(),
										CurrencyId = currencyId,
										CurrencyAmount = currencyAmount.toLong(),
										CurrRateEntryModeE = currRateEntryModeE,
										CurrRateIDR = currRateIDR.ifBlank { null }?.toLong(),
										IsOverseas = isOverseas,
										StorageName = storageName,
										PolisNo = polisNo.ifBlank { null },
										StorageAddress = storageAddress.ifBlank { null }
									)
									
									println(dataModel)
									
									scope.launch {
										isReady = false
										sptManager.saveWealthL(scope, dataModel)
										navigator.pop()
									}
								}
							) {
								Text(
									modifier = Modifier.padding(vertical = 6.dp),
									text = "Simpan",
									fontSize = 16.sp,
									fontWeight = FontWeight.Bold
								)
							}
						}
					}
				}
				
				//Spacer
				item {
					Box(
						modifier = Modifier.height(88.dp)
					)
				}
			}
		}
		
		//Popups
		//Wealth Type Popup
		popUpBox(
			popupWidth = 300f,
			popupHeight = 200f,
			showPopup = showWealthTypePopup,
			onClickOutside = { showWealthTypePopup = false },
			content = {
				Column(
					modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 12.dp)
				) {
					Text(
						text = "Jenis Harta",
						fontSize = 12.sp,
						color = Colors().textBlack,
						fontWeight = FontWeight.Bold,
						modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
					)
					TextField(
						enabled = true,
						shape = CircleShape,
						modifier = Modifier.fillMaxWidth()
							.padding(horizontal = 8.dp, vertical = 8.dp)
							.border(
								border = BorderStroke(1.dp, Colors().textLightGrey),
								shape = CircleShape
							),
						value = wealthTypeSearchText,
						onValueChange = {
							wealthTypeSearchText = it
						},
						singleLine = true,
						colors = textFieldColors(
							backgroundColor = Color(0xFFF8F8F8),
							focusedIndicatorColor = Color.Transparent,
							unfocusedIndicatorColor = Color.Transparent,
							disabledIndicatorColor = Colors().slate20
						),
						keyboardOptions = KeyboardOptions(
							keyboardType = KeyboardType.Text,
							imeAction = ImeAction.Done
						),
						leadingIcon = {
							Image(
								painterResource(Res.drawable.icon_search),
								null,
								modifier = Modifier.size(36.dp).padding(start = 16.dp)
							)
						},
						trailingIcon = {
							if(wealthTypeSearchText != ""){
								Image(
									painterResource(Res.drawable.icon_clear),
									null,
									modifier = Modifier.size(36.dp).padding(end = 16.dp)
										.clickable(true, onClick = {
											wealthTypeSearchText = ""
										})
								)
							}
						}
					)
					LazyColumn(modifier = Modifier.heightIn(max = 200.dp).padding(horizontal = 18.dp)) {
						wealthTypeList.forEach {
							if (it.WealthTypeName.contains(wealthTypeSearchText, ignoreCase = true) && it.WealthTypeCode != AssetCode.PERSEDIAAN_USAHA.value) {
								item {
									Text(
										text = "${it.WealthTypeCode} - ${it.WealthTypeName}",
										fontSize = 18.sp,
										modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
											.clickable(true, onClick = {
												selectedWealthType = "${it.WealthTypeCode} - ${it.WealthTypeName}"
												wealthTypeId = it.Id
												showWealthTypePopup = false
												when (it.WealthTypeCode) {
													//A
													AssetCode.UANG_TUNAI.value -> {
														formCategory = AssetApiSaveCode.A.toString()
													}
													
													//B
													AssetCode.TABUNGAN.value, AssetCode.GIRO.value -> {
														formCategory = AssetApiSaveCode.B.toString()
													}
													
													//C
													AssetCode.DEPOSITO.value -> {
														formCategory = AssetApiSaveCode.C.toString()
													}
													
													//D
													AssetCode.PIUTANG.value,
													AssetCode.PIUTANG_AFILIASI.value,
													AssetCode.PIUTANG_LAINNYA.value,
													AssetCode.OBLIGASI_PERUSAHAAN.value,
													AssetCode.OBLIGASI_PEMERINTAH_INDONESIA.value,
													AssetCode.SURAT_UTANG_LAINNYA.value,
													AssetCode.INSTRUMEN_DERIVATIF.value -> {
														formCategory = AssetApiSaveCode.D.toString()
													}
													
													//E
													AssetCode.PERSEDIAAN_USAHA.value -> {
														formCategory = AssetApiSaveCode.E.toString()
													}
													
													//F
													AssetCode.SAHAM_YANG_DIBELI_UNTUK_DIJUAL_KEMBALI.value,
													AssetCode.SAHAM.value -> {
														formCategory = AssetApiSaveCode.F.toString()
													}
													
													//G
													AssetCode.PENYERTAAN_MODAL_DALAM_PERUSAHAAN_LAIN_YANG_TIDAK_ATAS_SAHAM.value,
													AssetCode.INVESTASI_LAINNYA.value -> {
														formCategory = AssetApiSaveCode.G.toString()
													}
													
													//H
													AssetCode.SEPEDA.value,
													AssetCode.SEPEDA_MOTOR.value,
													AssetCode.MOBIL.value,
													AssetCode.ALAT_TRANSPORTASI_LAINNYA.value,
													AssetCode.KAPAL_PESIAR_PESAWAT_TERBANG_HELIKOPTER_JETSKI_PERALATAN_OLAH_RAGA_KHUSUS.value,
													AssetCode.PERALATAN_ELEKTRONIK_FURINITURE.value,
													AssetCode.HARTA_BERGERAK_LAINNYA.value-> {
														formCategory = AssetApiSaveCode.H.toString()
													}
													
													//I
													AssetCode.LOGAM_MULIA.value,
													AssetCode.BATU_MULIA.value,
													AssetCode.BARANG_SENI_DAN_ANTIK.value -> {
														formCategory = AssetApiSaveCode.I.toString()
													}
													
													//J
													AssetCode.TANAH_DAN_ATAU_BANGUNAN_UNTUK_TEMPAT_TINGGAL.value,
													AssetCode.TANAH_DAN_ATAU_BANGUNAN_UNTUK_USAHA.value,
													AssetCode.TANAH_ATAU_LAHAN_UNTUK_USAHA.value,
													AssetCode.HARTA_TIDAK_GERAK_LAINNYA.value-> {
														formCategory = AssetApiSaveCode.J.toString()
													}
													
													//K
													AssetCode.PATEN.value,
													AssetCode.ROYALTI.value,
													AssetCode.MEREK_DAGANG.value,
													AssetCode.HARTA_TIDAK_BERWUJUD_LAINNYA.value-> {
														formCategory = AssetApiSaveCode.K.toString()
													}
													
													//L
													AssetCode.SETARA_KAS_LAINNYA.value,
													AssetCode.REKSADANA.value-> {
														formCategory = AssetApiSaveCode.L.toString()
													}
												}
											})
									)
								}
							}
						}
					}
				}
			}
		)
		
		//Currency List Popup
		popUpBox(
			popupWidth = 300f,
			popupHeight = 200f,
			showPopup = showCurrencyPopup,
			onClickOutside = { showCurrencyPopup = false },
			content = {
				Column(
					modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 12.dp)
				) {
					Text(
						text = "Mata Uang",
						fontSize = 12.sp,
						color = Colors().textBlack,
						fontWeight = FontWeight.Bold,
						modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
					)
					TextField(
						enabled = true,
						shape = CircleShape,
						modifier = Modifier.fillMaxWidth()
							.padding(horizontal = 8.dp, vertical = 8.dp)
							.border(
								border = BorderStroke(1.dp, Colors().textLightGrey),
								shape = CircleShape
							),
						value = currencSearchText,
						onValueChange = {
							currencSearchText = it
						},
						singleLine = true,
						colors = textFieldColors(
							backgroundColor = Color(0xFFF8F8F8),
							focusedIndicatorColor = Color.Transparent,
							unfocusedIndicatorColor = Color.Transparent,
							disabledIndicatorColor = Colors().slate20
						),
						keyboardOptions = KeyboardOptions(
							keyboardType = KeyboardType.Text,
							imeAction = ImeAction.Done
						),
						leadingIcon = {
							Image(
								painterResource(Res.drawable.icon_search),
								null,
								modifier = Modifier.size(36.dp).padding(start = 16.dp)
							)
						},
						trailingIcon = {
							if(currencSearchText != ""){
								Image(
									painterResource(Res.drawable.icon_clear),
									null,
									modifier = Modifier.size(36.dp).padding(end = 16.dp)
										.clickable(true, onClick = {
											currencSearchText = ""
										})
								)
							}
						}
					)
					LazyColumn(modifier = Modifier.heightIn(max = 200.dp).padding(horizontal = 18.dp)) {
						currencyList.forEach {
							if (it.Name.contains(currencSearchText, ignoreCase = true) || it.Code.contains(currencSearchText, ignoreCase = true)) {
								item {
									Text(
										text = "(${it.Code}) ${it.Name}",
										fontSize = 18.sp,
										modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
											.clickable(true, onClick = {
												selectedCurrency = "(${it.Code}) ${it.Name}"
												currencyId = it.Id
												showCurrencyPopup = false
												currRateIDR = CurrencyFormatter(getSelectedRate().toString())
											})
									)
								}
							}
						}
					}
				}
			}
		)
		
		//Delete Dependent Popup
		popUpBox(
			showPopup = showDeletePopup,
			onClickOutside = { showDeletePopup = false },
			content = {
				Text(
					text = "Hapus Harta",
					fontSize = 16.sp,
					color = Colors().textRed,
					modifier = Modifier.padding(vertical = 24.dp)
						.clickable(true, onClick = {
							showDeletePopup = false
							showConfirmPopup = true
						})
				)
			}
		)
		
		//Confirm Delete Popup
		popUpBox(
			showPopup = showConfirmPopup,
			onClickOutside = { showConfirmPopup = false },
			content = {
				Column(horizontalAlignment = Alignment.CenterHorizontally) {
					Text(
						modifier = Modifier.padding(vertical = 24.dp),
						text = "Hapus Harta",
						fontSize = 18.sp,
						color = Colors().textBlack,
						fontWeight = FontWeight.Bold
					)
					
					Text(
						modifier = Modifier.padding(vertical = 24.dp, horizontal = 16.dp),
						text = "Apakah Anda yakin ingin menghapus Harta ini?",
						fontSize = 16.sp,
						textAlign = TextAlign.Center,
						color = Colors().textBlack
					)
					
					Text(
						text = "Hapus",
						fontSize = 14.sp,
						color = Colors().textRed,
						modifier = Modifier.padding(vertical = 24.dp)
							.clickable(true, onClick = {
								scope.launch{
									isReady = false
									sptManager.deleteWealth(scope, id.toString())
									navigator.pop()
								}
							})
					)
					
					Box(
						modifier = Modifier
							.fillMaxWidth()
							.padding(horizontal = 16.dp)
							.height(1.dp)
							.background(Colors().slate30)
					)
					
					Text(
						text = "Kembali",
						fontSize = 14.sp,
						color = Colors().textBlack,
						modifier = Modifier.padding(vertical = 24.dp)
							.clickable(true, onClick = {
								showConfirmPopup = false
							})
					)
				}
			}
		)
		
		//Start Date Picker Popup
		popUpBox(
			showPopup = showStartDatePopup,
			onClickOutside = { showStartDatePopup = false },
			content = {
				Column {
					Box(modifier = Modifier.height(16.dp))
					WheelDatePicker(
						height = 256.dp,
						title = "Pilih Tanggal Mulai",
						doneLabel = "Selesai",
						yearsRange = IntRange(1920, (sptHd?.TaxYear?.toInt() ?: LocalDate.now().year) - 1),
						titleStyle = TextStyle(fontSize = 16.sp, color = Colors().textBlack, fontWeight = FontWeight.Bold),
						doneLabelStyle = TextStyle(fontSize = 14.sp, color = Colors().textClickable, fontWeight = FontWeight.Bold),
						showShortMonths = true,
						startDate = LocalDate.now().minus(DatePeriod(1,0,0)),
						onDoneClick = {
							startDate = it.toString()
							showStartDatePopup = false
						}
					)
				}
			}
		)
		
		//Due Date Picker Popup
		popUpBox(
			showPopup = showDueDatePopup,
			onClickOutside = { showDueDatePopup = false },
			content = {
				Column {
					Box(modifier = Modifier.height(16.dp))
					WheelDatePicker(
						height = 256.dp,
						title = "Pilih Tanggal Jatuh Tempo",
						doneLabel = "Selesai",
//						yearsRange = IntRange(1920, (sptHd?.TaxYear?.toInt() ?: LocalDate.now().year) - 1),
						titleStyle = TextStyle(fontSize = 16.sp, color = Colors().textBlack, fontWeight = FontWeight.Bold),
						doneLabelStyle = TextStyle(fontSize = 14.sp, color = Colors().textClickable, fontWeight = FontWeight.Bold),
						showShortMonths = true,
						startDate = LocalDate.now().minus(DatePeriod(1,0,0)),
						onDoneClick = {
							dueDate = it.toString()
							showDueDatePopup = false
						}
					)
				}
			}
		)
		
		//Interest Type List Popup
		popUpBox(
			popupWidth = 300f,
			popupHeight = 200f,
			showPopup = showInterestPopup,
			onClickOutside = { showInterestPopup = false },
			content = {
				Column(
					modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 12.dp)
				) {
					Text(
						text = "Jenis Bunga",
						fontSize = 12.sp,
						color = Colors().textBlack,
						fontWeight = FontWeight.Bold,
						modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
					)
					TextField(
						enabled = true,
						shape = CircleShape,
						modifier = Modifier.fillMaxWidth()
							.padding(horizontal = 8.dp, vertical = 8.dp)
							.border(
								border = BorderStroke(1.dp, Colors().textLightGrey),
								shape = CircleShape
							),
						value = interestSearchText,
						onValueChange = {
							interestSearchText = it
						},
						singleLine = true,
						colors = textFieldColors(
							backgroundColor = Color(0xFFF8F8F8),
							focusedIndicatorColor = Color.Transparent,
							unfocusedIndicatorColor = Color.Transparent,
							disabledIndicatorColor = Colors().slate20
						),
						keyboardOptions = KeyboardOptions(
							keyboardType = KeyboardType.Text,
							imeAction = ImeAction.Done
						),
						leadingIcon = {
							Image(
								painterResource(Res.drawable.icon_search),
								null,
								modifier = Modifier.size(36.dp).padding(start = 16.dp)
							)
						},
						trailingIcon = {
							if(interestSearchText != ""){
								Image(
									painterResource(Res.drawable.icon_clear),
									null,
									modifier = Modifier.size(36.dp).padding(end = 16.dp)
										.clickable(true, onClick = {
											interestSearchText = ""
										})
								)
							}
						}
					)
					LazyColumn(modifier = Modifier.heightIn(max = 200.dp).padding(horizontal = 18.dp)) {
						interestTypeList.forEach {
							if (it.Name.contains(interestSearchText, ignoreCase = true)) {
								item {
									Text(
										text = it.Name,
										fontSize = 18.sp,
										modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
											.clickable(true, onClick = {
												selectedInterest = it.Name
												interestTypeId = it.Id
												showInterestPopup = false
											})
									)
								}
							}
						}
					}
				}
			}
		)
		
		//Paten Start Date Picker Popup
		popUpBox(
			showPopup = showPatenStartPopup,
			onClickOutside = { showPatenStartPopup = false },
			content = {
				Column {
					Box(modifier = Modifier.height(16.dp))
					WheelDatePicker(
						height = 256.dp,
						title = "Pilih Tanggal Berlaku",
						doneLabel = "Selesai",
						yearsRange = IntRange(1920, (sptHd?.TaxYear?.toInt() ?: LocalDate.now().year) - 1),
						titleStyle = TextStyle(fontSize = 16.sp, color = Colors().textBlack, fontWeight = FontWeight.Bold),
						doneLabelStyle = TextStyle(fontSize = 14.sp, color = Colors().textClickable, fontWeight = FontWeight.Bold),
						showShortMonths = true,
						startDate = LocalDate.now().minus(DatePeriod(1,0,0)),
						onDoneClick = {
							patenStartDate = it.toString()
							showPatenStartPopup = false
						}
					)
				}
			}
		)
		
		//Paten End Date Picker Popup
		popUpBox(
			showPopup = showPatenEndPopup,
			onClickOutside = { showPatenEndPopup = false },
			content = {
				Column {
					Box(modifier = Modifier.height(16.dp))
					WheelDatePicker(
						height = 256.dp,
						title = "Pilih Tanggal Kadaluarsa",
						doneLabel = "Selesai",
//						yearsRange = IntRange(1920, (sptHd?.TaxYear?.toInt() ?: LocalDate.now().year) - 1),
						titleStyle = TextStyle(fontSize = 16.sp, color = Colors().textBlack, fontWeight = FontWeight.Bold),
						doneLabelStyle = TextStyle(fontSize = 14.sp, color = Colors().textClickable, fontWeight = FontWeight.Bold),
						showShortMonths = true,
						startDate = LocalDate.now().minus(DatePeriod(1,0,0)),
						onDoneClick = {
							patenDueDate = it.toString()
							showPatenEndPopup = false
						}
					)
				}
			}
		)
	}
}