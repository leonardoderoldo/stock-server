package br.com.stock.server.domain.wallet

const val GETNET_GATEWAY_NAME = "GETNET"
const val UNKNOW_GATEWAY_NAME = "UNKNOW"

enum class PaymentGateway(
    val gatewayName: String
) {

    UNKNOW(UNKNOW_GATEWAY_NAME),
    GETNET(GETNET_GATEWAY_NAME)

}