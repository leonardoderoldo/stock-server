package br.com.stock.server.domain.wallet

enum class WalletType(
    val card: Boolean,
    val needTurnover: Boolean,
) {

    MONEY(false, false),
    AVAILABLE(false, true),
    CREDIT_CARD(true, true),
    DEBIT_CARD(true, true)

}