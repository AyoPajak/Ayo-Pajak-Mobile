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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import ayopajakmobile.composeapp.generated.resources.Icon_Dropdown_Arrow
import ayopajakmobile.composeapp.generated.resources.Res
import ayopajakmobile.composeapp.generated.resources.arrow_back
import ayopajakmobile.composeapp.generated.resources.icon_tripledot_black
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import global.Colors
import global.FinalIncomeType
import global.NonTaxedIncomeType
import global.universalUIComponents.loadingPopupBox
import global.universalUIComponents.popUpBox
import http.Account
import http.Interfaces
import kotlinx.coroutines.launch
import models.transaction.Form1770HdResponseApiModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource

class NonTaxedIncomeFormScreen(val id: Int, val sptHd: Form1770HdResponseApiModel?, val client: Account, val sptPertamaClient: Interfaces, val prefs: DataStore<Preferences>): Screen {
	
	val sptManager = SPTManager(prefs, client, sptPertamaClient)
	
	@Composable
	override fun Content() {
		
		val scope = rememberCoroutineScope()
		val navigator = LocalNavigator.currentOrThrow
		
		var showDeletePopup by remember { mutableStateOf(false) }
		var showConfirmPopup by remember { mutableStateOf(false) }
		var showIncomeTypePopup by remember { mutableStateOf(false) }
		
		
		var incomeTypeE by remember { mutableStateOf(0) }
		var selectedIncomeType by remember { mutableStateOf("") }
		
		var isReady by remember { mutableStateOf(false) }
		
		LaunchedEffect(null) {
			
			val oldData = sptManager.getNonTaxedIncomeDataById(scope, id.toString())
			
			if (oldData != null) {
				selectedIncomeType = NonTaxedIncomeType.fromValue(oldData.IncomeTypeE) ?: ""
				incomeTypeE = oldData.IncomeTypeE
			}
			
			println(oldData)
			
			isReady = true
		}
		
		@Composable
		fun incomeTypeSelector() {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp),
				text = "Jenis Penghasilan",
				fontSize = 12.sp,
				color = Color.Black,
				fontWeight = FontWeight.Bold
			)
			
			Box(
				modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
					.background(if(id == 0) Color.White else Colors().slate20, RoundedCornerShape(4.dp))
					.border(1.dp, Colors().textDarkGrey, RoundedCornerShape(4.dp))
					.clickable(id == 0, onClick = {
						showIncomeTypePopup = true
					})
			) {
				Row (
					modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
					horizontalArrangement = Arrangement.SpaceBetween,
					verticalAlignment = Alignment.CenterVertically
				){
					Text(
						text = selectedIncomeType.ifBlank { "Pilih Jenis Penghasilan" },
						fontSize = 14.sp,
						color = if(selectedIncomeType == "" || id != 0) Colors().textDarkGrey else Colors().textBlack
					)
					Image(painterResource(Res.drawable.Icon_Dropdown_Arrow), null, modifier = Modifier.size(24.dp))
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
				item {
					Row(
						modifier = Modifier.fillMaxWidth(),
						verticalAlignment = Alignment.CenterVertically
					) {
						IconButton(
							modifier = Modifier
								.padding(horizontal = 16.dp)
								.padding(vertical = 24.dp)
								.clip(CircleShape)
								.border(1.dp, Color.LightGray, CircleShape)
								.background(Colors().panel)
								.align(Alignment.CenterVertically),
							onClick = {
								navigator.pop()
							}
						) {
							Icon(
								modifier = Modifier.scale(1.2f),
								imageVector = vectorResource(Res.drawable.arrow_back), contentDescription = null
							)
						}
						
						Row(
							modifier = Modifier.fillMaxWidth(),
							horizontalArrangement = Arrangement.SpaceBetween
						) {
							Text(
								text = if (id == 0) "Tambah Penghasilan" else "Edit Penghasilan",
								fontWeight = FontWeight.Bold,
								fontSize = 16.sp,
								color = Colors().textBlack,
								modifier = Modifier.align(Alignment.CenterVertically)
							)
							
							if (id != 0) {
								Image(
									painter = painterResource(Res.drawable.icon_tripledot_black),
									null,
									modifier = Modifier
										.padding(end = 16.dp)
										.clickable(true, onClick = {
											showDeletePopup = true
										})
								)
							}
						}
					}
				}
				
				//Income Type
				item { incomeTypeSelector() }
			}
		}
		
