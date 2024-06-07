package br.com.stock.server.lib.security

import com.google.api.client.util.Value
import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.core.annotation.Order
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component

@Order(1)
@Component
@EnableConfigurationProperties(CorsProperties::class)
class CorsWebFilter(
    private val corsProperties: CorsProperties,
) : Filter {

    override fun doFilter(req: ServletRequest, resp: ServletResponse, chain: FilterChain) {
        try {
            chain.doFilter(req, resp)
        } finally {
            val request = req as HttpServletRequest
            val response = resp as HttpServletResponse

            request.headerNames.toList()
                .firstOrNull { it.equals(HttpHeaders.ORIGIN, true) }
                ?.let { request.getHeader(it) }
                ?.takeIf { corsProperties.allowedOrigins?.any { allowedOrigin -> it.matches(allowedOrigin.toRegex()) } ?: false }
                ?.also { response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, it) }
                ?: response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, corsProperties.defaultOrigin)
        }
    }
}
