package screens.SPT

import SPT.SPTManager
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults.textFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import ayopajakmobile.composeapp.generated.resources.Res
import ayopajakmobile.composeapp.generated.resources.placeholder_NPWP
import ayopajakmobile.composeapp.generated.resources.placeholder_username
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import global.Colors
import global.PreferencesKey.Companion.NPWP
import global.PreferencesKey.Companion.WPNPWP
import global.PreferencesKey.Companion.WPName
import global.universalUIComponents.topBar
import http.Account
import http.Interfaces
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import models.transaction.Form1770HdRequestApiModel
import org.jetbrains.compose.resources.stringResource

class CreateSPTForm(val client: Account, val sptPertamaClient: Interfaces, val prefs: DataStore<Preferences>): Screen {
	
	val sptManager = SPTManager(prefs, client, sptPertamaClient)
	
	@Composable
	override fun Content() {
		
		val navigator = LocalNavigator.currentOrThrow
		val scope = rememberCoroutineScope()
		
		var taxPayerName by remember { mutableStateOf("") }
		var taxPayerNPWP by remember { mutableStateOf("") }
		var formType by remember { mutableStateOf("1770S") }
		var taxYear by remember { mutableStateOf((Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).year - 1).toString()) }
		var correctionSeq by remember { mutableStateOf("") }
		
		LaunchedEffect(null) {
			try {
				scope.launch {
					taxPayerName = prefs.data.first()[stringPreferencesKey(WPName)] ?: ""
					taxPayerNPWP = prefs.data.first()[stringPreferencesKey(NPWP)] ?: ""
				}
			} catch(ex: Exception) {
				println(ex.message)
			}
		}
		
		Column(
			modifier = Modifier.fillMaxSize().background(Colors().panel)
		) {
			topBar("Buat SPT")
			
			LazyColumn{
				//Nama Wajib Pajak
				item {
					Text(
						modifier = Modifier.padding(horizontal = 16.dp),
						text = "Nama Wajib Pajak",
						fontSize = 12.sp,
						color = Color.Black,
						fontWeight = FontWeight.Bold
					)
					TextField(
						enabled = false,
						modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp, top = 8.dp)
							.padding(horizontal = 16.dp)
							.border(
								border = BorderStroke(1.dp, Colors().textDarkGrey),
								shape = RoundedCornerShape(4.dp)
							),
						placeholder = {
							Text(
								stringResource(Res.string.placeholder_username), fontSize = 16.sp,
								color = Colors().textDarkGrey
							)
						},
						value = taxPayerName,
						onValueChange = {
							taxPayerName = it
						},
						singleLine = true,
						colors = textFieldColors(
							backgroundColor = Colors().slate20,
							focusedIndicatorColor = Color.Transparent,
							unfocusedIndicatorColor = Color.Transparent,
							disabledIndicatorColor = Colors().slate20
						),
						keyboardOptions = KeyboardOptions(
							keyboardType = KeyboardType.Text,
							imeAction = ImeAction.Next
						)
					)
				}
				
				//NPWP
				item {
					Text(
						modifier = Modifier.padding(horizontal = 16.dp),
						text = "NPWP",
						fontSize = 12.sp,
						color = Color.Black,
						fontWeight = FontWeight.Bold
					)
					TextField(
						enabled = false,
						modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp, top = 8.dp)
							.padding(horizontal = 16.dp)
							.border(
								border = BorderStroke(1.dp, Colors().textDarkGrey),
								shape = RoundedCornerShape(4.dp)
							),
						placeholder = {
							Text(
								stringResource(Res.string.placeholder_NPWP), fontSize = 16.sp,
								color = Colors().textDarkGrey
							)
						},
						value = taxPayerNPWP,
						onValueChange = {
							taxPayerNPWP = it
						},
						singleLine = true,
						colors = textFieldColors(
							backgroundColor = Colors().slate20,
							focusedIndicatorColor = Color.Transparent,
							unfocusedIndicatorColor = Color.Transparent,
							disabledIndicatorColor = Colors().slate20
						),
						keyboardOptions = KeyboardOptions(
							keyboardType = KeyboardType.Number,
							imeAction = ImeAction.Next
						)
					)
				}
				
