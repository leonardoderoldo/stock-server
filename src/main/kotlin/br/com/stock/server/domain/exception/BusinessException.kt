package br.com.stock.server.domain.exception

import org.springframework.http.HttpStatus

class BusinessException(
    override val message: String,
): ApiException(message, "Não encontrado", HttpStatus.CONFLICT)