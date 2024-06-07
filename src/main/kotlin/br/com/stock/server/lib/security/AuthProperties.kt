package br.com.stock.server.lib.security

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix = "app.auth")
data class AuthProperties @ConstructorBinding constructor(
    val serviceSharedSecret: String?,
    val fixToolsSharedSecret: String?,
    val jwtPublicKey: String?,
    val backofficePublicKey: String?,
    val ignorePaths: List<String> = emptyList()
)