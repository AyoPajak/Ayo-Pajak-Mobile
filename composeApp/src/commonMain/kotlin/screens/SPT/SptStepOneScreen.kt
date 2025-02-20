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
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonColors
import androidx.compose.material.RadioButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import ayopajakmobile.composeapp.generated.resources.Icon_Dropdown_Arrow
import ayopajakmobile.composeapp.generated.resources.Res
import ayopajakmobile.composeapp.generated.resources.icon_clear
import ayopajakmobile.composeapp.generated.resources.icon_search
import ayopajakmobile.composeapp.generated.resources.icon_tripledot_black
import ayopajakmobile.composeapp.generated.resources.placeholder_NPWP
import ayopajakmobile.composeapp.generated.resources.placeholder_username
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import global.AccountRecordType
import global.Colors
import global.Gender
import global.JobName
import global.MaritalStatus
import global.PertamaSptFillingStep
import global.PreferencesKey.Companion.NPWP
import global.PreferencesKey.Companion.WPNIK
import global.PreferencesKey.Companion.WPName
import global.TaxStatus
import global.universalUIComponents.loadingPopupBox
import global.universalUIComponents.popUpBox
import global.universalUIComponents.topBar
import http.Account
import http.Interfaces
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import models.ApiODataQueryModel
import models.master.CityModel
import models.master.JobModel
import models.master.KluModel
import models.transaction.Form1770HdResponseApiModel
import models.transaction.FormIdentityRequestApiModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

class FilteredKluModel(var Id: Int, var KLUCode: String, var KLUName: String)

class SptStepOneScreen(val sptHd: Form1770HdResponseApiModel?, val client: Account, val sptPertamaClient: Interfaces, val prefs: DataStore<Preferences>): Screen {
	
	private val sptManager = SPTManager(prefs, client, sptPertamaClient)
	
