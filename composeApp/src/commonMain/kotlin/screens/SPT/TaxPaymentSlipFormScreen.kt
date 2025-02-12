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
import androidx.compose.ui.text.input.KeyboardCapitalization
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
import global.Colors
import global.DepositType
import global.DomesticNetIncomeType
import global.OverseasNetIncomeType
import global.Variables
import global.universalUIComponents.loadingPopupBox
import global.universalUIComponents.popUpBox
import http.Account
import http.Interfaces
import kotlinx.coroutines.launch
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import models.transaction.Form1770HdResponseApiModel
import models.transaction.FormNetOtherIncomeARequestApiModel
import models.transaction.FormNetOtherIncomeBRequestApiModel
import models.transaction.FormNetOtherIncomeCRequestApiModel
import models.transaction.FormNetOtherIncomeDRequestApiModel
import models.transaction.FormNetOtherIncomeERequestApiModel
import models.transaction.FormNetOtherIncomeFRequestApiModel
import models.transaction.FormTaxPaymentSlipRequestApiModel
import network.chaintech.kmp_date_time_picker.ui.datepicker.WheelDatePickerComponent.WheelDatePicker
import network.chaintech.kmp_date_time_picker.utils.now
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource
import util.BigDeciToLong
import util.BigDeciToString
import util.CurrencyFormatter
import util.DateFormatter

class TaxPaymentSlipFormScreen(val id: Int, val sptHd: Form1770HdResponseApiModel?, val client: Account, val sptPertamaClient: Interfaces, val prefs: DataStore<Preferences>): Screen {
	
	val sptManager = SPTManager(prefs, client, sptPertamaClient)
	
