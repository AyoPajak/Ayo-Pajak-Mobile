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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults.buttonColors
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import ayopajakmobile.composeapp.generated.resources.Icon_Dropdown_Arrow
import ayopajakmobile.composeapp.generated.resources.Res
import ayopajakmobile.composeapp.generated.resources.icon_edit_grey
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import global.Colors
import global.universalUIComponents.loadingPopupBox
import global.universalUIComponents.topBar
import http.Account
import http.Interfaces
import kotlinx.coroutines.launch
import models.transaction.Form1770FinalIncomeUmkm2023BusinessModel
import models.transaction.Form1770FinalIncomeUmkm2023BusinessRequestModel
import models.transaction.Form1770FinalIncomeUmkm2023SummaryModel
import models.transaction.Form1770HdResponseApiModel
import org.jetbrains.compose.resources.painterResource
import util.BigDeciToString
import util.CurrencyFormatter

class FinalIncomeUMKMScreen(val sptHd: Form1770HdResponseApiModel?, val client: Account, val sptPertamaClient: Interfaces, val prefs: DataStore<Preferences>): Screen {
	
	val sptManager = SPTManager(prefs, client, sptPertamaClient)
	
	@Composable
	override fun Content() {
		
		val scope = rememberCoroutineScope()
		val navigator = LocalNavigator.currentOrThrow
		
		var businessList by remember { mutableStateOf<List<Form1770FinalIncomeUmkm2023BusinessModel>>(emptyList()) }
		var summary by remember { mutableStateOf<Form1770FinalIncomeUmkm2023SummaryModel?>(null) }
		
		var isReady by remember { mutableStateOf(false) }
		
		LaunchedEffect(null) {
			
			val data = sptManager.getFinalIncomeUMKMByHdId(scope, sptHd!!.Id.toString())
			
			if (data != null) {
				businessList = data.BusinessList
				summary = data.Summary
			}
			
			isReady = true
		}
		
		@Composable
		fun businessCard(businessData: Form1770FinalIncomeUmkm2023BusinessModel) {
			Box(
				modifier = Modifier
					.fillMaxWidth()
					.padding(horizontal = 16.dp)
					.padding(bottom = 8.dp)
					.clip(RoundedCornerShape(8.dp))
					.border(1.dp, Colors().slate20, RoundedCornerShape(8.dp))
					.background(Colors().panel)
			) {
				Row(
					modifier = Modifier.fillMaxWidth().padding(16.dp),
					horizontalArrangement = Arrangement.SpaceBetween,
					verticalAlignment = Alignment.CenterVertically
				) {
					Column {
						//Business Name
						Text(
							text = businessData.BusinessName ?: "-",
							fontSize = 14.sp,
							fontWeight = FontWeight.Bold,
							modifier = Modifier.padding(bottom = 8.dp)
						)
						
						//NPWP
						Text(
							text = businessData.NPWP ?: "-",
							fontSize = 10.sp,
							color = Colors().textDarkGrey
						)
					}
					
					Image(
						painterResource(Res.drawable.icon_edit_grey),
						contentDescription = null,
						modifier = Modifier.clickable(true, onClick = {
							navigator.push(FinalIncomeUMKMFormScreen(businessData.Id, businessData, sptHd, client, sptPertamaClient, prefs))
						})
					)
				}
			}
		}
		
		@Composable
		fun brutoCircCard(businessData: Form1770FinalIncomeUmkm2023BusinessModel) {
			
			var brutoDetails by remember { mutableStateOf(false) }
			
			Box(
				modifier = Modifier
					.fillMaxWidth()
					.padding(horizontal = 16.dp)
					.padding(bottom = 8.dp)
					.clip(RoundedCornerShape(8.dp))
					.border(1.dp, Colors().slate20, RoundedCornerShape(8.dp))
					.background(Colors().panel)
			) {
				Column(
					modifier = Modifier.fillMaxWidth().padding(16.dp),
				) {
					Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
						//Business Name
						Text(
							text = businessData.BusinessName ?: "-",
							fontSize = 14.sp,
							fontWeight = FontWeight.Bold,
							modifier = Modifier.padding(bottom = 8.dp)
						)
						
						//Dropdown
						Image(
							painterResource(Res.drawable.Icon_Dropdown_Arrow),
							contentDescription = null,
							modifier = Modifier.size(16.dp).clickable(true, onClick = {
								brutoDetails = !brutoDetails
							}).rotate(if(brutoDetails) 180f else 0f)
						)
					}
					
					//Monthly Details
					if(brutoDetails) {
						Text(
							text = "Januari: Rp ${CurrencyFormatter(BigDeciToString(businessData.Bruto_1.toString()))}\n" +
									"Februari: Rp ${CurrencyFormatter(BigDeciToString(businessData.Bruto_2.toString()))}\n" +
									"Maret: Rp ${CurrencyFormatter(BigDeciToString(businessData.Bruto_3.toString()))}\n" +
									"April: Rp ${CurrencyFormatter(BigDeciToString(businessData.Bruto_4.toString()))}\n" +
									"Mei: Rp ${CurrencyFormatter(BigDeciToString(businessData.Bruto_5.toString()))}\n" +
									"Juni: Rp ${CurrencyFormatter(BigDeciToString(businessData.Bruto_6.toString()))}\n" +
									"Juli: Rp ${CurrencyFormatter(BigDeciToString(businessData.Bruto_7.toString()))}\n" +
									"Agustus: Rp ${CurrencyFormatter(BigDeciToString(businessData.Bruto_8.toString()))}\n" +
									"September: Rp ${CurrencyFormatter(BigDeciToString(businessData.Bruto_9.toString()))}\n" +
									"Oktober: Rp ${CurrencyFormatter(BigDeciToString(businessData.Bruto_10.toString()))}\n" +
									"November: Rp ${CurrencyFormatter(BigDeciToString(businessData.Bruto_11.toString()))}\n" +
									"Desember: Rp ${CurrencyFormatter(BigDeciToString(businessData.Bruto_12.toString()))}",
							fontSize = 12.sp,
							color = Colors().textDarkGrey
						)
					}
					
					//Total
					Text(
						text = "Total: Rp ${CurrencyFormatter(BigDeciToString(businessData.Total.toString()))}",
						fontSize = 14.sp,
						modifier = Modifier.padding(top = 8.dp)
					)
				}
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
				item{ topBar("Rincian") }
				
				//Business List SubTitle Row
				item {
					Row(
						modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(bottom = 16.dp),
						horizontalArrangement = Arrangement.SpaceBetween
					) {
						Text(
							text = "Daftar Usaha",
							fontSize = 16.sp,
							fontWeight = FontWeight.Bold,
							color = Colors().slate70
						)
						
						Text(
							text = "Tambah",
							fontSize = 16.sp,
							fontWeight = FontWeight.Bold,
							color = Colors().textClickable,
							modifier = Modifier.clickable(true, onClick = {
								navigator.push(FinalIncomeUMKMFormScreen(0, null, sptHd, client, sptPertamaClient, prefs))
							})
						)
					}
				}
				
				//Business List
				businessList.forEach {
					item {
						businessCard(it)
					}
				}
				
				//Bruto Circulation SubTitle Row
				item {
					Text(
						text = "Peredaran Bruto",
						fontSize = 16.sp,
						fontWeight = FontWeight.Bold,
						color = Colors().slate70,
						modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(bottom = 16.dp, top = 24.dp)
					)
				}
				
				//Bruto Circulation List
				businessList.forEach {
					item {
						brutoCircCard(it)
					}
				}
				
				//Continue Button
				item {
					Button(
						enabled = true,
						modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 22.dp),
						colors = buttonColors(backgroundColor = Colors().buttonActive, contentColor = Color.White),
						onClick = {
							navigator.push(BrutoRecapScreen(sptHd, client, sptPertamaClient, prefs))
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
}