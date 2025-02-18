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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults.textFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import ayopajakmobile.composeapp.generated.resources.Icon_Dropdown_Arrow
import ayopajakmobile.composeapp.generated.resources.Res
import ayopajakmobile.composeapp.generated.resources.icon_calendar
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import global.Art25InstallmentBase
import global.Authority
import global.Colors
import global.DepositType
import global.MaritalStatus
import global.OverpaidRequest
import global.PreferencesKey.Companion.NPWP
import global.PreferencesKey.Companion.WPName
import global.TaxPaymentState
import global.TaxStatus
import global.universalUIComponents.loadingPopupBox
import global.universalUIComponents.popUpBox
import global.universalUIComponents.topBar
import http.Account
import http.Interfaces
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import models.transaction.Form1770HdResponseApiModel
import models.transaction.FormAdditionalRequestApiModel
import models.transaction.FormTaxPaymentSlipRequestApiModel
import network.chaintech.kmp_date_time_picker.ui.datepicker.WheelDatePickerComponent.WheelDatePicker
import network.chaintech.kmp_date_time_picker.utils.now
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource
import screens.divider
import util.BigDeciToLong
import util.BigDeciToString
import util.CurrencyFormatter
import util.DateFormatter

class SptStepThirteenScreen(val sptHd: Form1770HdResponseApiModel?, val client: Account, val sptPertamaClient: Interfaces, val prefs: DataStore<Preferences>): Screen {
	
	val sptManager = SPTManager(prefs, client, sptPertamaClient)
	
