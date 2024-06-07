package br.com.stock.server.controller.partner.restaurant.registry.dto

import org.hibernate.validator.constraints.Length

data class RestaurantAddressUpdateRequestDto(
    @field:Length(min = 3, max = 100, message = "Informe um nome de 3 a 100 caracteres para identificar o endereço")
    val name: String,
    @field:Length(min = 3, max = 1000, message = "O nome da rua deve ter de 3 a 1000 caracteres")
    val street: String,
    @field:Length(max = 10, message = "O número deve ter no máximo 10 caracteres")
    val number: String?,
    @field:Length(max = 10, message = "O complemento deve ter no máximo 10 caracteres")
    val complement: String?,
    @field:Length(min = 8, max = 8, message = "O CEP deve conter 8 caracteres")
    val cep: String,
    @field:Length(min = 3, max = 200, message = "O bairro deve ter de 3 a 200 caracteres")
    val neiborhood: String,
    @field:Length(min = 2, max = 2, message = "A sigla do estado deve ter 2 caracteres")
    val state: String,
    @field:Length(min = 3, max = 100, message = "A cidade deve ter de 3 a 100 caracteres")
    val city: String,
)
