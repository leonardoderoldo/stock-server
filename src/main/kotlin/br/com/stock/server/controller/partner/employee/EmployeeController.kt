package br.com.stock.server.controller.partner.employee

import br.com.stock.server.controller.partner.employee.dto.EmployeeActivateRequestDto
import br.com.stock.server.controller.partner.employee.dto.EmployeeResponseDto
import br.com.stock.server.controller.partner.employee.dto.EmployeeSignUpRequestDto
import br.com.stock.server.lib.observability.Observable
import br.com.stock.server.lib.security.UserAuthentication
import br.com.stock.server.service.image.FileService
import br.com.stock.server.service.partner.employee.EmployeeService
import io.micrometer.core.instrument.MeterRegistry
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/employees"])
class EmployeeController(
    meter: MeterRegistry,
    private val fileService: FileService,
    private val employeeService: EmployeeService,
): Observable(meter) {


    @PostMapping(path = ["/signup"])
    fun signup(
        userAuthentication: UserAuthentication,
        @Valid @RequestBody dto: EmployeeSignUpRequestDto,
    ): ResponseEntity<EmployeeResponseDto> = loggable(journey("signup")) { logger ->
        employeeService.signup(dto, userAuthentication.restaurantId!!)
            .let { EmployeeResponseDto.from(it, fileService) }
            .also { logger.info { "Employee with cpf=${dto.cpf} created [externalId=${it.externalId}]" } }
            .let { ResponseEntity.status(HttpStatus.CREATED).body(it) }
    }

    @PutMapping(path = ["/activate"])
    fun activate(
        @Valid @RequestBody dto: EmployeeActivateRequestDto
    ): ResponseEntity<EmployeeResponseDto> = loggable(journey("activate")) { logger ->
        employeeService.activate(dto)
            .let { EmployeeResponseDto.from(it, fileService) }
            .also { logger.info { "Employee with externalId=${it.externalId} activated" } }
            .let { ResponseEntity.ok(it) }
    }


}