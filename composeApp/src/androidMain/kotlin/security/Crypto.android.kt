package security

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

actual class Crypto {

    val ALGORITHM: String = "AES"
    lateinit var _cipher: Cipher
    lateinit var _secretKey: SecretKey
    lateinit var _IVParamSpec: IvParameterSpec

    actual fun MultiPlatformCrypt(key: String) {
        try {
            val subsKey = key.substring(0, 16)

            _secretKey = SecretKeySpec(subsKey.toByteArray(), ALGORITHM)

            val TRANSFORMATION = "AES/CBC/PKCS5Padding"
            _cipher = Cipher.getInstance(TRANSFORMATION)
            _IVParamSpec = IvParameterSpec(subsKey.toByteArray())
        } catch (e: Exception) {
            println(e.message)
        }
    }

    actual fun Encrypt(text: String, key: String): String {
        lateinit var encryptedData: ByteArray

        try {
            MultiPlatformCrypt(key)

            _cipher.init(Cipher.ENCRYPT_MODE, _secretKey, _IVParamSpec)
            
            encryptedData = _cipher.doFinal(text.toByteArray())
        } catch (e: Exception) {
            println(e.message)
        }

        return Base64.encodeToString(encryptedData, Base64.NO_WRAP)
    }

    actual fun Decrypt(text: String, key: String): String {
        try {
            MultiPlatformCrypt(key)

            _cipher.init(Cipher.DECRYPT_MODE, _secretKey, _IVParamSpec)

            var decodedValue = Base64.decode(text.toByteArray(), Base64.NO_WRAP)
            var decryptedVal = _cipher.doFinal(decodedValue)

            return String(decryptedVal)
        } catch (e: Exception) {
            println(e.message)
            return ""
        }
    }
}