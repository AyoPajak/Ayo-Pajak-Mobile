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
import models.transaction.FormIncomeNetJobResponseApiModel
import models.transaction.FormTaxCreditResponseApiModel
import util.BigDeciToString
import util.CurrencyFormatter
import util.DateFormatter

class SptStepEightScreen(val sptHd: Form1770HdResponseApiModel?, val client: Account, val sptPertamaClient: Interfaces, val prefs: DataStore<Preferences>): Screen {
	
	val sptManager = SPTManager(prefs, client, sptPertamaClient)
	
	@Composable
	override fun Content() {
		
		val scope = rememberCoroutineScope()
		val navigator = LocalNavigator.currentOrThrow
		
		var incomeList by remember { mutableStateOf<List<FormIncomeNetJobResponseApiModel>>(emptyList()) }
		
		var isReady by remember { mutableStateOf(false) }
		
		LaunchedEffect(null) {
			incomeList = sptManager.getIncomeNetJobData(scope, sptHd!!.Id.toString())
			
			isReady = true
		}
		
		@Composable
		fun incomeCard(data: FormIncomeNetJobResponseApiModel) {
			Box(
				modifier = Modifier
					.fillMaxWidth()
					.padding(horizontal = 16.dp)
					.padding(bottom = 8.dp)
					.clip(RoundedCornerShape(8.dp))
					.border(1.dp, Colors().slate20, RoundedCornerShape(8.dp))
					.background(Colors().panel)
					.clickable(true, onClick = {
						navigator.push(IncomeNetJobFormScreen(data.Id, sptHd, client, sptPertamaClient, prefs))
					})
			) {
				Column(
					modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 24.dp)
				) {
					Text(
						text = data.EmployerName,
						fontSize = 12.sp,
						fontWeight = FontWeight.Bold,
						modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
					)
					
					Text(
						text = data.EmployerNPWP,
						fontSize = 10.sp,
						color = Colors().textDarkGrey,
						modifier = Modifier.padding(bottom = 16.dp)
					)
					
					Row (
						modifier = Modifier.fillMaxWidth(),
						horizontalArrangement = Arrangement.SpaceBetween,
						verticalAlignment = Alignment.CenterVertically
					) {
						Text(
							text = "Jumlah Bruto",
							fontSize = 10.sp,
							color = Colors().textDarkGrey
						)
						Text(
							text = "Rp ${CurrencyFormatter(BigDeciToString(data.GrossIncomeIDR.toString()))}",
							fontSize = 14.sp,
							color = if(data.GrossIncomeIDR == 0.0) Colors().textBlack else Colors().textGreen,
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
				item { topBar("Penghasilan Neto Dalam Negeri (Pekerjaan)") }
				
				//New Income
				item {
					Box(
						modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 24.dp)
							.background(Colors().panel)
							.clip(RoundedCornerShape(4.dp))
							.border(1.dp, Colors().brandDark40, RoundedCornerShape(4.dp))
							.clickable(true, onClick = {
								navigator.push(IncomeNetJobFormScreen(0, sptHd, client, sptPertamaClient, prefs))
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
				
				//SubTitle Row
				item {
					Row(
						modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 16.dp),
						horizontalArrangement = Arrangement.SpaceBetween
					) {
						Text(
							text = "Daftar Penghasilan",
							fontSize = 16.sp,
							fontWeight = FontWeight.Bold,
							color = Colors().slate70
						)
					}
				}
				
				//Income List
				incomeList.forEach { item { incomeCard(it) } }
				
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