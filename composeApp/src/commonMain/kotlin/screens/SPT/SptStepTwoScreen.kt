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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
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
import models.transaction.FormDependentResponseApiModel

class SptStepTwoScreen(val sptHd: Form1770HdResponseApiModel?, val client: Account, val sptPertamaClient: Interfaces, val prefs: DataStore<Preferences>): Screen {
	
	val sptManager = SPTManager(prefs, client, sptPertamaClient)
	
	@Composable
	override fun Content() {
		
		val scope = rememberCoroutineScope()
		val navigator = LocalNavigator.currentOrThrow
		
		var dependentList by remember { mutableStateOf<List<FormDependentResponseApiModel>?>(emptyList()) }
		var dependentCount by remember { mutableStateOf(0) }
		
		var isReady by remember { mutableStateOf(false) }
		
		LaunchedEffect(null) {
			dependentList = sptManager.getDependentList(scope, sptHd!!.Id, ApiODataQueryModel())
			dependentCount = dependentList?.size ?: 0
			isReady = true
		}
		
		@Composable
		fun dependentCard(number: Int, dependentName: String, dependentRelation: String, dependentId: Int, lastSeq: Int) {
			Box(
				modifier = Modifier
					.fillMaxWidth()
					.padding(horizontal = 16.dp)
					.padding(bottom = 12.dp)
					.shadow(1.dp, RoundedCornerShape(8.dp))
					.clip(RoundedCornerShape(8.dp))
					.background(Color.White)
					.clickable(true, onClick = {
						navigator.push(DependentFormScreen(dependentId, sptHd, client, sptPertamaClient, prefs, lastSeq))
					})
			) {
				Row(
					modifier = Modifier.fillMaxWidth().padding(16.dp),
					verticalAlignment = Alignment.CenterVertically
				){
					Box(
						modifier = Modifier
							.padding(end = 16.dp)
							.size(48.dp)
							.clip(CircleShape)
							.background(Colors().brandDark5)
					) {
						Text(
							text = "$number",
							fontSize = 16.sp,
							fontWeight = FontWeight.Bold,
							color = Colors().brandDark40,
							modifier = Modifier.align(Alignment.Center)
						)
					}
					
					Column {
						Text(
							text = dependentName,
							fontSize = 14.sp,
							fontWeight = FontWeight.Bold,
							modifier = Modifier.padding(bottom = 8.dp)
						)
						Text(
							text = dependentRelation,
							fontSize = 10.sp,
							color = Colors().textDarkGrey
						)
					}
				}
			}
		}
		
		loadingPopupBox(!isReady)
		
		Column(
			modifier = Modifier.fillMaxSize().background(Colors().panel)
		) {
			topBar("Tanggungan")
			
			Box(
				modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 8.dp)
					.background(Colors().panel)
					.clip(RoundedCornerShape(4.dp))
					.border(1.dp, Colors().brandDark40, RoundedCornerShape(4.dp))
					.clickable(true, onClick = {
						navigator.push(DependentFormScreen(0, sptHd, client, sptPertamaClient, prefs, dependentCount))
					})
			) {
				Text(
					text = "+ Tambah Tanggungan",
					color = Colors().brandDark40,
					fontWeight = FontWeight.Bold,
					fontSize = 14.sp,
					modifier = Modifier.fillMaxWidth().padding(vertical = 18.dp),
					textAlign = TextAlign.Center
				)
			}
			
			Text(
				text = "Daftar Tanggungan",
				color = Colors().slate70,
				fontSize = 16.sp,
				fontWeight = FontWeight.Bold,
				modifier = Modifier.padding(16.dp)
			)
			
			LazyColumn {
				dependentList?.forEach {
					item { dependentCard(it.Seq, it.DependentName, it.FamilyRel.FamilyRelName, it.Id, it.Seq) }
				}
			}
		}
	}
}