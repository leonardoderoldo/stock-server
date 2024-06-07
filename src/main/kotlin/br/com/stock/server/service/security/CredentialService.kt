package br.com.stock.server.service.security

import br.com.stock.server.domain.auth.AuthenticatedUser
import br.com.stock.server.domain.entity.backoffice.BackofficeUser
import br.com.stock.server.domain.entity.customer.Customer
import br.com.stock.server.domain.entity.partner.employee.Employee
import br.com.stock.server.domain.entity.partner.restaurant.Restaurant
import br.com.stock.server.domain.exception.ApiException
import br.com.stock.server.lib.crypto.HashedData
import br.com.stock.server.lib.crypto.RsaCipher
import br.com.stock.server.lib.crypto.ShaHasher
import br.com.stock.server.lib.crypto.extensions.asRsa
import br.com.stock.server.lib.crypto.extensions.toPrivateKey
import br.com.stock.server.lib.crypto.extensions.toPublicKey
import br.com.stock.server.lib.observability.Observable
import br.com.stock.server.lib.security.UserAuthentication
import br.com.stock.server.service.notification.Notification
import br.com.stock.server.service.notification.NotificationService
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import de.huxhorn.sulky.ulid.ULID
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.stereotype.Service
import java.security.PrivateKey
import java.security.PublicKey
import java.security.SecureRandom
import java.security.interfaces.RSAPrivateKey
import java.time.OffsetDateTime

private val codeChars = listOf("2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L", "M", "N", "P", "R", "S", "T", "U", "V", "W", "X", "Y", "Z")

