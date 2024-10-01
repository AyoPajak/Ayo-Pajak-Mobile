package security

expect class Crypto {
    fun MultiPlatformCrypt(key: String)
    fun Encrypt(text: String, key: String): String
    fun Decrypt(text: String, key: String): String
}