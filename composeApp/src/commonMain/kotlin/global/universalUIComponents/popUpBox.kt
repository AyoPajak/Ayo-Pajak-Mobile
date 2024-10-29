package global.universalUIComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.zIndex

@Composable
fun popUpBox(
	popupWidth: Float,
	popupHeight:Float,
	showPopup: Boolean,
	onClickOutside: () -> Unit,
	content: @Composable() () -> Unit) {
	
	if (showPopup) {
		// full screen background
		Box(
			modifier = Modifier
				.fillMaxSize()
				.background(Color.Black.copy(alpha = 0.5f))
				.zIndex(10F),
			contentAlignment = Alignment.Center
		) {
			// popup
			Popup(
				alignment = Alignment.Center,
				properties = PopupProperties(
					focusable = true,
					dismissOnBackPress = true,
					dismissOnClickOutside = true
				),
				onDismissRequest = { onClickOutside() }
			) {
				Box(
					Modifier
						.fillMaxWidth()
						.padding(horizontal = 16.dp)
						.clip(RoundedCornerShape(8.dp))
						.background(Color.White),
					contentAlignment = Alignment.Center
				) {
					content()
				}
			}
		}
	}
}