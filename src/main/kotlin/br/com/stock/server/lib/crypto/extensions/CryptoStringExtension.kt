package br.com.stock.server.lib.crypto.extensions

import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.Base64

const val RSA_ALGORITHM = "RSA"

fun String.encodeBase64(): String = this.toByteArray().encodeBase64()

fun String.decodeBase64(): ByteArray = Base64.getDecoder().decode(this)

fun String.removeBreakLines() = this.replace("\n", "")

fun String.removeSpaces() = this.replace(" ", "")

fun String.removePublicKeyHeaderAndFooter() = this
    .replace("-----BEGIN PUBLIC KEY-----", "")
    .replace("-----END PUBLIC KEY-----", "")

fun String.removePrivateKeyHeaderAndFooter() = this
    .replace("-----BEGIN PRIVATE KEY-----", "")
    .replace("-----END PRIVATE KEY-----", "")

fun String.toPublicKey(): PublicKey = this
    .removePublicKeyHeaderAndFooter()
    .removeBreakLines()
    .removeSpaces()
    .decodeBase64()
    .let { X509EncodedKeySpec(it) }
    .let { KeyFactory.getInstance(RSA_ALGORITHM).generatePublic(it) }

fun String.toPrivateKey(): PrivateKey = this
    .removePrivateKeyHeaderAndFooter()
    .removeBreakLines()
    .removeSpaces()
    .decodeBase64()
    .let { PKCS8EncodedKeySpec(it) }
    .let { KeyFactory.getInstance(RSA_ALGORITHM).generatePrivate(it) }
