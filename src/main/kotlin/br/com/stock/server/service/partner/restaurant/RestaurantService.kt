package br.com.stock.server.service.partner.restaurant

import br.com.stock.server.controller.partner.restaurant.dto.RestaurantSignUpRequestDto
import br.com.stock.server.controller.partner.restaurant.registry.dto.RestaurantAccountBankingUpdateRequestDto
import br.com.stock.server.controller.partner.restaurant.registry.dto.RestaurantAddressUpdateRequestDto
import br.com.stock.server.domain.entity.partner.restaurant.Restaurant
import br.com.stock.server.domain.entity.partner.restaurant.RestaurantAccountBanking
import br.com.stock.server.domain.entity.partner.restaurant.RestaurantAddress
import br.com.stock.server.domain.exception.ApiException
import br.com.stock.server.domain.image.FileCategory
import br.com.stock.server.extentions.toTermVersion
import br.com.stock.server.lib.observability.Observable
import br.com.stock.server.lib.security.UserAuthentication
import br.com.stock.server.repository.partner.restaurant.RestaurantAccountBankingRepository
import br.com.stock.server.repository.partner.restaurant.RestaurantAddressRepository
import br.com.stock.server.repository.partner.restaurant.RestaurantRepository
import br.com.stock.server.service.image.FileService
import br.com.stock.server.service.partner.employee.EmployeeService
import io.micrometer.core.instrument.MeterRegistry
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class RestaurantService(
    meter: MeterRegistry,
    private val fileService: FileService,
    private val employeeService: EmployeeService,
    private val restaurantRepository: RestaurantRepository,
    private val restaurantAddressRepository: RestaurantAddressRepository,
    private val restaurantAccountBankingRepository: RestaurantAccountBankingRepository,
    @Value("\${app.terms.restaurant.version}") private val termVersion: String
): Observable(meter) {

    private val currentTermVersion = termVersion.toTermVersion()


    fun getByExternalId(restaurantId: String): Restaurant = observe("getByExternalId") { _ ->
        restaurantRepository.getByExternalId(restaurantId)
    }

    fun getAddress(restaurantId: String): RestaurantAddress? = observe("getAddress") { _ ->
        restaurantAddressRepository.findByRestaurant(restaurantId)
    }

    fun getAccountBanking(restaurantId: String): RestaurantAccountBanking? = observe("getAccountBanking") { _ ->
        restaurantAccountBankingRepository.findByRestaurant(restaurantId)
    }

    @Transactional
    fun signup(
        dto: RestaurantSignUpRequestDto,
    ): Restaurant = observe("signup") { _ ->
        restaurantRepository.findByCnpj(dto.cnpj)?.also {
            throw ApiException(
                "Restaurant with cnpj=${it.cnpj} already exists",
                "CNPJ já cadastrado",
                HttpStatus.CONFLICT
            )
        }

        restaurantRepository.save(
            Restaurant(
                companyName = dto.companyName,
                businessName = dto.businessName,
                cnpj = dto.cnpj,
                ddd = dto.phone.ddd,
                phoneNumber = dto.phone.number
            ).apply {
                this.imagePath = dto.image?.let { fileService.saveToRestaurant(this, FileCategory.RESTAURANT_PROFILE.toFileData(this.externalId, it)) }
            }
        ).also {
            employeeService.signup(dto.employee, it.externalId)
        }
    }

    @Transactional
    fun updateTerms(
        userAuthentication: UserAuthentication,
        termVersion: String,
    ): Restaurant = observe("updateTerms") { _ ->
        if (termVersion.toTermVersion() < currentTermVersion) throw ApiException(
            "Invalid version $termVersion of term of use",
            "Versão inválida do termo de aceite",
            HttpStatus.UNPROCESSABLE_ENTITY,
        )
        restaurantRepository.getByExternalId(userAuthentication.restaurantId!!).let {
            restaurantRepository.save(it.apply {
                this.termVersion = termVersion
                this.termAcceptedByEmployee = employeeService.getEmployee(userAuthentication)
            })
        }
    }

    fun updateAccountBanking(
        restaurantId: String,
        dto: RestaurantAccountBankingUpdateRequestDto
    ): RestaurantAccountBanking = observe("updateAccountBanking") { _ ->
        val account = restaurantAccountBankingRepository.findByRestaurant(restaurantId)
            ?.apply {
                bankCode = dto.bankCode
                agency = dto.agency
                accountNumber = dto.accountNumber
                verifyingDigit = dto.verifyingDigit
            } ?: RestaurantAccountBanking(
                restaurant = getByExternalId(restaurantId),
                bankCode = dto.bankCode,
                agency = dto.agency,
                accountNumber = dto.accountNumber,
                verifyingDigit = dto.verifyingDigit,
            )
        restaurantAccountBankingRepository.save(account)
    }

    @Transactional
    fun updateAddress(
        restaurantId: String,
        dto: RestaurantAddressUpdateRequestDto
    ): RestaurantAddress = observe("updateAddress") { _ ->
        val address = restaurantAddressRepository.findByRestaurant(restaurantId)
            ?.apply {
                name = dto.name
                street = dto.street
                number = dto.number
                complement = dto.complement
                cep = dto.cep
                neiborhood = dto.neiborhood
                state = dto.state
                city = dto.city
            }
            ?: RestaurantAddress(
                restaurant = getByExternalId(restaurantId),
                name = dto.name,
                street = dto.street,
                number = dto.number,
                complement = dto.complement,
                cep = dto.cep,
                neiborhood = dto.neiborhood,
                state = dto.state,
                city = dto.city,
            )

        restaurantAddressRepository.save(address)
    }


}