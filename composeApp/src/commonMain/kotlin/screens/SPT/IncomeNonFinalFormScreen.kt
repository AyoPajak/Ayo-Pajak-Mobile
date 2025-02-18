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
import androidx.compose.foundation.text.input.InputTransformation.Companion.keyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
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
import ayopajakmobile.composeapp.generated.resources.icon_clear
import ayopajakmobile.composeapp.generated.resources.icon_search
import ayopajakmobile.composeapp.generated.resources.icon_tripledot_black
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import global.CityCategory
import global.Colors
import global.FinalIncomeType
import global.MaritalStatus
import global.NonFinalIncomeType
import global.TaxStatus
import global.universalUIComponents.loadingPopupBox
import global.universalUIComponents.popUpBox
import global.universalUIComponents.topBar
import http.Account
import http.Interfaces
import kotlinx.coroutines.launch
import models.ApiODataQueryModel
import models.master.CityModel
import models.master.KluModel
import models.transaction.BrutoCirculationRequestApiModel
import models.transaction.BrutoCirculationResponseApiModel
import models.transaction.Form1770HdResponseApiModel
import models.transaction.FormBookKeepingRequestApiModel
import models.transaction.FormNonFinalIncomeRequestApiModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource
import util.BigDeciToLong
import util.BigDeciToString
import util.CurrencyFormatter

class IncomeNonFinalFormScreen(val id: Int, val sptHd: Form1770HdResponseApiModel?, val client: Account, val sptPertamaClient: Interfaces, val prefs: DataStore<Preferences>): Screen {
	
	val sptManager = SPTManager(prefs, client, sptPertamaClient)
	