				//Form Type Selection
				item {
					Box(
						modifier = Modifier.fillMaxWidth()
							.padding(horizontal = 16.dp).padding(bottom = 22.dp)
							.shadow(4.dp, RoundedCornerShape(16.dp))
							.clip(RoundedCornerShape(16.dp))
							.background(Color.White)
							.border(1.dp, Colors().slate20)
					) {
						Column(
							modifier = Modifier.fillMaxWidth().padding(16.dp)
						) {
							Box(
								modifier = Modifier.fillMaxWidth()
									.shadow(4.dp, RoundedCornerShape(8.dp))
									.clip(RoundedCornerShape(8.dp))
									.border(
										1.dp,
										if (formType == "1770S") Color(0xFF3565FC) else Colors().slate30,
										RoundedCornerShape(8.dp)
									)
									.background(if (formType == "1770S") Colors().brandDark40 else Color.White)
									.clickable(true, onClick = { formType = "1770S" })
							) {
								Column(
									modifier = Modifier.padding(horizontal = 16.dp, vertical = 22.dp)
								) {
									Row(
										modifier = Modifier.padding(bottom = 28.dp),
										horizontalArrangement = Arrangement.SpaceBetween
									) {
										Text(
											text = "FORM SPT 1770 S",
											fontSize = 12.sp,
											color = if (formType == "1770S") Color.White else Colors().textBlack,
											fontWeight = FontWeight.Bold
										)
										
										//TODO("Create Selected pill")
									}
									
									Text(
										modifier = Modifier.padding(bottom = 4.dp),
										text = "Untuk :",
										fontSize = 10.sp,
										color = if (formType == "1770S") Color.White else Colors().textBlack,
									)
									Row {
										Text(
											modifier = Modifier.padding(horizontal = 4.dp),
											text = "•",
											fontSize = 10.sp,
											color = if (formType == "1770S") Color.White else Colors().textBlack,
										)
										Text(
											text = "Penghasilan dari 1 atau lebih pemberi kerja",
											fontSize = 10.sp,
											color = if (formType == "1770S") Color.White else Colors().textBlack,
										)
									}
									Spacer(modifier = Modifier.padding(bottom = 2.dp))
									Row {
										Text(
											modifier = Modifier.padding(horizontal = 4.dp),
											text = "•",
											fontSize = 10.sp,
											color = if (formType == "1770S") Color.White else Colors().textBlack,
										)
										Text(
											text = "Penghasilan dari dalam negeri lainnya",
											fontSize = 10.sp,
											color = if (formType == "1770S") Color.White else Colors().textBlack,
										)
									}
								}
							}
							
							Spacer(modifier = Modifier.padding(bottom = 12.dp))
							
							Box(
								modifier = Modifier.fillMaxWidth()
									.shadow(4.dp, RoundedCornerShape(8.dp))
									.clip(RoundedCornerShape(8.dp))
									.border(
										1.dp,
										if (formType == "1770") Color(0xFF3565FC) else Colors().slate30,
										RoundedCornerShape(8.dp)
									)
									.background(if (formType == "1770") Colors().brandDark40 else Color.White)
									.clickable(true, onClick = { formType = "1770" })
							) {
								Column(
									modifier = Modifier.padding(horizontal = 16.dp, vertical = 22.dp)
								) {
									Row(
										modifier = Modifier.padding(bottom = 28.dp),
										horizontalArrangement = Arrangement.SpaceBetween
									) {
										Text(
											text = "FORM SPT 1770",
											fontSize = 12.sp,
											color = if (formType == "1770") Color.White else Colors().textBlack,
											fontWeight = FontWeight.Bold
										)
										
										//TODO("Create Selected pill")
									}
									
									Text(
										modifier = Modifier.padding(bottom = 4.dp),
										text = "Untuk :",
										fontSize = 10.sp,
										color = if (formType == "1770") Color.White else Colors().textBlack,
									)
									Row {
										Text(
											modifier = Modifier.padding(horizontal = 4.dp),
											text = "•",
											fontSize = 10.sp,
											color = if (formType == "1770") Color.White else Colors().textBlack,
										)
										Text(
											text = "Penghasilan dari usaha atau pekerjaan bebas",
											fontSize = 10.sp,
											color = if (formType == "1770") Color.White else Colors().textBlack,
										)
									}
									Spacer(modifier = Modifier.padding(bottom = 2.dp))
									Row {
										Text(
											modifier = Modifier.padding(horizontal = 4.dp),
											text = "•",
											fontSize = 10.sp,
											color = if (formType == "1770") Color.White else Colors().textBlack,
										)
										Text(
											text = "Penghasilan dari 1 atau lebih pemberi kerja",
											fontSize = 10.sp,
											color = if (formType == "1770") Color.White else Colors().textBlack,
										)
									}
									Spacer(modifier = Modifier.padding(bottom = 2.dp))
									Row {
										Text(
											modifier = Modifier.padding(horizontal = 4.dp),
											text = "•",
											fontSize = 10.sp,
											color = if (formType == "1770") Color.White else Colors().textBlack,
										)
										Text(
											text = "Penghasilan dari dalam atau luar negeri lainnya",
											fontSize = 10.sp,
											color = if (formType == "1770") Color.White else Colors().textBlack,
										)
									}
								}
							}
						}
					}
				}
				
