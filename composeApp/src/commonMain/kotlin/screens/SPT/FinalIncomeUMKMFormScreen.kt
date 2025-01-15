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
import global.Colors
import global.universalUIComponents.loadingPopupBox
import global.universalUIComponents.popUpBox
import http.Account
import http.Interfaces
import kotlinx.coroutines.launch
import models.ApiODataQueryModel
import models.master.CityModel
import models.master.KluModel
import models.transaction.BrutoCirculationRequestApiModel
import models.transaction.Form1770FinalIncomeUmkm2023BusinessModel
import models.transaction.Form1770FinalIncomeUmkm2023BusinessRequestModel
import models.transaction.Form1770HdResponseApiModel
import models.transaction.FormFinalIncomeARequestApiModel
import models.transaction.FormFinalIncomeBRequestApiModel
import models.transaction.FormFinalIncomeCRequestApiModel
import models.transaction.FormFinalIncomeDRequestApiModel
import models.transaction.FormFinalIncomeERequestApiModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource
import util.BigDeciToString
import util.CurrencyFormatter

class FinalIncomeUMKMFormScreen(val id: Int, val formData: Form1770FinalIncomeUmkm2023BusinessModel? = null, val sptHd: Form1770HdResponseApiModel?, val client: Account, val sptPertamaClient: Interfaces, val prefs: DataStore<Preferences>): Screen {
	
	val sptManager: SPTManager = SPTManager(prefs, client, sptPertamaClient)
	
