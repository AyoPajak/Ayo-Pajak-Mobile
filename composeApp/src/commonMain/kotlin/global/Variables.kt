package global

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue


class Variables {
    companion object {
        const val FirstRun = "FirstRun"
        const val EncryptedPreferenceFileName = "encrypted_preferences"

        const val APIUrl = "APIUrl"
        const val Version = "Version"

        const val RijndaelKey = "39843287511548451"

        const val AyoPajakWebBaseUrl = "https://apps.ayopajak.com/"
        const val AyoPajakApiBaseUrl = "https://api.ayopajak.com/"
        const val AyoPajakApiSandboxUrl = "https://api.sandbox.ayopajak.com/"
        const val PertamaApiBaseUrl = "https://apipertama.ayopajak.com/"
        const val LetTaxApiBaseUrl = "https://chat.ayopajak.com/"

        const val TPApiUserKey = "a51jd1dqcilpz5ka"
        const val TPApiUserSecret = "6AsIUDpgIl5Vry7J9lob4sbyR98ILhx2"

        const val DefaultTextViewColor = -1979711488
        const val CurrencyCodeIDR = "IDR"
    }
}