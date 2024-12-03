package screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.*
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
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import global.Colors
import global.PreferencesKey
import global.PreferencesKey.Companion.Address
import global.PreferencesKey.Companion.AyoPajakUserApiToken
import global.PreferencesKey.Companion.CityId
import global.PreferencesKey.Companion.ECertEFakturExpiryDate
import global.PreferencesKey.Companion.ECertEFilingExpiryDate
import global.PreferencesKey.Companion.EFINNo
import global.PreferencesKey.Companion.GenderE
import global.PreferencesKey.Companion.JobId
import global.PreferencesKey.Companion.JobTitle
import global.PreferencesKey.Companion.KluId
import global.PreferencesKey.Companion.MaritalStatusE
import global.PreferencesKey.Companion.NPWP
import global.PreferencesKey.Companion.NPWP_New
import global.PreferencesKey.Companion.RegisterName
import global.PreferencesKey.Companion.SpouseNPWP
import global.PreferencesKey.Companion.TPApiToken
import global.PreferencesKey.Companion.TaxPayerTypeE
import global.PreferencesKey.Companion.TaxStatusE
import global.PreferencesKey.Companion.TelephoneNo
import global.PreferencesKey.Companion.UserApiKey
import global.PreferencesKey.Companion.UserApiSecret
import global.PreferencesKey.Companion.UserGuid
import global.PreferencesKey.Companion.Username
import global.PreferencesKey.Companion.WPNIK
import global.PreferencesKey.Companion.WPName
import global.Variables
import http.Account
import http.Interfaces
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import models.account.ClientProfileResponseApiModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import security.Crypto
import util.onError
import util.onSuccess
import viewmodels.UserProfileModel

class HomeScreen(private val client: Account, private val sptPertamaClient: Interfaces, val cryptoManager: Crypto, private val prefs: DataStore<Preferences>) : Screen {

	@Composable
	override fun Content() {
		val navigator = LocalNavigator.current
		val placeholderName = stringResource(Res.string.placeholder_username)
		
		val scope = rememberCoroutineScope()
		
		var key by remember { mutableStateOf("") }
		var secret by remember { mutableStateOf("") }
		
		runBlocking {
			val pref = prefs.data.first()
			key = pref[stringPreferencesKey(UserApiKey)] ?: ""
			secret = pref[stringPreferencesKey(UserApiSecret)] ?: ""
		}
		
		val GreetingsName by prefs
			.data
			.map {
				it[stringPreferencesKey(WPName)] ?: ""
			}
			.collectAsState(placeholderName)
		
		//LOGIC
		LaunchedEffect(key) {
			var userProfile = UserProfileModel()
			val tokenCoroutine = scope.launch {
				client.ApiToken(userKey = key, userSecret = secret, body = "grant_type=password")
					.onSuccess { prefs.edit { dataStore ->
						dataStore[stringPreferencesKey(AyoPajakUserApiToken)] = it.AccessToken
						userProfile = GetUserProfile(it.AccessToken)
					} }
			}
			tokenCoroutine.join()
			
//			val apiToken = prefs.data.first()[stringPreferencesKey(AyoPajakUserApiToken)] ?: ""
//			val userProfile = GetUserProfile(apiToken)
			
			scope.launch { SetUserProfileLocally(userProfile) }
		}

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
						text = "Hi, $GreetingsName",
						color = Color.White,
						fontWeight = FontWeight.Bold,
						fontSize = 20.sp
					)
				}
			}
			
