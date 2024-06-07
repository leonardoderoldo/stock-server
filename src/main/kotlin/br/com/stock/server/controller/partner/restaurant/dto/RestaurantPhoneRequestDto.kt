package br.com.stock.server.controller.partner.restaurant.dto

import org.hibernate.validator.constraints.Length

data class RestaurantPhoneRequestDto(
    @field:Length(min = 2, max = 2, message = "O DDD deve ter 2 dígitos")
    val ddd: String,
    @field:Length(min = 8, max = 9, message = "O número deve ter 8 ou 9 dígitos")
    val number: String,
    val type: RestaurantPhoneType,
)

enum class RestaurantPhoneType {
    WHATSAPP,
    MOBILE,
    LANDLINE,
}
