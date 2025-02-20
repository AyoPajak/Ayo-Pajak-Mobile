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
import ayopajakmobile.composeapp.generated.resources.icon_tripledot_black
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import global.AssetCode
import global.Colors
import global.DomesticNetIncomeType
import global.FinalIncomeType
import global.OverseasNetIncomeType
import global.universalUIComponents.loadingPopupBox
import global.universalUIComponents.popUpBox
import http.Account
import http.Interfaces
import kotlinx.coroutines.launch
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import models.transaction.Form1770FinalIncomeUmkm2023BusinessRequestModel
import models.transaction.Form1770HdResponseApiModel
import models.transaction.FormNetOtherIncomeARequestApiModel
import models.transaction.FormNetOtherIncomeBRequestApiModel
import models.transaction.FormNetOtherIncomeCRequestApiModel
import models.transaction.FormNetOtherIncomeDRequestApiModel
import models.transaction.FormNetOtherIncomeERequestApiModel
import models.transaction.FormNetOtherIncomeFRequestApiModel
import models.transaction.FormWealthResponseApiModel
import network.chaintech.kmp_date_time_picker.ui.datepicker.WheelDatePickerComponent.WheelDatePicker
import network.chaintech.kmp_date_time_picker.utils.now
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource
import util.BigDeciToLong
import util.BigDeciToString
import util.CurrencyFormatter
import util.DateFormatter

class IncomeNetOtherFormScreen(val id: Int, val sptHd: Form1770HdResponseApiModel?, val client: Account, val sptPertamaClient: Interfaces, val prefs: DataStore<Preferences>): Screen {
	
	val sptManager = SPTManager(prefs, client, sptPertamaClient)
	
