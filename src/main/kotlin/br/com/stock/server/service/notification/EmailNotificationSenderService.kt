package br.com.stock.server.service.notification

import br.com.stock.server.domain.exception.ApiException
import br.com.stock.server.lib.observability.Observable
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ClassPathResource
import org.springframework.http.HttpStatus
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service

@Service
class EmailNotificationSenderService(
    meter: MeterRegistry,
    @Value("\${app.email.enabled}") private val enabled: Boolean,
    @Value("\${app.email.from}") private val emailFrom: String,
    private val emailSender: JavaMailSender,
) : Observable(meter) {

    private val contentTemplate: Map<EmailTemplate, String> by lazy {
        EmailTemplate.entries.associateWith { loadFileData(it) }
    }

    private val template: String by lazy { loadTemplateBase() }

    private val templateImageHeader: String by lazy { loadTemplateImageHeader() }

    fun send(notification: Notification): Unit = observe("send") { logger ->
        if (!enabled) return@observe
        val emailTarget = notification.target as EmailTarget
        val template = emailTarget.template
        val htmlBody = contentTemplate[template]!!

        val subject = notification.data.entries.fold(template.subject) { acc, entry -> acc.replace("{{${entry.key}}}", entry.value ?: "") }
        val templateTitle = notification.data.entries.fold(template.title) { acc, entry -> acc.replace("{{${entry.key}}}", entry.value ?: "") }
        val templateContent = notification.data.entries.fold(htmlBody) { acc, entry -> acc.replace("{{${entry.key}}}", entry.value ?: "") }
        val imageHeader = template.imageHeaderLink?.let { this.templateImageHeader.replace(IMAGE_LINK, it) } ?: ""
        val text = this.template.replace(TITLE, templateTitle).replace(CONTENT, templateContent).replace(IMAGE_HEADER, imageHeader)

        try {
            val message = emailSender.createMimeMessage().also {
                MimeMessageHelper(it, false).apply {
                    setFrom(emailFrom)
                    setTo(emailTarget.email)
                    setSubject(subject)
                    setText(text, true)
                }
            }
            emailSender.send(message)
        }  catch (e: Exception){
            throw ApiException(
                "Error sending email with verification code",
                "Erro ao enviar email com o código de verificação.",
                HttpStatus.INTERNAL_SERVER_ERROR
            )
        }
    }

    private fun loadFileData(emailTemplate: EmailTemplate): String {
        return ClassPathResource(emailTemplate.filePath).inputStream.reader().readLines().reduce { acc, value -> "$acc$value" }
    }

    private fun loadTemplateBase(): String {
        return ClassPathResource("template/email/template.html").inputStream.reader().readLines().reduce { acc, value -> "$acc$value" }
    }

    private fun loadTemplateImageHeader(): String {
        return ClassPathResource("template/email/image_header_template.html").inputStream.reader().readLines().reduce { acc, value -> "$acc$value" }
    }

    companion object {
        private const val TITLE = "{{title}}"
        private const val CONTENT = "{{content}}"
        private const val IMAGE_HEADER = "{{imageHeader}}"
        private const val IMAGE_LINK = "{{imageLink}}"
    }
}

data class EmailTarget(
    val email: String,
    val template: EmailTemplate
) : NotificationTarget

enum class EmailTemplate(
    val subject: String,
    val imageHeaderLink: String?,
    val title: String,
    val filePath: String,
) {
    ENROLLMENT_INVITE(
        "Convite: {{name}}",
        null,
        "Você foi adicionado como responsável no Escola na Mão",
        "template/email/enrollment_invite.html",
    ),
    EMPLOYEE_REGISTRATION(
        "Bem vindo ao Cardápio Digital STOCK",
        null,
        "Cadastro",
        "template/email/employee_registration.html",
    ),
    EMPLOYEE_REGISTRATION_BY_RESTAURANT(
        "Bem vindo ao Cardápio Digital STOCK",
        null,
        "Você foi cadastrado",
        "template/email/employee_registration_by_restaurant.html",
    ),
    EMPLOYEE_RELATION(
        "Cadastro de funcionário",
        null,
        "Função alterada",
        "template/email/employee_relation.html",
    ),
    RESTAURANT_INDICATION(
        "Indicação de restaurante ({{environment}})",
        null,
        "Indicação de restaurante ({{environment}})",
        "template/email/restaurant_indication.html",
    ),
    CUSTOMER_REGISTRATION(
        "Cadastro de cliente",
        null,
        "Cadastro",
        "template/email/customer_registration.html",
    ),
    SITE_CONTACT(
        "Fale conosco ({{environment}}) - {{subject}}",
        null,
        "Fale conosco ({{environment}})",
        "template/email/site_contact.html",
    ),
    RESET_PASSWORD_CONFIRMATION_CODE(
        "Código para alteração de senha",
        null,
        "App: Fast Bee{{appDetail}}",
        "template/email/reset_password_confirmation_code.html",
    ),
    RESET_PASSWORD(
        "Senha alterada",
        null,
        "App: Fast Bee{{appDetail}}",
        "template/email/reset_password.html",
    ),
}
