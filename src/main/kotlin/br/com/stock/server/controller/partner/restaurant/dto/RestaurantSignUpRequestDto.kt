package br.com.stock.server.controller.partner.restaurant.dto

import br.com.stock.server.controller.dto.ImageDto
import br.com.stock.server.controller.partner.employee.dto.EmployeeSignUpRequestDto
import jakarta.validation.Valid
import org.hibernate.validator.constraints.Length

data class RestaurantSignUpRequestDto(
    val businessName: String,
    @field:Length(min = 3, max = 1000, message = "A razão social da escola deve ter de 3 a 1000 caracteres")
    val companyName: String,
    val cnpj: String,
    @field:Valid
    val phone: RestaurantPhoneRequestDto,
    @field:Valid
    val image: ImageDto?,
    @field:Valid
    val employee: EmployeeSignUpRequestDto
)
