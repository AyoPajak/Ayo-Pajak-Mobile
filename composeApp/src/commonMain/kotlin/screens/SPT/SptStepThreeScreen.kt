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
import ayopajakmobile.composeapp.generated.resources.icon_chevron_right
import ayopajakmobile.composeapp.generated.resources.icon_chevron_right_black
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import global.Colors
import global.universalUIComponents.loadingPopupBox
import global.universalUIComponents.topBar
import http.Account
import http.Interfaces
import models.ApiODataQueryModel
import models.transaction.AssetGroupResponseApiModel
import models.transaction.Form1770HdResponseApiModel
import org.jetbrains.compose.resources.painterResource
import util.BigDeciToString
import util.CurrencyFormatter

class SptStepThreeScreen(val sptHd: Form1770HdResponseApiModel?, val client: Account, val sptPertamaClient: Interfaces, val prefs: DataStore<Preferences>): Screen {
	
	val sptManager = SPTManager(prefs, client, sptPertamaClient)
	
	@Composable
	override fun Content() {
		
		val scope = rememberCoroutineScope()
		val navigator = LocalNavigator.currentOrThrow
		
		var totalWealthAcrossAllType by remember { mutableStateOf(0.0) }
		var wealthCountAcrossAllType by remember { mutableStateOf(0) }
		var wealthListByType by remember { mutableStateOf<List<AssetGroupResponseApiModel>>(emptyList()) }
		
		var isReady by remember { mutableStateOf(false) }
		
		LaunchedEffect(null) {
			totalWealthAcrossAllType = sptManager.getWealthTotal(scope, sptHd!!.Id.toString())
			wealthListByType = sptManager.getWealthSummaryByType(scope, sptHd.Id.toString())
			println(wealthListByType)
			isReady = true
		}
		
		@Composable
		fun wealthCard(assetCode: String, assetName: String, dataCount: Int, total: Double, wealthTypeId: String) {
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
					Row(
						modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
						horizontalArrangement = Arrangement.SpaceBetween
					) {
						Text(
							text = "$assetCode - $assetName",
							fontSize = 12.sp,
							color = Colors().textBlack,
							modifier = Modifier.weight(0.2f)
						)
						Text(
							text = "$dataCount data",
							fontSize = 10.sp,
							fontWeight = FontWeight.Bold,
							color = Colors().textDarkGrey
						)
					}
					Row(
						modifier = Modifier.fillMaxWidth(),
						horizontalArrangement = Arrangement.SpaceBetween
					) {
						Text(
							text = "Rp ${CurrencyFormatter(BigDeciToString(total.toString()))}",
							fontSize = 12.sp,
							fontWeight = FontWeight.Bold,
							color = Colors().textBlack
						)
						Image(
							painterResource(Res.drawable.icon_chevron_right_black),
							contentDescription = null,
							modifier = Modifier.clickable(true, onClick = {
								navigator.push(WealthTypeDetailScreen(sptHd, wealthTypeId, client, sptPertamaClient, prefs, assetCode, assetName))
							})
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
				item { topBar("Harta") }
				
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
							modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp).padding(top = 16.dp)
						) {
							Text(
								text = "Total Harta",
								fontSize = 10.sp,
								color = Color.White,
								modifier = Modifier.padding(bottom = 8.dp)
							)
							Text(
								text = "Rp ${CurrencyFormatter(BigDeciToString(totalWealthAcrossAllType.toString()))}",
								fontSize = 16.sp,
								color = Color.White,
								fontWeight = FontWeight.Bold
							)
							Row(
								modifier = Modifier.padding(top = 32.dp)
							){
								Box(
									modifier = Modifier
										.padding(end = 8.dp)
										.clip(RoundedCornerShape(4.dp))
										.background(Color(0xff598dff))
										.weight(0.5f)
								) {
									Column(
										modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp)
									) {
										Text(
											text = "Jenis Harta",
											fontSize = 10.sp,
											color = Color.White,
											modifier = Modifier.padding(bottom = 8.dp)
										)
										Text(
											text = "${wealthListByType.size}",
											fontSize = 14.sp,
											color = Color.White,
											fontWeight = FontWeight.Bold
										)
									}
								}
								
								Box(
									modifier = Modifier
										.padding(start = 8.dp)
										.clip(RoundedCornerShape(4.dp))
										.background(Color(0xff598dff))
										.weight(0.5f)
								) {
									Column(
										modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp)
									) {
										Text(
											text = "Jumlah Harta",
											fontSize = 10.sp,
											color = Color.White,
											modifier = Modifier.padding(bottom = 8.dp)
										)
										Text(
											text = wealthCountAcrossAllType.toString(),
											fontSize = 14.sp,
											color = Color.White,
											fontWeight = FontWeight.Bold
										)
									}
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
								navigator.push(AssetFormScreen(0, sptHd, client, sptPertamaClient, prefs))
							})
					) {
						Text(
							text = "+ Tambah Harta",
							color = Colors().brandDark40,
							fontWeight = FontWeight.Bold,
							fontSize = 14.sp,
							modifier = Modifier.fillMaxWidth().padding(vertical = 18.dp),
							textAlign = TextAlign.Center
						)
					}
				}
				
				//Import Asset
				item {
					//TODO("Create Import Asset feature")
				}
				
				//SubTitle Row
				item {
					Row(
						modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 16.dp),
						horizontalArrangement = Arrangement.SpaceBetween
					) {
						Text(
							text = "Daftar Harta",
							fontSize = 16.sp,
							fontWeight = FontWeight.Bold,
							color = Colors().slate70
						)
						
						//TODO("Create Filter List feature")
					}
				}
				
				//List Wealth by Type
				wealthListByType.forEach {
					item {
						wealthCard(it.AssetCode, it.AssetName, it.DataCount, it.Total, it.WealthTypeId.toString())
					}
					
					wealthCountAcrossAllType += it.DataCount
				}
				
				//Spacer
				item { Box(modifier = Modifier.fillMaxWidth().height(88.dp)) }
			}
		}
	}
}