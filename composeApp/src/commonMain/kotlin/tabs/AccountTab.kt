package tabs

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import ayopajakmobile.composeapp.generated.resources.*
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import http.Account
import http.Interfaces
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import screens.AccountScreen
import security.Crypto

class AccountTab(
	val client: Account, val cryptoManager: Crypto,
	val prefs: DataStore<Preferences>, val sptPertamaClient: Interfaces
) : Tab {
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
		Navigator(AccountScreen(client, cryptoManager, prefs, sptPertamaClient))
	}
}