	@Composable
	override fun Content() {
		val navigator = LocalNavigator.currentOrThrow
		val scope = rememberCoroutineScope()
		
		val jobKluMap = sptManager.getJobKluMap()
		var isReady by remember { mutableStateOf(false) }
		
		var showCityPopup by remember { mutableStateOf(false) }
		var citySearchText by remember { mutableStateOf("") }
		var showJobPopup by remember { mutableStateOf(false) }
		var jobSearchText by remember { mutableStateOf("") }
		var showKLUPopup by remember { mutableStateOf(false) }
		var kluSearchText by remember { mutableStateOf("") }
		
		var showTaxPayerDependentPopup by remember { mutableStateOf(false) }
		var showSpouseDependentPopup by remember { mutableStateOf(false) }
		
		var taxPayerName by remember { mutableStateOf("") }
		var taxPayerNPWP by remember { mutableStateOf("") }
		var taxPayerNIK by remember { mutableStateOf("") }
		var taxPayerGenderE by remember { mutableStateOf(Gender.Male.value) }
		var taxPayerAccountRecordTypeE by remember { mutableStateOf(AccountRecordType.RECORD.value) }
		var taxPayerCityId by remember { mutableStateOf(0) }
		var taxPayerJobId by remember { mutableStateOf(0) }
		var taxPayerKluId by remember { mutableStateOf(0) }
		var taxPayerPhoneNumber by remember { mutableStateOf("") }
		var taxPayerMaritalStatusE by remember { mutableStateOf(MaritalStatus.NotMarried.value) }
		var taxPayerTaxStatusE by remember { mutableStateOf(TaxStatus.KepalaKeluarga.value) }
		var taxPayerDependentCount by remember { mutableStateOf(0) }
		var isWifeOwnBusiness by remember { mutableStateOf(false) }
		var spouseNPWP by remember { mutableStateOf("") }
		var spouseName by remember { mutableStateOf("") }
		var spouseDependentCount by remember { mutableStateOf(0) }
		
		var cityList by remember { mutableStateOf<List<CityModel>>(emptyList()) }
		var selectedCity by remember { mutableStateOf("") }
		
		var jobList by remember { mutableStateOf<List<JobModel>>(emptyList()) }
		var selectedJob by remember { mutableStateOf("") }
		
		var kluList by remember { mutableStateOf<List<KluModel>>(emptyList()) }
		var selectedKlu by remember { mutableStateOf("") }
		
		var filteredKluList by remember { mutableStateOf<List<FilteredKluModel>>(emptyList()) }
		
		suspend fun populateKluList() {
			var initGetData = true
			val itemCountPerPage = 100
			val odataQuery = ApiODataQueryModel(itemCountPerPage)
			var currentItemCount = 0
			
			while (initGetData || currentItemCount == itemCountPerPage) {
				currentItemCount = 0
				odataQuery.PageNum++
				initGetData = false
				
				try {
					val apiResult = sptManager.getKluList(scope, odataQuery, sptHd!!.TaxYear.toInt())
					if (apiResult.isNotEmpty()) {
						currentItemCount += apiResult.size
						kluList += apiResult
						println(apiResult[apiResult.size - 1].Id)
					} else break
				}
				catch (ex: Exception) {
					break
				}
			}
		}
		
		LaunchedEffect(null) {
			try {
				scope.launch {
					taxPayerName = prefs.data.first()[stringPreferencesKey(WPName)] ?: ""
					taxPayerNPWP = prefs.data.first()[stringPreferencesKey(NPWP)] ?: ""
					taxPayerNIK = prefs.data.first()[stringPreferencesKey(WPNIK)] ?: ""
					
					cityList = sptManager.getCityList(scope)
					jobList = sptManager.getJobList(scope)
					
					//Get KLU List
					populateKluList()
					
					//Prepopulate Form
					if(selectedCity == "") {
						val oldIdentityData = sptManager.getIdentityData(scope, sptHd!!.Id)
//						println(oldIdentityData)
						
						taxPayerGenderE = oldIdentityData?.GenderE ?: Gender.Male.value
						taxPayerAccountRecordTypeE = oldIdentityData!!.AccountRecordE
						taxPayerCityId = oldIdentityData.City!!.Id
						taxPayerJobId = oldIdentityData.Job!!.Id
						taxPayerKluId = oldIdentityData.KLU!!.Id
						taxPayerPhoneNumber = oldIdentityData.TelephoneNo
						taxPayerMaritalStatusE = oldIdentityData.MaritalStatusE
						taxPayerTaxStatusE = oldIdentityData.TaxStatusE
						taxPayerDependentCount = oldIdentityData.DependentCount
						isWifeOwnBusiness = oldIdentityData.IsWifeOwnBusiness
						spouseNPWP = oldIdentityData.SpouseNPWP ?: ""
						spouseName = oldIdentityData.SpouseName ?: ""
						spouseDependentCount = oldIdentityData.SpouseDependentCount ?: 0
						
						selectedCity = oldIdentityData.City.CityName
						selectedJob = oldIdentityData.Job.JobName
						selectedKlu = "(${oldIdentityData.KLU.KLUCode}) ${oldIdentityData.KLU.KLUName}"
					}
					
					isReady = true
				}
			} catch(ex: Exception) {
				println(ex.message)
			}
		}
		
		fun isFormValid(): Boolean {
			return if(taxPayerTaxStatusE == TaxStatus.KepalaKeluarga.value) {
				taxPayerCityId != 0 && taxPayerJobId != 0 && taxPayerKluId != 0 && taxPayerPhoneNumber.isNotBlank()
			} else {
				taxPayerCityId != 0 && taxPayerJobId != 0 && taxPayerKluId != 0 && taxPayerPhoneNumber.isNotBlank() && spouseNPWP.isNotBlank() && spouseName.isNotBlank()
			}
		}
		
		fun filterKluListByJob(jobId: Int) {
			val job = jobList.firstOrNull { it.Id == jobId }
			val jobEnum = if (job != null) enumValues<JobName>().firstOrNull { it.value.equals(job.JobName, true) } else null
			
			if (jobEnum != null && jobKluMap.containsKey(jobEnum))
			{
				val kluCodeList = jobKluMap[jobEnum]!!
				if (kluCodeList.any()) filteredKluList = kluList.filter { kluCodeList.contains(it.KLUCode) }.map { FilteredKluModel(it.Id, it.KLUCode, it.KLUName) }
			}
		}
	
		loadingPopupBox(!isReady)
		
		Column(
			modifier = Modifier.fillMaxSize().background(Colors().panel)
		) {
			LazyColumn(
				modifier = Modifier.fillMaxSize()
			) {
				item {
					topBar("Pengisian Identitas")
				}
				
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
				
				//NIK
				item {
					Text(
						modifier = Modifier.padding(horizontal = 16.dp),
						text = "NIK (Nomor Induk Kependudukan)",
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
						value = taxPayerNIK,
						onValueChange = {
							taxPayerNIK = it
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
				
				//Gender Select
				item {
					Column(
						modifier = Modifier.padding(horizontal = 16.dp)
					) {
						Text(
							text = "Jenis Kelamin",
							fontSize = 12.sp,
							color = Color.Black,
							fontWeight = FontWeight.Bold
						)
						Row(modifier = Modifier.selectableGroup().fillMaxWidth()){
							Row(modifier = Modifier.weight(.5f), verticalAlignment = Alignment.CenterVertically) {
								RadioButton(
									selected = taxPayerGenderE == Gender.Male.value,
									onClick = { taxPayerGenderE = Gender.Male.value },
									colors = RadioButtonDefaults.colors(selectedColor = Colors().buttonActive)
								)
								Text(
									text = "Laki-laki",
									fontSize = 14.sp,
									color = Colors().textBlack,
									modifier = Modifier.clickable(true, onClick = {
										taxPayerGenderE = Gender.Male.value
									})
								)
							}
							Row(modifier = Modifier.weight(.5f), verticalAlignment = Alignment.CenterVertically) {
								RadioButton(
									selected = taxPayerGenderE == Gender.Female.value,
									onClick = { taxPayerGenderE = Gender.Female.value },
									colors = RadioButtonDefaults.colors(selectedColor = Colors().buttonActive)
								)
								Text(
									text = "Perempuan",
									fontSize = 14.sp,
									color = Colors().textBlack,
									modifier = Modifier.clickable(true, onClick = {
										taxPayerGenderE = Gender.Female.value
									})
								)
							}
						}
					}
				}
				
				//Account Record Type
				if (sptHd!!.SPTType == "1770") {
					item {
						Column(
							modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp)
						) {
							Text(
								text = "Jenis Pencatatan",
								fontSize = 12.sp,
								color = Color.Black,
								fontWeight = FontWeight.Bold
							)
							Row(modifier = Modifier.selectableGroup().fillMaxWidth()) {
								Row(
									modifier = Modifier.weight(.5f),
									verticalAlignment = Alignment.CenterVertically
								) {
									RadioButton(
										selected = taxPayerAccountRecordTypeE == AccountRecordType.BOOKKEEPING.value,
										onClick = { taxPayerAccountRecordTypeE = AccountRecordType.BOOKKEEPING.value },
										colors = RadioButtonDefaults.colors(selectedColor = Colors().buttonActive)
									)
									Text(
										text = "Pembukuan",
										fontSize = 14.sp,
										color = Colors().textBlack,
										modifier = Modifier.clickable(true, onClick = {
											taxPayerAccountRecordTypeE = AccountRecordType.BOOKKEEPING.value
										})
									)
								}
								Row(
									modifier = Modifier.weight(.5f),
									verticalAlignment = Alignment.CenterVertically
								) {
									RadioButton(
										selected = taxPayerAccountRecordTypeE == AccountRecordType.RECORD.value,
										onClick = { taxPayerAccountRecordTypeE = AccountRecordType.RECORD.value },
										colors = RadioButtonDefaults.colors(selectedColor = Colors().buttonActive)
									)
									Text(
										text = "Pencatatan",
										fontSize = 14.sp,
										color = Colors().textBlack,
										modifier = Modifier.clickable(true, onClick = {
											taxPayerAccountRecordTypeE = AccountRecordType.RECORD.value
										})
									)
								}
							}
						}
					}
				}
				
				//City
				item {
					Text(
						modifier = Modifier.padding(horizontal = 16.dp).padding(top = 18.dp),
						text = "Kota Wajib Pajak",
						fontSize = 12.sp,
						color = Color.Black,
						fontWeight = FontWeight.Bold
					)
					
					Box(
						modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 8.dp)
//							.background(Colors().slate20, RoundedCornerShape(4.dp))
							.border(1.dp, Colors().textDarkGrey, RoundedCornerShape(4.dp))
							.clickable(true, onClick = {
								showCityPopup = true
							})
					) {
						Row (
							modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
							horizontalArrangement = Arrangement.SpaceBetween,
							verticalAlignment = Alignment.CenterVertically
						){
							Text(
								text = if(selectedCity == "") "Pilih Kota" else selectedCity,
								fontSize = 14.sp,
								color = if(selectedCity == "") Colors().textDarkGrey else Colors().textBlack
							)
							Image(painterResource(Res.drawable.Icon_Dropdown_Arrow), null, modifier = Modifier.size(24.dp))
						}
					}
				}
				
				//Job
				item {
					Text(
						modifier = Modifier.padding(horizontal = 16.dp).padding(top = 18.dp),
						text = "Pekerjaan",
						fontSize = 12.sp,
						color = Color.Black,
						fontWeight = FontWeight.Bold
					)
					
					Box(
						modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 8.dp)
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
				
				//Job CLass
				item {
					Text(
						modifier = Modifier.padding(horizontal = 16.dp).padding(top = 18.dp),
						text = "KLU",
						fontSize = 12.sp,
						color = Color.Black,
						fontWeight = FontWeight.Bold
					)
					
					Box(
						modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 8.dp)
//							.background(Colors().slate20, RoundedCornerShape(4.dp))
							.border(1.dp, Colors().textDarkGrey, RoundedCornerShape(4.dp))
							.clickable(true, onClick = {
								showKLUPopup = true
							})
					) {
						Row (
							modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
							horizontalArrangement = Arrangement.SpaceBetween,
							verticalAlignment = Alignment.CenterVertically
						){
							Text(
								text = if(selectedKlu == "") "Pilih Klasifikasi Lapangan Usaha" else selectedKlu,
								fontSize = 14.sp,
								color = if(selectedKlu == "") Colors().textDarkGrey else Colors().textBlack
							)
							Image(painterResource(Res.drawable.Icon_Dropdown_Arrow), null, modifier = Modifier.size(24.dp))
						}
					}
				}
				
				//Phone Number
				item {
					Text(
						modifier = Modifier.padding(horizontal = 16.dp).padding(top = 18.dp),
						text = "No. Telepon / Handphone",
						fontSize = 12.sp,
						color = Color.Black,
						fontWeight = FontWeight.Bold
					)
					TextField(
						enabled = true,
						isError = taxPayerPhoneNumber.length < 8 || taxPayerPhoneNumber.length > 30,
						modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp, top = 8.dp)
							.padding(horizontal = 16.dp)
							.border(
								border = BorderStroke(1.dp, Colors().textDarkGrey),
								shape = RoundedCornerShape(4.dp)
							),
						placeholder = {
							Text(
								"Masukkan nomor telepon/handphone", fontSize = 16.sp,
								color = Colors().textDarkGrey
							)
						},
						value = taxPayerPhoneNumber,
						onValueChange = {
							if(taxPayerPhoneNumber.length <= 30) { taxPayerPhoneNumber = it }
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
				
				//Marital Status: 1 = belum/tidak menikah, 3 = menikah
				item {
					Column(
						modifier = Modifier.padding(horizontal = 16.dp)
					){
						Text(
							text = "Status Pernikahan:",
							fontSize = 12.sp,
							color = Color.Black,
							fontWeight = FontWeight.Bold
						)
						Row(modifier = Modifier.selectableGroup().fillMaxWidth()){
							Row(modifier = Modifier.weight(.5f), verticalAlignment = Alignment.CenterVertically) {
								RadioButton(
									selected = taxPayerMaritalStatusE == MaritalStatus.NotMarried.value,
									onClick = { taxPayerMaritalStatusE = MaritalStatus.NotMarried.value
														taxPayerTaxStatusE = TaxStatus.KepalaKeluarga.value
														isWifeOwnBusiness = false
														},
									colors = RadioButtonDefaults.colors(selectedColor = Colors().buttonActive)
								)
								Text(
									text = "Belum Menikah",
									fontSize = 14.sp,
									color = Colors().textBlack,
									modifier = Modifier.clickable(true, onClick = {
										taxPayerMaritalStatusE = MaritalStatus.NotMarried.value
										taxPayerTaxStatusE = TaxStatus.KepalaKeluarga.value
										isWifeOwnBusiness = false
									})
								)
							}
							Row(modifier = Modifier.weight(.5f), verticalAlignment = Alignment.CenterVertically) {
								RadioButton(
									selected = taxPayerMaritalStatusE == MaritalStatus.Married.value,
									onClick = { taxPayerMaritalStatusE = MaritalStatus.Married.value},
									colors = RadioButtonDefaults.colors(selectedColor = Colors().buttonActive)
								)
								Text(
									text = "Sudah Menikah",
									fontSize = 14.sp,
									color = Colors().textBlack,
									modifier = Modifier.clickable(true, onClick = {
										taxPayerMaritalStatusE = MaritalStatus.Married.value
									})
								)
							}
						}
					}
				}
				
				//Tax Status: KK = Kepala Keluarga, HB = Hidup Berpisah, PH = Pisah Harta, MT = Memilih Terpisah
				item {
					Column(
						modifier = Modifier.padding(horizontal = 16.dp).padding(top = 16.dp)
					){
						Text(
							text = "Status Perpajakan:",
							fontSize = 12.sp,
							color = Color.Black,
							fontWeight = FontWeight.Bold
						)
						Column(modifier = Modifier.selectableGroup().fillMaxWidth()) {
							Row {
								Row(modifier = Modifier.weight(.5f), verticalAlignment = Alignment.CenterVertically) {
									RadioButton(
										enabled = taxPayerMaritalStatusE == MaritalStatus.Married.value,
										selected = taxPayerTaxStatusE == TaxStatus.KepalaKeluarga.value,
										onClick = { taxPayerTaxStatusE = TaxStatus.KepalaKeluarga.value },
										colors = RadioButtonDefaults.colors(selectedColor = Colors().buttonActive, disabledColor = Colors().buttonActive)
									)
									Text(
										text = "Kepala Keluarga",
										fontSize = 14.sp,
										color = Colors().textBlack,
										modifier = Modifier.clickable(enabled = taxPayerMaritalStatusE == MaritalStatus.Married.value, onClick = {
											taxPayerTaxStatusE = TaxStatus.KepalaKeluarga.value
										})
									)
								}
								Row(modifier = Modifier.weight(.5f), verticalAlignment = Alignment.CenterVertically) {
									RadioButton(
										enabled = taxPayerMaritalStatusE == MaritalStatus.Married.value,
										selected = taxPayerTaxStatusE == TaxStatus.HidupBerpisah.value,
										onClick = { taxPayerTaxStatusE = TaxStatus.HidupBerpisah.value
															isWifeOwnBusiness = false},
										colors = RadioButtonDefaults.colors(selectedColor = Colors().buttonActive)
									)
									Text(
										text = "Hidup Berpisah",
										fontSize = 14.sp,
										color = if(taxPayerMaritalStatusE == MaritalStatus.Married.value) Colors().textBlack else Colors().textDarkGrey,
										modifier = Modifier.clickable(enabled = taxPayerMaritalStatusE == MaritalStatus.Married.value, onClick = {
											taxPayerTaxStatusE = TaxStatus.HidupBerpisah.value
											isWifeOwnBusiness = false
										})
									)
								}
							}
							
							Row {
								Row(modifier = Modifier.weight(.5f), verticalAlignment = Alignment.CenterVertically) {
									RadioButton(
										enabled = taxPayerMaritalStatusE == MaritalStatus.Married.value,
										selected = taxPayerTaxStatusE == TaxStatus.PisahHarta.value,
										onClick = { taxPayerTaxStatusE = TaxStatus.PisahHarta.value
															isWifeOwnBusiness = false},
										colors = RadioButtonDefaults.colors(selectedColor = Colors().buttonActive)
									)
									Text(
										text = "Pisah Harta",
										fontSize = 14.sp,
										color = if(taxPayerMaritalStatusE == MaritalStatus.Married.value) Colors().textBlack else Colors().textDarkGrey,
										modifier = Modifier.clickable(enabled = taxPayerMaritalStatusE == MaritalStatus.Married.value, onClick = {
											taxPayerTaxStatusE = TaxStatus.PisahHarta.value
											isWifeOwnBusiness = false
										})
									)
								}
								Row(modifier = Modifier.weight(.5f), verticalAlignment = Alignment.CenterVertically) {
									RadioButton(
										enabled = taxPayerMaritalStatusE == MaritalStatus.Married.value,
										selected = taxPayerTaxStatusE == TaxStatus.MemilihTerpisahKewajibanPajak.value,
										onClick = { taxPayerTaxStatusE = TaxStatus.MemilihTerpisahKewajibanPajak.value
															isWifeOwnBusiness = false},
										colors = RadioButtonDefaults.colors(selectedColor = Colors().buttonActive)
									)
									Text(
										text = "Memilih Terpisah",
										fontSize = 14.sp,
										color = if(taxPayerMaritalStatusE == MaritalStatus.Married.value) Colors().textBlack else Colors().textDarkGrey,
										modifier = Modifier.clickable(enabled = taxPayerMaritalStatusE == MaritalStatus.Married.value, onClick = {
											taxPayerTaxStatusE = TaxStatus.MemilihTerpisahKewajibanPajak.value
											isWifeOwnBusiness = false
										})
									)
								}
							}
						}
					}
				}
				
				//Dependent Count
				item {
					Text(
						modifier = Modifier.padding(horizontal = 16.dp).padding(top = 18.dp),
						text = "Jumlah Tanggungan",
						fontSize = 12.sp,
						color = Color.Black,
						fontWeight = FontWeight.Bold
					)
					
					Box(
						modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 8.dp)
//							.background(Colors().slate20, RoundedCornerShape(4.dp))
							.border(1.dp, Colors().textDarkGrey, RoundedCornerShape(4.dp))
							.clickable(true, onClick = {
								showTaxPayerDependentPopup = true
							})
					) {
						Row (
							modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
							horizontalArrangement = Arrangement.SpaceBetween,
							verticalAlignment = Alignment.CenterVertically
						){
							Text(
								text = if(taxPayerDependentCount == 0) "Tidak ada tanggungan" else "$taxPayerDependentCount Orang",
								fontSize = 14.sp,
								color = Colors().textBlack
							)
							Image(painterResource(Res.drawable.Icon_Dropdown_Arrow), null, modifier = Modifier.size(24.dp))
						}
					}
				}
				
				//IsWifeOwnBusiness
				item {
					Text(
						modifier = Modifier.padding(horizontal = 16.dp).padding(top = 18.dp),
						text = "Istri memiliki usaha sendiri",
						fontSize = 14.sp,
						color = Color.Black,
						fontWeight = FontWeight.SemiBold
					)
					Row(
						modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 8.dp),
						verticalAlignment = Alignment.Top
					) {
						Checkbox(
							enabled = taxPayerTaxStatusE == TaxStatus.KepalaKeluarga.value && taxPayerMaritalStatusE == MaritalStatus.Married.value,
							checked = isWifeOwnBusiness && taxPayerTaxStatusE == TaxStatus.KepalaKeluarga.value,
							onCheckedChange = { isWifeOwnBusiness = it },
							colors = CheckboxDefaults.colors(checkedColor = Colors().buttonActive)
						)
						Text (
							text = "Centang apabila istri memiliki usaha sendiri dan [Status Perpajakan] anda sebagai Kepala Keluaraga (Penghasilan join income)",
							fontSize = 14.sp,
							color = if(taxPayerTaxStatusE == TaxStatus.KepalaKeluarga.value && taxPayerMaritalStatusE == MaritalStatus.Married.value) Colors().textBlack else Colors().textDarkGrey,
							modifier = Modifier.clickable(taxPayerTaxStatusE == TaxStatus.KepalaKeluarga.value && taxPayerMaritalStatusE == MaritalStatus.Married.value, onClick = {
								isWifeOwnBusiness = !isWifeOwnBusiness
							})
						)
					}
				}
				
				//SpouseNPWP
				item {
					Text(
						modifier = Modifier.padding(horizontal = 16.dp).padding(top = 18.dp),
						text = "NPWP Pasangan",
						fontSize = 12.sp,
						color = Color.Black,
						fontWeight = FontWeight.Bold
					)
					TextField(
						enabled = taxPayerTaxStatusE != TaxStatus.KepalaKeluarga.value,
						modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
							.padding(horizontal = 16.dp)
							.border(
								border = BorderStroke(1.dp, Colors().textDarkGrey),
								shape = RoundedCornerShape(4.dp)
							),
						placeholder = {
							Text(
								"", fontSize = 16.sp,
								color = Colors().textDarkGrey
							)
						},
						value = spouseNPWP,
						onValueChange = {
							spouseNPWP = it
						},
						singleLine = true,
						colors = textFieldColors(
							backgroundColor = if(taxPayerTaxStatusE != TaxStatus.KepalaKeluarga.value) Colors().panel else Colors().slate20,
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
				
				//SpouseName
				item {
					Text(
						modifier = Modifier.padding(horizontal = 16.dp).padding(top = 18.dp),
						text = "Nama Pasangan",
						fontSize = 12.sp,
						color = Color.Black,
						fontWeight = FontWeight.Bold
					)
					TextField(
						enabled = taxPayerTaxStatusE != TaxStatus.KepalaKeluarga.value,
						modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
							.padding(horizontal = 16.dp)
							.border(
								border = BorderStroke(1.dp, Colors().textDarkGrey),
								shape = RoundedCornerShape(4.dp)
							),
						placeholder = {
							Text(
								"", fontSize = 16.sp,
								color = Colors().textDarkGrey
							)
						},
						value = spouseName,
						onValueChange = {
							if(spouseName.length <= 200) { spouseName = it }
						},
						singleLine = true,
						colors = textFieldColors(
							backgroundColor = if(taxPayerTaxStatusE != TaxStatus.KepalaKeluarga.value) Colors().panel else Colors().slate20,
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
				
				//Spouse Dependent Count
				item {
					Text(
						modifier = Modifier.padding(horizontal = 16.dp).padding(top = 18.dp),
						text = "Jumlah Tanggungan Pasangan",
						fontSize = 12.sp,
						color = Color.Black,
						fontWeight = FontWeight.Bold
					)
					
					Box(
						modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 8.dp)
							.background(if(taxPayerTaxStatusE != TaxStatus.KepalaKeluarga.value) Colors().panel else Colors().slate20, RoundedCornerShape(4.dp))
							.border(1.dp, Colors().textDarkGrey, RoundedCornerShape(4.dp))
							.clickable(taxPayerTaxStatusE != TaxStatus.KepalaKeluarga.value, onClick = {
								showSpouseDependentPopup = true
							})
					) {
						Row (
							modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
							horizontalArrangement = Arrangement.SpaceBetween,
							verticalAlignment = Alignment.CenterVertically
						){
							Text(
								text = if(spouseDependentCount == 0) "Tidak ada tanggungan" else "$spouseDependentCount Orang",
								fontSize = 14.sp,
								color = if(taxPayerTaxStatusE != TaxStatus.KepalaKeluarga.value) Colors().textBlack else Colors().textDarkGrey
							)
							Image(painterResource(Res.drawable.Icon_Dropdown_Arrow), null, modifier = Modifier.size(24.dp))
						}
					}
				}
				
				//Submit Button
				item {
					Button(
						enabled = isFormValid(),
						modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 22.dp),
						colors = buttonColors(backgroundColor = Colors().buttonActive, contentColor = Color.White),
						onClick = {
							isReady = false
							val dataModel = if(taxPayerTaxStatusE == TaxStatus.KepalaKeluarga.value){
								FormIdentityRequestApiModel(
									Tr1770HdId = sptHd.Id,
									AccountRecordE = taxPayerAccountRecordTypeE,
									CityId = taxPayerCityId,
									JobId = taxPayerJobId,
									KLUId = taxPayerKluId,
									TelephoneNo = taxPayerPhoneNumber,
									MaritalStatusE = taxPayerMaritalStatusE,
									TaxStatusE = taxPayerTaxStatusE,
									DependentCount = taxPayerDependentCount,
									IsWifeOwnBusiness = isWifeOwnBusiness,
									HandphoneNo = null,
									SpouseNPWP = null,
									SpouseName = null,
									SpouseDependentCount = null
								)
							}
							else {
								FormIdentityRequestApiModel(
									Tr1770HdId = sptHd.Id,
									AccountRecordE = taxPayerAccountRecordTypeE,
									CityId = taxPayerCityId,
									JobId = taxPayerJobId,
									KLUId = taxPayerKluId,
									TelephoneNo = taxPayerPhoneNumber,
									MaritalStatusE = taxPayerMaritalStatusE,
									TaxStatusE = taxPayerTaxStatusE,
									DependentCount = taxPayerDependentCount,
									IsWifeOwnBusiness = isWifeOwnBusiness,
									HandphoneNo = null,
									SpouseNPWP = spouseNPWP,
									SpouseName = spouseName,
									SpouseDependentCount = spouseDependentCount
								)
							}
							
							println(dataModel)
							
							scope.launch {
								sptManager.saveIdentity(scope, dataModel)
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
		
		//Popups
		//City Popup
		popUpBox(
			popupWidth = 300f,
			popupHeight = 200f,
			showPopup = showCityPopup,
			onClickOutside = { showCityPopup = false },
			content = {
				Column(
					modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 12.dp)
				) {
					Text(
						text = "Kota Wajib Pajak",
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
						value = citySearchText,
						onValueChange = {
							citySearchText = it
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
							if(citySearchText != ""){
								Image(
									painterResource(Res.drawable.icon_clear),
									null,
									modifier = Modifier.size(36.dp).padding(end = 16.dp)
										.clickable(true, onClick = {
											citySearchText = ""
										})
								)
							}
						}
					)
					LazyColumn(modifier = Modifier.heightIn(max = 200.dp).padding(horizontal = 18.dp)) {
						cityList.forEach {
							if (it.CityName.contains(citySearchText, ignoreCase = true)) {
								item {
									Text(
										text = it.CityName,
										fontSize = 18.sp,
										modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
											.clickable(true, onClick = {
												selectedCity = it.CityName
												taxPayerCityId = it.Id
												showCityPopup = false
											})
									)
								}
							}
						}
					}
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
						text = "Pekerjaan Wajib Pajak",
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
							if (it.JobName.contains(jobSearchText, ignoreCase = true) && it.JobName != JobName.Balita.value) {
								item {
									Text(
										text = it.JobName,
										fontSize = 18.sp,
										modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
											.clickable(true, onClick = {
												selectedJob = it.JobName
												taxPayerJobId = it.Id
												showJobPopup = false
												
												filterKluListByJob(it.Id)
											})
									)
								}
							}
						}
					}
				}
			}
		)
		
		//KLU Popup
		popUpBox(
			popupWidth = 300f,
			popupHeight = 200f,
			showPopup = showKLUPopup,
			onClickOutside = { showKLUPopup = false },
			content = {
				Column(
					modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 12.dp)
				) {
					Text(
						text = "Klasifikasi Lapangan Usaha Wajib Pajak",
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
						value = kluSearchText,
						onValueChange = {
							kluSearchText = it
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
							if(kluSearchText != ""){
								Image(
									painterResource(Res.drawable.icon_clear),
									null,
									modifier = Modifier.size(36.dp).padding(end = 16.dp)
										.clickable(true, onClick = {
											kluSearchText = ""
										})
								)
							}
						}
					)
					LazyColumn(modifier = Modifier.heightIn(max = 200.dp).padding(horizontal = 18.dp)) {
						if(selectedJob == JobName.Pegawai.value || selectedJob == JobName.Pelajar.value || selectedJob == JobName.Mahasiswa.value){
							filteredKluList.forEach {
								if (it.KLUName.contains(kluSearchText, ignoreCase = true)) {
									item {
										Text(
											text = "(${it.KLUCode}) ${it.KLUName}",
											fontSize = 18.sp,
											modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
												.clickable(true, onClick = {
													selectedKlu = "(${it.KLUCode}) ${it.KLUName}"
													taxPayerKluId = it.Id
													showKLUPopup = false
												})
										)
									}
								}
							}
						}
						else {
							kluList.forEach {
								if (it.KLUName.contains(kluSearchText, ignoreCase = true)) {
									item {
										Text(
											text = "(${it.KLUCode}) ${it.KLUName}",
											fontSize = 18.sp,
											modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
												.clickable(true, onClick = {
													selectedKlu = "(${it.KLUCode}) ${it.KLUName}"
													taxPayerKluId = it.Id
													showKLUPopup = false
												})
										)
									}
								}
							}
						}
					}
				}
			}
		)
		
		//Tax Payer Dependent Popup
		popUpBox(
			popupWidth = 300f,
			popupHeight = 200f,
			showPopup = showTaxPayerDependentPopup,
			onClickOutside = { showTaxPayerDependentPopup = false },
			content = {
				Column(
					modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 12.dp)
				) {
					Text(
						text = "Jumlah Tanggungan Wajib Pajak",
						fontSize = 12.sp,
						color = Colors().textBlack,
						fontWeight = FontWeight.Bold,
						modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
					)
					LazyColumn(modifier = Modifier.heightIn(max = 200.dp).padding(horizontal = 18.dp)) {
						for (i in 0..8) {
							item {
								Text(
									text = if(i == 0)"Tidak ada tanggungan" else "$i Orang",
									fontSize = 18.sp,
									modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
										.clickable(true, onClick = {
											taxPayerDependentCount = i
											showTaxPayerDependentPopup = false
										})
								)
							}
						}
					}
				}
			}
		)
		
		//Spouse Dependent Popup
		popUpBox(
			popupWidth = 300f,
			popupHeight = 200f,
			showPopup = showSpouseDependentPopup,
			onClickOutside = { showSpouseDependentPopup = false },
			content = {
				Column(
					modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 12.dp)
				) {
					Text(
						text = "Jumlah Tanggungan Wajib Pajak",
						fontSize = 12.sp,
						color = Colors().textBlack,
						fontWeight = FontWeight.Bold,
						modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
					)
					LazyColumn(modifier = Modifier.heightIn(max = 200.dp).padding(horizontal = 18.dp)) {
						for (i in 0..3) {
							item {
								Text(
									text = if(i == 0)"Tidak ada tanggungan" else "$i Orang",
									fontSize = 18.sp,
									modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
										.clickable(true, onClick = {
											spouseDependentCount = i
											showSpouseDependentPopup = false
										})
								)
							}
						}
					}
				}
			}
		)
	}
}