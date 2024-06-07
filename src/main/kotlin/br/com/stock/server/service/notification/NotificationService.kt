package br.com.stock.server.service.notification

import br.com.stock.server.domain.exception.ApiException
import br.com.stock.server.lib.observability.Observable
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class NotificationService(
    meter: MeterRegistry,
    private val pushNotificationService: PushNotificationService,
    private val emailNotificationSenderService: EmailNotificationSenderService
): Observable(meter) {

    fun send(notification: Notification): Unit = observe("send") { _ ->
        runCatching {
            when(notification.target) {
                is PushTarget -> pushNotificationService.send(notification)
                is EmailTarget -> emailNotificationSenderService.send(notification)
            }
        }.onFailure {
            throw ApiException(
                "Error when try to send notification to ${notification.target} [message=${it.message}]",
                "Não foi possível enviar a notificação",
                HttpStatus.BAD_GATEWAY,
                it
            )
        }
    }
}

class Notification(
    val target: NotificationTarget,
    val data: Map<String, String?>,
    val replaces: Map<String, String?> = emptyMap()
)

interface NotificationTarget