//			item {
//				//Service Category
//				Row(
//					modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
//				) {
//					Box(
//						modifier = Modifier.padding(top = 16.dp, end = 8.dp)
//							.clip(RoundedCornerShape(24.dp))
//							.background(Colors().brandDark5)
//							.border(1.dp, Colors().textClickable, RoundedCornerShape(24.dp))
//					) {
//						Text(
//							modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
//							text = "Pajak",
//							fontWeight = FontWeight.Medium,
//							fontSize = 12.sp,
//							color = Colors().textClickable
//						)
//					}
//
//					Box(
//						modifier = Modifier.padding(top = 16.dp, end = 8.dp)
//							.clip(RoundedCornerShape(24.dp))
//							.background(Colors().brandDark30)
//							.border(1.dp, Colors().brandDark40, RoundedCornerShape(24.dp))
//					) {
//						Text(
//							modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
//							text = "Non Pajak",
//							fontWeight = FontWeight.Medium,
//							fontSize = 12.sp,
//							color = Color.White
//						)
//					}
//
//					Box(
//						modifier = Modifier.padding(top = 16.dp)
//							.clip(RoundedCornerShape(24.dp))
//							.background(Colors().brandDark30)
//							.border(1.dp, Colors().brandDark40, RoundedCornerShape(24.dp))
//					) {
//						Text(
//							modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
//							text = "Pembayaran",
//							fontWeight = FontWeight.Medium,
//							fontSize = 12.sp,
//							color = Color.White
//						)
//					}
//				}
//			}
			
			item{
				//Services
				LazyRow(
					modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
					contentPadding = PaddingValues(horizontal = 16.dp)
				){
					//SPT
					item{
						Box(
							modifier = Modifier.height(76.dp).width(72.dp).padding(end = 8.dp)
								.clickable(true, onClick = {
									navigator?.push(SPTScreen(client, sptPertamaClient, prefs))
								})
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

//					//e-Faktur
//					item{
//						Box(
//							modifier = Modifier.height(76.dp).width(72.dp).padding(end = 8.dp)
//						) {
//							Column(
//								modifier = Modifier.fillMaxSize(),
//								verticalArrangement = Arrangement.Center
//							) {
//								Image(
//									modifier = Modifier.height(48.dp).align(Alignment.CenterHorizontally),
//									painter = painterResource(Res.drawable.icon_ebilling),
//									contentDescription = null
//								)
//								Text(
//									modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
//									text = "e-Faktur",
//									color = Color.White,
//									fontSize = 10.sp,
//									fontWeight = FontWeight.Bold,
//									textAlign = TextAlign.Center
//								)
//							}
//						}
//					}

//					//e-Bupot
//					item{
//						Box(
//							modifier = Modifier.height(76.dp).width(72.dp).padding(end = 8.dp)
//						) {
//							Column(
//								modifier = Modifier.fillMaxSize(),
//								verticalArrangement = Arrangement.Center
//							) {
//								Image(
//									modifier = Modifier.height(48.dp).align(Alignment.CenterHorizontally),
//									painter = painterResource(Res.drawable.icon_ebilling),
//									contentDescription = null
//								)
//								Text(
//									modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
//									text = "e-Bupot",
//									color = Color.White,
//									fontSize = 10.sp,
//									fontWeight = FontWeight.Bold,
//									textAlign = TextAlign.Center
//								)
//							}
//						}
//					}

//					//Tax Manager
//					item{
//						Box(
//							modifier = Modifier.height(76.dp).width(72.dp).padding(end = 8.dp)
//								.clickable(true, onClick = {
//									navigator?.push(TaxManagerScreen())
//								})
//						) {
//							Column(
//								modifier = Modifier.fillMaxSize(),
//								verticalArrangement = Arrangement.Center
//							) {
//								Image(
//									modifier = Modifier.height(48.dp).align(Alignment.CenterHorizontally),
//									painter = painterResource(Res.drawable.icon_ebilling),
//									contentDescription = null
//								)
//								Text(
//									modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
//									text = "Tax Manager",
//									color = Color.White,
//									fontSize = 10.sp,
//									fontWeight = FontWeight.Bold,
//									textAlign = TextAlign.Center
//								)
//							}
//						}
//					}
				}
			}

			//Body
			item{
				Column(
					modifier = Modifier.fillMaxSize()
						.clip(shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
						.background(Color.White)
				){
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
	
	suspend fun GetUserProfile(apiToken: String): UserProfileModel {
		var result = UserProfileModel()
		client.getUserProfile("Bearer $apiToken")
			.onSuccess {
				result = it.toDataModel()
				return result
			}
			.onError {
				println(it.name)
			}
		return result
	}
	
	suspend fun SetUserProfileLocally(data: UserProfileModel) {
		prefs.edit { dataStore ->
			dataStore[intPreferencesKey(TaxPayerTypeE)] = data.TaxPayerTypeE
			dataStore[stringPreferencesKey(WPNIK)] = data.WPNIK
			dataStore[stringPreferencesKey(WPName)] = data.WPName
			dataStore[stringPreferencesKey(GenderE)] = data.GenderE
			dataStore[intPreferencesKey(MaritalStatusE)] = data.MaritalStatusE
			dataStore[stringPreferencesKey(TaxStatusE)] = data.TaxStatusE
			dataStore[stringPreferencesKey(SpouseNPWP)] = data.SpouseNPWP.toString()
			dataStore[intPreferencesKey(JobId)] = data.JobId
			dataStore[intPreferencesKey(KluId)] = data.KluId
			dataStore[stringPreferencesKey(Address)] = data.Address
			dataStore[intPreferencesKey(CityId)] = data.CityId
			dataStore[stringPreferencesKey(TelephoneNo)] = data.TelephoneNo
			dataStore[stringPreferencesKey(RegisterName)] = data.RegisterName
			dataStore[stringPreferencesKey(JobTitle)] = data.JobTitle
			dataStore[stringPreferencesKey(NPWP)] = data.NPWP
			dataStore[stringPreferencesKey(EFINNo)] = data.EFINNo.toString()
			dataStore[stringPreferencesKey(UserGuid)] = data.UserGuid
			dataStore[stringPreferencesKey(ECertEFilingExpiryDate)] = data.ECertEFilingExpiryDate.toString()
			dataStore[stringPreferencesKey(ECertEFakturExpiryDate)] = data.ECertEFakturExpiryDate.toString()
		}
	}
}