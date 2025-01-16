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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
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
import androidx.compose.ui.text.Placeholder
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
import global.AssetCode
import global.Colors
import global.FinalIncomeType
import global.JobName
import global.PreferencesKey.Companion.CityId
import global.PreferencesKey.Companion.NPWP
import global.universalUIComponents.loadingPopupBox
import global.universalUIComponents.popUpBox
import http.Account
import http.Interfaces
import kotlinx.coroutines.launch
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import models.ApiODataQueryModel
import models.master.CityModel
import models.master.KluModel
import models.transaction.BrutoCirculationRequestApiModel
import models.transaction.Form1770HdResponseApiModel
import models.transaction.FormFinalIncomeARequestApiModel
import models.transaction.FormFinalIncomeBRequestApiModel
import models.transaction.FormFinalIncomeCRequestApiModel
import models.transaction.FormFinalIncomeDRequestApiModel
import models.transaction.FormFinalIncomeERequestApiModel
import models.transaction.FormWealthBRequestApiModel
import models.transaction.FormWealthResponseApiModel
import network.chaintech.kmp_date_time_picker.ui.datepicker.WheelDatePickerComponent.WheelDatePicker
import network.chaintech.kmp_date_time_picker.utils.now
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource
import util.BigDeciToString
import util.CurrencyFormatter
import util.DateFormatter

class FinalIncomeFormScreen(val id: Int, val sptHd: Form1770HdResponseApiModel?, val client: Account, val sptPertamaClient: Interfaces, val prefs: DataStore<Preferences>): Screen {
	
	val sptManager = SPTManager(prefs, client, sptPertamaClient)
	
