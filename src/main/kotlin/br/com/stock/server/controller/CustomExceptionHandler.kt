package br.com.stock.server.controller

import br.com.stock.server.domain.exception.ApiException
import br.com.stock.server.domain.exception.ResponseDataException
import br.com.stock.server.lib.observability.Observable
import com.fasterxml.jackson.annotation.JsonIgnore
import io.micrometer.core.instrument.MeterRegistry
import jakarta.servlet.ServletException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.access.AccessDeniedException
import org.springframework.validation.FieldError
import org.springframework.web.ErrorResponse
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class CustomExceptionHandler(
    meter: MeterRegistry,
): Observable(meter) {

    @ExceptionHandler(ApiException::class)
    fun genericApiHandler(e: ApiException): ResponseEntity<ErrorResponseDto> = observe("genericApiHandler") { logger ->
        if (e.statusCode.is5xxServerError) {
            logger.error(e) { "Mapped exception [message=${e.message}]" }
        } else {
            logger.info { e.message }
        }
        ResponseEntity.status(e.statusCode).body(ErrorResponseDto(e.responseMessage))
    }

    @ExceptionHandler(ResponseDataException::class)
    fun responseDataHandler(e: ResponseDataException): ResponseEntity<ErrorResponseDto> = observe("responseDataHandler") { logger ->
        logger.info { "${e.message} [data=${e.responseData}]" }
        ResponseEntity.status(e.statusCode).body(ErrorResponseDto(e.responseMessage, e.responseData))
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun dataIntegrityViolationException(e: DataIntegrityViolationException): ResponseEntity<ErrorResponseDto> = observe("dataIntegrityViolationException") { logger ->
        logger.warn { "Mapped exception [message=${e.message}]" }
        ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponseDto("Violação de dados"))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun invalidMethodData(e: MethodArgumentNotValidException): ResponseEntity<ErrorResponseDto> = observe("invalidMethodData") { logger ->
        val data = e.bindingResult.fieldErrors.map { ErrorValidationData(it) }
        logger.info { "Invalid JSON [objectName=${e.bindingResult.objectName}, details=$data]" }
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponseDto("JSON inválido", data))
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun httpMessageNotReadableException(e: HttpMessageNotReadableException): ResponseEntity<ErrorResponseDto> = observe("httpMessageNotReadableException") { logger ->
        logger.info { "Invalid JSON [message=${e.message}]" }
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponseDto("JSON inválido"))
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun accessDeniedException(e: AccessDeniedException): ResponseEntity<ErrorResponseDto> = observe("accessDeniedException") { logger ->
        logger.warn { "User without permission to resource" }
        ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorResponseDto("Sem permissão"))
    }


    @ExceptionHandler(ServletException::class)
    fun servletException(e: ServletException): ResponseEntity<ErrorResponseDto> = observe("servletException") { logger ->
        if (e is ErrorResponse) {
            logger.warn { "Unmapped servlet error exception [message=${e.message}]" }
            ResponseEntity.status(e.statusCode).body(ErrorResponseDto(e.message ?: "Erro inesperado"))
        } else {
            logger.error(e) { "Unmapped servlet exception [message=${e.message}]" }
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResponseDto("Erro inesperado"))
        }
    }

    @ExceptionHandler(Exception::class)
    fun genericHandler(e: Exception): ResponseEntity<ErrorResponseDto> = observe("genericHandler") { logger ->
        logger.error(e) { "Unmapped exception [message=${e.message}]" }
        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResponseDto("Erro inesperado"))
    }
}

data class ErrorResponseDto(
    val message: String,
    val data: Any? = null
)

data class ErrorValidationData(
    val field: String,
    val message: String,
    @JsonIgnore
    val rejectedData: String,
) {
    constructor(fieldError: FieldError): this(
        field = fieldError.field,
        message = fieldError.defaultMessage ?: "Inválido",
        rejectedData = fieldError.rejectedValue.toString(),
    )
}