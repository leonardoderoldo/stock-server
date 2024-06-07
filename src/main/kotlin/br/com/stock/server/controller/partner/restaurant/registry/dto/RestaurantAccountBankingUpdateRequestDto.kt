package br.com.stock.server.controller.partner.restaurant.registry.dto

import org.hibernate.validator.constraints.Length

data class RestaurantAccountBankingUpdateRequestDto(
    @field:Length(min = 3, max = 3, message = "O código do banco deve ter 4 caracteres")
    val bankCode: String,
    @field:Length(min = 4, max = 4, message = "A agência deve ter 4 caracteres")
    val agency: String,
    @field:Length(max = 20, message = "O número da conta deve ter no máximo 20 caracteres")
    val accountNumber: String,
    @field:Length(max = 2, message = "O digito verificador deve ter no máximo 2 caracteres")
    val verifyingDigit: String
)