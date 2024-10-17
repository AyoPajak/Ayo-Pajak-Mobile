package tabs

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import ayopajakmobile.composeapp.generated.resources.*
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

object AccountTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(Res.string.account_tab_title)
            val icon = painterResource(Res.drawable.icon_account_selected)

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
        Text("AyoPajak Mobile: Account Screen")
    }
}