	@Composable
	override fun Content() {
		val scope = rememberCoroutineScope()
		val navigator = LocalNavigator.currentOrThrow
		
		var showDeletePopup by remember { mutableStateOf(false) }
		var showConfirmPopup by remember { mutableStateOf(false) }
		var showBusinessTypePopup by remember { mutableStateOf(false) }
		var showCityPopup by remember { mutableStateOf(false) }
		var showKLUPopup by remember { mutableStateOf(false) }
		
		var citySearchText by remember { mutableStateOf("") }
		var kluSearchText by remember { mutableStateOf("") }
		
		var cityList by remember { mutableStateOf<List<CityModel>>(emptyList()) }
		var kluList by remember { mutableStateOf<List<KluModel>>(emptyList()) }
		
		var businessTypeE by remember { mutableStateOf(0) }
		var selectedBusinessType by remember { mutableStateOf("") }
		var cityId by remember { mutableStateOf(0) }
		var selectedCity by remember { mutableStateOf("") }
		var isPayingPPh25 by remember { mutableStateOf(false) }
		var brutoList by remember { mutableStateOf<List<BrutoCirculationResponseApiModel>>(emptyList()) }
		var kluId by remember { mutableStateOf(0) }
		var selectedKlu by remember { mutableStateOf("") }
		var taxNormPerc by remember { mutableStateOf("0") }
		var businessCirculationIDR by remember { mutableStateOf("") }
		var nettIncomeIDR by remember { mutableStateOf("Rp 0") }
		
		var bruto1IDR by remember { mutableStateOf("0") }
		var bruto1IDRActual by remember { mutableStateOf(0L) }
		var bruto2IDR by remember { mutableStateOf("0") }
		var bruto2IDRActual by remember { mutableStateOf(0L) }
		var bruto3IDR by remember { mutableStateOf("0") }
		var bruto3IDRActual by remember { mutableStateOf(0L) }
		var bruto4IDR by remember { mutableStateOf("0") }
		var bruto4IDRActual by remember { mutableStateOf(0L) }
		var bruto5IDR by remember { mutableStateOf("0") }
		var bruto5IDRActual by remember { mutableStateOf(0L) }
		var bruto6IDR by remember { mutableStateOf("0") }
		var bruto6IDRActual by remember { mutableStateOf(0L) }
		var bruto7IDR by remember { mutableStateOf("0") }
		var bruto7IDRActual by remember { mutableStateOf(0L) }
		var bruto8IDR by remember { mutableStateOf("0") }
		var bruto8IDRActual by remember { mutableStateOf(0L) }
		var bruto9IDR by remember { mutableStateOf("0") }
		var bruto9IDRActual by remember { mutableStateOf(0L) }
		var bruto10IDR by remember { mutableStateOf("0") }
		var bruto10IDRActual by remember { mutableStateOf(0L) }
		var bruto11IDR by remember { mutableStateOf("0") }
		var bruto11IDRActual by remember { mutableStateOf(0L) }
		var bruto12IDR by remember { mutableStateOf("0") }
		var bruto12IDRActual by remember { mutableStateOf(0L) }
		
		var businessNPWP by remember { mutableStateOf("") }
		var businessAddress by remember { mutableStateOf("") }
		var kpp by remember { mutableStateOf("") }
		
		var isReady by remember { mutableStateOf(false) }
		
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
			cityList = sptManager.getCityList(scope)
			populateKluList()
			
			if(id != 0) {
				val oldData = sptManager.getIncomeNonFinalDataById(scope, id.toString())
				
				if(oldData != null) {
					businessTypeE = oldData.BusinessTypeE
					selectedBusinessType = NonFinalIncomeType.fromValue(oldData.BusinessTypeE) ?: ""
					cityId = oldData.City.Id
					selectedCity = oldData.City.CityName
					isPayingPPh25 = oldData.IsPayingPPhPasal25
					brutoList = oldData.BrutoCirculations ?: emptyList()
					kluId = oldData.KLU?.Id ?: 0
					selectedKlu = oldData.KLU?.KLUName ?: ""
					taxNormPerc = oldData.TaxNormPerc.toString()
					businessCirculationIDR = "Rp ${CurrencyFormatter(BigDeciToString(oldData.BusinessCirculationIDR.toString()))}"
					nettIncomeIDR = "Rp ${CurrencyFormatter(BigDeciToString(oldData.NettIncomeIDR.toString()))}"
					
					brutoList.forEach {
						when(it.Period.takeLast(2)) {
							"01" -> {
								bruto1IDR = CurrencyFormatter(BigDeciToString(it.IncomeIDR.toString()))
								bruto1IDRActual = BigDeciToLong(it.IncomeIDR.toString())
							}
							"02" -> {
								bruto2IDR = CurrencyFormatter(BigDeciToString(it.IncomeIDR.toString()))
								bruto2IDRActual = BigDeciToLong(it.IncomeIDR.toString())
							}
							"03" -> {
								bruto3IDR = CurrencyFormatter(BigDeciToString(it.IncomeIDR.toString()))
								bruto3IDRActual = BigDeciToLong(it.IncomeIDR.toString())
							}
							"04" -> {
								bruto4IDR = CurrencyFormatter(BigDeciToString(it.IncomeIDR.toString()))
								bruto4IDRActual = BigDeciToLong(it.IncomeIDR.toString())
							}
							"05" -> {
								bruto5IDR = CurrencyFormatter(BigDeciToString(it.IncomeIDR.toString()))
								bruto5IDRActual = BigDeciToLong(it.IncomeIDR.toString())
							}
							"06" -> {
								bruto6IDR = CurrencyFormatter(BigDeciToString(it.IncomeIDR.toString()))
								bruto6IDRActual = BigDeciToLong(it.IncomeIDR.toString())
							}
							"07" -> {
								bruto7IDR = CurrencyFormatter(BigDeciToString(it.IncomeIDR.toString()))
								bruto7IDRActual = BigDeciToLong(it.IncomeIDR.toString())
							}
							"08" -> {
								bruto8IDR = CurrencyFormatter(BigDeciToString(it.IncomeIDR.toString()))
								bruto8IDRActual = BigDeciToLong(it.IncomeIDR.toString())
							}
							"09" -> {
								bruto9IDR = CurrencyFormatter(BigDeciToString(it.IncomeIDR.toString()))
								bruto9IDRActual = BigDeciToLong(it.IncomeIDR.toString())
							}
							"10" -> {
								bruto10IDR = CurrencyFormatter(BigDeciToString(it.IncomeIDR.toString()))
								bruto10IDRActual = BigDeciToLong(it.IncomeIDR.toString())
							}
							"11" -> {
								bruto11IDR = CurrencyFormatter(BigDeciToString(it.IncomeIDR.toString()))
								bruto11IDRActual = BigDeciToLong(it.IncomeIDR.toString())
							}
							"12" -> {
								bruto12IDR = CurrencyFormatter(BigDeciToString(it.IncomeIDR.toString()))
								bruto12IDRActual = BigDeciToLong(it.IncomeIDR.toString())
							}
						}
					}
					
					businessNPWP = oldData.NPWP ?: ""
					businessAddress = oldData.BusinessAddress ?: ""
					kpp = oldData.KPPLocation ?: ""
				}
			}
			
			isReady = true
		}
		
