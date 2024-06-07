package br.com.stock.server.controller.partner.restaurant.registry

import br.com.stock.server.controller.partner.restaurant.registry.dto.RestaurantAddressResponseDto
import br.com.stock.server.controller.partner.restaurant.registry.dto.RestaurantAddressUpdateRequestDto
import br.com.stock.server.lib.observability.Observable
import br.com.stock.server.lib.security.UserAuthentication
import br.com.stock.server.service.partner.restaurant.RestaurantService
import io.micrometer.core.instrument.MeterRegistry
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/partner/restaurant/registries/address"])
class RestaurantRegistryAddressController(
    meter: MeterRegistry,
    private val restaurantService: RestaurantService
) : Observable(meter) {

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE_ON_RESTAURANT')")
    fun getAddress(
        userAuthentication: UserAuthentication
    ): ResponseEntity<RestaurantAddressResponseDto?> = loggable(journey("getAddress")) { logger ->
        restaurantService.getAddress(userAuthentication.restaurantId!!)
            ?.let { address ->
                ResponseEntity.ok(RestaurantAddressResponseDto.from(address))
                    .also { logger.info { "Restaurant address getted with success [restaurantId=${address.restaurant.externalId}]" } }
            }
            ?: ResponseEntity.noContent().build<RestaurantAddressResponseDto?>().also {
                logger.info { "Restaurant address getted empty address [restaurantId=${userAuthentication.restaurantId}]" }
            }
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE_JOB_FUNCTION_OWNER') or hasAuthority('ROLE_EMPLOYEE_RELATION_OWNER')")
    fun updateAddress(
        userAuthentication: UserAuthentication,
        @Valid @RequestBody dto: RestaurantAddressUpdateRequestDto
    ): ResponseEntity<RestaurantAddressResponseDto> = loggable(journey("updateAddress")) { logger ->
        val address = restaurantService.updateAddress(userAuthentication.restaurantId!!, dto)
        ResponseEntity.ok(RestaurantAddressResponseDto.from(address)).also {
            logger.info { "Restaurant address updated with success [restaurantId=${address.restaurant.externalId}]" }
        }
    }

}