package screens.SPT

import SPT.SPTManager
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
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import global.Colors
import global.universalUIComponents.loadingPopupBox
import global.universalUIComponents.topBar
import http.Account
import http.Interfaces
import models.transaction.Form1770FinalIncomeUmkm2023SummaryModel
import models.transaction.Form1770HdResponseApiModel
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource
import screens.divider
import util.BigDeciToString
import util.CurrencyFormatter

class BrutoRecapScreen(val sptHd: Form1770HdResponseApiModel?, val client: Account, val sptPertamaClient: Interfaces, val prefs: DataStore<Preferences>): Screen {
	
	val sptManager = SPTManager(prefs, client, sptPertamaClient)
	
	@Composable
	override fun Content() {
		
		val scope = rememberCoroutineScope()
		val navigator = LocalNavigator.currentOrThrow
		
		var summary by remember { mutableStateOf<Form1770FinalIncomeUmkm2023SummaryModel?>(null) }
		
		var sumDetails by remember { mutableStateOf(false) }
		var accumulateDetails by remember { mutableStateOf(false) }
		var pkpDetails by remember { mutableStateOf(false) }
		var pphFinalDetails by remember { mutableStateOf(false) }
		var pphSelfPaidDetails by remember { mutableStateOf(false) }
		var pphDeductedDetails by remember { mutableStateOf(false) }
		var pphDifferenceDetails by remember { mutableStateOf(false) }
		
		var isReady by remember { mutableStateOf(false) }
		
		LaunchedEffect(null) {
			val data = sptManager.getFinalIncomeUMKMByHdId(scope, sptHd!!.Id.toString())
			
			if (data != null) {
				summary = data.Summary
			}
			
			isReady = true
		}
		
		loadingPopupBox(!isReady)
		
		LazyColumn(
			modifier = Modifier.fillMaxSize().background(Colors().panel)
		) {
			//Top Bar
			item { topBar("Rekapan") }
			
			//Summary
			item {
				Column(
					modifier = Modifier.fillMaxWidth()
						.padding(horizontal = 16.dp)
						.background(Color.White)
						.clip(RoundedCornerShape(8.dp))
						.border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
				) {
					//a. Sum
					Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
						Text(
							text = "a.",
							fontSize = 14.sp,
							fontWeight = FontWeight.Bold,
							modifier = Modifier.padding(end = 4.dp)
						)
						Row(
							modifier = Modifier.fillMaxWidth(),
							horizontalArrangement = Arrangement.SpaceBetween,
							verticalAlignment = Alignment.Top
						) {
							Column {
								Text(
									text = "Jumlah Peredaran Bruto",
									fontSize = 14.sp,
									fontWeight = FontWeight.Bold,
									modifier = Modifier.padding(end = 8.dp)
								)
								
								if(sumDetails) {
									Text(
										text = "Januari: Rp ${CurrencyFormatter(BigDeciToString(summary?.Sum_1.toString()))}\n" +
												"Februari: Rp ${CurrencyFormatter(BigDeciToString(summary?.Sum_2.toString()))}\n" +
												"Maret: Rp ${CurrencyFormatter(BigDeciToString(summary?.Sum_3.toString()))}\n" +
												"April: Rp ${CurrencyFormatter(BigDeciToString(summary?.Sum_4.toString()))}\n" +
												"Mei: Rp ${CurrencyFormatter(BigDeciToString(summary?.Sum_5.toString()))}\n" +
												"Juni: Rp ${CurrencyFormatter(BigDeciToString(summary?.Sum_6.toString()))}\n" +
												"Juli: Rp ${CurrencyFormatter(BigDeciToString(summary?.Sum_7.toString()))}\n" +
												"Agustus: Rp ${CurrencyFormatter(BigDeciToString(summary?.Sum_8.toString()))}\n" +
												"September: Rp ${CurrencyFormatter(BigDeciToString(summary?.Sum_9.toString()))}\n" +
												"Oktober: Rp ${CurrencyFormatter(BigDeciToString(summary?.Sum_10.toString()))}\n" +
												"November: Rp ${CurrencyFormatter(BigDeciToString(summary?.Sum_11.toString()))}\n" +
												"Desember: Rp ${CurrencyFormatter(BigDeciToString(summary?.Sum_12.toString()))}\n",
										fontSize = 12.sp,
										modifier = Modifier.padding(top = 8.dp)
									)
								}
								
								Text(
									text = "Total: Rp ${CurrencyFormatter(BigDeciToString(summary?.SumTotal.toString()))}",
									fontSize = 12.sp,
									modifier = Modifier.padding(top = 8.dp)
								)
							}
							Image(
								painterResource(Res.drawable.Icon_Dropdown_Arrow),
								contentDescription = null,
								modifier = Modifier.size(16.dp).clickable(true, onClick = {
									sumDetails = !sumDetails
								}).rotate(if(sumDetails) 180f else 0f)
							)
						}
					}
					divider(0.dp)
					//b. Akumulasi Peredaran Bruto
					Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
						Text(
							text = "b.",
							fontSize = 14.sp,
							fontWeight = FontWeight.Bold,
							modifier = Modifier.padding(end = 4.dp)
						)
						Row(
							modifier = Modifier.fillMaxWidth(),
							horizontalArrangement = Arrangement.SpaceBetween,
							verticalAlignment = Alignment.Top
						) {
							Column {
								Text(
									text = "Akumulasi Peredaran Bruto",
									fontSize = 14.sp,
									fontWeight = FontWeight.Bold,
									modifier = Modifier.padding(end = 8.dp)
								)
								
								if(accumulateDetails) {
									Text(
										text = "Januari: Rp ${CurrencyFormatter(BigDeciToString(summary?.Accumulate_1.toString()))}\n" +
												"Februari: Rp ${CurrencyFormatter(BigDeciToString(summary?.Accumulate_2.toString()))}\n" +
												"Maret: Rp ${CurrencyFormatter(BigDeciToString(summary?.Accumulate_3.toString()))}\n" +
												"April: Rp ${CurrencyFormatter(BigDeciToString(summary?.Accumulate_4.toString()))}\n" +
												"Mei: Rp ${CurrencyFormatter(BigDeciToString(summary?.Accumulate_5.toString()))}\n" +
												"Juni: Rp ${CurrencyFormatter(BigDeciToString(summary?.Accumulate_6.toString()))}\n" +
												"Juli: Rp ${CurrencyFormatter(BigDeciToString(summary?.Accumulate_7.toString()))}\n" +
												"Agustus: Rp ${CurrencyFormatter(BigDeciToString(summary?.Accumulate_8.toString()))}\n" +
												"September: Rp ${CurrencyFormatter(BigDeciToString(summary?.Accumulate_9.toString()))}\n" +
												"Oktober: Rp ${CurrencyFormatter(BigDeciToString(summary?.Accumulate_10.toString()))}\n" +
												"November: Rp ${CurrencyFormatter(BigDeciToString(summary?.Accumulate_11.toString()))}\n" +
												"Desember: Rp ${CurrencyFormatter(BigDeciToString(summary?.Accumulate_12.toString()))}\n",
										fontSize = 12.sp,
										modifier = Modifier.padding(top = 8.dp)
									)
								}
							}
							Image(
								painterResource(Res.drawable.Icon_Dropdown_Arrow),
								contentDescription = null,
								modifier = Modifier.size(16.dp).clickable(true, onClick = {
									accumulateDetails = !accumulateDetails
								}).rotate(if(accumulateDetails) 180f else 0f)
							)
						}
					}
					divider(0.dp)
					//c. Jumlah Bruto Tidak Kena Pajak
					Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
						Text(
							text = "c.",
							fontSize = 14.sp,
							fontWeight = FontWeight.Bold,
							modifier = Modifier.padding(end = 4.dp)
						)
						Row(
							modifier = Modifier.fillMaxWidth(),
							horizontalArrangement = Arrangement.SpaceBetween,
							verticalAlignment = Alignment.Top
						) {
							Column {
								Text(
									text = "Jumlah Bruto Tidak Kena Pajak",
									fontSize = 14.sp,
									fontWeight = FontWeight.Bold,
									modifier = Modifier.padding(end = 8.dp)
								)
								
								Text(
									text = "Total: Rp 500,000,000",
									fontSize = 12.sp,
									modifier = Modifier.padding(top = 8.dp)
								)
							}
						}
					}
					divider(0.dp)
					//d. Peredaran Bruto Kena Pajak
					Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
						Text(
							text = "d.",
							fontSize = 14.sp,
							fontWeight = FontWeight.Bold,
							modifier = Modifier.padding(end = 4.dp)
						)
						Row(
							modifier = Modifier.fillMaxWidth(),
							horizontalArrangement = Arrangement.SpaceBetween,
							verticalAlignment = Alignment.Top
						) {
							Column {
								Text(
									text = "Peredaran Bruto Kena Pajak",
									fontSize = 14.sp,
									fontWeight = FontWeight.Bold,
									modifier = Modifier.padding(end = 8.dp)
								)
								
								if(pkpDetails) {
									Text(
										text = "Januari: Rp ${CurrencyFormatter(BigDeciToString(summary?.PKP_1.toString()))}\n" +
												"Februari: Rp ${CurrencyFormatter(BigDeciToString(summary?.PKP_2.toString()))}\n" +
												"Maret: Rp ${CurrencyFormatter(BigDeciToString(summary?.PKP_3.toString()))}\n" +
												"April: Rp ${CurrencyFormatter(BigDeciToString(summary?.PKP_4.toString()))}\n" +
												"Mei: Rp ${CurrencyFormatter(BigDeciToString(summary?.PKP_5.toString()))}\n" +
												"Juni: Rp ${CurrencyFormatter(BigDeciToString(summary?.PKP_6.toString()))}\n" +
												"Juli: Rp ${CurrencyFormatter(BigDeciToString(summary?.PKP_7.toString()))}\n" +
												"Agustus: Rp ${CurrencyFormatter(BigDeciToString(summary?.PKP_8.toString()))}\n" +
												"September: Rp ${CurrencyFormatter(BigDeciToString(summary?.PKP_9.toString()))}\n" +
												"Oktober: Rp ${CurrencyFormatter(BigDeciToString(summary?.PKP_10.toString()))}\n" +
												"November: Rp ${CurrencyFormatter(BigDeciToString(summary?.PKP_11.toString()))}\n" +
												"Desember: Rp ${CurrencyFormatter(BigDeciToString(summary?.PKP_12.toString()))}\n",
										fontSize = 12.sp,
										modifier = Modifier.padding(top = 8.dp)
									)
								}
								
								Text(
									text = "Total: Rp ${CurrencyFormatter(BigDeciToString(summary?.PKPTotal.toString()))}",
									fontSize = 12.sp,
									modifier = Modifier.padding(top = 8.dp)
								)
							}
							Image(
								painterResource(Res.drawable.Icon_Dropdown_Arrow),
								contentDescription = null,
								modifier = Modifier.size(16.dp).clickable(true, onClick = {
									pkpDetails = !pkpDetails
								}).rotate(if(pkpDetails) 180f else 0f)
							)
						}
					}
					divider(0.dp)
					//e. Jumlah PPh Final Terutang
					Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
						Text(
							text = "e.",
							fontSize = 14.sp,
							fontWeight = FontWeight.Bold,
							modifier = Modifier.padding(end = 4.dp)
						)
						Row(
							modifier = Modifier.fillMaxWidth(),
							horizontalArrangement = Arrangement.SpaceBetween,
							verticalAlignment = Alignment.Top
						) {
							Column {
								Text(
									text = "Jumlah PPh Final Terutang",
									fontSize = 14.sp,
									fontWeight = FontWeight.Bold,
									modifier = Modifier.padding(end = 8.dp)
								)
								
								if(pphFinalDetails) {
									Text(
										text = "Januari: Rp ${CurrencyFormatter(BigDeciToString(summary?.PPhFinal_1.toString()))}\n" +
												"Februari: Rp ${CurrencyFormatter(BigDeciToString(summary?.PPhFinal_2.toString()))}\n" +
												"Maret: Rp ${CurrencyFormatter(BigDeciToString(summary?.PPhFinal_3.toString()))}\n" +
												"April: Rp ${CurrencyFormatter(BigDeciToString(summary?.PPhFinal_4.toString()))}\n" +
												"Mei: Rp ${CurrencyFormatter(BigDeciToString(summary?.PPhFinal_5.toString()))}\n" +
												"Juni: Rp ${CurrencyFormatter(BigDeciToString(summary?.PPhFinal_6.toString()))}\n" +
												"Juli: Rp ${CurrencyFormatter(BigDeciToString(summary?.PPhFinal_7.toString()))}\n" +
												"Agustus: Rp ${CurrencyFormatter(BigDeciToString(summary?.PPhFinal_8.toString()))}\n" +
												"September: Rp ${CurrencyFormatter(BigDeciToString(summary?.PPhFinal_9.toString()))}\n" +
												"Oktober: Rp ${CurrencyFormatter(BigDeciToString(summary?.PPhFinal_10.toString()))}\n" +
												"November: Rp ${CurrencyFormatter(BigDeciToString(summary?.PPhFinal_11.toString()))}\n" +
												"Desember: Rp ${CurrencyFormatter(BigDeciToString(summary?.PPhFinal_12.toString()))}\n",
										fontSize = 12.sp,
										modifier = Modifier.padding(top = 8.dp)
									)
								}
								
								Text(
									text = "Total: Rp ${CurrencyFormatter(BigDeciToString(summary?.PPhFinalTotal.toString()))}",
									fontSize = 12.sp,
									modifier = Modifier.padding(top = 8.dp)
								)
							}
							Image(
								painterResource(Res.drawable.Icon_Dropdown_Arrow),
								contentDescription = null,
								modifier = Modifier.size(16.dp).clickable(true, onClick = {
									pphFinalDetails = !pphFinalDetails
								}).rotate(if(pphFinalDetails) 180f else 0f)
							)
						}
					}
					divider(0.dp)
					//f. PPh Final Disetor Sendiri
					Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
						Text(
							text = "f.",
							fontSize = 14.sp,
							fontWeight = FontWeight.Bold,
							modifier = Modifier.padding(end = 4.dp)
						)
						Row(
							modifier = Modifier.fillMaxWidth(),
							horizontalArrangement = Arrangement.SpaceBetween,
							verticalAlignment = Alignment.Top
						) {
							Column {
								Text(
									text = "PPh Final Disetor Sendiri",
									fontSize = 14.sp,
									fontWeight = FontWeight.Bold,
									modifier = Modifier.padding(end = 8.dp)
								)
								
								if(pphSelfPaidDetails) {
									Text(
										text = "Januari: Rp ${CurrencyFormatter(BigDeciToString(summary?.PPhSelfPaid_1.toString()))}\n" +
												"Februari: Rp ${CurrencyFormatter(BigDeciToString(summary?.PPhSelfPaid_2.toString()))}\n" +
												"Maret: Rp ${CurrencyFormatter(BigDeciToString(summary?.PPhSelfPaid_3.toString()))}\n" +
												"April: Rp ${CurrencyFormatter(BigDeciToString(summary?.PPhSelfPaid_4.toString()))}\n" +
												"Mei: Rp ${CurrencyFormatter(BigDeciToString(summary?.PPhSelfPaid_5.toString()))}\n" +
												"Juni: Rp ${CurrencyFormatter(BigDeciToString(summary?.PPhSelfPaid_6.toString()))}\n" +
												"Juli: Rp ${CurrencyFormatter(BigDeciToString(summary?.PPhSelfPaid_7.toString()))}\n" +
												"Agustus: Rp ${CurrencyFormatter(BigDeciToString(summary?.PPhSelfPaid_8.toString()))}\n" +
												"September: Rp ${CurrencyFormatter(BigDeciToString(summary?.PPhSelfPaid_9.toString()))}\n" +
												"Oktober: Rp ${CurrencyFormatter(BigDeciToString(summary?.PPhSelfPaid_10.toString()))}\n" +
												"November: Rp ${CurrencyFormatter(BigDeciToString(summary?.PPhSelfPaid_11.toString()))}\n" +
												"Desember: Rp ${CurrencyFormatter(BigDeciToString(summary?.PPhSelfPaid_12.toString()))}\n",
										fontSize = 12.sp,
										modifier = Modifier.padding(top = 8.dp)
									)
								}
								
								Text(
									text = "Total: Rp ${CurrencyFormatter(BigDeciToString(summary?.PPhSelfPaidTotal.toString()))}",
									fontSize = 12.sp,
									modifier = Modifier.padding(top = 8.dp)
								)
							}
							Image(
								painterResource(Res.drawable.Icon_Dropdown_Arrow),
								contentDescription = null,
								modifier = Modifier.size(16.dp).clickable(true, onClick = {
									pphSelfPaidDetails = !pphSelfPaidDetails
								}).rotate(if(pphSelfPaidDetails) 180f else 0f)
							)
						}
					}
					divider(0.dp)
					//g. Jumlah PPh Final Yang Dipotong Pihak Lain
					Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
						Text(
							text = "g.",
							fontSize = 14.sp,
							fontWeight = FontWeight.Bold,
							modifier = Modifier.padding(end = 4.dp)
						)
						Row(
							modifier = Modifier.fillMaxWidth(),
							horizontalArrangement = Arrangement.SpaceBetween,
							verticalAlignment = Alignment.Top
						) {
							Column {
								Text(
									text = "Jumlah PPh Final Yang Dipotong Pihak Lain",
									fontSize = 14.sp,
									fontWeight = FontWeight.Bold,
									modifier = Modifier.padding(end = 8.dp)
								)
								
								if(pphDeductedDetails) {
									Text(
										text = "Januari: Rp ${CurrencyFormatter(BigDeciToString(summary?.PPhDeducted_1.toString()))}\n" +
												"Februari: Rp ${CurrencyFormatter(BigDeciToString(summary?.PPhDeducted_2.toString()))}\n" +
												"Maret: Rp ${CurrencyFormatter(BigDeciToString(summary?.PPhDeducted_3.toString()))}\n" +
												"April: Rp ${CurrencyFormatter(BigDeciToString(summary?.PPhDeducted_4.toString()))}\n" +
												"Mei: Rp ${CurrencyFormatter(BigDeciToString(summary?.PPhDeducted_5.toString()))}\n" +
												"Juni: Rp ${CurrencyFormatter(BigDeciToString(summary?.PPhDeducted_6.toString()))}\n" +
												"Juli: Rp ${CurrencyFormatter(BigDeciToString(summary?.PPhDeducted_7.toString()))}\n" +
												"Agustus: Rp ${CurrencyFormatter(BigDeciToString(summary?.PPhDeducted_8.toString()))}\n" +
												"September: Rp ${CurrencyFormatter(BigDeciToString(summary?.PPhDeducted_9.toString()))}\n" +
												"Oktober: Rp ${CurrencyFormatter(BigDeciToString(summary?.PPhDeducted_10.toString()))}\n" +
												"November: Rp ${CurrencyFormatter(BigDeciToString(summary?.PPhDeducted_11.toString()))}\n" +
												"Desember: Rp ${CurrencyFormatter(BigDeciToString(summary?.PPhDeducted_12.toString()))}\n",
										fontSize = 12.sp,
										modifier = Modifier.padding(top = 8.dp)
									)
								}
								
								Text(
									text = "Total: Rp ${CurrencyFormatter(BigDeciToString(summary?.PPhDeductedTotal.toString()))}",
									fontSize = 12.sp,
									modifier = Modifier.padding(top = 8.dp)
								)
							}
							Image(
								painterResource(Res.drawable.Icon_Dropdown_Arrow),
								contentDescription = null,
								modifier = Modifier.size(16.dp).clickable(true, onClick = {
									pphDeductedDetails = !pphDeductedDetails
								}).rotate(if(pphDeductedDetails) 180f else 0f)
							)
						}
					}
					divider(0.dp)
					//h. Selisih
					Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
						Text(
							text = "h.",
							fontSize = 14.sp,
							fontWeight = FontWeight.Bold,
							modifier = Modifier.padding(end = 4.dp)
						)
						Row(
							modifier = Modifier.fillMaxWidth(),
							horizontalArrangement = Arrangement.SpaceBetween,
							verticalAlignment = Alignment.Top
						) {
							Column {
								Text(
									text = "Selisih",
									fontSize = 14.sp,
									fontWeight = FontWeight.Bold,
									modifier = Modifier.padding(end = 8.dp)
								)
								
								if(pphDifferenceDetails) {
									Text(
										text = "Januari: Rp ${CurrencyFormatter(BigDeciToString(summary?.PPhDifference_1.toString()))}\n" +
												"Februari: Rp ${CurrencyFormatter(BigDeciToString(summary?.PPhDifference_2.toString()))}\n" +
												"Maret: Rp ${CurrencyFormatter(BigDeciToString(summary?.PPhDifference_3.toString()))}\n" +
												"April: Rp ${CurrencyFormatter(BigDeciToString(summary?.PPhDifference_4.toString()))}\n" +
												"Mei: Rp ${CurrencyFormatter(BigDeciToString(summary?.PPhDifference_5.toString()))}\n" +
												"Juni: Rp ${CurrencyFormatter(BigDeciToString(summary?.PPhDifference_6.toString()))}\n" +
												"Juli: Rp ${CurrencyFormatter(BigDeciToString(summary?.PPhDifference_7.toString()))}\n" +
												"Agustus: Rp ${CurrencyFormatter(BigDeciToString(summary?.PPhDifference_8.toString()))}\n" +
												"September: Rp ${CurrencyFormatter(BigDeciToString(summary?.PPhDifference_9.toString()))}\n" +
												"Oktober: Rp ${CurrencyFormatter(BigDeciToString(summary?.PPhDifference_10.toString()))}\n" +
												"November: Rp ${CurrencyFormatter(BigDeciToString(summary?.PPhDifference_11.toString()))}\n" +
												"Desember: Rp ${CurrencyFormatter(BigDeciToString(summary?.PPhDifference_12.toString()))}\n",
										fontSize = 12.sp,
										modifier = Modifier.padding(top = 8.dp)
									)
								}
								
								Text(
									text = "Total: Rp ${CurrencyFormatter(BigDeciToString(summary?.PPhDifferenceTotal.toString()))}",
									fontSize = 12.sp,
									modifier = Modifier.padding(top = 8.dp)
								)
							}
							Image(
								painterResource(Res.drawable.Icon_Dropdown_Arrow),
								contentDescription = null,
								modifier = Modifier.size(16.dp).clickable(true, onClick = {
									pphDifferenceDetails = !pphDifferenceDetails
								}).rotate(if(pphDifferenceDetails) 180f else 0f)
							)
						}
					}
				}
			}
			
			//Continue Button
			item {
				Button(
					enabled = true,
					modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 22.dp),
					colors = buttonColors(backgroundColor = Colors().buttonActive, contentColor = Color.White),
					onClick = {
						navigator.push(BrutoRecapEditScreen(summary, client, sptPertamaClient, prefs))
					}
				) {
					Text(
						modifier = Modifier.padding(vertical = 6.dp),
						text = "Lanjut",
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