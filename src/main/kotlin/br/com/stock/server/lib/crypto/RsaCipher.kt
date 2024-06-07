package br.com.stock.server.lib.crypto

import br.com.stock.server.lib.crypto.extensions.decodeBase64
import br.com.stock.server.lib.crypto.extensions.encodeBase64
import br.com.stock.server.lib.crypto.extensions.toPrivateKey
import br.com.stock.server.lib.crypto.extensions.toPublicKey
import java.security.Key
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.MGF1ParameterSpec
import javax.crypto.Cipher
import javax.crypto.spec.OAEPParameterSpec
import javax.crypto.spec.PSource

object RsaCipher {

    fun encrypt(plaintext: String, publicKey: String): String = encrypt(plaintext, publicKey.toPublicKey())
    fun encrypt(plaintext: String, publicKey: PublicKey): String = cipher(publicKey).doFinal(plaintext.toByteArray()).encodeBase64()

    fun decrypt(cipher: String, privateKey: String): String = decrypt(cipher, privateKey.toPrivateKey())
    fun decrypt(cipher: String, privateKey: PrivateKey): String = cipher.decodeBase64().let { cipher(privateKey).doFinal(it) }.let { String(it) }

    fun generateKeyPair(): KeyPair = KeyPairGenerator.getInstance("RSA").generateKeyPair()

    private fun cipher(key: Key) = OAEPParameterSpec(
        "SHA-512",
        "MGF1",
        MGF1ParameterSpec("SHA-512"),
        PSource.PSpecified.DEFAULT
    ).let {
        Cipher.getInstance("RSA/ECB/OAEPWithSHA-512AndMGF1Padding").apply { init(encryptMode(key), key, it) }
    }

    private fun encryptMode(key: Key) = if(key is PublicKey) {
        Cipher.ENCRYPT_MODE
    } else {
        Cipher.DECRYPT_MODE
    }

}