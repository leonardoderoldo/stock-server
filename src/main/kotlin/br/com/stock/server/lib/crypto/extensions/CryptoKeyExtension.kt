package br.com.stock.server.lib.crypto.extensions

import java.security.PrivateKey
import java.security.PublicKey
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey

fun PublicKey.asRsa() = this as RSAPublicKey
fun PrivateKey.asRsa() = this as RSAPrivateKey