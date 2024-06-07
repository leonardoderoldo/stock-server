package br.com.stock.server.controller.partner.employee.dto

import br.com.stock.server.domain.entity.partner.employee.Employee
import br.com.stock.server.service.image.FileService
import java.time.LocalDate
import java.time.OffsetDateTime

data class EmployeeResponseDto(
    val externalId: String,
    val cpf: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val birthDate: LocalDate,
    val updatedAt: OffsetDateTime,
    val imageUrl: String?,
) {
    companion object {
        fun from(employee: Employee, fileService: FileService) = EmployeeResponseDto(
            externalId = employee.externalId,
            cpf = employee.cpf,
            email = employee.email,
            firstName = employee.firstName,
            lastName = employee.lastName,
            birthDate = employee.birthDate,
            updatedAt = employee.updatedAt,
            imageUrl = fileService.getAddressToFileUrl(employee.imagePath)
        )
    }
}