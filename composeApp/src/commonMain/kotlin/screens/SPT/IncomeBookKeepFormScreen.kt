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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxColors
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import ayopajakmobile.composeapp.generated.resources.Icon_Dropdown_Arrow
import ayopajakmobile.composeapp.generated.resources.Res
import ayopajakmobile.composeapp.generated.resources.arrow_back
import ayopajakmobile.composeapp.generated.resources.icon_infocircle
import ayopajakmobile.composeapp.generated.resources.icon_tripledot_black
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import global.AccountRecordType
import global.AuditOpinion
import global.Colors
import global.TaxType
import global.universalUIComponents.loadingPopupBox
import global.universalUIComponents.popUpBox
import global.universalUIComponents.topBar
import http.Account
import http.Interfaces
import kotlinx.coroutines.launch
import models.transaction.Form1770HdResponseApiModel
import models.transaction.FormBookKeepingRequestApiModel
import models.transaction.FormTaxCreditARequestApiModel
import models.transaction.FormTaxCreditBRequestApiModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource
import screens.divider
import util.BigDeciToLong
import util.CurrencyFormatter

class IncomeBookKeepFormScreen(val sptHd: Form1770HdResponseApiModel?, val client: Account, val sptPertamaClient: Interfaces, val prefs: DataStore<Preferences>): Screen {
	
	val sptManager = SPTManager(prefs, client, sptPertamaClient)
	
