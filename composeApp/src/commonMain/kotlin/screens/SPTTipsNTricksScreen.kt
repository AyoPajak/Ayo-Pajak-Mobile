package screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ayopajakmobile.composeapp.generated.resources.Res
import ayopajakmobile.composeapp.generated.resources.placeholder
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import global.Colors
import global.universalUIComponents.topBar
import org.jetbrains.compose.resources.painterResource

class SPTTipsNTricksScreen(private val isFromTripleDot: Boolean): Screen {
	
	@Composable
	override fun Content() {
		
		val navigator = LocalNavigator.currentOrThrow
		
		Column(
			modifier = Modifier.fillMaxSize()
		) {
			//Header
			topBar("Tips & Trik")
			
			//Body
			LazyColumn(
				modifier = Modifier.fillMaxWidth().background(Colors().panel)
			) {
				item() {
					Image(
						modifier = Modifier.fillMaxWidth().padding(16.dp),
						painter = painterResource(Res.drawable.placeholder),
						contentDescription = null
					)
				}
				
				item() {
					//TODO("Style")
					Text(
						modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
						text = "Persiapkan waktu secukupnya\n" +
								"Persiapkan data-data identitas, penghasilan, harta & uang\n" +
								"Lakukan pengisian SPT dimulai dari form lampiran terlebih dulu\n" +
								"Cross check daftar harta & utang di SPH (khusus peserta tax amnesti)\n" +
								"Jangan lupa membubuhkan tanda tangan (jika melaporkan SPT secara manual / bukan E-Filling), karena jika tidak SPT yang anda laporkan dianggap tidak sah\n" +
								"Estimasikan biaya hidup\n" +
								"Mulai peduli dengan inventaris dokumen-dokumen (bukti potong, sertifikat, dll)\n" +
								"Mulai peduli dengan legalitas identitas (status wp, jenis usaha, dokumen persyaratan)\n" +
								"Mulai peduli dengan pencatatan / pembukuan",
						fontSize = 14.sp,
					)
				}
			}
		}
		
		//Footer
		Box(
			modifier = Modifier.fillMaxSize(),
			contentAlignment = Alignment.BottomCenter
		) {
			if(!isFromTripleDot) {
				Button(
					modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(bottom = 88.dp),
					colors = buttonColors(backgroundColor = Colors().buttonActive, contentColor = Color.White),
					onClick = {
						navigator.push(CreateSPTForm())
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
		}
	}
}