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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import ayopajakmobile.composeapp.generated.resources.Res
import ayopajakmobile.composeapp.generated.resources.icon_notification_border
import ayopajakmobile.composeapp.generated.resources.icon_tripledot_black
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import global.Colors
import global.PreferencesKey.Companion.IsSptFirstRun
import global.universalUIComponents.loadingPopupBox
import global.universalUIComponents.popUpBox
import global.universalUIComponents.topBar
import http.Account
import http.Interfaces
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import models.ApiODataQueryModel
import models.ODataQueryOrderDirection
import models.transaction.Form1770HdResponseApiModel
import org.jetbrains.compose.resources.painterResource
import screens.divider
import util.CurrencyFormatter

class SPTScreen(val client: Account, val sptPertamaClient: Interfaces, val prefs: DataStore<Preferences>): Screen {
	
	val sptManager = SPTManager(prefs, client, sptPertamaClient)
	
	@Composable
	override fun Content() {
		var dataList by remember { mutableStateOf<List<Form1770HdResponseApiModel>>(emptyList()) }
		
		val scope = rememberCoroutineScope()
		var isReady by remember { mutableStateOf(false) }
		
		val navigator = LocalNavigator.currentOrThrow
		var showTripleDotPopup by remember { mutableStateOf(false) }
		
		var isSptFirstRun by remember { mutableStateOf(true) }
		
		LaunchedEffect(dataList){
			try {
				scope.launch {
					dataList = getSPTListByQuery(scope, ApiODataQueryModel()).second
					isReady = true
				}
				scope.launch {
					isSptFirstRun = prefs.data.first()[booleanPreferencesKey(IsSptFirstRun)] ?: true
				}
			} catch (ex: Exception) {
				print(ex.message)
			}
		}
		
		loadingPopupBox(!isReady)
		
		Column(
			modifier = Modifier.fillMaxWidth().background(Colors().panel),
		) {
			topBar(
				"e-SPT Orang Pribadi",
				textColor = Color.Black,
				bgColor = Colors().panel,
				buttonColor = Colors().panel
			)
			
			LazyColumn(
				modifier = Modifier.fillMaxWidth().padding(bottom = 88.dp)
			) {
				//Notification
				item{
					Box(
						modifier = Modifier
							.fillMaxWidth()
							.padding(horizontal = 16.dp).padding(top = 2.dp, bottom = 24.dp)
							.shadow(1.dp, RoundedCornerShape(8.dp))
							.clip(RoundedCornerShape(8.dp))
							.border(1.dp, color = Color.White, RoundedCornerShape(8.dp))
							.background(Color.White)
					) {
						Row(
							modifier = Modifier.padding(16.dp),
							verticalAlignment = Alignment.CenterVertically
						) {
							Image(
								painter = painterResource(Res.drawable.icon_notification_border),
								modifier = Modifier.padding(end = 16.dp),
								contentDescription = null,
								colorFilter = ColorFilter.tint(Color.Black)
							)
							
							Column {
								Text(
									modifier = Modifier.padding(bottom = 8.dp),
									text = "Batas pelaporan SPT",
									fontSize = 12.sp,
									color = Color.Black
								)
								
								Text(
									text = "24 Februari 2025",
									fontSize = 12.sp,
									color = Color.Black,
									fontWeight = FontWeight.Bold
								)
							}
						}
					}
				}
				
				//Body
				item {
					Column(
						modifier = Modifier
							.fillMaxSize()
					) {
						Text(
							text = "Daftar SPT",
							modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp, bottom = 24.dp),
							fontSize = 16.sp,
							fontWeight = FontWeight.Bold,
							color = Colors().slate70
						)
						
						// Display the list of SPT cards if data is available
						if (dataList.isNotEmpty()) {
							dataList.forEach {
								sptCard(it.TaxYear, it.CorrectionSeq.toString(), it.SPTType, it.TaxReportStatusE, it.SSPAmount?.toLong() ?: 0, it.TaxPayable?.toLong() ?: 0, it.Id, it.TaxPaymentStateE)
							}
						} else {
							// Optionally display a loading indicator or empty state
							Text(text = "No SPT available", modifier = Modifier.padding(16.dp))
						}
						
						Spacer(modifier = Modifier.padding(bottom = 40.dp))
					}
				}
			}
		}
		
		Box(
			modifier = Modifier.fillMaxSize().padding(top = 36.dp, end = 16.dp),
			contentAlignment = Alignment.TopEnd
		) {
			Image(
				painter = painterResource(Res.drawable.icon_tripledot_black),
				modifier = Modifier
					.clickable(true, onClick = {
						showTripleDotPopup = true
					}),
				contentDescription = null
			)
		}
		
		Box(
			modifier = Modifier.fillMaxSize().padding(bottom = 88.dp).padding(end = 8.dp),
			contentAlignment = Alignment.BottomEnd
		){
			FloatingActionButton(
				onClick = {
					if(isSptFirstRun) navigator.push(SPTTipsNTricksScreen(false, client, prefs, sptPertamaClient))
					else navigator.push(CreateSPTForm(client, sptPertamaClient, prefs))
				},
				modifier = Modifier.size(64.dp).shadow(1.dp, CircleShape),
				shape = CircleShape,
				backgroundColor = Colors().brandDark50
			) {
				Text(
					text = "+",
					color = Color.White,
					fontWeight = FontWeight.Bold,
					fontSize = 24.sp
				)
			}
		}
		