	@Composable
	override fun Content() {
		
		val scope = rememberCoroutineScope()
		val navigator = LocalNavigator.currentOrThrow
		
		var taxPayerAccountRecordTypeE by remember { mutableStateOf(0) }
		
		var showAuditOpinionPopup by remember { mutableStateOf(false) }
		var expandP1 by remember { mutableStateOf(false) }
		var expandP2 by remember { mutableStateOf(false) }
		var expandP3 by remember { mutableStateOf(false) }
		
		var isAudited by remember { mutableStateOf(false) }
		var auditOpinionE by remember { mutableStateOf(0) }
		var selectedAuditOpinion by remember { mutableStateOf("") }
		var pubAccName by remember { mutableStateOf("") }
		var pubAccNPWP by remember { mutableStateOf("") }
		var pubAccOfficeName by remember { mutableStateOf("") }
		var pubAccOfficeNPWP by remember { mutableStateOf("") }
		var taxConsultantName by remember { mutableStateOf("") }
		var taxConsultantNPWP by remember { mutableStateOf("") }
		var taxConsultantOfficeName by remember { mutableStateOf("") }
		var taxConsultantOfficeNPWP by remember { mutableStateOf("") }
		
		var businessCirculationIDR by remember { mutableStateOf("") }
		var businessCirculationIDRActual by remember { mutableStateOf(0L) }
		var hppIDR by remember { mutableStateOf("") }
		var hppIDRActual by remember { mutableStateOf(0L) }
		var profitLossIDR by remember { mutableStateOf("") }
		var profitLossIDRActual by remember { mutableStateOf(0L) }
		var businessCostIDR by remember { mutableStateOf("") }
		var businessCostIDRActual by remember { mutableStateOf(0L) }
		var nettIncomeIDR by remember { mutableStateOf("") }
		var nettIncomeIDRActual by remember { mutableStateOf(0L) }
		
		var p2aIDR by remember { mutableStateOf("") }
		var p2aIDRActual by remember { mutableStateOf(0L) }
		var p2bIDR by remember { mutableStateOf("") }
		var p2bIDRActual by remember { mutableStateOf(0L) }
		var p2cIDR by remember { mutableStateOf("") }
		var p2cIDRActual by remember { mutableStateOf(0L) }
		var p2dIDR by remember { mutableStateOf("") }
		var p2dIDRActual by remember { mutableStateOf(0L) }
		var p2eIDR by remember { mutableStateOf("") }
		var p2eIDRActual by remember { mutableStateOf(0L) }
		var p2fIDR by remember { mutableStateOf("") }
		var p2fIDRActual by remember { mutableStateOf(0L) }
		var p2gIDR by remember { mutableStateOf("") }
		var p2gIDRActual by remember { mutableStateOf(0L) }
		var p2hIDR by remember { mutableStateOf("") }
		var p2hIDRActual by remember { mutableStateOf(0L) }
		var p2iIDR by remember { mutableStateOf("") }
		var p2iIDRActual by remember { mutableStateOf(0L) }
		var p2jIDR by remember { mutableStateOf("") }
		var p2jIDRActual by remember { mutableStateOf(0L) }
		var p2kIDR by remember { mutableStateOf("") }
		var p2kIDRActual by remember { mutableStateOf(0L) }
		var p2lIDR by remember { mutableStateOf("") }
		var p2lIDRActual by remember { mutableStateOf(0L) }
		
		var p3aIDR by remember { mutableStateOf("") }
		var p3aIDRActual by remember { mutableStateOf(0L) }
		var p3bIDR by remember { mutableStateOf("") }
		var p3bIDRActual by remember { mutableStateOf(0L) }
		var p3cIDR by remember { mutableStateOf("") }
		var p3cIDRActual by remember { mutableStateOf(0L) }
		var p3dIDR by remember { mutableStateOf("") }
		var p3dIDRActual by remember { mutableStateOf(0L) }
		
		var finalIDR by remember { mutableStateOf("") }
		var finalIDRActual by remember { mutableStateOf(0L) }
		var lossCompIDR by remember { mutableStateOf("") }
		var lossCompIDRActual by remember { mutableStateOf(0L) }
		
		var isReady by remember { mutableStateOf(false) }
		
		LaunchedEffect(null) {
			val oldData = sptManager.getBookKeepingData(scope, sptHd!!.Id.toString())
			println(oldData)
			
			if(oldData != null) {
				isAudited = oldData.IsAudited
				auditOpinionE = oldData.AuditOpinionE ?: 0
				selectedAuditOpinion = AuditOpinion.fromValue(auditOpinionE) ?: ""
				pubAccName = oldData.PubAccName ?: ""
				pubAccNPWP = oldData.PubAccNPWP ?: ""
				pubAccOfficeName = oldData.PubAccOfficeName ?: ""
				pubAccOfficeNPWP = oldData.PubAccOfficeNPWP ?: ""
				taxConsultantName = oldData.TaxConsultantName ?: ""
				taxConsultantNPWP = oldData.TaxConsultantNPWP ?: ""
				taxConsultantOfficeName = oldData.TaxConsultantOfficeName ?: ""
				taxConsultantOfficeNPWP = oldData.TaxConsultantOfficeNPWP ?: ""
				
				businessCirculationIDRActual = BigDeciToLong(oldData.BusinessCirculationIDR.toString())
				hppIDRActual = BigDeciToLong(oldData.HPPIDR.toString())
				profitLossIDRActual = BigDeciToLong(oldData.ProfitLossIDR.toString())
				businessCostIDRActual = BigDeciToLong(oldData.BusinessCostIDR.toString())
				nettIncomeIDRActual = BigDeciToLong(oldData.NettIncomeIDR.toString())
				
				p2aIDRActual = BigDeciToLong(oldData.P2A_IDR.toString())
				p2bIDRActual = BigDeciToLong(oldData.P2B_IDR.toString())
				p2cIDRActual = BigDeciToLong(oldData.P2C_IDR.toString())
				p2dIDRActual = BigDeciToLong(oldData.P2D_IDR.toString())
				p2eIDRActual = BigDeciToLong(oldData.P2E_IDR.toString())
				p2fIDRActual = BigDeciToLong(oldData.P2F_IDR.toString())
				p2gIDRActual = BigDeciToLong(oldData.P2G_IDR.toString())
				p2hIDRActual = BigDeciToLong(oldData.P2H_IDR.toString())
				p2iIDRActual = BigDeciToLong(oldData.P2I_IDR.toString())
				p2jIDRActual = BigDeciToLong(oldData.P2J_IDR.toString())
				p2kIDRActual = BigDeciToLong(oldData.P2K_IDR.toString())
				p2lIDRActual = BigDeciToLong(oldData.P2L_IDR.toString())
				
				p3aIDRActual = BigDeciToLong(oldData.P3A_IDR.toString())
				p3bIDRActual = BigDeciToLong(oldData.P3B_IDR.toString())
				p3cIDRActual = BigDeciToLong(oldData.P3C_IDR.toString())
				p3dIDRActual = BigDeciToLong(oldData.P3D_IDR.toString())
				
				finalIDRActual = BigDeciToLong(oldData.Final_IDR.toString())
				lossCompIDRActual = BigDeciToLong(oldData.LossCompensationIDR.toString())
				
				businessCirculationIDR = CurrencyFormatter(businessCirculationIDRActual.toString())
				hppIDR = CurrencyFormatter(hppIDRActual.toString())
				profitLossIDR = CurrencyFormatter(profitLossIDRActual.toString())
				businessCostIDR = CurrencyFormatter(businessCostIDRActual.toString())
				nettIncomeIDR = CurrencyFormatter(nettIncomeIDRActual.toString())
				
				p2aIDR = CurrencyFormatter(p2aIDRActual.toString())
				p2bIDR = CurrencyFormatter(p2bIDRActual.toString())
				p2cIDR = CurrencyFormatter(p2cIDRActual.toString())
				p2dIDR = CurrencyFormatter(p2dIDRActual.toString())
				p2eIDR = CurrencyFormatter(p2eIDRActual.toString())
				p2fIDR = CurrencyFormatter(p2fIDRActual.toString())
				p2gIDR = CurrencyFormatter(p2gIDRActual.toString())
				p2hIDR = CurrencyFormatter(p2hIDRActual.toString())
				p2iIDR = CurrencyFormatter(p2iIDRActual.toString())
				p2jIDR = CurrencyFormatter(p2jIDRActual.toString())
				p2kIDR = CurrencyFormatter(p2kIDRActual.toString())
				p2lIDR = CurrencyFormatter(p2lIDRActual.toString())
				
				p3aIDR = CurrencyFormatter(p3aIDRActual.toString())
				p3bIDR = CurrencyFormatter(p3bIDRActual.toString())
				p3cIDR = CurrencyFormatter(p3cIDRActual.toString())
				p3dIDR = CurrencyFormatter(p3dIDRActual.toString())
				
				finalIDR = CurrencyFormatter(finalIDRActual.toString())
				lossCompIDR = CurrencyFormatter(lossCompIDRActual.toString())
			}
			
			taxPayerAccountRecordTypeE = sptManager.getIdentityData(scope, sptHd.Id)?.AccountRecordE ?: 0
			
			isReady = true
		}
		
		loadingPopupBox(!isReady)
		
		fun calculate1C() {
			val result = businessCirculationIDRActual - hppIDRActual
			profitLossIDR = if(result < 0L) "0" else result.toString()
		}
		fun calculate1E() {
			val result = profitLossIDRActual - businessCostIDRActual
			nettIncomeIDR = if(result < 0L) "0" else result.toString()
		}
		fun calculate2L() {
			val result = p2aIDRActual + p2bIDRActual + p2cIDRActual + p2dIDRActual + p2eIDRActual + p2fIDRActual + p2gIDRActual + p2hIDRActual + p2iIDRActual + p2jIDRActual + p2kIDRActual
			p2lIDR = if(result < 0L) "0" else result.toString()
		}
		fun calculate3D() {
			val result = p3aIDRActual + p3bIDRActual + p3cIDRActual
			p3dIDR = if(result < 0L) "0" else result.toString()
		}
		fun calculateFinal() {
			val result = nettIncomeIDRActual + p2lIDRActual + p3dIDRActual
			finalIDR = if(result < 0L) "0" else result.toString()
		}
		fun checkValidity(): Boolean {
			if(isAudited){
				return selectedAuditOpinion.isNotBlank() &&
						pubAccName.isNotBlank() &&
						pubAccNPWP.isNotBlank() &&
						pubAccOfficeName.isNotBlank() &&
						pubAccOfficeNPWP.isNotBlank() &&
						taxConsultantName.isNotBlank() &&
						taxConsultantNPWP.isNotBlank() &&
						taxConsultantOfficeName.isNotBlank() &&
						taxConsultantOfficeNPWP.isNotBlank() &&
						businessCirculationIDR.isNotBlank() &&
						hppIDR.isNotBlank() &&
						profitLossIDR.isNotBlank() &&
						businessCostIDR.isNotBlank() &&
						nettIncomeIDR.isNotBlank() &&
						p2aIDR.isNotBlank() &&
						p2bIDR.isNotBlank() &&
						p2cIDR.isNotBlank() &&
						p2dIDR.isNotBlank() &&
						p2eIDR.isNotBlank() &&
						p2fIDR.isNotBlank() &&
						p2gIDR.isNotBlank() &&
						p2hIDR.isNotBlank() &&
						p2iIDR.isNotBlank() &&
						p2jIDR.isNotBlank() &&
						p2kIDR.isNotBlank() &&
						p2lIDR.isNotBlank() &&
						p3aIDR.isNotBlank() &&
						p3bIDR.isNotBlank() &&
						p3cIDR.isNotBlank() &&
						p3dIDR.isNotBlank() &&
						finalIDR.isNotBlank() &&
						lossCompIDR.isNotBlank()
			} else {
				return selectedAuditOpinion.isNotBlank() &&
						taxConsultantName.isNotBlank() &&
						taxConsultantNPWP.isNotBlank() &&
						taxConsultantOfficeName.isNotBlank() &&
						taxConsultantOfficeNPWP.isNotBlank() &&
						businessCirculationIDR.isNotBlank() &&
						hppIDR.isNotBlank() &&
						profitLossIDR.isNotBlank() &&
						businessCostIDR.isNotBlank() &&
						nettIncomeIDR.isNotBlank() &&
						p2aIDR.isNotBlank() &&
						p2bIDR.isNotBlank() &&
						p2cIDR.isNotBlank() &&
						p2dIDR.isNotBlank() &&
						p2eIDR.isNotBlank() &&
						p2fIDR.isNotBlank() &&
						p2gIDR.isNotBlank() &&
						p2hIDR.isNotBlank() &&
						p2iIDR.isNotBlank() &&
						p2jIDR.isNotBlank() &&
						p2kIDR.isNotBlank() &&
						p2lIDR.isNotBlank() &&
						p3aIDR.isNotBlank() &&
						p3bIDR.isNotBlank() &&
						p3cIDR.isNotBlank() &&
						p3dIDR.isNotBlank() &&
						finalIDR.isNotBlank() &&
						lossCompIDR.isNotBlank()
			}
		}
		
		//Form
		Column(
			modifier = Modifier.fillMaxSize().background(Colors().panel)
		) {
			LazyColumn(
				modifier = Modifier.fillMaxSize()
			) {
				//Top Bar
				item {
					topBar("Penghasilan Non Final (Pembukuan)")
				}
				
				if(taxPayerAccountRecordTypeE == AccountRecordType.RECORD.value) {
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
									text = "Anda tidak perlu mengisi form ini dikarenakan [Jenis Pencatatan Penghasilan] anda pada Form Pengisian Identitas adalah [Pencatatan]. Form ini hanya diisi oleh Wajib Pajak yang menyelenggarakan [Pembukuan], untuk melaporkan besarnya penghasilan neto dalam negeri dari usaha dan/atau pekerjaan bebas yang diterima atau diperoleh Wajib Pajak sendiri dan anggota keluarganya dalam Tahun Pajak yang bersangkutan.",
									fontSize = 12.sp,
									lineHeight = 20.sp
								)
							}
						}
					}
				} else {
					//Is Audited
					item{
						Text(
							text = "Pembukuan / Laporan Keuangan",
							fontSize = 14.sp,
							fontWeight = FontWeight.SemiBold,
							color = Color(0xFF495057),
							modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 8.dp)
						)
						Row(
							modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(bottom = 8.dp),
							verticalAlignment = Alignment.CenterVertically
						) {
							Switch(
								checked = isAudited,
								onCheckedChange = {
									isAudited = it
								},
								colors = SwitchDefaults.colors(
									checkedThumbColor = Color.White,
									uncheckedThumbColor = Color.White,
									checkedTrackColor = Colors().brandDark40,
									uncheckedTrackColor = Colors().slate40,
									checkedTrackAlpha = 1f,
									uncheckedTrackAlpha = 1f
								),
								modifier = Modifier.padding(end = 8.dp)
							)
							Text(
								text = "Diaudit Oleh Akuntan Publik",
								fontSize = 14.sp,
								color = Color(0xFF495057),
								modifier = Modifier.clickable(true, onClick = {
									isAudited = !isAudited
								})
							)
						}
					}
					
					//Public Accountant Start
					//Audit Opinion
					item {
						Text(
							modifier = Modifier.padding(horizontal = 16.dp),
							text = "Opini Akuntan",
							fontSize = 12.sp,
							color = Color.Black,
							fontWeight = FontWeight.Bold
						)
						
						Box(
							modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
								.background(if(isAudited) Color.White else Colors().slate20, RoundedCornerShape(4.dp))
								.border(1.dp, Colors().textDarkGrey, RoundedCornerShape(4.dp))
								.clickable(isAudited, onClick = {
									showAuditOpinionPopup = true
								})
						) {
							Row (
								modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
								horizontalArrangement = Arrangement.SpaceBetween,
								verticalAlignment = Alignment.CenterVertically
							){
								Text(
									text = selectedAuditOpinion.ifBlank { "Pilih Opini Akuntan" },
									fontSize = 14.sp,
									color = if(selectedAuditOpinion == "" || !isAudited) Colors().textDarkGrey else Colors().textBlack
								)
								Image(painterResource(Res.drawable.Icon_Dropdown_Arrow), null, modifier = Modifier.size(24.dp))
							}
						}
					}
					//Public Accountant Name
					item {
						Text(
							modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
							text = "Nama Akuntan Publik",
							fontSize = 12.sp,
							color = Color.Black,
							fontWeight = FontWeight.Bold
						)
						TextField(
							enabled = isAudited,
							modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
								.padding(horizontal = 16.dp)
								.border(
									border = BorderStroke(1.dp, Colors().textDarkGrey),
									shape = RoundedCornerShape(4.dp)
								),
							value = pubAccName,
							onValueChange = {
								if(pubAccName.length <= 200){ pubAccName = it }
							},
							singleLine = true,
							colors = textFieldColors(
								backgroundColor = if(isAudited) Color.White else Colors().slate20,
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
					//Public Accountant NPWP
					item {
						Text(
							modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
							text = "NPWP Akuntan Publik",
							fontSize = 12.sp,
							color = Color.Black,
							fontWeight = FontWeight.Bold
						)
						TextField(
							enabled = isAudited,
							modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
								.padding(horizontal = 16.dp)
								.border(
									border = BorderStroke(1.dp, Colors().textDarkGrey),
									shape = RoundedCornerShape(4.dp)
								),
							value = pubAccNPWP,
							onValueChange = {
								if(pubAccNPWP.length <= 16) { pubAccNPWP = it }
							},
							singleLine = true,
							colors = textFieldColors(
								backgroundColor = if(isAudited) Color.White else Colors().slate20,
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
					//Public Accountant Office Name
					item {
						Text(
							modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
							text = "Nama Kantor Akuntan Publik",
							fontSize = 12.sp,
							color = Color.Black,
							fontWeight = FontWeight.Bold
						)
						TextField(
							enabled = isAudited,
							modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
								.padding(horizontal = 16.dp)
								.border(
									border = BorderStroke(1.dp, Colors().textDarkGrey),
									shape = RoundedCornerShape(4.dp)
								),
							value = pubAccOfficeName,
							onValueChange = {
								if(pubAccOfficeName.length <= 200){ pubAccOfficeName = it }
							},
							singleLine = true,
							colors = textFieldColors(
								backgroundColor = if(isAudited) Color.White else Colors().slate20,
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
					//Public Accountant NPWP
					item {
						Text(
							modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
							text = "NPWP Kantor Akuntan Publik",
							fontSize = 12.sp,
							color = Color.Black,
							fontWeight = FontWeight.Bold
						)
						TextField(
							enabled = isAudited,
							modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
								.padding(horizontal = 16.dp)
								.border(
									border = BorderStroke(1.dp, Colors().textDarkGrey),
									shape = RoundedCornerShape(4.dp)
								),
							value = pubAccOfficeNPWP,
							onValueChange = {
								if(pubAccOfficeNPWP.length <= 16) { pubAccOfficeNPWP = it }
							},
							singleLine = true,
							colors = textFieldColors(
								backgroundColor = if(isAudited) Color.White else Colors().slate20,
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
					//Public Accountant End
					
					//Tax Consultant Name
					item {
						Text(
							modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
							text = "Nama Konsultan Pajak",
							fontSize = 12.sp,
							color = Color.Black,
							fontWeight = FontWeight.Bold
						)
						TextField(
							enabled = true,
							modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
								.padding(horizontal = 16.dp)
								.border(
									border = BorderStroke(1.dp, Colors().textDarkGrey),
									shape = RoundedCornerShape(4.dp)
								),
							value = taxConsultantName,
							onValueChange = {
								if(taxConsultantName.length <= 200){ taxConsultantName = it }
							},
							singleLine = true,
							colors = textFieldColors(
								backgroundColor = Color.White,
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
					//Tax Consultant NPWP
					item {
						Text(
							modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
							text = "NPWP Konsultan Pajak",
							fontSize = 12.sp,
							color = Color.Black,
							fontWeight = FontWeight.Bold
						)
						TextField(
							enabled = true,
							modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
								.padding(horizontal = 16.dp)
								.border(
									border = BorderStroke(1.dp, Colors().textDarkGrey),
									shape = RoundedCornerShape(4.dp)
								),
							value = taxConsultantNPWP,
							onValueChange = {
								if(taxConsultantNPWP.length <= 16) { taxConsultantNPWP = it }
							},
							singleLine = true,
							colors = textFieldColors(
								backgroundColor = Color.White,
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
					//Tax Consultant Office Name
					item {
						Text(
							modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
							text = "Nama Kantor Konsultan Pajak",
							fontSize = 12.sp,
							color = Color.Black,
							fontWeight = FontWeight.Bold
						)
						TextField(
							enabled = true,
							modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
								.padding(horizontal = 16.dp)
								.border(
									border = BorderStroke(1.dp, Colors().textDarkGrey),
									shape = RoundedCornerShape(4.dp)
								),
							value = taxConsultantOfficeName,
							onValueChange = {
								if(taxConsultantOfficeName.length <= 200){ taxConsultantOfficeName = it }
							},
							singleLine = true,
							colors = textFieldColors(
								backgroundColor = Color.White,
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
					//Tax Consultant Office NPWP
					item {
						Text(
							modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
							text = "NPWP Kantor Konsultan Pajak",
							fontSize = 12.sp,
							color = Color.Black,
							fontWeight = FontWeight.Bold
						)
						TextField(
							enabled = true,
							modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
								.padding(horizontal = 16.dp)
								.border(
									border = BorderStroke(1.dp, Colors().textDarkGrey),
									shape = RoundedCornerShape(4.dp)
								),
							value = taxConsultantOfficeNPWP,
							onValueChange = {
								if(taxConsultantOfficeNPWP.length <= 16) { taxConsultantOfficeNPWP = it }
							},
							singleLine = true,
							colors = textFieldColors(
								backgroundColor = Color.White,
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
					
					//Part 1
					item {
						Box(
							modifier = Modifier
								.fillMaxWidth()
								.padding(horizontal = 16.dp, vertical = 8.dp)
								.clip(RoundedCornerShape(8.dp))
								.border(1.dp, Colors().slate20, RoundedCornerShape(8.dp))
								.background(Color.White)
						) {
							Column(
								modifier = Modifier.fillMaxWidth().padding(16.dp),
							) {
								Row(
									modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
									verticalAlignment = Alignment.CenterVertically,
//									horizontalArrangement = Arrangement.SpaceBetween
								) {
									Text(
										text = "1. Penghasilan dari Usaha dan/atau Pekerjaan Bebas Berdasarkan Laporan Keuangan Komersial",
										fontSize = 12.sp,
										fontWeight = FontWeight.Bold,
										lineHeight = 20.sp,
										color = Colors().textDarkGrey,
										modifier = Modifier.weight(0.9f)
									)
									Image(
										painterResource(Res.drawable.Icon_Dropdown_Arrow),
										null,
										modifier = Modifier.size(24.dp).weight(0.1f)
											.clickable(true, onClick = {
												expandP1 = !expandP1
											}).rotate(if(expandP1) 180f else 0f)
									)
								}
								if(expandP1) {
									divider(0.dp)
									
									//Part 1 Content
									//Part 1 A
									Text(
										modifier = Modifier.padding(top = 16.dp),
										text = "a. Peredaran Usaha",
										fontSize = 12.sp,
										color = Color.Black,
										fontWeight = FontWeight.Bold
									)
									TextField(
										enabled = true,
										modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
											.border(
												border = BorderStroke(1.dp, Colors().textDarkGrey),
												shape = RoundedCornerShape(4.dp)
											),
										value = businessCirculationIDR,
										onValueChange = {
											businessCirculationIDR = it
											businessCirculationIDRActual = businessCirculationIDR.replace("R", "").replace("p", "").replace(".", "").replace(" ", "").replace(",", "").toLongOrNull() ?: 0L
											calculate1C()
										},
										singleLine = true,
										colors = textFieldColors(
											backgroundColor = Color.White,
											focusedIndicatorColor = Color.Transparent,
											unfocusedIndicatorColor = Color.Transparent,
											disabledIndicatorColor = Colors().slate20
										),
										keyboardOptions = KeyboardOptions(
											keyboardType = KeyboardType.Number,
											imeAction = ImeAction.Next
										)
									)
									
									//Part 1 B
									Text(
										modifier = Modifier.padding(top = 8.dp),
										text = "b. Harga Pokok Penjualan",
										fontSize = 12.sp,
										color = Color.Black,
										fontWeight = FontWeight.Bold
									)
									TextField(
										enabled = true,
										modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
											.border(
												border = BorderStroke(1.dp, Colors().textDarkGrey),
												shape = RoundedCornerShape(4.dp)
											),
										value = hppIDR,
										onValueChange = {
											hppIDR = it
											hppIDRActual = hppIDR.replace("R", "").replace("p", "").replace(".", "").replace(" ", "").replace(",", "").toLongOrNull() ?: 0L
											calculate1C()
										},
										singleLine = true,
										colors = textFieldColors(
											backgroundColor = Color.White,
											focusedIndicatorColor = Color.Transparent,
											unfocusedIndicatorColor = Color.Transparent,
											disabledIndicatorColor = Colors().slate20
										),
										keyboardOptions = KeyboardOptions(
											keyboardType = KeyboardType.Number,
											imeAction = ImeAction.Next
										)
									)
									
									//Part 1 C
									Text(
										modifier = Modifier.padding(top = 8.dp),
										text = "c. Laba / Rugi Bruto Usaha (1a - 1b)",
										fontSize = 12.sp,
										color = Color.Black,
										fontWeight = FontWeight.Bold
									)
									TextField(
										enabled = false,
										modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
											.border(
												border = BorderStroke(1.dp, Colors().textDarkGrey),
												shape = RoundedCornerShape(4.dp)
											),
										value = "Rp ${CurrencyFormatter(profitLossIDR)}",
										onValueChange = {
											profitLossIDRActual = profitLossIDR.replace("R", "").replace("p", "").replace(".", "").replace(" ", "").replace(",", "").toLongOrNull() ?: 0L
										},
										singleLine = true,
										colors = textFieldColors(
											backgroundColor = Colors().slate20,
											disabledTextColor = Color.Black,
											focusedIndicatorColor = Color.Transparent,
											unfocusedIndicatorColor = Color.Transparent,
											disabledIndicatorColor = Colors().slate20
										),
										keyboardOptions = KeyboardOptions(
											keyboardType = KeyboardType.Number,
											imeAction = ImeAction.Next
										)
									)
									
									//Part 1 D
									Text(
										modifier = Modifier.padding(top = 8.dp),
										text = "d. Biaya Usaha",
										fontSize = 12.sp,
										color = Color.Black,
										fontWeight = FontWeight.Bold
									)
									TextField(
										enabled = true,
										modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
											.border(
												border = BorderStroke(1.dp, Colors().textDarkGrey),
												shape = RoundedCornerShape(4.dp)
											),
										value = businessCostIDR,
										onValueChange = {
											businessCostIDR = it
											businessCostIDRActual = businessCostIDR.replace("R", "").replace("p", "").replace(".", "").replace(" ", "").replace(",", "").toLongOrNull() ?: 0L
											calculate1E()
										},
										singleLine = true,
										colors = textFieldColors(
											backgroundColor = Color.White,
											focusedIndicatorColor = Color.Transparent,
											unfocusedIndicatorColor = Color.Transparent,
											disabledIndicatorColor = Colors().slate20
										),
										keyboardOptions = KeyboardOptions(
											keyboardType = KeyboardType.Number,
											imeAction = ImeAction.Next
										)
									)
									
									//Part 1 E
									Text(
										modifier = Modifier.padding(top = 8.dp),
										text = "e. Penghasilan Neto (1c - 1d)",
										fontSize = 12.sp,
										color = Color.Black,
										fontWeight = FontWeight.Bold
									)
									TextField(
										enabled = false,
										modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
											.border(
												border = BorderStroke(1.dp, Colors().textDarkGrey),
												shape = RoundedCornerShape(4.dp)
											),
										value = "Rp ${CurrencyFormatter(nettIncomeIDR)}",
										onValueChange = {
											nettIncomeIDRActual = nettIncomeIDR.replace("R", "").replace("p", "").replace(".", "").replace(" ", "").replace(",", "").toLongOrNull() ?: 0L
											calculateFinal()
										},
										singleLine = true,
										colors = textFieldColors(
											backgroundColor = Colors().slate20,
											disabledTextColor = Color.Black,
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
							}
						}
					}
					
					//Part 2
					item {
						Box(
							modifier = Modifier
								.fillMaxWidth()
								.padding(horizontal = 16.dp, vertical = 8.dp)
								.clip(RoundedCornerShape(8.dp))
								.border(1.dp, Colors().slate20, RoundedCornerShape(8.dp))
								.background(Color.White)
						) {
							Column(
								modifier = Modifier.fillMaxWidth().padding(16.dp),
							) {
								Row(
									modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
									verticalAlignment = Alignment.CenterVertically,
//									horizontalArrangement = Arrangement.SpaceBetween
								) {
									Text(
										text = "2. Penyesuaian Fiskal Positif",
										fontSize = 12.sp,
										fontWeight = FontWeight.Bold,
										lineHeight = 20.sp,
										color = Colors().textDarkGrey,
										modifier = Modifier.weight(0.9f)
									)
									Image(
										painterResource(Res.drawable.Icon_Dropdown_Arrow),
										null,
										modifier = Modifier.size(24.dp).weight(0.1f)
											.clickable(true, onClick = {
												expandP2 = !expandP2
											}).rotate(if(expandP2) 180f else 0f)
									)
								}
								if(expandP2) {
									divider(0.dp)
									
									//Part 2 Content
									//Part 2 A
									Text(
										modifier = Modifier.padding(top = 16.dp),
										text = "a. Biaya yang Dibebankan/Dikeluarkan untuk Kepentingan Pribadi Wajib Pajak atau Orang yang Menjadi Tanggungannya",
										fontSize = 12.sp,
										color = Color.Black,
										fontWeight = FontWeight.Bold
									)
									TextField(
										enabled = true,
										modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
											.border(
												border = BorderStroke(1.dp, Colors().textDarkGrey),
												shape = RoundedCornerShape(4.dp)
											),
										value = p2aIDR,
										onValueChange = {
											p2aIDR = it
											p2aIDRActual = p2aIDR.replace("R", "").replace("p", "").replace(".", "").replace(" ", "").replace(",", "").toLongOrNull() ?: 0L
											calculate2L()
										},
										singleLine = true,
										colors = textFieldColors(
											backgroundColor = Color.White,
											focusedIndicatorColor = Color.Transparent,
											unfocusedIndicatorColor = Color.Transparent,
											disabledIndicatorColor = Colors().slate20
										),
										keyboardOptions = KeyboardOptions(
											keyboardType = KeyboardType.Number,
											imeAction = ImeAction.Next
										)
									)
									
									//Part 2 B
									Text(
										modifier = Modifier.padding(top = 16.dp),
										text = "b. Premi Asuransi Kesehatan, Asuransi Kecelakaan, Asuransi Jiwa, Asuransi Dwiguna, dan Asuransi Beasiswa yang Dibayar oleh Wajib Pajak",
										fontSize = 12.sp,
										color = Color.Black,
										fontWeight = FontWeight.Bold
									)
									TextField(
										enabled = true,
										modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
											.border(
												border = BorderStroke(1.dp, Colors().textDarkGrey),
												shape = RoundedCornerShape(4.dp)
											),
										value = p2bIDR,
										onValueChange = {
											p2bIDR = it
											p2bIDRActual = p2bIDR.replace("R", "").replace("p", "").replace(".", "").replace(" ", "").replace(",", "").toLongOrNull() ?: 0L
											calculate2L()
										},
										singleLine = true,
										colors = textFieldColors(
											backgroundColor = Color.White,
											focusedIndicatorColor = Color.Transparent,
											unfocusedIndicatorColor = Color.Transparent,
											disabledIndicatorColor = Colors().slate20
										),
										keyboardOptions = KeyboardOptions(
											keyboardType = KeyboardType.Number,
											imeAction = ImeAction.Next
										)
									)
									
									//Part 2 C
									Text(
										modifier = Modifier.padding(top = 16.dp),
										text = "c. Penggantian atau Imbalan Sehubungan dengan Pekerjaan atau Jasa yang Diberikan Dalam Bentuk Natura atau Kenikmatan",
										fontSize = 12.sp,
										color = Color.Black,
										fontWeight = FontWeight.Bold
									)
									TextField(
										enabled = true,
										modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
											.border(
												border = BorderStroke(1.dp, Colors().textDarkGrey),
												shape = RoundedCornerShape(4.dp)
											),
										value = p2cIDR,
										onValueChange = {
											p2cIDR = it
											p2cIDRActual = p2cIDR.replace("R", "").replace("p", "").replace(".", "").replace(" ", "").replace(",", "").toLongOrNull() ?: 0L
											calculate2L()
										},
										singleLine = true,
										colors = textFieldColors(
											backgroundColor = Color.White,
											focusedIndicatorColor = Color.Transparent,
											unfocusedIndicatorColor = Color.Transparent,
											disabledIndicatorColor = Colors().slate20
										),
										keyboardOptions = KeyboardOptions(
											keyboardType = KeyboardType.Number,
											imeAction = ImeAction.Next
										)
									)
									
									//Part 2 D
									Text(
										modifier = Modifier.padding(top = 16.dp),
										text = "d. Jumlah yang Melebihi Kewajaran yang Dibayarkan Kepada Pihak yang Mempunyai Hubungan Istimewa Sehubungan dengan Pekerjaan yang Dilakukan",
										fontSize = 12.sp,
										color = Color.Black,
										fontWeight = FontWeight.Bold
									)
									TextField(
										enabled = true,
										modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
											.border(
												border = BorderStroke(1.dp, Colors().textDarkGrey),
												shape = RoundedCornerShape(4.dp)
											),
										value = p2dIDR,
										onValueChange = {
											p2dIDR = it
											p2dIDRActual = p2dIDR.replace("R", "").replace("p", "").replace(".", "").replace(" ", "").replace(",", "").toLongOrNull() ?: 0L
											calculate2L()
										},
										singleLine = true,
										colors = textFieldColors(
											backgroundColor = Color.White,
											focusedIndicatorColor = Color.Transparent,
											unfocusedIndicatorColor = Color.Transparent,
											disabledIndicatorColor = Colors().slate20
										),
										keyboardOptions = KeyboardOptions(
											keyboardType = KeyboardType.Number,
											imeAction = ImeAction.Next
										)
									)
									
									//Part 2 E
									Text(
										modifier = Modifier.padding(top = 16.dp),
										text = "e. Harta yang Dihibahkan, Bantuan atau Sumbangan",
										fontSize = 12.sp,
										color = Color.Black,
										fontWeight = FontWeight.Bold
									)
									TextField(
										enabled = true,
										modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
											.border(
												border = BorderStroke(1.dp, Colors().textDarkGrey),
												shape = RoundedCornerShape(4.dp)
											),
										value = p2eIDR,
										onValueChange = {
											p2eIDR = it
											p2eIDRActual = p2eIDR.replace("R", "").replace("p", "").replace(".", "").replace(" ", "").replace(",", "").toLongOrNull() ?: 0L
											calculate2L()
										},
										singleLine = true,
										colors = textFieldColors(
											backgroundColor = Color.White,
											focusedIndicatorColor = Color.Transparent,
											unfocusedIndicatorColor = Color.Transparent,
											disabledIndicatorColor = Colors().slate20
										),
										keyboardOptions = KeyboardOptions(
											keyboardType = KeyboardType.Number,
											imeAction = ImeAction.Next
										)
									)
									
									//Part 2 F
									Text(
										modifier = Modifier.padding(top = 16.dp),
										text = "f. Pajak Penghasilan",
										fontSize = 12.sp,
										color = Color.Black,
										fontWeight = FontWeight.Bold
									)
									TextField(
										enabled = true,
										modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
											.border(
												border = BorderStroke(1.dp, Colors().textDarkGrey),
												shape = RoundedCornerShape(4.dp)
											),
										value = p2fIDR,
										onValueChange = {
											p2fIDR = it
											p2fIDRActual = p2fIDR.replace("R", "").replace("p", "").replace(".", "").replace(" ", "").replace(",", "").toLongOrNull() ?: 0L
											calculate2L()
										},
										singleLine = true,
										colors = textFieldColors(
											backgroundColor = Color.White,
											focusedIndicatorColor = Color.Transparent,
											unfocusedIndicatorColor = Color.Transparent,
											disabledIndicatorColor = Colors().slate20
										),
										keyboardOptions = KeyboardOptions(
											keyboardType = KeyboardType.Number,
											imeAction = ImeAction.Next
										)
									)
									
									//Part 2 G
									Text(
										modifier = Modifier.padding(top = 16.dp),
										text = "g. Gaji yang Dibayarkan kepada Pemilik / Orang yang Menjadi Tanggungannya",
										fontSize = 12.sp,
										color = Color.Black,
										fontWeight = FontWeight.Bold
									)
									TextField(
										enabled = true,
										modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
											.border(
												border = BorderStroke(1.dp, Colors().textDarkGrey),
												shape = RoundedCornerShape(4.dp)
											),
										value = p2gIDR,
										onValueChange = {
											p2gIDR = it
											p2gIDRActual = p2gIDR.replace("R", "").replace("p", "").replace(".", "").replace(" ", "").replace(",", "").toLongOrNull() ?: 0L
											calculate2L()
										},
										singleLine = true,
										colors = textFieldColors(
											backgroundColor = Color.White,
											focusedIndicatorColor = Color.Transparent,
											unfocusedIndicatorColor = Color.Transparent,
											disabledIndicatorColor = Colors().slate20
										),
										keyboardOptions = KeyboardOptions(
											keyboardType = KeyboardType.Number,
											imeAction = ImeAction.Next
										)
									)
									
									//Part 2 H
									Text(
										modifier = Modifier.padding(top = 16.dp),
										text = "h. Sanksi Administrasi",
										fontSize = 12.sp,
										color = Color.Black,
										fontWeight = FontWeight.Bold
									)
									TextField(
										enabled = true,
										modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
											.border(
												border = BorderStroke(1.dp, Colors().textDarkGrey),
												shape = RoundedCornerShape(4.dp)
											),
										value = p2hIDR,
										onValueChange = {
											p2hIDR = it
											p2hIDRActual = p2hIDR.replace("R", "").replace("p", "").replace(".", "").replace(" ", "").replace(",", "").toLongOrNull() ?: 0L
											calculate2L()
										},
										singleLine = true,
										colors = textFieldColors(
											backgroundColor = Color.White,
											focusedIndicatorColor = Color.Transparent,
											unfocusedIndicatorColor = Color.Transparent,
											disabledIndicatorColor = Colors().slate20
										),
										keyboardOptions = KeyboardOptions(
											keyboardType = KeyboardType.Number,
											imeAction = ImeAction.Next
										)
									)
									
									//Part 2 I
									Text(
										modifier = Modifier.padding(top = 16.dp),
										text = "i. Selisih Penyusutan / Amortisasi Komersial Diatas Penyusutan / Amortisasi Fiskal",
										fontSize = 12.sp,
										color = Color.Black,
										fontWeight = FontWeight.Bold
									)
									TextField(
										enabled = true,
										modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
											.border(
												border = BorderStroke(1.dp, Colors().textDarkGrey),
												shape = RoundedCornerShape(4.dp)
											),
										value = p2iIDR,
										onValueChange = {
											p2iIDR = it
											p2iIDRActual = p2iIDR.replace("R", "").replace("p", "").replace(".", "").replace(" ", "").replace(",", "").toLongOrNull() ?: 0L
											calculate2L()
										},
										singleLine = true,
										colors = textFieldColors(
											backgroundColor = Color.White,
											focusedIndicatorColor = Color.Transparent,
											unfocusedIndicatorColor = Color.Transparent,
											disabledIndicatorColor = Colors().slate20
										),
										keyboardOptions = KeyboardOptions(
											keyboardType = KeyboardType.Number,
											imeAction = ImeAction.Next
										)
									)
									
									//Part 2 J
									Text(
										modifier = Modifier.padding(top = 16.dp),
										text = "j. Biaya untuk Mendapatkan, Menagih dan Memelihara Penghasilan yang Dikenakan PPh Final dan Penghasilan yang tidak Termasuk Objek Pajak",
										fontSize = 12.sp,
										color = Color.Black,
										fontWeight = FontWeight.Bold
									)
									TextField(
										enabled = true,
										modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
											.border(
												border = BorderStroke(1.dp, Colors().textDarkGrey),
												shape = RoundedCornerShape(4.dp)
											),
										value = p2jIDR,
										onValueChange = {
											p2jIDR = it
											p2jIDRActual = p2jIDR.replace("R", "").replace("p", "").replace(".", "").replace(" ", "").replace(",", "").toLongOrNull() ?: 0L
											calculate2L()
										},
										singleLine = true,
										colors = textFieldColors(
											backgroundColor = Color.White,
											focusedIndicatorColor = Color.Transparent,
											unfocusedIndicatorColor = Color.Transparent,
											disabledIndicatorColor = Colors().slate20
										),
										keyboardOptions = KeyboardOptions(
											keyboardType = KeyboardType.Number,
											imeAction = ImeAction.Next
										)
									)
									
									//Part 2 K
									Text(
										modifier = Modifier.padding(top = 16.dp),
										text = "k. Penyesuaian Fiskal Positif Lainnya",
										fontSize = 12.sp,
										color = Color.Black,
										fontWeight = FontWeight.Bold
									)
									TextField(
										enabled = true,
										modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
											.border(
												border = BorderStroke(1.dp, Colors().textDarkGrey),
												shape = RoundedCornerShape(4.dp)
											),
										value = p2kIDR,
										onValueChange = {
											p2kIDR = it
											p2kIDRActual = p2kIDR.replace("R", "").replace("p", "").replace(".", "").replace(" ", "").replace(",", "").toLongOrNull() ?: 0L
											calculate2L()
										},
										singleLine = true,
										colors = textFieldColors(
											backgroundColor = Color.White,
											focusedIndicatorColor = Color.Transparent,
											unfocusedIndicatorColor = Color.Transparent,
											disabledIndicatorColor = Colors().slate20
										),
										keyboardOptions = KeyboardOptions(
											keyboardType = KeyboardType.Number,
											imeAction = ImeAction.Next
										)
									)
									
									//Part 2 L
									Text(
										modifier = Modifier.padding(top = 8.dp),
										text = "l. Jumlah (2a s.d. 2k)",
										fontSize = 12.sp,
										color = Color.Black,
										fontWeight = FontWeight.Bold
									)
									TextField(
										enabled = false,
										modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
											.border(
												border = BorderStroke(1.dp, Colors().textDarkGrey),
												shape = RoundedCornerShape(4.dp)
											),
										value = "Rp ${CurrencyFormatter(p2lIDR)}",
										onValueChange = {
											p2lIDRActual = p2lIDR.replace("R", "").replace("p", "").replace(".", "").replace(" ", "").replace(",", "").toLongOrNull() ?: 0L
											calculateFinal()
										},
										singleLine = true,
										colors = textFieldColors(
											backgroundColor = Colors().slate20,
											disabledTextColor = Color.Black,
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
							}
						}
					}
					
					//Part 3
					item {
						Box(
							modifier = Modifier
								.fillMaxWidth()
								.padding(horizontal = 16.dp, vertical = 8.dp)
								.clip(RoundedCornerShape(8.dp))
								.border(1.dp, Colors().slate20, RoundedCornerShape(8.dp))
								.background(Color.White)
						) {
							Column(
								modifier = Modifier.fillMaxWidth().padding(16.dp),
							) {
								Row(
									modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
									verticalAlignment = Alignment.CenterVertically,
//									horizontalArrangement = Arrangement.SpaceBetween
								) {
									Text(
										text = "3. Penyesuaian Fiskal Negatif",
										fontSize = 12.sp,
										fontWeight = FontWeight.Bold,
										lineHeight = 20.sp,
										color = Colors().textDarkGrey,
										modifier = Modifier.weight(0.9f)
									)
									Image(
										painterResource(Res.drawable.Icon_Dropdown_Arrow),
										null,
										modifier = Modifier.size(24.dp).weight(0.1f)
											.clickable(true, onClick = {
												expandP3 = !expandP3
											}).rotate(if(expandP3) 180f else 0f)
									)
								}
								if(expandP3) {
									divider(0.dp)
									
									//Part 3 Content
									//Part 3 A
									Text(
										modifier = Modifier.padding(top = 16.dp),
										text = "a. Penghasilan yang Dikenakan PPh Final dan Penghasilan yang tidak Termasuk Objek Pajak tetapi Termasuk Dalam Peredaran Usaha",
										fontSize = 12.sp,
										color = Color.Black,
										fontWeight = FontWeight.Bold
									)
									TextField(
										enabled = true,
										modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
											.border(
												border = BorderStroke(1.dp, Colors().textDarkGrey),
												shape = RoundedCornerShape(4.dp)
											),
										value = p3aIDR,
										onValueChange = {
											p3aIDR = it
											p3aIDRActual = p3aIDR.replace("R", "").replace("p", "").replace(".", "").replace(" ", "").replace(",", "").toLongOrNull() ?: 0L
											calculate3D()
										},
										singleLine = true,
										colors = textFieldColors(
											backgroundColor = Color.White,
											focusedIndicatorColor = Color.Transparent,
											unfocusedIndicatorColor = Color.Transparent,
											disabledIndicatorColor = Colors().slate20
										),
										keyboardOptions = KeyboardOptions(
											keyboardType = KeyboardType.Number,
											imeAction = ImeAction.Next
										)
									)
									
									//Part 3 B
									Text(
										modifier = Modifier.padding(top = 16.dp),
										text = "b. Selisih Penyusutan / Amortisasi Komersial di Bawah Penyusutan Amortisasi Fiskal",
										fontSize = 12.sp,
										color = Color.Black,
										fontWeight = FontWeight.Bold
									)
									TextField(
										enabled = true,
										modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
											.border(
												border = BorderStroke(1.dp, Colors().textDarkGrey),
												shape = RoundedCornerShape(4.dp)
											),
										value = p3bIDR,
										onValueChange = {
											p3bIDR = it
											p3bIDRActual = p3bIDR.replace("R", "").replace("p", "").replace(".", "").replace(" ", "").replace(",", "").toLongOrNull() ?: 0L
											calculate3D()
										},
										singleLine = true,
										colors = textFieldColors(
											backgroundColor = Color.White,
											focusedIndicatorColor = Color.Transparent,
											unfocusedIndicatorColor = Color.Transparent,
											disabledIndicatorColor = Colors().slate20
										),
										keyboardOptions = KeyboardOptions(
											keyboardType = KeyboardType.Number,
											imeAction = ImeAction.Next
										)
									)
									
									//Part 3 C
									Text(
										modifier = Modifier.padding(top = 16.dp),
										text = "c. Penyesuaian Fiskal Negatif Lainnya",
										fontSize = 12.sp,
										color = Color.Black,
										fontWeight = FontWeight.Bold
									)
									TextField(
										enabled = true,
										modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
											.border(
												border = BorderStroke(1.dp, Colors().textDarkGrey),
												shape = RoundedCornerShape(4.dp)
											),
										value = p3cIDR,
										onValueChange = {
											p3cIDR = it
											p3cIDRActual = p3cIDR.replace("R", "").replace("p", "").replace(".", "").replace(" ", "").replace(",", "").toLongOrNull() ?: 0L
											calculate3D()
										},
										singleLine = true,
										colors = textFieldColors(
											backgroundColor = Color.White,
											focusedIndicatorColor = Color.Transparent,
											unfocusedIndicatorColor = Color.Transparent,
											disabledIndicatorColor = Colors().slate20
										),
										keyboardOptions = KeyboardOptions(
											keyboardType = KeyboardType.Number,
											imeAction = ImeAction.Next
										)
									)
									
									//Part 3 D
									Text(
										modifier = Modifier.padding(top = 8.dp),
										text = "d. Jumlah (3a s.d. 3c)",
										fontSize = 12.sp,
										color = Color.Black,
										fontWeight = FontWeight.Bold
									)
									TextField(
										enabled = false,
										modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
											.border(
												border = BorderStroke(1.dp, Colors().textDarkGrey),
												shape = RoundedCornerShape(4.dp)
											),
										value = "Rp ${CurrencyFormatter(p3dIDR)}",
										onValueChange = {
											p3dIDRActual = p3dIDR.replace("R", "").replace("p", "").replace(".", "").replace(" ", "").replace(",", "").toLongOrNull() ?: 0L
											calculateFinal()
										},
										singleLine = true,
										colors = textFieldColors(
											backgroundColor = Colors().slate20,
											disabledTextColor = Color.Black,
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
							}
						}
					}
					
					//Final
					item {
						Text(
							modifier = Modifier.padding(top = 8.dp).padding(horizontal = 16.dp),
							text = "Jumlah Akhir (1e + 2l - 3d)",
							fontSize = 12.sp,
							color = Color.Black,
							fontWeight = FontWeight.Bold
						)
						TextField(
							enabled = false,
							modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
								.border(
									border = BorderStroke(1.dp, Colors().textDarkGrey),
									shape = RoundedCornerShape(4.dp)
								),
							value = "Rp ${CurrencyFormatter(finalIDR)}",
							onValueChange = {
								finalIDRActual = finalIDR.replace("R", "").replace("p", "").replace(".", "").replace(" ", "").replace(",", "").toLongOrNull() ?: 0L
							},
							singleLine = true,
							colors = textFieldColors(
								backgroundColor = Colors().slate20,
								disabledTextColor = Color.Black,
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
					
					//Loss Compensation
					item {
						Text(
							modifier = Modifier.padding(top = 8.dp).padding(horizontal = 16.dp),
							text = "Kompensasi Kerugian",
							fontSize = 12.sp,
							color = Color.Black,
							fontWeight = FontWeight.Bold
						)
						TextField(
							enabled = true,
							modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
								.border(
									border = BorderStroke(1.dp, Colors().textDarkGrey),
									shape = RoundedCornerShape(4.dp)
								),
							value = lossCompIDR,
							onValueChange = {
								lossCompIDR = it
								lossCompIDRActual =
									lossCompIDR.replace("R", "").replace("p", "").replace(".", "").replace(" ", "").replace(",", "").toLongOrNull() ?: 0L
							},
							singleLine = true,
							colors = textFieldColors(
								backgroundColor = Color.White,
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
							enabled = checkValidity(),
							modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 16.dp),
							colors = buttonColors(backgroundColor = Colors().buttonActive, contentColor = Color.White),
							onClick = {
								if(isAudited) {
										val dataModel = FormBookKeepingRequestApiModel(
											Tr1770HdId = sptHd!!.Id,
											IsAudited = isAudited,
											AuditOpinionE = auditOpinionE,
											PubAccName = pubAccName,
											PubAccNPWP = pubAccNPWP,
											PubAccOfficeName = pubAccOfficeName,
											PubAccOfficeNPWP = pubAccOfficeNPWP,
											TaxConsultantName = taxConsultantName,
											TaxConsultantNPWP = taxConsultantNPWP,
											TaxConsultantOfficeName = taxConsultantOfficeName,
											TaxConsultantOfficeNPWP = taxConsultantOfficeNPWP,
											BusinessCirculationIDR = businessCirculationIDRActual,
											HPPIDR = hppIDRActual,
											BusinessCostIDR = businessCostIDRActual,
											P2A_IDR = p2aIDRActual,
											P2B_IDR = p2bIDRActual,
											P2C_IDR = p2cIDRActual,
											P2D_IDR = p2dIDRActual,
											P2E_IDR = p2eIDRActual,
											P2F_IDR = p2fIDRActual,
											P2G_IDR = p2gIDRActual,
											P2H_IDR = p2hIDRActual,
											P2I_IDR = p2iIDRActual,
											P2J_IDR = p2jIDRActual,
											P2K_IDR = p2kIDRActual,
											P3A_IDR = p3aIDRActual,
											P3B_IDR = p3bIDRActual,
											P3C_IDR = p3cIDRActual,
											LossCompensationIDR = lossCompIDRActual
									)
									
									scope.launch {
										isReady = false
										sptManager.saveIncomeBookKeep(scope, dataModel)
										navigator.pop()
									}
								} else {
									val dataModel = FormBookKeepingRequestApiModel(
										Tr1770HdId = sptHd!!.Id,
										IsAudited = isAudited,
										AuditOpinionE = null,
										PubAccName = null,
										PubAccNPWP = null,
										PubAccOfficeName = null,
										PubAccOfficeNPWP = null,
										TaxConsultantName = taxConsultantName,
										TaxConsultantNPWP = taxConsultantNPWP,
										TaxConsultantOfficeName = taxConsultantOfficeName,
										TaxConsultantOfficeNPWP = taxConsultantOfficeNPWP,
										BusinessCirculationIDR = businessCirculationIDRActual,
										HPPIDR = hppIDRActual,
										BusinessCostIDR = businessCostIDRActual,
										P2A_IDR = p2aIDRActual,
										P2B_IDR = p2bIDRActual,
										P2C_IDR = p2cIDRActual,
										P2D_IDR = p2dIDRActual,
										P2E_IDR = p2eIDRActual,
										P2F_IDR = p2fIDRActual,
										P2G_IDR = p2gIDRActual,
										P2H_IDR = p2hIDRActual,
										P2I_IDR = p2iIDRActual,
										P2J_IDR = p2jIDRActual,
										P2K_IDR = p2kIDRActual,
										P3A_IDR = p3aIDRActual,
										P3B_IDR = p3bIDRActual,
										P3C_IDR = p3cIDRActual,
										LossCompensationIDR = lossCompIDRActual
									)
									
									scope.launch {
										isReady = false
										sptManager.saveIncomeBookKeep(scope, dataModel)
										navigator.pop()
									}
								}
							}
						) {
							Text(
								modifier = Modifier.padding(vertical = 6.dp),
								text = "Simpan",
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
		}
		
		//Popups
		//Audit Opinion Popup
		popUpBox(
			showPopup = showAuditOpinionPopup,
			onClickOutside = { showAuditOpinionPopup = false },
			content = {
				Column(
					modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 12.dp)
				) {
					Text(
						text = "Opini Akuntan",
						fontSize = 12.sp,
						color = Colors().textBlack,
						fontWeight = FontWeight.Bold,
						modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
					)
					LazyColumn(modifier = Modifier.heightIn(max = 200.dp).padding(horizontal = 18.dp)) {
						AuditOpinion.entries.forEach {
							item {
								Text(
									text = AuditOpinion.fromValue(it.value).toString(),
									fontSize = 18.sp,
									modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
										.clickable(true, onClick = {
											selectedAuditOpinion = AuditOpinion.fromValue(it.value).toString()
											auditOpinionE = it.value
											showAuditOpinionPopup = false
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