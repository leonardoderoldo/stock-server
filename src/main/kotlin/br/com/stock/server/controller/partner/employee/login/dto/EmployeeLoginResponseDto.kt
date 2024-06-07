package br.com.stock.server.controller.partner.employee.login.dto

import br.com.stock.server.domain.auth.AuthenticatedUser
import br.com.stock.server.domain.entity.partner.employee.Employee
import br.com.stock.server.domain.entity.partner.restaurant.Restaurant
import br.com.stock.server.service.image.FileService
import java.time.OffsetDateTime

data class EmployeeLoginResponseDto(
    val employee: EmployeeDataResponseDto,
    val restaurant: RestaurantDataResponseDto?,
    val authorization: String,
    val refreshAuthorization: String,
    val expiresAt: OffsetDateTime,
    val refreshAt: OffsetDateTime,
) {
    companion object {
        fun from(
            employee: Employee,
            authorizationUser: AuthenticatedUser,
            fileService: FileService
        ) = EmployeeLoginResponseDto(
            employee = EmployeeDataResponseDto.from(employee, fileService),
            restaurant = RestaurantDataResponseDto.from(employee.restaurant, fileService),
            authorization = authorizationUser.authorization,
            refreshAuthorization = authorizationUser.refreshAuthorization,
            expiresAt = authorizationUser.expiresAt,
            refreshAt = authorizationUser.refreshAt,
        )
    }
}

data class EmployeeDataResponseDto(
    val externalId: String,
    val firstName: String,
    val lastName: String,
    val imageUrl: String?,
    val jobFunction: String?,
) {

    companion object {
        fun from(employee: Employee, fileService: FileService) = EmployeeDataResponseDto(
            externalId = employee.externalId,
            firstName = employee.firstName,
            lastName = employee.lastName,
            imageUrl = fileService.getAddressToFileUrl(employee.imagePath),
            jobFunction = employee.jobFunction?.name,
        )
    }
}

data class RestaurantDataResponseDto(
    val externalId: String,
    val companyName: String,
    val imageUrl: String?,
) {
    companion object {
        fun from(restaurant: Restaurant, fileService: FileService) = RestaurantDataResponseDto(
            externalId = restaurant.externalId,
            companyName = restaurant.companyName,
            imageUrl = fileService.getAddressToFileUrl(restaurant.imagePath),
        )
    }
}