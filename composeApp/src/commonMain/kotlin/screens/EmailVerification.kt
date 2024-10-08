package screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key.Companion.T
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ayopajakmobile.composeapp.generated.resources.Res
import ayopajakmobile.composeapp.generated.resources.email_verification
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import global.Colors
import global.universalUIComponents.topBar
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.resources.painterResource

class EmailVerification(val email: String, val isFromRegister:Boolean): Screen {

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.currentOrThrow

        var enabled by remember { mutableStateOf(false) }
        var isLoading by remember { mutableStateOf(false) }
        val delayTime = 32000L
        var timer by remember { mutableStateOf(delayTime.toInt()/1000 - 2) }

        Column(
            modifier = Modifier.fillMaxSize().background(Colors().panel)
        ) {
            //Topbar
            topBar("Verifikasi Email")

            //Body
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp).align(Alignment.CenterHorizontally)
                ) {
                    Image(
                        modifier = Modifier.fillMaxWidth().fillMaxHeight(0.5f),
                        painter = painterResource(Res.drawable.email_verification),
                        contentDescription = null
                    )

                    Text(
                        modifier = Modifier.fillMaxWidth().padding(top = 64.dp),
                        text = "Verifikasi akan dikirim ke email",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        text = email,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Colors().textClickable,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        text = "Klik tautan di email kamu dan kembali ke aplikasi Ayopajak untuk melanjutkan proses pembaruan kata sandi ya!",
                        fontSize = 14.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Center
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
                    else delay(delayTime)
                    enabled = true
                }

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Belum dapat email?",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )

                Button(
                    modifier = Modifier.height(88.dp).fillMaxWidth().padding(16.dp),
                    colors = buttonColors(backgroundColor = Colors().buttonActive, contentColor = Color.White),
                    onClick = {
                        enabled = false
                        timer = delayTime.toInt()/1000 - 2

                        if (isFromRegister) {
                            //TODO("Implement re-Post register email to API")
                        } else {
                            //TODO("Implement re-Post forget password email to API")
                        }
                    },
                    enabled = enabled
                ) {
                    LaunchedEffect(timer) {
                        if (timer >= 0) {
                            delay(1000L)
                            timer -= 1
                        }
                    }

                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(15.dp),
                            strokeWidth = 1.dp,
                            color = Color.White
                        )
                    } else {
                        if ( timer >= 10)
                            Text("Kirim Ulang (00:$timer)", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        else if (timer >= 0)
                            Text("Kirim Ulang (00:0$timer)", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        else
                            Text("Kirim Ulang", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}