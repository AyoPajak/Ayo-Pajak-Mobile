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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import ayopajakmobile.composeapp.generated.resources.Res
import ayopajakmobile.composeapp.generated.resources.icon_edit_grey
import ayopajakmobile.composeapp.generated.resources.placeholder
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import global.Colors
import global.FinalIncomeType
import global.IncomeGroup
import global.PertamaSptFillingStep
import global.SptType
import global.universalUIComponents.loadingPopupBox
import global.universalUIComponents.topBar
import http.Account
import http.Interfaces
import kotlinx.coroutines.launch
import models.transaction.Form1770HdResponseApiModel
import models.transaction.FormFinalIncomeResponseApiModel
import org.jetbrains.compose.resources.painterResource
import util.BigDeciToLong
import util.BigDeciToString
import util.CurrencyFormatter

class SptStepFiveScreen(val sptHd: Form1770HdResponseApiModel?, val client: Account, val sptPertamaClient: Interfaces, val prefs: DataStore<Preferences>): Screen {
	
	val sptManager = SPTManager(prefs, client, sptPertamaClient)
	
	@Composable
	override fun Content() {
		
		val scope = rememberCoroutineScope()
		val navigator = LocalNavigator.currentOrThrow
		
		var infoExpandState by remember { mutableStateOf(false) }
		
		var finalIncomeList by remember { mutableStateOf<List<FormFinalIncomeResponseApiModel>>(emptyList()) }
		var finalIncomeSummary by remember { mutableStateOf(0.0) }
		
		var isReady by remember { mutableStateOf(false) }
		
		LaunchedEffect(null) {
			finalIncomeList = sptManager.getFinalIncomeData(scope, sptHd!!.Id.toString())
			finalIncomeList.forEach { finalIncomeSummary += it.IncomeIDR }
			
			println(finalIncomeList)
			
			isReady = true
		}
		
		@Composable
		fun spt1770sCard(finalIncomeType: String, incomeIDR: String, id: Int) {
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
							text = finalIncomeType,
							fontSize = 12.sp,
							color = Colors().textBlack,
							modifier = Modifier.weight(0.2f)
						)
						
						Image(
							painterResource(Res.drawable.icon_edit_grey),
							contentDescription = null,
							modifier = Modifier.clickable(true, onClick = {
								navigator.push(FinalIncomeFormScreen(id, sptHd, client, sptPertamaClient, prefs))
							})
						)
					}
					Row(
						modifier = Modifier.fillMaxWidth(),
						verticalAlignment = Alignment.CenterVertically,
						horizontalArrangement = Arrangement.SpaceBetween
					) {
						Text(
							text = "Rp ${CurrencyFormatter(BigDeciToString(incomeIDR))}",
							fontSize = 12.sp,
							fontWeight = FontWeight.Bold,
							color = Colors().textBlack
						)
					}
				}
			}
		}
		
		@Composable
		fun spt1770Card(finalIncomeType: String, description : String?, incomeIDR: String, pph: String?, id: Int) {
			Box(
				modifier = Modifier
					.fillMaxWidth()
					.padding(horizontal = 16.dp)
					.padding(bottom = 8.dp)
					.clip(RoundedCornerShape(8.dp))
					.border(1.dp, Colors().slate20, RoundedCornerShape(8.dp))
					.background(Color.White)
					.clickable(true, onClick = {
						if(sptHd?.TaxYear?.toInt()!! >= 2023 && finalIncomeType == "Penghasilan Lain") navigator.push(FinalIncomeUMKMScreen(sptHd, client, sptPertamaClient, prefs)) else navigator.push(FinalIncomeFormScreen(id, sptHd, client, sptPertamaClient, prefs))
					})
			) {
				Column(
					modifier = Modifier.padding(16.dp)
				) {
					Text(
						text = finalIncomeType,
						fontSize = 14.sp,
						fontWeight = FontWeight.Bold,
						modifier = Modifier.padding(bottom = 8.dp)
					)
					Text(
						text = description ?: "-",
						fontSize = 10.sp,
						color = Colors().textDarkGrey,
						modifier = Modifier.padding(bottom = 16.dp)
					)
					
					Row(
						modifier = Modifier.fillMaxWidth(),
						horizontalArrangement = Arrangement.SpaceBetween
					) {
						Column {
							Text(
								text = "Dasar Pengenaan Pajak",
								fontSize = 10.sp,
								color = Colors().textDarkGrey,
								modifier = Modifier.padding(bottom = 8.dp)
							)
							Text(
								text = "Rp ${CurrencyFormatter(BigDeciToString(incomeIDR))}",
								fontSize = 10.sp,
								fontWeight = FontWeight.Bold,
								color = Colors().textBlack
							)
						}
						
						Column(horizontalAlignment = Alignment.End) {
							Text(
								text = "PPH Terhutang",
								fontSize = 10.sp,
								color = Colors().textDarkGrey,
								modifier = Modifier.padding(bottom = 8.dp)
							)
							Text(
								text = "Rp ${CurrencyFormatter(BigDeciToString(pph ?: "0"))}",
								fontSize = 10.sp,
								fontWeight = FontWeight.Bold,
								color = Colors().textRed
							)
						}
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
				item { topBar("Penghasilan Pajak Final") }
				
				if (sptHd?.SPTType == SptType.SPT_1770S.value) {
					//Summary Box
					item {
						Box(
							modifier = Modifier
								.fillMaxWidth()
								.padding(horizontal = 16.dp).padding(bottom = 24.dp)
								.clip(RoundedCornerShape(8.dp))
								.background(Colors().greenMain50)
						) {
							Column(
								modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 24.dp)
									.padding(top = 16.dp)
							) {
								Text(
									text = "Total Penghasilan Pajak Final",
									fontSize = 10.sp,
									color = Color.White,
									modifier = Modifier.padding(bottom = 8.dp)
								)
								
								Text(
									text = "Rp ${CurrencyFormatter(BigDeciToString(finalIncomeSummary.toString()))}",
									fontSize = 16.sp,
									color = Color.White,
									fontWeight = FontWeight.Bold
								)
								
								Box(
									modifier = Modifier
										.fillMaxWidth()
										.padding(top = 32.dp)
										.clip(RoundedCornerShape(4.dp))
										.background(Color(0xff1CCA9A))
								) {
									Row(
										modifier = Modifier.fillMaxWidth()
											.padding(horizontal = 16.dp, vertical = 24.dp),
										verticalAlignment = Alignment.CenterVertically,
										horizontalArrangement = Arrangement.SpaceBetween
									) {
										Text(
											text = "Jumlah Penghasilan",
											fontSize = 10.sp,
											color = Color.White,
										)
										Text(
											text = "${finalIncomeList.size}",
											fontSize = 14.sp,
											color = Color.White,
											fontWeight = FontWeight.Bold
										)
									}
								}
							}
						}
					}
					
					//New Final Income
					item {
						Box(
							modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 24.dp)
								.background(Colors().panel)
								.clip(RoundedCornerShape(4.dp))
								.border(1.dp, Colors().brandDark40, RoundedCornerShape(4.dp))
								.clickable(true, onClick = {
									navigator.push(FinalIncomeFormScreen(0, sptHd, client, sptPertamaClient, prefs))
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
					
					//SubTitle Row
					item {
						Row(
							modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 16.dp),
							horizontalArrangement = Arrangement.SpaceBetween
						) {
							Text(
								text = "Daftar Penghasilan Final",
								fontSize = 16.sp,
								fontWeight = FontWeight.Bold,
								color = Colors().slate70
							)
							
							//TODO("Create Filter List feature")
						}
					}
					
					//List Final Income by Type
					finalIncomeList.forEach {
						item {
							val diff = BigDeciToLong(it.IncomeIDR.toString()) - BigDeciToLong(it.TaxPayableIDR.toString())
							spt1770sCard(FinalIncomeType.fromValue(it.IncomeTypeE) ?: "", diff.toString(), it.Id)
						}
					}
				}
				else {
					//Info
					item {
						Box(
							modifier = Modifier
								.fillMaxWidth()
								.padding(horizontal = 16.dp).padding(bottom = 24.dp)
								.shadow(4.dp, RoundedCornerShape(8.dp))
								.clip(RoundedCornerShape(8.dp))
								.background(Color.White)
						) {
							if (!infoExpandState) {
								Column(
									modifier = Modifier
										.fillMaxWidth()
										.padding(horizontal = 16.dp, vertical = 24.dp)
								) {
									Row(
										modifier = Modifier.fillMaxWidth()
									) {
										Column(
											modifier = Modifier
												.fillMaxWidth()
												.padding(bottom = 24.dp, end = 8.dp)
												.weight(0.5f)
										) {
											Text(
												text = "Data Yang Anda Masukkan Sudah Sesuai?",
												fontSize = 12.sp,
												color = Colors().textBlack,
												fontWeight = FontWeight.Bold,
												modifier = Modifier.padding(bottom = 32.dp)
											)
											
											Text(
												text = "Jika anda melakukan kegiatan usaha dengan peredaran bruto dalam satu tahun kurang dari Rp4.800.000.000,00 (di tahun pajak sebelumnya)...",
												fontSize = 10.sp,
												color = Colors().textBlack
											)
										}
										Image(
											painter = painterResource(Res.drawable.placeholder),
											contentDescription = null,
											modifier = Modifier.padding(start = 8.dp).weight(0.5f)
										)
									}
									
									Text(
										text = "Tampilkan Lebih Banyak",
										fontSize = 10.sp,
										color = Colors().textDarkGrey,
										fontWeight = FontWeight.Bold,
										modifier = Modifier
											.clickable(true, onClick = {
												infoExpandState = true
											})
									)
								}
							}
							else {
								Column(
									modifier = Modifier
										.fillMaxWidth()
										.padding(horizontal = 16.dp, vertical = 24.dp),
									horizontalAlignment = Alignment.CenterHorizontally
								) {
									Image(
										painter = painterResource(Res.drawable.placeholder),
										contentDescription = null,
										modifier = Modifier.padding(bottom = 32.dp)
									)
									
									Text(
										text = "Data Yang Anda Masukkan Sudah Sesuai?",
										fontSize = 12.sp,
										color = Colors().textBlack,
										fontWeight = FontWeight.Bold,
										modifier = Modifier.padding(bottom = 24.dp).fillMaxWidth()
									)
									
									Text(
										text = "Jika anda melakukan kegiatan usaha dengan peredaran bruto dalam satu tahun kurang dari Rp4.800.000.000,00 (di tahun pajak sebelumnya) dan dikenai Peraturan Pemerintah Nomor 23 Tahun 2018 (peraturan pengganti PP 46/2013 sejak 1 Juli 2018), anda dapat mendaftarkannya dengan memilih jenis penghasilan :[Penghasilan Lain Yang Dikenakan Pajak Final dan/atau Bersifat Final]",
										fontSize = 10.sp,
										color = Colors().textBlack,
										modifier = Modifier.padding(bottom = 16.dp)
									)
									
									Text(
										text = "Lalu berdasarkan UU Nomor 7 Tahun 2021 atau biasa dikenal dengan nama Undang-Undang Harmonisasi Perpajakan (UU HPP), tarif PPh Final untuk pengusaha dengan peredaran bruto tertentu mengalami perubahan, yaitu bagi orang pribadi pengusaha yang menghitung PPh dengan tarif final 0,5% (PP 23/2018) dan memiliki peredaran bruto sampai dengan Rp500 juta setahun tidak dikenai PPh.",
										fontSize = 10.sp,
										color = Colors().textBlack,
										modifier = Modifier.padding(bottom = 16.dp)
									)
									
									Text(
										text = "Jangka waktu pengenaan tarif PPh final 0,5% paling lama 7 tahun untuk WP Orang Pribadi, terhitung sejak WP terdaftar bagi WP yang terdaftar setelah tahun 2018, atau sejak tahun 2018 bagi WP yang terdaftar sebelum tahun 2018.",
										fontSize = 10.sp,
										color = Colors().textBlack,
										modifier = Modifier.padding(bottom = 16.dp)
									)
									
									Text(
										text = "Jangka waktu pengenaan tarif PPh final 0,5% paling lama 7 tahun untuk WP Orang Pribadi, terhitung sejak WP terdaftar bagi WP yang terdaftar setelah tahun 2018, atau sejak tahun 2018 bagi WP yang terdaftar sebelum tahun 2018.",
										fontSize = 10.sp,
										color = Colors().textBlack,
										modifier = Modifier.padding(bottom = 16.dp)
									)
									
									Text(
										text = "Namun jika anda termasuk WAJIB PAJAK ORANG PRIBADI PENGUSAHA TERTENTU atau usaha anda termasuk dalam kategori pedagang pengecer, anda akan dikenakan PPh Pasal 25 yang dapat didaftarkan pada Form [Penghasilan non final (Pencatatan)]",
										fontSize = 10.sp,
										color = Colors().textBlack,
										modifier = Modifier.padding(bottom = 24.dp)
									)
									
									Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End){
										Text(
											text = "Tampilkan Lebih Sedikit",
											fontSize = 10.sp,
											color = Colors().textDarkGrey,
											fontWeight = FontWeight.Bold,
											modifier = Modifier
												.clickable(true, onClick = {
													infoExpandState = false
												})
										)
									}
								}
							}
						}
					}
					
					//New Final Income
					item {
						Box(
							modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 24.dp)
								.background(Colors().panel)
								.clip(RoundedCornerShape(4.dp))
								.border(1.dp, Colors().brandDark40, RoundedCornerShape(4.dp))
								.clickable(true, onClick = {
									navigator.push(FinalIncomeFormScreen(0, sptHd, client, sptPertamaClient, prefs))
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
					
					//SubTitle Row
					item {
						Row(
							modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 16.dp),
							horizontalArrangement = Arrangement.SpaceBetween
						) {
							Text(
								text = "Daftar Penghasilan Final",
								fontSize = 16.sp,
								fontWeight = FontWeight.Bold,
								color = Colors().slate70
							)
							
							//TODO("Create Filter List feature")
						}
					}
					
					//List
					finalIncomeList.forEach {
						item {
							spt1770Card(FinalIncomeType.fromValue(it.IncomeTypeE) ?: "",it.Description, it.IncomeIDR.toString(), it.TaxPayableIDR.toString(), it.Id)
						}
					}
				}
				
				//Spacer
				item { Box(modifier = Modifier.fillMaxWidth().height(156.dp)) }
			}
		}
		
		//Done Button
		Box(
			modifier = Modifier.fillMaxSize().padding(bottom = 88.dp).padding(horizontal = 16.dp),
			contentAlignment = Alignment.BottomCenter
		){
			Box(
				contentAlignment = Alignment.Center,
				modifier = Modifier.fillMaxWidth()
					.clip(RoundedCornerShape(8.dp))
					.background(Colors().brandDark40, RoundedCornerShape(8.dp))
					.clickable(true, onClick = {
						println()
						isReady = false
						scope.launch{
							sptManager.updateStepForm1770(
								scope,
								sptHd!!.Id,
								PertamaSptFillingStep.FinalIncome.value
							)
							navigator.pop()
						}
					})
			) {
				Text(
					text = "Selesai",
					fontSize = 16.sp,
					color = Color.White,
					modifier = Modifier.padding(vertical = 16.dp)
				)
			}
		}
	}
}