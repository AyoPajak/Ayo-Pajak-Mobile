package screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ayopajakmobile.composeapp.generated.resources.Res
import ayopajakmobile.composeapp.generated.resources.blog_placeholder
import ayopajakmobile.composeapp.generated.resources.event_placeholder
import ayopajakmobile.composeapp.generated.resources.icon_chevron_right
import ayopajakmobile.composeapp.generated.resources.icon_ebilling
import ayopajakmobile.composeapp.generated.resources.icon_notification
import ayopajakmobile.composeapp.generated.resources.logo_bnw
import ayopajakmobile.composeapp.generated.resources.placeholder_username
import ayopajakmobile.composeapp.generated.resources.promo_placeholder
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import global.Colors
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

class HomeScreen : Screen {

	@Composable
	override fun Content() {
		val navigator = LocalNavigator.current
		val placeholderName = stringResource(Res.string.placeholder_username)

		LazyColumn(
			modifier = Modifier.fillMaxSize().background(Colors().brandDark60)
		) {
			//Header
			item {
				Column(
					modifier = Modifier.padding(16.dp)
				) {
					Row(
						modifier = Modifier.fillMaxWidth(),
						horizontalArrangement = Arrangement.SpaceBetween
					){
						Image(
							modifier = Modifier.height(40.dp).align(Alignment.CenterVertically),
							painter = painterResource(Res.drawable.logo_bnw),
							contentDescription = null
						)
						Image(
							modifier = Modifier.height(20.dp).align(Alignment.CenterVertically),
							painter = painterResource(Res.drawable.icon_notification),
							contentDescription = null
						)
					}
					Text(
						modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
						text = "Hi, $placeholderName",
						color = Color.White,
						fontWeight = FontWeight.Bold,
						fontSize = 20.sp
					)
				}
			}

			//Recent Transaction
			item{
				Box(
					modifier = Modifier
						.padding(horizontal = 16.dp)
						.padding(bottom = 16.dp)
						.fillMaxWidth()
						.clip(shape = RoundedCornerShape(8.dp))
						.border(1.dp, Color.White, RoundedCornerShape(8.dp))
				) {
					Column()
					{
						Row(
							modifier = Modifier.padding(16.dp).fillMaxWidth(),
							horizontalArrangement = Arrangement.SpaceBetween
						) {
							Text(
								text = "Transaksi November 2024",
								color = Color.White,
								fontWeight = FontWeight.Medium,
								fontSize = 12.sp
							)
							
							Image(
								modifier = Modifier.align(Alignment.CenterVertically),
								painter = painterResource(Res.drawable.icon_chevron_right),
								contentDescription = null
							)
						}
						
						//Divider
						Box(
							modifier = Modifier
								.fillMaxWidth()
								.padding(horizontal = 16.dp)
								.height(1.dp)
								.background(Color.White)
						)
						
						Box(
							modifier = Modifier
								.fillMaxWidth()
								.padding(16.dp)
						) {
							Column{
								Row(
									modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
								) {
									Column(
										modifier = Modifier.weight(0.5f)
									) {
										Text(
											text = "SPT",
											modifier = Modifier.padding(bottom = 4.dp),
											color = Color.White,
											fontSize = 10.sp
										)
										Text(
											text = "31",
											color = Color.White,
											fontSize = 14.sp,
											fontWeight = FontWeight.Medium
										)
									}
									Column(
										modifier = Modifier.weight(0.5f)
									) {
										Text(
											text = "e-Billing",
											modifier = Modifier.padding(bottom = 4.dp),
											color = Color.White,
											fontSize = 10.sp
										)
										Text(
											text = "51363135",
											color = Color.White,
											fontSize = 14.sp,
											fontWeight = FontWeight.Medium
										)
									}
								}
								Row(
									modifier = Modifier.fillMaxWidth()
								){
									Column(
										modifier = Modifier.weight(0.5f)
									) {
										Text(
											text = "e-Faktur",
											modifier = Modifier.padding(bottom = 4.dp),
											color = Color.White,
											fontSize = 10.sp
										)
										Text(
											text = "41256342",
											color = Color.White,
											fontSize = 14.sp,
											fontWeight = FontWeight.Medium
										)
									}
									Column(
										modifier = Modifier.weight(0.5f)
									) {
										Text(
											text ="e-Bupot",
											modifier = Modifier.padding(bottom = 4.dp),
											color = Color.White,
											fontSize = 10.sp
										)
										Text(
											text = "3636",
											color = Color.White,
											fontSize = 14.sp,
											fontWeight = FontWeight.Medium
										)
									}
								}
							}
						}
					}
				}
			}
			
			item {
				//Service Category
				Row(
					modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
				) {
					Box(
						modifier = Modifier.padding(top = 16.dp, end = 8.dp)
							.clip(RoundedCornerShape(24.dp))
							.background(Colors().brandDark5)
							.border(1.dp, Colors().textClickable, RoundedCornerShape(24.dp))
					) {
						Text(
							modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
							text = "Pajak",
							fontWeight = FontWeight.Medium,
							fontSize = 12.sp,
							color = Colors().textClickable
						)
					}
					
					Box(
						modifier = Modifier.padding(top = 16.dp, end = 8.dp)
							.clip(RoundedCornerShape(24.dp))
							.background(Colors().brandDark30)
							.border(1.dp, Colors().brandDark40, RoundedCornerShape(24.dp))
					) {
						Text(
							modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
							text = "Non Pajak",
							fontWeight = FontWeight.Medium,
							fontSize = 12.sp,
							color = Color.White
						)
					}
					
					Box(
						modifier = Modifier.padding(top = 16.dp)
							.clip(RoundedCornerShape(24.dp))
							.background(Colors().brandDark30)
							.border(1.dp, Colors().brandDark40, RoundedCornerShape(24.dp))
					) {
						Text(
							modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
							text = "Pembayaran",
							fontWeight = FontWeight.Medium,
							fontSize = 12.sp,
							color = Color.White
						)
					}
				}
			}
			
			item{
				//Services
				LazyRow(
					modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
					contentPadding = PaddingValues(horizontal = 16.dp)
				){
					//e-Billing
					item{
						Box(
							modifier = Modifier.height(76.dp).width(72.dp).padding(end = 8.dp)
						) {
							Column(
								modifier = Modifier.fillMaxSize(),
								verticalArrangement = Arrangement.Center
							) {
								Image(
									modifier = Modifier.height(48.dp).align(Alignment.CenterHorizontally),
									painter = painterResource(Res.drawable.icon_ebilling),
									contentDescription = null
								)
								Text(
									modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
									text = "e-Billing",
									color = Color.White,
									fontSize = 10.sp,
									fontWeight = FontWeight.Bold,
									textAlign = TextAlign.Center
								)
							}
						}
					}

					//e-Faktur
					item{
						Box(
							modifier = Modifier.height(76.dp).width(72.dp).padding(end = 8.dp)
						) {
							Column(
								modifier = Modifier.fillMaxSize(),
								verticalArrangement = Arrangement.Center
							) {
								Image(
									modifier = Modifier.height(48.dp).align(Alignment.CenterHorizontally),
									painter = painterResource(Res.drawable.icon_ebilling),
									contentDescription = null
								)
								Text(
									modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
									text = "e-Faktur",
									color = Color.White,
									fontSize = 10.sp,
									fontWeight = FontWeight.Bold,
									textAlign = TextAlign.Center
								)
							}
						}
					}

					//e-Bupot
					item{
						Box(
							modifier = Modifier.height(76.dp).width(72.dp).padding(end = 8.dp)
						) {
							Column(
								modifier = Modifier.fillMaxSize(),
								verticalArrangement = Arrangement.Center
							) {
								Image(
									modifier = Modifier.height(48.dp).align(Alignment.CenterHorizontally),
									painter = painterResource(Res.drawable.icon_ebilling),
									contentDescription = null
								)
								Text(
									modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
									text = "e-Bupot",
									color = Color.White,
									fontSize = 10.sp,
									fontWeight = FontWeight.Bold,
									textAlign = TextAlign.Center
								)
							}
						}
					}

					//SPT
					item{
						Box(
							modifier = Modifier.height(76.dp).width(72.dp).padding(end = 8.dp)
						) {
							Column(
								modifier = Modifier.fillMaxSize(),
								verticalArrangement = Arrangement.Center
							) {
								Image(
									modifier = Modifier.height(48.dp).align(Alignment.CenterHorizontally),
									painter = painterResource(Res.drawable.icon_ebilling),
									contentDescription = null
								)
								Text(
									modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
									text = "SPT",
									color = Color.White,
									fontSize = 10.sp,
									fontWeight = FontWeight.Bold,
									textAlign = TextAlign.Center
								)
							}
						}
					}

					//Tax Manager
					item{
						Box(
							modifier = Modifier.height(76.dp).width(72.dp).padding(end = 8.dp)
						) {
							Column(
								modifier = Modifier.fillMaxSize(),
								verticalArrangement = Arrangement.Center
							) {
								Image(
									modifier = Modifier.height(48.dp).align(Alignment.CenterHorizontally),
									painter = painterResource(Res.drawable.icon_ebilling),
									contentDescription = null
								)
								Text(
									modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
									text = "Tax Manager",
									color = Color.White,
									fontSize = 10.sp,
									fontWeight = FontWeight.Bold,
									textAlign = TextAlign.Center
								)
							}
						}
					}

					item{
						Box(
							modifier = Modifier.height(76.dp).width(72.dp).padding(end = 8.dp)
						) {
							Column(
								modifier = Modifier.fillMaxSize(),
								verticalArrangement = Arrangement.Center
							) {
								Image(
									modifier = Modifier.height(48.dp).align(Alignment.CenterHorizontally),
									painter = painterResource(Res.drawable.icon_ebilling),
									contentDescription = null
								)
								Text(
									modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
									text = "Tax Manager",
									color = Color.White,
									fontSize = 10.sp,
									fontWeight = FontWeight.Bold,
									textAlign = TextAlign.Center
								)
							}
						}
					}
				}
			}

			//Body
			item{
				Column(
					modifier = Modifier.fillMaxSize()
						.clip(shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
						.background(Color.White)
				){


//                    //Tax Calender Preview
//                    LazyRow(
//                        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
//                        contentPadding = PaddingValues(horizontal = 16.dp)
//                    ){
//                        item{
//                            Box(
//                                modifier = Modifier
//                                    .padding(end = 8.dp)
//                                    .shadow(12.dp, RoundedCornerShape(8.dp), ambientColor = Color.Black)
//                                    .background(Color.White)
//                            ) {
//                                Row(
//                                    modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
//                                    horizontalArrangement = Arrangement.SpaceBetween
//                                ) {
//                                    Column(
//                                        modifier = Modifier.padding(vertical = 24.dp).padding(end = 36.dp)
//                                    ) {
//                                        Text(
//                                            modifier = Modifier.padding(bottom = 16.dp),
//                                            text = "Batas Akhir Penyetoran SPT\nMasa Bea Materai",
//                                            color = Color.Black,
//                                            fontSize = 10.sp
//                                        )
//                                        Text(
//                                            text = "7 Hari Lagi",
//                                            color = Colors().textClickable,
//                                            fontWeight = FontWeight.Bold,
//                                            fontSize = 10.sp
//                                        )
//                                    }
//                                    Image(
//                                        modifier = Modifier.align(Alignment.CenterVertically),
//                                        painter = painterResource(Res.drawable.icon_chevron_right),
//                                        contentDescription = null,
//                                        colorFilter = ColorFilter.tint(Color.Black)
//                                    )
//                                }
//                            }
//                        }
//
//                        item{
//                            Box(
//                                modifier = Modifier
//                                    .padding(end = 8.dp)
//                                    .shadow(12.dp, RoundedCornerShape(8.dp), ambientColor = Color.Black)
//                                    .background(Color.White)
//                            ) {
//                                Row(
//                                    modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
//                                    horizontalArrangement = Arrangement.SpaceBetween
//                                ) {
//                                    Column(
//                                        modifier = Modifier.padding(vertical = 24.dp).padding(end = 36.dp)
//                                    ) {
//                                        Text(
//                                            modifier = Modifier.padding(bottom = 16.dp),
//                                            text = "Batas Akhir Penyetoran SPT\nMasa Bea Materai",
//                                            color = Color.Black,
//                                            fontSize = 10.sp
//                                        )
//                                        Text(
//                                            text = "7 Hari Lagi",
//                                            color = Colors().textClickable,
//                                            fontWeight = FontWeight.Bold,
//                                            fontSize = 10.sp
//                                        )
//                                    }
//                                    Image(
//                                        modifier = Modifier.align(Alignment.CenterVertically),
//                                        painter = painterResource(Res.drawable.icon_chevron_right),
//                                        contentDescription = null,
//                                        colorFilter = ColorFilter.tint(Color.Black)
//                                    )
//                                }
//                            }
//                        }
//
//                        item{
//                            Box(
//                                modifier = Modifier
//                                    .shadow(12.dp, RoundedCornerShape(8.dp), ambientColor = Color.Black)
//                                    .background(Color.White)
//                            ) {
//                                Row(
//                                    modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
//                                    horizontalArrangement = Arrangement.SpaceBetween
//                                ) {
//                                    Column(
//                                        modifier = Modifier.padding(vertical = 24.dp).padding(end = 36.dp)
//                                    ) {
//                                        Text(
//                                            modifier = Modifier.padding(bottom = 16.dp),
//                                            text = "Batas Akhir Penyetoran SPT\nMasa Bea Materai",
//                                            color = Color.Black,
//                                            fontSize = 10.sp
//                                        )
//                                        Text(
//                                            text = "7 Hari Lagi",
//                                            color = Colors().textClickable,
//                                            fontWeight = FontWeight.Bold,
//                                            fontSize = 10.sp
//                                        )
//                                    }
//                                    Image(
//                                        modifier = Modifier.align(Alignment.CenterVertically),
//                                        painter = painterResource(Res.drawable.icon_chevron_right),
//                                        contentDescription = null,
//                                        colorFilter = ColorFilter.tint(Color.Black)
//                                    )
//                                }
//                            }
//                        }
//                    }

					//Promo
					LazyRow(
						modifier = Modifier.padding(vertical = 16.dp),
						contentPadding = PaddingValues(horizontal = 16.dp)
					){
						item{
							Image(
								modifier = Modifier.height(147.dp).width(328.dp).padding(end = 8.dp),
								painter = painterResource(Res.drawable.promo_placeholder),
								contentDescription = null,
								contentScale = ContentScale.FillBounds
							)
						}

						item{
							Image(
								modifier = Modifier.height(147.dp).width(328.dp).padding(end = 8.dp),
								painter = painterResource(Res.drawable.promo_placeholder),
								contentDescription = null,
								contentScale = ContentScale.FillBounds
							)
						}

						item{
							Image(
								modifier = Modifier.height(147.dp).width(328.dp).padding(end = 8.dp),
								painter = painterResource(Res.drawable.promo_placeholder),
								contentDescription = null,
								contentScale = ContentScale.FillBounds
							)
						}
					}

					//Event
					Row(
						modifier = Modifier.fillMaxWidth().padding(16.dp),
						horizontalArrangement = Arrangement.SpaceBetween
					){
						Text(
							text = "Event",
							color = Color(0xFF222222),
							fontWeight = FontWeight.Bold,
							fontSize = 14.sp
						)
						
						Text(
							text = "Lihat Semua",
							modifier = Modifier.align(Alignment.CenterVertically),
							color = Colors().brandDark40,
							fontWeight = FontWeight.Bold,
							fontSize = 12.sp
						)
					}

					//Event Card
					LazyRow(
						modifier = Modifier.padding(bottom = 16.dp),
						contentPadding = PaddingValues(horizontal = 16.dp)
					){
						item{
							Image(
								modifier = Modifier.height(210.dp).width(156.dp).padding(end = 8.dp),
								painter = painterResource(Res.drawable.event_placeholder),
								contentDescription = null,
								contentScale = ContentScale.FillBounds
							)
						}
						
						item{
							Image(
								modifier = Modifier.height(210.dp).width(156.dp).padding(end = 8.dp),
								painter = painterResource(Res.drawable.event_placeholder),
								contentDescription = null,
								contentScale = ContentScale.FillBounds
							)
						}
						
						item{
							Image(
								modifier = Modifier.height(210.dp).width(156.dp).padding(end = 8.dp),
								painter = painterResource(Res.drawable.event_placeholder),
								contentDescription = null,
								contentScale = ContentScale.FillBounds
							)
						}
					}

					//Blog
					Row(
						modifier = Modifier.fillMaxWidth().padding(16.dp),
						horizontalArrangement = Arrangement.SpaceBetween
					){
						Text(
							text = "Blog",
							color = Color(0xFF222222),
							fontWeight = FontWeight.Bold,
							fontSize = 14.sp
						)
						
						Text(
							text = "Lihat Semua",
							modifier = Modifier.align(Alignment.CenterVertically),
							color = Colors().brandDark40,
							fontWeight = FontWeight.Bold,
							fontSize = 12.sp
						)
					}

					//Blog Card
					Column(
						modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(bottom = 88.dp)
					){
						Image(
							modifier = Modifier.fillMaxSize().padding(bottom = 8.dp),
							painter = painterResource(Res.drawable.blog_placeholder),
							contentDescription = null,
							contentScale = ContentScale.FillBounds
						)
						
						Image(
							modifier = Modifier.fillMaxSize().padding(bottom = 8.dp),
							painter = painterResource(Res.drawable.blog_placeholder),
							contentDescription = null,
							contentScale = ContentScale.FillBounds
						)
						
						Image(
							modifier = Modifier.fillMaxSize().padding(bottom = 8.dp),
							painter = painterResource(Res.drawable.blog_placeholder),
							contentDescription = null,
							contentScale = ContentScale.FillBounds
						)
						
						Image(
							modifier = Modifier.fillMaxSize().padding(bottom = 8.dp),
							painter = painterResource(Res.drawable.blog_placeholder),
							contentDescription = null,
							contentScale = ContentScale.FillBounds
						)
						
						Image(
							modifier = Modifier.fillMaxSize().padding(bottom = 8.dp),
							painter = painterResource(Res.drawable.blog_placeholder),
							contentDescription = null,
							contentScale = ContentScale.FillBounds
						)
					}
				}
			}
		}
	}
}