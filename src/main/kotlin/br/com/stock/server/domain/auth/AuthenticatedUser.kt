package br.com.stock.server.domain.auth

import java.time.OffsetDateTime

data class AuthenticatedUser(
    val authorization: String,
    val refreshAuthorization: String,
    val authorities: List<String>,
    val expiresAt: OffsetDateTime,
    val refreshAt: OffsetDateTime,
)