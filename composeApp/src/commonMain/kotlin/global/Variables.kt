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

class PreferencesKey {
    companion object {
        const val Username = "Username"
        const val Password = "Password"
        const val WPNPWP = "WPNPWP"
        const val IsLoggedIn = "IsLoggedIn"
        
        const val Pin = "Pin"
        const val IsPinAlreadyRequested = "IsPinAlreadyRequested"
        const val IsUserProfileSetup = "IsUserProfileSetup"
        
        const val UserApiKey = "UserApiKey"
        const val UserApiSecret = "UserApiSecret"
        const val UserTaxPayerName = "UserTaxPayerName"
        
        const val TPApiToken = "TPApiToken"
        const val AyoPajakUserApiToken = "AyoPajakUserApiToken"
        const val PertamaUserApiToken = "PertamaUserApiToken"
        
        const val LastStep = "LastStep"
        const val KppList = "Master.KppList"
        const val CityList = "Master.CityList"
        const val JobList = "Master.JobList"
        const val KluList = "Master.KluList"
        const val FamilyRelList = "Master.FamilyRelList"
        const val CurrencyList = "Master.CurrencyList"
        const val CurrencyRateList = "Master.CurrencyRateList"
        const val IncomeTaxRateList = "Master.IncomeTaxRateList"
        const val InterestTypeList = "Master.InterestTypeList"
        const val WealthTypeList = "Master.WealthTypeList"
        const val DebtTypeList = "Master.DebtTypeList"
        
        const val IsBiometricOn = "IsBiometricOn"
        const val TaxTypeList = "TaxTypeList"
        const val DepositTypeList = "DepositTypeList"
    }
}