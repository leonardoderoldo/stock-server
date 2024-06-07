package br.com.stock.server.controller.partner.employee.dto

import org.hibernate.validator.constraints.Length
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

data class EmployeeUpdateRequestDto(
    @field:Length(min = 3, max = 200, message = "O nome deve ter de 3 a 200 caracteres")
    val firstName: String,
    @field:Length(min = 3, max = 200, message = "O sobrenome pode ser composto e deve ter de 3 a 200 caracteres")
    val lastName: String,
    @field:DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    val birthDate: LocalDate,
)