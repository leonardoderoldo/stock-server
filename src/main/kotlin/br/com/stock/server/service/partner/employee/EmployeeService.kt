package br.com.stock.server.service.partner.employee

import br.com.stock.server.controller.partner.employee.dto.EmployeeActivateRequestDto
import br.com.stock.server.controller.partner.employee.dto.EmployeeSignUpRequestDto
import br.com.stock.server.controller.partner.employee.login.dto.EmployeeLoginRequestDto
import br.com.stock.server.domain.auth.AuthenticatedUser
import br.com.stock.server.domain.entity.partner.employee.Employee
import br.com.stock.server.domain.entity.partner.employee.EmployeeCredential
import br.com.stock.server.domain.entity.partner.employee.EmployeeJobFunction
import br.com.stock.server.domain.entity.partner.employee.EmployeeRegistration
import br.com.stock.server.domain.entity.partner.employee.EmployeeRegistrationStatus
import br.com.stock.server.domain.entity.partner.employee.EmployeeStatus
import br.com.stock.server.domain.entity.partner.restaurant.Restaurant
import br.com.stock.server.domain.exception.ApiException
import br.com.stock.server.domain.image.FileCategory
import br.com.stock.server.lib.observability.Observable
import br.com.stock.server.lib.security.UserAuthentication
import br.com.stock.server.repository.partner.employee.EmployeeCredentialRepository
import br.com.stock.server.repository.partner.employee.EmployeeRegistrationRepository
import br.com.stock.server.repository.partner.employee.EmployeeRepository
import br.com.stock.server.repository.partner.restaurant.RestaurantRepository
import br.com.stock.server.service.image.FileService
import br.com.stock.server.service.notification.EmailTarget
import br.com.stock.server.service.notification.EmailTemplate
import br.com.stock.server.service.notification.Notification
import br.com.stock.server.service.partner.device.EmployeeDeviceService
import br.com.stock.server.service.security.CredentialPattern
import br.com.stock.server.service.security.CredentialService
import io.micrometer.core.instrument.MeterRegistry
import jakarta.transaction.Transactional
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class EmployeeService(
    meter: MeterRegistry,
    private val fileService: FileService,
    private val credentialService: CredentialService,
    private val employeeRepository: EmployeeRepository,
    private val restaurantRepository: RestaurantRepository,
    private val employeeDeviceService: EmployeeDeviceService,
    private val employeeCredentialRepository: EmployeeCredentialRepository,
    private val employeeRegistrationRepository: EmployeeRegistrationRepository,
): Observable(meter) {


    fun getEmployee(userAuthentication: UserAuthentication): Employee = observe("getEmployee") { _ ->
        employeeRepository.getByExternalId(userAuthentication.id)
    }


    @Transactional
    fun signup(
        dto: EmployeeSignUpRequestDto,
        restaurantId: String,
    ): Employee = observe("signup") { _ ->
        employeeRepository.findActiveEmployee(dto.email, restaurantId)?.let {
            throw ApiException(
                "Employee already exists with email=${dto.email} on restaurantId=$restaurantId",
                "Funcionário já cadastrado",
                HttpStatus.CONFLICT
            )
        }
        val restaurant = restaurantRepository.getByExternalId(restaurantId)
        employeeRepository.save(
            Employee(
                firstName = dto.firstName,
                lastName = dto.lastName,
                cpf = dto.cpf,
                email = dto.email,
                birthDate = dto.birthDate,
                restaurant = restaurant,
                jobFunction = EmployeeJobFunction.valueOf(dto.jobFunction.name)
        ).apply {
            this.imagePath = dto.image?.let { fileService.saveToEmployee(this, FileCategory.EMPLOYEE_PROFILE.toFileData(this.externalId, it)) }
        }).also {
            credentialService.sendRandomCode(registrationNotification(it, restaurant)) { cipher ->
                employeeRegistrationRepository.save(EmployeeRegistration(it, cipher))
            }
        }
    }

    @Transactional
    fun activate(
        dto: EmployeeActivateRequestDto
    ): Employee = observe("activate") { _ ->
        val registrations = employeeRegistrationRepository.findByEmployee(dto.email)
        registrations.firstOrNull { credentialService.verifyPlaintext(dto.code, it.code) }?.let { registration ->
            val passwordHash = credentialService.hash(dto.password, passwordPattern(dto.email))
            employeeRepository.save(
                registration.employee.apply { status = EmployeeStatus.ACTIVE }
            ).also { employee ->
                employeeCredentialRepository.save(EmployeeCredential(employee = employee, data = passwordHash))
                employeeRegistrationRepository.save(registration.apply { status = EmployeeRegistrationStatus.VALIDATED })
            }
        } ?: throw ApiException(
            "Invalid activation to email=${dto.email} [registrations=${registrations.size}]",
            "Não foi possível ativar o cadastro",
            HttpStatus.UNAUTHORIZED
        )
    }

    fun authenticate(
        dto: EmployeeLoginRequestDto,
    ): Pair<Employee, AuthenticatedUser> = observe("authenticate") { _ ->
        val employee = employeeRepository.findActiveEmployee(dto.username)
            ?: throw ApiException(
                "Employee with email=${dto.username} not found",
                "Não autorizado",
                HttpStatus.UNAUTHORIZED
            )

        val employeeCredential = employeeCredentialRepository.findCurrentCredential(employee.id)
        val verified = credentialService.verifyHashedData(dto.password, employeeCredential.getData())
        if (!verified) throw ApiException(
            "Invalid credentials to cpf=${dto.username} on restaurant",
            "Não autorizado",
            HttpStatus.UNAUTHORIZED
        )

        val userAuthentication = credentialService.getAuthenticatedEmployee(employee, null)

        dto.gcmToken?.let { token ->
            employeeDeviceService.save(employee, token)
        }

        Pair(employee, userAuthentication)
    }


    fun refresh(
        userAuthentication: UserAuthentication
    ): AuthenticatedUser = observe("refresh") { _ ->
        val employee = employeeRepository.findActiveEmployee(userAuthentication.username, userAuthentication.restaurantId!!)!!
        credentialService.getAuthenticatedEmployee(employee, userAuthentication)
    }




    private fun passwordPattern(email: String) = CredentialPattern(
        PASSWORD_REGEX,
        "cpf=$email",
        "A senha não atende aos requisitos",
        HttpStatus.UNPROCESSABLE_ENTITY
    )

    private fun registrationNotification(employee: Employee, restaurant: Restaurant?): Notification = if (restaurant != null) {
        Notification(
            target = EmailTarget(employee.email, EmailTemplate.EMPLOYEE_REGISTRATION_BY_RESTAURANT),
            data = mapOf(
                "companyName" to restaurant.companyName,
                "name" to "${employee.firstName} ${employee.lastName}",
                "cpf" to employee.cpf
            )
        )
    } else {
        Notification(
            target = EmailTarget(employee.email, EmailTemplate.EMPLOYEE_REGISTRATION),
            data = mapOf(
                "name" to "${employee.firstName} ${employee.lastName}",
                "cpf" to employee.cpf
            )
        )
    }


    companion object {
        private val PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*[!@#\$&*])(?=.*[0-9])(?=.*[a-z]).{8,50}\$".toRegex()
    }
}