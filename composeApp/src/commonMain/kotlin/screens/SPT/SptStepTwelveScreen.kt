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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import ayopajakmobile.composeapp.generated.resources.Res
import ayopajakmobile.composeapp.generated.resources.icon_infocircle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import global.Colors
import global.DepositType
import global.PertamaSptFillingStep
import global.TaxType
import global.universalUIComponents.loadingPopupBox
import global.universalUIComponents.topBar
import http.Account
import http.Interfaces
import kotlinx.coroutines.launch
import models.transaction.Form1770HdResponseApiModel
import models.transaction.FormTaxCreditResponseApiModel
import models.transaction.FormTaxPaymentSlipResponseApiModel
import org.jetbrains.compose.resources.painterResource
import screens.divider
import util.BigDeciToLong
import util.BigDeciToString
import util.CurrencyFormatter
import util.DateFormatter

class SptStepTwelveScreen(val sptHd: Form1770HdResponseApiModel?, val client: Account, val sptPertamaClient: Interfaces, val prefs: DataStore<Preferences>): Screen {
	
	val sptManager = SPTManager(prefs, client, sptPertamaClient)
	
	@Composable
	override fun Content() {
		val scope = rememberCoroutineScope()
		val navigator = LocalNavigator.currentOrThrow
		
		var dataList by remember { mutableStateOf<List<FormTaxPaymentSlipResponseApiModel>>(emptyList()) }
		
		var pphPaid by remember { mutableStateOf(0L) }
		var diff by remember { mutableStateOf(0L) }
		
		var isReady by remember { mutableStateOf(false) }
		
		LaunchedEffect(null) {
			
			dataList = sptManager.getTaxPaymentSlipData(scope, sptHd!!.Id.toString())
			
			dataList.forEach {
				if (it.DepositTypeE == 200) pphPaid += BigDeciToLong(it.SSPAmount.toString())
			}
			
			diff = BigDeciToLong(sptHd.TaxPayable.toString()) + pphPaid
			
			isReady = true
		}
		
		@Composable
		fun sspCard(data: FormTaxPaymentSlipResponseApiModel) {
			Box(
				modifier = Modifier
					.fillMaxWidth()
					.padding(horizontal = 16.dp)
					.padding(bottom = 8.dp)
					.clip(RoundedCornerShape(8.dp))
					.border(1.dp, Colors().slate20, RoundedCornerShape(8.dp))
					.background(Color.White)
					.clickable(true, onClick = {
						navigator.push(TaxPaymentSlipFormScreen(data.Id, sptHd, client, sptPertamaClient, prefs))
					})
			) {
				Column(
					modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 24.dp)
				) {
					Text(
						text = "[${data.DepositTypeE}] ${DepositType.fromValue(data.DepositTypeE)}",
						fontSize = 14.sp,
						fontWeight = FontWeight.Bold,
						modifier = Modifier.padding(bottom = 8.dp)
					)
					
					Text(
						text = DateFormatter(data.SSPDate),
						fontSize = 10.sp,
						color = Colors().textDarkGrey,
						modifier = Modifier.padding(bottom = 16.dp)
					)
					
					Row(
						modifier = Modifier.fillMaxWidth(),
						horizontalArrangement = Arrangement.SpaceBetween
					) {
						Text(
							text = "Penghasilan Neto",
							fontSize = 10.sp,
							color = Colors().textDarkGrey
						)
						
						Text(
							text = "Rp ${CurrencyFormatter(BigDeciToString(data.SSPAmount.toString()))}",
							fontSize = 10.sp,
							color = Colors().textBlack,
							fontWeight = FontWeight.Bold
						)
					}
				}
			}
		}
		
		loadingPopupBox(!isReady)
		
		//Form
		Column(
			modifier = Modifier.fillMaxSize().background(Colors().panel)
		) {
			LazyColumn {
				//Top Bar
				item { topBar("Surat Setoran Pajak (SSP)") }
				
				//Attention Box
				item {
					Box(
						modifier = Modifier
							.fillMaxWidth()
							.padding(horizontal = 16.dp).padding(bottom = 24.dp)
							.clip(RoundedCornerShape(8.dp))
							.background(Color(0xFFEEF4FF))
					) {
						Row(
							modifier = Modifier.fillMaxWidth().padding(16.dp),
							verticalAlignment = Alignment.Top
						) {
							Image(painterResource(Res.drawable.icon_infocircle), null, modifier = Modifier.padding(end = 8.dp))
							Text(
								text = "Anda dapat melewati step ini dan melanjutkan ke-step berikutnya terlebih dahulu",
								fontSize = 12.sp,
								lineHeight = 20.sp,
								color = Colors().brandDark90
							)
						}
					}
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
									text = "Rp ${CurrencyFormatter(BigDeciToString(sptHd!!.TaxPayable.toString()))}",
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
									text = "Rp ${CurrencyFormatter(pphPaid.toString())}",
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
									text = "Rp ${CurrencyFormatter(diff.toString())}",
									fontSize = 12.sp,
									color = Colors().textClickable,
									fontWeight = FontWeight.Bold
								)
							}
						}
					}
				}
				
				//New Income
				item {
					Box(
						modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 24.dp, top = 16.dp)
							.background(Colors().panel)
							.clip(RoundedCornerShape(4.dp))
							.border(1.dp, Colors().brandDark40, RoundedCornerShape(4.dp))
							.clickable(true, onClick = {
								navigator.push(TaxPaymentSlipFormScreen(0, sptHd, client, sptPertamaClient, prefs))
							})
					) {
						Text(
							text = "+ Tambah Penghasilan",
							color = Colors().brandDark40,
							fontWeight = FontWeight.Bold,
							fontSize = 14.sp,
							modifier = Modifier.fillMaxWidth().padding(vertical = 18.dp),
							textAlign = TextAlign.Center
						)
					}
				}
				
				//List
				dataList.forEach {
					item { sspCard(it) }
				}
				
				//Spacer
				item {
					Box(
						modifier = Modifier.height(88.dp)
					)
				}
			}
		}
		
		//Done Button
		Box(
			modifier = Modifier.fillMaxSize().padding(bottom = 88.dp).padding(horizontal = 16.dp),
			contentAlignment = Alignment.BottomCenter
		){
			Box(
				contentAlignment = Alignment.Center,
				modifier = Modifier.fillMaxWidth()
					.clip(RoundedCornerShape(8.dp))
					.background(Colors().brandDark40, RoundedCornerShape(8.dp))
					.clickable(true, onClick = {
						println()
						isReady = false
						scope.launch{
							sptManager.updateStepForm1770(
								scope,
								sptHd!!.Id,
								PertamaSptFillingStep.TaxPaymentSlip.value
							)
							navigator.pop()
						}
					})
			) {
				Text(
					text = "Selesai",
					fontSize = 16.sp,
					color = Color.White,
					modifier = Modifier.padding(vertical = 16.dp)
				)
			}
		}
	}
}