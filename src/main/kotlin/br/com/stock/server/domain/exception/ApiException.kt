package br.com.stock.server.domain.exception

import org.springframework.http.HttpStatus

open class ApiException(
    override val message: String,
    val responseMessage: String,
    val statusCode: HttpStatus,
    val exception: Throwable? = null
) : RuntimeException(message, exception)