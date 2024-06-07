package br.com.stock.server.controller.partner.employee.dto

data class EmployeePasswordChangeRequestDto(
    val externalId: String,
    val code: String,
    val password: String,
)
