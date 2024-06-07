package br.com.stock.server.config

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
class HttpConfig: Filter {

    override fun doFilter(req: ServletRequest, res: ServletResponse, chain: FilterChain) {
        val response = res as HttpServletResponse
        val request = req as HttpServletRequest

        response.setHeader("Access-Control-Allow-Origin", "*")
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PATCH, PUT, DELETE, OPTIONS, HEAD")
        response.setHeader("Access-Control-Max-Age", "3600")
        response.setHeader("Access-Control-Expose-Headers", "*")
        response.setHeader("Access-Control-Allow-Headers", "*")
        response.setHeader("Access-Control-Allow-Credentials", "true")

        if (HttpMethod.OPTIONS.name() == request.method) {
            response.setStatus(HttpStatus.OK.value());
        } else {
            chain.doFilter(req, res)
        }
    }

}