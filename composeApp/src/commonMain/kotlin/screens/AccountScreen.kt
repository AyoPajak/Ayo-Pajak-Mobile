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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ayopajakmobile.composeapp.generated.resources.Res
import ayopajakmobile.composeapp.generated.resources.icon_account
import ayopajakmobile.composeapp.generated.resources.icon_accountsetting
import ayopajakmobile.composeapp.generated.resources.icon_bill
import ayopajakmobile.composeapp.generated.resources.icon_changepass
import ayopajakmobile.composeapp.generated.resources.icon_edit
import ayopajakmobile.composeapp.generated.resources.icon_help
import ayopajakmobile.composeapp.generated.resources.icon_logout
import ayopajakmobile.composeapp.generated.resources.icon_usage
import ayopajakmobile.composeapp.generated.resources.logo_bnw
import ayopajakmobile.composeapp.generated.resources.placeholder_NPWP
import ayopajakmobile.composeapp.generated.resources.placeholder_username
import cafe.adriel.voyager.core.screen.Screen
import global.Colors
import global.universalUIComponents.popUpBox
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

class AccountScreen: Screen {
	
	@Composable
	override fun Content() {
		
		var showLogoutPopUp by remember { mutableStateOf(false) }
		
		Column(
			modifier = Modifier.fillMaxSize().background(Colors().panel)
		) {
			//Title
			Text(
				modifier = Modifier.padding(horizontal = 16.dp).padding(vertical = 32.dp),
				text = "Pengaturan Akun",
				fontSize = 16.sp,
				fontWeight = FontWeight.Bold)
			
			//Profile Card
			Box(
				modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(bottom = 8.dp).clip(shape = RoundedCornerShape(8.dp)).background(Colors().brandDark50)
			){
				Column(
					modifier = Modifier.fillMaxWidth().padding(16.dp)
				) {
					Image(
						modifier = Modifier.padding(bottom = 64.dp).padding(top = 8.dp).size(96.dp, 48.dp),
						painter = painterResource(Res.drawable.logo_bnw),
						contentDescription = null
					)
					
					Row(
						modifier = Modifier.fillMaxWidth(),
						horizontalArrangement = Arrangement.SpaceBetween,
					) {
						Column {
							Text(text = stringResource(Res.string.placeholder_username), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
							Text(text = stringResource(Res.string.placeholder_NPWP), fontSize = 10.sp, color = Color.White)
						}
						Image(
							modifier = Modifier.size(24.dp),
							painter = painterResource(Res.drawable.icon_edit),
							contentDescription = null
						)
					}
				}
			}
			
			//Items
			selection(painterResource(Res.drawable.icon_accountsetting), "Kelola Akun", "Kelola akun wajib pajak")
			divider()
			selection(painterResource(Res.drawable.icon_usage), "Pemakaian", "Track jumlah Pemakaian anda")
			divider()
			selection(painterResource(Res.drawable.icon_bill), "Tagihan", "Cek tagihan anda")
			divider()
			selection(painterResource(Res.drawable.icon_changepass), "Ubah Password", "Kirimkan link perubahan password")
			divider()
			selection(painterResource(Res.drawable.icon_help), "Bantuan", "Bantuan")
			divider()
			
			//Logout
			Row(
				modifier = Modifier.fillMaxWidth().padding(16.dp).clickable(!showLogoutPopUp, onClick = {
					showLogoutPopUp = true
				}),
				verticalAlignment = Alignment.CenterVertically
			) {
				Image(
					modifier = Modifier.size(24.dp),
					painter = painterResource(Res.drawable.icon_logout),
					contentDescription = null
				)
				Text(
					modifier = Modifier.padding(start = 16.dp),
					text = "Logout", fontSize = 14.sp,
					fontWeight = FontWeight.Bold,
					color = Colors().textRed
				)
			}
		}
		
		popUpBox(
			popupWidth = 300f,
			popupHeight = 200f,
			showPopup = showLogoutPopUp,
			onClickOutside = { showLogoutPopUp = false},
			content = {
				Column(
				
				) {
					Text(
						modifier = Modifier.fillMaxWidth().padding(top = 32.dp),
						text = "Logout?",
						fontSize = 20.sp,
						fontWeight = FontWeight.Bold,
						textAlign = TextAlign.Center
					)
					Text(
						modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
						text = "Anda yakin untuk logout?",
						fontSize = 14.sp,
						textAlign = TextAlign.Center
					)
				}
			}
		)
	}
}

@Composable
fun selection(icon: Painter, title: String, subTitle: String){
	Row(
		modifier = Modifier.fillMaxWidth().padding(16.dp),
		verticalAlignment = Alignment.CenterVertically
	) {
		Image(
			modifier = Modifier.size(24.dp),
			painter = icon,
			contentDescription = null
		)
		
		Column(
			modifier = Modifier.padding(start = 16.dp)
		) {
			Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold)
			Text(text = subTitle, fontSize = 10.sp, color = Colors().textDarkGrey)
		}
	}
}

@Composable
fun divider(){
	Box(
		modifier = Modifier
			.fillMaxWidth()
			.padding(horizontal = 16.dp)
			.height(1.dp)
			.background(Colors().slate30)
	)
}