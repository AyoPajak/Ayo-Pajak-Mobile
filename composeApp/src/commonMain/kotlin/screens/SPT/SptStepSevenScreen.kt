package screens.SPT

import SPT.SPTManager
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
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import global.Colors
import global.TaxType
import global.universalUIComponents.loadingPopupBox
import global.universalUIComponents.topBar
import http.Account
import http.Interfaces
import models.transaction.Form1770HdResponseApiModel
import models.transaction.FormTaxCreditResponseApiModel
import util.BigDeciToLong
import util.BigDeciToString
import util.CurrencyFormatter
import util.DateFormatter

class SptStepSevenScreen(val sptHd: Form1770HdResponseApiModel?, val client: Account, val sptPertamaClient: Interfaces, val prefs: DataStore<Preferences>): Screen {
	
	val sptManager = SPTManager(prefs, client, sptPertamaClient)
	
	@Composable
	override fun Content() {
		
		val scope = rememberCoroutineScope()
		val navigator = LocalNavigator.currentOrThrow
		
		var taxCreditTotal by remember { mutableStateOf(0L) }
		var taxCreditList by remember { mutableStateOf<List<FormTaxCreditResponseApiModel>>(emptyList()) }
		
		var isReady by remember { mutableStateOf(false) }
		
		LaunchedEffect(null) {
			taxCreditTotal = BigDeciToLong(sptManager.getTaxCreditTotal(scope, sptHd!!.Id.toString())!!.TaxCreditIDR.toString())
			taxCreditList = sptManager.getTaxCreditData(scope, sptHd.Id.toString())
			
			isReady = true
		}
		
		@Composable
		fun taxCreditCard(data: FormTaxCreditResponseApiModel) {
			Box(
				modifier = Modifier
					.fillMaxWidth()
					.padding(horizontal = 16.dp)
					.padding(bottom = 8.dp)
					.clip(RoundedCornerShape(8.dp))
					.border(1.dp, Colors().slate20, RoundedCornerShape(8.dp))
					.background(Colors().panel)
					.clickable(true, onClick = {
						navigator.push(TaxCreditFormScreen(data.Id, sptHd, client, sptPertamaClient, prefs))
					})
			) {
				Column(
					modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 24.dp)
				) {
					Row(
						modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
						horizontalArrangement = Arrangement.SpaceBetween,
						verticalAlignment = Alignment.Top
					) {
						Text(
							text = "${TaxType.fromValue(data.TaxTypeE)}",
							fontSize = 12.sp,
							fontWeight = FontWeight.Bold
						)
						
						Text(
							text = DateFormatter(data.WithholdingDate),
							fontSize = 10.sp,
							fontWeight = FontWeight.Bold
						)
					}
					
					Text(
						text = data.WithholdingTaxNo,
						fontSize = 10.sp,
						color = Colors().textDarkGrey,
						modifier = Modifier.padding(bottom = 16.dp)
					)
					
					Row (
						modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
						horizontalArrangement = Arrangement.SpaceBetween
					) {
						Text(
							text = "Pemungut Pajak",
							fontSize = 10.sp,
							color = Colors().textDarkGrey
						)
						Text(
							text = "PPh Dipotong",
							fontSize = 10.sp,
							color = Colors().textDarkGrey
						)
					}
					
					Row (
						modifier = Modifier.fillMaxWidth(),
						horizontalArrangement = Arrangement.SpaceBetween
					) {
						Text(
							text = data.WithholderName,
							fontSize = 10.sp,
							fontWeight = FontWeight.Bold
						)
						Text(
							text = "Rp ${CurrencyFormatter(BigDeciToString(data.WithholdingTaxAmountIDR.toString()))}",
							fontSize = 10.sp,
							fontWeight = FontWeight.Bold
						)
					}
				}
			}
		}
		
		loadingPopupBox(!isReady)
		
		Column(
			modifier = Modifier.fillMaxSize().background(Colors().panel)
		) {
			LazyColumn {
				//Top Bar
				item { topBar("Kredit Pajak") }
				
				//Summary Box
				item {
					Box(
						modifier = Modifier
							.fillMaxWidth()
							.padding(horizontal = 16.dp).padding(bottom = 24.dp)
							.clip(RoundedCornerShape(8.dp))
							.background(Colors().brandDark40)
					) {
						Column(
							modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 36.dp)
						) {
							Text(
								text = "Total Kredit Pajak",
								fontSize = 10.sp,
								color = Color.White,
								modifier = Modifier.padding(bottom = 8.dp)
							)
							
							Text(
								text = "Rp ${CurrencyFormatter(BigDeciToString(taxCreditTotal.toString()))}",
								fontSize = 20.sp,
								color = Color.White,
								fontWeight = FontWeight.Bold
							)
						}
					}
				}
				
				//New Tax Credit
				item {
					Box(
						modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 24.dp)
							.background(Colors().panel)
							.clip(RoundedCornerShape(4.dp))
							.border(1.dp, Colors().brandDark40, RoundedCornerShape(4.dp))
							.clickable(true, onClick = {
								navigator.push(TaxCreditFormScreen(0, sptHd, client, sptPertamaClient, prefs))
							})
					) {
						Text(
							text = "+ Tambah Kredit",
							color = Colors().brandDark40,
							fontWeight = FontWeight.Bold,
							fontSize = 14.sp,
							modifier = Modifier.fillMaxWidth().padding(vertical = 18.dp),
							textAlign = TextAlign.Center
						)
					}
				}
				
				//TODO("Import Data")
				
				//SubTitle Row
				item {
					Row(
						modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 16.dp),
						horizontalArrangement = Arrangement.SpaceBetween
					) {
						Text(
							text = "Daftar Kredit Pajak",
							fontSize = 16.sp,
							fontWeight = FontWeight.Bold,
							color = Colors().slate70
						)
					}
				}
				
				//Tax Credit List
				taxCreditList.forEach { item { taxCreditCard(it) } }
				
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