	@Composable
	override fun Content() {
		
		val scope = rememberCoroutineScope()
		val navigator = LocalNavigator.currentOrThrow
		
		var showDeletePopup by remember { mutableStateOf(false) }
		var showConfirmPopup by remember { mutableStateOf(false) }
		var showAssetListPopup by remember { mutableStateOf(false) }
		var showIncomeTypePopup by remember { mutableStateOf(false) }
		var showRentStartDatePopup by remember { mutableStateOf(false) }
		var showRentEndDatePopup by remember { mutableStateOf(false) }
		var showKLUPopup by remember { mutableStateOf(false) }
		var showCityPopup by remember { mutableStateOf(false) }
		
		var kluSearchText by remember { mutableStateOf("") }
		var citySearchText by remember { mutableStateOf("") }
		
		var incomeTypeE by remember { mutableStateOf("") }
		var selectedIncomeType by remember { mutableStateOf("") }
		
		var wealthId by remember { mutableStateOf("") }
		var selectedWealth by remember { mutableStateOf("") }
		
		var incomeIDR by remember { mutableStateOf("") }
		var taxPayableIDR by remember { mutableStateOf("") }
		var description by remember { mutableStateOf("") }
		
		var netIncomeIDR by remember { mutableStateOf("") }
		
		var sellPriceIDR by remember { mutableStateOf("") }
		
		var assetList by remember { mutableStateOf<List<FormWealthResponseApiModel>>(emptyList()) }
		var selectedAssetList by remember { mutableStateOf<List<FormWealthResponseApiModel>>(emptyList()) }
		
		var rentStartDate by remember { mutableStateOf("") }
		var rentEndDate by remember { mutableStateOf("") }
		
		var formType by remember { mutableStateOf("") }
		var isReady by remember { mutableStateOf(false) }
		
		var kluList by remember { mutableStateOf<List<KluModel>>(emptyList())}
		var selectedKlu by remember { mutableStateOf("") }
		var kluId by remember { mutableStateOf(0) }
		
		var cityList by remember { mutableStateOf<List<CityModel>>(emptyList())}
		var selectedCity by remember { mutableStateOf("") }
		var cityId by remember { mutableStateOf(0) }
		
		var businessNPWP by remember { mutableStateOf("") }
		var businessAddress by remember { mutableStateOf("") }
		var kpp by remember { mutableStateOf("") }
		
		var bruto_1 by remember { mutableStateOf("") }
		var bruto_2 by remember { mutableStateOf("") }
		var bruto_3 by remember { mutableStateOf("") }
		var bruto_4 by remember { mutableStateOf("") }
		var bruto_5 by remember { mutableStateOf("") }
		var bruto_6 by remember { mutableStateOf("") }
		var bruto_7 by remember { mutableStateOf("") }
		var bruto_8 by remember { mutableStateOf("") }
		var bruto_9 by remember { mutableStateOf("") }
		var bruto_10 by remember { mutableStateOf("") }
		var bruto_11 by remember { mutableStateOf("") }
		var bruto_12 by remember { mutableStateOf("") }
		
		var pph_1 by remember { mutableStateOf("") }
		var pph_2 by remember { mutableStateOf("") }
		var pph_3 by remember { mutableStateOf("") }
		var pph_4 by remember { mutableStateOf("") }
		var pph_5 by remember { mutableStateOf("") }
		var pph_6 by remember { mutableStateOf("") }
		var pph_7 by remember { mutableStateOf("") }
		var pph_8 by remember { mutableStateOf("") }
		var pph_9 by remember { mutableStateOf("") }
		var pph_10 by remember { mutableStateOf("") }
		var pph_11 by remember { mutableStateOf("") }
		var pph_12 by remember { mutableStateOf("") }
		
		fun getFilteredAsset(): List<FormWealthResponseApiModel> {
			when(incomeTypeE) {
				FinalIncomeType.BUNGA_DEPOSITO_TABUNGAN_DISKONTO_SUN.value.toString() -> {
					return assetList.filter { it.WealthType.WealthTypeCode == AssetCode.TABUNGAN.value
							|| it.WealthType.WealthTypeCode == AssetCode.DEPOSITO.value
							|| it.WealthType.WealthTypeCode == AssetCode.GIRO.value
							|| it.WealthType.WealthTypeCode == AssetCode.SETARA_KAS_LAINNYA.value
							|| it.WealthType.WealthTypeCode == AssetCode.SURAT_UTANG_LAINNYA.value }
				}
				FinalIncomeType.BUNGA_DISKONTO_OBLIGASI.value.toString() -> {
					return assetList.filter { it.WealthType.WealthTypeCode == AssetCode.OBLIGASI_PEMERINTAH_INDONESIA.value
							|| it.WealthType.WealthTypeCode == AssetCode.OBLIGASI_PERUSAHAAN.value }
				}
				FinalIncomeType.SAHAM_BE.value.toString() -> {
					return assetList.filter { it.WealthType.WealthTypeCode == AssetCode.SAHAM_YANG_DIBELI_UNTUK_DIJUAL_KEMBALI.value }
				}
				FinalIncomeType.ALIH_HAK_TANAH_BANGUNAN.value.toString() -> {
					return assetList.filter { it.WealthType.WealthTypeCode == AssetCode.TANAH_DAN_ATAU_BANGUNAN_UNTUK_TEMPAT_TINGGAL.value
							|| it.WealthType.WealthTypeCode == AssetCode.TANAH_DAN_ATAU_BANGUNAN_UNTUK_USAHA.value
							|| it.WealthType.WealthTypeCode == AssetCode.TANAH_ATAU_LAHAN_UNTUK_USAHA.value }
				}
				FinalIncomeType.BANGUNAN_GUNA_SERAH.value.toString(), FinalIncomeType.SEWA_TANAH_BANGUNAN.value.toString() -> {
					return assetList.filter { it.WealthType.WealthTypeCode == AssetCode.TANAH_DAN_ATAU_BANGUNAN_UNTUK_TEMPAT_TINGGAL.value
							|| it.WealthType.WealthTypeCode == AssetCode.TANAH_DAN_ATAU_BANGUNAN_UNTUK_USAHA.value
							|| it.WealthType.WealthTypeCode == AssetCode.TANAH_ATAU_LAHAN_UNTUK_USAHA.value
							|| it.WealthType.WealthTypeCode == AssetCode.HARTA_TIDAK_GERAK_LAINNYA.value }
				}
				FinalIncomeType.BUNGA_KOPERASI.value.toString() -> {
					return assetList.filter { it.WealthType.WealthTypeCode == AssetCode.PENYERTAAN_MODAL_DALAM_PERUSAHAAN_LAIN_YANG_TIDAK_ATAS_SAHAM.value }
				}
				FinalIncomeType.DIVIDEN.value.toString() -> {
					return assetList.filter { it.WealthType.WealthTypeCode == AssetCode.SAHAM.value
							|| it.WealthType.WealthTypeCode == AssetCode.SAHAM_YANG_DIBELI_UNTUK_DIJUAL_KEMBALI.value
							|| it.WealthType.WealthTypeCode == AssetCode.INVESTASI_LAINNYA.value
							|| it.WealthType.WealthTypeCode == AssetCode.PENYERTAAN_MODAL_DALAM_PERUSAHAAN_LAIN_YANG_TIDAK_ATAS_SAHAM.value
							|| it.WealthType.WealthTypeCode == AssetCode.REKSADANA.value
							|| it.WealthType.WealthTypeCode == AssetCode.OBLIGASI_PERUSAHAAN.value
							|| it.WealthType.WealthTypeCode == AssetCode.OBLIGASI_PEMERINTAH_INDONESIA.value}
				}
				else -> { return listOf() }
			}
		}
		
		suspend fun populateKluList() {
			var initGetData = true
			val itemCountPerPage = 100
			val odataQuery = ApiODataQueryModel(itemCountPerPage)
			var currentItemCount = 0
			
			while (initGetData || currentItemCount == itemCountPerPage) {
				currentItemCount = 0
				odataQuery.PageNum++
				initGetData = false
				
				try {
					val apiResult = sptManager.getKluList(scope, odataQuery, sptHd!!.TaxYear.toInt())
					if (apiResult.isNotEmpty()) {
						currentItemCount += apiResult.size
						kluList += apiResult
						println(apiResult[apiResult.size - 1].Id)
					} else break
				}
				catch (ex: Exception) {
					break
				}
			}
		}
		
		LaunchedEffect(null) {
			assetList = sptManager.getWealthData(scope, sptHd!!.Id.toString())
			cityList = sptManager.getCityList(scope)
			populateKluList()
			
			if (id != 0) {
				val oldData = sptManager.getFinalIncomeDataById(scope, id.toString())
				
				if (oldData != null) {
					incomeTypeE = oldData.IncomeTypeE.toString()
					
					selectedIncomeType = FinalIncomeType.fromValue(incomeTypeE.toInt()).toString()
					formType = FinalIncomeType.formTypeFromValue(incomeTypeE.toInt()).toString()
					
					wealthId = oldData.Wealth?.Id.toString()
					
					selectedWealth = oldData.Wealth?.WealthType?.WealthTypeName ?: ""
					incomeIDR = "Rp ${CurrencyFormatter(BigDeciToString(oldData.IncomeIDR.toString()))}"
					taxPayableIDR = "Rp ${CurrencyFormatter(BigDeciToString(oldData.TaxPayableIDR.toString()))}"
					description = oldData.Description ?: ""
					
					sellPriceIDR = "Rp ${CurrencyFormatter(BigDeciToString(oldData.SellPriceIDR.toString()))}"
					
					rentStartDate = DateFormatter(oldData.RentStartDate ?: "")
					rentEndDate = DateFormatter(oldData.RentEndDate ?: "")
					
					selectedAssetList = getFilteredAsset()
					println(oldData)
				}
			}
			
			isReady = true
		}
		
		fun getBrutoSum(): String {
			return ((bruto_1.ifBlank { "0" }).toLong() +
					(bruto_2.ifBlank { "0" }).toLong() +
					(bruto_3.ifBlank { "0" }).toLong() +
					(bruto_4.ifBlank { "0" }).toLong() +
					(bruto_5.ifBlank { "0" }).toLong() +
					(bruto_6.ifBlank { "0" }).toLong() +
					(bruto_7.ifBlank { "0" }).toLong() +
					(bruto_8.ifBlank { "0" }).toLong() +
					(bruto_9.ifBlank { "0" }).toLong() +
					(bruto_10.ifBlank { "0" }).toLong() +
					(bruto_11.ifBlank { "0" }).toLong() +
					(bruto_12.ifBlank { "0" }).toLong()).toString()
		}
		
		fun getPphSum(): String {
			return ((pph_1.ifBlank { "0" }).toLong() +
					(pph_2.ifBlank { "0" }).toLong() +
					(pph_3.ifBlank { "0" }).toLong() +
					(pph_4.ifBlank { "0" }).toLong() +
					(pph_5.ifBlank { "0" }).toLong() +
					(pph_6.ifBlank { "0" }).toLong() +
					(pph_7.ifBlank { "0" }).toLong() +
					(pph_8.ifBlank { "0" }).toLong() +
					(pph_9.ifBlank { "0" }).toLong() +
					(pph_10.ifBlank { "0" }).toLong() +
					(pph_11.ifBlank { "0" }).toLong() +
					(pph_12.ifBlank { "0" }).toLong()).toString()
		}
		
		@Composable
		fun incomeTypeSelector() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp),
				text = "Jenis Penghasilan",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			
			Box(
				modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
					.background(if(id == 0) Color.White else Colors().slate20, RoundedCornerShape(4.dp))
					.border(1.dp, Colors().textDarkGrey, RoundedCornerShape(4.dp))
					.clickable(id == 0, onClick = {
						showIncomeTypePopup = true
					})
			) {
				Row (
					modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
					horizontalArrangement = Arrangement.SpaceBetween,
					verticalAlignment = Alignment.CenterVertically
				){
					Text(
						text = selectedIncomeType.ifBlank { "Pilih Jenis Penghasilan" },
						fontSize = 14.sp,
						color = if(selectedIncomeType == "" || id != 0) Colors().textDarkGrey else Colors().textBlack
					)
					Image(painterResource(Res.drawable.Icon_Dropdown_Arrow), null, modifier = Modifier.size(24.dp))
				}
			}
		}
		
		@Composable
		fun incomeTextField() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Dasar Pengenaan Pajak / Penghasilan Bruto (IDR)",
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
				value = incomeIDR,
				onValueChange = {
					incomeIDR = it
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
		fun netIncomeTextField() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Penghasilan Neto (Realized Gain / Loss)",
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
				value = netIncomeIDR,
				onValueChange = {
					netIncomeIDR = it
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
		fun wealthSelector() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Nama Aset",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			
			Box(
				modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
					.background(Color.White, RoundedCornerShape(4.dp))
					.border(1.dp, Colors().textDarkGrey, RoundedCornerShape(4.dp))
					.clickable(true, onClick = {
						showAssetListPopup = true
					})
			) {
				Row (
					modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
					horizontalArrangement = Arrangement.SpaceBetween,
					verticalAlignment = Alignment.CenterVertically
				){
					Text(
						text = selectedWealth.ifBlank { "011 - Uang Tunai" },
						fontSize = 14.sp,
						color = if(selectedWealth == "") Colors().textDarkGrey else Colors().textBlack
					)
					Image(painterResource(Res.drawable.Icon_Dropdown_Arrow), null, modifier = Modifier.size(24.dp))
				}
			}
		}
		
		@Composable
		fun pphTextField() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "PPh Terutang (IDR)",
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
				value = taxPayableIDR,
				onValueChange = {
					taxPayableIDR = it
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
		fun descriptionTextField() {
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
				value = description,
				onValueChange = {
					description = it
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
		fun sellPriceTextField() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Harga Jual (IDR)",
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
				value = sellPriceIDR,
				onValueChange = {
					sellPriceIDR = it
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
		fun rentStartDatePicker() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Tanggal Mulai Sewa",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			Box(
				modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
//							.background(Colors().slate20, RoundedCornerShape(4.dp))
					.border(1.dp, Colors().textDarkGrey, RoundedCornerShape(4.dp))
					.clickable(true, onClick = {
						showRentStartDatePopup = true
					})
			) {
				Row (
					modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
					horizontalArrangement = Arrangement.SpaceBetween,
					verticalAlignment = Alignment.CenterVertically
				){
					Text(
						text = if(rentStartDate == "") "Pilih tanggal mulai sewa" else DateFormatter(rentStartDate),
						fontSize = 14.sp,
						color = if(rentStartDate == "") Colors().textDarkGrey else Colors().textBlack
					)
					Image(painterResource(Res.drawable.icon_calendar), null, modifier = Modifier.size(24.dp))
				}
			}
		}
		
		@Composable
		fun rentEndDatePicker() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Tanggal Akhir Sewa",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			Box(
				modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
//							.background(Colors().slate20, RoundedCornerShape(4.dp))
					.border(1.dp, Colors().textDarkGrey, RoundedCornerShape(4.dp))
					.clickable(true, onClick = {
						showRentEndDatePopup = true
					})
			) {
				Row (
					modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
					horizontalArrangement = Arrangement.SpaceBetween,
					verticalAlignment = Alignment.CenterVertically
				){
					Text(
						text = if(rentEndDate == "") "Pilih tanggal akhir sewa" else DateFormatter(rentEndDate),
						fontSize = 14.sp,
						color = if(rentEndDate == "") Colors().textDarkGrey else Colors().textBlack
					)
					Image(painterResource(Res.drawable.icon_calendar), null, modifier = Modifier.size(24.dp))
				}
			}
		}
		
		@Composable
		fun kluSelector() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "KLU",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			
			Box(
				modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
//							.background(Colors().slate20, RoundedCornerShape(4.dp))
					.border(1.dp, Colors().textDarkGrey, RoundedCornerShape(4.dp))
					.clickable(true, onClick = {
						showKLUPopup = true
					})
			) {
				Row (
					modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
					horizontalArrangement = Arrangement.SpaceBetween,
					verticalAlignment = Alignment.CenterVertically
				){
					Text(
						text = if(selectedKlu == "") "Pilih Klasifikasi Lapangan Usaha" else selectedKlu,
						fontSize = 14.sp,
						color = if(selectedKlu == "") Colors().textDarkGrey else Colors().textBlack
					)
					Image(painterResource(Res.drawable.Icon_Dropdown_Arrow), null, modifier = Modifier.size(24.dp))
				}
			}
		}
		
		@Composable
		fun citySelector() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Kota Tempat Usaha",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			
			Box(
				modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
//							.background(Colors().slate20, RoundedCornerShape(4.dp))
					.border(1.dp, Colors().textDarkGrey, RoundedCornerShape(4.dp))
					.clickable(true, onClick = {
						showCityPopup = true
					})
			) {
				Row (
					modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
					horizontalArrangement = Arrangement.SpaceBetween,
					verticalAlignment = Alignment.CenterVertically
				){
					Text(
						text = if(selectedCity == "") "Pilih Kota" else selectedCity,
						fontSize = 14.sp,
						color = if(selectedCity == "") Colors().textDarkGrey else Colors().textBlack
					)
					Image(painterResource(Res.drawable.Icon_Dropdown_Arrow), null, modifier = Modifier.size(24.dp))
				}
			}
		}
		
		@Composable
		fun businessNPWPTextField() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "NPWP Tempat Usaha",
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
				value = businessNPWP,
				onValueChange = {
					if(businessNPWP.length <= 16){ businessNPWP = it }
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
		fun businessAddressTextField() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Alamat Tempat Usaha",
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
				value = businessAddress,
				onValueChange = {
					if(businessAddress.length <= 500){ businessAddress = it }
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
		fun kppTextField() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Lokasi KPP dari tempat registrasi NPWP usaha",
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
				value = kpp,
				onValueChange = {
					if(kpp.length <= 100) { kpp = it }
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
		fun brutoTable() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Rincian Peredaran Bruto",
				fontSize = 12.sp,
				color = Colors().textBlack,
				fontWeight = FontWeight.Bold
			)
			Row(
				modifier = Modifier.height(56.dp).fillMaxWidth().padding(horizontal = 16.dp).padding(top = 8.dp).clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)).background(Colors().brandDark40),
				verticalAlignment = Alignment.CenterVertically
			) {
				Text(
					text = "Masa",
					fontSize = 14.sp,
					color = Color.White,
					fontWeight = FontWeight.Bold,
					modifier = Modifier.padding(start = 8.dp).weight(0.2f)
				)
				
				Text(
					text = "Peredaran Bruto",
					fontSize = 14.sp,
					color = Color.White,
					fontWeight = FontWeight.Bold,
					modifier = Modifier.padding(start = 8.dp).weight(0.4f)
				)
				
				Text(
					text = "PPh Dibayar",
					fontSize = 14.sp,
					color = Color.White,
					fontWeight = FontWeight.Bold,
					modifier = Modifier.padding(start = 8.dp).weight(0.4f)
				)
			}
			
			Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(bottom = 8.dp).clip(RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)).background(Colors().slate20)) {
				Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
					//Jan
					Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
						Text(text = "Jan", fontSize = 14.sp, color = Colors().textBlack, modifier = Modifier.padding(vertical = 16.dp).padding(start = 8.dp).weight(0.2f))
						TextField(
							enabled = true,
							modifier = Modifier.fillMaxWidth().weight(0.4f),
							value = bruto_1,
							onValueChange = {
								bruto_1 = it
								pph_1 = if(bruto_1.isBlank()) "0" else (bruto_1.toLong() * 5 / 1000).toString()
							},
							singleLine = true,
							placeholder = {
								Text(
									text = "0",
									color = Colors().textDarkGrey
								)
							},
							colors = textFieldColors(
								backgroundColor = Colors().slate20,
								focusedIndicatorColor = Color.Transparent,
								unfocusedIndicatorColor = Color.Transparent,
								disabledIndicatorColor = Colors().slate20
							),
							keyboardOptions = KeyboardOptions(
								keyboardType = KeyboardType.Number,
								imeAction = ImeAction.Next
							)
						)
						Text(text = pph_1.ifBlank { "0" }, fontSize = 14.sp, color = Colors().textBlack, modifier = Modifier.padding(vertical = 16.dp).padding(start = 8.dp).weight(0.4f))
					}
					
					//Feb
					Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
						Text(text = "Feb", fontSize = 14.sp, color = Colors().textBlack, modifier = Modifier.padding(vertical = 16.dp).padding(start = 8.dp).weight(0.2f))
						TextField(
							enabled = true,
							modifier = Modifier.fillMaxWidth().weight(0.4f),
							value = bruto_2,
							onValueChange = {
								bruto_2 = it
								pph_2 = if(bruto_2.isBlank()) "0" else (bruto_2.toLong() * 5 / 1000).toString()
							},
							singleLine = true,
							placeholder = {
								Text(
									text = "0",
									color = Colors().textDarkGrey
								)
							},
							colors = textFieldColors(
								backgroundColor = Colors().slate20,
								focusedIndicatorColor = Color.Transparent,
								unfocusedIndicatorColor = Color.Transparent,
								disabledIndicatorColor = Colors().slate20
							),
							keyboardOptions = KeyboardOptions(
								keyboardType = KeyboardType.Number,
								imeAction = ImeAction.Next
							)
						)
						Text(text = pph_2.ifBlank { "0" }, fontSize = 14.sp, color = Colors().textBlack, modifier = Modifier.padding(vertical = 16.dp).padding(start = 8.dp).weight(0.4f))
					}
					
					//Mar
					Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
						Text(text = "Mar", fontSize = 14.sp, color = Colors().textBlack, modifier = Modifier.padding(vertical = 16.dp).padding(start = 8.dp).weight(0.2f))
						TextField(
							enabled = true,
							modifier = Modifier.fillMaxWidth().weight(0.4f),
							value = bruto_3,
							onValueChange = {
								bruto_3 = it
								pph_3 = if(bruto_3.isBlank()) "0" else (bruto_3.toLong() * 5 / 1000).toString()
							},
							singleLine = true,
							placeholder = {
								Text(
									text = "0",
									color = Colors().textDarkGrey
								)
							},
							colors = textFieldColors(
								backgroundColor = Colors().slate20,
								focusedIndicatorColor = Color.Transparent,
								unfocusedIndicatorColor = Color.Transparent,
								disabledIndicatorColor = Colors().slate20
							),
							keyboardOptions = KeyboardOptions(
								keyboardType = KeyboardType.Number,
								imeAction = ImeAction.Next
							)
						)
						Text(text = pph_3.ifBlank { "0" }, fontSize = 14.sp, color = Colors().textBlack, modifier = Modifier.padding(vertical = 16.dp).padding(start = 8.dp).weight(0.4f))
					}
					
					//Apr
					Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
						Text(text = "Apr", fontSize = 14.sp, color = Colors().textBlack, modifier = Modifier.padding(vertical = 16.dp).padding(start = 8.dp).weight(0.2f))
						TextField(
							enabled = true,
							modifier = Modifier.fillMaxWidth().weight(0.4f),
							value = bruto_4,
							onValueChange = {
								bruto_4 = it
								pph_4 = if(bruto_4.isBlank()) "0" else (bruto_4.toLong() * 5 / 1000).toString()
							},
							singleLine = true,
							placeholder = {
								Text(
									text = "0",
									color = Colors().textDarkGrey
								)
							},
							colors = textFieldColors(
								backgroundColor = Colors().slate20,
								focusedIndicatorColor = Color.Transparent,
								unfocusedIndicatorColor = Color.Transparent,
								disabledIndicatorColor = Colors().slate20
							),
							keyboardOptions = KeyboardOptions(
								keyboardType = KeyboardType.Number,
								imeAction = ImeAction.Next
							)
						)
						Text(text = pph_4.ifBlank { "0" }, fontSize = 14.sp, color = Colors().textBlack, modifier = Modifier.padding(vertical = 16.dp).padding(start = 8.dp).weight(0.4f))
					}
					
					//Mei
					Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
						Text(text = "Mei", fontSize = 14.sp, color = Colors().textBlack, modifier = Modifier.padding(vertical = 16.dp).padding(start = 8.dp).weight(0.2f))
						TextField(
							enabled = true,
							modifier = Modifier.fillMaxWidth().weight(0.4f),
							value = bruto_5,
							onValueChange = {
								bruto_5 = it
								pph_5 = if(bruto_5.isBlank()) "0" else (bruto_5.toLong() * 5 / 1000).toString()
							},
							singleLine = true,
							placeholder = {
								Text(
									text = "0",
									color = Colors().textDarkGrey
								)
							},
							colors = textFieldColors(
								backgroundColor = Colors().slate20,
								focusedIndicatorColor = Color.Transparent,
								unfocusedIndicatorColor = Color.Transparent,
								disabledIndicatorColor = Colors().slate20
							),
							keyboardOptions = KeyboardOptions(
								keyboardType = KeyboardType.Number,
								imeAction = ImeAction.Next
							)
						)
						Text(text = pph_5.ifBlank { "0" }, fontSize = 14.sp, color = Colors().textBlack, modifier = Modifier.padding(vertical = 16.dp).padding(start = 8.dp).weight(0.4f))
					}
					
					//Jun
					Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
						Text(text = "Jun", fontSize = 14.sp, color = Colors().textBlack, modifier = Modifier.padding(vertical = 16.dp).padding(start = 8.dp).weight(0.2f))
						TextField(
							enabled = true,
							modifier = Modifier.fillMaxWidth().weight(0.4f),
							value = bruto_6,
							onValueChange = {
								bruto_6 = it
								pph_6 = if(bruto_6.isBlank()) "0" else (bruto_6.toLong() * 5 / 1000).toString()
							},
							singleLine = true,
							placeholder = {
								Text(
									text = "0",
									color = Colors().textDarkGrey
								)
							},
							colors = textFieldColors(
								backgroundColor = Colors().slate20,
								focusedIndicatorColor = Color.Transparent,
								unfocusedIndicatorColor = Color.Transparent,
								disabledIndicatorColor = Colors().slate20
							),
							keyboardOptions = KeyboardOptions(
								keyboardType = KeyboardType.Number,
								imeAction = ImeAction.Next
							)
						)
						Text(text = pph_6.ifBlank { "0" }, fontSize = 14.sp, color = Colors().textBlack, modifier = Modifier.padding(vertical = 16.dp).padding(start = 8.dp).weight(0.4f))
					}
					
					//Jul
					Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
						Text(text = "Jul", fontSize = 14.sp, color = Colors().textBlack, modifier = Modifier.padding(vertical = 16.dp).padding(start = 8.dp).weight(0.2f))
						TextField(
							enabled = true,
							modifier = Modifier.fillMaxWidth().weight(0.4f),
							value = bruto_7,
							onValueChange = {
								bruto_7 = it
								pph_7 = if(bruto_7.isBlank()) "0" else (bruto_7.toLong() * 5 / 1000).toString()
							},
							singleLine = true,
							placeholder = {
								Text(
									text = "0",
									color = Colors().textDarkGrey
								)
							},
							colors = textFieldColors(
								backgroundColor = Colors().slate20,
								focusedIndicatorColor = Color.Transparent,
								unfocusedIndicatorColor = Color.Transparent,
								disabledIndicatorColor = Colors().slate20
							),
							keyboardOptions = KeyboardOptions(
								keyboardType = KeyboardType.Number,
								imeAction = ImeAction.Next
							)
						)
						Text(text = pph_7.ifBlank { "0" }, fontSize = 14.sp, color = Colors().textBlack, modifier = Modifier.padding(vertical = 16.dp).padding(start = 8.dp).weight(0.4f))
					}
					
					//Aug
					Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
						Text(text = "Aug", fontSize = 14.sp, color = Colors().textBlack, modifier = Modifier.padding(vertical = 16.dp).padding(start = 8.dp).weight(0.2f))
						TextField(
							enabled = true,
							modifier = Modifier.fillMaxWidth().weight(0.4f),
							value = bruto_8,
							onValueChange = {
								bruto_8 = it
								pph_8 = if(bruto_8.isBlank()) "0" else (bruto_8.toLong() * 5 / 1000).toString()
							},
							singleLine = true,
							placeholder = {
								Text(
									text = "0",
									color = Colors().textDarkGrey
								)
							},
							colors = textFieldColors(
								backgroundColor = Colors().slate20,
								focusedIndicatorColor = Color.Transparent,
								unfocusedIndicatorColor = Color.Transparent,
								disabledIndicatorColor = Colors().slate20
							),
							keyboardOptions = KeyboardOptions(
								keyboardType = KeyboardType.Number,
								imeAction = ImeAction.Next
							)
						)
						Text(text = pph_8.ifBlank { "0" }, fontSize = 14.sp, color = Colors().textBlack, modifier = Modifier.padding(vertical = 16.dp).padding(start = 8.dp).weight(0.4f))
					}
					
					//Sep
					Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
						Text(text = "Sep", fontSize = 14.sp, color = Colors().textBlack, modifier = Modifier.padding(vertical = 16.dp).padding(start = 8.dp).weight(0.2f))
						TextField(
							enabled = true,
							modifier = Modifier.fillMaxWidth().weight(0.4f),
							value = bruto_9,
							onValueChange = {
								bruto_9 = it
								pph_9 = if(bruto_9.isBlank()) "0" else (bruto_9.toLong() * 5 / 1000).toString()
							},
							singleLine = true,
							placeholder = {
								Text(
									text = "0",
									color = Colors().textDarkGrey
								)
							},
							colors = textFieldColors(
								backgroundColor = Colors().slate20,
								focusedIndicatorColor = Color.Transparent,
								unfocusedIndicatorColor = Color.Transparent,
								disabledIndicatorColor = Colors().slate20
							),
							keyboardOptions = KeyboardOptions(
								keyboardType = KeyboardType.Number,
								imeAction = ImeAction.Next
							)
						)
						Text(text = pph_9.ifBlank { "0" }, fontSize = 14.sp, color = Colors().textBlack, modifier = Modifier.padding(vertical = 16.dp).padding(start = 8.dp).weight(0.4f))
					}
					
					//Okt
					Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
						Text(text = "Okt", fontSize = 14.sp, color = Colors().textBlack, modifier = Modifier.padding(vertical = 16.dp).padding(start = 8.dp).weight(0.2f))
						TextField(
							enabled = true,
							modifier = Modifier.fillMaxWidth().weight(0.4f),
							value = bruto_10,
							onValueChange = {
								bruto_10 = it
								pph_10 = if(bruto_10.isBlank()) "0" else (bruto_10.toLong() * 5 / 1000).toString()
							},
							singleLine = true,
							placeholder = {
								Text(
									text = "0",
									color = Colors().textDarkGrey
								)
							},
							colors = textFieldColors(
								backgroundColor = Colors().slate20,
								focusedIndicatorColor = Color.Transparent,
								unfocusedIndicatorColor = Color.Transparent,
								disabledIndicatorColor = Colors().slate20
							),
							keyboardOptions = KeyboardOptions(
								keyboardType = KeyboardType.Number,
								imeAction = ImeAction.Next
							)
						)
						Text(text = pph_10.ifBlank { "0" }, fontSize = 14.sp, color = Colors().textBlack, modifier = Modifier.padding(vertical = 16.dp).padding(start = 8.dp).weight(0.4f))
					}
					
					//Nov
					Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
						Text(text = "Nov", fontSize = 14.sp, color = Colors().textBlack, modifier = Modifier.padding(vertical = 16.dp).padding(start = 8.dp).weight(0.2f))
						TextField(
							enabled = true,
							modifier = Modifier.fillMaxWidth().weight(0.4f),
							value = bruto_11,
							onValueChange = {
								bruto_11 = it
								pph_11 = if(bruto_11.isBlank()) "0" else (bruto_11.toLong() * 5 / 1000).toString()
							},
							singleLine = true,
							placeholder = {
								Text(
									text = "0",
									color = Colors().textDarkGrey
								)
							},
							colors = textFieldColors(
								backgroundColor = Colors().slate20,
								focusedIndicatorColor = Color.Transparent,
								unfocusedIndicatorColor = Color.Transparent,
								disabledIndicatorColor = Colors().slate20
							),
							keyboardOptions = KeyboardOptions(
								keyboardType = KeyboardType.Number,
								imeAction = ImeAction.Next
							)
						)
						Text(text = pph_11.ifBlank { "0" }, fontSize = 14.sp, color = Colors().textBlack, modifier = Modifier.padding(vertical = 16.dp).padding(start = 8.dp).weight(0.4f))
					}
					
					//Dec
					Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
						Text(text = "Dec", fontSize = 14.sp, color = Colors().textBlack, modifier = Modifier.padding(vertical = 16.dp).padding(start = 8.dp).weight(0.2f))
						TextField(
							enabled = true,
							modifier = Modifier.fillMaxWidth().weight(0.4f),
							value = bruto_12,
							onValueChange = {
								bruto_12 = it
								pph_12 = if(bruto_12.isBlank()) "0" else (bruto_12.toLong() * 5 / 1000).toString()
							},
							singleLine = true,
							placeholder = {
								Text(
									text = "0",
									color = Colors().textDarkGrey
								)
							},
							colors = textFieldColors(
								backgroundColor = Colors().slate20,
								focusedIndicatorColor = Color.Transparent,
								unfocusedIndicatorColor = Color.Transparent,
								disabledIndicatorColor = Colors().slate20
							),
							keyboardOptions = KeyboardOptions(
								keyboardType = KeyboardType.Number,
								imeAction = ImeAction.Next
							)
						)
						Text(text = pph_12.ifBlank { "0" }, fontSize = 14.sp, color = Colors().textBlack, modifier = Modifier.padding(vertical = 16.dp).padding(start = 8.dp).weight(0.4f))
					}
					
					//Total
					Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
						Text(text = "Total", fontSize = 14.sp, color = Colors().textBlack, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 8.dp, bottom = 16.dp, top = 24.dp).weight(0.2f))
						Text(text = "Rp ${CurrencyFormatter(getBrutoSum())}", fontSize = 14.sp, color = Colors().textBlack, modifier = Modifier.padding(bottom = 16.dp, top = 24.dp).weight(0.4f))
						Text(text = "Rp ${CurrencyFormatter(getPphSum())}", fontSize = 14.sp, color = Colors().textBlack, modifier = Modifier.padding(bottom = 16.dp, top = 24.dp).weight(0.4f))
					}
				}
			}
		}
		
		fun checkValidity(): Boolean {
			 when(formType) {
				 "A" -> {
					 return incomeTypeE.isNotBlank() && incomeIDR.isNotBlank() && taxPayableIDR.isNotBlank()
				 }
				 "B" -> {
					 return incomeTypeE.isNotBlank() && incomeIDR.isNotBlank() && taxPayableIDR.isNotBlank()
				 }
				 "C" -> {
					 return sellPriceIDR.isNotBlank() && incomeIDR.isNotBlank() && taxPayableIDR.isNotBlank()
				 }
				 "D" -> {
					 return cityId != 0 && kluId != 0 && businessNPWP.isNotBlank() && kpp.isNotBlank() && businessAddress.isNotBlank()
				 }
				 "E" -> {
					 return incomeIDR.isNotBlank() && taxPayableIDR.isNotBlank()
				 }
				 else -> return false
			 }
		}
		
		loadingPopupBox(!isReady)
		
		//Form
		Column(
			modifier = Modifier.fillMaxSize().background(Colors().panel)
		) {
			LazyColumn(
				modifier = Modifier.fillMaxSize()
			) {
				//Top Bar
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
						
						Row(
							modifier = Modifier.fillMaxWidth(),
							horizontalArrangement = Arrangement.SpaceBetween
						) {
							Text(
								text = if (id == 0) "Tambah Penghasilan Final" else "Edit Penghasilan Final",
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
				
				//Income Type
				item { incomeTypeSelector() }
				
				if(formType == "D") {
					//KLU
					item { kluSelector() }
					
					//City
					item { citySelector() }
					
					//Business NPWP
					item { businessNPWPTextField() }
					
					//Business Address
					item { businessAddressTextField() }
					
					//KPP
					item { kppTextField() }
					
					//Bruto
					item { brutoTable() }
					
				} else {
					//Wealth Selector
					if (formType == "A" || formType == "C" || formType == "E") {
						item { wealthSelector() }
					}
					
					//Sell Price
					if (formType == "C") {
						item { sellPriceTextField() }
					}
					
					//Income
					item { incomeTextField() }
					
					//Nett Income
					if (incomeTypeE == "30") {
						item { netIncomeTextField() }
					}
					
					//Tax Payable
					item { pphTextField() }
					
					//Rent Start & End Date
					if (formType == "E") {
						item { rentStartDatePicker() }
						item { rentEndDatePicker() }
					}
					
					//Description
					item { descriptionTextField() }
				}
				
				//Submit Button
				item {
					Button(
						enabled = checkValidity(),
						modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 22.dp),
						colors = buttonColors(backgroundColor = Colors().buttonActive, contentColor = Color.White),
						onClick = {
							when(formType) {
								"A" -> {
									incomeIDR = incomeIDR.replace("Rp ", "")
									incomeIDR = incomeIDR.replace(",", "")
									taxPayableIDR = taxPayableIDR.replace("Rp ", "")
									taxPayableIDR = taxPayableIDR.replace(",", "")
									
									val dataModel = FormFinalIncomeARequestApiModel(
										Id = id,
										Tr1770HdId = sptHd!!.Id,
										IncomeTypeE = incomeTypeE.toInt(),
										Tr1770WealthId = wealthId.ifBlank { null }?.toInt(),
										IncomeIDR = incomeIDR.toLong(),
										TaxPayableIDR = taxPayableIDR.toLong(),
										RealizedIDR = netIncomeIDR.ifBlank { null }?.toLong(),
										Description = description.ifBlank { null }
									)
									
									scope.launch {
										isReady = false
										sptManager.saveFinalIncomeA(scope, dataModel)
										navigator.pop()
									}
								}
								"B" -> {
									incomeIDR = incomeIDR.replace("Rp ", "")
									incomeIDR = incomeIDR.replace(",", "")
									taxPayableIDR = taxPayableIDR.replace("Rp ", "")
									taxPayableIDR = taxPayableIDR.replace(",", "")
									
									val dataModel = FormFinalIncomeBRequestApiModel(
										Id = id,
										Tr1770HdId = sptHd!!.Id,
										IncomeTypeE = incomeTypeE.toInt(),
										IncomeIDR = incomeIDR.toLong(),
										TaxPayableIDR = taxPayableIDR.toLong(),
										Description = description.ifBlank { null }
									)
									
									scope.launch {
										isReady = false
										sptManager.saveFinalIncomeB(scope, dataModel)
										navigator.pop()
									}
								}
								"C" -> {
									incomeIDR = incomeIDR.replace("Rp ", "")
									incomeIDR = incomeIDR.replace(",", "")
									taxPayableIDR = taxPayableIDR.replace("Rp ", "")
									taxPayableIDR = taxPayableIDR.replace(",", "")
									sellPriceIDR = sellPriceIDR.replace("Rp ", "")
									sellPriceIDR = sellPriceIDR.replace(",", "")
									
									val dataModel = FormFinalIncomeCRequestApiModel(
										Id = id,
										Tr1770HdId = sptHd!!.Id,
										Tr1770WealthId = wealthId.ifBlank { null }?.toInt(),
										SellPriceIDR = sellPriceIDR.toLong(),
										IncomeIDR = incomeIDR.toLong(),
										TaxPayableIDR = taxPayableIDR.toLong(),
										Description = description.ifBlank { null }
									)
									
									scope.launch {
										isReady = false
										sptManager.saveFinalIncomeC(scope, dataModel)
										navigator.pop()
									}
								}
								"D" -> {
									incomeIDR = incomeIDR.replace("Rp ", "")
									incomeIDR = incomeIDR.replace(",", "")
									taxPayableIDR = taxPayableIDR.replace("Rp ", "")
									taxPayableIDR = taxPayableIDR.replace(",", "")
									sellPriceIDR = sellPriceIDR.replace("Rp ", "")
									sellPriceIDR = sellPriceIDR.replace(",", "")
									
									val brutoList: List<BrutoCirculationRequestApiModel> = listOf(
										BrutoCirculationRequestApiModel(
											Id = null,
											Tr1770IncomeId = null,
											Period = "${sptHd!!.TaxYear}-01-01",
											IncomeIDR = bruto_1.ifBlank { "0" }.toLong(),
											TaxPayableIDR = null
										),
										BrutoCirculationRequestApiModel(
											Id = null,
											Tr1770IncomeId = null,
											Period = "${sptHd.TaxYear}-02-01",
											IncomeIDR = bruto_2.ifBlank { "0" }.toLong(),
											TaxPayableIDR = null
										),
										BrutoCirculationRequestApiModel(
											Id = null,
											Tr1770IncomeId = null,
											Period = "${sptHd.TaxYear}-03-01",
											IncomeIDR = bruto_3.ifBlank { "0" }.toLong(),
											TaxPayableIDR = null
										),
										BrutoCirculationRequestApiModel(
											Id = null,
											Tr1770IncomeId = null,
											Period = "${sptHd.TaxYear}-04-01",
											IncomeIDR = bruto_4.ifBlank { "0" }.toLong(),
											TaxPayableIDR = null
										),
										BrutoCirculationRequestApiModel(
											Id = null,
											Tr1770IncomeId = null,
											Period = "${sptHd.TaxYear}-05-01",
											IncomeIDR = bruto_5.ifBlank { "0" }.toLong(),
											TaxPayableIDR = null
										),
										BrutoCirculationRequestApiModel(
											Id = null,
											Tr1770IncomeId = null,
											Period = "${sptHd.TaxYear}-06-01",
											IncomeIDR = bruto_6.ifBlank { "0" }.toLong(),
											TaxPayableIDR = null
										),
										BrutoCirculationRequestApiModel(
											Id = null,
											Tr1770IncomeId = null,
											Period = "${sptHd.TaxYear}-07-01",
											IncomeIDR = bruto_7.ifBlank { "0" }.toLong(),
											TaxPayableIDR = null
										),
										BrutoCirculationRequestApiModel(
											Id = null,
											Tr1770IncomeId = null,
											Period = "${sptHd.TaxYear}-08-01",
											IncomeIDR = bruto_8.ifBlank { "0" }.toLong(),
											TaxPayableIDR = null
										),
										BrutoCirculationRequestApiModel(
											Id = null,
											Tr1770IncomeId = null,
											Period = "${sptHd.TaxYear}-09-01",
											IncomeIDR = bruto_9.ifBlank { "0" }.toLong(),
											TaxPayableIDR = null
										),
										BrutoCirculationRequestApiModel(
											Id = null,
											Tr1770IncomeId = null,
											Period = "${sptHd.TaxYear}-10-01",
											IncomeIDR = bruto_10.ifBlank { "0" }.toLong(),
											TaxPayableIDR = null
										),
										BrutoCirculationRequestApiModel(
											Id = null,
											Tr1770IncomeId = null,
											Period = "${sptHd.TaxYear}-11-01",
											IncomeIDR = bruto_11.ifBlank { "0" }.toLong(),
											TaxPayableIDR = null
										),
										BrutoCirculationRequestApiModel(
											Id = null,
											Tr1770IncomeId = null,
											Period = "${sptHd.TaxYear}-12-01",
											IncomeIDR = bruto_12.ifBlank { "0" }.toLong(),
											TaxPayableIDR = null
										)
									)
									
									val dataModel = FormFinalIncomeDRequestApiModel(
										Id = id,
										Tr1770HdId = sptHd.Id,
										CityId = cityId,
										KLUId = kluId,
										NPWP = businessNPWP,
										KPPLocation = kpp,
										UseUUHPP = false,
										BusinessAddress = businessAddress,
										BrutoCirculations = brutoList
									)
									
									scope.launch {
										isReady = false
										sptManager.saveFinalIncomeD(scope, dataModel)
										navigator.pop()
									}
								}
								"E" -> {
									incomeIDR = incomeIDR.replace("Rp ", "")
									incomeIDR = incomeIDR.replace(",", "")
									taxPayableIDR = taxPayableIDR.replace("Rp ", "")
									taxPayableIDR = taxPayableIDR.replace(",", "")
									
									val dataModel = FormFinalIncomeERequestApiModel(
										Id = id,
										Tr1770HdId = sptHd!!.Id,
										Tr1770WealthId = wealthId.ifBlank { null }?.toInt(),
										IncomeIDR = incomeIDR.toLong(),
										TaxPayableIDR = taxPayableIDR.toLong(),
										RentStartDate = rentStartDate.ifBlank { null },
										RentEndDate = rentEndDate.ifBlank { null },
										Description = description.ifBlank { null }
									)
									
									scope.launch {
										isReady = false
										sptManager.saveFinalIncomeE(scope, dataModel)
										navigator.pop()
									}
								}
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
				
				//Spacer
				item {
					Box(
						modifier = Modifier.height(88.dp)
					)
				}
			}
		}
		
		//Popups
		//Income Type Popup
		popUpBox(
			showPopup = showIncomeTypePopup,
			onClickOutside = { showIncomeTypePopup = false },
			content = {
				Column(
					modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 12.dp)
				) {
					Text(
						text = "Jenis Penghasilan Final",
						fontSize = 12.sp,
						color = Colors().textBlack,
						fontWeight = FontWeight.Bold,
						modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
					)
					LazyColumn(modifier = Modifier.heightIn(max = 200.dp).padding(horizontal = 18.dp)) {
						FinalIncomeType.entries.forEach {
							item {
								Text(
									text = FinalIncomeType.fromValue(it.value).toString(),
									fontSize = 18.sp,
									modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
										.clickable(true, onClick = {
											selectedIncomeType = FinalIncomeType.fromValue(it.value).toString()
											incomeTypeE = it.value.toString()
											formType = FinalIncomeType.formTypeFromValue(it.value).toString()
											if( formType == "A" || formType == "C" || formType == "E" ) selectedAssetList = getFilteredAsset()
											if(sptHd?.TaxYear?.toInt()!! >= 2023 && incomeTypeE == "160") navigator.push(FinalIncomeUMKMScreen(sptHd, client, sptPertamaClient, prefs))
											showIncomeTypePopup = false
										})
								)
							}
						}
					}
				}
			}
		)
		
		//Wealth List Popup
		popUpBox(
			showPopup = showAssetListPopup,
			onClickOutside = { showAssetListPopup = false },
			content = {
				Column(
					modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 12.dp)
				) {
					Text(
						text = "Nama Aset",
						fontSize = 12.sp,
						color = Colors().textBlack,
						fontWeight = FontWeight.Bold,
						modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
					)
					LazyColumn(modifier = Modifier.heightIn(max = 200.dp).padding(horizontal = 18.dp)) {
						selectedAssetList.forEach {
							item {
								Text(
									text = "(${it.WealthType.WealthTypeName}) ${it.AcquisitionYear} Rp ${CurrencyFormatter(BigDeciToString(it.CurrencyAmountIDR.toString()))}",
									fontSize = 18.sp,
									modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
										.clickable(true, onClick = {
											selectedWealth = "(${it.WealthType.WealthTypeName}) ${it.AcquisitionYear} Rp ${CurrencyFormatter(BigDeciToString(it.CurrencyAmountIDR.toString()))}"
											wealthId = it.Id.toString()
											showAssetListPopup = false
										})
								)
							}
						}
					}
				}
			}
		)
		
		//Rent Start Date Picker Popup
		popUpBox(
			showPopup = showRentStartDatePopup,
			onClickOutside = { showRentStartDatePopup = false },
			content = {
				Column {
					Box(modifier = Modifier.height(16.dp))
					WheelDatePicker(
						height = 256.dp,
						title = "Pilih Tanggal Mulai Sewa",
						doneLabel = "Selesai",
						yearsRange = IntRange(1920, (sptHd?.TaxYear?.toInt() ?: LocalDate.now().year) - 1),
						titleStyle = TextStyle(fontSize = 16.sp, color = Colors().textBlack, fontWeight = FontWeight.Bold),
						doneLabelStyle = TextStyle(fontSize = 14.sp, color = Colors().textClickable, fontWeight = FontWeight.Bold),
						showShortMonths = true,
						startDate = LocalDate.now().minus(DatePeriod(1,0,0)),
						onDoneClick = {
							rentStartDate = it.toString()
							showRentStartDatePopup = false
						}
					)
				}
			}
		)
		
		//Rent End Date Picker Popup
		popUpBox(
			showPopup = showRentEndDatePopup,
			onClickOutside = { showRentEndDatePopup = false },
			content = {
				Column {
					Box(modifier = Modifier.height(16.dp))
					WheelDatePicker(
						height = 256.dp,
						title = "Pilih Tanggal Akhir Sewa",
						doneLabel = "Selesai",
//						yearsRange = IntRange(1920, (sptHd?.TaxYear?.toInt() ?: LocalDate.now().year) - 1),
						titleStyle = TextStyle(fontSize = 16.sp, color = Colors().textBlack, fontWeight = FontWeight.Bold),
						doneLabelStyle = TextStyle(fontSize = 14.sp, color = Colors().textClickable, fontWeight = FontWeight.Bold),
						showShortMonths = true,
						startDate = LocalDate.now().minus(DatePeriod(1,0,0)),
						onDoneClick = {
							rentEndDate = it.toString()
							showRentEndDatePopup = false
						}
					)
				}
			}
		)
		
		//KLU Popup
		popUpBox(
			showPopup = showKLUPopup,
			onClickOutside = { showKLUPopup = false },
			content = {
				Column(
					modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 12.dp)
				) {
					Text(
						text = "Klasifikasi Lapangan Usaha",
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
						value = kluSearchText,
						onValueChange = {
							kluSearchText = it
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
							if(kluSearchText != ""){
								Image(
									painterResource(Res.drawable.icon_clear),
									null,
									modifier = Modifier.size(36.dp).padding(end = 16.dp)
										.clickable(true, onClick = {
											kluSearchText = ""
										})
								)
							}
						}
					)
					LazyColumn(modifier = Modifier.heightIn(max = 200.dp).padding(horizontal = 18.dp)) {
						kluList.forEach {
							if (it.KLUName.contains(kluSearchText, ignoreCase = true)) {
								item {
									Text(
										text = "(${it.KLUCode}) ${it.KLUName}",
										fontSize = 18.sp,
										modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
											.clickable(true, onClick = {
												selectedKlu = "(${it.KLUCode}) ${it.KLUName}"
												kluId = it.Id
												showKLUPopup = false
											})
									)
								}
							}
						}
					}
				}
			}
		)
		
		//City Popup
		popUpBox(
			showPopup = showCityPopup,
			onClickOutside = { showCityPopup = false },
			content = {
				Column(
					modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 12.dp)
				) {
					Text(
						text = "Kota Tempat Usaha",
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
						value = citySearchText,
						onValueChange = {
							citySearchText = it
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
							if(citySearchText != ""){
								Image(
									painterResource(Res.drawable.icon_clear),
									null,
									modifier = Modifier.size(36.dp).padding(end = 16.dp)
										.clickable(true, onClick = {
											citySearchText = ""
										})
								)
							}
						}
					)
					LazyColumn(modifier = Modifier.heightIn(max = 200.dp).padding(horizontal = 18.dp)) {
						cityList.forEach {
							if (it.CityName.contains(citySearchText, ignoreCase = true)) {
								item {
									Text(
										text = it.CityName,
										fontSize = 18.sp,
										modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
											.clickable(true, onClick = {
												selectedCity = it.CityName
												cityId = it.Id
												showCityPopup = false
											})
									)
								}
							}
						}
					}
				}
			}
		)
		
		//Delete Final Income Popup
		popUpBox(
			showPopup = showDeletePopup,
			onClickOutside = { showDeletePopup = false },
			content = {
				Text(
					text = "Hapus Penghasilan Final",
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
						text = "Hapus Penghasilan Final",
						fontSize = 18.sp,
						color = Colors().textBlack,
						fontWeight = FontWeight.Bold
					)
					
					Text(
						modifier = Modifier.padding(vertical = 24.dp, horizontal = 16.dp),
						text = "Apakah Anda yakin ingin menghapus Penghasilan Final ini?",
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
									sptManager.deleteIncome(scope, id.toString())
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
	}
}