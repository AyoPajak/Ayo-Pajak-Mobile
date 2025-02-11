package screens.SPT

import SPT.SPTManager
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults.buttonColors
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import global.Colors
import global.universalUIComponents.loadingPopupBox
import global.universalUIComponents.topBar
import http.Account
import http.Interfaces
import kotlinx.coroutines.launch
import models.transaction.Form1770HdResponseApiModel
import models.transaction.FormOtherDetailRequestApiModel
import models.transaction.FormSpousePHMTIncomeRequestApiModel
import screens.divider
import util.BigDeciToLong
import util.BigDeciToString
import util.CurrencyFormatter

class SptStepElevenScreen(val sptHd: Form1770HdResponseApiModel?, val client: Account, val sptPertamaClient: Interfaces, val prefs: DataStore<Preferences>): Screen {
	
	val sptManager = SPTManager(prefs, client, sptPertamaClient)
	
	@Composable
	override fun Content() {
		
		val scope = rememberCoroutineScope()
		val navigator = LocalNavigator.currentOrThrow
		
		var isReady by remember { mutableStateOf(false) }
		
		var selfLivingCostIDR by remember { mutableStateOf("Rp 0") }
		var selfLivingCostIDRActual by remember { mutableStateOf(0L) }
		var religiousDonationIDR by remember { mutableStateOf("Rp 0") }
		var religiousDonationIDRActual by remember { mutableStateOf(0L) }
		var article24DiffIDR by remember { mutableStateOf("Rp 0") }
		var article24DiffIDRActual by remember { mutableStateOf(0L) }
		var monthlyPPH25PaidIDR by remember { mutableStateOf("Rp 0") }
		var monthlyPPH25PaidIDRActual by remember { mutableStateOf(0L) }
		var stpPasal25PaidIDR by remember { mutableStateOf("Rp 0") }
		var stpPasal25PaidIDRActual by remember { mutableStateOf(0L) }
		
		LaunchedEffect(null) {
			
			val oldData = sptManager.getOtherDetail(scope, sptHd!!.Id.toString())
			
			if(oldData != null) {
				selfLivingCostIDR = "Rp ${CurrencyFormatter(BigDeciToString(oldData.SelfLivingCostIDR.toString()))}"
				selfLivingCostIDRActual = BigDeciToLong(oldData.SelfLivingCostIDR.toString())
				religiousDonationIDR = "Rp ${CurrencyFormatter(BigDeciToString(oldData.ReligiousDonationIDR.toString()))}"
				religiousDonationIDRActual = BigDeciToLong(oldData.ReligiousDonationIDR.toString())
				article24DiffIDR = "Rp ${CurrencyFormatter(BigDeciToString(oldData.Article24DiffIDR.toString()))}"
				article24DiffIDRActual = BigDeciToLong(oldData.Article24DiffIDR.toString())
				monthlyPPH25PaidIDR = "Rp ${CurrencyFormatter(BigDeciToString(oldData.MonthlyPPhPasal25Paid_IDR.toString()))}"
				monthlyPPH25PaidIDRActual = BigDeciToLong(oldData.MonthlyPPhPasal25Paid_IDR.toString())
				stpPasal25PaidIDR = "Rp ${CurrencyFormatter(BigDeciToString(oldData.STPPasal25Paid_IDR.toString()))}"
				stpPasal25PaidIDRActual = BigDeciToLong(oldData.STPPasal25Paid_IDR.toString())
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
					topBar("Detail Lainnya")
				}
				
				//Self Living Cost
				item {
					Text(
						modifier = Modifier.padding(horizontal = 16.dp),
						text = "Biaya Hidup Anda Sendiri",
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
						value = selfLivingCostIDR,
						onValueChange = {
							selfLivingCostIDR = it
							selfLivingCostIDRActual = selfLivingCostIDR.replace("R", "").replace("p", "").replace(" ", "").replace(",", "").toLongOrNull() ?: 0L
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
				
				//Article 24 Diff
				item {
					Text(
						modifier = Modifier.padding(top = 16.dp).padding(horizontal = 16.dp),
						text = "Pengembalian/Pengurangan PPh Pasal 24 Yang Telah Dikreditkan",
						fontSize = 12.sp,
						color = Color.Black,
						fontWeight = FontWeight.Bold
					)
					TextField(
						enabled = true,
						modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 24.dp).padding(horizontal = 16.dp)
							.border(
								border = BorderStroke(1.dp, Colors().textDarkGrey),
								shape = RoundedCornerShape(4.dp)
							),
						value = article24DiffIDR,
						onValueChange = {
							article24DiffIDR = it
							article24DiffIDRActual = article24DiffIDR.replace("R", "").replace("p", "").replace(" ", "").replace(",", "").toLongOrNull() ?: 0L
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
				
				//Divider
				item {
					divider(16.dp)
				}
				
				//Title
				item {
					Text(
						text = "PPh Pasal 25",
						fontSize = 16.sp,
						fontWeight = FontWeight.Bold,
						color = Colors().textDarkGrey,
						modifier = Modifier.padding(horizontal = 16.dp).padding(top = 24.dp)
					)
				}
				
				//Monthly PPh 25 Paid
				item {
					Text(
						modifier = Modifier.padding(top = 16.dp).padding(horizontal = 16.dp),
						text = "Total PPh Pasal 25 Bulanan yang dibayar sendiri",
						fontSize = 12.sp,
						color = Color.Black,
						fontWeight = FontWeight.Bold
					)
					TextField(
						enabled = true,
						modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 24.dp).padding(horizontal = 16.dp)
							.border(
								border = BorderStroke(1.dp, Colors().textDarkGrey),
								shape = RoundedCornerShape(4.dp)
							),
						value = monthlyPPH25PaidIDR,
						onValueChange = {
							monthlyPPH25PaidIDR = it
							monthlyPPH25PaidIDRActual = monthlyPPH25PaidIDR.replace("R", "").replace("p", "").replace(" ", "").replace(",", "").toLongOrNull() ?: 0L
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
				
				//STP Pasal 25 Paid
				item {
					Text(
						modifier = Modifier.padding(top = 16.dp).padding(horizontal = 16.dp),
						text = "STP PPh Pasal 25 yang dibayar sendiri ",
						fontSize = 12.sp,
						color = Color.Black,
						fontWeight = FontWeight.Bold
					)
					TextField(
						enabled = true,
						modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 24.dp).padding(horizontal = 16.dp)
							.border(
								border = BorderStroke(1.dp, Colors().textDarkGrey),
								shape = RoundedCornerShape(4.dp)
							),
						value = stpPasal25PaidIDR,
						onValueChange = {
							stpPasal25PaidIDR = it
							stpPasal25PaidIDRActual = stpPasal25PaidIDR.replace("R", "").replace("p", "").replace(" ", "").replace(",", "").toLongOrNull() ?: 0L
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
						enabled = true,
						modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 16.dp),
						colors = buttonColors(backgroundColor = Colors().buttonActive, contentColor = Color.White),
						onClick = {
							val dataModel = FormOtherDetailRequestApiModel(
								Tr1770HdId = sptHd!!.Id,
								SelfLivingCostIDR = selfLivingCostIDRActual,
								ReligiousDonationIDR = religiousDonationIDRActual,
								Article24DiffIDR = article24DiffIDRActual,
								MonthlyPPhPasal25Paid_IDR = monthlyPPH25PaidIDRActual,
								STPPasal25Paid_IDR = stpPasal25PaidIDRActual
							)
							
							scope.launch {
								isReady = false
								sptManager.saveOtherDetail(scope, dataModel)
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