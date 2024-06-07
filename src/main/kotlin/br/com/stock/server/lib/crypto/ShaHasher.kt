package br.com.stock.server.lib.crypto

import br.com.stock.server.lib.crypto.extensions.decodeBase64
import br.com.stock.server.lib.crypto.extensions.encodeBase64
import java.security.MessageDigest
import java.security.PrivateKey
import java.security.PublicKey
import java.security.SecureRandom

object ShaHasher {

    private const val HEX_CHARS = "0123456789abcdef"

    fun verify(plaintext: String, hashedData: HashedData, privateKey: PrivateKey): Boolean {
        val salt = RsaCipher.decrypt(hashedData.iv, privateKey).decodeBase64()
        val hash = hash(plaintext, salt)
        return hash == hashedData.hash
    }

    fun hash(plaintext: String, publicKey: PublicKey): HashedData {
        val randomSalt = randomSalt()
        val encryptedSalt = RsaCipher.encrypt(randomSalt.encodeBase64(), publicKey)
        val hash = hash(plaintext, randomSalt)
        return HashedData(hash = hash, iv = encryptedSalt)
    }

    private fun hash(plaintext: String, salt: ByteArray): String {
        val messageDigest = MessageDigest.getInstance("SHA-512").apply { update(salt) }
        val encryptedBytes = messageDigest.digest(plaintext.toByteArray())
        val result = StringBuilder(encryptedBytes.size * 2)
        encryptedBytes.forEach {
            val i = it.toInt()
            result.append(HEX_CHARS[i shr 4 and 0x0f])
            result.append(HEX_CHARS[i and 0x0f])
        }
        return result.toString()
    }

    private fun randomSalt(): ByteArray = SecureRandom.getSeed(16)
}

class HashedData(
    val hash: String,
    val iv: String,
)