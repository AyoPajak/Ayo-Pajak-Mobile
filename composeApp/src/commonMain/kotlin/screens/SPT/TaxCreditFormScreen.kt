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
import global.Colors
import global.NonTaxedIncomeType
import global.TaxType
import global.universalUIComponents.loadingPopupBox
import global.universalUIComponents.popUpBox
import http.Account
import http.Interfaces
import kotlinx.coroutines.launch
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import models.transaction.BrutoCirculationRequestApiModel
import models.transaction.Form1770HdResponseApiModel
import models.transaction.FormFinalIncomeARequestApiModel
import models.transaction.FormFinalIncomeBRequestApiModel
import models.transaction.FormFinalIncomeCRequestApiModel
import models.transaction.FormFinalIncomeDRequestApiModel
import models.transaction.FormFinalIncomeERequestApiModel
import models.transaction.FormIncomeNetJobResponseApiModel
import models.transaction.FormNetOtherIncomeResponseApiModel
import models.transaction.FormTaxCreditARequestApiModel
import models.transaction.FormTaxCreditBRequestApiModel
import network.chaintech.kmp_date_time_picker.ui.datepicker.WheelDatePickerComponent.WheelDatePicker
import network.chaintech.kmp_date_time_picker.utils.now
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource
import util.BigDeciToLong
import util.BigDeciToString
import util.CurrencyFormatter
import util.DateFormatter

class TaxCreditFormScreen(val id: Int, val sptHd: Form1770HdResponseApiModel?, val client: Account, val sptPertamaClient: Interfaces, val prefs: DataStore<Preferences>): Screen {
	
	val sptManager = SPTManager(prefs, client, sptPertamaClient)
	
