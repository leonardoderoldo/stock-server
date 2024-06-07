package br.com.stock.server.domain.exception

import org.springframework.http.HttpStatus

class ApiNotFoundException(
    override val message: String,
): ApiException(message, "Não encontrado", HttpStatus.NOT_FOUND)