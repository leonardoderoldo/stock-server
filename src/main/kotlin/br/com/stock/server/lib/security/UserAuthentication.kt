package br.com.stock.server.lib.security

import br.com.stock.server.lib.crypto.extensions.asRsa
import br.com.stock.server.lib.crypto.extensions.toPublicKey
import com.auth0.jwt.JWT
import com.auth0.jwt.RegisteredClaims
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import org.slf4j.MDC
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

class UserAuthentication(
    authorizationHeader: String,
    authProperties: AuthProperties
) : Authentication {
    var jti = ""
        private set

    var username = ""
        private set

    var id = ""
        private set

    var restaurantId: String? = null
        private set

    private var authorization = ""
    private var authenticated = false
    private var authorities = emptySet<GrantedAuthority>()

    init {
        authorization = authorizationHeader.replace("Bearer ", "")

        if (authorizationHeader.contains("Bearer")) {
            runCatching {
                val (decodedJwt, auth) = decodedJWT(authProperties.jwtPublicKey, authorization)?.let { it to "CUSTOMER" }
                    ?: decodedJWT(authProperties.backofficePublicKey, authorization)?.let { it to "BACKOFFICE" }
                    ?: throw UnauthorizedException()

                jti = decodedJwt.claims[RegisteredClaims.JWT_ID]?.asString() ?: throw UnauthorizedException()
                id = decodedJwt.claims[ID]?.asString() ?: throw UnauthorizedException()
                restaurantId = decodedJwt.claims[RESTAURANT_ID]?.asString()
                username = decodedJwt.claims[USERNAME]?.asString() ?: throw UnauthorizedException()
                authorities = decodedJwt.claims[AUTHORITIES]?.asList(String::class.java)
                    ?.map { SimpleGrantedAuthority(it) }
                    ?.toSet() ?: throw UnauthorizedException()

                MDC.put("authType", auth)
                MDC.put("id", id)
                MDC.put("username", username)
                MDC.put("restaurantId", restaurantId)
            }.onSuccess { isAuthenticated = true }
                .onFailure { emptyState() }
        } else if (validateSharedSecret(authProperties.serviceSharedSecret, authorization)) {
            validSharedSecretRole(ROLE_SERVICE)
            MDC.put("authType", "SERVICE")
        }
    }

    private fun decodedJWT(publicKey: String?, jwt: String): DecodedJWT? = runCatching {
        publicKey
            ?.toPublicKey()
            ?.asRsa()
            ?.let { Algorithm.RSA256(it) }
            ?.let { JWT.require(it).build() }
            ?.verify(jwt)
    }.getOrNull()

    private fun validateSharedSecret(sharedSecret: String?, authorization: String) = sharedSecret?.let { it == authorization } ?: false

    private fun validSharedSecretRole(role: String) {
        isAuthenticated = true
        username = role
        authorities = setOf(SimpleGrantedAuthority(role))
    }

    fun isEmployee() = authorities.any { it.authority == "ROLE_EMPLOYEE" }

    fun isCustomer() = authorities.any { it.authority == "ROLE_CUSTOMER" }


    private fun emptyState() {
        id = ""
        username = ""
        authorities = emptySet()
        isAuthenticated = false
    }

    override fun getName(): String  = username

    override fun getAuthorities(): Set<GrantedAuthority> = authorities

    override fun getCredentials(): Any = authorization

    override fun getDetails(): Any = ""

    override fun getPrincipal(): Any = authorization

    override fun isAuthenticated(): Boolean = authenticated

    override fun setAuthenticated(isAuthenticated: Boolean) {
        authenticated = isAuthenticated
    }

    companion object {
        const val ROLE_SERVICE = "ROLE_SERVICE"
        const val ID = "id"
        const val USERNAME = "username"
        const val RESTAURANT_ID = "restaurant_id"
        const val AUTHORITIES = "authorities"
    }

}

private class UnauthorizedException: Exception()
