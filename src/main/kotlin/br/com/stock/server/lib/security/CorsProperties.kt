package br.com.stock.server.lib.security

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix = "app.web.cors")
data class CorsProperties @ConstructorBinding constructor(
    val defaultOrigin: String?,
    val allowedOrigins: List<String>?,
)
