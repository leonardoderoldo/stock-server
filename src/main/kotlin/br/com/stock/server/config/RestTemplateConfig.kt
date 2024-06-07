package br.com.stock.server.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate

@Configuration
class RestTemplateConfig {

    @Bean
    fun restTemplate(objectMapper: ObjectMapper) = RestTemplate().apply {
        messageConverters.add(0, MappingJackson2HttpMessageConverter().apply {
            setObjectMapper(objectMapper)
        })
    }

}