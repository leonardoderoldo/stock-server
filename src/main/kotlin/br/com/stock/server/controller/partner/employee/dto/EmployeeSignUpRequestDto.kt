package br.com.stock.server.controller.partner.employee.dto

import br.com.stock.server.controller.dto.ImageDto
import br.com.stock.server.controller.partner.dto.EmployeeJobFunctionDto
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import org.hibernate.validator.constraints.Length
import org.hibernate.validator.constraints.br.CPF
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

data class EmployeeSignUpRequestDto(
    @field:Length(min = 3, max = 200, message = "O nome deve ter de 3 a 200 caracteres")
    val firstName: String,
    @field:Length(min = 3, max = 200, message = "O sobrenome pode ser composto e deve ter de 3 a 200 caracteres")
    val lastName: String,
    @field:CPF(message = "CPF inválido")
    val cpf: String,
    @field:Email(message = "E-mail inválido")
    val email: String,
    @field:DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    val birthDate: LocalDate,
    @field:Valid
    val image: ImageDto?,
    @field:Valid
    val jobFunction: EmployeeJobFunctionDto,
)