package br.com.stock.server.lib.security

import de.huxhorn.sulky.ulid.ULID
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.MDC
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

class SecurityWebServletFilter(
    authenticationManager: AuthenticationManager,
    private val authProperties: AuthProperties,
) : BasicAuthenticationFilter(authenticationManager) {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val authorization = runCatching { request.getHeader(HttpHeaders.AUTHORIZATION) }.getOrNull()

        MDC.put("cid", runCatching { request.getHeader("cid") }.getOrNull()?.takeIf { it.isNotBlank() } ?: ULID().nextULID())

        if (authorization == null) {
            chain.doFilter(request, response)
        } else {
            try {
                val authentication = authenticationManager.authenticate(UserAuthentication(authorization, authProperties))
                SecurityContextHolder.getContext().authentication = authentication
                onSuccessfulAuthentication(request, response, authentication)
            } catch (e: AuthenticationException) {
                SecurityContextHolder.clearContext()
                onUnsuccessfulAuthentication(request, response, e)
            } finally {
                chain.doFilter(request, response)
            }
        }

        MDC.clear()
    }
}
