package br.com.stock.server.domain.exception

import org.springframework.http.HttpStatus

class ResponseDataException(
    message: String,
    responseMessage: String,
    statusCode: HttpStatus,
    val responseData: Any
): ApiException(message, responseMessage, statusCode)