package br.com.stock.server.domain.wallet

enum class WalletCardBrand(
    private val regex: String? = null
) {

    UNKNOWN,
    MASTERCARD("^(?:5[1-5]|2(?!2([01]|20)|7(2[1-9]|3))[2-7])\\d{14}$"),
    AMERICAN_EXPRESS("^3[47][0-9]{13}$"),
    DINERS_CLUB("^3(?:0[0-5]\\d|095|6\\d{0,2}|[89]\\d{2})\\d{10}$"),
    ELO("^((((636368)|(438935)|(504175)|(451416)|(636297)(506699)|(636369))\\d{0,10})|((5067)|(4576)|(4011))\\d{0,12})$"),
    DISCOVER("^6(?:011|[45][0-9]{2})[0-9]{12}$"),
    VISA("^4[0-9]{12}(?:[0-9]{3}){0,2}$");

    companion object {

        fun fromNumber(number: String): WalletCardBrand {
            val parsedNumber = number.replace(" ", "")
            return values().firstOrNull {
                it.regex != null && parsedNumber.matches(it.regex.toRegex())
            } ?: UNKNOWN
        }

//        private val gatewayBrandMap = mapOf(
//            PaymentGateway. to mapOf(
//                WalletType.CREDIT_CARD to mapOf(
//                    VISA to "Visa",
//                    MASTERCARD to "Master",
//                    AMERICAN_EXPRESS to "Amex",
//                    ELO to "Elo",
//                    DINERS_CLUB to "Diners",
//                    DISCOVER to "Discover"
//                ),
//                WalletType.DEBIT_CARD to mapOf(
//                    VISA to "Visa",
//                    MASTERCARD to "Master",
//                    AMERICAN_EXPRESS to "Amex",
//                    ELO to "Elo",
//                    DINERS_CLUB to "Diners",
//                    DISCOVER to "Discover"
//                )
//            )
//        )

    }

//    fun toGateway(gateway: PaymentGateway, walletType: WalletType): String {
//        return gatewayBrandMap.get(gateway)?.get(walletType)?.get(this) ?: this.name
//    }

}