package screens.SPT

import SPT.SPTManager
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import ayopajakmobile.composeapp.generated.resources.Icon_Dropdown_Arrow
import ayopajakmobile.composeapp.generated.resources.Res
import ayopajakmobile.composeapp.generated.resources.arrow_back
import ayopajakmobile.composeapp.generated.resources.icon_calendar
import ayopajakmobile.composeapp.generated.resources.icon_clear
import ayopajakmobile.composeapp.generated.resources.icon_search
import ayopajakmobile.composeapp.generated.resources.icon_tripledot_black
import ayopajakmobile.composeapp.generated.resources.placeholder_username
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import global.Colors
import global.JobName
import global.TaxStatus
import global.universalUIComponents.loadingPopupBox
import global.universalUIComponents.popUpBox
import global.universalUIComponents.topBar
import http.Account
import http.Interfaces
import kotlinx.coroutines.launch
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDate
import models.master.CurrencyModel
import models.master.FamilyRelModel
import models.master.JobModel
import models.transaction.Form1770HdResponseApiModel
import models.transaction.FormDependentRequestApiModel
import models.transaction.FormDependentResponseApiModel
import models.transaction.FormIdentityRequestApiModel
import network.chaintech.kmp_date_time_picker.ui.datepicker.WheelDatePickerComponent.WheelDatePicker
import network.chaintech.kmp_date_time_picker.ui.datepicker.WheelDatePickerView
import network.chaintech.kmp_date_time_picker.utils.DateTimePickerView
import network.chaintech.kmp_date_time_picker.utils.WheelPickerDefaults
import network.chaintech.kmp_date_time_picker.utils.now
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import util.BigDeciToString
import util.CurrencyFormatter
import util.DateFormatter

class DependentFormScreen(val id: Int, val sptHd: Form1770HdResponseApiModel?, val client: Account, val sptPertamaClient: Interfaces, val prefs: DataStore<Preferences>, val lastSeq: Int): Screen {

	val sptManager = SPTManager(prefs, client, sptPertamaClient)
	