@Service
class CredentialService(
    meter: MeterRegistry,
    private val notificationService: NotificationService,
    @Value("\${app.frontend.url}") private val frontendUrl: String,
    @Value("\${app.database.public-key}") private val databasePublicKeyString: String,
    @Value("\${app.database.private-key}") private val databasePrivateKeyString: String,
    @Value("\${app.http.private-key}") private val httpPrivateKeyString: String,
    @Value("\${app.auth.jwt-private-key}") private val jwtPrivateKeyString: String,
    @Value("\${app.auth.backoffice-private-key}") private val backofficePrivateKeyString: String,
    @Value("\${app.auth.jwt.duration-in-minutes}") private val jwtDurationInMinutes: Long,
    @Value("\${app.auth.jwt.refresh-duration-after-token-expired-in-minutes}") private val refreshDurationInMinutes: Long,
) : Observable(meter) {
    
    private val databasePublicKey: PublicKey = databasePublicKeyString.toPublicKey()
    private val databasePrivateKey: PrivateKey = databasePrivateKeyString.toPrivateKey()

    private val httpPrivateKey: PrivateKey = httpPrivateKeyString.toPrivateKey()

    private val jwtPrivateKey: RSAPrivateKey = jwtPrivateKeyString.toPrivateKey().asRsa()
    private val backofficePrivateKey: RSAPrivateKey = backofficePrivateKeyString.toPrivateKey().asRsa()

    fun hash(cipher: String, credentialPattern: CredentialPattern? = null) = observe("hash") { _ ->
        runCatching {
            val plaintext = decrypt(cipher)
            credentialPattern?.let {
                if (!plaintext.matches(it.regex)) throw ApiException(
                    "Plaintext=$plaintext don't match to regex=${it.regex} [${it.failMessage}]",
                    it.failResponseMessage,
                    HttpStatus.UNPROCESSABLE_ENTITY
                )
            }
            ShaHasher.hash(plaintext, databasePublicKey)
        }.getOrElse {
            if (it is ApiException) throw it
            throw ApiException(
                "Invalid cipher data",
                "Criptografia inválida",
                HttpStatus.UNPROCESSABLE_ENTITY
            )
        }
    }

    fun <T> sendRandomCode(notification: Notification, beforeSend: (cipherCode: String) -> T): T = observe("sendRandomCode") { _ ->
        (1..6).fold("") { acc, _ ->
            "$acc${codeChars[SecureRandom.getInstanceStrong().nextInt(0, codeChars.size)]}"
        }.let {
            it to RsaCipher.encrypt(it, databasePublicKey)
        }.let { (plaintext, cipherCode) ->
            beforeSend(cipherCode).also {
                notificationService.send(Notification(notification.target, notification.data + mapOf(
                    "code" to plaintext,
                    "link" to frontendUrl
                )))
            }
        }
    }

    fun verifyPlaintext(plaintext: String, cipher: String) = observe("verifyPlaintext") { _ ->
        runCatching { plaintext == RsaCipher.decrypt(cipher, databasePrivateKey) }.getOrElse { false }
    }

    fun verifyHashedData(cipher: String, hashedData: HashedData) = observe("verifyHashedData") { _ ->
        runCatching {
            ShaHasher.verify(decrypt(cipher), hashedData, databasePrivateKey)
        }.getOrElse { false }
    }

    fun getAuthenticatedEmployee(employee: Employee, userAuthentication: UserAuthentication?): AuthenticatedUser {
        val authorities = listOf(
            "ROLE_USER",
            "ROLE_EMPLOYEE",
            "ROLE_EMPLOYEE_ON_RESTAURANT",
            "ROLE_EMPLOYEE_JOB_FUNCTION_${employee.jobFunction}"
        )
        return getAuthenticatedEmployee(employee, employee.restaurant, authorities, userAuthentication)
    }

    private fun getAuthenticatedEmployee(employee: Employee, restaurant: Restaurant?, authorities: List<String>, userAuthentication: UserAuthentication?): AuthenticatedUser {
        val token = getToken(
            userAuthentication,
            restaurant?.let { mapOf("restaurant_id" to it.externalId) } ?: emptyMap(),
            employee.externalId,
            employee.cpf,
            authorities,
            "ROLE_EMPLOYEE_REFRESH",
            jwtDurationInMinutes,
            refreshDurationInMinutes,
            jwtPrivateKey
        )
        return AuthenticatedUser(
            token.accessToken,
            token.refreshToken,
            authorities,
            token.expiresAt,
            token.refreshAt
        )
    }

    fun getAuthenticatedCustomer(customer: Customer, userAuthentication: UserAuthentication?): AuthenticatedUser {
        val authorities = listOf(
            "ROLE_USER",
            "ROLE_TUTOR",
            "ROLE_TUTOR_ON_ENROLLMENT",
        )
        return getAuthenticatedCustomer(customer, authorities, userAuthentication)
    }

    fun getAuthenticatedCustomer(customer: Customer, authorities: List<String>, userAuthentication: UserAuthentication?): AuthenticatedUser {
        val token = getToken(
            userAuthentication,
            emptyMap(),
            customer.externalId,
            customer.cpf,
            authorities,
            "ROLE_CUSTOMER_REFRESH",
            jwtDurationInMinutes,
            refreshDurationInMinutes,
            jwtPrivateKey
        )
        return AuthenticatedUser(
            token.accessToken,
            token.refreshToken,
            authorities,
            token.expiresAt,
            token.refreshAt
        )
    }

    fun getAuthenticatedBackofficeUser(backofficeUser: BackofficeUser, userAuthentication: UserAuthentication?): AuthenticatedUser {
        val authorities = listOf("ROLE_BACKOFFICE")
        val username = backofficeUser.cpf
        val token = getToken(
            userAuthentication,
            emptyMap(),
            backofficeUser.externalId,
            username,
            authorities,
            "ROLE_BACKOFFICE_REFRESH",
            jwtDurationInMinutes,
            refreshDurationInMinutes,
            jwtPrivateKey
        )
        return AuthenticatedUser(
            token.accessToken,
            token.refreshToken,
            authorities,
            token.expiresAt,
            token.refreshAt
        )
    }

    private fun decrypt(cipher: String) = RsaCipher.decrypt(cipher, httpPrivateKey)

    private fun getToken(
        userAuthentication: UserAuthentication?,
        claims: Map<String, String>,
        id: String,
        username: String,
        authorities: List<String>,
        refreshAuthority: String,
        accessTokenDurationInMinutes: Long,
        refreshTokenAfterAccessExpiredInMinutes: Long,
        privateKey: RSAPrivateKey
    ): Token {
        val expiresAt = OffsetDateTime.now().plusMinutes(accessTokenDurationInMinutes)
        val refreshAt = expiresAt.plusMinutes(refreshTokenAfterAccessExpiredInMinutes)
        return Token(
            accessToken = getToken(
                jti = userAuthentication?.jti,
                claims = mapOf(
                    "id" to id,
                    "username" to username,
                    "authorities" to authorities,
                ) + claims,
                expiresAt = expiresAt,
                privateKey = privateKey
            ),
            expiresAt = expiresAt,
            refreshToken = getToken(
                jti = userAuthentication?.jti,
                claims = mapOf(
                    "id" to id,
                    "username" to username,
                    "authorities" to listOf(refreshAuthority),
                ) + claims,
                expiresAt = refreshAt,
                privateKey = privateKey
            ),
            refreshAt = refreshAt
        )
    }

    private fun getToken(
        jti: String?,
        claims: Map<String, Any>,
        expiresAt: OffsetDateTime,
        privateKey: RSAPrivateKey
    ): String = observe("jwrt") { _ ->
        JWT.create()
            .apply {
                claims.forEach {
                    if (it.value is List<*>) {
                        withClaim(it.key, it.value as List<*>)
                    } else {
                        withClaim(it.key, it.value as String)
                    }
                }
            }
            .withExpiresAt(expiresAt.toInstant())
            .withJWTId(jti ?: ULID().nextULID())
            .sign(Algorithm.RSA256(privateKey))
    }
}

data class CredentialPattern(
    val regex: Regex,
    val failMessage: String,
    val failResponseMessage: String,
    val failStatusCode: HttpStatusCode
)

private data class Token(
    val accessToken: String,
    val expiresAt: OffsetDateTime,
    val refreshToken: String,
    val refreshAt: OffsetDateTime,
)