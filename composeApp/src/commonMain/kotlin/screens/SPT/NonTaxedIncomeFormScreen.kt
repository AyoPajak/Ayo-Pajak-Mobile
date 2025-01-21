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
import ayopajakmobile.composeapp.generated.resources.icon_tripledot_black
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import global.Colors
import global.FinalIncomeType
import global.NonTaxedIncomeType
import global.universalUIComponents.loadingPopupBox
import global.universalUIComponents.popUpBox
import http.Account
import http.Interfaces
import kotlinx.coroutines.launch
import models.transaction.Form1770FinalIncomeUmkm2023BusinessRequestModel
import models.transaction.Form1770HdResponseApiModel
import models.transaction.FormNonTaxedIncomeRequestApiModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource
import util.BigDeciToLong
import util.BigDeciToString
import util.CurrencyFormatter

class NonTaxedIncomeFormScreen(val id: Int, val sptHd: Form1770HdResponseApiModel?, val client: Account, val sptPertamaClient: Interfaces, val prefs: DataStore<Preferences>): Screen {
	
	val sptManager = SPTManager(prefs, client, sptPertamaClient)
	
	@Composable
	override fun Content() {
		
		val scope = rememberCoroutineScope()
		val navigator = LocalNavigator.currentOrThrow
		
		var showDeletePopup by remember { mutableStateOf(false) }
		var showConfirmPopup by remember { mutableStateOf(false) }
		var showIncomeTypePopup by remember { mutableStateOf(false) }
		
		var incomeTypeE by remember { mutableStateOf(0) }
		var selectedIncomeType by remember { mutableStateOf("") }
		
		var incomeIDR by remember { mutableStateOf("") }
		var incomeIDRActual by remember { mutableStateOf(0L) }
		
		var employerName by remember { mutableStateOf("") }
		var description by remember { mutableStateOf("") }
		
		var isReady by remember { mutableStateOf(false) }
		
		LaunchedEffect(null) {
			
			if(id != 0){
				val oldData = sptManager.getNonTaxedIncomeDataById(scope, id.toString())
				
				if (oldData != null) {
					selectedIncomeType = NonTaxedIncomeType.fromValue(oldData.IncomeTypeE) ?: ""
					incomeTypeE = oldData.IncomeTypeE
					incomeIDR = "Rp ${CurrencyFormatter(BigDeciToString(oldData.IncomeIDR.toString()))}"
					incomeIDRActual = BigDeciToLong(oldData.IncomeIDR.toString())
					employerName = oldData.EmployerName ?: ""
					description = oldData.Description ?: ""
				}
			}
			
			isReady = true
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
				text = "Penghasilan Bruto (IDR)",
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
					incomeIDRActual = if(incomeIDR.isBlank() || incomeIDR == "-") 0 else incomeIDR.toLong()
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
		fun employerNameTextField() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Nama Pemberi Penghasilan",
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
				value = employerName,
				onValueChange = {
					if(employerName.length <= 200 ) { employerName = it }
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
					if(description.length <= 500 ) { description = it }
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
				
				//Income Type
				item { incomeTypeSelector() }
				
				//Income
				item { incomeTextField() }
				
				//Employer Name
				item { employerNameTextField() }
				
				//Description
				item { descriptionTextField() }
				
				//Submit Button
				item {
					Button(
						enabled = incomeTypeE != 0 && incomeIDR.isNotBlank(),
						modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 22.dp),
						colors = buttonColors(backgroundColor = Colors().buttonActive, contentColor = Color.White),
						onClick = {
							
							val dataModel = FormNonTaxedIncomeRequestApiModel(
								Id = id,
								Tr1770HdId = sptHd!!.Id,
								IncomeTypeE = incomeTypeE,
								IncomeIDR = incomeIDRActual,
								EmployerName = employerName.ifBlank { null },
								Description = description.ifBlank { null }
							)
							
							scope.launch {
								isReady = false
								sptManager.saveNonTaxedIncome(scope, dataModel)
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
						NonTaxedIncomeType.entries.forEach {
							item {
								Text(
									text = NonTaxedIncomeType.fromValue(it.value).toString(),
									fontSize = 18.sp,
									modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
										.clickable(true, onClick = {
											selectedIncomeType = NonTaxedIncomeType.fromValue(it.value).toString()
											incomeTypeE = it.value
											showIncomeTypePopup = false
										})
								)
							}
						}
					}
				}
			}
		)
		
		//Delete Non Taxed Income Popup
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