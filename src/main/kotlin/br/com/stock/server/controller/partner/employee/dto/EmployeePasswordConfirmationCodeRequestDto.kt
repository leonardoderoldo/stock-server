package br.com.stock.server.controller.partner.employee.dto

data class EmployeePasswordConfirmationCodeRequestDto(
    val externalId: String,
    val confirmationData: String,
)