		popUpBox(
			popupWidth = 300f,
			popupHeight = 200f,
			showPopup = showTripleDotPopup,
			onClickOutside = { showTripleDotPopup = false },
			content = {
				Column(
				
				) {
					Text(
						modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
							.clickable(true, onClick = {
								//TODO
							}),
						text = "Laporan",
						fontSize = 14.sp,
						textAlign = TextAlign.Center
					)
					divider(0.dp)
					Text(
						modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
							.clickable(true, onClick = {
								navigator.push(SPTTipsNTricksScreen(true, client, prefs, sptPertamaClient))
							}),
						text = "Tips & Trik",
						fontSize = 14.sp,
						textAlign = TextAlign.Center
					)
					divider(0.dp)
					Text(
						modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
							.clickable(true, onClick = { showTripleDotPopup = false }),
						text = "Batal",
						fontSize = 14.sp,
						textAlign = TextAlign.Center
					)
				}
			}
		)
	}

	private suspend fun getSPTListByQuery(scope: CoroutineScope, query: ApiODataQueryModel) : Pair<Boolean, ArrayList<Form1770HdResponseApiModel>>
	{
		var successCall = true
		var dataList = ArrayList<Form1770HdResponseApiModel>()
		try {
			val sptList = sptManager.getSptHdList(scope, query)
			if (sptList.any()) {
				dataList = sptList as ArrayList<Form1770HdResponseApiModel>
			}
		}
		catch (ex: Exception) {
			successCall = false
			println(ex.message)
		}
		
		return Pair(successCall, dataList)
	}
	
	@Composable
	fun sptCard(year: String, pembetulan: String, sptType: String, status: Int?, ssp: Long, taxPayable: Long, sptHdId: Int, taxPaymentState: Int?) {
		val navigator = LocalNavigator.currentOrThrow
		
		Box(
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 16.dp)
				.padding(bottom = 12.dp)
				.shadow(1.dp, RoundedCornerShape(8.dp))
				.clip(RoundedCornerShape(8.dp))
				.background(Color.White)
				.clickable(true, onClick = {
					navigator.push(SummarySPTScreen(sptHdId, client, sptPertamaClient, prefs))
				})
		) {
			Column(
				modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(vertical = 20.dp)
			) {
				Row(
					modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
					horizontalArrangement = Arrangement.SpaceBetween,
					verticalAlignment = Alignment.CenterVertically
				) {
					Text(
						text = if(pembetulan == "0") "SPT $year - normal" else "SPT $year - $pembetulan",
						fontSize = 14.sp,
						color = Color.Black,
						fontWeight = FontWeight.Bold
					)
					
					//TODO("Create Status Chip")
					when(status) {
						null -> {
							Box(
								modifier = Modifier.clip(CircleShape).background(Color(0xFFFFF7C5), CircleShape)
							) {
								Text(
									text = "Draft",
									fontSize = 10.sp,
									color = Colors().textYellow,
									modifier = Modifier.padding(vertical = 2.dp, horizontal = 8.dp)
								)
							}
						}
						
						3 -> {
							Box(
								modifier = Modifier.clip(CircleShape).background(Colors().slate10, CircleShape)
							) {
								Text(
									text = "Menunggu",
									fontSize = 10.sp,
									color = Colors().textDarkGrey,
									modifier = Modifier.padding(vertical = 2.dp, horizontal = 8.dp)
								)
							}
						}
						
						5 -> {
							Box(
								modifier = Modifier.clip(CircleShape).background(Color(0xFFC5FFE5), CircleShape)
							) {
								Text(
									text = "Berhasil",
									fontSize = 10.sp,
									color = Colors().textGreen,
									modifier = Modifier.padding(vertical = 2.dp, horizontal = 8.dp)
								)
							}
						}
						
						99 -> {
							Box(
								modifier = Modifier.clip(CircleShape).background(Color(0xFFFEE5E7), CircleShape)
							) {
								Text(
									text = "Gagal",
									fontSize = 10.sp,
									color = Colors().textRed,
									modifier = Modifier.padding(vertical = 2.dp, horizontal = 8.dp)
								)
							}
						}
					}
				}
				
				Text(
					modifier = Modifier.padding(bottom = 24.dp),
					text = sptType,
					fontSize = 10.sp,
					color = Colors().textDarkGrey
				)
				
				Row(
					modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
					horizontalArrangement = Arrangement.SpaceBetween,
					verticalAlignment = Alignment.CenterVertically
				) {
					Text(
						text = "Nilai SSP",
						fontSize = 10.sp,
						color = Colors().textDarkGrey
					)
					Text(
						text = "PPH",
						fontSize = 10.sp,
						color = Colors().textDarkGrey
					)
				}
				
				Row(
					modifier = Modifier.fillMaxWidth(),
					horizontalArrangement = Arrangement.SpaceBetween,
					verticalAlignment = Alignment.CenterVertically
				) {
					Text(
						text = "Rp. ${CurrencyFormatter(ssp.toString())}",
						fontSize = 10.sp,
						color = Color.Black,
						fontWeight = FontWeight.Bold
					)
					
					Text(
						text = "Rp ${CurrencyFormatter(taxPayable.toString())}",
						fontSize = 10.sp,
						color = if(taxPaymentState == 3) Colors().textGreen else if(taxPaymentState == 1) Colors().textRed else Color.Black,
						fontWeight = FontWeight.Bold
					)
				}
			}
		}
	}
}