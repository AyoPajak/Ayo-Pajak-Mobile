package screens.SPT

import SPT.SPTManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import ayopajakmobile.composeapp.generated.resources.icon_infocircle
import ayopajakmobile.composeapp.generated.resources.placeholder
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import global.Colors
import global.universalUIComponents.loadingPopupBox
import global.universalUIComponents.topBar
import http.Account
import http.Interfaces
import kotlinx.coroutines.launch
import models.transaction.Form1770HdResponseApiModel
import models.transaction.FormAdditionalRequestApiModel
import org.jetbrains.compose.resources.painterResource

class SptStepFourteenScreen(val sptHd: Form1770HdResponseApiModel?, val client: Account, val sptPertamaClient: Interfaces, val prefs: DataStore<Preferences>): Screen {
	
	val sptManager = SPTManager(prefs, client, sptPertamaClient)
	
	@Composable
	override fun Content() {
		
		val scope = rememberCoroutineScope()
		val navigator = LocalNavigator.currentOrThrow
		
		var agree by remember { mutableStateOf(false) }
		
		var isReady = true
		
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
					topBar("Konfirmasi")
				}
				
				//Image
				item {
					Image(
						painterResource(Res.drawable.placeholder),
						null,
						modifier = Modifier.fillMaxWidth().height(256.dp)
					)
				}
				
				//Subtitle
				item {
					Text(
						text = "Data Yang Anda Masukkan Sudah Sesuai?",
						fontSize = 14.sp,
						fontWeight = FontWeight.Bold,
						modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 24.dp),
						textAlign = TextAlign.Center
					)
				}
				
				//Clause 1
				item {
					Row(
						modifier = Modifier.padding(bottom = 8.dp).padding(horizontal = 16.dp),
						verticalAlignment = Alignment.Top
					) {
						Text(
							text = "•",
							fontSize = 14.sp,
							fontWeight = FontWeight.Bold,
							modifier = Modifier.padding(top = 2.dp)
						)
						Text(
							text = "Pastikan data yang anda masukkan sudah sesuai.",
							fontSize = 14.sp,
							modifier = Modifier.padding(start = 8.dp),
							lineHeight = 20.sp
						)
					}
				}
				
				//Clause 2
				item {
					Row(
						modifier = Modifier.padding(bottom = 8.dp).padding(horizontal = 16.dp),
						verticalAlignment = Alignment.Top
					) {
						Text(
							text = "•",
							fontSize = 14.sp,
							fontWeight = FontWeight.Bold,
							modifier = Modifier.padding(top = 2.dp)
						)
						Text(
							text = "Perubahan data masih dapat dilakukan sebelum SPT dilaporkan melalui tombol [Edit].",
							fontSize = 14.sp,
							modifier = Modifier.padding(start = 8.dp),
							lineHeight = 20.sp
						)
					}
				}
				
				//Clause 3
				item {
					Row(
						modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 24.dp),
						verticalAlignment = Alignment.Top
					) {
						Text(
							text = "•",
							fontSize = 14.sp,
							fontWeight = FontWeight.Bold,
							modifier = Modifier.padding(top = 2.dp)
						)
						Text(
							text = "Jika SPT telah dilaporkan, perubahan data harus dilakukan dengan membuat SPT pembetulan selanjutnya.",
							fontSize = 14.sp,
							modifier = Modifier.padding(start = 8.dp),
							lineHeight = 20.sp
						)
					}
				}
				
				//Agreement
				item {
					Box(
						modifier = Modifier
							.fillMaxWidth()
							.padding(bottom = 24.dp).padding(horizontal = 16.dp)
							.clip(RoundedCornerShape(8.dp))
							.background(Color(0xFFEEF4FF))
					) {
						Row(
							modifier = Modifier.fillMaxWidth(),
							verticalAlignment = Alignment.Top
						) {
							Checkbox(
								enabled = true,
								checked = agree,
								onCheckedChange = { agree = !agree },
								colors = CheckboxDefaults.colors(
									checkedColor = Colors().buttonActive,
									uncheckedColor = Colors().textLightGrey
								)
							)
							Text(
								text = "Dengan menyadari sepenuhnya akan segala akibatnya termasuk sanksi-sanksi sesuai dengan ketentuan peraturan perundang-undangan yang berlaku, saya menyatakan bahwa apa yang telah saya beritahukan pada form sebelumnya adalah benar, lengkap, jelas.",
								fontSize = 14.sp,
								lineHeight = 20.sp,
								color = Colors().brandDark90,
								modifier = Modifier.padding(end = 16.dp, bottom = 16.dp, top = 14.dp)
									.clickable(true, onClick = {
										agree != agree
									})
							)
						}
					}
				}
				
				//Submit Button
				item {
					Button(
						enabled = agree,
						modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
						colors = buttonColors(backgroundColor = Colors().buttonActive, contentColor = Color.White),
						onClick = {
							scope.launch {
								isReady = false
								sptManager.submitForm1770(scope, sptHd!!.Id.toString())
								navigator.pop()
							}
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