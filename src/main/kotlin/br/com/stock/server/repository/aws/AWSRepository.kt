package br.com.stock.server.repository.aws

import br.com.stock.server.domain.exception.BusinessException
import br.com.stock.server.domain.notification.MessageWrapper
import br.com.stock.server.lib.observability.Observable
import br.com.stock.server.service.notification.NotificationTarget
import com.fasterxml.jackson.databind.ObjectMapper
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate
import org.springframework.stereotype.Service

@Service
class AWSRepository(
    meter: MeterRegistry,
//    private val amazonSNSClient: AmazonSNS,
//    private val queueMessagingTemplate: QueueMessagingTemplate,
    private val objectMapper: ObjectMapper,
    private val notificationMessagingTemplate: NotificationMessagingTemplate,
): Observable(meter) {

    fun send(notification: NotificationSNSTarget):Unit
        = observe("send") { _ ->
            try {
                this.notificationMessagingTemplate.sendNotification(
                    notification.topicArn,
                    this.objectMapper.writeValueAsString(notification.data),
                    notification.status
                )
            } catch (e: Exception) {
                throw BusinessException("Publish erro send message to topic=${notification.topicArn}")
            }
    }

}

data class NotificationSNSTarget(
    val topicArn: String,
    val data: MessageWrapper<*>,
    val status: String
): NotificationTarget