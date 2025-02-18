package screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import ayopajakmobile.composeapp.generated.resources.Icon_Peek
import ayopajakmobile.composeapp.generated.resources.Icon_Peek_Crossed
import ayopajakmobile.composeapp.generated.resources.Res
import ayopajakmobile.composeapp.generated.resources.logo_bnw
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import global.Colors
import global.PreferencesKey
import global.PreferencesKey.Companion.TPApiToken
import global.PreferencesKey.Companion.UserApiKey
import global.PreferencesKey.Companion.UserApiSecret
import global.Variables
import http.Account
import http.Interfaces
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import models.LoginRequest
import models.PertamaGeneralApiResponse
import models.ReturnStatus
import models.account.ApiAuthenticationModel
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.painterResource
import security.Crypto
import tabs.AccountTab
import tabs.HomeTab
import tabs.TaxManagerTab
import util.NetworkError
import util.onError
import util.onSuccess

class LoginScreen(val client: Account, val cryptoManager: Crypto,
									val prefs: DataStore<Preferences>, val sptPertamaClient: Interfaces
) : Screen {
	
	private val userKey = Variables.TPApiUserKey
	private val userSecret = Variables.TPApiUserSecret
	private val cryptoKey = Variables.RijndaelKey
	
	@Composable
	override fun Content() {
		
		val previousUserName by prefs
			.data
			.map {
				it[stringPreferencesKey(PreferencesKey.Username)] ?: ""
			}
			.collectAsState("")
		
		val isLoggedIn by prefs
			.data
			.map {
				it[booleanPreferencesKey(PreferencesKey.IsLoggedIn)] ?: false
			}
			.collectAsState(false)
		
		val navigator = LocalNavigator.currentOrThrow
		val focusManager = LocalFocusManager.current
		
		var email by remember { mutableStateOf("") }
		var pass by remember { mutableStateOf("") }
		
		var isEmailValid by remember { mutableStateOf(false) }
		var isPassValid by remember { mutableStateOf(false) }
		
		var isPasswordVisible by remember { mutableStateOf(false) }
		
		var isLoginSuccess by remember { mutableStateOf(false) }
		
		var enabled by remember { mutableStateOf(false) }
		var isLoading by remember { mutableStateOf(false) }
		
		var errorMessage by remember {
			mutableStateOf<NetworkError?>(null)
		}
		
		val scope = rememberCoroutineScope()
		
		val apiToken by prefs
			.data
			.map {
				it[stringPreferencesKey(TPApiToken)] ?: ""
			}
			.collectAsState("")
		
		fun getUserKeySecret(tokenBearer: String, username: String): ReturnStatus {
			val result = ReturnStatus()
			scope.launch {
				
				client.GetAuthentication(tokenBearer, username)
					.onSuccess {
						val pertamaResponseModel = it

				if (pertamaResponseModel.ErrorCode == 0 && pertamaResponseModel.Data != null) {
					prefs.edit { dataStore ->
						dataStore[stringPreferencesKey(UserApiKey)] = pertamaResponseModel.Data.jsonObject["ApiKey"]?.jsonPrimitive?.content.toString()
						dataStore[stringPreferencesKey(UserApiSecret)] = pertamaResponseModel.Data.jsonObject["ApiSecret"]?.jsonPrimitive?.content.toString()
					}
				} else {
					println("Error")
				}
					}
					.onError {
						println("Error")
					}
			}
			return result
		}
		
		suspend fun login(email: String, pass: String) {
			val tokenCoroutine = scope.launch {
				client.ApiToken(userKey, userSecret, "grant_type=password")
					.onSuccess {
						prefs.edit { dataStore ->
							dataStore[stringPreferencesKey(TPApiToken)] = it.AccessToken
						}
					}
					.onError {
						println("Error Occurred")
					}
			}
			
			tokenCoroutine.join()
			
			scope.launch {
				isLoading = true
				errorMessage = null
				
				val encryptedEmail = cryptoManager.Encrypt(email, cryptoKey)
				val encryptedPass = cryptoManager.Encrypt(pass, cryptoKey)
				
				val loginModel = LoginRequest(encryptedEmail, encryptedPass)
				
				client.Login(loginModel, "Bearer $apiToken")
					.onSuccess {
						if (it.ErrorCode == 0) {
							val isUserNameSameAsPreviousLogin = email == previousUserName
							
							prefs.edit { dataStore ->
								dataStore[stringPreferencesKey(PreferencesKey.Username)] = email
								dataStore[stringPreferencesKey(PreferencesKey.Password)] = encryptedPass
								dataStore[stringPreferencesKey(PreferencesKey.AyoPajakUserApiToken)] = ""
								dataStore[stringPreferencesKey(PreferencesKey.PertamaUserApiToken)] = ""
								dataStore[booleanPreferencesKey(PreferencesKey.IsLoggedIn)] = true
							}
							
							if(!isUserNameSameAsPreviousLogin) {
								prefs.edit { dataStore ->
									dataStore[stringPreferencesKey(PreferencesKey.UserTaxPayerName)] = ""
								}
							}
							
							val getUserKeySecretResult = getUserKeySecret("Bearer $apiToken", email)
							if (!getUserKeySecretResult.IsSuccess) {
								isLoginSuccess = false
							}
							
							isLoginSuccess = true
							focusManager.clearFocus()
						}
					}
					.onError {
//						errorMessage =
					}
				isLoading = false
			}
		}
		
		//Content
		Column(
			modifier = Modifier.fillMaxSize().background(Colors().brandDark60)
				.pointerInput(Unit) {
					detectTapGestures(onTap = {
						focusManager.clearFocus()
					})
				}
		) {
			//Header
			Box(
				modifier = Modifier.fillMaxWidth().weight(2.8f).pointerInput(Unit) {
					detectTapGestures(onTap = {
						focusManager.clearFocus()
					})
				}
			) {
				Image(
					modifier = Modifier.height(80.dp).align(Alignment.Center),
					painter = painterResource(Res.drawable.logo_bnw),
					contentDescription = null
				)
			}
			
			//Body
			Column(
				modifier = Modifier.fillMaxWidth()
					.clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
					.background(Colors().panel)
			) {
				//Greeting
				Text(
					modifier = Modifier.padding(top = 24.dp).padding(bottom = 8.dp).padding(horizontal = 16.dp),
					text = "Selamat Datang!",
					fontSize = 24.sp,
					fontWeight = FontWeight.Bold,
					color = Color.Black
				)
				
				//Subtitle
				Text(
					modifier = Modifier.padding(bottom = 32.dp).padding(horizontal = 16.dp),
					text = "Kelola Pajak dengan Bijak bersama AyoPajak",
					fontSize = 14.sp,
					color = Colors().textDarkGrey
				)
				
				//Email
				Text(
					modifier = Modifier.padding(vertical = 8.dp).padding(horizontal = 16.dp),
					text = "Email",
					fontSize = 12.sp,
					fontWeight = FontWeight.Bold,
					color = Color.Black
				)
				TextField(
					modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp).padding(horizontal = 16.dp)
						.border(
							border = BorderStroke(1.dp, Color.LightGray),
							shape = RoundedCornerShape(4.dp)
						),
					placeholder = { Text("Masukkan email", fontSize = 16.sp,
						color = Colors().textDarkGrey) },
					value = email,
					onValueChange = {
						email = it
						if (email.contains("@") && email.contains(".")) isEmailValid = true else isEmailValid = false
					},
					singleLine = true,
					keyboardOptions = KeyboardOptions(
						keyboardType = KeyboardType.Email,
						imeAction = ImeAction.Next
					),
					colors = TextFieldDefaults.textFieldColors(
						backgroundColor = Color.White,
						focusedIndicatorColor = Color.Transparent,
						unfocusedIndicatorColor = Color.Transparent,
						disabledIndicatorColor = Color.Transparent
					)
				)
				
				//Password
				Text(
					modifier = Modifier.padding(vertical = 8.dp).padding(horizontal = 16.dp),
					text = "Kata Sandi",
					fontSize = 12.sp,
					fontWeight = FontWeight.Bold,
					color = Color.Black
				)
				TextField(
					modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
						.border(
							border = BorderStroke(1.dp, Color.LightGray),
							shape = RoundedCornerShape(4.dp)
						),
					placeholder = { Text("Masukkan kata sandi", fontSize = 16.sp,
						color = Colors().textDarkGrey) },
					value = pass,
					onValueChange = {
						pass = it
						isPassValid = pass.length >= 8
					},
					singleLine = true,
					keyboardOptions = KeyboardOptions(
						keyboardType = KeyboardType.Password,
						imeAction = ImeAction.Done
					),
					visualTransformation = if(!isPasswordVisible) PasswordVisualTransformation() else VisualTransformation.None,
					colors = TextFieldDefaults.textFieldColors(
						backgroundColor = Color.White,
						focusedIndicatorColor = Color.Transparent,
						unfocusedIndicatorColor = Color.Transparent,
						disabledIndicatorColor = Color.Transparent
					),
					trailingIcon = { IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
						Icon(
							painter = if(!isPasswordVisible) painterResource(Res.drawable.Icon_Peek_Crossed) else painterResource(Res.drawable.Icon_Peek),
							contentDescription = "Show Password",
							modifier = Modifier.size(24.dp)
						)
					}}
				)
				
				Text(
					modifier = Modifier.padding(vertical = 16.dp).padding(horizontal = 16.dp).align(Alignment.End)
						.clickable(true, onClick = {
							navigator.push(ForgetPassword())
						}),
					text = "Lupa kata sandi",
					fontSize = 14.sp,
					fontWeight = FontWeight.Bold,
					color = Colors().textClickable
				)
			}
			
			//Footer
			Column(
				modifier = Modifier.weight(1.5f).background(Colors().panel),
				verticalArrangement = Arrangement.Bottom
			)
			{
				LaunchedEffect(enabled) {
					if (enabled) return@LaunchedEffect
					else delay(1000L)
					enabled = true
				}
				
				Button(
					modifier = Modifier.height(48.dp).fillMaxWidth().padding(horizontal = 16.dp),
					colors = buttonColors(backgroundColor = Colors().buttonActive, contentColor = Color.White),
					onClick = {
						enabled = false
						
						scope.launch { login(email, pass) }
					},
					enabled = enabled && isEmailValid && isPassValid
				) {
					if (isLoading) {
						CircularProgressIndicator(
							modifier = Modifier
								.size(15.dp),
							strokeWidth = 1.dp,
							color = Color.White
						)
					} else {
						Text("Masuk", fontSize = 16.sp, fontWeight = FontWeight.Bold)
					}
				}
				
				Row(
					modifier = Modifier.fillMaxWidth().padding(16.dp),
					horizontalArrangement = Arrangement.Center
				) {
					Text(
						text = "Belum punya akun? ",
						fontSize = 14.sp
					)
					Text(
						modifier = Modifier.clickable(true, onClick = {
							println("Daftar")
							navigator.push(RegisterAccount())
						}),
						text = "Daftar",
						fontSize = 14.sp,
						fontWeight = FontWeight.Bold,
						color = Colors().textClickable
					)
				}
			}
		}
		
		isLoginSuccess = isLoggedIn
		if (isLoginSuccess) NavigateToHomeScreen()
	}
	
	@Composable
	fun NavigateToHomeScreen() {
		TabNavigator(HomeTab(client, cryptoManager, prefs, sptPertamaClient)) {
			Scaffold(
				content = {
					CurrentTab()
				},
				bottomBar = {
					BottomNavigation(
						modifier = Modifier.height(80.dp)
							.shadow( //TODO("Create Custom Shadow")
								elevation = 12.dp,
								shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
								spotColor = Color.Black
							)
							.clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
							.background(Color.White),
						backgroundColor = Color.White
					) {
						TabNavigationItem(HomeTab(client, cryptoManager, prefs, sptPertamaClient))
						TabNavigationItem(TaxManagerTab)
						TabNavigationItem(AccountTab(client, cryptoManager, prefs, sptPertamaClient))
					}
				}
			)
		}
	}
}

@Composable
private fun RowScope.TabNavigationItem(tab: Tab) {
	val tabNavigator = LocalTabNavigator.current
	
	val isSelected = tabNavigator.current == tab
	
	BottomNavigationItem(
		modifier = Modifier.align(Alignment.CenterVertically),
		selected = tabNavigator.current == tab,
		onClick = { tabNavigator.current = tab },
		icon = {
			tab.options.icon?.let {
				Image(
					painter = it,
					contentDescription = tab.options.title,
					colorFilter = if(!isSelected) ColorFilter.colorMatrix(ColorMatrix().apply {
						setToSaturation(0f)
					})  else null,
				)
			} },
		label = { Text(text = tab.options.title, fontWeight = if(isSelected) FontWeight.Bold else FontWeight.Normal) },
		alwaysShowLabel = true,
	)
}