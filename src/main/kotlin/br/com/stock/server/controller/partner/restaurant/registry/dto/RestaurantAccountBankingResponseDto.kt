package br.com.stock.server.controller.partner.restaurant.registry.dto

import br.com.stock.server.domain.entity.partner.restaurant.RestaurantAccountBanking

data class RestaurantAccountBankingResponseDto(
    val bankCode: String,
    val agency: String,
    val accountNumber: String,
    val verifyingDigit: String,
){

    companion object {

        fun from(account: RestaurantAccountBanking) = RestaurantAccountBankingResponseDto(
            bankCode = account.bankCode,
            agency = account.agency,
            accountNumber = account.accountNumber,
            verifyingDigit = account.verifyingDigit
        )
    }
}
