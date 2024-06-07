package br.com.stock.server.controller.partner.employee.dto

import jakarta.validation.constraints.Email

data class EmployeeActivateRequestDto(
    @field:Email(message = "E-mail inválido")
    val email: String,
    val code: String,
    val password: String
)