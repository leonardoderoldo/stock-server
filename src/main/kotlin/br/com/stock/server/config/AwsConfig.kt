package br.com.stock.server.config

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.sns.AmazonSNS
import com.amazonaws.services.sns.AmazonSNSClient
import com.amazonaws.services.sqs.AmazonSQSAsync
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.cloud.aws.messaging.config.QueueMessageHandlerFactory
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.messaging.converter.MappingJackson2MessageConverter
import org.springframework.messaging.handler.annotation.support.PayloadMethodArgumentResolver
import org.springframework.util.MimeTypeUtils
import java.util.*


@Profile("!test")
@Configuration(proxyBeanMethods = false)
class AwsConfig(
    private val objectMapper: ObjectMapper
){

    @Bean
    fun amazonS3(): AmazonS3 = AmazonS3Client.builder().apply {
        credentials = DefaultAWSCredentialsProviderChain.getInstance()
    }.build()

    @Bean
    @Primary
    fun amazonSNS(): AmazonSNS = AmazonSNSClient.builder().apply {
        credentials = DefaultAWSCredentialsProviderChain.getInstance().apply {
            region = Regions.US_EAST_1.getName()
        }
    }.build()

    @Bean
    @Primary
    fun amazonSQSAsync(): AmazonSQSAsync = AmazonSQSAsyncClientBuilder.standard().apply {
        credentials = DefaultAWSCredentialsProviderChain.getInstance().apply {
            region = Regions.US_EAST_1.getName()
        }
    }.build()

    @Bean
    @Primary
    fun notificationMessagingTemplate(amazonSNS: AmazonSNS) = NotificationMessagingTemplate(amazonSNS)

    @Bean
    @Primary
    fun queueMessagingTemplate(amazonSQSAsync: AmazonSQSAsync) = QueueMessagingTemplate(amazonSQSAsync)

    @Bean
    @Primary
    fun queueMessageHandlerFactory(amazonSQSAsync: AmazonSQSAsync): QueueMessageHandlerFactory {
        val converter = MappingJackson2MessageConverter(MimeTypeUtils.TEXT_PLAIN, MimeTypeUtils.APPLICATION_JSON)
        converter.objectMapper = objectMapper
        converter.isStrictContentTypeMatch = false
        converter.serializedPayloadClass = String::class.java

        return QueueMessageHandlerFactory().apply {
            setAmazonSqs(amazonSQSAsync)
            messageConverters = listOf(converter)
        }
    }

}