		fun calculateNettIncome() {
			businessCirculationIDR = (bruto1IDRActual + bruto2IDRActual + bruto3IDRActual + bruto4IDRActual + bruto5IDRActual + bruto6IDRActual + bruto7IDRActual + bruto8IDRActual + bruto9IDRActual + bruto10IDRActual + bruto11IDRActual + bruto12IDRActual).toString()
			nettIncomeIDR = "Rp ${CurrencyFormatter(BigDeciToString(((businessCirculationIDR.replace("R", "").replace("p", "").replace(".", "").replace(" ", "").replace(",", "").toLongOrNull() ?: 0) * BigDeciToLong(taxNormPerc) / 100L).toString()))}"
		}
		
		fun calculateTaxNorm() {
			val kluObj = kluList.firstOrNull { it.Id == kluId }
			val cityObj = cityList.firstOrNull { it.Id == cityId }
			
			if (kluObj != null && cityObj != null) {
				taxNormPerc = when (cityObj.CityCtgE) {
					CityCategory.MAIN_CAPITALS.value -> kluObj.N1A.toString()
					CityCategory.OTHER_CAPITALS.value -> kluObj.N1B.toString()
					CityCategory.OTHER_AREAS.value -> kluObj.N1C.toString()
					else -> kluObj.N1A.toString()
				}
			}
			
			calculateNettIncome()
		}
		
		fun checkValidity(): Boolean {
			if(isPayingPPh25){
				return businessTypeE != 0 &&
						cityId != 0 &&
						businessNPWP.isNotBlank() &&
						kpp.isNotBlank() &&
						businessAddress.isNotBlank() &&
						kluId != 0
			} else {
				return businessTypeE != 0 &&
						cityId != 0 &&
						kluId != 0
			}
		}
		
