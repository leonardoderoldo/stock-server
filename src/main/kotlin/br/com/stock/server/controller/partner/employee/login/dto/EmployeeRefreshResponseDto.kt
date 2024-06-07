package br.com.stock.server.controller.partner.employee.login.dto

import java.time.OffsetDateTime

data class EmployeeRefreshResponseDto(
    val authorization: String,
    val refreshAuthorization: String,
    val expiresAt: OffsetDateTime,
    val refreshAt: OffsetDateTime,
)