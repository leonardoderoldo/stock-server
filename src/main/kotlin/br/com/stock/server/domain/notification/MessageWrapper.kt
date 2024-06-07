package br.com.stock.server.domain.notification

import br.com.stock.server.service.notification.DeviceTemplate
import br.com.stock.server.service.notification.Notification
import br.com.stock.server.service.notification.PushTarget
import java.time.OffsetDateTime
import java.util.*

interface MessageWrapper<T> {
    var cid: String
    var createdAt: OffsetDateTime?
    var subject: String?
    var data: T
    var delay: Int?

    fun increaseDelay() : MessageWrapper<T> {
        return object : MessageWrapper<T> {
            override var cid: String = this@MessageWrapper.cid
            override var createdAt: OffsetDateTime? = this@MessageWrapper.createdAt
            override var subject: String? = this@MessageWrapper.subject
            override var data: T = this@MessageWrapper.data
            override var delay: Int? = this@MessageWrapper.delay?.let { it + it } ?: 5
        }
    }
}



data class MessageWrapperExternalId(
    override var cid: String = UUID.randomUUID().toString(),
    override var createdAt: OffsetDateTime?,
    override var subject: String? = null,
    override var data: String,
    override var delay: Int? = 0
): MessageWrapper<String> {
    companion object {

        fun from(externalId: String, createdAt: OffsetDateTime) = MessageWrapperExternalId(
            data = externalId,
            createdAt = createdAt
        )

        fun from(externalId: String, createdAt: OffsetDateTime, subject: String) = MessageWrapperExternalId(
            subject = subject,
            data = externalId,
            createdAt = createdAt
        )
    }
}

data class MessageWrapperPush(
    override var cid: String = UUID.randomUUID().toString(),
    override var createdAt: OffsetDateTime?,
    override var subject: String? = null,
    override var data: MessagePush,
    override var delay: Int? = 0
): MessageWrapper<MessagePush> {

    companion object {

        fun from(notification: Notification): MessageWrapper<*> {
            val target = notification.target as PushTarget
            return MessageWrapperPush(
                createdAt = OffsetDateTime.now(),
                data = MessagePush(
                    FCMToken = target.FCMToken,
                    template = target.template,
                    data = notification.data
                )
            )
        }
    }
}

data class MessagePush(
    var FCMToken: String,
    var template: DeviceTemplate,
    var data: Map<String, String?>
)
