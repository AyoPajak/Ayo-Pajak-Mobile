package screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import global.Colors
import global.universalUIComponents.topBar

class DetailCreateSPTScreen(private val formType: String, private val taxYear: String, private val correctionSeq: String): Screen {
	
	@Composable
	override fun Content() {
		
		val navigator = LocalNavigator.currentOrThrow
		
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
					topBar("")
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
							//TODO("Create Progress Bar")
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
							step(1, "Pengisian Identitas")
							divider(0.dp)
							step(2, "Daftar Keluarga & Tanggungan")
							divider(0.dp)
							step(3, "Harta")
							divider(0.dp)
							step(4, "Utang")
							divider(0.dp)
							step(5, "Penghasilan Pajak Final")
							divider(0.dp)
							step(6, "Penghasilan Non Objek Pajak")
							divider(0.dp)
							step(7, "Kredit Pajak")
							divider(0.dp)
							step(8, "Penghasilan Neto Dalam Negeri")
							divider(0.dp)
							step(9, "Penghasilan Neto Lainnya")
							divider(0.dp)
							step(10, "PPh Terutang (PH/MT)")
							divider(0.dp)
							step(11, "Detail Lainnya")
							divider(0.dp)
							step(12, "Surat Setoran Pajak")
							divider(0.dp)
							step(13, "Data Pelengkap")
							divider(0.dp)
							step(14, "Konfirmasi")
						}
					}
				}
				
				item {
					Button(
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
			
			Box (
				modifier =  Modifier
					.fillMaxWidth()
					.padding(top = 16.dp, bottom = 88.dp)
					.background(Color.White)
			) {
			
			}
		}
	}
}

@Composable
fun step(number: Int, title: String, status: Boolean = false) {
	Row(
		modifier = Modifier.fillMaxWidth().padding(16.dp),
		verticalAlignment = Alignment.CenterVertically
	) {
		Box (
			modifier = Modifier
				.padding(end = 8.dp)
				.size(32.dp)
				.clip(CircleShape)
				.background(Colors().brandDark5)
		) {
			Text(
				text = "$number",
				fontSize = 16.sp,
				fontWeight = FontWeight.Bold,
				color = Colors().brandDark40,
				modifier = Modifier.align(Alignment.Center)
			)
		}
		
		Text (
			text = title,
			fontSize = 14.sp,
		)
	}
}