		//Popups
		//Income Type Popup
		popUpBox(
			showPopup = showIncomeTypePopup,
			onClickOutside = { showIncomeTypePopup = false },
			content = {
				Column(
					modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 12.dp)
				) {
					Text(
						text = "Jenis Penghasilan",
						fontSize = 12.sp,
						color = Colors().textBlack,
						fontWeight = FontWeight.Bold,
						modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
					)
					LazyColumn(modifier = Modifier.heightIn(max = 200.dp).padding(horizontal = 18.dp)) {
						NonTaxedIncomeType.entries.forEach {
							item {
								Text(
									text = NonTaxedIncomeType.fromValue(it.value).toString(),
									fontSize = 18.sp,
									modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
										.clickable(true, onClick = {
											selectedIncomeType = NonTaxedIncomeType.fromValue(it.value).toString()
											incomeTypeE = it.value
//											formType = FinalIncomeType.formTypeFromValue(it.value).toString()
//											if( formType == "A" || formType == "C" || formType == "E" ) selectedAssetList = getFilteredAsset()
//											if(sptHd?.TaxYear?.toInt()!! >= 2023 && incomeTypeE == "160") navigator.push(FinalIncomeUMKMScreen(sptHd, client, sptPertamaClient, prefs))
											showIncomeTypePopup = false
										})
								)
							}
						}
					}
				}
			}
		)
		
		//Delete Non Taxed Income Popup
		popUpBox(
			showPopup = showDeletePopup,
			onClickOutside = { showDeletePopup = false },
			content = {
				Text(
					text = "Hapus Penghasilan",
					fontSize = 16.sp,
					color = Colors().textRed,
					modifier = Modifier.padding(vertical = 24.dp)
						.clickable(true, onClick = {
							showDeletePopup = false
							showConfirmPopup = true
						})
				)
			}
		)
		
		//Confirm Delete Popup
		popUpBox(
			showPopup = showConfirmPopup,
			onClickOutside = { showConfirmPopup = false },
			content = {
				Column(horizontalAlignment = Alignment.CenterHorizontally) {
					Text(
						modifier = Modifier.padding(vertical = 24.dp),
						text = "Hapus Penghasilan",
						fontSize = 18.sp,
						color = Colors().textBlack,
						fontWeight = FontWeight.Bold
					)
					
					Text(
						modifier = Modifier.padding(vertical = 24.dp, horizontal = 16.dp),
						text = "Apakah Anda yakin ingin menghapus Penghasilan ini?",
						fontSize = 16.sp,
						textAlign = TextAlign.Center,
						color = Colors().textBlack
					)
					
					Text(
						text = "Hapus",
						fontSize = 14.sp,
						color = Colors().textRed,
						modifier = Modifier.padding(vertical = 24.dp)
							.clickable(true, onClick = {
								scope.launch{
									isReady = false
//									sptManager.deleteIncome(scope, id.toString())
									navigator.pop()
								}
							})
					)
					
					Box(
						modifier = Modifier
							.fillMaxWidth()
							.padding(horizontal = 16.dp)
							.height(1.dp)
							.background(Colors().slate30)
					)
					
					Text(
						text = "Kembali",
						fontSize = 14.sp,
						color = Colors().textBlack,
						modifier = Modifier.padding(vertical = 24.dp)
							.clickable(true, onClick = {
								showConfirmPopup = false
							})
					)
				}
			}
		)
	}
}