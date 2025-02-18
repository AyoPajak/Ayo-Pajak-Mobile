package screens.SPT

import SPT.SPTManager
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxColors
import androidx.compose.material.CheckboxDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import ayopajakmobile.composeapp.generated.resources.Res
import ayopajakmobile.composeapp.generated.resources.icon_infocircle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import global.AccountRecordType
import global.Colors
import global.CurrRateEntryMode
import global.MaritalStatus
import global.universalUIComponents.loadingPopupBox
import global.universalUIComponents.topBar
import http.Account
import http.Interfaces
import kotlinx.coroutines.launch
import models.transaction.Form1770HdResponseApiModel
import models.transaction.FormBookKeepingRequestApiModel
import models.transaction.FormSpousePHMTIncomeRequestApiModel
import org.jetbrains.compose.resources.painterResource
import util.BigDeciToLong
import util.BigDeciToString
import util.CurrencyFormatter

class SptStepTenScreen(val sptHd: Form1770HdResponseApiModel?, val client: Account, val sptPertamaClient: Interfaces, val prefs: DataStore<Preferences>): Screen {
	
	val sptManager = SPTManager(prefs, client, sptPertamaClient)
	
	@Composable
	override fun Content() {
		
		val scope = rememberCoroutineScope()
		val navigator = LocalNavigator.currentOrThrow
		
		var maritalStatusE by remember { mutableStateOf(0) }
		
		var onlyHasFinalIncomeSelf by remember { mutableStateOf(false) }
		var onlyHasFinalIncomeSpouse by remember { mutableStateOf(false) }
		var nonFinalIncomeIDR by remember { mutableStateOf("Rp 0") }
		var nonFinalIncomeIDRActual by remember { mutableStateOf(0L) }
		var jobNettIncomeIDR by remember { mutableStateOf("Rp 0") }
		var jobNettIncomeIDRActual by remember { mutableStateOf(0L) }
		var otherNettIncomeIDR by remember { mutableStateOf("Rp 0") }
		var otherNettIncomeIDRActual by remember { mutableStateOf(0L) }
		var overseasNettIncomeIDR by remember { mutableStateOf("Rp 0") }
		var overseasNettIncomeIDRActual by remember { mutableStateOf(0L) }
		var religiousDonationIDR by remember { mutableStateOf("Rp 0") }
		var religiousDonationIDRActual by remember { mutableStateOf(0L) }
		var subTotalIDR by remember { mutableStateOf("Rp 0") }
		var subTotalIDRActual by remember { mutableStateOf(0L) }
		var lossCompIDR by remember { mutableStateOf("Rp 0") }
		var lossCompIDRActual by remember { mutableStateOf(0L) }
		var totalIDR by remember { mutableStateOf("Rp 0") }
		var totalIDRActual by remember { mutableStateOf(0L) }
		
		var isReady by remember { mutableStateOf(false) }
		
		LaunchedEffect(null) {
			maritalStatusE = sptManager.getIdentityData(scope, sptHd!!.Id)?.MaritalStatusE ?: 0
			
			val oldData = sptManager.getIncomeSpousePHMTData(scope, sptHd.Id.toString())
			
			if (oldData != null) {
				onlyHasFinalIncomeSelf = oldData.OnlyHasFinalIncomeSelf
				onlyHasFinalIncomeSpouse = oldData.OnlyHasFinalIncomeSpouse
				nonFinalIncomeIDR = "Rp ${CurrencyFormatter(BigDeciToString(oldData.NonFinalIncomeIDR.toString()))}"
				nonFinalIncomeIDRActual = BigDeciToLong(oldData.NonFinalIncomeIDR.toString())
				jobNettIncomeIDR = "Rp ${CurrencyFormatter(BigDeciToString(oldData.JobNettIncomeIDR.toString()))}"
				jobNettIncomeIDRActual = BigDeciToLong(oldData.JobNettIncomeIDR.toString())
				otherNettIncomeIDR = "Rp ${CurrencyFormatter(BigDeciToString(oldData.OtherNettIncomeIDR.toString()))}"
				otherNettIncomeIDRActual = BigDeciToLong(oldData.OtherNettIncomeIDR.toString())
				overseasNettIncomeIDR = "Rp ${CurrencyFormatter(BigDeciToString(oldData.OverseasNettIncomeIDR.toString()))}"
				overseasNettIncomeIDRActual = BigDeciToLong(oldData.OverseasNettIncomeIDR.toString())
				religiousDonationIDR = "Rp ${CurrencyFormatter(BigDeciToString(oldData.ReligiousDonationIDR.toString()))}"
				religiousDonationIDRActual = BigDeciToLong(oldData.ReligiousDonationIDR.toString())
				subTotalIDR = "Rp ${CurrencyFormatter(BigDeciToString(oldData.SubTotalA_IDR.toString()))}"
				subTotalIDRActual = BigDeciToLong(oldData.SubTotalA_IDR.toString())
				lossCompIDR = "Rp ${CurrencyFormatter(BigDeciToString(oldData.LossCompensationIDR.toString()))}"
				lossCompIDRActual = BigDeciToLong(oldData.LossCompensationIDR.toString())
				totalIDR = "Rp ${CurrencyFormatter(BigDeciToString(oldData.TotalA_IDR.toString()))}"
				totalIDRActual = BigDeciToLong(oldData.TotalA_IDR.toString())
			}
			
			println(oldData)
			
			isReady = true
		}
		
		fun calculateSubTotal() {
			subTotalIDRActual = nonFinalIncomeIDRActual + jobNettIncomeIDRActual + otherNettIncomeIDRActual + overseasNettIncomeIDRActual - religiousDonationIDRActual
			if(subTotalIDRActual < 0) subTotalIDRActual = 0L
		}
		
		fun calculateTotal() {
			totalIDRActual = subTotalIDRActual - lossCompIDRActual
			if(totalIDRActual < 0) totalIDRActual = 0L
		}
		
		fun resetAToD() {
			nonFinalIncomeIDR = "Rp 0"
			nonFinalIncomeIDRActual = 0L
			jobNettIncomeIDR = "Rp 0"
			jobNettIncomeIDRActual = 0L
			otherNettIncomeIDR = "Rp 0"
			otherNettIncomeIDRActual = 0L
			overseasNettIncomeIDR = "Rp 0"
			overseasNettIncomeIDRActual = 0L
			calculateSubTotal()
			calculateTotal()
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
					topBar("PPh Terutang (PH/MT)")
				}
				
				if(maritalStatusE == MaritalStatus.NotMarried.value) {
					item {
						Box(
							modifier = Modifier
								.fillMaxWidth()
								.padding(horizontal = 16.dp)
								.clip(RoundedCornerShape(8.dp))
								.border(1.dp, Colors().slate20, RoundedCornerShape(8.dp))
								.background(Color.White)
						) {
							Column(
								modifier = Modifier.fillMaxWidth().padding(16.dp),
							) {
								Row(
									modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
									verticalAlignment = Alignment.CenterVertically
								) {
									Image(
										painter = painterResource(Res.drawable.icon_infocircle),
										contentDescription = null,
										modifier = Modifier.padding(end = 8.dp).scale(1.2f)
									)
									Text(
										text = "Anda Tidak Perlu Mengisi Form Ini",
										fontSize = 14.sp,
										fontWeight = FontWeight.Bold,
										modifier = Modifier.fillMaxWidth()
									)
								}
								Text(
									text = "Anda tidak perlu mengisi form ini dikarenakan pada Form Pengisian Identitas, [Status Pernikahan] anda adalah [Belum Menikah / Tidak Menikah] atau [Status Perpajakan] anda bukan [Pisah Harta] & [Memilih Terpisah].\n\nForm ini hanya diisi oleh Wajib Pajak yang [Status Pernikahan]nya adalah [Menikah] dan [Status Perpajakan]nya adalah [Pisah Harta] atau [Memilih Terpisah].",
									fontSize = 12.sp,
									lineHeight = 20.sp
								)
							}
						}
					}
				}
				else {
					//Attention Box
					
					
					//Final Income Checkboxes
					item {
						Column(
							modifier = Modifier.padding(horizontal = 16.dp)
						) {
							Text(
								text = "Mohon isi sesuai dengan kondisi pendapatan Anda dan pasangan Anda.",
								fontSize = 12.sp,
								fontWeight = FontWeight.Bold,
								color = Colors().textBlack,
								lineHeight = 20.sp
							)
							Row(
								modifier = Modifier.padding(vertical = 8.dp),
								verticalAlignment = Alignment.Top
							) {
								Checkbox(
									enabled = true,
									checked = onlyHasFinalIncomeSelf,
									onCheckedChange = { onlyHasFinalIncomeSelf = !onlyHasFinalIncomeSelf },
									colors = CheckboxDefaults.colors(
										checkedColor = Colors().buttonActive,
										uncheckedColor = Colors().textLightGrey
									)
								)
								Text(
									text = "Seluruh penghasilan Anda merupakan penghasilan yang dikenakan pajak final (PTKP Anda akan bernilai 0)",
									fontSize = 14.sp,
									color = Colors().textBlack,
									modifier = Modifier.clickable(true, onClick = {
										onlyHasFinalIncomeSelf = !onlyHasFinalIncomeSelf
									})
								)
							}
							Row(
								modifier = Modifier.padding(vertical = 8.dp),
								verticalAlignment = Alignment.Top
							) {
								Checkbox(
									enabled = true,
									checked = onlyHasFinalIncomeSpouse,
									onCheckedChange = {
										onlyHasFinalIncomeSpouse = !onlyHasFinalIncomeSpouse
										resetAToD()
									},
									colors = CheckboxDefaults.colors(
										checkedColor = Colors().buttonActive,
										uncheckedColor = Colors().textLightGrey
									)
								)
								Text(
									text = "Seluruh penghasilan pasangan Anda merupakan penghasilan yang dikenakan pajak final (PTKP pasangan Anda akan bernilai 0)",
									fontSize = 14.sp,
									color = Colors().textBlack,
									modifier = Modifier.clickable(true, onClick = {
										onlyHasFinalIncomeSpouse = !onlyHasFinalIncomeSpouse
										resetAToD()
									})
								)
							}
							Text(
								text = "Jika terdapat data penghasilan tidak final, maka centangan diatas tidak berlaku.",
								fontSize = 12.sp,
								fontWeight = FontWeight.SemiBold,
								lineHeight = 20.sp,
								color = Color(0xFF3565FC),
								modifier = Modifier.padding(top = 8.dp, bottom = 36.dp)
							)
						}
					}
					
					//Non Final Income
					item {
						Text(
							modifier = Modifier.padding(horizontal = 16.dp),
							text = "Penghasilan Neto Dalam Negeri dari Usaha dan/atau Pekerjaan Bebas",
							fontSize = 12.sp,
							color = Color.Black,
							fontWeight = FontWeight.Bold
						)
						TextField(
							enabled = !onlyHasFinalIncomeSpouse,
							modifier = Modifier.fillMaxWidth().padding(top = 8.dp).padding(horizontal = 16.dp)
								.border(
									border = BorderStroke(1.dp, Colors().textDarkGrey),
									shape = RoundedCornerShape(4.dp)
								),
							value = nonFinalIncomeIDR,
							onValueChange = {
								nonFinalIncomeIDR = it
								nonFinalIncomeIDRActual = nonFinalIncomeIDR.replace("R", "").replace("p", "").replace(" ", "").replace(",", "").toLongOrNull() ?: 0L
								calculateSubTotal()
							},
							singleLine = true,
							colors = textFieldColors(
								backgroundColor = if(!onlyHasFinalIncomeSpouse)Color.White else Colors().slate20,
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
					
					//Job Nett Income
					item {
						Text(
							modifier = Modifier.padding(top = 16.dp).padding(horizontal = 16.dp),
							text = "Penghasilan Neto Dalam Negeri Sehubungan Dengan Pekerjaan",
							fontSize = 12.sp,
							color = Color.Black,
							fontWeight = FontWeight.Bold
						)
						TextField(
							enabled = !onlyHasFinalIncomeSpouse,
							modifier = Modifier.fillMaxWidth().padding(top = 8.dp).padding(horizontal = 16.dp)
								.border(
									border = BorderStroke(1.dp, Colors().textDarkGrey),
									shape = RoundedCornerShape(4.dp)
								),
							value = jobNettIncomeIDR,
							onValueChange = {
								jobNettIncomeIDR = it
								jobNettIncomeIDRActual = jobNettIncomeIDR.replace("R", "").replace("p", "").replace(" ", "").replace(",", "").toLongOrNull() ?: 0L
								calculateSubTotal()
							},
							singleLine = true,
							colors = textFieldColors(
								backgroundColor = if(!onlyHasFinalIncomeSpouse)Color.White else Colors().slate20,
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
					
					//Other Nett Income
					item {
						Text(
							modifier = Modifier.padding(top = 16.dp).padding(horizontal = 16.dp),
							text = "Penghasilan Neto dalam Negeri Lainnya",
							fontSize = 12.sp,
							color = Color.Black,
							fontWeight = FontWeight.Bold
						)
						TextField(
							enabled = !onlyHasFinalIncomeSpouse,
							modifier = Modifier.fillMaxWidth().padding(top = 8.dp).padding(horizontal = 16.dp)
								.border(
									border = BorderStroke(1.dp, Colors().textDarkGrey),
									shape = RoundedCornerShape(4.dp)
								),
							value = otherNettIncomeIDR,
							onValueChange = {
								otherNettIncomeIDR = it
								otherNettIncomeIDRActual = otherNettIncomeIDR.replace("R", "").replace("p", "").replace(" ", "").replace(",", "").toLongOrNull() ?: 0L
								calculateSubTotal()
							},
							singleLine = true,
							colors = textFieldColors(
								backgroundColor = if(!onlyHasFinalIncomeSpouse)Color.White else Colors().slate20,
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
					
					//Overseas Nett Income
					item {
						Text(
							modifier = Modifier.padding(top = 16.dp).padding(horizontal = 16.dp),
							text = "Penghasilan Neto Luar Negeri",
							fontSize = 12.sp,
							color = Color.Black,
							fontWeight = FontWeight.Bold
						)
						TextField(
							enabled = !onlyHasFinalIncomeSpouse,
							modifier = Modifier.fillMaxWidth().padding(top = 8.dp).padding(horizontal = 16.dp)
								.border(
									border = BorderStroke(1.dp, Colors().textDarkGrey),
									shape = RoundedCornerShape(4.dp)
								),
							value = overseasNettIncomeIDR,
							onValueChange = {
								overseasNettIncomeIDR = it
								overseasNettIncomeIDRActual = overseasNettIncomeIDR.replace("R", "").replace("p", "").replace(" ", "").replace(",", "").toLongOrNull() ?: 0L
								calculateSubTotal()
							},
							singleLine = true,
							colors = textFieldColors(
								backgroundColor = if(!onlyHasFinalIncomeSpouse)Color.White else Colors().slate20,
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
					
					//Religious Donation
					item {
						Text(
							modifier = Modifier.padding(top = 16.dp).padding(horizontal = 16.dp),
							text = "Zakat / Sumbangan Keagamaan Yang Bersifat Wajib",
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
							value = religiousDonationIDR,
							onValueChange = {
								religiousDonationIDR = it
								religiousDonationIDRActual = religiousDonationIDR.replace("R", "").replace("p", "").replace(" ", "").replace(",", "").toLongOrNull() ?: 0L
								calculateSubTotal()
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
					
					//Subtotal
					item {
						Text(
							modifier = Modifier.padding(top = 16.dp).padding(horizontal = 16.dp),
							text = "Jumlah",
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
							value = "Rp ${CurrencyFormatter(subTotalIDRActual.toString())}",
							onValueChange = {
							
							},
							singleLine = true,
							colors = textFieldColors(
								backgroundColor = Colors().slate20,
								disabledTextColor = Color.Black,
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
					
					//Loss Comp
					item {
						Text(
							modifier = Modifier.padding(top = 16.dp).padding(horizontal = 16.dp),
							text = "Kompensasi Kerugian",
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
							value = lossCompIDR,
							onValueChange = {
								lossCompIDR = it
								lossCompIDRActual = lossCompIDR.replace("R", "").replace("p", "").replace(" ", "").replace(",", "").toLongOrNull() ?: 0L
								calculateTotal()
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
					
					//Total
					item {
						Text(
							modifier = Modifier.padding(top = 16.dp).padding(horizontal = 16.dp),
							text = "Jumlah Penghasilan Neto",
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
							value = "Rp ${CurrencyFormatter(totalIDRActual.toString())}",
							onValueChange = {
							
							},
							singleLine = true,
							colors = textFieldColors(
								backgroundColor = Colors().slate20,
								disabledTextColor = Color.Black,
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
							enabled = true,
							modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 16.dp),
							colors = buttonColors(backgroundColor = Colors().buttonActive, contentColor = Color.White),
							onClick = {
								val dataModel = FormSpousePHMTIncomeRequestApiModel(
									Tr1770HdId = sptHd!!.Id,
									OnlyHasFinalIncomeSelf = onlyHasFinalIncomeSelf,
									OnlyHasFinalIncomeSpouse = onlyHasFinalIncomeSpouse,
									NonFinalIncomeIDR = nonFinalIncomeIDRActual,
									JobNettIncomeIDR = jobNettIncomeIDRActual,
									OtherNettIncomeIDR = otherNettIncomeIDRActual,
									OverseasNettIncomeIDR = overseasNettIncomeIDRActual,
									ReligiousDonationIDR = religiousDonationIDRActual,
									LossCompensationIDR = lossCompIDRActual
								)
								
								scope.launch {
									isReady = false
									sptManager.saveIncomeSpousePHMT(scope, dataModel)
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
		}
	}
}