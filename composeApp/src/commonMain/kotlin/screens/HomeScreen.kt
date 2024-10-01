package screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ayopajakmobile.composeapp.generated.resources.Res
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import org.jetbrains.compose.resources.painterResource

class HomeScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current

        Box(
            modifier = Modifier.fillMaxWidth().height(512.dp).background(Color.Blue)
        )
        {

        }

        LazyColumn {
        }
    }
}