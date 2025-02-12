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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import ayopajakmobile.composeapp.generated.resources.Res
import ayopajakmobile.composeapp.generated.resources.icon_infocircle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import global.AccountRecordType
import global.Colors
import global.NonFinalIncomeType
import global.TaxType
import global.universalUIComponents.loadingPopupBox
import global.universalUIComponents.topBar
import http.Account
import http.Interfaces
import models.transaction.Form1770HdResponseApiModel
import models.transaction.FormNonFinalIncomeResponseApiModel
import models.transaction.FormTaxCreditResponseApiModel
import org.jetbrains.compose.resources.painterResource
import util.BigDeciToLong
import util.BigDeciToString
import util.CurrencyFormatter
import util.DateFormatter

class IncomeNonFinalScreen(val sptHd: Form1770HdResponseApiModel?, val client: Account, val sptPertamaClient: Interfaces, val prefs: DataStore<Preferences>): Screen {
	
	val sptManager = SPTManager(prefs, client, sptPertamaClient)
	
	@Composable
	override fun Content() {
		
		val scope = rememberCoroutineScope()
		val navigator = LocalNavigator.currentOrThrow
		
		var taxPayerAccountRecordTypeE by remember { mutableStateOf(0) }
		
		var nonFinalIncomeList by remember { mutableStateOf<List<FormNonFinalIncomeResponseApiModel>>(emptyList()) }
		var nonFinalIncomeTotal by remember { mutableStateOf(0L) }
		
		var isReady by remember { mutableStateOf(false) }
		
		LaunchedEffect(null) {
			nonFinalIncomeList = sptManager.getIncomeNonFinalData(scope, sptHd!!.Id.toString())
			
			taxPayerAccountRecordTypeE = sptManager.getIdentityData(scope, sptHd.Id)?.AccountRecordE ?: 0
			
			nonFinalIncomeList.forEach {
				nonFinalIncomeTotal += BigDeciToLong(it.NettIncomeIDR.toString())
			}
			
			isReady = true
		}
		
		@Composable
		fun nonFinalIncomeCard(data: FormNonFinalIncomeResponseApiModel) {
			Box(
				modifier = Modifier
					.fillMaxWidth()
					.padding(horizontal = 16.dp)
					.padding(bottom = 8.dp)
					.clip(RoundedCornerShape(8.dp))
					.border(1.dp, Colors().slate20, RoundedCornerShape(8.dp))
					.background(Color.White)
					.clickable(true, onClick = {
						navigator.push(IncomeNonFinalFormScreen(data.Id, sptHd, client, sptPertamaClient, prefs))
					})
			) {
				Column(
					modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 24.dp)
				) {
					Row(
						modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
						horizontalArrangement = Arrangement.SpaceBetween,
						verticalAlignment = Alignment.Top
					) {
						Text(
							text = "${NonFinalIncomeType.fromValue(data.BusinessTypeE)}",
							fontSize = 12.sp,
							fontWeight = FontWeight.Bold
						)
						
						Text(
							text = "${data.TaxNormPerc}%",
							fontSize = 10.sp,
							fontWeight = FontWeight.Bold
						)
					}
					
					Text(
						text = data.KLU?.KLUName ?: "",
						fontSize = 10.sp,
						color = Colors().textDarkGrey,
						modifier = Modifier.padding(bottom = 16.dp)
					)
					
					Row (
						modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
						horizontalArrangement = Arrangement.SpaceBetween
					) {
						Text(
							text = "Peredaran Usaha",
							fontSize = 10.sp,
							color = Colors().textDarkGrey
						)
						Text(
							text = "Penghasilan Neto",
							fontSize = 10.sp,
							color = Colors().textDarkGrey
						)
					}
					
					Row (
						modifier = Modifier.fillMaxWidth(),
						horizontalArrangement = Arrangement.SpaceBetween
					) {
						Text(
							text = "Rp ${CurrencyFormatter(BigDeciToString(data.BusinessCirculationIDR.toString()))}",
							fontSize = 10.sp,
							fontWeight = FontWeight.Bold
						)
						Text(
							text = "Rp ${CurrencyFormatter(BigDeciToString(data.NettIncomeIDR.toString()))}",
							fontSize = 10.sp,
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
			LazyColumn(
				modifier = Modifier.fillMaxSize()
			) {
				//Top Bar
				item {
					topBar("Penghasilan Non Final (Pencatatan)")
				}
				
				if (taxPayerAccountRecordTypeE == AccountRecordType.BOOKKEEPING.value) {
					item {
						Box(
							modifier = Modifier
								.fillMaxWidth()
								.padding(horizontal = 16.dp)
								.clip(RoundedCornerShape(8.dp))
								.border(1.dp, Colors().slate20, RoundedCornerShape(8.dp))
								.background(Color.White)
						) {
							Column(
								modifier = Modifier.fillMaxWidth().padding(16.dp),
							) {
								Row(
									modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
									verticalAlignment = Alignment.CenterVertically
								) {
									Image(
										painter = painterResource(Res.drawable.icon_infocircle),
										contentDescription = null,
										modifier = Modifier.padding(end = 8.dp).scale(1.2f)
									)
									Text(
										text = "Anda Tidak Perlu Mengisi Form Ini",
										fontSize = 14.sp,
										fontWeight = FontWeight.Bold,
										modifier = Modifier.fillMaxWidth()
									)
								}
								Text(
									text = "Anda tidak perlu mengisi form ini dikarenakan [Jenis Pencatatan Penghasilan] anda pada Form Pengisian Identitas adalah [Pembukuan]. Form ini hanya diisi oleh Wajib Pajak yang menyelenggarakan [Pencatatan], untuk melaporkan besarnya penghasilan neto dalam negeri dari usaha dan/atau pekerjaan bebas yang diterima atau diperoleh Wajib Pajak sendiri dan anggota keluarganya dalam Tahun Pajak yang bersangkutan.",
									fontSize = 12.sp,
									lineHeight = 20.sp
								)
							}
						}
					}
				}
				else {
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
								modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 36.dp)
							) {
								Text(
									text = "Total Penghasilan",
									fontSize = 10.sp,
									color = Color.White,
									modifier = Modifier.padding(bottom = 8.dp)
								)
								
								Text(
									text = "Rp ${CurrencyFormatter(BigDeciToString(nonFinalIncomeTotal.toString()))}",
									fontSize = 20.sp,
									color = Color.White,
									fontWeight = FontWeight.Bold
								)
							}
						}
					}
					
					//New Tax Credit
					item {
						Box(
							modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 24.dp)
								.background(Colors().panel)
								.clip(RoundedCornerShape(4.dp))
								.border(1.dp, Colors().brandDark40, RoundedCornerShape(4.dp))
								.clickable(true, onClick = {
									navigator.push(IncomeNonFinalFormScreen(0, sptHd, client, sptPertamaClient, prefs))
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
					
					//TODO("Import Data")
					
					//SubTitle Row
					item {
						Row(
							modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 16.dp),
							horizontalArrangement = Arrangement.SpaceBetween
						) {
							Text(
								text = "Daftar Penghasilan Non Final",
								fontSize = 16.sp,
								fontWeight = FontWeight.Bold,
								color = Colors().slate70
							)
						}
					}
					
					//Tax Credit List
					nonFinalIncomeList.forEach { item { nonFinalIncomeCard(it) } }
					
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
}