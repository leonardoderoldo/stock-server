package br.com.stock.server.controller.partner.employee.login.dto

data class EmployeeLoginRequestDto(
    val username: String,
    val password: String,
    val gcmToken: String? = null,
)
