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
import ayopajakmobile.composeapp.generated.resources.icon_chevron_right_black
import ayopajakmobile.composeapp.generated.resources.icon_edit
import ayopajakmobile.composeapp.generated.resources.icon_edit_grey
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import global.Colors
import global.universalUIComponents.loadingPopupBox
import global.universalUIComponents.topBar
import http.Account
import http.Interfaces
import models.transaction.AssetGroupResponseApiModel
import models.transaction.Form1770HdResponseApiModel
import models.transaction.FormDebtResponseApiModel
import org.jetbrains.compose.resources.painterResource
import util.BigDeciToString
import util.CurrencyFormatter

class SptStepFourScreen(val sptHd: Form1770HdResponseApiModel?, val client: Account, val sptPertamaClient: Interfaces, val prefs: DataStore<Preferences>): Screen {
	
	val sptManager = SPTManager(prefs, client, sptPertamaClient)
	
	@Composable
	override fun Content() {
		
		val scope = rememberCoroutineScope()
		val navigator = LocalNavigator.currentOrThrow
		
		var totalDebt by remember { mutableStateOf(0.0) }
		var debtListByType by remember { mutableStateOf<List<FormDebtResponseApiModel>>(emptyList()) }
		
		var isReady by remember { mutableStateOf(false) }
		
		LaunchedEffect(null) {
			totalDebt = sptManager.getDebtTotal(scope, sptHd!!.Id.toString())
			debtListByType = sptManager.getDebtData(scope, sptHd.Id.toString())
			println(debtListByType)
			isReady = true
		}
		
		@Composable
		fun debtCard(debtType: String, debtYear: String, outstanding: Double, id: Int) {
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
					modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 24.dp)
				) {
					Row(
						modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
						horizontalArrangement = Arrangement.SpaceBetween
					) {
						Text(
							text = debtType,
							fontSize = 12.sp,
							color = Colors().textBlack,
							modifier = Modifier.weight(0.2f)
						)
						
						Image(
							painterResource(Res.drawable.icon_edit_grey),
							contentDescription = null,
							modifier = Modifier.clickable(true, onClick = {
								navigator.push(DebtFormScreen(id, sptHd, client, sptPertamaClient, prefs))
							})
						)
					}
					Row(
						modifier = Modifier.fillMaxWidth(),
						verticalAlignment = Alignment.CenterVertically,
						horizontalArrangement = Arrangement.SpaceBetween
					) {
						Text(
							text = debtYear,
							fontSize = 10.sp,
							color = Colors().textDarkGrey
						)
						
						Text(
							text = "Rp ${CurrencyFormatter(BigDeciToString(outstanding.toString()))}",
							fontSize = 12.sp,
							fontWeight = FontWeight.Bold,
							color = Colors().textBlack
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
				item { topBar("Utang") }
				
				//Summary Box
				item {
					Box(
						modifier = Modifier
							.fillMaxWidth()
							.padding(horizontal = 16.dp).padding(bottom = 24.dp)
							.clip(RoundedCornerShape(8.dp))
							.background(Colors().redMain50)
					) {
						Column(
							modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 24.dp).padding(top = 16.dp)
						) {
							Text(
								text = "Total Utang",
								fontSize = 10.sp,
								color = Color.White,
								modifier = Modifier.padding(bottom = 8.dp)
							)
							
							Text(
								text = "Rp ${CurrencyFormatter(BigDeciToString(totalDebt.toString()))}",
								fontSize = 16.sp,
								color = Color.White,
								fontWeight = FontWeight.Bold
							)
							
							Box(
								modifier = Modifier
									.fillMaxWidth()
									.padding(top = 32.dp)
									.clip(RoundedCornerShape(4.dp))
									.background(Color(0xffEC506B))
							) {
								Row(
									modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 24.dp),
									verticalAlignment = Alignment.CenterVertically,
									horizontalArrangement = Arrangement.SpaceBetween
								) {
									Text(
										text = "Jumlah Utang",
										fontSize = 10.sp,
										color = Color.White,
									)
									Text(
										text = debtListByType.size.toString(),
										fontSize = 14.sp,
										color = Color.White,
										fontWeight = FontWeight.Bold
									)
								}
							}
						}
					}
				}
				
				//New Asset
				item {
					Box(
						modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 24.dp)
							.background(Colors().panel)
							.clip(RoundedCornerShape(4.dp))
							.border(1.dp, Colors().brandDark40, RoundedCornerShape(4.dp))
							.clickable(true, onClick = {
								navigator.push(DebtFormScreen(0, sptHd, client, sptPertamaClient, prefs))
							})
					) {
						Text(
							text = "+ Tambah Utang",
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
							text = "Daftar Utang",
							fontSize = 16.sp,
							fontWeight = FontWeight.Bold,
							color = Colors().slate70
						)
						
						//TODO("Create Filter List feature")
					}
				}
				
				//List Wealth by Type
				debtListByType.forEach {
					item {
						debtCard(it.DebtType.DebtTypeName, it.DebtYear, it.Outstanding_IDR, it.Id)
					}
				}
				
				//Spacer
				item { Box(modifier = Modifier.fillMaxWidth().height(88.dp)) }
			}
		}
	}
}