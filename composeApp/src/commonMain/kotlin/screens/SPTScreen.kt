package screens

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.Divider
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
import ayopajakmobile.composeapp.generated.resources.Res
import ayopajakmobile.composeapp.generated.resources.icon_notification_border
import ayopajakmobile.composeapp.generated.resources.icon_tripledot_black
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import global.Colors
import global.universalUIComponents.popUpBox
import global.universalUIComponents.topBar
import http.Account
import http.Interfaces
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import models.ApiODataQueryModel
import models.transaction.Form1770HdResponseApiModel
import org.jetbrains.compose.resources.painterResource
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SPTScreen(val client: Account, val sptPertamaClient: Interfaces, val prefs: DataStore<Preferences>): Screen {
	
	val sptManager = SPTManager(prefs, client, sptPertamaClient)
	
	@Composable
	override fun Content() {
		var dataList by remember { mutableStateOf<List<Form1770HdResponseApiModel>>(emptyList()) }
		
		val scope = rememberCoroutineScope()
		
		val navigator = LocalNavigator.currentOrThrow
		var showTripleDotPopup by remember { mutableStateOf(false) }
		
		LaunchedEffect(dataList){
			try {
				scope.launch {
					dataList = getSPTListByQuery(scope, ApiODataQueryModel(5)).second
				}
			} catch (ex: Exception) {
				print(ex.message)
			}
		}
		
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
//						sptCard("2024", "0", "SPT 1770 S", 0, 100000000, 10800000)
//						sptCard("2023", "1", "SPT 1770 S", 1, 100000000, 10800000)
//						sptCard("2023", "0", "SPT 1770 S", 2, 100000000, 10800000)
//						sptCard("2022", "0", "SPT 1770 S", 3, 100000000, -10800000)
						
						// Display the list of SPT cards if data is available
						if (dataList.isNotEmpty()) {
							dataList.forEach {
								sptCard(it.TaxYear, it.CorrectionSeq.toString(), it.SPTType, 0, 100000000, 10800000)
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
					navigator.push(SPTTipsNTricksScreen(false))
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
								navigator.push(SPTTipsNTricksScreen(true))
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
}

@Composable
fun sptCard(year: String, pembetulan: String, sptType: String, status: Int, value: Long, delta: Long) {
	Box(
		modifier = Modifier
			.fillMaxWidth()
			.padding(horizontal = 16.dp)
			.padding(bottom = 12.dp)
			.shadow(1.dp, RoundedCornerShape(8.dp))
			.clip(RoundedCornerShape(8.dp))
			.background(Color.White)
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
					text = "SPT $year - $pembetulan",
					fontSize = 14.sp,
					color = Color.Black,
					fontWeight = FontWeight.Bold
				)
				
				//TODO("Create Status Chip")
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
					text = "Rp $value",
					fontSize = 10.sp,
					color = Color.Black,
					fontWeight = FontWeight.Bold
				)
				
				Text(
					text = "Rp $delta",
					fontSize = 10.sp,
					color = if(delta >= 0) Colors().textGreen else Colors().textRed,
					fontWeight = FontWeight.Bold
				)
			}
		}
	}
}