	@Composable
	override fun Content() {
		
		val scope = rememberCoroutineScope()
		val navigator = LocalNavigator.currentOrThrow
		
		var showTaxPaidDatePopup by remember { mutableStateOf(false) }
		var showOverPaidRequestPopup by remember { mutableStateOf(false) }
		var showArt25InstallmentPopup by remember { mutableStateOf(false) }
		var showReportDatePopup by remember { mutableStateOf(false) }
		
		var taxPaymentStateE by remember { mutableStateOf(0) }
		var taxAmount by remember { mutableStateOf(0.0) }
		var sspAmount by remember { mutableStateOf(0.0) }
		var taxPaidDate by remember { mutableStateOf("") }
		var overPaidRequestE by remember { mutableStateOf(0) }
		var selectedOverPaid by remember { mutableStateOf("") }
		var art25InstallmentBaseE by remember { mutableStateOf(0) }
		var selectedInstallment by remember { mutableStateOf("") }
		var art25InstallmentIDR by remember { mutableStateOf("Rp 0") }
		var art25InstallmentIDRActual by remember { mutableStateOf(0L) }
		var reportDate by remember { mutableStateOf("") }
		var authorityE by remember { mutableStateOf(1) }
		var reporterName by remember { mutableStateOf("") }
		var reporterNPWP by remember { mutableStateOf("") }
		var inclLampA by remember { mutableStateOf(false) }
		var inclLampB by remember { mutableStateOf(false) }
		var inclLampC by remember { mutableStateOf(false) }
		var inclLampD by remember { mutableStateOf(false) }
		var inclLampE by remember { mutableStateOf(false) }
		var inclLampF by remember { mutableStateOf(false) }
		var inclLampG by remember { mutableStateOf(false) }
		var inclLampH by remember { mutableStateOf(false) }
		var inclLampI by remember { mutableStateOf(false) }
		var inclLampJ by remember { mutableStateOf(false) }
		var inclLampK by remember { mutableStateOf(false) }
		var inclLampL by remember { mutableStateOf(false) }
		
		var lampFCount by remember { mutableStateOf(0) }
		
		var lampHName by remember { mutableStateOf("") }
		var lampLName by remember { mutableStateOf("") }
		
		var isReady by remember { mutableStateOf(false) }
		
		val defaultReporterName by prefs
		.data
		.map {
				it[stringPreferencesKey(WPName)] ?: ""
		}.collectAsState(reporterName)
		
		val defaultReporterNPWP by prefs
			.data
			.map {
				it[stringPreferencesKey(NPWP)] ?: ""
			}.collectAsState(reporterNPWP)
		
		LaunchedEffect(null) {
			
			val data = sptManager.getAdditional(scope, sptHd!!.Id.toString())
			
			taxPaymentStateE = data?.FinalTaxPaymentStateE ?: 0
			taxAmount = data?.TaxAmount ?: 0.0
			sspAmount = data?.SSPAmount ?: 0.0
			taxPaidDate = data?.TaxPaidDate ?: ""
			overPaidRequestE = data?.OverpaidRequestE ?: 0
			selectedOverPaid = OverpaidRequest.fromValue(overPaidRequestE)
			art25InstallmentBaseE = data?.Art25InstallmentBaseE ?: 0
			selectedInstallment = Art25InstallmentBase.fromValue(art25InstallmentBaseE)
			art25InstallmentIDR = "Rp ${CurrencyFormatter(BigDeciToString(data?.Art25Installment_IDR.toString()))}"
			art25InstallmentIDRActual = BigDeciToLong(data?.Art25Installment_IDR.toString())
			reportDate = data?.ReportDate ?: ""
			authorityE = data?.AuthorityE ?: 0
			reporterName = data?.ReporterName ?: ""
			reporterNPWP = data?.ReporterNPWP ?: ""
			inclLampA = data?.InclLampA ?: false
			inclLampB = data?.InclLampB ?: false
			inclLampC = data?.InclLampC ?: false
			inclLampD = data?.InclLampD ?: false
			inclLampE = data?.InclLampE ?: false
			inclLampF = data?.InclLampF ?: false
			inclLampG = data?.InclLampG ?: false
			inclLampH = data?.InclLampH ?: false
			inclLampI = data?.InclLampI ?: false
			inclLampJ = data?.InclLampJ ?: false
			inclLampK = data?.InclLampK ?: false
			inclLampL = data?.InclLampL ?: false
			
			lampFCount = data?.LampFCount ?: 0
			
			lampHName = data?.LampHName ?: ""
			lampLName = data?.LampLName ?: ""
			
			println(data)
			
			isReady = true
		}
		
		fun checkValidity(): Boolean {
			if(overPaidRequestE == 0) return false
			if(art25InstallmentIDRActual <= 0) return false
			if(reportDate.takeLast(4).toInt() <= sptHd!!.TaxYear.toInt()) return false
			if(reporterName.length < 4) return false
			if(authorityE == 3 && reporterNPWP == defaultReporterNPWP) return false
			if(inclLampH && lampHName.length < 4) return false
			if(inclLampL && lampLName.length < 4) return false
			
			return true
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
					topBar("Data Pelengkap")
				}
				
				//Summary Box
				item {
					Box(
						modifier = Modifier
							.fillMaxWidth()
							.padding(horizontal = 16.dp)
							.padding(bottom = 8.dp)
							.clip(RoundedCornerShape(8.dp))
							.border(1.dp, Colors().slate20, RoundedCornerShape(8.dp))
							.background(Color.White)
					) {
						Column(
							modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 24.dp)
						) {
							Text(
								text = "Selisih Setor",
								fontSize = 16.sp,
								fontWeight = FontWeight.Bold,
								color = Colors().textDarkGrey,
								modifier = Modifier.padding(bottom = 24.dp)
							)
							
							Row(
								modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
								horizontalArrangement = Arrangement.SpaceBetween
							) {
								Text(
									text = "PPh yang Lebih Bayar",
									fontSize = 10.sp,
									color = Colors().textBlack
								)
								Text(
									text = "Rp ${CurrencyFormatter(BigDeciToString(taxAmount.toString()))}",
									fontSize = 10.sp,
									color = Colors().textBlack,
									fontWeight = FontWeight.Bold
								)
							}
							
							Row(
								modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
								horizontalArrangement = Arrangement.SpaceBetween
							) {
								Text(
									text = "Pajak yang Telah Dibayar",
									fontSize = 10.sp,
									color = Colors().textBlack
								)
								Text(
									text = "Rp ${CurrencyFormatter(BigDeciToString(sspAmount.toString()))}",
									fontSize = 10.sp,
									color = Colors().textBlack,
									fontWeight = FontWeight.Bold
								)
							}
							
							divider(0.dp)
							
							Row(
								modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
								horizontalArrangement = Arrangement.SpaceBetween
							) {
								Text(
									text = "Selisih Setor",
									fontSize = 12.sp,
									color = Colors().textClickable,
									fontWeight = FontWeight.Bold
								)
								
								Text(
									text = "Rp ${CurrencyFormatter(BigDeciToString((taxAmount + sspAmount).toString()))}",
									fontSize = 12.sp,
									color = Colors().textClickable,
									fontWeight = FontWeight.Bold
								)
							}
						}
					}
				}
				
