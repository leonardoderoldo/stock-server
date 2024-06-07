package br.com.stock.server.lib.security

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

@EnableWebSecurity
@EnableAutoConfiguration
@Configuration(proxyBeanMethods = false)
@EnableMethodSecurity(prePostEnabled = true)
@EnableConfigurationProperties(AuthProperties::class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
class SecurityWebServletConfiguration(
    private val authProperties: AuthProperties,
) {

    @Bean
    fun securityFilterChain(
        httpSecurity: HttpSecurity,
        authenticationManager: AuthenticationManager,
    ): SecurityFilterChain {
        httpSecurity
            .csrf().apply { disable() }
            .and().exceptionHandling().apply { authenticationEntryPoint(UnauthorizaedEntryPoint()) }
            .and().addFilter(SecurityWebServletFilter(authenticationManager, authProperties)).sessionManagement().apply { sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .and().headers().apply { disable() }
            .and().authorizeHttpRequests().apply {
                requestMatchers(AntPathRequestMatcher.antMatcher("/health")).permitAll()
                requestMatchers(AntPathRequestMatcher.antMatcher("/health/complete")).permitAll()
                requestMatchers(AntPathRequestMatcher.antMatcher("/health/liveness")).permitAll()
                requestMatchers(AntPathRequestMatcher.antMatcher("/health/readiness")).permitAll()
                requestMatchers(AntPathRequestMatcher.antMatcher("/info")).permitAll()
                requestMatchers(AntPathRequestMatcher.antMatcher("/metrics")).permitAll()
                authProperties.ignorePaths.forEach {
                    requestMatchers(AntPathRequestMatcher.antMatcher(it)).permitAll()
                }
                anyRequest().authenticated()
            }
        return httpSecurity.build()
    }

    @Bean
    fun webSecurityCustomizer() = WebSecurityCustomizer { web: WebSecurity ->
        authProperties.ignorePaths.forEach {
            web.ignoring().requestMatchers(AntPathRequestMatcher.antMatcher(it))
        }
    }

    @Bean
    fun authenticationManager(): AuthenticationManager = AuthenticationManager { authentication -> authentication }
}

private class UnauthorizaedEntryPoint : AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException,
    ) {
        response.status = HttpStatus.UNAUTHORIZED.value()
        response.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        response.writer.write(RESPONSE_JSON)
    }

    companion object {
        private const val RESPONSE_JSON = """{"message":"Não autorizado"}"""
    }
}
