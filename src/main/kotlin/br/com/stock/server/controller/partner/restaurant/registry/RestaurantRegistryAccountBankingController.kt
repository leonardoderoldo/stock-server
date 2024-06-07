package br.com.stock.server.controller.partner.restaurant.registry

import br.com.stock.server.controller.partner.restaurant.registry.dto.RestaurantAccountBankingResponseDto
import br.com.stock.server.controller.partner.restaurant.registry.dto.RestaurantAccountBankingUpdateRequestDto
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
@RequestMapping(path = ["/partner/restaurant/registries/account-banking"])
class RestaurantRegistryAccountBankingController(
    meter: MeterRegistry,
    private val restaurantService: RestaurantService
) : Observable(meter) {

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE_ON_RESTAURANT')")
    fun getAccountBanking(
        userAuthentication: UserAuthentication
    ): ResponseEntity<RestaurantAccountBankingResponseDto?> = loggable(journey("getAccountBanking")) { logger ->
        restaurantService.getAccountBanking(userAuthentication.restaurantId!!)
            ?.let { address ->
                ResponseEntity.ok(RestaurantAccountBankingResponseDto.from(address))
                    .also { logger.info { "Restaurant account banking getted with success [restaurantId=${address.restaurant.externalId}]" } }
            }
            ?: ResponseEntity.noContent().build<RestaurantAccountBankingResponseDto?>().also {
                logger.info { "Restaurant account banking getted empty account banking [restaurantId=${userAuthentication.restaurantId}]" }
            }
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ROLE_EMPLOYEE_JOB_FUNCTION_OWNER') or hasAuthority('ROLE_EMPLOYEE_RELATION_OWNER')")
    fun updateAccountBanking(
        userAuthentication: UserAuthentication,
        @Valid @RequestBody dto: RestaurantAccountBankingUpdateRequestDto
    ): ResponseEntity<RestaurantAccountBankingResponseDto> = loggable(journey("updateAccountBanking")) { logger ->
        val account = restaurantService.updateAccountBanking(userAuthentication.restaurantId!!, dto)
        ResponseEntity.ok(RestaurantAccountBankingResponseDto.from(account)).also {
            logger.info { "Restaurant account banking updated with success [restaurantId=${account.restaurant.externalId}]" }
        }
    }


}