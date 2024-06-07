package br.com.stock.server.lib.crypto.extensions

import java.util.Base64

fun ByteArray.encodeBase64() = String(Base64.getEncoder().encode(this))