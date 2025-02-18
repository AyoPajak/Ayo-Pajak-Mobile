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
import models.master.DebtTypeModel
import models.transaction.Form1770HdResponseApiModel
import models.transaction.FormDebtRequestApiModel
import models.transaction.FormWealthARequestApiModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource
import screens.divider
import util.BigDeciToString
import util.CurrencyFormatter

class DebtFormScreen(val id: Int, val sptHd: Form1770HdResponseApiModel?, val client: Account, val sptPertamaClient: Interfaces, val prefs: DataStore<Preferences>): Screen {
	
	val sptManager = SPTManager(prefs, client, sptPertamaClient)
	
	@Composable
	override fun Content() {
		
		val scope = rememberCoroutineScope()
		val navigator = LocalNavigator.currentOrThrow
		
		var showDebtTypePopup by remember { mutableStateOf(false) }
		var showDeletePopup by remember { mutableStateOf(false) }
		var showConfirmPopup by remember { mutableStateOf(false) }
		
		var debtTypeList by remember { mutableStateOf<List<DebtTypeModel>>(emptyList()) }
		
		var selectedDebtType by remember { mutableStateOf("") }
		
		var debtTypeCode by remember { mutableStateOf("") }
		var lenderName by remember { mutableStateOf("") }
		var lenderAddress by remember { mutableStateOf("") }
		var debtYear by remember { mutableStateOf("") }
		var principalIDR by remember { mutableStateOf("") }
		var monInstallmentIDR by remember { mutableStateOf("") }
		var outstandingIDR by remember { mutableStateOf("") }
		
		var isReady by remember { mutableStateOf(false) }
		
		LaunchedEffect(null) {
			debtTypeList = sptManager.getDebtTypeList(scope) ?: listOf()
			
			if (id != 0) {
				val oldData = sptManager.getDebtDataById(scope, id.toString())
				
				if (oldData != null) {
					selectedDebtType = oldData.DebtType.DebtTypeName
					
					debtTypeCode = oldData.DebtType.DebtTypeCode
					lenderName = oldData.LenderName
					lenderAddress = oldData.LenderAddress
					debtYear = oldData.DebtYear
					principalIDR = BigDeciToString(oldData.Principal_IDR.toString())
					monInstallmentIDR = BigDeciToString(oldData.MonInstallment_IDR.toString())
					outstandingIDR = BigDeciToString(oldData.Outstanding_IDR.toString())
				}
			}
			
			isReady = true
		}
		
		fun checkFormValid(): Boolean {
			return debtTypeCode.isNotBlank() &&
					lenderName.isNotBlank() &&
					lenderAddress.isNotBlank() &&
					debtYear.isNotBlank() &&
					principalIDR.isNotBlank() &&
					monInstallmentIDR.isNotBlank() &&
					outstandingIDR.isNotBlank() &&
					principalIDR.toLong() > 0 &&
					monInstallmentIDR.toLong() > 0 &&
					outstandingIDR.toLong() > 0
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
				
				//Select Debt Type
				item {
					Text(
						modifier = Modifier.padding(horizontal = 16.dp).padding(top = 18.dp),
						text = "Jenis Utang",
						fontSize = 12.sp,
						color = Color.Black,
						fontWeight = FontWeight.Bold
					)
					
					Box(
						modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 8.dp)
							.background(if(id == 0) Color.White else Colors().slate20, RoundedCornerShape(4.dp))
							.border(1.dp, Colors().textDarkGrey, RoundedCornerShape(4.dp))
							.clickable(id == 0, onClick = {
								showDebtTypePopup = true
							})
					) {
						Row (
							modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
							horizontalArrangement = Arrangement.SpaceBetween,
							verticalAlignment = Alignment.CenterVertically
						){
							Text(
								text = if(selectedDebtType == "") "Pilih Jenis Utang" else selectedDebtType,
								fontSize = 14.sp,
								color = if(selectedDebtType == "" || id != 0) Colors().textDarkGrey else Colors().textBlack
							)
							Image(painterResource(Res.drawable.Icon_Dropdown_Arrow), null, modifier = Modifier.size(24.dp))
						}
					}
				}
				
				//Lender Name
				item {
					Text(
						modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
						text = "Nama Pemberi Pinjaman",
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
						value = lenderName,
						onValueChange = {
							if(it.length <= 500) { lenderName = it }
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
				
				//Lender Address
				item {
					Text(
						modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
						text = "Lender Address",
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
						value = lenderAddress,
						onValueChange = {
							if(it.length <= 500) { lenderAddress = it }
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
				
				//Debt Year
				item {
					Text(
						modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
						text = "Tahun Pinjaman",
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
						value = debtYear,
						onValueChange = {
							if(it.length <= 4) { debtYear = it }
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
				
				//Principal IDR
				item {
					Text(
						modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
						text = "Pokok Pinjaman",
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
						value = principalIDR,
						onValueChange = {
							principalIDR = it
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
				
				//Monthly Installment IDR
				item {
					Text(
						modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
						text = "Angsuran pada Tahun Pajak SPT",
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
						value = monInstallmentIDR,
						onValueChange = {
							monInstallmentIDR = it
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
				
				//Sisa Utang per Akhir Tahun
				item {
					Text(
						modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
						text = "Sisa Utang per Akhir Tahun",
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
						value = outstandingIDR,
						onValueChange = {
							outstandingIDR = it
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
						enabled = checkFormValid(),
						modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 22.dp),
						colors = buttonColors(backgroundColor = Colors().buttonActive, contentColor = Color.White),
						onClick = {
							principalIDR = principalIDR.replace("Rp. ", "")
							principalIDR = principalIDR.replace(",", "")
							monInstallmentIDR = monInstallmentIDR.replace("Rp. ", "")
							monInstallmentIDR = monInstallmentIDR.replace(",", "")
							outstandingIDR = outstandingIDR.replace("Rp. ", "")
							outstandingIDR = outstandingIDR.replace(",", "")
							
							val dataModel = FormDebtRequestApiModel(
								Id = id,
								Tr1770HdId = sptHd?.Id ?: 0,
								DebtTypeCode = debtTypeCode,
								LenderName = lenderName,
								LenderAddress = lenderAddress,
								DebtYear = debtYear.toInt(),
								Principal_IDR = principalIDR.toLong(),
								MonInstallment_IDR = monInstallmentIDR.toLong(),
								Outstanding_IDR = outstandingIDR.toLong()
							)
							
							println(dataModel)
							
							scope.launch {
								isReady = false
								sptManager.saveDebt(scope, dataModel)
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
		//Debt Type Popup
		popUpBox(
			popupWidth = 300f,
			popupHeight = 200f,
			showPopup = showDebtTypePopup,
			onClickOutside = { showDebtTypePopup = false },
			content = {
				Column(
					modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 12.dp)
				) {
					Text(
						text = "Jenis Utang",
						fontSize = 12.sp,
						color = Colors().textBlack,
						fontWeight = FontWeight.Bold,
						modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
					)
					LazyColumn(modifier = Modifier.heightIn(max = 200.dp).padding(horizontal = 18.dp)) {
						debtTypeList.forEach {
							item {
								Text(
									text = "${it.DebtTypeCode} - ${it.DebtTypeName}",
									fontSize = 18.sp,
									modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
										.clickable(true, onClick = {
											selectedDebtType = it.DebtTypeName
											debtTypeCode = it.DebtTypeCode
											showDebtTypePopup = false
										})
								)
								if(it != debtTypeList.last()){ divider(0.dp) }
							}
						}
					}
				}
			}
		)
		
		//Delete Debt Popup
		popUpBox(
			showPopup = showDeletePopup,
			onClickOutside = { showDeletePopup = false },
			content = {
				Text(
					text = "Hapus Utang",
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
						text = "Hapus Utang",
						fontSize = 18.sp,
						color = Colors().textBlack,
						fontWeight = FontWeight.Bold
					)
					
					Text(
						modifier = Modifier.padding(vertical = 24.dp, horizontal = 16.dp),
						text = "Apakah Anda yakin ingin menghapus Utang ini?",
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
									sptManager.deleteDebt(scope, id.toString())
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