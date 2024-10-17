package tabs

import screens.HomeScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import ayopajakmobile.composeapp.generated.resources.*
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

object HomeTab : Tab {

    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(Res.string.home_tab_title)
            val icon = painterResource(Res.drawable.icon_home_selected)

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
        Navigator(HomeScreen())
    }
}