				//Tahun Pajak
				item {
					Text(
						modifier = Modifier.padding(horizontal = 16.dp),
						text = "Tahun Pajak",
						fontSize = 12.sp,
						color = Color.Black,
						fontWeight = FontWeight.Bold
					)
					TextField(
						enabled = true,
						modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp, top = 8.dp)
							.padding(horizontal = 16.dp)
							.border(
								border = BorderStroke(1.dp, Colors().textDarkGrey),
								shape = RoundedCornerShape(4.dp)
							)
							.onFocusChanged {
								if (!it.isFocused) {
									try {
										scope.launch { correctionSeq =  (sptManager.getLatestSPT(scope, taxYear).CorrectionSeq + 1).toString() }
									} catch(ex: Exception) {
										println(ex.message)
									}
								}
							},
						placeholder = {
							Text(
								(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).year - 1).toString(), fontSize = 16.sp,
								color = Colors().textDarkGrey
							)
						},
						value = taxYear,
						onValueChange = {
							taxYear = if (it == "") (Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).year - 1).toString() else it
						},
						singleLine = true,
						colors = textFieldColors(
							backgroundColor = Color.White,
							focusedIndicatorColor = Color.Transparent,
							unfocusedIndicatorColor = Color.Transparent,
							disabledIndicatorColor = Color.Transparent
						),
						keyboardOptions = KeyboardOptions(
							keyboardType = KeyboardType.Number,
							imeAction = ImeAction.Next
						),
					)
				}
				
				//Pembetulan
				item {
					Text(
						modifier = Modifier.padding(horizontal = 16.dp),
						text = "Pembetulan ke",
						fontSize = 12.sp,
						color = Color.Black,
						fontWeight = FontWeight.Bold
					)
					TextField(
						enabled = true,
						modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp, top = 8.dp)
							.padding(horizontal = 16.dp)
							.border(
								border = BorderStroke(1.dp, Colors().textDarkGrey),
								shape = RoundedCornerShape(4.dp)
							),
						placeholder = {
							Text(
								"0", fontSize = 16.sp,
								color = Colors().textDarkGrey
							)
						},
						value = correctionSeq,
						onValueChange = {
							correctionSeq = it
						},
						singleLine = true,
						colors = textFieldColors(
							backgroundColor = Color.White,
							focusedIndicatorColor = Color.Transparent,
							unfocusedIndicatorColor = Color.Transparent,
							disabledIndicatorColor = Color.Transparent
						),
						keyboardOptions = KeyboardOptions(
							keyboardType = KeyboardType.Number,
							imeAction = ImeAction.Done
						)
					)
				}
				
				item {
					Button(
						modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
						colors = buttonColors(backgroundColor = Colors().buttonActive, contentColor = Color.White),
						onClick = {
							val model = Form1770HdRequestApiModel(
								taxYear.toInt(),
								formType,
								correctionSeq.toInt()
							)
							
							scope.launch {
								val id = sptManager.createSpt(scope, model)?.Data?.jsonObject?.get("Id")?.jsonPrimitive?.content!!.toInt()
								
								id.let {
									println("SPT ID: $id")
									navigator.push(SummarySPTScreen(it, client, sptPertamaClient, prefs))
								}
							}
						}
					) {
						Text(
							modifier = Modifier.padding(vertical = 6.dp),
							text = "Berikutnya",
							fontSize = 16.sp,
							fontWeight = FontWeight.Bold
						)
					}
				}
				
				item { Spacer(modifier = Modifier.padding(bottom = 88.dp)) }
			}
		}
	}
}