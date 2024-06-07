package br.com.stock.server.controller.partner.restaurant.registry.dto

import br.com.stock.server.domain.entity.partner.restaurant.RestaurantAddress
import java.time.OffsetDateTime

data class RestaurantAddressResponseDto(
    val name: String,
    val street: String,
    val number: String?,
    val complement: String?,
    val cep: String,
    val neiborhood: String,
    val state: String,
    val city: String,
    var updatedAt: OffsetDateTime
) {

    companion object {
        fun from(address: RestaurantAddress) = RestaurantAddressResponseDto(
            name = address.name,
            street = address.street,
            number = address.number,
            complement = address.complement,
            cep = address.cep,
            neiborhood = address.neiborhood,
            state = address.state,
            city = address.city,
            updatedAt = address.updatedAt,
        )
    }
}