package br.com.stock.server.controller.partner.employee.dto

import br.com.stock.server.domain.entity.partner.employee.Employee
import br.com.stock.server.extentions.emailMask

data class EmployeePasswordConfirmationDataResponseDto(
    val externalId: String,
    val data: String,
) {
    companion object {
        fun from(employee: Employee) = EmployeePasswordConfirmationDataResponseDto(
            externalId = employee.externalId,
            data = employee.email.emailMask(),
        )
    }
}
