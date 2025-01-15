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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import ayopajakmobile.composeapp.generated.resources.Res
import ayopajakmobile.composeapp.generated.resources.arrow_back
import ayopajakmobile.composeapp.generated.resources.icon_chevron_right
import ayopajakmobile.composeapp.generated.resources.icon_chevron_right_grey
import ayopajakmobile.composeapp.generated.resources.icon_spttick
import ayopajakmobile.composeapp.generated.resources.icon_tripledot_black
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import global.Colors
import global.PertamaSptFillingStep
import global.universalUIComponents.loadingPopupBox
import global.universalUIComponents.popUpBox
import global.universalUIComponents.topBar
import http.Account
import http.Interfaces
import kotlinx.coroutines.launch
import models.transaction.Form1770HdResponseApiModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource
import screens.divider

class SummarySPTScreen(private val id: Int, val client: Account, val sptPertamaClient: Interfaces, val prefs: DataStore<Preferences>): Screen {
	
	private val sptManager = SPTManager(prefs, client, sptPertamaClient)
	
	@Composable
	override fun Content() {
		
		val scope = rememberCoroutineScope()
		val navigator = LocalNavigator.currentOrThrow
		var sptHd by remember {mutableStateOf<Form1770HdResponseApiModel?>(null)}
		
		var isReady by remember { mutableStateOf(false) }
		
		var formType by remember { mutableStateOf("") }
		var taxYear by remember { mutableStateOf("") }
		var correctionSeq by remember { mutableStateOf(0) }
		var status by remember { mutableStateOf(0) }
		
		var showPopup by remember { mutableStateOf(false) }
		var showConfirmPopup by remember { mutableStateOf(false) }
		
		var completedStep by remember { mutableStateOf(0) }
		
		LaunchedEffect(null) {
			scope.launch {
				sptHd = sptManager.getSptHdById(scope, id)
				
				if(sptHd == null) navigator.pop()
				else {
					formType = sptHd!!.SPTType
					taxYear = sptHd!!.TaxYear
					correctionSeq = sptHd!!.CorrectionSeq
					status = sptHd!!.LastStep
					completedStep = when(sptHd!!.LastStep) {
						10 -> { 1 }
						20 -> { 2 }
						30 -> { 3 }
						40 -> { 4 }
						110 -> { 5 }
						120 -> { 6 }
						210 -> { 7 }
						330 -> { 8 }
						340 -> { 9 }
						410 -> { 10 }
						430 -> { 11 }
						450 -> { 12 }
						470 -> { 13 }
						999 -> { 14 }
						
						else -> { 0 }
					}
				}
				
				isReady = true
			}
		}
		
		@Composable
		fun step(number: Int, title: String, status: Boolean = false) {
			Row(
				modifier = Modifier.fillMaxWidth().padding(16.dp)
					.clickable(completedStep + 1 >= number, onClick = {
						when(number) {
							1 -> {
								navigator.push(SptStepOneScreen(sptHd, client, sptPertamaClient, prefs))
							}
							2 -> {
								navigator.push(SptStepTwoScreen(sptHd, client, sptPertamaClient, prefs))
							}
							3 -> {
								navigator.push(SptStepThreeScreen(sptHd, client, sptPertamaClient, prefs))
							}
							4 -> {
								navigator.push(SptStepFourScreen(sptHd, client, sptPertamaClient, prefs))
							}
							5 -> {
								navigator.push(SptStepFiveScreen(sptHd, client, sptPertamaClient, prefs))
							}
							6 -> {
							
							}
							7 -> {
							
							}
							8 -> {
							
							}
							9 -> {
							
							}
							10 -> {
							
							}
							11 -> {
							
							}
							12 -> {
							
							}
							13 -> {
							
							}
							14 -> {
							
							}
						}
					}),
				verticalAlignment = Alignment.CenterVertically
			) {
				Box (
					modifier = Modifier
						.padding(end = 8.dp)
						.size(32.dp)
						.clip(CircleShape)
						.background(if(completedStep + 1 >= number) Colors().brandDark5 else Colors().slate20)
				) {
					if(status) {
						Image(
							painterResource(Res.drawable.icon_spttick), null, alignment = Alignment.Center, modifier = Modifier.fillMaxSize()
						)
					} else {
						Text(
							text = "$number",
							fontSize = 16.sp,
							fontWeight = FontWeight.Bold,
							color = if(completedStep + 1 >= number) Colors().brandDark40 else Colors().textDarkGrey,
							modifier = Modifier.align(Alignment.Center)
						)
					}
				}
				
				Text (
					text = title,
					fontSize = 14.sp,
					color = if(completedStep + 1 >= number) Colors().textBlack else Colors().textDarkGrey,
					modifier = Modifier.weight(0.7f)
				)
				
				Image(
					painterResource(Res.drawable.icon_chevron_right_grey),
					null
				)
			}
		}
		
		loadingPopupBox(!isReady)
		
		Column (
			modifier = Modifier
				.fillMaxSize()
				.background(Colors().panel),
		) {
			LazyColumn (
				modifier = Modifier
					.fillMaxWidth()
			) {
				item{
					Row(
						modifier = Modifier.fillMaxWidth(),
						horizontalArrangement = Arrangement.SpaceBetween,
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
						
						Image(
							painter = painterResource(Res.drawable.icon_tripledot_black),
							null,
							modifier = Modifier
								.padding(end = 16.dp)
								.clickable(true, onClick = {
									showPopup = true
								})
						)
					}
				}
				
				item{
					Box (
						modifier = Modifier.fillMaxWidth()
							.padding(horizontal = 16.dp)
							.padding(bottom = 16.dp)
							.shadow(4.dp, RoundedCornerShape(8.dp))
							.clip(RoundedCornerShape(8.dp))
							.background(Color.White)
					) {
						Column(
							modifier = Modifier.padding(horizontal = 16.dp, vertical = 22.dp)
						) {
							Row (horizontalArrangement = Arrangement.SpaceBetween) {
								Text (
									text = if (formType == "1770S") "SPT 1770 S" else "SPT 1770",
									fontSize = 16.sp,
									fontWeight = FontWeight.Bold
								)
								
								//TODO("Create Chip")
							}
							Spacer(modifier = Modifier.padding(bottom = 24.dp))
							Text (
								text = "Tahun $taxYear",
								fontSize = 10.sp,
								fontWeight = FontWeight.Bold,
								color = Colors().textDarkGrey
							)
							Spacer(modifier = Modifier.padding(bottom = 4.dp))
							Text (
								text = "Pembetulan ke - $correctionSeq",
								fontSize = 10.sp,
								color = Colors().textDarkGrey
							)
						}
					}
				}
				
				item {
					Box (
						modifier = Modifier.fillMaxWidth()
							.padding(horizontal = 16.dp)
							.shadow(4.dp, RoundedCornerShape(8.dp))
							.clip(RoundedCornerShape(8.dp))
							.background(Color.White)
					) {
						Column(
							modifier = Modifier.padding(horizontal = 16.dp, vertical = 22.dp)
						) {
							Text (
								text = "Progress!",
								fontSize = 14.sp,
								fontWeight = FontWeight.Bold
							)
							Spacer(modifier = Modifier.padding(bottom = 8.dp))
							Text (
								text = "Selesaikan pengisian form SPTmu sebelum dilaporkan",
								fontSize = 12.sp,
								color = Colors().textDarkGrey
							)
							Spacer(modifier = Modifier.padding(bottom = 24.dp))
							Row(
								modifier = Modifier.fillMaxWidth(),
								verticalAlignment = Alignment.CenterVertically,
								horizontalArrangement = Arrangement.SpaceBetween
							) {
								LinearProgressIndicator(
									progress = completedStep.toFloat() / 14,
									color = Colors().brandDark50,
									backgroundColor = Colors().slate20,
									modifier = Modifier.height(24.dp).clip(RoundedCornerShape(16.dp)),
									strokeCap = StrokeCap.Square
								)
								
								Row(
									modifier = Modifier.padding(start = 16.dp),
									verticalAlignment = Alignment.CenterVertically
								) {
									Text(
										text = "$completedStep",
										fontSize = 14.sp,
										fontWeight = FontWeight.Bold
									)
									Text(
										text = "/14",
										fontSize = 10.sp,
									)
								}
							}
						}
					}
				}
				
				item {
					Text(
						modifier = Modifier.padding(vertical = 22.dp, horizontal = 16.dp),
						text = if (formType == "1770S") "Form 1770 S" else "Form 1770",
						fontSize = 14.sp,
						fontWeight = FontWeight.Bold
					)
				}
				
				item {
					Box (
						modifier = Modifier
							.fillMaxWidth()
							.padding(horizontal = 16.dp)
							.shadow(4.dp, RoundedCornerShape(8.dp))
							.clip(RoundedCornerShape(8.dp))
							.background(Color.White)
					) {
						Column {
							step(1, "Pengisian Identitas", status >= PertamaSptFillingStep.Identity.value)
							divider(0.dp)
							step(2, "Daftar Keluarga & Tanggungan", status >= PertamaSptFillingStep.Dependent.value)
							divider(0.dp)
							step(3, "Harta", status >= PertamaSptFillingStep.Asset.value)
							divider(0.dp)
							step(4, "Utang", status >= PertamaSptFillingStep.Liability.value)
							divider(0.dp)
							step(5, "Penghasilan Pajak Final", status >= PertamaSptFillingStep.FinalIncome.value)
							divider(0.dp)
							step(6, "Penghasilan Non Objek Pajak", status >= PertamaSptFillingStep.NonTaxedIncome.value)
							divider(0.dp)
							step(7, "Kredit Pajak", status >= PertamaSptFillingStep.TaxCredit.value)
							divider(0.dp)
							step(8, "Penghasilan Neto Dalam Negeri", status >= PertamaSptFillingStep.IncomeNetJob.value)
							divider(0.dp)
							step(9, "Penghasilan Neto Lainnya", status >= PertamaSptFillingStep.IncomeNetOther.value)
							divider(0.dp)
							step(10, "PPh Terutang (PH/MT)", status >= PertamaSptFillingStep.IncomeSpousePHMT.value)
							divider(0.dp)
							step(11, "Detail Lainnya", status >= PertamaSptFillingStep.OtherDetail.value)
							divider(0.dp)
							step(12, "Surat Setoran Pajak", status >= PertamaSptFillingStep.TaxPaymentSlip.value)
							divider(0.dp)
							step(13, "Data Pelengkap", status >= PertamaSptFillingStep.Additional.value)
							divider(0.dp)
							step(14, "Konfirmasi", status >= PertamaSptFillingStep.Confirm.value)
						}
					}
				}
				
				//Lapor SPT Button
				item {
					Button(
						enabled = status >= PertamaSptFillingStep.Confirm.value,
						modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 22.dp, bottom = 88.dp),
						colors = buttonColors(backgroundColor = Colors().buttonActive, contentColor = Color.White),
						onClick = {
							//TODO("Create HTTP Request")
						}
					) {
						Text(
							modifier = Modifier.padding(vertical = 6.dp),
							text = "Kirim SPT",
							fontSize = 16.sp,
							fontWeight = FontWeight.Bold
						)
					}
				}
			}
			
			//Spacer
			Box (
				modifier =  Modifier
					.fillMaxWidth()
					.padding(top = 16.dp, bottom = 88.dp)
					.background(Color.White)
			) {
			
			}
		}
		
		//Delete SPT Popup
		popUpBox(
			showPopup = showPopup,
			onClickOutside = { showPopup = false },
			content = {
				Text(
					text = "Hapus SPT",
					fontSize = 16.sp,
					color = Colors().textRed,
					modifier = Modifier.padding(vertical = 24.dp)
						.clickable(true, onClick = {
							showPopup = false
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
						text = "Hapus SPT",
						fontSize = 18.sp,
						color = Colors().textBlack,
						fontWeight = FontWeight.Bold
					)
					
					Text(
						modifier = Modifier.padding(vertical = 24.dp, horizontal = 16.dp),
						text = "Apakah Anda yakin ingin menghapus SPT ini?",
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
									sptManager.deleteSpt(scope, id)
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
					divider(0.dp)
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