				//Tax Paid Date
				item {
					Text(
						modifier = Modifier.padding(horizontal = 16.dp).padding(top = 16.dp),
						text = "Tanggal Pelunasan PPh Kurang Bayar (PPh Pasal 29)",
						fontSize = 12.sp,
						color = Color.Black,
						fontWeight = FontWeight.Bold
					)
					Box(
						modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 8.dp)
							.background(if(false) Color.White else Colors().slate20, RoundedCornerShape(4.dp))
							.border(1.dp, Colors().textDarkGrey, RoundedCornerShape(4.dp))
							.clickable(false, onClick = {
								showTaxPaidDatePopup = true
							})
					) {
						Row (
							modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
							horizontalArrangement = Arrangement.SpaceBetween,
							verticalAlignment = Alignment.CenterVertically
						){
							Text(
								text = if(taxPaidDate == "") "Pilih tanggal pelunasan PPh" else DateFormatter(taxPaidDate),
								fontSize = 14.sp,
								color = if(taxPaidDate == "") Colors().textDarkGrey else Colors().textBlack
							)
							Image(painterResource(Res.drawable.icon_calendar), null, modifier = Modifier.size(24.dp))
						}
					}
				}
				
				//Over Paid Request
				item {
					Text(
						modifier = Modifier.padding(top = 16.dp).padding(horizontal = 16.dp),
						text = "Permohonan Lebih Bayar",
						fontSize = 12.sp,
						color = Color.Black,
						fontWeight = FontWeight.Bold
					)
					
					Box(
						modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 8.dp)
							.background(if(taxPaymentStateE == 3) Color.White else Colors().slate20, RoundedCornerShape(4.dp))
							.border(1.dp, Colors().textDarkGrey, RoundedCornerShape(4.dp))
							.clickable(taxPaymentStateE == 3, onClick = {
								showOverPaidRequestPopup = true
							})
					) {
						Row (
							modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
							horizontalArrangement = Arrangement.SpaceBetween,
							verticalAlignment = Alignment.CenterVertically
						){
							Text(
								text = selectedOverPaid.ifBlank { "Pilih Salah Satu" },
								fontSize = 14.sp,
								color = if(selectedOverPaid == "") Colors().textDarkGrey else Colors().textBlack
							)
							Image(painterResource(Res.drawable.Icon_Dropdown_Arrow), null, modifier = Modifier.size(24.dp))
						}
					}
					
					Text(
						text = "Lebih bayar dihitung dari jumlah PPh tanpa melihat jumlah Surat Setoran Pajak",
						fontSize = 12.sp,
						color = Colors().textDarkGrey,
						modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
						lineHeight = 20.sp
					)
				}
				
				//Article 25 Installment Base
				item {
					Text(
						text = "Angsuran PPh Pasal 25 untuk Tahun Pajak Berikutnya",
						fontSize = 16.sp,
						fontWeight = FontWeight.SemiBold,
						color = Colors().textDarkGrey,
						modifier = Modifier.padding(horizontal = 16.dp).padding(top = 32.dp)
					)
					
					Text(
						modifier = Modifier.padding(top = 24.dp).padding(horizontal = 16.dp),
						text = "Jenis Perhitungan Angsuran",
						fontSize = 12.sp,
						color = Color.Black,
						fontWeight = FontWeight.Bold
					)
					
					Box(
						modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 8.dp)
							.background(if(taxPaymentStateE == 1) Color.White else Colors().slate20, RoundedCornerShape(4.dp))
							.border(1.dp, Colors().textDarkGrey, RoundedCornerShape(4.dp))
							.clickable(taxPaymentStateE == 1, onClick = {
								showArt25InstallmentPopup = true
							})
					) {
						Row (
							modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
							horizontalArrangement = Arrangement.SpaceBetween,
							verticalAlignment = Alignment.CenterVertically
						){
							Text(
								text = selectedInstallment.ifBlank { "Pilih Jenis Setoran" },
								fontSize = 14.sp,
								color = if(selectedInstallment == "") Colors().textDarkGrey else Colors().textBlack
							)
							Image(painterResource(Res.drawable.Icon_Dropdown_Arrow), null, modifier = Modifier.size(24.dp))
						}
					}
				}
				
				//Article 25 Installment Amount
				item {
					Text(
						modifier = Modifier.padding(top = 16.dp).padding(horizontal = 16.dp),
						text = "Jumlah angsuran PPh Pasal 25",
						fontSize = 12.sp,
						color = Color.Black,
						fontWeight = FontWeight.Bold
					)
					TextField(
						enabled = art25InstallmentBaseE == 3 || art25InstallmentBaseE == 5,
						modifier = Modifier.fillMaxWidth().padding(top = 8.dp).padding(horizontal = 16.dp)
							.border(
								border = BorderStroke(1.dp, Colors().textDarkGrey),
								shape = RoundedCornerShape(4.dp)
							),
						value = art25InstallmentIDR,
						onValueChange = {
							art25InstallmentIDR = it
							art25InstallmentIDRActual = art25InstallmentIDR.replace("R", "").replace("p", "").replace(" ", "").replace(",", "").toLongOrNull() ?: 0L
						},
						singleLine = true,
						colors = textFieldColors(
							backgroundColor = if(art25InstallmentBaseE == 3 || art25InstallmentBaseE == 5) Color.White else Colors().slate20,
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
				
				//Report Date
				item {
					Text(
						text = "Pelaporan SPT",
						fontSize = 16.sp,
						fontWeight = FontWeight.SemiBold,
						color = Colors().textDarkGrey,
						modifier = Modifier.padding(horizontal = 16.dp).padding(top = 32.dp)
					)
					
					Text(
						modifier = Modifier.padding(horizontal = 16.dp).padding(top = 24.dp),
						text = "Tanggal Lapor",
						fontSize = 12.sp,
						color = Color.Black,
						fontWeight = FontWeight.Bold
					)
					Box(
						modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 8.dp)
							.background(Color.White, RoundedCornerShape(4.dp))
							.border(1.dp, Colors().textDarkGrey, RoundedCornerShape(4.dp))
							.clickable(true, onClick = {
								showReportDatePopup = true
							})
					) {
						Row (
							modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
							horizontalArrangement = Arrangement.SpaceBetween,
							verticalAlignment = Alignment.CenterVertically
						){
							Text(
								text = if(reportDate == "") "Pilih tanggal lapor" else DateFormatter(reportDate),
								fontSize = 14.sp,
								color = if(reportDate == "") Colors().textDarkGrey else Colors().textBlack
							)
							Image(painterResource(Res.drawable.icon_calendar), null, modifier = Modifier.size(24.dp))
						}
					}
				}
				
				//Authority
				item {
					Text(
						text = "Wewenang",
						fontSize = 12.sp,
						color = Color.Black,
						fontWeight = FontWeight.Bold,
						modifier = Modifier.padding(horizontal = 16.dp).padding(top = 16.dp)
					)
					Row(modifier = Modifier.selectableGroup().fillMaxWidth())
					{
						Row(modifier = Modifier.weight(.5f), verticalAlignment = Alignment.CenterVertically) {
							RadioButton(
								selected = authorityE == Authority.Self.value,
								onClick = {
									authorityE = Authority.Self.value
									reporterName = defaultReporterName
									reporterNPWP = defaultReporterNPWP
									inclLampA = false
								},
								colors = RadioButtonDefaults.colors(selectedColor = Colors().buttonActive)
							)
							Text(
								text = "Wajib Pajak",
								fontSize = 14.sp,
								color = Colors().textBlack,
								modifier = Modifier.clickable(true, onClick = {
									authorityE = Authority.Self.value
									reporterName = defaultReporterName
									reporterNPWP = defaultReporterNPWP
									inclLampA = false
								})
							)
						}
						Row(modifier = Modifier.weight(.5f), verticalAlignment = Alignment.CenterVertically) {
							RadioButton(
								selected = authorityE == Authority.Representative.value,
								onClick = {
									authorityE = Authority.Representative.value
									inclLampA = true
								},
								colors = RadioButtonDefaults.colors(selectedColor = Colors().buttonActive)
							)
							Text(
								text = "Kuasa",
								fontSize = 14.sp,
								color = Colors().textBlack,
								modifier = Modifier.clickable(true, onClick = {
									authorityE = Authority.Representative.value
									inclLampA = true
								})
							)
						}
					}
				}
				
				//Reporter Name
				item {
					Text(
						modifier = Modifier.padding(horizontal = 16.dp).padding(top = 16.dp),
						text = "Nama Pelapor SPT",
						fontSize = 12.sp,
						color = Color.Black,
						fontWeight = FontWeight.Bold
					)
					TextField(
						enabled = authorityE == Authority.Representative.value,
						modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
							.padding(horizontal = 16.dp)
							.border(
								border = BorderStroke(1.dp, Colors().textDarkGrey),
								shape = RoundedCornerShape(4.dp)
							),
						value = reporterName,
						onValueChange = {
							if(it.length <= 50) { reporterName = it }
						},
						singleLine = true,
						colors = textFieldColors(
							backgroundColor = if(authorityE == Authority.Representative.value) Color.White else Colors().slate20,
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
				
				//Reporter NPWP
				item {
					Text(
						modifier = Modifier.padding(horizontal = 16.dp).padding(top = 16.dp),
						text = "NPWP Pelapor SOP",
						fontSize = 12.sp,
						color = Color.Black,
						fontWeight = FontWeight.Bold
					)
					TextField(
						enabled = authorityE == Authority.Representative.value,
						modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
							.padding(horizontal = 16.dp)
							.border(
								border = BorderStroke(1.dp, Colors().textDarkGrey),
								shape = RoundedCornerShape(4.dp)
							),
						value = reporterNPWP,
						onValueChange = {
							if(it.length <= 16) { reporterNPWP = it }
						},
						singleLine = true,
						colors = textFieldColors(
							backgroundColor = if(authorityE == Authority.Representative.value) Color.White else Colors().slate20,
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
				
				if(sptHd!!.SPTType == "1770") {
					//Lampiran A
					item {
						Text(
							text = "Lampiran",
							fontSize = 16.sp,
							fontWeight = FontWeight.SemiBold,
							color = Colors().textDarkGrey,
							modifier = Modifier.padding(horizontal = 16.dp).padding(top = 32.dp, bottom = 16.dp)
						)
						
						Row(
							modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 8.dp).background(Colors().slate20),
							verticalAlignment = Alignment.CenterVertically
						) {
							Checkbox(
								enabled = false,
								checked = inclLampA,
								onCheckedChange = { inclLampA = !inclLampA },
								colors = CheckboxDefaults.colors(
									checkedColor = Colors().buttonActive,
									uncheckedColor = Colors().textLightGrey
								)
							)
							Text(
								text = "a. Surat Kuasa Khusus (Bila Dikuasakan)",
								fontSize = 14.sp,
								color = Colors().textBlack,
								modifier = Modifier.clickable(false, onClick = {
									inclLampA = !inclLampA
								})
							)
						}
					}
					
					//Lampiran B
					item {
						Row(
							modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 8.dp).background(Colors().slate20),
							verticalAlignment = Alignment.CenterVertically
						) {
							Checkbox(
								enabled = false,
								checked = inclLampB,
								onCheckedChange = { inclLampB = !inclLampB },
								colors = CheckboxDefaults.colors(
									checkedColor = Colors().buttonActive,
									uncheckedColor = Colors().textLightGrey
								)
							)
							Text(
								text = "b. SPP Lembar ke-3 PPh Pasal 29",
								fontSize = 14.sp,
								color = Colors().textBlack,
								modifier = Modifier.clickable(false, onClick = {
									inclLampB = !inclLampB
								})
							)
						}
					}
					
					//Lampiran C
					item {
						Row(
							modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 8.dp).background(Colors().slate20),
							verticalAlignment = Alignment.CenterVertically
						) {
							Checkbox(
								enabled = false,
								checked = inclLampC,
								onCheckedChange = { inclLampC = !inclLampC },
								colors = CheckboxDefaults.colors(
									checkedColor = Colors().buttonActive,
									uncheckedColor = Colors().textLightGrey
								)
							)
							Text(
								text = "c. Neraca dan Lap. Laba Rugi / Rekapitulasi Bulanan Peredaran Bruto dan/atau Penghasilan Lain dan Biaya",
								fontSize = 14.sp,
								color = Colors().textBlack,
								modifier = Modifier.clickable(false, onClick = {
									inclLampC = !inclLampC
								})
							)
						}
					}
					
					//Lampiran D
					item {
						Row(
							modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 8.dp).background(Colors().slate20),
							verticalAlignment = Alignment.CenterVertically
						) {
							Checkbox(
								enabled = false,
								checked = inclLampD,
								onCheckedChange = { inclLampD = !inclLampD },
								colors = CheckboxDefaults.colors(
									checkedColor = Colors().buttonActive,
									uncheckedColor = Colors().textLightGrey
								)
							)
							Text(
								text = "d. Perhitungan Kompensasi Kerugian Fiskal",
								fontSize = 14.sp,
								color = Colors().textBlack,
								modifier = Modifier.clickable(false, onClick = {
									inclLampD = !inclLampD
								})
							)
						}
					}
					
					//Lampiran E
					item {
						Row(
							modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 8.dp).background(Colors().slate20),
							verticalAlignment = Alignment.CenterVertically
						) {
							Checkbox(
								enabled = false,
								checked = inclLampE,
								onCheckedChange = { inclLampE = !inclLampE },
								colors = CheckboxDefaults.colors(
									checkedColor = Colors().buttonActive,
									uncheckedColor = Colors().textLightGrey
								)
							)
							Text(
								text = "e. Bukti Pemotongan/Pemungutan Oleh Pihak Lain/Ditanggung Pemerintah dan yang Dibayar/Dipotong Di Luar Negeri",
								fontSize = 14.sp,
								color = Colors().textBlack,
								modifier = Modifier.clickable(false, onClick = {
									inclLampE = !inclLampE
								})
							)
						}
					}
					
					//Lampiran F
					item {
						Row(
							modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 8.dp).background(Colors().slate20),
							verticalAlignment = Alignment.CenterVertically
						) {
							Checkbox(
								enabled = false,
								checked = inclLampF,
								onCheckedChange = { inclLampF = !inclLampF },
								colors = CheckboxDefaults.colors(
									checkedColor = Colors().buttonActive,
									uncheckedColor = Colors().textLightGrey
								)
							)
							Text(
								text = "f. Fotokopi Formulir 1721-A1 dan/atau 1721-A2",
								fontSize = 14.sp,
								color = Colors().textBlack,
								modifier = Modifier.clickable(false, onClick = {
									inclLampF = !inclLampF
								})
							)
						}
						
						Box(
							modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
								.background(Colors().slate20, RoundedCornerShape(4.dp))
								.border(1.dp, Colors().textDarkGrey, RoundedCornerShape(4.dp))
						) {
							Row (
								modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
								horizontalArrangement = Arrangement.SpaceBetween,
								verticalAlignment = Alignment.CenterVertically
							){
								Text(
									text = "$lampFCount",
									fontSize = 14.sp,
									color = Colors().textDarkGrey
								)
								Text(
									text = "Lembar",
									fontSize = 14.sp,
									color = Colors().textBlack
								)
							}
						}
					}
					
					//Lampiran G
					item {
						Row(
							modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 8.dp).background(Colors().slate20),
							verticalAlignment = Alignment.CenterVertically
						) {
							Checkbox(
								enabled = false,
								checked = inclLampG,
								onCheckedChange = { inclLampG = !inclLampG },
								colors = CheckboxDefaults.colors(
									checkedColor = Colors().buttonActive,
									uncheckedColor = Colors().textLightGrey
								)
							)
							Text(
								text = "g. Petunjuk pengisian :\nPerhitungan Angsuran PPh Pasal 25 Tahun Pajak Berikutnya",
								fontSize = 14.sp,
								color = Colors().textBlack,
								modifier = Modifier.clickable(false, onClick = {
									inclLampG = !inclLampG
								})
							)
						}
					}
					
					//Lampiran H
					item {
						Row(
							modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 8.dp),
							verticalAlignment = Alignment.CenterVertically
						) {
							Checkbox(
								enabled = true,
								checked = inclLampH,
								onCheckedChange = { inclLampH = !inclLampH },
								colors = CheckboxDefaults.colors(
									checkedColor = Colors().buttonActive,
									uncheckedColor = Colors().textLightGrey
								)
							)
							Text(
								text = "h.",
								fontSize = 14.sp,
								color = Colors().textBlack,
								modifier = Modifier.clickable(true, onClick = {
									inclLampH = !inclLampH
								})
							)
						}
						
						TextField(
							enabled = inclLampH,
							modifier = Modifier.fillMaxWidth()
								.padding(horizontal = 16.dp)
								.border(
									border = BorderStroke(1.dp, Colors().textDarkGrey),
									shape = RoundedCornerShape(4.dp)
								),
							value = lampHName,
							onValueChange = {
								if(it.length <= 255) { lampHName = it }
							},
							singleLine = true,
							colors = textFieldColors(
								backgroundColor = if(inclLampH) Color.White else Colors().slate20,
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
					
					//Lampiran I
					item {
						Row(
							modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 8.dp).background(Colors().slate20),
							verticalAlignment = Alignment.CenterVertically
						) {
							Checkbox(
								enabled = false,
								checked = inclLampI,
								onCheckedChange = { inclLampI = !inclLampI },
								colors = CheckboxDefaults.colors(
									checkedColor = Colors().buttonActive,
									uncheckedColor = Colors().textLightGrey
								)
							)
							Text(
								text = "i. Petunjuk pengisian :\nPerhitungan PPh Terutang Bagi Wajib Pajak Dengan Status Perpajakan PH atau MT",
								fontSize = 14.sp,
								color = Colors().textBlack,
								modifier = Modifier.clickable(false, onClick = {
									inclLampI = !inclLampI
								})
							)
						}
					}
					
					//Lampiran J
					item {
						Row(
							modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 8.dp).background(Colors().slate20),
							verticalAlignment = Alignment.CenterVertically
						) {
							Checkbox(
								enabled = false,
								checked = inclLampJ,
								onCheckedChange = { inclLampJ = !inclLampJ },
								colors = CheckboxDefaults.colors(
									checkedColor = Colors().buttonActive,
									uncheckedColor = Colors().textLightGrey
								)
							)
							Text(
								text = "j. Daftar Jumlah Penghasilan dan Pembayaran PPh Pasal 25 (Khusus Untuk Orang Pribadi Pengusaha Tertentu)",
								fontSize = 14.sp,
								color = Colors().textBlack,
								modifier = Modifier.clickable(false, onClick = {
									inclLampJ = !inclLampJ
								})
							)
						}
					}
					
					//Lampiran K
					item {
						Row(
							modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 8.dp).background(Colors().slate20),
							verticalAlignment = Alignment.CenterVertically
						) {
							Checkbox(
								enabled = false,
								checked = inclLampK,
								onCheckedChange = { inclLampK = !inclLampK },
								colors = CheckboxDefaults.colors(
									checkedColor = Colors().buttonActive,
									uncheckedColor = Colors().textLightGrey
								)
							)
							Text(
								text = "k. Daftar Jumlah Penghasilan Bruto dan Pembayaran PPh Final Berdasarkan PP46 Tahun 2013 Per Masa Pajak",
								fontSize = 14.sp,
								color = Colors().textBlack,
								modifier = Modifier.clickable(false, onClick = {
									inclLampK = !inclLampK
								})
							)
						}
					}
					
					//Lampiran L
					item {
						Row(
							modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 8.dp),
							verticalAlignment = Alignment.CenterVertically
						) {
							Checkbox(
								enabled = true,
								checked = inclLampL,
								onCheckedChange = { inclLampL = !inclLampL },
								colors = CheckboxDefaults.colors(
									checkedColor = Colors().buttonActive,
									uncheckedColor = Colors().textLightGrey
								)
							)
							Text(
								text = "l.",
								fontSize = 14.sp,
								color = Colors().textBlack,
								modifier = Modifier.clickable(true, onClick = {
									inclLampL = !inclLampL
								})
							)
						}
						
						TextField(
							enabled = inclLampL,
							modifier = Modifier.fillMaxWidth()
								.padding(horizontal = 16.dp)
								.border(
									border = BorderStroke(1.dp, Colors().textDarkGrey),
									shape = RoundedCornerShape(4.dp)
								),
							value = lampLName,
							onValueChange = {
								if(it.length <= 255) { lampLName = it }
							},
							singleLine = true,
							colors = textFieldColors(
								backgroundColor = if(inclLampL) Color.White else Colors().slate20,
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
				}
				else {
					//Lampiran F
					item {
						Row(
							modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 8.dp).background(Colors().slate20),
							verticalAlignment = Alignment.CenterVertically
						) {
							Checkbox(
								enabled = false,
								checked = inclLampF,
								onCheckedChange = { inclLampF = !inclLampF },
								colors = CheckboxDefaults.colors(
									checkedColor = Colors().buttonActive,
									uncheckedColor = Colors().textLightGrey
								)
							)
							Text(
								text = "a. Fotokopi Formulir 1721-A1 dan/atau 1721-A2",
								fontSize = 14.sp,
								color = Colors().textBlack,
								modifier = Modifier.clickable(false, onClick = {
									inclLampF = !inclLampF
								})
							)
						}
						
						Box(
							modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
								.background(Colors().slate20, RoundedCornerShape(4.dp))
								.border(1.dp, Colors().textDarkGrey, RoundedCornerShape(4.dp))
						) {
							Row (
								modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
								horizontalArrangement = Arrangement.SpaceBetween,
								verticalAlignment = Alignment.CenterVertically
							){
								Text(
									text = "$lampFCount",
									fontSize = 14.sp,
									color = Colors().textDarkGrey
								)
								Text(
									text = "Lembar",
									fontSize = 14.sp,
									color = Colors().textBlack
								)
							}
						}
					}
					
					//Lampiran B
					item {
						Row(
							modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 8.dp).background(Colors().slate20),
							verticalAlignment = Alignment.CenterVertically
						) {
							Checkbox(
								enabled = false,
								checked = inclLampB,
								onCheckedChange = { inclLampB = !inclLampB },
								colors = CheckboxDefaults.colors(
									checkedColor = Colors().buttonActive,
									uncheckedColor = Colors().textLightGrey
								)
							)
							Text(
								text = "b. SPP Lembar ke-3 PPh Pasal 29",
								fontSize = 14.sp,
								color = Colors().textBlack,
								modifier = Modifier.clickable(false, onClick = {
									inclLampB = !inclLampB
								})
							)
						}
					}
					
					//Lampiran A
					item {
						Row(
							modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 8.dp).background(Colors().slate20),
							verticalAlignment = Alignment.CenterVertically
						) {
							Checkbox(
								enabled = false,
								checked = inclLampA,
								onCheckedChange = { inclLampA = !inclLampA },
								colors = CheckboxDefaults.colors(
									checkedColor = Colors().buttonActive,
									uncheckedColor = Colors().textLightGrey
								)
							)
							Text(
								text = "c. Surat Kuasa Khusus (Bila Dikuasakan)",
								fontSize = 14.sp,
								color = Colors().textBlack,
								modifier = Modifier.clickable(false, onClick = {
									inclLampA = !inclLampA
								})
							)
						}
					}
					
					//Lampiran I
					item {
						Row(
							modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 8.dp).background(Colors().slate20),
							verticalAlignment = Alignment.CenterVertically
						) {
							Checkbox(
								enabled = false,
								checked = inclLampI,
								onCheckedChange = { inclLampI = !inclLampI },
								colors = CheckboxDefaults.colors(
									checkedColor = Colors().buttonActive,
									uncheckedColor = Colors().textLightGrey
								)
							)
							Text(
								text = "d. Petunjuk pengisian :\nPerhitungan PPh Terutang Bagi Wajib Pajak Dengan Status Perpajakan PH atau MT",
								fontSize = 14.sp,
								color = Colors().textBlack,
								modifier = Modifier.clickable(false, onClick = {
									inclLampI = !inclLampI
								})
							)
						}
					}
					
					//Lampiran L
					item {
						Row(
							modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 8.dp),
							verticalAlignment = Alignment.CenterVertically
						) {
							Checkbox(
								enabled = true,
								checked = inclLampL,
								onCheckedChange = { inclLampL = !inclLampL },
								colors = CheckboxDefaults.colors(
									checkedColor = Colors().buttonActive,
									uncheckedColor = Colors().textLightGrey
								)
							)
							Text(
								text = "e.",
								fontSize = 14.sp,
								color = Colors().textBlack,
								modifier = Modifier.clickable(true, onClick = {
									inclLampL = !inclLampL
								})
							)
						}
						
						TextField(
							enabled = inclLampL,
							modifier = Modifier.fillMaxWidth()
								.padding(horizontal = 16.dp)
								.border(
									border = BorderStroke(1.dp, Colors().textDarkGrey),
									shape = RoundedCornerShape(4.dp)
								),
							value = lampLName,
							onValueChange = {
								if(it.length <= 255) { lampLName = it }
							},
							singleLine = true,
							colors = textFieldColors(
								backgroundColor = if(inclLampL) Color.White else Colors().slate20,
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
				}
				
				//Submit Button
				item {
					Button(
						enabled = checkValidity(),
						modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 22.dp),
						colors = buttonColors(backgroundColor = Colors().buttonActive, contentColor = Color.White),
						onClick = {
							val dataModel = FormAdditionalRequestApiModel(
								Tr1770HdId = sptHd.Id,
								TaxPaidDate = taxPaidDate.ifBlank { null },
								OverpaidRequestE = overPaidRequestE,
								Art25InstallmentBaseE = art25InstallmentBaseE,
								Art25Installment_IDR = art25InstallmentIDRActual,
								ReportDate = reportDate,
								AuthorityE = authorityE,
								ReporterName = reporterName,
								ReporterNPWP = reporterNPWP,
								InclLampH = inclLampH,
								LampHName = if(inclLampH) lampHName else null,
								InclLampL = inclLampL,
								LampLName = if(inclLampL) lampLName else null
							)
							
							scope.launch {
								isReady = false
								sptManager.saveAdditional(scope, dataModel)
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
		//Tax Paid Date Picker
		popUpBox(
			showPopup = showTaxPaidDatePopup,
			onClickOutside = { showTaxPaidDatePopup = false },
			content = {
				Column {
					Box(modifier = Modifier.height(16.dp))
					WheelDatePicker(
						height = 256.dp,
						title = "Pilih Tanggal",
						doneLabel = "Selesai",
						yearsRange = IntRange(1945, LocalDate.now().year),
						titleStyle = TextStyle(fontSize = 16.sp, color = Colors().textBlack, fontWeight = FontWeight.Bold),
						doneLabelStyle = TextStyle(fontSize = 14.sp, color = Colors().textClickable, fontWeight = FontWeight.Bold),
						showShortMonths = true,
						startDate = LocalDate.now(),
						onDoneClick = {
							taxPaidDate = it.toString()
							showTaxPaidDatePopup = false
						}
					)
				}
			}
		)
		
		//Report Date Picker
		popUpBox(
			showPopup = showReportDatePopup,
			onClickOutside = { showReportDatePopup = false },
			content = {
				Column {
					Box(modifier = Modifier.height(16.dp))
					WheelDatePicker(
						height = 256.dp,
						title = "Pilih Tanggal",
						doneLabel = "Selesai",
						yearsRange = IntRange(1945, LocalDate.now().year),
						titleStyle = TextStyle(fontSize = 16.sp, color = Colors().textBlack, fontWeight = FontWeight.Bold),
						doneLabelStyle = TextStyle(fontSize = 14.sp, color = Colors().textClickable, fontWeight = FontWeight.Bold),
						showShortMonths = true,
						startDate = LocalDate.now(),
						onDoneClick = {
							taxPaidDate = it.toString()
							showReportDatePopup = false
						}
					)
				}
			}
		)
		
		//Over Paid Request Type Popup
		popUpBox(
			showPopup = showOverPaidRequestPopup,
			onClickOutside = { showOverPaidRequestPopup = false },
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
						OverpaidRequest.entries.forEach {
							item {
								Text(
									text = OverpaidRequest.fromValue(it.value),
									fontSize = 18.sp,
									modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
										.clickable(true, onClick = {
											selectedOverPaid = OverpaidRequest.fromValue(it.value) ?: ""
											overPaidRequestE = it.value
											showOverPaidRequestPopup = false
										})
								)
							}
						}
					}
				}
			}
		)
		
		//Article 25 Installment Base Popup
		popUpBox(
			showPopup = showArt25InstallmentPopup,
			onClickOutside = { showArt25InstallmentPopup = false },
			content = {
				Column(
					modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 12.dp)
				) {
					Text(
						text = "Jenis Angsuran",
						fontSize = 12.sp,
						color = Colors().textBlack,
						fontWeight = FontWeight.Bold,
						modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
					)
					LazyColumn(modifier = Modifier.heightIn(max = 200.dp).padding(horizontal = 18.dp)) {
						Art25InstallmentBase.entries.forEach {
							item {
								Text(
									text = Art25InstallmentBase.fromValue(it.value),
									fontSize = 18.sp,
									modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
										.clickable(true, onClick = {
											selectedInstallment = Art25InstallmentBase.fromValue(it.value) ?: ""
											art25InstallmentBaseE = it.value
											showArt25InstallmentPopup = false
											if(it.value == 0 || it.value == 1){
												art25InstallmentIDR = "Rp 0"
												art25InstallmentIDRActual = 0L
											}
										})
								)
							}
						}
					}
				}
			}
		)
	}
}