package br.com.stock.server.service.notification

import br.com.stock.server.domain.notification.MessageWrapperPush
import br.com.stock.server.lib.observability.Observable
import br.com.stock.server.repository.aws.AWSRepository
import br.com.stock.server.repository.aws.NotificationSNSTarget
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class PushNotificationService(
    meter: MeterRegistry,
    @Value("\${app.push.enabled}") private val enabled: Boolean,
    @Value("\${app.aws.sns.push-notification}") private val pushNotificationTopicArn: String,
//    private val firebaseMessage: FirebaseMessaging,
    private val AWSRepository: AWSRepository,
): Observable(meter) {

    fun sendAsync(notification: Notification)
        = observe("sendAsync") { _ ->
        this.AWSRepository.send(
            NotificationSNSTarget(
                status = "SENDING",
                topicArn = pushNotificationTopicArn,
                data = MessageWrapperPush.from(notification)
            )
        )
    }

    fun send(notification: Notification): Unit = observe("send") { _ ->
        if (!enabled) return@observe
        val pushTarget = notification.target as PushTarget
        val fcmNotification = com.google.firebase.messaging.Notification.builder()
        fcmNotification.setTitle(pushTarget.template.title)

        var body = pushTarget.template.body
        notification.replaces.forEach { t, u ->
            body = body.replace("{{${t}}}", u.let { u } ?: "")
        }
        fcmNotification.setBody(body)
        val allData = notification.data + mapOf(
            "ACTION" to pushTarget.template.name
        )
        val message = Message.builder()
            .setToken(pushTarget.FCMToken)
            .setNotification(fcmNotification.build())
            .putAllData(allData.toMutableMap())
            .build()
//        firebaseMessage.send(message)
    }
}

data class PushTarget(
    val FCMToken: String,
    val template: DeviceTemplate,
) : NotificationTarget

enum class DeviceTemplate(
    val title: String,
    val body: String
) {

    BILLING_REMINDER("Vencimento chegando!", "O vencimento da cobrança está chegando! Fique atento e evite cobrança de multas e juros"),
    BILLING_OVERDUE("Cobrança vencida", "Existe uma cobrança vencida! Abra o aplicativo e confira. Fique atento e evite cobrança de multas e  juros"),
    BILLING_DUE_TODAY("O Vencimento é hoje!", "O vencimento da parcela {{installment}} de {{installmentOf}} da cobrança {{invoiceTitle}} é hoje! Fique atento e evite cobrança de multas e juros"),
    ANNOUNCEMENT_NEW("Chegou um novo comunicado", "Assunto: {{subject}}"),
    APPOINTMENTBOOK_UPDATE("O diário da(o) {{studentName}} foi atualizado", "Para ver mais detalhes do diário acesse o APP e confira todas as atualizações.")

}