	@Composable
	override fun Content() {
	
		val scope = rememberCoroutineScope()
		val navigator = LocalNavigator.currentOrThrow
		
		var showDeletePopup by remember { mutableStateOf(false) }
		var showConfirmPopup by remember { mutableStateOf(false) }
		var showDepositTypePopup by remember { mutableStateOf(false) }
		var showSSPDatePopup by remember { mutableStateOf(false) }
		
		var depositTypeE by remember { mutableStateOf(0) }
		var selectedDepositType by remember { mutableStateOf("") }
		var sspDate by remember { mutableStateOf("") }
		var sspAmountIDR by remember { mutableStateOf("") }
		var sspAmountIDRActual by remember { mutableStateOf(0L) }
		var ntpn by remember { mutableStateOf("") }
		
		var isReady by remember { mutableStateOf(false) }
		
		LaunchedEffect(null) {
			
			if(id != 0) {
				val oldData = sptManager.getTaxPaymentSlipDataById(scope, id.toString())
				
				if(oldData != null) {
					depositTypeE = oldData.DepositTypeE
					selectedDepositType = "[${oldData.DepositTypeE}] ${DepositType.fromValue(oldData.DepositTypeE)}"
					sspDate =oldData.SSPDate
					sspAmountIDR = "Rp ${CurrencyFormatter(BigDeciToString(oldData.SSPAmount.toString()))}"
					sspAmountIDRActual = BigDeciToLong(oldData.SSPAmount.toString())
					ntpn = oldData.NTPN ?: ""
				}
			}
			
			isReady = true
		}
		
		fun checkValidity(): Boolean {
			return if(ntpn.isNotBlank()) {
				depositTypeE != 0 && sspDate.isNotBlank() && sspAmountIDRActual > 0 && ntpn.length == 16
			} else {
				depositTypeE != 0 && sspDate.isNotBlank() && sspAmountIDRActual > 0
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
								text = if (id == 0) "Tambah Surat Setoran Pajak" else "Edit Surat Setoran Pajak",
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
				
				//KAP Code
				item {
					Text(
						modifier = Modifier.padding(horizontal = 16.dp),
						text = "Kode KAP",
						fontSize = 12.sp,
						color = Color.Black,
						fontWeight = FontWeight.Bold
					)
					TextField(
						enabled = false,
						modifier = Modifier.fillMaxWidth().padding(top = 8.dp).padding(horizontal = 16.dp)
							.border(
								border = BorderStroke(1.dp, Colors().textDarkGrey),
								shape = RoundedCornerShape(4.dp)
							),
						value = Variables.KAPCode,
						onValueChange = {
						
						},
						singleLine = true,
						colors = textFieldColors(
							backgroundColor = Colors().slate20,
							disabledTextColor = Colors().textDarkGrey,
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
				
				//Deposit Type Selector
				item {
					Text(
						modifier = Modifier.padding(top = 16.dp).padding(horizontal = 16.dp),
						text = "Jenis Setoran",
						fontSize = 12.sp,
						color = Color.Black,
						fontWeight = FontWeight.Bold
					)
					
					Box(
						modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 8.dp)
							.background(if(id == 0) Color.White else Colors().slate20, RoundedCornerShape(4.dp))
							.border(1.dp, Colors().textDarkGrey, RoundedCornerShape(4.dp))
							.clickable(id == 0, onClick = {
								showDepositTypePopup = true
							})
					) {
						Row (
							modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
							horizontalArrangement = Arrangement.SpaceBetween,
							verticalAlignment = Alignment.CenterVertically
						){
							Text(
								text = selectedDepositType.ifBlank { "Pilih Jenis Setoran" },
								fontSize = 14.sp,
								color = if(selectedDepositType == "" || id != 0) Colors().textDarkGrey else Colors().textBlack
							)
							Image(painterResource(Res.drawable.Icon_Dropdown_Arrow), null, modifier = Modifier.size(24.dp))
						}
					}
				}
				
				//SSP Date
				item {
					Text(
						modifier = Modifier.padding(horizontal = 16.dp).padding(top = 16.dp),
						text = "Tanggal Surat Setoran Pajak",
						fontSize = 12.sp,
						color = Color.Black,
						fontWeight = FontWeight.Bold
					)
					Box(
						modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 8.dp)
//							.background(Colors().slate20, RoundedCornerShape(4.dp))
							.border(1.dp, Colors().textDarkGrey, RoundedCornerShape(4.dp))
							.clickable(true, onClick = {
								showSSPDatePopup = true
							})
					) {
						Row (
							modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
							horizontalArrangement = Arrangement.SpaceBetween,
							verticalAlignment = Alignment.CenterVertically
						){
							Text(
								text = if(sspDate == "") "Pilih tanggal mulai sewa" else DateFormatter(sspDate),
								fontSize = 14.sp,
								color = if(sspDate == "") Colors().textDarkGrey else Colors().textBlack
							)
							Image(painterResource(Res.drawable.icon_calendar), null, modifier = Modifier.size(24.dp))
						}
					}
				}
				
				//SSP Amount
				item {
					Text(
						modifier = Modifier.padding(top = 16.dp).padding(horizontal = 16.dp),
						text = "Nilai SSP",
						fontSize = 12.sp,
						color = Color.Black,
						fontWeight = FontWeight.Bold
					)
					TextField(
						enabled = true,
						modifier = Modifier.fillMaxWidth().padding(top = 8.dp).padding(horizontal = 16.dp)
							.border(
								border = BorderStroke(1.dp, Colors().textDarkGrey),
								shape = RoundedCornerShape(4.dp)
							),
						value = sspAmountIDR,
						onValueChange = {
							sspAmountIDR = it
							sspAmountIDRActual = sspAmountIDR.replace("R", "").replace("p", "").replace(" ", "").replace(",", "").toLongOrNull() ?: 0L
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
			
				//NTPN
				item {
					Text(
						modifier = Modifier.padding(horizontal = 16.dp).padding(top = 16.dp),
						text = "Nomor Transaksi Penerimaan Negara",
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
						value = ntpn,
						onValueChange = {
							if(it.length <= 16) { ntpn = it }
						},
						singleLine = true,
						colors = textFieldColors(
							backgroundColor = Color.White,
							focusedIndicatorColor = Color.Transparent,
							unfocusedIndicatorColor = Color.Transparent,
							disabledIndicatorColor = Colors().slate20
						),
						keyboardOptions = KeyboardOptions(
							capitalization = KeyboardCapitalization.Characters,
							keyboardType = KeyboardType.Text,
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
							val dataModel = FormTaxPaymentSlipRequestApiModel(
								Id = id,
								Tr1770HdId = sptHd!!.Id,
								DepositTypeE = depositTypeE,
								SSPDate = sspDate,
								SSPAmount = sspAmountIDRActual,
								NTPN = ntpn.ifBlank { null }
							)
							
							scope.launch {
								isReady = false
								sptManager.saveTaxPaymentSlip(scope, dataModel)
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
		//Deposit Type Popup
		popUpBox(
			showPopup = showDepositTypePopup,
			onClickOutside = { showDepositTypePopup = false },
			content = {
				Column(
					modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 12.dp)
				) {
					Text(
						text = "Jenis Setoran",
						fontSize = 12.sp,
						color = Colors().textBlack,
						fontWeight = FontWeight.Bold,
						modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
					)
					LazyColumn(modifier = Modifier.heightIn(max = 200.dp).padding(horizontal = 18.dp)) {
						DepositType.entries.forEach {
							item {
								Text(
									text = "[${it.value}] ${DepositType.fromValue(it.value)}",
									fontSize = 18.sp,
									modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
										.clickable(true, onClick = {
											selectedDepositType = DepositType.fromValue(it.value) ?: ""
											depositTypeE = it.value
											showDepositTypePopup = false
										})
								)
							}
						}
					}
				}
			}
		)
		
		//SSP Date Picker Popup
		popUpBox(
			showPopup = showSSPDatePopup,
			onClickOutside = { showSSPDatePopup = false },
			content = {
				Column {
					Box(modifier = Modifier.height(16.dp))
					WheelDatePicker(
						height = 256.dp,
						title = "Pilih Tanggal SSP",
						doneLabel = "Selesai",
						yearsRange = IntRange(LocalDate.now().year - 2, LocalDate.now().year),
						titleStyle = TextStyle(fontSize = 16.sp, color = Colors().textBlack, fontWeight = FontWeight.Bold),
						doneLabelStyle = TextStyle(fontSize = 14.sp, color = Colors().textClickable, fontWeight = FontWeight.Bold),
						showShortMonths = true,
						startDate = LocalDate.now(),
						onDoneClick = {
							sspDate = it.toString()
							showSSPDatePopup = false
						}
					)
				}
			}
		)
		
		//Delete SSP Popup
		popUpBox(
			showPopup = showDeletePopup,
			onClickOutside = { showDeletePopup = false },
			content = {
				Text(
					text = "Hapus SSP",
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
						text = "Hapus SSP",
						fontSize = 18.sp,
						color = Colors().textBlack,
						fontWeight = FontWeight.Bold
					)
					
					Text(
						modifier = Modifier.padding(vertical = 24.dp, horizontal = 16.dp),
						text = "Apakah Anda yakin ingin menghapus SSP ini?",
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
									sptManager.deleteTaxPaymentSlip(scope, id.toString())
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