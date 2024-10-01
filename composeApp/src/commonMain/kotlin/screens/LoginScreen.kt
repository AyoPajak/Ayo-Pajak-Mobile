package screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.VectorComposable
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import ayopajakmobile.composeapp.generated.resources.Res
import ayopajakmobile.composeapp.generated.resources.*
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import global.Variables
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import models.LoginRequest
import org.jetbrains.compose.resources.painterResource
import security.Crypto
import http.Account
import org.jetbrains.compose.resources.vectorResource
import tabs.AccountTab
import tabs.HomeTab
import util.NetworkError
import util.onError
import util.onSuccess

class LoginScreen(val client: Account, val cryptoManager: Crypto,
                  val dataStore: DataStore<Preferences>
) : Screen {

    private val userKey = Variables.TPApiUserKey
    private val userSecret = Variables.TPApiUserSecret
    private val cryptoKey = Variables.RijndaelKey

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        var email by remember { mutableStateOf("") }
        var pass by remember { mutableStateOf("") }

        var isPasswordVisible = false

        var isLoginSuccess by remember { mutableStateOf(false) }

        var isLoading by remember {
            mutableStateOf(false)
        }

        var errorMessage by remember {
            mutableStateOf<NetworkError?>(null)
        }

        val scope = rememberCoroutineScope()

        var apiToken = ""
        scope.launch {
            client.GetApiToken(userKey, userSecret, "grant_type=password")
                .onSuccess {
                    apiToken = it.AccessToken
                }
                .onError {
                    apiToken = ""
                }
        }

        val focusManager = LocalFocusManager.current

        //Header
        Row (
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp).pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }
        ) {
            Image (
                modifier = Modifier.height(80.dp),
                painter = painterResource(Res.drawable.ayo_pajak_logo),
                contentDescription = null
            )
        }

        //Body
        Column (
            modifier = Modifier.fillMaxSize().padding(16.dp).pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            },
            verticalArrangement = Arrangement.Center
        ) {
            //Greeting
            Text(
                modifier = Modifier.padding(vertical = 8.dp),
                text = "Selamat Datang!",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            //Subtitle
            Text(
                modifier = Modifier.padding(bottom = 32.dp),
                text = "Kelola Pajak dengan Bijak bersama AyoPajak",
                fontSize = 16.sp,
                color = Color.Gray
            )

            //Email
            Text(
                modifier = Modifier.padding(vertical = 8.dp),
                text = "Email",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            TextField(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                    .border(border = BorderStroke(1.dp, Color.LightGray), shape = RoundedCornerShape(4.dp)),
                placeholder = { Text("Masukkan Email") },
                value = email,
                onValueChange = { email = it },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent)
            )

            //Password
            Text(
                modifier = Modifier.padding(vertical = 8.dp),
                text = "Kata Sandi",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            TextField(
                modifier = Modifier.fillMaxWidth()
                    .border(border = BorderStroke(1.dp, Color.LightGray), shape = RoundedCornerShape(4.dp)),
                placeholder = { Text("Masukkan Kata Sandi") },
                value = pass,
                onValueChange = { pass = it },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                visualTransformation = PasswordVisualTransformation(),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent)
            )

            Text(
                modifier = Modifier.padding(vertical = 16.dp).align(Alignment.End).
                clickable(true, onClick = {
                    println("Lupa Password")
                }),
                text = "Lupa kata sandi",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Blue
            )
        }

        //Footer
        Column (
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Bottom
        )
        {
            var enabled by remember { mutableStateOf(false) }

            LaunchedEffect(enabled) {
                if (enabled) return@LaunchedEffect
                else delay(1000L)
                enabled = true
            }

            Button (
                modifier = Modifier.height(48.dp).fillMaxWidth(),
                colors = buttonColors(backgroundColor = Color.Blue, contentColor = Color.White),
                onClick = {
                    enabled = false

                    scope.launch {
                        isLoading = true
                        errorMessage = null

                        val encryptedEmail = cryptoManager.Encrypt(email, cryptoKey)
                        val encryptedPass = cryptoManager.Encrypt(pass, cryptoKey)

                        val loginModel = LoginRequest(encryptedEmail, encryptedPass)

                        client.Login(loginModel, "Bearer $apiToken")
                            .onSuccess {
                                println(it)
                                if (it.ErrorCode == 0) {
                                    isLoginSuccess = true
                                    focusManager.clearFocus()
                                }
                            }
                            .onError {
                                errorMessage = it
                            }
                        isLoading = false
                    }
                },
                enabled = enabled
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(15.dp),
                        strokeWidth = 1.dp,
                        color = Color.White
                    )
                } else {
                    Text("Masuk")
                }
            }

            Row(modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.Center) {
                Text(
                    text = "Belum punya akun? ",
                    fontSize = 16.sp,
                )
                Text(
                    modifier = Modifier.clickable(true, onClick = {
                        println("Daftar")
                    }),
                    text = "Daftar",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color= Color.Blue
                )
            }
        }

        if (isLoginSuccess) NavigateToHomeScreen()
    }
}

@Composable
private fun NavigateToHomeScreen() {
    TabNavigator(HomeTab) {
        Scaffold(
            content = {
                CurrentTab()
            },
            bottomBar = {
                BottomNavigation {
                    TabNavigationItem(HomeTab)
                    TabNavigationItem(AccountTab)
                }
            }
        )
    }
}

@Composable
private fun RowScope.TabNavigationItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current

    BottomNavigationItem(
        selected = tabNavigator.current == tab,
        onClick = { tabNavigator.current = tab },
        icon = { tab.options.icon?.let { Icon(painter = it, contentDescription = tab.options.title) } }
    )
}