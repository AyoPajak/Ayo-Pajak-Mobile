package screens.SPT

import SPT.SPTManager
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import global.Colors
import global.universalUIComponents.loadingPopupBox
import global.universalUIComponents.topBar
import http.Account
import http.Interfaces
import models.ApiODataQueryModel
import models.transaction.Form1770HdResponseApiModel

class SptStepThreeScreen(val sptHd: Form1770HdResponseApiModel?, val client: Account, val sptPertamaClient: Interfaces, val prefs: DataStore<Preferences>): Screen {
	
	val sptManager = SPTManager(prefs, client, sptPertamaClient)
	
	@Composable
	override fun Content() {
		
		val scope = rememberCoroutineScope()
		val navigator = LocalNavigator.currentOrThrow
		
		var isReady by remember { mutableStateOf(false) }
		
		LaunchedEffect(null) {
			isReady = true
		}
		
		loadingPopupBox(!isReady)
		
		Column(
			modifier = Modifier.fillMaxSize().background(Colors().panel)
		) {
			topBar("Harta")
			
			//Sumarry Box
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
						text = "525,125,121",
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
									text = "16",
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
									text = "53",
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
			Box(
				modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 8.dp)
					.background(Colors().panel)
					.clip(RoundedCornerShape(4.dp))
					.border(1.dp, Colors().brandDark40, RoundedCornerShape(4.dp))
					.clickable(true, onClick = {
//								navigator.push(DependentFormScreen(0, sptHd, client, sptPertamaClient, prefs, dependentCount))
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
	}
}