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
import ayopajakmobile.composeapp.generated.resources.icon_edit_grey
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import global.AssetApiSaveCode
import global.AssetCode
import global.Colors
import global.CurrRateEntryMode
import global.universalUIComponents.loadingPopupBox
import global.universalUIComponents.topBar
import http.Account
import http.Interfaces
import models.transaction.Form1770HdResponseApiModel
import models.transaction.FormWealthResponseApiModel
import org.jetbrains.compose.resources.painterResource
import screens.divider
import util.BigDeciToString
import util.CurrencyFormatter

class WealthTypeDetailScreen(
	private val sptHd: Form1770HdResponseApiModel?,
	private val wealthTypeId: String,
	val client: Account,
	val sptPertamaClient: Interfaces,
	val prefs: DataStore<Preferences>,
	val assetCode: String,
	val assetName: String
): Screen {
	private val sptManager = SPTManager(prefs, client, sptPertamaClient)
	
	@Composable
	override fun Content() {
		val scope = rememberCoroutineScope()
		val navigator = LocalNavigator.currentOrThrow
		
		var category by remember { mutableStateOf("") }
		var wealthList by remember { mutableStateOf<List<FormWealthResponseApiModel>>(emptyList()) }
		val totalWealthInType by remember { mutableStateOf(0.0) }
		
		var isReady by remember { mutableStateOf(false) }
		
		LaunchedEffect(null) {
			wealthList = sptManager.getWealthData(scope, sptHd!!.Id.toString(), wealthTypeId)
			
			when (assetCode) {
				AssetCode.UANG_TUNAI.value -> {
					category = AssetApiSaveCode.A.toString()
				}
				AssetCode.TABUNGAN.value, AssetCode.GIRO.value -> {
					category = AssetApiSaveCode.B.toString()
				}
				AssetCode.DEPOSITO.value -> {
					category = AssetApiSaveCode.C.toString()
				}
				AssetCode.PIUTANG.value,
				AssetCode.PIUTANG_AFILIASI.value,
				AssetCode.PIUTANG_LAINNYA.value,
				AssetCode.OBLIGASI_PERUSAHAAN.value,
				AssetCode.OBLIGASI_PEMERINTAH_INDONESIA.value,
				AssetCode.SURAT_UTANG_LAINNYA.value,
				AssetCode.INSTRUMEN_DERIVATIF.value -> {
					category = AssetApiSaveCode.D.toString()
				}
				AssetCode.PERSEDIAAN_USAHA.value -> {
					category = AssetApiSaveCode.E.toString()
				}
				AssetCode.SAHAM_YANG_DIBELI_UNTUK_DIJUAL_KEMBALI.value,
				AssetCode.SAHAM.value -> {
					category = AssetApiSaveCode.F.toString()
				}
				AssetCode.PENYERTAAN_MODAL_DALAM_PERUSAHAAN_LAIN_YANG_TIDAK_ATAS_SAHAM.value,
				AssetCode.INVESTASI_LAINNYA.value -> {
					category = AssetApiSaveCode.G.toString()
				}
				AssetCode.SEPEDA.value,
				AssetCode.SEPEDA_MOTOR.value,
				AssetCode.MOBIL.value,
				AssetCode.ALAT_TRANSPORTASI_LAINNYA.value,
				AssetCode.KAPAL_PESIAR_PESAWAT_TERBANG_HELIKOPTER_JETSKI_PERALATAN_OLAH_RAGA_KHUSUS.value,
				AssetCode.PERALATAN_ELEKTRONIK_FURINITURE.value,
				AssetCode.HARTA_BERGERAK_LAINNYA.value-> {
					category = AssetApiSaveCode.H.toString()
				}
				AssetCode.LOGAM_MULIA.value,
				AssetCode.BATU_MULIA.value,
				AssetCode.BARANG_SENI_DAN_ANTIK.value -> {
					category = AssetApiSaveCode.I.toString()
				}
				AssetCode.TANAH_DAN_ATAU_BANGUNAN_UNTUK_TEMPAT_TINGGAL.value,
				AssetCode.TANAH_DAN_ATAU_BANGUNAN_UNTUK_USAHA.value,
				AssetCode.TANAH_ATAU_LAHAN_UNTUK_USAHA.value,
				AssetCode.HARTA_TIDAK_GERAK_LAINNYA.value-> {
					category = AssetApiSaveCode.J.toString()
				}
				AssetCode.PATEN.value,
				AssetCode.ROYALTI.value,
				AssetCode.MEREK_DAGANG.value,
				AssetCode.HARTA_TIDAK_BERWUJUD_LAINNYA.value-> {
					category = AssetApiSaveCode.K.toString()
				}
				AssetCode.SETARA_KAS_LAINNYA.value,
				AssetCode.REKSADANA.value-> {
					category = AssetApiSaveCode.L.toString()
				}
			}
			
			isReady = true
		}
		
		@Composable
		fun wealthCard(assetName: String, acquisitionYear: String, currencyAmmount: Double, id: String) {
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
							text = assetName,
							fontSize = 12.sp,
							color = Colors().textBlack,
							modifier = Modifier.weight(0.2f)
						)
						Image(
							painterResource(Res.drawable.icon_edit_grey),
							contentDescription = null,
							modifier = Modifier.clickable(true, onClick = {
								navigator.push(AssetFormScreen(id.toInt(), sptHd!!.Id, client, sptPertamaClient, prefs))
							})
						)
					}
					Row(
						modifier = Modifier.fillMaxWidth(),
						horizontalArrangement = Arrangement.SpaceBetween,
						verticalAlignment = Alignment.CenterVertically
					) {
						Text(
							text = acquisitionYear,
							fontSize = 10.sp,
							fontWeight = FontWeight.Bold,
							color = Colors().textDarkGrey
						)
						Text(
							text = "Rp ${CurrencyFormatter(BigDeciToString(currencyAmmount.toString()))}",
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
				item { topBar("Detail Harta") }
				
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
								text = "$assetCode - $assetName",
								fontSize = 14.sp,
								color = Color.White,
								fontWeight = FontWeight.Bold,
								modifier = Modifier.padding(bottom = 12.dp)
							)
							divider(0.dp)
							Text(
								text = "Total Harta",
								fontSize = 10.sp,
								color = Color.White,
								modifier = Modifier.padding(bottom = 8.dp, top = 24.dp)
							)
							Text(
								text = "Rp ${CurrencyFormatter(BigDeciToString(totalWealthInType.toString()))}",
								fontSize = 16.sp,
								color = Color.White,
								fontWeight = FontWeight.Bold
							)
							Box(
								modifier = Modifier
									.fillMaxWidth()
									.padding(top = 24.dp)
									.clip(RoundedCornerShape(4.dp))
									.background(Color(0xff598dff))
							) {
								Row(
									modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 24.dp),
									verticalAlignment = Alignment.CenterVertically,
									horizontalArrangement = Arrangement.SpaceBetween
								) {
									Text(
										text = "Jumlah Harta",
										fontSize = 10.sp,
										color = Color.White
									)
									Text(
										text = "${wealthList.size}",
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
								navigator.push(AssetFormScreen(0, sptHd!!.Id, client, sptPertamaClient, prefs))
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
				
				//List Wealth
				wealthList.forEach {
					item {
						wealthCard(it.Description ?: "-", it.AcquisitionYear, it.CurrencyAmountIDR, it.Id.toString())
					}
				}
				
				//Spacer
				item { Box(modifier = Modifier.fillMaxWidth().height(88.dp)) }
			}
		}
	}
}