	@Composable
	override fun Content() {
		
		val scope = rememberCoroutineScope()
		val navigator = LocalNavigator.currentOrThrow
		
		var showDeletePopup by remember { mutableStateOf(false) }
		var showConfirmPopup by remember { mutableStateOf(false) }
		var showIncomeTypePopup by remember { mutableStateOf(false) }
		var showRentStartDatePopup by remember { mutableStateOf(false) }
		var showRentEndDatePopup by remember { mutableStateOf(false) }
		var showAssetListPopup by remember { mutableStateOf(false) }
		
		var formType by remember { mutableStateOf("") }
		
		var incomeTypeE by remember { mutableStateOf(0) }
		var selectedIncomeType by remember { mutableStateOf("") }
		var nettIncomeIDR by remember { mutableStateOf("") }
		var nettIncomeIDRActual by remember { mutableStateOf(0L) }
		var description by remember { mutableStateOf("") }
		var wealthId by remember { mutableStateOf(0) }
		var selectedWealth by remember { mutableStateOf("") }
		var sellPriceIDR by remember { mutableStateOf("") }
		var sellPriceIDRActual by remember { mutableStateOf(0L) }
		var rentStartDate by remember { mutableStateOf("") }
		var rentEndDate by remember { mutableStateOf("") }
		var employerNameAddr by remember { mutableStateOf("") }
		var overseasIncomeType by remember { mutableStateOf("") }
		
		var wealthList by remember { mutableStateOf<List<FormWealthResponseApiModel>>(emptyList())}
		var filteredWealthList by remember { mutableStateOf<List<FormWealthResponseApiModel>>(emptyList())}
		
		var isReady by remember { mutableStateOf(false) }
		
		fun getFilteredWealthList(): List<FormWealthResponseApiModel> {
			when(formType) {
				"B" -> {
					return wealthList.filter { it.IsOverseas == false && (it.WealthType.WealthTypeCode == AssetCode.UANG_TUNAI.value ||
							it.WealthType.WealthTypeCode == AssetCode.SAHAM.value ||
							it.WealthType.WealthTypeCode == AssetCode.INVESTASI_LAINNYA.value ||
							it.WealthType.WealthTypeCode == AssetCode.PENYERTAAN_MODAL_DALAM_PERUSAHAAN_LAIN_YANG_TIDAK_ATAS_SAHAM.value ||
							it.WealthType.WealthTypeCode == AssetCode.REKSADANA.value ||
							it.WealthType.WealthTypeCode == AssetCode.OBLIGASI_PERUSAHAAN.value ||
							it.WealthType.WealthTypeCode == AssetCode.OBLIGASI_PEMERINTAH_INDONESIA.value ||
							it.WealthType.WealthTypeCode == AssetCode.SEPEDA.value ||
							it.WealthType.WealthTypeCode == AssetCode.SEPEDA_MOTOR.value ||
							it.WealthType.WealthTypeCode == AssetCode.MOBIL.value ||
							it.WealthType.WealthTypeCode == AssetCode.ALAT_TRANSPORTASI_LAINNYA.value ||
							it.WealthType.WealthTypeCode == AssetCode.LOGAM_MULIA.value ||
							it.WealthType.WealthTypeCode == AssetCode.BATU_MULIA.value ||
							it.WealthType.WealthTypeCode == AssetCode.BARANG_SENI_DAN_ANTIK.value ||
							it.WealthType.WealthTypeCode == AssetCode.KAPAL_PESIAR_PESAWAT_TERBANG_HELIKOPTER_JETSKI_PERALATAN_OLAH_RAGA_KHUSUS.value ||
							it.WealthType.WealthTypeCode == AssetCode.PERALATAN_ELEKTRONIK_FURINITURE.value ||
							it.WealthType.WealthTypeCode == AssetCode.HARTA_BERGERAK_LAINNYA.value ||
							it.WealthType.WealthTypeCode == AssetCode.HARTA_TIDAK_GERAK_LAINNYA.value) }
				}
				
				"C" -> {
					return wealthList.filter { it.IsOverseas == false && (it.WealthType.WealthTypeCode == AssetCode.SEPEDA.value ||
							it.WealthType.WealthTypeCode == AssetCode.SEPEDA_MOTOR.value ||
							it.WealthType.WealthTypeCode == AssetCode.MOBIL.value ||
							it.WealthType.WealthTypeCode == AssetCode.ALAT_TRANSPORTASI_LAINNYA.value ||
							it.WealthType.WealthTypeCode == AssetCode.KAPAL_PESIAR_PESAWAT_TERBANG_HELIKOPTER_JETSKI_PERALATAN_OLAH_RAGA_KHUSUS.value ||
							it.WealthType.WealthTypeCode == AssetCode.PERALATAN_ELEKTRONIK_FURINITURE.value ||
							it.WealthType.WealthTypeCode == AssetCode.HARTA_BERGERAK_LAINNYA.value ||
							it.WealthType.WealthTypeCode == AssetCode.HARTA_TIDAK_GERAK_LAINNYA.value) }
				}
				
				"D" -> {
					return wealthList.filter { it.IsOverseas == true && (it.WealthType.WealthTypeCode == AssetCode.UANG_TUNAI.value ||
							it.WealthType.WealthTypeCode == AssetCode.SAHAM.value ||
							it.WealthType.WealthTypeCode == AssetCode.INVESTASI_LAINNYA.value ||
							it.WealthType.WealthTypeCode == AssetCode.PENYERTAAN_MODAL_DALAM_PERUSAHAAN_LAIN_YANG_TIDAK_ATAS_SAHAM.value ||
							it.WealthType.WealthTypeCode == AssetCode.REKSADANA.value ||
							it.WealthType.WealthTypeCode == AssetCode.OBLIGASI_PERUSAHAAN.value ||
							it.WealthType.WealthTypeCode == AssetCode.OBLIGASI_PEMERINTAH_INDONESIA.value ||
							it.WealthType.WealthTypeCode == AssetCode.SEPEDA.value ||
							it.WealthType.WealthTypeCode == AssetCode.SEPEDA_MOTOR.value ||
							it.WealthType.WealthTypeCode == AssetCode.MOBIL.value ||
							it.WealthType.WealthTypeCode == AssetCode.ALAT_TRANSPORTASI_LAINNYA.value ||
							it.WealthType.WealthTypeCode == AssetCode.LOGAM_MULIA.value ||
							it.WealthType.WealthTypeCode == AssetCode.BATU_MULIA.value ||
							it.WealthType.WealthTypeCode == AssetCode.BARANG_SENI_DAN_ANTIK.value ||
							it.WealthType.WealthTypeCode == AssetCode.KAPAL_PESIAR_PESAWAT_TERBANG_HELIKOPTER_JETSKI_PERALATAN_OLAH_RAGA_KHUSUS.value ||
							it.WealthType.WealthTypeCode == AssetCode.PERALATAN_ELEKTRONIK_FURINITURE.value ||
							it.WealthType.WealthTypeCode == AssetCode.HARTA_BERGERAK_LAINNYA.value ||
							it.WealthType.WealthTypeCode == AssetCode.HARTA_TIDAK_GERAK_LAINNYA.value) }
				}
				"E" -> {
					return wealthList.filter { it.IsOverseas == true }
				}
				"F" -> {
					return wealthList.filter { it.IsOverseas == true && (it.WealthType.WealthTypeCode == AssetCode.SEPEDA.value ||
							it.WealthType.WealthTypeCode == AssetCode.SEPEDA_MOTOR.value ||
							it.WealthType.WealthTypeCode == AssetCode.MOBIL.value ||
							it.WealthType.WealthTypeCode == AssetCode.ALAT_TRANSPORTASI_LAINNYA.value ||
							it.WealthType.WealthTypeCode == AssetCode.KAPAL_PESIAR_PESAWAT_TERBANG_HELIKOPTER_JETSKI_PERALATAN_OLAH_RAGA_KHUSUS.value ||
							it.WealthType.WealthTypeCode == AssetCode.PERALATAN_ELEKTRONIK_FURINITURE.value ||
							it.WealthType.WealthTypeCode == AssetCode.HARTA_BERGERAK_LAINNYA.value ||
							it.WealthType.WealthTypeCode == AssetCode.HARTA_TIDAK_GERAK_LAINNYA.value) }
				}
				else -> return emptyList()
			}
		}
		
		LaunchedEffect(null) {
			wealthList = sptManager.getWealthData(scope, sptHd!!.Id.toString())
			
			if(id != 0) {
				val oldData = sptManager.getIncomeNetOtherDataById(scope, id.toString())
				
				if(oldData != null) {
					incomeTypeE = oldData.IncomeTypeE
					selectedIncomeType = if (incomeTypeE < 200) DomesticNetIncomeType.fromValue(incomeTypeE) else OverseasNetIncomeType.fromValue(incomeTypeE)
					formType = if (incomeTypeE < 200) DomesticNetIncomeType.formTypeFromValue(incomeTypeE).toString() else OverseasNetIncomeType.formTypeFromValue(incomeTypeE).toString()
					filteredWealthList = getFilteredWealthList()
					nettIncomeIDR = "Rp ${CurrencyFormatter(BigDeciToString(oldData.NettIncomeIDR.toString()))}"
					nettIncomeIDRActual = BigDeciToLong(oldData.NettIncomeIDR.toString())
					description = oldData.Description ?: ""
					wealthId = oldData.Wealth?.Id ?: 0
					selectedWealth = if(wealthId != 0) "(${oldData.Wealth?.WealthType?.WealthTypeName}) ${oldData.Wealth?.AcquisitionYear} Rp ${CurrencyFormatter(BigDeciToString(oldData.Wealth?.CurrencyAmountIDR.toString()))}" else ""
					sellPriceIDR = "Rp ${CurrencyFormatter(BigDeciToString(oldData.SellPriceIDR.toString()))}"
					sellPriceIDRActual = if(oldData.SellPriceIDR == null) 0L else BigDeciToLong(oldData.SellPriceIDR.toString())
					rentStartDate = oldData.RentStartDate ?: ""
					rentEndDate = oldData.RentEndDate ?: ""
					employerNameAddr = oldData.EmployerNameAddr ?: ""
					overseasIncomeType = oldData.OverseasIncomeType ?: ""
				}
				
				println(oldData)
			}
			
			isReady = true
		}
		
		fun checkValidity(): Boolean {
			when(formType) {
				"A" -> {
					return incomeTypeE != 0 && nettIncomeIDRActual > 0
				}
				"B" -> {
					return incomeTypeE != 0 && nettIncomeIDRActual > 0 && sellPriceIDRActual > 0
				}
				"C" -> {
					return incomeTypeE != 0 && nettIncomeIDRActual > 0
				}
				"D" -> {
					return incomeTypeE != 0 && nettIncomeIDRActual > 0 && employerNameAddr.isNotBlank()
				}
				"E" -> {
					return incomeTypeE != 0 && nettIncomeIDRActual > 0 && employerNameAddr.isNotBlank()
				}
				"F" -> {
					return incomeTypeE != 0 && nettIncomeIDRActual > 0 && employerNameAddr.isNotBlank()
				}
				else -> return false
			}
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
		fun nettIncomeTextField() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Penghasilan Neto",
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
				value = nettIncomeIDR,
				onValueChange = {
					nettIncomeIDR = it
					nettIncomeIDRActual = nettIncomeIDR.replace("R", "")
						.replace("p", "").replace(" ", "").replace(",", "")
						.toLongOrNull() ?: 0
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
					if(it.length <= 500){ description = it }
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
		fun wealthSelector() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Harta",
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
		fun sellPriceTextField() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Nilai Jual",
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
					sellPriceIDRActual = sellPriceIDR.replace("R", "")
						.replace("p", "").replace(" ", "").replace(",", "")
						.toLongOrNull() ?: 0
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
		fun employerNameAddrTextField() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Nama dan Alamat Sumber / Pemberi Penghasilan",
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
				value = employerNameAddr,
				onValueChange = {
					if(it.length <= 500){ employerNameAddr = it }
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
		fun overseasIncomeTypeTextField() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Jenis Penghasilan Luar Negeri",
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
				value = overseasIncomeType,
				onValueChange = {
					if(it.length <= 200){ overseasIncomeType = it }
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
								text = if (id == 0) "Tambah Penghasilan" else "Edit Penghasilan",
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
				
				//Income Type Selector
				item { incomeTypeSelector() }
				
				if(formType == "B" || formType == "C" || formType == "D" || formType == "E" || formType == "F") item { wealthSelector() }
				
				if(formType == "D" || formType == "E" || formType == "F") item { employerNameAddrTextField() }
				
				if(formType == "E") item { overseasIncomeTypeTextField() }
				
				if(formType == "B" || formType == "D") item { sellPriceTextField() }
				
				//Nett Income Text Field
				item { nettIncomeTextField() }
				
				if(formType == "C" || formType == "F") {
					item { rentStartDatePicker() }
					item { rentEndDatePicker() }
				}
				
				//Description Text Field
				item { descriptionTextField() }
				
				//Submit Button
				item {
					Button(
						enabled = checkValidity(),
						modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 22.dp),
						colors = buttonColors(backgroundColor = Colors().buttonActive, contentColor = Color.White),
						onClick = {
							when(formType) {
								"A" -> {
									val dataModel = FormNetOtherIncomeARequestApiModel(
										Id = id,
										Tr1770HdId = sptHd!!.Id,
										DomesticNetIncomeE = incomeTypeE,
										NettIncomeIDR = nettIncomeIDRActual,
										Description = description.ifBlank { null }
									)
									
									scope.launch {
										isReady = false
										sptManager.saveIncomeNetOtherA(scope, dataModel)
										navigator.pop()
									}
								}
								"B" -> {
									val dataModel = FormNetOtherIncomeBRequestApiModel(
										Id = id,
										Tr1770HdId = sptHd!!.Id,
										Tr1770WealthId = wealthId,
										SellPriceIDR = sellPriceIDRActual,
										NettIncomeIDR = nettIncomeIDRActual,
										Description = description.ifBlank { null }
									)
									
									scope.launch {
										isReady = false
										sptManager.saveIncomeNetOtherB(scope, dataModel)
										navigator.pop()
									}
								}
								"C" -> {
									val dataModel = FormNetOtherIncomeCRequestApiModel(
										Id = id,
										Tr1770HdId = sptHd!!.Id,
										Tr1770WealthId = wealthId,
										NettIncomeIDR = nettIncomeIDRActual,
										RentStartDate = rentStartDate.ifBlank { null },
										RentEndDate = rentEndDate.ifBlank { null },
										Description = description.ifBlank { null }
									)
									
									scope.launch {
										isReady = false
										sptManager.saveIncomeNetOtherC(scope, dataModel)
										navigator.pop()
									}
								}
								"D" -> {
									val dataModel = FormNetOtherIncomeDRequestApiModel(
										Id = id,
										Tr1770HdId = sptHd!!.Id,
										Tr1770WealthId = wealthId,
										EmployerNameAddr = employerNameAddr.ifBlank { null },
										SellPriceIDR = sellPriceIDRActual,
										NettIncomeIDR = nettIncomeIDRActual,
										Description = description.ifBlank { null }
									)
									
									scope.launch {
										isReady = false
										sptManager.saveIncomeNetOtherD(scope, dataModel)
										navigator.pop()
									}
								}
								"E" -> {
									val dataModel = FormNetOtherIncomeERequestApiModel(
										Id = id,
										Tr1770HdId = sptHd!!.Id,
										Tr1770WealthId = wealthId,
										EmployerNameAddr = employerNameAddr.ifBlank { null },
										OverseasIncomeType = overseasIncomeType.ifBlank { null },
										NettIncomeIDR = nettIncomeIDRActual,
										Description = description.ifBlank { null }
									)
									
									scope.launch {
										isReady = false
										sptManager.saveIncomeNetOtherE(scope, dataModel)
										navigator.pop()
									}
								}
								"F" -> {
									val dataModel = FormNetOtherIncomeFRequestApiModel(
										Id = id,
										Tr1770HdId = sptHd!!.Id,
										Tr1770WealthId = wealthId,
										EmployerNameAddr = employerNameAddr.ifBlank { null },
										NettIncomeIDR = nettIncomeIDRActual,
										RentStartDate = rentStartDate.ifBlank { null },
										RentEndDate = rentEndDate.ifBlank { null },
										Description = description.ifBlank { null }
									)
									
									scope.launch {
										isReady = false
										sptManager.saveIncomeNetOtherF(scope, dataModel)
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
						text = "Jenis Penghasilan",
						fontSize = 12.sp,
						color = Colors().textBlack,
						fontWeight = FontWeight.Bold,
						modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
					)
					LazyColumn(modifier = Modifier.heightIn(max = 200.dp).padding(horizontal = 18.dp)) {
						item {
							Text(
								text = "Dalam Negeri",
								fontSize = 22.sp,
								fontWeight = FontWeight.Bold,
								color = Colors().brandDark40,
							)
						}
						
						DomesticNetIncomeType.entries.forEach {
							item {
								Text(
									text = DomesticNetIncomeType.fromValue(it.value),
									fontSize = 18.sp,
									modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
										.clickable(true, onClick = {
											selectedIncomeType = DomesticNetIncomeType.fromValue(it.value)
											incomeTypeE = it.value
											formType = DomesticNetIncomeType.formTypeFromValue(it.value).toString()
											showIncomeTypePopup = false
											filteredWealthList = getFilteredWealthList()
										})
								)
							}
						}
						
						item {
							Text(
								text = "Luar Negeri",
								fontSize = 22.sp,
								fontWeight = FontWeight.Bold,
								color = Colors().brandDark40,
								modifier = Modifier.padding(top = 16.dp)
							)
						}
						
						OverseasNetIncomeType.entries.forEach {
							item {
								Text(
									text = OverseasNetIncomeType.fromValue(it.value),
									fontSize = 18.sp,
									modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
										.clickable(true, onClick = {
											selectedIncomeType = OverseasNetIncomeType.fromValue(it.value)
											incomeTypeE = it.value
											formType = OverseasNetIncomeType.formTypeFromValue(it.value).toString()
											showIncomeTypePopup = false
											filteredWealthList = getFilteredWealthList()
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
						filteredWealthList.forEach {
							item {
								Text(
									text = "(${it.WealthType.WealthTypeName}) ${it.AcquisitionYear} Rp ${CurrencyFormatter(BigDeciToString(it.CurrencyAmountIDR.toString()))}",
									fontSize = 18.sp,
									modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
										.clickable(true, onClick = {
											selectedWealth = "(${it.WealthType.WealthTypeName}) ${it.AcquisitionYear} Rp ${CurrencyFormatter(BigDeciToString(it.CurrencyAmountIDR.toString()))}"
											wealthId = it.Id
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
		
		//Delete Popup
		popUpBox(
			showPopup = showDeletePopup,
			onClickOutside = { showDeletePopup = false },
			content = {
				Text(
					text = "Hapus Data",
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
						text = "Hapus Data",
						fontSize = 18.sp,
						color = Colors().textBlack,
						fontWeight = FontWeight.Bold
					)
					
					Text(
						modifier = Modifier.padding(vertical = 24.dp, horizontal = 16.dp),
						text = "Apakah Anda yakin ingin menghapus Data ini?",
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
									sptManager.deleteIncomeNetOther(scope, id.toString())
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