package screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ayopajakmobile.composeapp.generated.resources.Res
import ayopajakmobile.composeapp.generated.resources.icon_notification_border
import cafe.adriel.voyager.core.screen.Screen
import global.Colors
import global.universalUIComponents.topBar
import org.jetbrains.compose.resources.painterResource

class SPTScreen: Screen {
	
	@Composable
	override fun Content() {
		Column(
			modifier = Modifier.fillMaxSize().background(Colors().brandDark50),
		) {
			topBar(
				"e-SPT Orang Pribadi",
				textColor = Color.White,
				bgColor = Colors().brandDark50,
				buttonColor = Colors().panel
			)
			
			LazyColumn {
				//Notification
				item{
					Box(
						modifier = Modifier
							.fillMaxWidth()
							.padding(horizontal = 16.dp)
							.clip(RoundedCornerShape(8.dp))
							.border(1.dp, color = Color.White, RoundedCornerShape(8.dp))
							.background(Color(0xFF4974FC))
					) {
						Row(
							modifier = Modifier.padding(16.dp),
							verticalAlignment = Alignment.CenterVertically
						) {
							Image(
								painter = painterResource(Res.drawable.icon_notification_border),
								modifier = Modifier.padding(end = 16.dp),
								contentDescription = null
							)
							
							Column {
								Text(
									modifier = Modifier.padding(bottom = 8.dp),
									text = "Batas pelaporan SPT",
									fontSize = 12.sp,
									color = Color.White
								)
								
								Text(
									text = "24 Februari 2025",
									fontSize = 12.sp,
									color = Color.White,
									fontWeight = FontWeight.Bold
								)
							}
						}
					}
				}
				
				//Statistic
				item{
					Box(
						modifier = Modifier
							.fillMaxWidth()
							.padding(horizontal = 16.dp)
							.padding(vertical = 24.dp)
							.clip(RoundedCornerShape(8.dp))
							.border(1.dp, color = Color.White, RoundedCornerShape(8.dp))
							.background(Color(0xFF4974FC))
					) {
						Column(
							modifier = Modifier
								.padding(8.dp)
								.padding(top = 12.dp)
						) {
							Text(
								modifier = Modifier.padding(start = 8.dp).padding(bottom = 12.dp),
								text = "2024",
								fontSize = 10.sp,
								color = Color.White,
								fontWeight = FontWeight.Bold
							)
							
							Text(
								modifier = Modifier.padding(start = 8.dp).padding(bottom = 8.dp),
								text = "Harta saya",
								fontSize = 10.sp,
								color = Color.White
							)
							
							Row(
								modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp).padding(bottom = 8.dp),
								verticalAlignment = Alignment.CenterVertically,
								horizontalArrangement = Arrangement.SpaceBetween
							) {
								Text(
									text = "124,012,000",
									fontSize = 16.sp,
									color = Color.White,
									fontWeight = FontWeight.Bold
								)
								
								Box(
									modifier = Modifier.clip(CircleShape)
										.background(Colors().panelGreen)
								) {
									Text(
										modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
										text = "24%",
										fontSize = 12.sp,
										color = Colors().textGreen,
										fontWeight = FontWeight.Bold
									)
								}
							}
							
							Row(
								modifier = Modifier.fillMaxWidth()
							) {
								Box(
									modifier = Modifier.weight(0.5f).padding(end = 4.dp)
										.clip(RoundedCornerShape(4.dp)).background(Colors().panelGreen)
								) {
									Column {
										Text(
											modifier = Modifier.padding(start = 16.dp).padding(top = 20.dp).padding(bottom = 8.dp),
											text = "Pendapatan",
											fontSize = 10.sp,
											color = Colors().textGreen,
										)
										
										Text(
											modifier = Modifier.padding(start = 16.dp).padding(bottom = 20.dp),
											text = "124,012,000",
											fontSize = 10.sp,
											color = Colors().textGreen,
											fontWeight = FontWeight.Bold
										)
									}
								}
								
								Box(
									modifier = Modifier.weight(0.5f).padding(start = 4.dp)
										.clip(RoundedCornerShape(4.dp)).background(Colors().panelRed)
								) {
									Column {
										Text(
											modifier = Modifier.padding(start = 16.dp).padding(top = 20.dp).padding(bottom = 8.dp),
											text = "Utang",
											fontSize = 10.sp,
											color = Colors().textRed,
										)
										
										Text(
											modifier = Modifier.padding(start = 16.dp).padding(bottom = 20.dp),
											text = "124,012,000",
											fontSize = 10.sp,
											color = Colors().textRed,
											fontWeight = FontWeight.Bold
										)
									}
								}
							}
						}
					}
				}
			}
		}
	}
}