		@Composable
		fun businessTypeSelector() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp),
				text = "Jenis Usaha",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			
			Box(
				modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
					.background(if(id == 0) Color.White else Colors().slate20, RoundedCornerShape(4.dp))
					.border(1.dp, Colors().textDarkGrey, RoundedCornerShape(4.dp))
					.clickable(id == 0, onClick = {
						showBusinessTypePopup = true
					})
			) {
				Row (
					modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
					horizontalArrangement = Arrangement.SpaceBetween,
					verticalAlignment = Alignment.CenterVertically
				){
					Text(
						text = selectedBusinessType.ifBlank { "Pilih Jenis Usaha" },
						fontSize = 14.sp,
						color = if(selectedBusinessType == "" || id != 0) Colors().textDarkGrey else Colors().textBlack
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
		fun isPPh25Switch() {
			Text(
				text = "Pembayaran Angsuran PPh Pasal 25",
				fontSize = 14.sp,
				fontWeight = FontWeight.Bold,
				color = Color(0xFF495057),
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp)
			)
			Row(
				modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
				verticalAlignment = Alignment.CenterVertically
			) {
				Switch(
					checked = isPayingPPh25,
					onCheckedChange = {
						isPayingPPh25 = it
					},
					colors = SwitchDefaults.colors(
						checkedThumbColor = Color.White,
						uncheckedThumbColor = Color.White,
						checkedTrackColor = Colors().brandDark40,
						uncheckedTrackColor = Colors().slate40,
						checkedTrackAlpha = 1f,
						uncheckedTrackAlpha = 1f
					),
					modifier = Modifier.padding(end = 8.dp)
				)
				Text(
					text = "Anda termasuk WP OPPT dan telah menyetor PPh Pasal 25",
					fontSize = 14.sp,
					color = Color(0xFF495057),
					modifier = Modifier.clickable(true, onClick = {
						isPayingPPh25 = !isPayingPPh25
					})
				)
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
				enabled = isPayingPPh25,
				modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
					.padding(horizontal = 16.dp)
					.border(
						border = BorderStroke(1.dp, Colors().textDarkGrey),
						shape = RoundedCornerShape(4.dp)
					),
				value = businessNPWP,
				onValueChange = {
					if(businessNPWP.length <= 16) { businessNPWP = it }
				},
				singleLine = true,
				colors = textFieldColors(
					backgroundColor = if(isPayingPPh25) Color.White else Colors().slate20,
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
		fun kppLocationTextField() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Lokasi KPP",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			TextField(
				enabled = isPayingPPh25,
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
					backgroundColor = if(isPayingPPh25) Color.White else Colors().slate20,
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
		fun businessAddressTextField() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Alamat Tempat Usaha",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			TextField(
				enabled = isPayingPPh25,
				modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
					.padding(horizontal = 16.dp)
					.border(
						border = BorderStroke(1.dp, Colors().textDarkGrey),
						shape = RoundedCornerShape(4.dp)
					),
				value = businessAddress,
				onValueChange = {
					if(businessAddress.length <= 255) { businessAddress = it }
				},
				singleLine = true,
				colors = textFieldColors(
					backgroundColor = if(isPayingPPh25) Color.White else Colors().slate20,
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
		fun brutoCircTotal() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Rincian Peredaran Bruto",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			Box(
				modifier = Modifier
					.fillMaxWidth()
					.padding(horizontal = 16.dp, vertical = 8.dp)
					.clip(RoundedCornerShape(8.dp))
					.border(1.dp, Colors().textDarkGrey, RoundedCornerShape(8.dp))
					.background(Colors().panel)
			) {
				Column(
					modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 36.dp)
				) {
					Text(
						text = "Total Penghasilan",
						fontSize = 10.sp,
						color = Colors().textBlack,
						modifier = Modifier.padding(bottom = 8.dp)
					)
					
					Text(
						text = businessCirculationIDR.ifBlank { "Rp 0" },
						fontSize = 20.sp,
						color = Colors().textBlack,
						fontWeight = FontWeight.Bold
					)
				}
			}
		}
		
		@Composable
		fun brutoListTextField() {
			//Jan
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Januari",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			TextField(
				modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
					.padding(horizontal = 16.dp)
					.border(
						border = BorderStroke(1.dp, Colors().textDarkGrey),
						shape = RoundedCornerShape(4.dp)
					),
				value = bruto1IDR,
				onValueChange = {
					bruto1IDR = it
					bruto1IDRActual = bruto1IDR.replace("R", "").replace("p", "").replace(".", "").replace(" ", "").replace(",", "").toLongOrNull() ?: 0L
					calculateNettIncome()
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
			
			//Feb
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Februari",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			TextField(
				modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
					.padding(horizontal = 16.dp)
					.border(
						border = BorderStroke(1.dp, Colors().textDarkGrey),
						shape = RoundedCornerShape(4.dp)
					),
				value = bruto2IDR,
				onValueChange = {
					bruto2IDR = it
					bruto2IDRActual = bruto2IDR.replace("R", "").replace("p", "").replace(".", "").replace(" ", "").replace(",", "").toLongOrNull() ?: 0L
					calculateNettIncome()
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
			
			//Mar
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Maret",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			TextField(
				modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
					.padding(horizontal = 16.dp)
					.border(
						border = BorderStroke(1.dp, Colors().textDarkGrey),
						shape = RoundedCornerShape(4.dp)
					),
				value = bruto3IDR,
				onValueChange = {
					bruto3IDR = it
					bruto3IDRActual = bruto3IDR.replace("R", "").replace("p", "").replace(".", "").replace(" ", "").replace(",", "").toLongOrNull() ?: 0L
					calculateNettIncome()
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
			
			//Apr
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "April",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			TextField(
				modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
					.padding(horizontal = 16.dp)
					.border(
						border = BorderStroke(1.dp, Colors().textDarkGrey),
						shape = RoundedCornerShape(4.dp)
					),
				value = bruto4IDR,
				onValueChange = {
					bruto4IDR = it
					bruto4IDRActual = bruto4IDR.replace("R", "").replace("p", "").replace(".", "").replace(" ", "").replace(",", "").toLongOrNull() ?: 0L
					calculateNettIncome()
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
			
			//May
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Mei",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			TextField(
				modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
					.padding(horizontal = 16.dp)
					.border(
						border = BorderStroke(1.dp, Colors().textDarkGrey),
						shape = RoundedCornerShape(4.dp)
					),
				value = bruto5IDR,
				onValueChange = {
					bruto5IDR = it
					bruto5IDRActual = bruto5IDR.replace("R", "").replace("p", "").replace(".", "").replace(" ", "").replace(",", "").toLongOrNull() ?: 0L
					calculateNettIncome()
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
			
			//Jun
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Juni",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			TextField(
				modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
					.padding(horizontal = 16.dp)
					.border(
						border = BorderStroke(1.dp, Colors().textDarkGrey),
						shape = RoundedCornerShape(4.dp)
					),
				value = bruto6IDR,
				onValueChange = {
					bruto6IDR = it
					bruto6IDRActual = bruto6IDR.replace("R", "").replace("p", "").replace(".", "").replace(" ", "").replace(",", "").toLongOrNull() ?: 0L
					calculateNettIncome()
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
			
			//Jul
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Juli",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			TextField(
				modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
					.padding(horizontal = 16.dp)
					.border(
						border = BorderStroke(1.dp, Colors().textDarkGrey),
						shape = RoundedCornerShape(4.dp)
					),
				value = bruto7IDR,
				onValueChange = {
					bruto7IDR = it
					bruto7IDRActual = bruto7IDR.replace("R", "").replace("p", "").replace(".", "").replace(" ", "").replace(",", "").toLongOrNull() ?: 0L
					calculateNettIncome()
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
			
			//Aug
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Agustus",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			TextField(
				modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
					.padding(horizontal = 16.dp)
					.border(
						border = BorderStroke(1.dp, Colors().textDarkGrey),
						shape = RoundedCornerShape(4.dp)
					),
				value = bruto8IDR,
				onValueChange = {
					bruto8IDR = it
					bruto8IDRActual = bruto8IDR.replace("R", "").replace("p", "").replace(".", "").replace(" ", "").replace(",", "").toLongOrNull() ?: 0L
					calculateNettIncome()
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
			
			//Sep
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "September",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			TextField(
				modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
					.padding(horizontal = 16.dp)
					.border(
						border = BorderStroke(1.dp, Colors().textDarkGrey),
						shape = RoundedCornerShape(4.dp)
					),
				value = bruto9IDR,
				onValueChange = {
					bruto9IDR = it
					bruto9IDRActual = bruto9IDR.replace("R", "").replace("p", "").replace(".", "").replace(" ", "").replace(",", "").toLongOrNull() ?: 0L
					calculateNettIncome()
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
			
			//Oct
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Oktober",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			TextField(
				modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
					.padding(horizontal = 16.dp)
					.border(
						border = BorderStroke(1.dp, Colors().textDarkGrey),
						shape = RoundedCornerShape(4.dp)
					),
				value = bruto10IDR,
				onValueChange = {
					bruto10IDR = it
					bruto10IDRActual = bruto10IDR.replace("R", "").replace("p", "").replace(".", "").replace(" ", "").replace(",", "").toLongOrNull() ?: 0L
					calculateNettIncome()
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
			
			//Nov
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "November",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			TextField(
				modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
					.padding(horizontal = 16.dp)
					.border(
						border = BorderStroke(1.dp, Colors().textDarkGrey),
						shape = RoundedCornerShape(4.dp)
					),
				value = bruto11IDR,
				onValueChange = {
					bruto11IDR = it
					bruto11IDRActual = bruto11IDR.replace("R", "").replace("p", "").replace(".", "").replace(" ", "").replace(",", "").toLongOrNull() ?: 0L
					calculateNettIncome()
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
			
			//Dec
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Desember",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			TextField(
				modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
					.padding(horizontal = 16.dp)
					.border(
						border = BorderStroke(1.dp, Colors().textDarkGrey),
						shape = RoundedCornerShape(4.dp)
					),
				value = bruto12IDR,
				onValueChange = {
					bruto12IDR = it
					bruto12IDRActual = bruto12IDR.replace("R", "").replace("p", "").replace(".", "").replace(" ", "").replace(",", "").toLongOrNull() ?: 0L
					calculateNettIncome()
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
		fun taxNormPercTextField() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Norma %",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			TextField(
				enabled = false,
				modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
					.padding(horizontal = 16.dp)
					.border(
						border = BorderStroke(1.dp, Colors().textDarkGrey),
						shape = RoundedCornerShape(4.dp)
					),
				value = "$taxNormPerc %",
				onValueChange = {},
				singleLine = true,
				colors = textFieldColors(
					disabledTextColor = Colors().textBlack,
					backgroundColor = Colors().slate20,
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
		fun nettIncomeTextField() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Penghasilan Neto",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			TextField(
				enabled = false,
				modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
					.padding(horizontal = 16.dp)
					.border(
						border = BorderStroke(1.dp, Colors().textDarkGrey),
						shape = RoundedCornerShape(4.dp)
					),
				value = nettIncomeIDR,
				onValueChange = {},
				singleLine = true,
				colors = textFieldColors(
					disabledTextColor = Colors().textBlack,
					backgroundColor = Colors().slate20,
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
				
				//Business Type Selector
				item { businessTypeSelector() }
				
				//City Selector
				item { citySelector() }
				
				//PPh 25 Switch
				item { isPPh25Switch() }
				
				//Business NPWP
				item { businessNPWPTextField() }
				
				//KPP Location
				item { kppLocationTextField() }
				
				//Business Addrress
				item { businessAddressTextField() }
				
				//Bruto Circ Total
				item { brutoCircTotal() }
				
				//Bruto Circ
				item { brutoListTextField() }
				
				//KLU Selector
				item { kluSelector() }
				
				//Tax Norma
				item { taxNormPercTextField() }
				
				//Nett Income
				item { nettIncomeTextField() }
				
				//Submit Button
				item {
					Button(
						enabled = checkValidity(),
						modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 16.dp),
						colors = buttonColors(backgroundColor = Colors().buttonActive, contentColor = Color.White),
						onClick = {
							val brutoListReq: List<BrutoCirculationRequestApiModel> = listOf(
								BrutoCirculationRequestApiModel(
									Id = null,
									Tr1770IncomeId = null,
									Period = "${sptHd!!.TaxYear}-01-01",
									IncomeIDR =bruto12IDRActual,
									TaxPayableIDR = null
								),
								BrutoCirculationRequestApiModel(
									Id = null,
									Tr1770IncomeId = null,
									Period = "${sptHd.TaxYear}-02-01",
									IncomeIDR =bruto12IDRActual,
									TaxPayableIDR = null
								),
								BrutoCirculationRequestApiModel(
									Id = null,
									Tr1770IncomeId = null,
									Period = "${sptHd.TaxYear}-03-01",
									IncomeIDR =bruto12IDRActual,
									TaxPayableIDR = null
								),
								BrutoCirculationRequestApiModel(
									Id = null,
									Tr1770IncomeId = null,
									Period = "${sptHd.TaxYear}-04-01",
									IncomeIDR =bruto12IDRActual,
									TaxPayableIDR = null
								),
								BrutoCirculationRequestApiModel(
									Id = null,
									Tr1770IncomeId = null,
									Period = "${sptHd.TaxYear}-05-01",
									IncomeIDR =bruto12IDRActual,
									TaxPayableIDR = null
								),
								BrutoCirculationRequestApiModel(
									Id = null,
									Tr1770IncomeId = null,
									Period = "${sptHd.TaxYear}-06-01",
									IncomeIDR =bruto12IDRActual,
									TaxPayableIDR = null
								),
								BrutoCirculationRequestApiModel(
									Id = null,
									Tr1770IncomeId = null,
									Period = "${sptHd.TaxYear}-07-01",
									IncomeIDR =bruto12IDRActual,
									TaxPayableIDR = null
								),
								BrutoCirculationRequestApiModel(
									Id = null,
									Tr1770IncomeId = null,
									Period = "${sptHd.TaxYear}-08-01",
									IncomeIDR =bruto12IDRActual,
									TaxPayableIDR = null
								),
								BrutoCirculationRequestApiModel(
									Id = null,
									Tr1770IncomeId = null,
									Period = "${sptHd.TaxYear}-09-01",
									IncomeIDR =bruto12IDRActual,
									TaxPayableIDR = null
								),
								BrutoCirculationRequestApiModel(
									Id = null,
									Tr1770IncomeId = null,
									Period = "${sptHd.TaxYear}-10-01",
									IncomeIDR = bruto12IDRActual,
									TaxPayableIDR = null
								),
								BrutoCirculationRequestApiModel(
									Id = null,
									Tr1770IncomeId = null,
									Period = "${sptHd.TaxYear}-11-01",
									IncomeIDR = bruto12IDRActual,
									TaxPayableIDR = null
								),
								BrutoCirculationRequestApiModel(
									Id = null,
									Tr1770IncomeId = null,
									Period = "${sptHd.TaxYear}-12-01",
									IncomeIDR = bruto12IDRActual,
									TaxPayableIDR = null
								)
							)
							
							if(isPayingPPh25) {
								val dataModel = FormNonFinalIncomeRequestApiModel(
									Id = id,
									Tr1770HdId = sptHd.Id,
									BusinessTypeE = businessTypeE,
									CityId = cityId,
									KluId = kluId,
									IsPayingPPhPasal25 = isPayingPPh25,
									NPWP = businessNPWP,
									KPPLocation = kpp,
									BusinessAddress = businessAddress,
									BrutoCirculations = brutoListReq
								)
								
								scope.launch {
									isReady = false
									sptManager.saveIncomeNonFinal(scope, dataModel)
									navigator.pop()
								}
							} else {
								val dataModel = FormNonFinalIncomeRequestApiModel(
									Id = id,
									Tr1770HdId = sptHd.Id,
									BusinessTypeE = businessTypeE,
									CityId = cityId,
									KluId = kluId,
									IsPayingPPhPasal25 = isPayingPPh25,
									NPWP = null,
									KPPLocation = null,
									BusinessAddress = null,
									BrutoCirculations = brutoListReq
								)
								
								scope.launch {
									isReady = false
									sptManager.saveIncomeNonFinal(scope, dataModel)
									navigator.pop()
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
			showPopup = showBusinessTypePopup,
			onClickOutside = { showBusinessTypePopup = false },
			content = {
				Column(
					modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 12.dp)
				) {
					Text(
						text = "Jenis Usaha",
						fontSize = 12.sp,
						color = Colors().textBlack,
						fontWeight = FontWeight.Bold,
						modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
					)
					LazyColumn(modifier = Modifier.heightIn(max = 200.dp).padding(horizontal = 18.dp)) {
						NonFinalIncomeType.entries.forEach {
							item {
								Text(
									text = NonFinalIncomeType.fromValue(it.value).toString(),
									fontSize = 18.sp,
									modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
										.clickable(true, onClick = {
											selectedBusinessType = NonFinalIncomeType.fromValue(it.value).toString()
											businessTypeE = it.value
											showBusinessTypePopup = false
										})
								)
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
												calculateTaxNorm()
											})
									)
								}
							}
						}
					}
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
												calculateTaxNorm()
											})
									)
								}
							}
						}
					}
				}
			}
		)
		
		//Delete Non Final Income
		popUpBox(
			showPopup = showDeletePopup,
			onClickOutside = { showDeletePopup = false },
			content = {
				Text(
					text = "Hapus Penghasilan",
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
						text = "Hapus Penghasilan",
						fontSize = 18.sp,
						color = Colors().textBlack,
						fontWeight = FontWeight.Bold
					)
					
					Text(
						modifier = Modifier.padding(vertical = 24.dp, horizontal = 16.dp),
						text = "Apakah Anda yakin ingin menghapus Penghasilan ini?",
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
									sptManager.deleteIncomeNonFinal(scope, id.toString())
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