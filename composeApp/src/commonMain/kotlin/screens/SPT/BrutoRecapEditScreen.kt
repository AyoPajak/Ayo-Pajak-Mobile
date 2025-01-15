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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import ayopajakmobile.composeapp.generated.resources.Res
import ayopajakmobile.composeapp.generated.resources.icon_clear
import ayopajakmobile.composeapp.generated.resources.icon_search
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import global.Colors
import global.universalUIComponents.loadingPopupBox
import global.universalUIComponents.topBar
import http.Account
import http.Interfaces
import kotlinx.coroutines.launch
import models.transaction.Form1770FinalIncomeUmkm2023BusinessRequestModel
import models.transaction.Form1770FinalIncomeUmkm2023SummaryModel
import models.transaction.Form1770FinalIncomeUmkm2023SummaryRequestModel
import org.jetbrains.compose.resources.painterResource
import screens.divider
import util.BigDeciToLong
import util.BigDeciToString
import util.CurrencyFormatter

class BrutoRecapEditScreen(val summaryData: Form1770FinalIncomeUmkm2023SummaryModel?, val client: Account, val sptPertamaClient: Interfaces, val prefs: DataStore<Preferences>): Screen {
	
	val sptManager = SPTManager(prefs, client, sptPertamaClient)
	
	@Composable
	override fun Content() {
		
		val scope = rememberCoroutineScope()
		val navigator = LocalNavigator.currentOrThrow
		
//		var diff_1 by remember { mutableStateOf("0") }
//		var diff_2 by remember { mutableStateOf("0") }
//		var diff_3 by remember { mutableStateOf("0") }
//		var diff_4 by remember { mutableStateOf("0") }
//		var diff_5 by remember { mutableStateOf("0") }
//		var diff_6 by remember { mutableStateOf("0") }
//		var diff_7 by remember { mutableStateOf("0") }
//		var diff_8 by remember { mutableStateOf("0") }
//		var diff_9 by remember { mutableStateOf("0") }
//		var diff_10 by remember { mutableStateOf("0") }
//		var diff_11 by remember { mutableStateOf("0") }
//		var diff_12 by remember { mutableStateOf("0") }
		
		var selfPaid_1 by remember { mutableStateOf("0") }
		var selfPaid_2 by remember { mutableStateOf("0") }
		var selfPaid_3 by remember { mutableStateOf("0") }
		var selfPaid_4 by remember { mutableStateOf("0") }
		var selfPaid_5 by remember { mutableStateOf("0") }
		var selfPaid_6 by remember { mutableStateOf("0") }
		var selfPaid_7 by remember { mutableStateOf("0") }
		var selfPaid_8 by remember { mutableStateOf("0") }
		var selfPaid_9 by remember { mutableStateOf("0") }
		var selfPaid_10 by remember { mutableStateOf("0") }
		var selfPaid_11 by remember { mutableStateOf("0") }
		var selfPaid_12 by remember { mutableStateOf("0") }
		
		var selfPaidActual_1 by remember { mutableStateOf(0L) }
		var selfPaidActual_2 by remember { mutableStateOf(0L) }
		var selfPaidActual_3 by remember { mutableStateOf(0L) }
		var selfPaidActual_4 by remember { mutableStateOf(0L) }
		var selfPaidActual_5 by remember { mutableStateOf(0L) }
		var selfPaidActual_6 by remember { mutableStateOf(0L) }
		var selfPaidActual_7 by remember { mutableStateOf(0L) }
		var selfPaidActual_8 by remember { mutableStateOf(0L) }
		var selfPaidActual_9 by remember { mutableStateOf(0L) }
		var selfPaidActual_10 by remember { mutableStateOf(0L) }
		var selfPaidActual_11 by remember { mutableStateOf(0L) }
		var selfPaidActual_12 by remember { mutableStateOf(0L) }
		
		var deducted_1 by remember { mutableStateOf("0") }
		var deducted_2 by remember { mutableStateOf("0") }
		var deducted_3 by remember { mutableStateOf("0") }
		var deducted_4 by remember { mutableStateOf("0") }
		var deducted_5 by remember { mutableStateOf("0") }
		var deducted_6 by remember { mutableStateOf("0") }
		var deducted_7 by remember { mutableStateOf("0") }
		var deducted_8 by remember { mutableStateOf("0") }
		var deducted_9 by remember { mutableStateOf("0") }
		var deducted_10 by remember { mutableStateOf("0") }
		var deducted_11 by remember { mutableStateOf("0") }
		var deducted_12 by remember { mutableStateOf("0") }
		
		var deductedActual_1 by remember { mutableStateOf(0L) }
		var deductedActual_2 by remember { mutableStateOf(0L) }
		var deductedActual_3 by remember { mutableStateOf(0L) }
		var deductedActual_4 by remember { mutableStateOf(0L) }
		var deductedActual_5 by remember { mutableStateOf(0L) }
		var deductedActual_6 by remember { mutableStateOf(0L) }
		var deductedActual_7 by remember { mutableStateOf(0L) }
		var deductedActual_8 by remember { mutableStateOf(0L) }
		var deductedActual_9 by remember { mutableStateOf(0L) }
		var deductedActual_10 by remember { mutableStateOf(0L) }
		var deductedActual_11 by remember { mutableStateOf(0L) }
		var deductedActual_12 by remember { mutableStateOf(0L) }
		
		var isReady by remember { mutableStateOf(false) }
		
		
		LaunchedEffect(null) {
			
			selfPaid_1 = BigDeciToString(summaryData?.PPhSelfPaid_1.toString())
			selfPaid_2 = BigDeciToString(summaryData?.PPhSelfPaid_2.toString())
			selfPaid_3 = BigDeciToString(summaryData?.PPhSelfPaid_3.toString())
			selfPaid_4 = BigDeciToString(summaryData?.PPhSelfPaid_4.toString())
			selfPaid_5 = BigDeciToString(summaryData?.PPhSelfPaid_5.toString())
			selfPaid_6 = BigDeciToString(summaryData?.PPhSelfPaid_6.toString())
			selfPaid_7 = BigDeciToString(summaryData?.PPhSelfPaid_7.toString())
			selfPaid_8 = BigDeciToString(summaryData?.PPhSelfPaid_8.toString())
			selfPaid_9 = BigDeciToString(summaryData?.PPhSelfPaid_9.toString())
			selfPaid_10 = BigDeciToString(summaryData?.PPhSelfPaid_10.toString())
			selfPaid_11 = BigDeciToString(summaryData?.PPhSelfPaid_11.toString())
			selfPaid_12 = BigDeciToString(summaryData?.PPhSelfPaid_12.toString())
			selfPaidActual_1 = selfPaid_1.toLong()
			selfPaidActual_2 = selfPaid_2.toLong()
			selfPaidActual_3 = selfPaid_3.toLong()
			selfPaidActual_4 = selfPaid_4.toLong()
			selfPaidActual_5 = selfPaid_5.toLong()
			selfPaidActual_6 = selfPaid_6.toLong()
			selfPaidActual_7 = selfPaid_7.toLong()
			selfPaidActual_8 = selfPaid_8.toLong()
			selfPaidActual_9 = selfPaid_9.toLong()
			selfPaidActual_10 = selfPaid_10.toLong()
			selfPaidActual_11 = selfPaid_11.toLong()
			selfPaidActual_12 = selfPaid_12.toLong()
			
			deducted_1 = BigDeciToString(summaryData?.PPhDeducted_1.toString())
			deducted_2 = BigDeciToString(summaryData?.PPhDeducted_2.toString())
			deducted_3 = BigDeciToString(summaryData?.PPhDeducted_3.toString())
			deducted_4 = BigDeciToString(summaryData?.PPhDeducted_4.toString())
			deducted_5 = BigDeciToString(summaryData?.PPhDeducted_5.toString())
			deducted_6 = BigDeciToString(summaryData?.PPhDeducted_6.toString())
			deducted_7 = BigDeciToString(summaryData?.PPhDeducted_7.toString())
			deducted_8 = BigDeciToString(summaryData?.PPhDeducted_8.toString())
			deducted_9 = BigDeciToString(summaryData?.PPhDeducted_9.toString())
			deducted_10 = BigDeciToString(summaryData?.PPhDeducted_10.toString())
			deducted_11 = BigDeciToString(summaryData?.PPhDeducted_11.toString())
			deducted_12 = BigDeciToString(summaryData?.PPhDeducted_12.toString())
			deductedActual_1 = deducted_1.toLong()
			deductedActual_2 = deducted_2.toLong()
			deductedActual_3 = deducted_3.toLong()
			deductedActual_4 = deducted_4.toLong()
			deductedActual_5 = deducted_5.toLong()
			deductedActual_6 = deducted_6.toLong()
			deductedActual_7 = deducted_7.toLong()
			deductedActual_8 = deducted_8.toLong()
			deductedActual_9 = deducted_9.toLong()
			deductedActual_10 = deducted_10.toLong()
			deductedActual_11 = deducted_11.toLong()
			deductedActual_12 = deducted_12.toLong()
			
			isReady = true
		}
		
		loadingPopupBox(!isReady)
		
		LazyColumn(
			modifier = Modifier.fillMaxSize().background(Colors().panel)
		) {
			//Top Bar
			item { topBar("Ubah Rekapan") }
			
			item {
				Column(
					modifier = Modifier.fillMaxWidth()
						.padding(horizontal = 16.dp)
						.background(Color.White)
						.clip(RoundedCornerShape(8.dp))
						.border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
				) {
					//Jan
					Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
						Row(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
							Text(
								text = "Januari",
								fontSize = 16.sp,
								fontWeight = FontWeight.Bold,
								color = Colors().brandDark40
							)
							
							Text(
								text = "Terutang: Rp ${CurrencyFormatter(BigDeciToString(summaryData?.PPhFinal_1.toString()))}",
								fontSize = 14.sp
							)
						}
						Text(
							text = "Selisih: Rp ${CurrencyFormatter((BigDeciToLong(summaryData?.PPhFinal_1.toString()) - (selfPaidActual_1 + deductedActual_1)).toString())}",
							fontSize = 14.sp,
						)
						
						//Self Paid
						Text(
							text = "PPh Final Disetor Sendiri",
							fontSize = 12.sp,
							color = Colors().textBlack,
							fontWeight = FontWeight.Bold,
							modifier = Modifier.padding(vertical = 8.dp)
						)
						TextField(
							enabled = true,
							shape = RoundedCornerShape(4.dp),
							modifier = Modifier.fillMaxWidth()
								.padding(vertical = 8.dp)
								.border(
									border = BorderStroke(1.dp, Colors().textLightGrey),
									shape = RoundedCornerShape(4.dp)
								),
							value = selfPaid_1,
							onValueChange = {
								selfPaid_1 = it
								selfPaidActual_1 = if(selfPaid_1.isBlank() || selfPaid_1 == "-") 0 else selfPaid_1.toLong()
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
							trailingIcon = {
								if((BigDeciToLong(summaryData?.PPhFinal_1.toString()) - (selfPaidActual_1 + deductedActual_1)) != 0L) {
									Image(
										painterResource(Res.drawable.icon_clear),
										null,
										modifier = Modifier.size(36.dp).padding(end = 16.dp).rotate(45F)
											.clickable(true, onClick = {
												selfPaid_1 =
													(BigDeciToLong(summaryData?.PPhFinal_1.toString()) - (selfPaidActual_1 + deductedActual_1)).toString()
												selfPaidActual_1 = if(selfPaid_1.isBlank() || selfPaid_1 == "-") 0 else selfPaid_1.toLong()
											})
									)
								}
							}
						)
						
						//Deducted
						Text(
							text = "PPh Final yang Dipotong Pihak Lain",
							fontSize = 12.sp,
							color = Colors().textBlack,
							fontWeight = FontWeight.Bold,
							modifier = Modifier.padding(vertical = 8.dp)
						)
						TextField(
							enabled = true,
							shape = RoundedCornerShape(4.dp),
							modifier = Modifier.fillMaxWidth()
								.padding(vertical = 8.dp)
								.border(
									border = BorderStroke(1.dp, Colors().textLightGrey),
									shape = RoundedCornerShape(4.dp)
								),
							value = deducted_1,
							onValueChange = {
								deducted_1 = it
								deductedActual_1 = deducted_1.ifBlank { "0" }.toLong()
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
							)
						)
					}
					divider(0.dp)
					
					//Feb
					Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
						Row(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
							Text(
								text = "Februari",
								fontSize = 16.sp,
								fontWeight = FontWeight.Bold,
								color = Colors().brandDark40
							)
							
							Text(
								text = "Terutang: Rp ${CurrencyFormatter(BigDeciToString(summaryData?.PPhFinal_2.toString()))}",
								fontSize = 14.sp
							)
						}
						Text(
							text = "Selisih: Rp ${CurrencyFormatter((BigDeciToLong(summaryData?.PPhFinal_2.toString()) - (selfPaidActual_2 + deductedActual_2)).toString())}",
							fontSize = 14.sp,
						)
						
						//Self Paid
						Text(
							text = "PPh Final Disetor Sendiri",
							fontSize = 12.sp,
							color = Colors().textBlack,
							fontWeight = FontWeight.Bold,
							modifier = Modifier.padding(vertical = 8.dp)
						)
						TextField(
							enabled = true,
							shape = RoundedCornerShape(4.dp),
							modifier = Modifier.fillMaxWidth()
								.padding(vertical = 8.dp)
								.border(
									border = BorderStroke(1.dp, Colors().textLightGrey),
									shape = RoundedCornerShape(4.dp)
								),
							value = selfPaid_2,
							onValueChange = {
								selfPaid_2 = it
								selfPaidActual_2 = if(selfPaid_2.isBlank() || selfPaid_2 == "-") 0 else selfPaid_2.toLong()
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
							trailingIcon = {
								if((BigDeciToLong(summaryData?.PPhFinal_2.toString()) - (selfPaidActual_2 + deductedActual_2)) != 0L) {
									Image(
										painterResource(Res.drawable.icon_clear),
										null,
										modifier = Modifier.size(36.dp).padding(end = 16.dp).rotate(45F)
											.clickable(true, onClick = {
												selfPaid_2 =
													(BigDeciToLong(summaryData?.PPhFinal_2.toString()) - (selfPaidActual_2 + deductedActual_2)).toString()
												selfPaidActual_2 = if(selfPaid_2.isBlank() || selfPaid_2 == "-") 0 else selfPaid_2.toLong()
											})
									)
								}
							}
						)
						
						//Deducted
						Text(
							text = "PPh Final yang Dipotong Pihak Lain",
							fontSize = 12.sp,
							color = Colors().textBlack,
							fontWeight = FontWeight.Bold,
							modifier = Modifier.padding(vertical = 8.dp)
						)
						TextField(
							enabled = true,
							shape = RoundedCornerShape(4.dp),
							modifier = Modifier.fillMaxWidth()
								.padding(vertical = 8.dp)
								.border(
									border = BorderStroke(1.dp, Colors().textLightGrey),
									shape = RoundedCornerShape(4.dp)
								),
							value = deducted_2,
							onValueChange = {
								deducted_2 = it
								deductedActual_2 = deducted_2.ifBlank { "0" }.toLong()
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
							)
						)
					}
					divider(0.dp)
					
					//Mar
					Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
						Row(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
							Text(
								text = "Maret",
								fontSize = 16.sp,
								fontWeight = FontWeight.Bold,
								color = Colors().brandDark40
							)
							
							Text(
								text = "Terutang: Rp ${CurrencyFormatter(BigDeciToString(summaryData?.PPhFinal_3.toString()))}",
								fontSize = 14.sp
							)
						}
						Text(
							text = "Selisih: Rp ${CurrencyFormatter((BigDeciToLong(summaryData?.PPhFinal_3.toString()) - (selfPaidActual_3 + deductedActual_3)).toString())}",
							fontSize = 14.sp,
						)
						
						//Self Paid
						Text(
							text = "PPh Final Disetor Sendiri",
							fontSize = 12.sp,
							color = Colors().textBlack,
							fontWeight = FontWeight.Bold,
							modifier = Modifier.padding(vertical = 8.dp)
						)
						TextField(
							enabled = true,
							shape = RoundedCornerShape(4.dp),
							modifier = Modifier.fillMaxWidth()
								.padding(vertical = 8.dp)
								.border(
									border = BorderStroke(1.dp, Colors().textLightGrey),
									shape = RoundedCornerShape(4.dp)
								),
							value = selfPaid_3,
							onValueChange = {
								selfPaid_3 = it
								selfPaidActual_3 = if(selfPaid_3.isBlank() || selfPaid_3 == "-") 0 else selfPaid_3.toLong()
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
							trailingIcon = {
								if((BigDeciToLong(summaryData?.PPhFinal_3.toString()) - (selfPaidActual_3 + deductedActual_3)) != 0L) {
									Image(
										painterResource(Res.drawable.icon_clear),
										null,
										modifier = Modifier.size(36.dp).padding(end = 16.dp).rotate(45F)
											.clickable(true, onClick = {
												selfPaid_3 =
													(BigDeciToLong(summaryData?.PPhFinal_3.toString()) - (selfPaidActual_3 + deductedActual_3)).toString()
												selfPaidActual_3 = if(selfPaid_3.isBlank() || selfPaid_3 == "-") 0 else selfPaid_3.toLong()
											})
									)
								}
							}
						)
						
						//Deducted
						Text(
							text = "PPh Final yang Dipotong Pihak Lain",
							fontSize = 12.sp,
							color = Colors().textBlack,
							fontWeight = FontWeight.Bold,
							modifier = Modifier.padding(vertical = 8.dp)
						)
						TextField(
							enabled = true,
							shape = RoundedCornerShape(4.dp),
							modifier = Modifier.fillMaxWidth()
								.padding(vertical = 8.dp)
								.border(
									border = BorderStroke(1.dp, Colors().textLightGrey),
									shape = RoundedCornerShape(4.dp)
								),
							value = deducted_3,
							onValueChange = {
								deducted_3 = it
								deductedActual_3 = deducted_3.ifBlank { "0" }.toLong()
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
							)
						)
					}
					divider(0.dp)
					
					//Apr
					Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
						Row(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
							Text(
								text = "April",
								fontSize = 16.sp,
								fontWeight = FontWeight.Bold,
								color = Colors().brandDark40
							)
							
							Text(
								text = "Terutang: Rp ${CurrencyFormatter(BigDeciToString(summaryData?.PPhFinal_4.toString()))}",
								fontSize = 14.sp
							)
						}
						Text(
							text = "Selisih: Rp ${CurrencyFormatter((BigDeciToLong(summaryData?.PPhFinal_4.toString()) - (selfPaidActual_4 + deductedActual_4)).toString())}",
							fontSize = 14.sp,
						)
						
						//Self Paid
						Text(
							text = "PPh Final Disetor Sendiri",
							fontSize = 12.sp,
							color = Colors().textBlack,
							fontWeight = FontWeight.Bold,
							modifier = Modifier.padding(vertical = 8.dp)
						)
						TextField(
							enabled = true,
							shape = RoundedCornerShape(4.dp),
							modifier = Modifier.fillMaxWidth()
								.padding(vertical = 8.dp)
								.border(
									border = BorderStroke(1.dp, Colors().textLightGrey),
									shape = RoundedCornerShape(4.dp)
								),
							value = selfPaid_4,
							onValueChange = {
								selfPaid_4 = it
								selfPaidActual_4 = if(selfPaid_4.isBlank() || selfPaid_4 == "-") 0 else selfPaid_4.toLong()
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
							trailingIcon = {
								if((BigDeciToLong(summaryData?.PPhFinal_4.toString()) - (selfPaidActual_4 + deductedActual_4)) != 0L) {
									Image(
										painterResource(Res.drawable.icon_clear),
										null,
										modifier = Modifier.size(36.dp).padding(end = 16.dp).rotate(45F)
											.clickable(true, onClick = {
												selfPaid_4 =
													(BigDeciToLong(summaryData?.PPhFinal_4.toString()) - (selfPaidActual_4 + deductedActual_4)).toString()
												selfPaidActual_4 = if(selfPaid_4.isBlank() || selfPaid_4 == "-") 0 else selfPaid_4.toLong()
											})
									)
								}
							}
						)
						
						//Deducted
						Text(
							text = "PPh Final yang Dipotong Pihak Lain",
							fontSize = 12.sp,
							color = Colors().textBlack,
							fontWeight = FontWeight.Bold,
							modifier = Modifier.padding(vertical = 8.dp)
						)
						TextField(
							enabled = true,
							shape = RoundedCornerShape(4.dp),
							modifier = Modifier.fillMaxWidth()
								.padding(vertical = 8.dp)
								.border(
									border = BorderStroke(1.dp, Colors().textLightGrey),
									shape = RoundedCornerShape(4.dp)
								),
							value = deducted_4,
							onValueChange = {
								deducted_4 = it
								deductedActual_4 = deducted_4.ifBlank { "0" }.toLong()
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
							)
						)
					}
					divider(0.dp)
					
					//May
					Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
						Row(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
							Text(
								text = "Mei",
								fontSize = 16.sp,
								fontWeight = FontWeight.Bold,
								color = Colors().brandDark40
							)
							
							Text(
								text = "Terutang: Rp ${CurrencyFormatter(BigDeciToString(summaryData?.PPhFinal_5.toString()))}",
								fontSize = 14.sp
							)
						}
						Text(
							text = "Selisih: Rp ${CurrencyFormatter((BigDeciToLong(summaryData?.PPhFinal_5.toString()) - (selfPaidActual_5 + deductedActual_5)).toString())}",
							fontSize = 14.sp,
						)
						
						//Self Paid
						Text(
							text = "PPh Final Disetor Sendiri",
							fontSize = 12.sp,
							color = Colors().textBlack,
							fontWeight = FontWeight.Bold,
							modifier = Modifier.padding(vertical = 8.dp)
						)
						TextField(
							enabled = true,
							shape = RoundedCornerShape(4.dp),
							modifier = Modifier.fillMaxWidth()
								.padding(vertical = 8.dp)
								.border(
									border = BorderStroke(1.dp, Colors().textLightGrey),
									shape = RoundedCornerShape(4.dp)
								),
							value = selfPaid_5,
							onValueChange = {
								selfPaid_5 = it
								selfPaidActual_5 = if(selfPaid_5.isBlank() || selfPaid_5 == "-") 0 else selfPaid_5.toLong()
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
							trailingIcon = {
								if((BigDeciToLong(summaryData?.PPhFinal_5.toString()) - (selfPaidActual_5 + deductedActual_5)) != 0L) {
									Image(
										painterResource(Res.drawable.icon_clear),
										null,
										modifier = Modifier.size(36.dp).padding(end = 16.dp).rotate(45F)
											.clickable(true, onClick = {
												selfPaid_5 =
													(BigDeciToLong(summaryData?.PPhFinal_5.toString()) - (selfPaidActual_5 + deductedActual_5)).toString()
												selfPaidActual_5 = if(selfPaid_5.isBlank() || selfPaid_5 == "-") 0 else selfPaid_5.toLong()
											})
									)
								}
							}
						)
						
						//Deducted
						Text(
							text = "PPh Final yang Dipotong Pihak Lain",
							fontSize = 12.sp,
							color = Colors().textBlack,
							fontWeight = FontWeight.Bold,
							modifier = Modifier.padding(vertical = 8.dp)
						)
						TextField(
							enabled = true,
							shape = RoundedCornerShape(4.dp),
							modifier = Modifier.fillMaxWidth()
								.padding(vertical = 8.dp)
								.border(
									border = BorderStroke(1.dp, Colors().textLightGrey),
									shape = RoundedCornerShape(4.dp)
								),
							value = deducted_5,
							onValueChange = {
								deducted_5 = it
								deductedActual_5 = deducted_5.ifBlank { "0" }.toLong()
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
							)
						)
					}
					divider(0.dp)
					
					//Jun
					Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
						Row(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
							Text(
								text = "Juni",
								fontSize = 16.sp,
								fontWeight = FontWeight.Bold,
								color = Colors().brandDark40
							)
							
							Text(
								text = "Terutang: Rp ${CurrencyFormatter(BigDeciToString(summaryData?.PPhFinal_6.toString()))}",
								fontSize = 14.sp
							)
						}
						Text(
							text = "Selisih: Rp ${CurrencyFormatter((BigDeciToLong(summaryData?.PPhFinal_6.toString()) - (selfPaidActual_6 + deductedActual_6)).toString())}",
							fontSize = 14.sp,
						)
						
						//Self Paid
						Text(
							text = "PPh Final Disetor Sendiri",
							fontSize = 12.sp,
							color = Colors().textBlack,
							fontWeight = FontWeight.Bold,
							modifier = Modifier.padding(vertical = 8.dp)
						)
						TextField(
							enabled = true,
							shape = RoundedCornerShape(4.dp),
							modifier = Modifier.fillMaxWidth()
								.padding(vertical = 8.dp)
								.border(
									border = BorderStroke(1.dp, Colors().textLightGrey),
									shape = RoundedCornerShape(4.dp)
								),
							value = selfPaid_6,
							onValueChange = {
								selfPaid_6 = it
								selfPaidActual_6 = if(selfPaid_6.isBlank() || selfPaid_6 == "-") 0 else selfPaid_6.toLong()
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
							trailingIcon = {
								if((BigDeciToLong(summaryData?.PPhFinal_6.toString()) - (selfPaidActual_6 + deductedActual_6)) != 0L) {
									Image(
										painterResource(Res.drawable.icon_clear),
										null,
										modifier = Modifier.size(36.dp).padding(end = 16.dp).rotate(45F)
											.clickable(true, onClick = {
												selfPaid_6 =
													(BigDeciToLong(summaryData?.PPhFinal_6.toString()) - (selfPaidActual_6 + deductedActual_6)).toString()
												selfPaidActual_6 = if(selfPaid_6.isBlank() || selfPaid_6 == "-") 0 else selfPaid_6.toLong()
											})
									)
								}
							}
						)
						
						//Deducted
						Text(
							text = "PPh Final yang Dipotong Pihak Lain",
							fontSize = 12.sp,
							color = Colors().textBlack,
							fontWeight = FontWeight.Bold,
							modifier = Modifier.padding(vertical = 8.dp)
						)
						TextField(
							enabled = true,
							shape = RoundedCornerShape(4.dp),
							modifier = Modifier.fillMaxWidth()
								.padding(vertical = 8.dp)
								.border(
									border = BorderStroke(1.dp, Colors().textLightGrey),
									shape = RoundedCornerShape(4.dp)
								),
							value = deducted_6,
							onValueChange = {
								deducted_6 = it
								deductedActual_6 = deducted_6.ifBlank { "0" }.toLong()
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
							)
						)
					}
					divider(0.dp)
					
					//Jul
					Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
						Row(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
							Text(
								text = "Juli",
								fontSize = 16.sp,
								fontWeight = FontWeight.Bold,
								color = Colors().brandDark40
							)
							
							Text(
								text = "Terutang: Rp ${CurrencyFormatter(BigDeciToString(summaryData?.PPhFinal_7.toString()))}",
								fontSize = 14.sp
							)
						}
						Text(
							text = "Selisih: Rp ${CurrencyFormatter((BigDeciToLong(summaryData?.PPhFinal_7.toString()) - (selfPaidActual_7 + deductedActual_7)).toString())}",
							fontSize = 14.sp,
						)
						
						//Self Paid
						Text(
							text = "PPh Final Disetor Sendiri",
							fontSize = 12.sp,
							color = Colors().textBlack,
							fontWeight = FontWeight.Bold,
							modifier = Modifier.padding(vertical = 8.dp)
						)
						TextField(
							enabled = true,
							shape = RoundedCornerShape(4.dp),
							modifier = Modifier.fillMaxWidth()
								.padding(vertical = 8.dp)
								.border(
									border = BorderStroke(1.dp, Colors().textLightGrey),
									shape = RoundedCornerShape(4.dp)
								),
							value = selfPaid_7,
							onValueChange = {
								selfPaid_7 = it
								selfPaidActual_7 = if(selfPaid_7.isBlank() || selfPaid_7 == "-") 0 else selfPaid_7.toLong()
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
							trailingIcon = {
								if((BigDeciToLong(summaryData?.PPhFinal_7.toString()) - (selfPaidActual_7 + deductedActual_7)) != 0L) {
									Image(
										painterResource(Res.drawable.icon_clear),
										null,
										modifier = Modifier.size(36.dp).padding(end = 16.dp).rotate(45F)
											.clickable(true, onClick = {
												selfPaid_7 =
													(BigDeciToLong(summaryData?.PPhFinal_7.toString()) - (selfPaidActual_7 + deductedActual_7)).toString()
												selfPaidActual_7 = if(selfPaid_7.isBlank() || selfPaid_7 == "-") 0 else selfPaid_7.toLong()
											})
									)
								}
							}
						)
						
						//Deducted
						Text(
							text = "PPh Final yang Dipotong Pihak Lain",
							fontSize = 12.sp,
							color = Colors().textBlack,
							fontWeight = FontWeight.Bold,
							modifier = Modifier.padding(vertical = 8.dp)
						)
						TextField(
							enabled = true,
							shape = RoundedCornerShape(4.dp),
							modifier = Modifier.fillMaxWidth()
								.padding(vertical = 8.dp)
								.border(
									border = BorderStroke(1.dp, Colors().textLightGrey),
									shape = RoundedCornerShape(4.dp)
								),
							value = deducted_7,
							onValueChange = {
								deducted_7 = it
								deductedActual_7 = deducted_7.ifBlank { "0" }.toLong()
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
							)
						)
					}
					divider(0.dp)
					
					//Aug
					Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
						Row(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
							Text(
								text = "Agustus",
								fontSize = 16.sp,
								fontWeight = FontWeight.Bold,
								color = Colors().brandDark40
							)
							
							Text(
								text = "Terutang: Rp ${CurrencyFormatter(BigDeciToString(summaryData?.PPhFinal_8.toString()))}",
								fontSize = 14.sp
							)
						}
						Text(
							text = "Selisih: Rp ${CurrencyFormatter((BigDeciToLong(summaryData?.PPhFinal_8.toString()) - (selfPaidActual_8 + deductedActual_8)).toString())}",
							fontSize = 14.sp,
						)
						
						//Self Paid
						Text(
							text = "PPh Final Disetor Sendiri",
							fontSize = 12.sp,
							color = Colors().textBlack,
							fontWeight = FontWeight.Bold,
							modifier = Modifier.padding(vertical = 8.dp)
						)
						TextField(
							enabled = true,
							shape = RoundedCornerShape(4.dp),
							modifier = Modifier.fillMaxWidth()
								.padding(vertical = 8.dp)
								.border(
									border = BorderStroke(1.dp, Colors().textLightGrey),
									shape = RoundedCornerShape(4.dp)
								),
							value = selfPaid_8,
							onValueChange = {
								selfPaid_8 = it
								selfPaidActual_8 = if(selfPaid_8.isBlank() || selfPaid_8 == "-") 0 else selfPaid_8.toLong()
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
							trailingIcon = {
								if((BigDeciToLong(summaryData?.PPhFinal_8.toString()) - (selfPaidActual_8 + deductedActual_8)) != 0L) {
									Image(
										painterResource(Res.drawable.icon_clear),
										null,
										modifier = Modifier.size(36.dp).padding(end = 16.dp).rotate(45F)
											.clickable(true, onClick = {
												selfPaid_8 =
													(BigDeciToLong(summaryData?.PPhFinal_8.toString()) - (selfPaidActual_8 + deductedActual_8)).toString()
												selfPaidActual_8 = if(selfPaid_8.isBlank() || selfPaid_8 == "-") 0 else selfPaid_8.toLong()
											})
									)
								}
							}
						)
						
						//Deducted
						Text(
							text = "PPh Final yang Dipotong Pihak Lain",
							fontSize = 12.sp,
							color = Colors().textBlack,
							fontWeight = FontWeight.Bold,
							modifier = Modifier.padding(vertical = 8.dp)
						)
						TextField(
							enabled = true,
							shape = RoundedCornerShape(4.dp),
							modifier = Modifier.fillMaxWidth()
								.padding(vertical = 8.dp)
								.border(
									border = BorderStroke(1.dp, Colors().textLightGrey),
									shape = RoundedCornerShape(4.dp)
								),
							value = deducted_8,
							onValueChange = {
								deducted_8 = it
								deductedActual_8 = deducted_8.ifBlank { "0" }.toLong()
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
							)
						)
					}
					divider(0.dp)
					
					//Sep
					Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
						Row(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
							Text(
								text = "September",
								fontSize = 16.sp,
								fontWeight = FontWeight.Bold,
								color = Colors().brandDark40
							)
							
							Text(
								text = "Terutang: Rp ${CurrencyFormatter(BigDeciToString(summaryData?.PPhFinal_9.toString()))}",
								fontSize = 14.sp
							)
						}
						Text(
							text = "Selisih: Rp ${CurrencyFormatter((BigDeciToLong(summaryData?.PPhFinal_9.toString()) - (selfPaidActual_9 + deductedActual_9)).toString())}",
							fontSize = 14.sp,
						)
						
						//Self Paid
						Text(
							text = "PPh Final Disetor Sendiri",
							fontSize = 12.sp,
							color = Colors().textBlack,
							fontWeight = FontWeight.Bold,
							modifier = Modifier.padding(vertical = 8.dp)
						)
						TextField(
							enabled = true,
							shape = RoundedCornerShape(4.dp),
							modifier = Modifier.fillMaxWidth()
								.padding(vertical = 8.dp)
								.border(
									border = BorderStroke(1.dp, Colors().textLightGrey),
									shape = RoundedCornerShape(4.dp)
								),
							value = selfPaid_9,
							onValueChange = {
								selfPaid_9 = it
								selfPaidActual_9 = if(selfPaid_9.isBlank() || selfPaid_9 == "-") 0 else selfPaid_9.toLong()
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
							trailingIcon = {
								if((BigDeciToLong(summaryData?.PPhFinal_9.toString()) - (selfPaidActual_9 + deductedActual_9)) != 0L) {
									Image(
										painterResource(Res.drawable.icon_clear),
										null,
										modifier = Modifier.size(36.dp).padding(end = 16.dp).rotate(45F)
											.clickable(true, onClick = {
												selfPaid_9 =
													(BigDeciToLong(summaryData?.PPhFinal_9.toString()) - (selfPaidActual_9 + deductedActual_9)).toString()
												selfPaidActual_9 = if(selfPaid_9.isBlank() || selfPaid_9 == "-") 0 else selfPaid_9.toLong()
											})
									)
								}
							}
						)
						
						//Deducted
						Text(
							text = "PPh Final yang Dipotong Pihak Lain",
							fontSize = 12.sp,
							color = Colors().textBlack,
							fontWeight = FontWeight.Bold,
							modifier = Modifier.padding(vertical = 8.dp)
						)
						TextField(
							enabled = true,
							shape = RoundedCornerShape(4.dp),
							modifier = Modifier.fillMaxWidth()
								.padding(vertical = 8.dp)
								.border(
									border = BorderStroke(1.dp, Colors().textLightGrey),
									shape = RoundedCornerShape(4.dp)
								),
							value = deducted_9,
							onValueChange = {
								deducted_9 = it
								deductedActual_9 = deducted_9.ifBlank { "0" }.toLong()
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
							)
						)
					}
					divider(0.dp)
					
					//Oct
					Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
						Row(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
							Text(
								text = "Oktober",
								fontSize = 16.sp,
								fontWeight = FontWeight.Bold,
								color = Colors().brandDark40
							)
							
							Text(
								text = "Terutang: Rp ${CurrencyFormatter(BigDeciToString(summaryData?.PPhFinal_10.toString()))}",
								fontSize = 14.sp
							)
						}
						Text(
							text = "Selisih: Rp ${CurrencyFormatter((BigDeciToLong(summaryData?.PPhFinal_10.toString()) - (selfPaidActual_10 + deductedActual_10)).toString())}",
							fontSize = 14.sp,
						)
						
						//Self Paid
						Text(
							text = "PPh Final Disetor Sendiri",
							fontSize = 12.sp,
							color = Colors().textBlack,
							fontWeight = FontWeight.Bold,
							modifier = Modifier.padding(vertical = 8.dp)
						)
						TextField(
							enabled = true,
							shape = RoundedCornerShape(4.dp),
							modifier = Modifier.fillMaxWidth()
								.padding(vertical = 8.dp)
								.border(
									border = BorderStroke(1.dp, Colors().textLightGrey),
									shape = RoundedCornerShape(4.dp)
								),
							value = selfPaid_10,
							onValueChange = {
								selfPaid_10 = it
								selfPaidActual_10 = if(selfPaid_10.isBlank() || selfPaid_10 == "-") 0 else selfPaid_10.toLong()
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
							trailingIcon = {
								if((BigDeciToLong(summaryData?.PPhFinal_10.toString()) - (selfPaidActual_10 + deductedActual_10)) != 0L) {
									Image(
										painterResource(Res.drawable.icon_clear),
										null,
										modifier = Modifier.size(36.dp).padding(end = 16.dp).rotate(45F)
											.clickable(true, onClick = {
												selfPaid_10 =
													(BigDeciToLong(summaryData?.PPhFinal_10.toString()) - (selfPaidActual_10 + deductedActual_10)).toString()
												selfPaidActual_10 = if(selfPaid_10.isBlank() || selfPaid_10 == "-") 0 else selfPaid_10.toLong()
											})
									)
								}
							}
						)
						
						//Deducted
						Text(
							text = "PPh Final yang Dipotong Pihak Lain",
							fontSize = 12.sp,
							color = Colors().textBlack,
							fontWeight = FontWeight.Bold,
							modifier = Modifier.padding(vertical = 8.dp)
						)
						TextField(
							enabled = true,
							shape = RoundedCornerShape(4.dp),
							modifier = Modifier.fillMaxWidth()
								.padding(vertical = 8.dp)
								.border(
									border = BorderStroke(1.dp, Colors().textLightGrey),
									shape = RoundedCornerShape(4.dp)
								),
							value = deducted_10,
							onValueChange = {
								deducted_10 = it
								deductedActual_10 = deducted_10.ifBlank { "0" }.toLong()
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
							)
						)
					}
					divider(0.dp)
					
					//Nov
					Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
						Row(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
							Text(
								text = "November",
								fontSize = 16.sp,
								fontWeight = FontWeight.Bold,
								color = Colors().brandDark40
							)
							
							Text(
								text = "Terutang: Rp ${CurrencyFormatter(BigDeciToString(summaryData?.PPhFinal_10.toString()))}",
								fontSize = 14.sp
							)
						}
						Text(
							text = "Selisih: Rp ${CurrencyFormatter((BigDeciToLong(summaryData?.PPhFinal_10.toString()) - (selfPaidActual_10 + deductedActual_10)).toString())}",
							fontSize = 14.sp,
						)
						
						//Self Paid
						Text(
							text = "PPh Final Disetor Sendiri",
							fontSize = 12.sp,
							color = Colors().textBlack,
							fontWeight = FontWeight.Bold,
							modifier = Modifier.padding(vertical = 8.dp)
						)
						TextField(
							enabled = true,
							shape = RoundedCornerShape(4.dp),
							modifier = Modifier.fillMaxWidth()
								.padding(vertical = 8.dp)
								.border(
									border = BorderStroke(1.dp, Colors().textLightGrey),
									shape = RoundedCornerShape(4.dp)
								),
							value = selfPaid_10,
							onValueChange = {
								selfPaid_10 = it
								selfPaidActual_10 = if(selfPaid_10.isBlank() || selfPaid_10 == "-") 0 else selfPaid_10.toLong()
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
							trailingIcon = {
								if((BigDeciToLong(summaryData?.PPhFinal_10.toString()) - (selfPaidActual_10 + deductedActual_10)) != 0L) {
									Image(
										painterResource(Res.drawable.icon_clear),
										null,
										modifier = Modifier.size(36.dp).padding(end = 16.dp).rotate(45F)
											.clickable(true, onClick = {
												selfPaid_10 =
													(BigDeciToLong(summaryData?.PPhFinal_10.toString()) - (selfPaidActual_10 + deductedActual_10)).toString()
												selfPaidActual_10 = if(selfPaid_10.isBlank() || selfPaid_10 == "-") 0 else selfPaid_10.toLong()
											})
									)
								}
							}
						)
						
						//Deducted
						Text(
							text = "PPh Final yang Dipotong Pihak Lain",
							fontSize = 12.sp,
							color = Colors().textBlack,
							fontWeight = FontWeight.Bold,
							modifier = Modifier.padding(vertical = 8.dp)
						)
						TextField(
							enabled = true,
							shape = RoundedCornerShape(4.dp),
							modifier = Modifier.fillMaxWidth()
								.padding(vertical = 8.dp)
								.border(
									border = BorderStroke(1.dp, Colors().textLightGrey),
									shape = RoundedCornerShape(4.dp)
								),
							value = deducted_10,
							onValueChange = {
								deducted_10 = it
								deductedActual_10 = deducted_10.ifBlank { "0" }.toLong()
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
							)
						)
					}
					divider(0.dp)
					
					//Dec
					Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
						Row(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
							Text(
								text = "Desember",
								fontSize = 16.sp,
								fontWeight = FontWeight.Bold,
								color = Colors().brandDark40
							)
							
							Text(
								text = "Terutang: Rp ${CurrencyFormatter(BigDeciToString(summaryData?.PPhFinal_12.toString()))}",
								fontSize = 14.sp
							)
						}
						Text(
							text = "Selisih: Rp ${CurrencyFormatter((BigDeciToLong(summaryData?.PPhFinal_12.toString()) - (selfPaidActual_12 + deductedActual_12)).toString())}",
							fontSize = 14.sp,
						)
						
						//Self Paid
						Text(
							text = "PPh Final Disetor Sendiri",
							fontSize = 12.sp,
							color = Colors().textBlack,
							fontWeight = FontWeight.Bold,
							modifier = Modifier.padding(vertical = 8.dp)
						)
						TextField(
							enabled = true,
							shape = RoundedCornerShape(4.dp),
							modifier = Modifier.fillMaxWidth()
								.padding(vertical = 8.dp)
								.border(
									border = BorderStroke(1.dp, Colors().textLightGrey),
									shape = RoundedCornerShape(4.dp)
								),
							value = selfPaid_12,
							onValueChange = {
								selfPaid_12 = it
								selfPaidActual_12 = if(selfPaid_12.isBlank() || selfPaid_12 == "-") 0 else selfPaid_12.toLong()
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
							trailingIcon = {
								if((BigDeciToLong(summaryData?.PPhFinal_12.toString()) - (selfPaidActual_12 + deductedActual_12)) != 0L) {
									Image(
										painterResource(Res.drawable.icon_clear),
										null,
										modifier = Modifier.size(36.dp).padding(end = 16.dp).rotate(45F)
											.clickable(true, onClick = {
												selfPaid_12 =
													(BigDeciToLong(summaryData?.PPhFinal_12.toString()) - (selfPaidActual_12 + deductedActual_12)).toString()
												selfPaidActual_12 = if(selfPaid_12.isBlank() || selfPaid_12 == "-") 0 else selfPaid_12.toLong()
											})
									)
								}
							}
						)
						
						//Deducted
						Text(
							text = "PPh Final yang Dipotong Pihak Lain",
							fontSize = 12.sp,
							color = Colors().textBlack,
							fontWeight = FontWeight.Bold,
							modifier = Modifier.padding(vertical = 8.dp)
						)
						TextField(
							enabled = true,
							shape = RoundedCornerShape(4.dp),
							modifier = Modifier.fillMaxWidth()
								.padding(vertical = 8.dp)
								.border(
									border = BorderStroke(1.dp, Colors().textLightGrey),
									shape = RoundedCornerShape(4.dp)
								),
							value = deducted_12,
							onValueChange = {
								deducted_12 = it
								deductedActual_12 = deducted_12.ifBlank { "0" }.toLong()
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
							)
						)
					}
					divider(0.dp)
				}
			}
			
			//Submit Button
			item {
				Button(
					enabled = true,
					modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 22.dp),
					colors = buttonColors(backgroundColor = Colors().buttonActive, contentColor = Color.White),
					onClick = {
						val dataModel = Form1770FinalIncomeUmkm2023SummaryRequestModel(
							Tr1770IncomeId = summaryData!!.Tr1770IncomeId,
							PPhSelfPaid_1 = selfPaidActual_1,
							PPhSelfPaid_2 = selfPaidActual_2,
							PPhSelfPaid_3 = selfPaidActual_3,
							PPhSelfPaid_4 = selfPaidActual_4,
							PPhSelfPaid_5 = selfPaidActual_5,
							PPhSelfPaid_6 = selfPaidActual_6,
							PPhSelfPaid_7 = selfPaidActual_7,
							PPhSelfPaid_8 = selfPaidActual_8,
							PPhSelfPaid_9 = selfPaidActual_9,
							PPhSelfPaid_10 = selfPaidActual_10,
							PPhSelfPaid_11 = selfPaidActual_11,
							PPhSelfPaid_12 = selfPaidActual_12,
							PPhDeducted_1 = deductedActual_1,
							PPhDeducted_2 = deductedActual_2,
							PPhDeducted_3 = deductedActual_3,
							PPhDeducted_4 = deductedActual_4,
							PPhDeducted_5 = deductedActual_5,
							PPhDeducted_6 = deductedActual_6,
							PPhDeducted_7 = deductedActual_7,
							PPhDeducted_8 = deductedActual_8,
							PPhDeducted_9 = deductedActual_9,
							PPhDeducted_10 = deductedActual_10,
							PPhDeducted_11 = deductedActual_11,
							PPhDeducted_12 = deductedActual_12,
						)
						
						scope.launch {
							isReady = false
							sptManager.saveFinalIncomeUmkm2023Summary(scope, dataModel)
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