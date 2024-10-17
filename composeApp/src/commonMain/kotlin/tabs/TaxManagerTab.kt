package tabs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import ayopajakmobile.composeapp.generated.resources.Res
import ayopajakmobile.composeapp.generated.resources.compose_multiplatform
import ayopajakmobile.composeapp.generated.resources.home_tab_title
import ayopajakmobile.composeapp.generated.resources.icon_taxmanager
import ayopajakmobile.composeapp.generated.resources.icon_taxmanager_resize
import ayopajakmobile.composeapp.generated.resources.icon_taxmanager_selected
import ayopajakmobile.composeapp.generated.resources.taxmanager_tab_title
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import screens.TaxManager

object TaxManagerTab : Tab {

	override val options: TabOptions
		@Composable
		get() {
			val title = stringResource(Res.string.taxmanager_tab_title)
			val icon = painterResource(Res.drawable.icon_taxmanager_selected)

			return remember {
				TabOptions(
					index = 0u,
					title = title,
					icon = icon
				)
			}
		}

	@Composable
	override fun Content() {
		Navigator(TaxManager())
	}
}