	@Composable
	override fun Content() {
		
		val scope = rememberCoroutineScope()
		val navigator = LocalNavigator.currentOrThrow
		
		var showDeletePopup by remember { mutableStateOf(false) }
		var showConfirmPopup by remember { mutableStateOf(false) }
		var showKLUPopup by remember { mutableStateOf(false) }
		var showCityPopup by remember { mutableStateOf(false) }
		
		var kluList by remember { mutableStateOf<List<KluModel>>(emptyList())}
		var selectedKlu by remember { mutableStateOf("") }
		var kluId by remember { mutableStateOf(0) }
		
		var cityList by remember { mutableStateOf<List<CityModel>>(emptyList())}
		var selectedCity by remember { mutableStateOf("") }
		var cityId by remember { mutableStateOf(0) }
		
		var kluSearchText by remember { mutableStateOf("") }
		var citySearchText by remember { mutableStateOf("") }
		
		var businessNPWP by remember { mutableStateOf("") }
		var businessName by remember { mutableStateOf("") }
		var provinsi by remember { mutableStateOf("") }
		var kecamatan by remember { mutableStateOf("") }
		var kelurahan by remember { mutableStateOf("") }
		var businessAddress by remember { mutableStateOf("") }
		
		var bruto_1 by remember { mutableStateOf("0") }
		var bruto_2 by remember { mutableStateOf("0") }
		var bruto_3 by remember { mutableStateOf("0") }
		var bruto_4 by remember { mutableStateOf("0") }
		var bruto_5 by remember { mutableStateOf("0") }
		var bruto_6 by remember { mutableStateOf("0") }
		var bruto_7 by remember { mutableStateOf("0") }
		var bruto_8 by remember { mutableStateOf("0") }
		var bruto_9 by remember { mutableStateOf("0") }
		var bruto_10 by remember { mutableStateOf("0") }
		var bruto_11 by remember { mutableStateOf("0") }
		var bruto_12 by remember { mutableStateOf("0") }
		var total by remember { mutableStateOf(0L) }
		
		var isReady by remember { mutableStateOf(false) }
		
		fun checkValidity(): Boolean {
			return businessNPWP.isNotBlank() &&
					businessName.isNotBlank() &&
					businessAddress.isNotBlank() &&
					kelurahan.isNotBlank() &&
					kecamatan.isNotBlank() &&
					provinsi.isNotBlank() &&
					cityId != 0 &&
					kluId != 0
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
			cityList = sptManager.getCityList(scope)
			populateKluList()
			
			if(id != 0) {
				businessNPWP = formData?.NPWP ?: ""
				businessName = formData?.BusinessName ?: ""
				selectedKlu = formData?.KLUStr ?: ""
				kluId = formData?.KLUId ?: 0
				provinsi = formData?.Provinsi ?: ""
				selectedCity = formData?.CityStr ?: ""
				cityId = formData?.CityId ?: 0
				kecamatan = formData?.Kecamatan ?: ""
				kelurahan = formData?.Kelurahan ?: ""
				businessAddress = formData?.BusinessAddress ?: ""
				bruto_1 = "Rp ${CurrencyFormatter(BigDeciToString(formData?.Bruto_1.toString()))}"
				bruto_2 = "Rp ${CurrencyFormatter(BigDeciToString(formData?.Bruto_2.toString()))}"
				bruto_3 = "Rp ${CurrencyFormatter(BigDeciToString(formData?.Bruto_3.toString()))}"
				bruto_4 = "Rp ${CurrencyFormatter(BigDeciToString(formData?.Bruto_4.toString()))}"
				bruto_5 = "Rp ${CurrencyFormatter(BigDeciToString(formData?.Bruto_5.toString()))}"
				bruto_6 = "Rp ${CurrencyFormatter(BigDeciToString(formData?.Bruto_6.toString()))}"
				bruto_7 = "Rp ${CurrencyFormatter(BigDeciToString(formData?.Bruto_7.toString()))}"
				bruto_8 = "Rp ${CurrencyFormatter(BigDeciToString(formData?.Bruto_8.toString()))}"
				bruto_9 = "Rp ${CurrencyFormatter(BigDeciToString(formData?.Bruto_9.toString()))}"
				bruto_10 = "Rp ${CurrencyFormatter(BigDeciToString(formData?.Bruto_10.toString()))}"
				bruto_11 = "Rp ${CurrencyFormatter(BigDeciToString(formData?.Bruto_11.toString()))}"
				bruto_12 = "Rp ${CurrencyFormatter(BigDeciToString(formData?.Bruto_12.toString()))}"
			}
			
			isReady = true
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
								text = if (id == 0) "Tambah Tempat Usaha" else "Edit Tempat Usaha",
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
				
				//Business NPWP
				item {
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
				
				//Business Name
				item {
					Text(
						modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
						text = "Nama Tempat Usaha",
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
						value = businessName,
						onValueChange = {
							if(businessName.length <= 50){ businessName = it }
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
				
				//KLU
				item {
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
				
				//Provinsi
				item {
					Text(
						modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
						text = "Provinsi Tempat Usaha",
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
						value = provinsi,
						onValueChange = {
							if(provinsi.length <= 50){ provinsi = it }
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
				
				//City
				item {
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
				
				//Kecamatan
				item {
					Text(
						modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
						text = "Kecamatan Tempat Usaha",
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
						value = kecamatan,
						onValueChange = {
							if(kecamatan.length <= 50){ kecamatan = it }
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
				
				//Kelurahan
				item {
					Text(
						modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
						text = "Kecamatan Tempat Usaha",
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
						value = kelurahan,
						onValueChange = {
							if(kelurahan.length <= 50){ kelurahan = it }
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
				
				//Alamat
				item {
					Text(
						modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
						text = "Kecamatan Tempat Usaha",
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
							if(businessAddress.length <= 50){ businessAddress = it }
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
				
				//Bruto Jan
				item {
					Text(
						modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
						text = "Bruto Bulan Januari",
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
						value = bruto_1,
						onValueChange = {
							bruto_1 = it
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
				
				//Bruto Feb
				item {
					Text(
						modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
						text = "Bruto Bulan Februari",
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
						value = bruto_2,
						onValueChange = {
							bruto_2 = it
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
				
				//Bruto Mar
				item {
					Text(
						modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
						text = "Bruto Bulan Maret",
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
						value = bruto_3,
						onValueChange = {
							bruto_3 = it
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
				
				//Bruto Apr
				item {
					Text(
						modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
						text = "Bruto Bulan April",
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
						value = bruto_4,
						onValueChange = {
							bruto_4 = it
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
				
				//Bruto May
				item {
					Text(
						modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
						text = "Bruto Bulan Mei",
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
						value = bruto_5,
						onValueChange = {
							bruto_5 = it
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
				
				//Bruto Jun
				item {
					Text(
						modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
						text = "Bruto Bulan Juni",
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
						value = bruto_6,
						onValueChange = {
							bruto_6 = it
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
				
				//Bruto Jul
				item {
					Text(
						modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
						text = "Bruto Bulan Juli",
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
						value = bruto_7,
						onValueChange = {
							bruto_7 = it
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
				
				//Bruto Aug
				item {
					Text(
						modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
						text = "Bruto Bulan Agustus",
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
						value = bruto_8,
						onValueChange = {
							bruto_8 = it
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
				
				//Bruto Sep
				item {
					Text(
						modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
						text = "Bruto Bulan September",
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
						value = bruto_9,
						onValueChange = {
							bruto_9 = it
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
				
				//Bruto Oct
				item {
					Text(
						modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
						text = "Bruto Bulan Oktober",
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
						value = bruto_10,
						onValueChange = {
							bruto_10 = it
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
				
				//Bruto Nov
				item {
					Text(
						modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
						text = "Bruto Bulan November",
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
						value = bruto_11,
						onValueChange = {
							bruto_11 = it
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
				
				//Bruto Dec
				item {
					Text(
						modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
						text = "Bruto Bulan Desember",
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
						value = bruto_12,
						onValueChange = {
							bruto_12 = it
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
				
				//Submit Button
				item {
					Button(
						enabled = checkValidity(),
						modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 22.dp),
						colors = buttonColors(backgroundColor = Colors().buttonActive, contentColor = Color.White),
						onClick = {
							bruto_1 = bruto_1.replace("Rp ", "")
							bruto_1 = bruto_1.replace(",", "")
							bruto_2 = bruto_2.replace("Rp ", "")
							bruto_2 = bruto_2.replace(",", "")
							bruto_3 = bruto_3.replace("Rp ", "")
							bruto_3 = bruto_3.replace(",", "")
							bruto_4 = bruto_4.replace("Rp ", "")
							bruto_4 = bruto_4.replace(",", "")
							bruto_5 = bruto_5.replace("Rp ", "")
							bruto_5 = bruto_5.replace(",", "")
							bruto_6 = bruto_6.replace("Rp ", "")
							bruto_6 = bruto_6.replace(",", "")
							bruto_7 = bruto_7.replace("Rp ", "")
							bruto_7 = bruto_7.replace(",", "")
							bruto_8 = bruto_8.replace("Rp ", "")
							bruto_8 = bruto_8.replace(",", "")
							bruto_9 = bruto_9.replace("Rp ", "")
							bruto_9 = bruto_9.replace(",", "")
							bruto_10 = bruto_10.replace("Rp ", "")
							bruto_10 = bruto_10.replace(",", "")
							bruto_11 = bruto_11.replace("Rp ", "")
							bruto_11 = bruto_11.replace(",", "")
							bruto_12 = bruto_12.replace("Rp ", "")
							bruto_12 = bruto_12.replace(",", "")
							total = bruto_1.ifBlank { 0L }.toString().toLong() +
									bruto_2.ifBlank { 0L }.toString().toLong() +
									bruto_3.ifBlank { 0L }.toString().toLong() +
									bruto_4.ifBlank { 0L }.toString().toLong() +
									bruto_5.ifBlank { 0L }.toString().toLong() +
									bruto_6.ifBlank { 0L }.toString().toLong() +
									bruto_7.ifBlank { 0L }.toString().toLong() +
									bruto_8.ifBlank { 0L }.toString().toLong() +
									bruto_9.ifBlank { 0L }.toString().toLong() +
									bruto_10.ifBlank { 0L }.toString().toLong() +
									bruto_11.ifBlank { 0L }.toString().toLong() +
									bruto_12.ifBlank { 0L }.toString().toLong()
							
							val dataModel = Form1770FinalIncomeUmkm2023BusinessRequestModel(
								Id = id,
								Tr1770HdId = sptHd!!.Id,
								Tr1770IncomeId = formData!!.Tr1770IncomeId,
								NPWP = businessNPWP,
								BusinessName = businessName,
								BusinessAddress = businessAddress,
								Kelurahan = kelurahan,
								Kecamatan = kecamatan,
								Provinsi = provinsi,
								CityId = cityId,
								CityStr = selectedCity,
								KLUId = kluId,
								KLUStr = selectedKlu,
								Bruto_1 = bruto_1.ifBlank { 0L }.toString().toLong(),
								Bruto_2 = bruto_2.ifBlank { 0L }.toString().toLong(),
								Bruto_3 = bruto_3.ifBlank { 0L }.toString().toLong(),
								Bruto_4 = bruto_4.ifBlank { 0L }.toString().toLong(),
								Bruto_5 = bruto_5.ifBlank { 0L }.toString().toLong(),
								Bruto_6 = bruto_6.ifBlank { 0L }.toString().toLong(),
								Bruto_7 = bruto_7.ifBlank { 0L }.toString().toLong(),
								Bruto_8 = bruto_8.ifBlank { 0L }.toString().toLong(),
								Bruto_9 = bruto_9.ifBlank { 0L }.toString().toLong(),
								Bruto_10 = bruto_10.ifBlank { 0L }.toString().toLong(),
								Bruto_11 = bruto_11.ifBlank { 0L }.toString().toLong(),
								Bruto_12 = bruto_12.ifBlank { 0L }.toString().toLong(),
								Total = total
							)
							
							scope.launch {
								isReady = false
								sptManager.saveFinalIncomeUmkm2023Business(scope, dataModel)
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
				
				//Spacer
				item {
					Box(
						modifier = Modifier.height(88.dp)
					)
				}
			}
		}
		
		//Popups
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
									sptManager.deleteFinalIncomeUmkm2023Business(scope, id.toString())
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