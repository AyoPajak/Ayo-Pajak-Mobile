package screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ayopajakmobile.composeapp.generated.resources.Res
import ayopajakmobile.composeapp.generated.resources.arrow_back
import ayopajakmobile.composeapp.generated.resources.button_back
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import global.Colors
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import models.LoginRequest
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.resources.vectorResource
import util.NetworkError
import util.onError
import util.onSuccess

class ForgetPassword: Screen {

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow

        var enabled by remember { mutableStateOf(false) }
        var isLoading by remember { mutableStateOf(false) }

        var email by remember { mutableStateOf("") }

        var errorMessage by remember {
            mutableStateOf<NetworkError?>(null)
        }

        val scope = rememberCoroutineScope()

        Column(
            modifier = Modifier.fillMaxSize().background(Colors().panel)
        ) {
            //TopBar
            global.universalUIComponents.topBar("")

            //Body
            Column(
                modifier = Modifier.fillMaxWidth().weight(7.25f)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    //Title
                    Text(
                        text = "Atur Ulang Kata Sandi",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.sp
                    )

                    //Subtitle
                    Text(
                        modifier = Modifier.padding(top = 16.dp),
                        text = "Masukkan e-mail yang terdaftar di Ayopajak.\nKami akan mengirimkan kode verifikasi untuk atur ulang kata sandi.",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        letterSpacing = 0.sp
                    )

                    Text(
                        modifier = Modifier.padding(top = 32.dp).padding(bottom = 8.dp),
                        text = "Email terdaftar",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    TextField(
                        modifier = Modifier.fillMaxWidth()
                            .border(
                                border = BorderStroke(1.dp, Color.LightGray),
                                shape = RoundedCornerShape(4.dp)
                            ),
                        placeholder = { Text("Masukkan Email") },
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
                }
            }
            //Footer
            Column(
                modifier = Modifier.fillMaxWidth().weight(1.5f),
                verticalArrangement = Arrangement.Bottom
            ) {
                LaunchedEffect(enabled) {
                    if (enabled) return@LaunchedEffect
                    else delay(1000L)
                    enabled = true
                }

                Button(
                    modifier = Modifier.height(88.dp).fillMaxWidth().padding(16.dp),
                    colors = buttonColors(backgroundColor = Colors().buttonActive, contentColor = Color.White),
                    onClick = {
                        println("send email")
                        //TODO("Implement Forget Password -> Post email to API")
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
                        Text("Selanjutnya")
                    }
                }
            }
        }
    }
}
