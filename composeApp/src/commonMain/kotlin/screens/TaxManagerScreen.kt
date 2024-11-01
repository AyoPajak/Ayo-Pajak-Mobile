package screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ayopajakmobile.composeapp.generated.resources.Res
import ayopajakmobile.composeapp.generated.resources.chart_placeholder
import cafe.adriel.voyager.core.screen.Screen
import global.Colors
import global.universalUIComponents.topBar
import org.jetbrains.compose.resources.painterResource

class TaxManagerScreen : Screen {

	@Composable
	override fun Content() {
		
		Column(
			modifier = Modifier.fillMaxSize().background(Colors().brandDark50),
		) {
			topBar(
				"Tax Manager",
				textColor = Color.White,
				bgColor = Colors().brandDark50,
				buttonColor = Colors().panel
			)
			LazyColumn(
			
			) {
				item {
					Box(
						modifier = Modifier.padding(16.dp)
							.fillMaxWidth()
							.clip(RoundedCornerShape(8.dp))
							.border(1.dp, Color.White, RoundedCornerShape(8.dp))
					) {
						Column(
							modifier = Modifier.padding(horizontal = 16.dp).padding(vertical = 24.dp)
						) {
							Text(
								text = "Kekayaan Bersih",
								color = Color.White,
								fontSize = 12.sp
							)
							
							Row(
								modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
								horizontalArrangement = Arrangement.SpaceBetween,
								verticalAlignment = Alignment.CenterVertically
							) {
								Text(
									text = "325,235,347,146",
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
						}
					}
				}
				
				item {
					Image(
						modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp).height(214.dp),
						painter = painterResource(Res.drawable.chart_placeholder),
						contentDescription = null
					)
				}
				
				item {
					Box(
						modifier = Modifier.fillMaxWidth()
							.clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
							.background(Colors().panel)
					) {
						Column(
							modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 20.dp).padding(bottom = 88.dp)
						) {
							Text(
								modifier = Modifier.padding(bottom = 12.dp),
								text = "Kelola Pajak",
								fontSize = 14.sp,
								color = Color.Black,
								fontWeight = FontWeight.Bold
							)
							itemBox(Colors().textGreen, "Penghasilan", "124,012,000")
							itemBox(Colors().textRed, "Utang", "124,012,000")
							itemBox(Colors().textBlack, "Harta", "124,012,000")
						}
					}
				}
			}
		}
	}
}


@Composable
fun itemBox(barColor: Color, title: String, value: String) {
	Box(
		modifier = Modifier
			.fillMaxWidth()
			.padding(bottom = 8.dp)
			.clip(RoundedCornerShape(4.dp))
			.border(1.dp, Colors().slate20, RoundedCornerShape(4.dp))
	) {
		Row(
			modifier = Modifier.fillMaxWidth().padding(16.dp),
			verticalAlignment = Alignment.CenterVertically
		) {
			Box(
				modifier = Modifier
					.width(12.dp)
					.height(48.dp)
					.padding(end = 8.dp)
					.background(barColor)
			) {
				Text("")
			}
			
			Column {
				Text(
					modifier = Modifier.padding(bottom = 12.dp),
					text = title,
					fontSize = 10.sp,
					color = Colors().textBlack
				)
				Text(
					text = value,
					fontSize = 14.sp,
					color = Colors().textBlack,
					fontWeight = FontWeight.Bold
				)
			}
		}
	}
}