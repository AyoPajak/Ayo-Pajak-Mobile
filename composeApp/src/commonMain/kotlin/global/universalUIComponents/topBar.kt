package global.universalUIComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ayopajakmobile.composeapp.generated.resources.Res
import ayopajakmobile.composeapp.generated.resources.arrow_back
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.jetbrains.compose.resources.vectorResource
import global.Colors

@Composable
fun topBar(title: String, textColor: Color = Color.Black, bgColor: Color = Colors().panel, buttonColor: Color = Colors().panel) {
    val navigator = LocalNavigator.currentOrThrow

    Row(
    modifier = Modifier.fillMaxWidth().background(bgColor)
    ) {
        IconButton(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(vertical = 24.dp)
                .clip(CircleShape)
                .border(1.dp, Color.LightGray, CircleShape)
                .background(buttonColor)
                .align(Alignment.CenterVertically),
            onClick = {
                navigator.pop()
            }
        ) {
            Icon(
                modifier = Modifier.scale(1.2f),
                imageVector = vectorResource(Res.drawable.arrow_back), contentDescription = null
            )
        }

        if (title.isNotEmpty()){
            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = textColor
            )
        }
    }
}