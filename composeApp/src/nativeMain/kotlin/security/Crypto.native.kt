package security

actual class Crypto {
    actual fun MultiPlatformCrypt(key: String) {
    }

    actual fun Encrypt(text: String, key: String): String {
        TODO("Not yet implemented")
    }

    actual fun Decrypt(text: String, key: String): String {
        TODO("Not yet implemented")
    }
}