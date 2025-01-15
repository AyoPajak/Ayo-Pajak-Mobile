package screens.SPT

import androidx.compose.runtime.Composable
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import cafe.adriel.voyager.core.screen.Screen
import http.Account
import http.Interfaces
import models.transaction.Form1770HdResponseApiModel

class SptStepSixScreen(val sptHd: Form1770HdResponseApiModel?, val client: Account, val sptPertamaClient: Interfaces, val prefs: DataStore<Preferences>): Screen {
	
	@Composable
	override fun Content() {
		TODO("Not yet implemented")
	}
}