	@Composable
	override fun Content() {
		
		val scope = rememberCoroutineScope()
		val navigator = LocalNavigator.currentOrThrow
		
		var showFamilyRelPopup by remember { mutableStateOf(false) }
		var showJobPopup by remember { mutableStateOf(false) }
		var jobSearchText by remember { mutableStateOf("") }
		var showDatePicker by remember { mutableStateOf(false) }
		
		var showDeletePopup by remember { mutableStateOf(false) }
		var showConfirmPopup by remember { mutableStateOf(false) }
		
		var dependentSequence by remember { mutableStateOf(0) }
		var dependentName by remember { mutableStateOf("") }
		var dependentNik by remember { mutableStateOf("") }
		var dependentFamilyRelId by remember { mutableStateOf(0) }
		var dependentBirthDate by remember { mutableStateOf("") }
		var dependentJobId by remember { mutableStateOf(0) }
		var schoolFee by remember { mutableStateOf("") }
		var dependentFee by remember { mutableStateOf("") }
		
		var familyRelList by remember { mutableStateOf<List<FamilyRelModel>>(emptyList()) }
		var jobList by remember { mutableStateOf<List<JobModel>>(emptyList()) }
		
		var selectedJob by remember { mutableStateOf("") }
		var selectedFamilyRel by remember { mutableStateOf("") }
		
		var isReady by remember { mutableStateOf(false) }
		
		LaunchedEffect(null) {
			familyRelList = sptManager.getFamilyRel(scope)
			jobList = sptManager.getJobList(scope)
			
			if(id != 0) {
				val dependentData = sptManager.getDependentById(scope, id)
				
				dependentSequence = dependentData!!.Seq
				dependentName = dependentData.DependentName
				dependentNik = dependentData.DependentNIK ?: ""
				dependentFamilyRelId = dependentData.FamilyRel.Id
				dependentBirthDate = dependentData.BirthDate
				dependentJobId = dependentData.Job.Id
				schoolFee = CurrencyFormatter(BigDeciToString(dependentData.SchoolFee.toString()))
				dependentFee = CurrencyFormatter(BigDeciToString(dependentData.DependentFee.toString()))
				
				println(dependentData)
				
				selectedJob = dependentData.Job.JobName
				selectedFamilyRel = dependentData.FamilyRel.FamilyRelName
			}
			else {
				dependentSequence = lastSeq + 1
			}
			
			isReady = true
		}
		
		fun isFormValid(): Boolean {
			return dependentName.isNotBlank() && dependentNik.isNotBlank() && dependentFamilyRelId != 0 && dependentBirthDate.isNotBlank() && dependentJobId != 0 && schoolFee.isNotBlank() && dependentFee.isNotBlank()
		}
		
		loadingPopupBox(!isReady)
		
		Column(
			modifier = Modifier.fillMaxSize().background(Colors().panel)
		) {
			LazyColumn(
				modifier = Modifier.fillMaxSize()
			) {
				item{
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
						
						Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
							Text(
								text = if (id == 0) "Tambah Tanggungan" else "Edit Tanggungan",
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
				
				//DependentSequence
				item {
					Text(
						modifier = Modifier.padding(horizontal = 16.dp),
						text = "Tanggungan ke",
						fontSize = 12.sp,
						color = Color.Black,
						fontWeight = FontWeight.Bold
					)
					TextField(
						value = dependentSequence.toString(),
						enabled = false,
						modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp, top = 8.dp)
							.padding(horizontal = 16.dp)
							.border(
								border = BorderStroke(1.dp, Colors().textDarkGrey),
								shape = RoundedCornerShape(4.dp)
							),
						onValueChange = {
							dependentSequence = it.toInt()
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
				
				//DependentName
				item {
					Text(
						modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
						text = "Nama Tanggungan",
						fontSize = 12.sp,
						color = Color.Black,
						fontWeight = FontWeight.Bold
					)
					TextField(
						enabled = true,
						isError = dependentName.length < 4 || dependentName.length > 30,
						modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp, top = 8.dp)
							.padding(horizontal = 16.dp)
							.border(
								border = BorderStroke(1.dp, Colors().textDarkGrey),
								shape = RoundedCornerShape(4.dp)
							),
						placeholder = {
							Text("Masukkan nama tanggungan", fontSize = 16.sp, color = Colors().textDarkGrey)
						},
						value = dependentName,
						onValueChange = {
							if(it.length <= 30) { dependentName = it }
						},
						singleLine = true,
						colors = textFieldColors(
							backgroundColor = Colors().panel,
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
				
				//DependentNIK
				item {
					Text(
						modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
						text = "NIK (Nomor Induk Kependudukan)",
						fontSize = 12.sp,
						color = Color.Black,
						fontWeight = FontWeight.Bold
					)
					TextField(
						enabled = true,
						modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp, top = 8.dp)
							.padding(horizontal = 16.dp)
							.border(
								border = BorderStroke(1.dp, Colors().textDarkGrey),
								shape = RoundedCornerShape(4.dp)
							),
						placeholder = {
							Text("Masukkan NIK", fontSize = 16.sp, color = Colors().textDarkGrey)
						},
						value = dependentNik,
						onValueChange = {
							if(it.length <= 16) { dependentNik = it }
						},
						singleLine = true,
						colors = textFieldColors(
							backgroundColor = Colors().panel,
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
				
				//Family Relation
				item {
					Text(
						modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
						text = "Hubungan Keluarga",
						fontSize = 12.sp,
						color = Color.Black,
						fontWeight = FontWeight.Bold
					)
					
					Box(
						modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(bottom = 12.dp, top = 8.dp)
//							.background(Colors().slate20, RoundedCornerShape(4.dp))
							.border(1.dp, Colors().textDarkGrey, RoundedCornerShape(4.dp))
							.clickable(true, onClick = {
								showFamilyRelPopup = true
							})
					) {
						Row (
							modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
							horizontalArrangement = Arrangement.SpaceBetween,
							verticalAlignment = Alignment.CenterVertically
						){
							Text(
								text = if(selectedFamilyRel == "") "Pilih hubungan keluarga" else selectedFamilyRel,
								fontSize = 14.sp,
								color = if(selectedFamilyRel == "") Colors().textDarkGrey else Colors().textBlack
							)
							Image(painterResource(Res.drawable.Icon_Dropdown_Arrow), null, modifier = Modifier.size(24.dp))
						}
					}
				}
				
				//DependentBirthDate
				item {
					Text(
						modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
						text = "Tanggal Lahir",
						fontSize = 12.sp,
						color = Color.Black,
						fontWeight = FontWeight.Bold
					)
					
					Box(
						modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(bottom = 12.dp, top = 8.dp)
//							.background(Colors().slate20, RoundedCornerShape(4.dp))
							.border(1.dp, Colors().textDarkGrey, RoundedCornerShape(4.dp))
							.clickable(true, onClick = {
								showDatePicker = true
							})
					) {
						Row (
							modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
							horizontalArrangement = Arrangement.SpaceBetween,
							verticalAlignment = Alignment.CenterVertically
						){
							Text(
								text = if(dependentBirthDate == "") "Pilih tanggal lahir" else DateFormatter(dependentBirthDate),
								fontSize = 14.sp,
								color = if(dependentBirthDate == "") Colors().textDarkGrey else Colors().textBlack
							)
							Image(painterResource(Res.drawable.icon_calendar), null, modifier = Modifier.size(24.dp))
						}
					}
				}
				
				//Job
				item {
					Text(
						modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
						text = "Pekerjaan",
						fontSize = 12.sp,
						color = Color.Black,
						fontWeight = FontWeight.Bold
					)
					
					Box(
						modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(bottom = 12.dp,top = 8.dp)
//							.background(Colors().slate20, RoundedCornerShape(4.dp))
							.border(1.dp, Colors().textDarkGrey, RoundedCornerShape(4.dp))
							.clickable(true, onClick = {
								showJobPopup = true
							})
					) {
						Row (
							modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
							horizontalArrangement = Arrangement.SpaceBetween,
							verticalAlignment = Alignment.CenterVertically
						){
							Text(
								text = if(selectedJob == "") "Pilih Pekerjaan" else selectedJob,
								fontSize = 14.sp,
								color = if(selectedJob == "") Colors().textDarkGrey else Colors().textBlack
							)
							Image(painterResource(Res.drawable.Icon_Dropdown_Arrow), null, modifier = Modifier.size(24.dp))
						}
					}
				}
				
				//School Fee
				item {
					Text(
						modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
						text = "Biaya Sekolah / Tahun",
						fontSize = 12.sp,
						color = Color.Black,
						fontWeight = FontWeight.Bold
					)
					TextField(
						enabled = true,
						modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp, top = 8.dp)
							.padding(horizontal = 16.dp)
							.border(
								border = BorderStroke(1.dp, Colors().textDarkGrey),
								shape = RoundedCornerShape(4.dp)
							),
//							.onFocusChanged {
//								if(!it.isFocused) {
//									schoolFee = "Rp. ${CurrencyFormatter(schoolFee)}"
//								}
//							},
						placeholder = {
							Text("Rp. ", fontSize = 16.sp, color = Colors().textDarkGrey)
						},
						value = schoolFee,
						onValueChange = {
							schoolFee = it
						},
						singleLine = true,
						colors = textFieldColors(
							backgroundColor = Colors().panel,
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
				
				//Dependent Fee
				item {
					Text(
						modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
						text = "Biaya Tanggungan / Tahun",
						fontSize = 12.sp,
						color = Color.Black,
						fontWeight = FontWeight.Bold
					)
					TextField(
						enabled = true,
						modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp, top = 8.dp)
							.padding(horizontal = 16.dp)
							.border(
								border = BorderStroke(1.dp, Colors().textDarkGrey),
								shape = RoundedCornerShape(4.dp)
							),
//							.onFocusChanged {
//								if(!it.isFocused) {
//									dependentFee = "Rp. ${CurrencyFormatter(dependentFee)}"
//								}
//							},
						placeholder = {
							Text("Rp. ", fontSize = 16.sp, color = Colors().textDarkGrey)
						},
						value = dependentFee,
						onValueChange = {
							dependentFee = it
						},
						singleLine = true,
						colors = textFieldColors(
							backgroundColor = Colors().panel,
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
				
				//Submit Button
				item {
					Button(
						enabled = isFormValid(),
						modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 22.dp),
						colors = buttonColors(backgroundColor = Colors().buttonActive, contentColor = Color.White),
						onClick = {
							dependentFee = dependentFee.replace("Rp. ", "")
							dependentFee = dependentFee.replace(",", "")
							schoolFee = schoolFee.replace("Rp. ", "")
							schoolFee = schoolFee.replace(",", "")
							
							val dataModel = FormDependentRequestApiModel(
								Id = id,
								Tr1770HdId = sptHd?.Id ?: 0,
								DependentName = dependentName,
								DependentNIK = dependentNik,
								FamilyRelId = dependentFamilyRelId,
								BirthDate = DateFormatter(dependentBirthDate),
								JobId = dependentJobId,
								DependentFee = dependentFee.toLong(),
								SchoolFee = schoolFee.toLong()
							)
							
							println(dataModel)
							
							scope.launch {
								isReady = false
								sptManager.saveDependent(scope, dataModel)
								navigator.pop()
							}
						}
					) {
						Text(
							modifier = Modifier.padding(vertical = 6.dp),
							text = "Selesai",
							fontSize = 16.sp,
							fontWeight = FontWeight.Bold
						)
					}
				}
				
				//Spacer
				item {
					Box(
						modifier = Modifier.height(88.dp)
					)
				}
			}
		}
		
		//Family Relation Popup
		popUpBox(
			popupWidth = 300f,
			popupHeight = 200f,
			showPopup = showFamilyRelPopup,
			onClickOutside = { showFamilyRelPopup = false },
			content = {
				Column(
					modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 12.dp)
				) {
					Text(
						text = "Hubungan Keluarga Dengan Wajib Pajak",
						fontSize = 12.sp,
						color = Colors().textBlack,
						fontWeight = FontWeight.Bold,
						modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
					)
					LazyColumn(modifier = Modifier.heightIn(max = 200.dp).padding(horizontal = 18.dp)) {
						familyRelList.forEach {
							item {
								Text(
									text = it.FamilyRelName,
									fontSize = 18.sp,
									modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
										.clickable(true, onClick = {
											dependentFamilyRelId = it.Id
											selectedFamilyRel = it.FamilyRelName
											showFamilyRelPopup = false
										})
								)
							}
						}
					}
				}
			}
		)
		
		//Date Picker Popup
		popUpBox(
			showPopup = showDatePicker,
			onClickOutside = { showDatePicker = false },
			content = {
				Column {
					Box(modifier = Modifier.height(16.dp))
					WheelDatePicker(
						height = 256.dp,
						title = "Pilih Tanggal Lahir",
						doneLabel = "Selesai",
						yearsRange = IntRange(1920, (sptHd?.TaxYear?.toInt() ?: LocalDate.now().year) - 1),
						titleStyle = TextStyle(fontSize = 16.sp, color = Colors().textBlack, fontWeight = FontWeight.Bold),
						doneLabelStyle = TextStyle(fontSize = 14.sp, color = Colors().textClickable, fontWeight = FontWeight.Bold),
						showShortMonths = true,
						startDate = LocalDate.now().minus(DatePeriod(1,0,0)),
						onDoneClick = {
							dependentBirthDate = it.toString()
							showDatePicker = false
						}
					)
				}
			}
		)
		
		//Job Popup
		popUpBox(
			popupWidth = 300f,
			popupHeight = 200f,
			showPopup = showJobPopup,
			onClickOutside = { showJobPopup = false },
			content = {
				Column(
					modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 12.dp)
				) {
					Text(
						text = "Pekerjaan Tanggungan",
						fontSize = 12.sp,
						color = Colors().textBlack,
						fontWeight = FontWeight.Bold,
						modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
					)
					TextField(
						enabled = true,
						shape = CircleShape,
						modifier = Modifier.fillMaxWidth()
							.padding(horizontal = 8.dp, vertical = 8.dp)
							.border(
								border = BorderStroke(1.dp, Colors().textLightGrey),
								shape = CircleShape
							),
						value = jobSearchText,
						onValueChange = {
							jobSearchText = it
						},
						singleLine = true,
						colors = textFieldColors(
							backgroundColor = Color(0xFFF8F8F8),
							focusedIndicatorColor = Color.Transparent,
							unfocusedIndicatorColor = Color.Transparent,
							disabledIndicatorColor = Colors().slate20
						),
						keyboardOptions = KeyboardOptions(
							keyboardType = KeyboardType.Text,
							imeAction = ImeAction.Done
						),
						leadingIcon = {
							Image(
								painterResource(Res.drawable.icon_search),
								null,
								modifier = Modifier.size(36.dp).padding(start = 16.dp)
							)
						},
						trailingIcon = {
							if(jobSearchText != ""){
								Image(
									painterResource(Res.drawable.icon_clear),
									null,
									modifier = Modifier.size(36.dp).padding(end = 16.dp)
										.clickable(true, onClick = {
											jobSearchText = ""
										})
								)
							}
						}
					)
					LazyColumn(modifier = Modifier.heightIn(max = 200.dp).padding(horizontal = 18.dp)) {
						jobList.forEach {
							if (it.JobName.contains(jobSearchText, ignoreCase = true)) {
								item {
									Text(
										text = it.JobName,
										fontSize = 18.sp,
										modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
											.clickable(true, onClick = {
												selectedJob = it.JobName
												dependentJobId = it.Id
												showJobPopup = false
											})
									)
								}
							}
						}
					}
				}
			}
		)
		
		//Delete Dependent Popup
		popUpBox(
			showPopup = showDeletePopup,
			onClickOutside = { showDeletePopup = false },
			content = {
				Text(
					text = "Hapus Tanggungan",
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
						text = "Hapus Tanggungan",
						fontSize = 18.sp,
						color = Colors().textBlack,
						fontWeight = FontWeight.Bold
					)
					
					Text(
						modifier = Modifier.padding(vertical = 24.dp, horizontal = 16.dp),
						text = "Apakah Anda yakin ingin menghapus Tanggungan ini?",
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
									sptManager.deleteDependent(scope, id)
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