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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
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
import global.Variables
import http.Account
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import models.LoginRequest
import org.jetbrains.compose.resources.painterResource
import security.Crypto
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

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        var email by remember { mutableStateOf("") }
        var pass by remember { mutableStateOf("") }

        var isPasswordVisible = false

        var isLoginSuccess by remember { mutableStateOf(false) }

        var enabled by remember { mutableStateOf(false) }
        var isLoading by remember { mutableStateOf(false) }

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
                    .weight(5.2f)
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
                    color = Color.Gray
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
                    placeholder = { Text("Masukkan Email", fontSize = 16.sp) },
                    value = email,
                    onValueChange = { email = it },
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
                    placeholder = { Text("Masukkan Kata Sandi", fontSize = 16.sp) },
                    value = pass,
                    onValueChange = { pass = it },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()

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
                                        pass = ""
                                    }
                                isLoading = false
                            }
                        }
                    ),
                    visualTransformation = PasswordVisualTransformation(),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
//                    trailingIcon = { IconButton(onClick = { showPassword = !showPassword }) {
//                        Icon(imageVector = if(showPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility, contentDescription = "Show Password")
//                    }})
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
                modifier = Modifier.background(Colors().panel),
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
                            //TODO("Implement Registration")
                        }),
                        text = "Daftar",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Colors().textClickable
                    )
                }
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