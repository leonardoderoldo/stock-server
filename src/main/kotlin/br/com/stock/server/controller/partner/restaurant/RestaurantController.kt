package br.com.stock.server.controller.partner.restaurant

import br.com.stock.server.controller.partner.restaurant.dto.RestaurantResponseDto
import br.com.stock.server.controller.partner.restaurant.dto.RestaurantSignUpRequestDto
import br.com.stock.server.lib.observability.Observable
import br.com.stock.server.lib.security.UserAuthentication
import br.com.stock.server.service.image.FileService
import br.com.stock.server.service.partner.restaurant.RestaurantService
import io.micrometer.core.instrument.MeterRegistry
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/partner/restaurant"])
class RestaurantController(
    meter: MeterRegistry,
    private val restaurantService: RestaurantService,
    private val fileService: FileService,
) : Observable(meter) {

    @PostMapping(path = ["/signup"])
    fun signup(
        @Valid @RequestBody dto: RestaurantSignUpRequestDto
    ): ResponseEntity<RestaurantResponseDto> = loggable(journey("signup")) { logger ->
        val restaurant = restaurantService.signup(dto)
        ResponseEntity.status(HttpStatus.CREATED).body(RestaurantResponseDto.from(restaurant, fileService)).also {
            logger.info { "Restaurant with cnpj ${restaurant.cnpj} craeted: external-id=${restaurant.externalId}" }
        }
    }

    @PutMapping(path = ["/terms/{termVersion}"])
    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE_JOB_FUNCTION_OWNER') or hasAuthority('ROLE_EMPLOYEE_RELATION_OWNER')")
    fun updateTerms(
        userAuthentication: UserAuthentication,
        @PathVariable("termVersion") termVersion: String,
    ): ResponseEntity<RestaurantResponseDto> = loggable(journey("updateTerms")) { logger ->
        val school = restaurantService.updateTerms(userAuthentication, termVersion)
        ResponseEntity.ok(RestaurantResponseDto.from(school, fileService)).also {
            logger.info { "Restaurant with externalId=${school.externalId} term version updated to $termVersion" }
        }
    }

}