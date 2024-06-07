package br.com.stock.server.controller.partner.employee.login

import br.com.stock.server.controller.partner.employee.login.dto.EmployeeLoginRequestDto
import br.com.stock.server.controller.partner.employee.login.dto.EmployeeLoginResponseDto
import br.com.stock.server.controller.partner.employee.login.dto.EmployeeRefreshResponseDto
import br.com.stock.server.lib.observability.Observable
import br.com.stock.server.lib.security.UserAuthentication
import br.com.stock.server.service.image.FileService
import br.com.stock.server.service.partner.employee.EmployeeService
import io.micrometer.core.instrument.MeterRegistry
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/employees/auth"])
class EmployeeLoginController(
    meter: MeterRegistry,
    private val employeeService: EmployeeService,
    private val fileService: FileService,
) : Observable(meter) {

    @Valid
    @PostMapping(path = ["/login"])
    fun login(
        @RequestBody credential: EmployeeLoginRequestDto
    ): ResponseEntity<EmployeeLoginResponseDto> = loggable(journey("employee_login")) { logger ->
        val (employee, authorizationUser) = employeeService.authenticate(credential)
        val loginResponse = EmployeeLoginResponseDto.from(employee, authorizationUser, fileService)
        ResponseEntity.ok(loginResponse).also {
            logger.info { "Success employee login [username=${credential.username}]" }
        }
    }

    @GetMapping(path = ["/refresh"])
    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE_REFRESH')")
    fun refresh(
        userAuthentication: UserAuthentication
    ): ResponseEntity<EmployeeRefreshResponseDto> = loggable(journey("refresh")) { logger ->
        val authorizationUser = employeeService.refresh(userAuthentication)
        ResponseEntity.ok(
            EmployeeRefreshResponseDto(
                authorizationUser.authorization,
                authorizationUser.refreshAuthorization,
                authorizationUser.expiresAt,
                authorizationUser.refreshAt,
            )
        ).also {
            logger.info { "Success employee refreshed [username=${userAuthentication.username}, restaurantId=${userAuthentication.restaurantId}]" }
        }
    }

}