	@Composable
	override fun Content() {
		
		val scope = rememberCoroutineScope()
		val navigator = LocalNavigator.currentOrThrow
		
		var showDeletePopup by remember { mutableStateOf(false) }
		var showConfirmPopup by remember { mutableStateOf(false) }
		var showTaxTypePopup by remember { mutableStateOf(false) }
		var showWithholdingDatePopup by remember { mutableStateOf(false) }
		var showNetOtherPopup by remember { mutableStateOf(false) }
		
		var selectedTaxType by remember { mutableStateOf("") }
		var taxTypeE by remember { mutableStateOf(0) }
		var withholderNpwp by remember { mutableStateOf("") }
		var withholderName by remember { mutableStateOf("") }
		var withholdingTaxNo by remember { mutableStateOf("") }
		var withholdingDate by remember { mutableStateOf("") }
		var withholdingTaxAmountIDR by remember { mutableStateOf("") }
		var withholdingTaxAmountIDRActual by remember { mutableStateOf(0L) }
		var taxCreditIDR by remember { mutableStateOf("") }
		var taxCreditIDRActual by remember { mutableStateOf(0L) }
		
		var netOtherList by remember { mutableStateOf<List<FormNetOtherIncomeResponseApiModel>?>(null) }
		
		var netJob by remember { mutableStateOf<FormIncomeNetJobResponseApiModel?>(null) }
		var netOther by remember { mutableStateOf<FormNetOtherIncomeResponseApiModel?>(null) }
		
		var selectedIncomeNetOther by remember { mutableStateOf("") }
		var incomeNetOtherId by remember { mutableStateOf(0) }
		
		var isReady by remember { mutableStateOf(false) }
		
		LaunchedEffect(null) {
			netOtherList = sptManager.getIncomeNetOtherData(scope, sptHd!!.Id.toString(), true)
			println(netOtherList)
			
			if(id != 0) {
				val oldData = sptManager.getTaxCreditDataById(scope, id.toString())
				
				if (oldData != null) {
					selectedTaxType = TaxType.fromValue(oldData.TaxTypeE).toString()
					taxTypeE = oldData.TaxTypeE
					withholderNpwp = oldData.WithholderNPWP
					withholderName = oldData.WithholderName
					withholdingTaxNo = oldData.WithholdingTaxNo
					withholdingDate = oldData.WithholdingDate
					withholdingTaxAmountIDRActual = BigDeciToLong(oldData.WithholdingTaxAmountIDR.toString())
					taxCreditIDRActual = BigDeciToLong(oldData.TaxCreditIDR.toString())
					netJob = oldData.NetJob
					netOther = oldData.NetOther
					
					selectedIncomeNetOther = "${netOther?.OverseasIncomeType ?: "-"}, ${netOther?.EmployerNameAddr ?: "-"} Rp ${CurrencyFormatter(BigDeciToString(netOther?.NettIncomeIDR.toString()))}"
					
					withholdingTaxAmountIDR = "Rp ${CurrencyFormatter(withholdingTaxAmountIDRActual.toString())}"
					taxCreditIDR = "Rp ${CurrencyFormatter(taxCreditIDRActual.toString())}"
				}
			}
			
			isReady = true
		}
		
		loadingPopupBox(!isReady)
		
		@Composable
		fun taxTypeSelector() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp),
				text = "Jenis Pajak",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			
			Box(
				modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
					.background(if(id == 0) Color.White else Colors().slate20, RoundedCornerShape(4.dp))
					.border(1.dp, Colors().textDarkGrey, RoundedCornerShape(4.dp))
					.clickable(id == 0, onClick = {
						showTaxTypePopup = true
					})
			) {
				Row (
					modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
					horizontalArrangement = Arrangement.SpaceBetween,
					verticalAlignment = Alignment.CenterVertically
				){
					Text(
						text = selectedTaxType.ifBlank { "Pilih Jenis Penghasilan" },
						fontSize = 14.sp,
						color = if(selectedTaxType == "" || id != 0) Colors().textDarkGrey else Colors().textBlack
					)
					Image(painterResource(Res.drawable.Icon_Dropdown_Arrow), null, modifier = Modifier.size(24.dp))
				}
			}
		}
		
		@Composable
		fun withholderNPWPTextField() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "NPWP Pemotong / Pemungut Pajak",
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
				value = withholderNpwp,
				onValueChange = {
					if(withholderNpwp.length <= 16){ withholderNpwp = it }
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
		fun withholderNameTextField() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Nama Pemotong / Pemungut Pajak",
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
				value = withholderName,
				onValueChange = {
					if(withholderName.length <= 50){ withholderName = it }
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
		fun withholdingTaxNoTextField() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Nomor Bukti Potong",
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
				value = withholdingTaxNo,
				onValueChange = {
					if(withholdingTaxNo.length <= 50){ withholdingTaxNo = it }
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
		fun withholdingDatePicker() {
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
						showWithholdingDatePopup = true
					})
			) {
				Row (
					modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
					horizontalArrangement = Arrangement.SpaceBetween,
					verticalAlignment = Alignment.CenterVertically
				){
					Text(
						text = if(withholdingDate == "") "Pilih tanggal bukti potong" else DateFormatter(withholdingDate),
						fontSize = 14.sp,
						color = if(withholdingDate == "") Colors().textDarkGrey else Colors().textBlack
					)
					Image(painterResource(Res.drawable.icon_calendar), null, modifier = Modifier.size(24.dp))
				}
			}
		}
		
		@Composable
		fun withholdingTaxAmountTextField() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Jumlah PPh yang Dipotong",
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
				value = withholdingTaxAmountIDR,
				onValueChange = {
					withholdingTaxAmountIDR = it
					withholdingTaxAmountIDRActual = withholdingTaxAmountIDR.replace("Rp", "").replace(" ", "").replace(",", "").toLongOrNull() ?: 0L
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
		fun incomeNetOtherSelector() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
				text = "Penghasilan Neto (Lainnya)",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			
			Box(
				modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
					.background(Color.White)
					.border(1.dp, Colors().textDarkGrey, RoundedCornerShape(4.dp))
					.clickable(true, onClick = {
						showNetOtherPopup = true
					})
			) {
				Row (
					modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
					horizontalArrangement = Arrangement.SpaceBetween,
					verticalAlignment = Alignment.CenterVertically
				){
					Text(
						text = selectedIncomeNetOther.ifBlank { "Pilih Penghasilan Neto (Lainnya)" },
						fontSize = 14.sp,
						color = if(selectedIncomeNetOther == "") Colors().textDarkGrey else Colors().textBlack
					)
					Image(painterResource(Res.drawable.Icon_Dropdown_Arrow), null, modifier = Modifier.size(24.dp))
				}
			}
		}
		
		fun checkValidity(): Boolean {
			return if(taxTypeE == TaxType.Pasal24.value) {
				taxTypeE != 0 &&
						selectedIncomeNetOther.isNotBlank() &&
						withholderNpwp.isNotBlank() &&
						withholderName.isNotBlank() &&
						withholdingTaxNo.isNotBlank() &&
						withholdingDate.isNotBlank() &&
						withholdingTaxAmountIDRActual != 0L
			} else {
				withholderNpwp.isNotBlank() &&
						withholderName.isNotBlank() &&
						withholdingTaxNo.isNotBlank() &&
						withholdingDate.isNotBlank() &&
						withholdingTaxAmountIDRActual != 0L
			}
		}
		
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
								text = if (id == 0) "Tambah Kredit" else "Edit Kredit",
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
				
				//Tax Type
				item { taxTypeSelector() }
				
				if(taxTypeE == TaxType.Pasal24.value) {
					//Income Net Other
					item { incomeNetOtherSelector() }
				}
				
				//Withholder NPWP
				item { withholderNPWPTextField() }
				
				//Withholder Name
				item { withholderNameTextField() }
				
				//Withholding Tax Number
				item { withholdingTaxNoTextField() }
				
				//Withholding Date
				item {
					withholdingDatePicker()
					Text(
						text = "Diisi dengan tanggal SKPPKP",
						fontSize = 12.sp,
						color = Colors().textDarkGrey,
						modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(bottom = 8.dp)
					)
				}
				
				//Withholding Tax Amount
				item {
					withholdingTaxAmountTextField()
					Text(
						text = "Diisi dengan jumlah kelebihan pembayaran pajak yang tercantum dalam SKPPKP (dalam nilai negatif).",
						fontSize = 12.sp,
						color = Colors().textDarkGrey,
						modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(bottom = 8.dp)
					)
				}
				
				//Submit Button
				item {
					Button(
						enabled = checkValidity(),
						modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 22.dp),
						colors = buttonColors(backgroundColor = Colors().buttonActive, contentColor = Color.White),
						onClick = {
							when(taxTypeE) {
								7 -> {
									val dataModel = FormTaxCreditBRequestApiModel(
										Id = id,
										Tr1770HdId = sptHd!!.Id,
										Tr1770IncomeNetOtherId = incomeNetOtherId,
										WithholderNPWP = withholderNpwp,
										WithholderName = withholderName,
										WithholdingTaxNo = withholdingTaxNo,
										WithholdingDate = withholdingDate,
										WithholdingTaxAmountIDR = withholdingTaxAmountIDRActual
									)
									
									scope.launch {
										isReady = false
										sptManager.saveTaxCreditB(scope, dataModel)
										navigator.pop()
									}
								}
								1, 3, 5, 9, 99 -> {
									val dataModel = FormTaxCreditARequestApiModel(
										Id = id,
										Tr1770HdId = sptHd!!.Id,
										TaxTypeE = taxTypeE,
										Tr1770IncomeNetJobId = null,
										WithholderNPWP = withholderNpwp,
										WithholderName = withholderName,
										WithholdingTaxNo = withholdingTaxNo,
										WithholdingDate = withholdingDate,
										WithholdingTaxAmountIDR = withholdingTaxAmountIDRActual
									)
									
									scope.launch {
										isReady = false
										sptManager.saveTaxCreditA(scope, dataModel)
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
		//Tax Type Popup
		popUpBox(
			showPopup = showTaxTypePopup,
			onClickOutside = { showTaxTypePopup = false },
			content = {
				Column(
					modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 12.dp)
				) {
					Text(
						text = "Jenis Pajak",
						fontSize = 12.sp,
						color = Colors().textBlack,
						fontWeight = FontWeight.Bold,
						modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
					)
					LazyColumn(modifier = Modifier.heightIn(max = 200.dp).padding(horizontal = 18.dp)) {
						TaxType.entries.forEach {
							item {
								Text(
									text = TaxType.fromValue(it.value).toString(),
									fontSize = 18.sp,
									modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
										.clickable(true, onClick = {
											selectedTaxType = TaxType.fromValue(it.value).toString()
											taxTypeE = it.value
											showTaxTypePopup = false
										})
								)
							}
						}
					}
				}
			}
		)
		
		//Tax Type Popup
		popUpBox(
			showPopup = showNetOtherPopup,
			onClickOutside = { showNetOtherPopup = false },
			content = {
				Column(
					modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 12.dp)
				) {
					Text(
						text = "Penghasilan Neto (Lainnya)",
						fontSize = 12.sp,
						color = Colors().textBlack,
						fontWeight = FontWeight.Bold,
						modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
					)
					LazyColumn(modifier = Modifier.heightIn(max = 200.dp).padding(horizontal = 18.dp)) {
						netOtherList?.forEach {
							item {
								Text(
									text = "${it.OverseasIncomeType ?: "-"}, ${it.EmployerNameAddr ?: "-"} Rp ${CurrencyFormatter(BigDeciToString(it.NettIncomeIDR.toString()))}",
									fontSize = 18.sp,
									modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
										.clickable(true, onClick = {
											selectedTaxType = "${it.OverseasIncomeType ?: "-"}, ${it.EmployerNameAddr ?: "-"} Rp ${CurrencyFormatter(BigDeciToString(it.NettIncomeIDR.toString()))}"
											incomeNetOtherId = it.Id
											showNetOtherPopup = false
										})
								)
							}
						}
					}
				}
			}
		)
		
		//Withholding Date Picker Popup
		popUpBox(
			showPopup = showWithholdingDatePopup,
			onClickOutside = { showWithholdingDatePopup = false },
			content = {
				Column {
					Box(modifier = Modifier.height(16.dp))
					WheelDatePicker(
						height = 256.dp,
						title = "Pilih Tanggal Bukti Potong",
						doneLabel = "Selesai",
						yearsRange = IntRange(1920, (sptHd?.TaxYear?.toInt() ?: LocalDate.now().year) - 1),
						titleStyle = TextStyle(fontSize = 16.sp, color = Colors().textBlack, fontWeight = FontWeight.Bold),
						doneLabelStyle = TextStyle(fontSize = 14.sp, color = Colors().textClickable, fontWeight = FontWeight.Bold),
						showShortMonths = true,
						startDate = LocalDate.now().minus(DatePeriod(1,0,0)),
						onDoneClick = {
							withholdingDate = it.toString()
							showWithholdingDatePopup = false
						}
					)
				}
			}
		)
		
		//Delete Tax Credit Popup
		popUpBox(
			showPopup = showDeletePopup,
			onClickOutside = { showDeletePopup = false },
			content = {
				Text(
					text = "Hapus Kredit Pajak",
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
						text = "Hapus Kredit Pajak",
						fontSize = 18.sp,
						color = Colors().textBlack,
						fontWeight = FontWeight.Bold
					)
					
					Text(
						modifier = Modifier.padding(vertical = 24.dp, horizontal = 16.dp),
						text = "Apakah Anda yakin ingin menghapus Kredit Pajak ini?",
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
									sptManager.deleteTaxCredit(scope, id.toString())
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