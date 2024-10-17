package screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import ayopajakmobile.composeapp.generated.resources.Res
import ayopajakmobile.composeapp.generated.resources.logo_bnw
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import global.Colors
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource

class RegisterAccount: Screen {

	@Composable
	override fun Content() {

		var email by remember { mutableStateOf("") }
		var pass by remember { mutableStateOf("") }
		var confirmPass by remember { mutableStateOf("") }
		var refCode by remember { mutableStateOf("") }

		var enabled by remember { mutableStateOf(false) }
		var isLoading by remember { mutableStateOf(false) }

		var isEmailValid by remember { mutableStateOf(false) }
		var isPassValid by remember { mutableStateOf(false) }
		var isConfirmPassValid by remember { mutableStateOf(false) }

		val focusManager = LocalFocusManager.current
		val navigator = LocalNavigator.currentOrThrow

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
				modifier = Modifier.fillMaxWidth().weight(2f).pointerInput(Unit) {
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
			LazyColumn(
				modifier = Modifier.fillMaxWidth()
					.clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
					.background(Colors().panel)
			) {
				//Title
				item {
					Text(
						modifier = Modifier.padding(vertical = 24.dp).padding(horizontal = 16.dp),
						text = "Daftar Akun Baru",
						fontSize = 24.sp,
						fontWeight = FontWeight.Bold
					)
				}

				//Email
				item {
					Text(
						modifier = Modifier.padding(vertical = 8.dp).padding(horizontal = 16.dp),
						text = "Email",
						fontSize = 12.sp,
						fontWeight = FontWeight.Bold
					)
					TextField(
						modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
							.padding(horizontal = 16.dp)
							.border(
								border = BorderStroke(1.dp, Color.LightGray),
								shape = RoundedCornerShape(4.dp)
							),
						placeholder = {
							Text(
								"Masukkan email", fontSize = 16.sp,
								color = Colors().textDarkGrey
							)
						},
						value = email,
						onValueChange = {
							email = it
							isEmailValid = email.contains("@") && email.contains(".")
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
				}

				//Pass
				item {
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
								border = BorderStroke(1.dp, if (pass.isNotBlank()) { if (isPassValid) Colors().textGreen else Colors().textRed } else Color.LightGray),
								shape = RoundedCornerShape(4.dp)
							),
						placeholder = {
							Text(
								"Masukkan kata sandi", fontSize = 16.sp,
								color = Colors().textDarkGrey
							)
						},
						value = pass,
						onValueChange = {
							pass = it
							isPassValid = checkPassValidity(pass)
						},
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
							disabledIndicatorColor = Color.Transparent
						),
//                    trailingIcon = { IconButton(onClick = { showPassword = !showPassword }) {
//                        Icon(imageVector = if(showPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility, contentDescription = "Show Password")
//                    }})
					)

					//PassHelperText
					Text(
						modifier = Modifier.padding(top = 8.dp).padding(bottom = 16.dp)
							.padding(horizontal = 24.dp),
						text = "Kata sandi harus berisi minimal 8 karakter,\n1 huruf besar, 1 angka dan 1 simbol.",
						fontSize = 12.sp,
						color = if (pass.isNotBlank()) { if (isPassValid) Colors().textGreen else Colors().textRed } else Colors().textDarkGrey
					)
				}

				//Confirm Pass
				item {
					Text(
						modifier = Modifier.padding(vertical = 8.dp).padding(horizontal = 16.dp),
						text = "Konfirmasi Kata Sandi",
						fontSize = 12.sp,
						fontWeight = FontWeight.Bold,
						color = Color.Black
					)
					TextField(
						modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp).padding(horizontal = 16.dp)
							.border(
								border = BorderStroke(1.dp, if (confirmPass.isNotBlank()) { if (isConfirmPassValid) Colors().textGreen else Colors().textRed } else Color.LightGray),
								shape = RoundedCornerShape(4.dp)
							),
						placeholder = {
							Text(
								"Konfirmasi kata sandi", fontSize = 16.sp,
								color = Colors().textDarkGrey
							)
						},
						value = confirmPass,
						onValueChange = {
							confirmPass = it
							isConfirmPassValid = confirmPass == pass && confirmPass.isNotBlank()
						},
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
							disabledIndicatorColor = Color.Transparent
						)
					)
				}

				//RefCode
				item {
					Text(
						modifier = Modifier.padding(vertical = 8.dp).padding(horizontal = 16.dp),
						text = "Kode Refferal (opsional)",
						fontSize = 12.sp,
						fontWeight = FontWeight.Bold
					)
					TextField(
						modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
							.padding(horizontal = 16.dp)
							.border(
								border = BorderStroke(1.dp, Color.LightGray),
								shape = RoundedCornerShape(4.dp)
							),
						placeholder = {
							Text(
								"Masukkan kode refferal", fontSize = 16.sp,
								color = Colors().textDarkGrey
							)
						},
						value = refCode,
						onValueChange = { refCode = it },
						singleLine = true,
						keyboardOptions = KeyboardOptions(
							keyboardType = KeyboardType.Email,
							imeAction = ImeAction.Done
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
				modifier = Modifier.fillMaxWidth().weight(1.5f).background(Colors().panel)
			) {
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
						navigator.push(TermsAndCondition(email))
					},
					enabled = enabled && isEmailValid && isPassValid && isConfirmPassValid
				) {
					if (isLoading) {
						CircularProgressIndicator(
							modifier = Modifier
								.size(15.dp),
							strokeWidth = 1.dp,
							color = Color.White
						)
					} else {
						Text("Daftar", fontSize = 16.sp, fontWeight = FontWeight.Bold)
					}
				}

				Row(
					modifier = Modifier.fillMaxWidth().padding(16.dp),
					horizontalArrangement = Arrangement.Center
				) {
					Text(
						text = "Sudah punya akun? ",
						fontSize = 14.sp
					)
					Text(
						modifier = Modifier.clickable(true, onClick = {
							navigator.pop()
						}),
						text = "Masuk",
						fontSize = 14.sp,
						fontWeight = FontWeight.Bold,
						color = Colors().textClickable
					)
				}
			}
		}
	}
}

fun checkPassValidity(pass: String): Boolean {
	val isUpper = pass.contains("[A-Z]".toRegex())
	val isNum = pass.contains("[0-9]".toRegex())
	val isSym = pass.contains("[!\"#\$%&'()*+,\\-./:;\\\\<=>?@\\[\\]^_`{|}~]".toRegex())
	val len = pass.length >= 8
	return isUpper && isNum && isSym && len
}