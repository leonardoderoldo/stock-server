package br.com.stock.server.lib.observability

import br.com.stock.server.lib.metrics.internalOps
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.micrometer.common.util.internal.logging.InternalLogger
import io.micrometer.common.util.internal.logging.Slf4JLoggerFactory
import io.micrometer.core.instrument.MeterRegistry
import org.jboss.logging.MDC
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

abstract class Observable(
    val meter: MeterRegistry
) {
    private val observableName = this.javaClass.name.substringBefore("\$Companion")
    private val observableSimpleName = this.javaClass.simpleName.substringBefore("\$Companion")

    private val logger: LoggableWrapper by lazy {
        val internalLogger = Slf4JLoggerFactory.getInstance(observableName)
        LoggableWrapperImpl(internalLogger)
    }

    fun <T> loggable(name: String, loggableBlock: (logger: LoggableWrapper) -> T): T = loggable(tags = arrayOf(journey(name)), loggableBlock)

    fun <T> loggable(vararg tags: Pair<String, String>, loggableBlock: (logger: LoggableWrapper) -> T): T {
        tags.toList().forEach {
            MDC.get(it.first)?.also { mdcValue ->
                MDC.put(it.first, "$mdcValue,${it.second}")
            } ?: run {
                MDC.put(it.first, it.second)
            }
        }
        return loggableBlock(logger)
    }

    fun <T> observe(
        name: String,
        vararg tags: Pair<String, String>,
        observableBlock: (logger: LoggableWrapper) -> T
    ): T = meter.internalOps("${observableSimpleName}_$name") {
        loggable(tags = arrayOf(journey(name), *tags), loggableBlock = observableBlock)
    }

    fun journey(name: String) = "journey" to "${observableSimpleName}_$name"
}

interface LoggableWrapper {
    fun info(vararg tags: Pair<String, String>, message: () -> String)
    fun error(vararg tags: Pair<String, String>, message: () -> String)
    fun error(exception: Exception, vararg tags: Pair<String, String>, message: () -> String)
    fun warn(vararg tags: Pair<String, String>, message: () -> String)
    fun warn(exception: Exception, vararg tags: Pair<String, String>, message: () -> String)
    fun debug(vararg tags: Pair<String, String>, message: () -> String)
    fun trace(vararg tags: Pair<String, String>, message: () -> String)
    fun audit(vararg tags: Pair<String, String>, message: () -> String)
    fun audit(exception: Exception, vararg tags: Pair<String, String>, message: () -> String)
}

private val objectMapper = jacksonObjectMapper()

private class LoggableWrapperImpl(
    private val logger: InternalLogger
) : LoggableWrapper {

    override fun info(vararg tags: Pair<String, String>, message: () -> String) {
        log("info", null, tags, message)
    }

    override fun error(vararg tags: Pair<String, String>, message: () -> String) {
        log("error", null, tags, message)
    }

    override fun error(exception: Exception, vararg tags: Pair<String, String>, message: () -> String) {
        log("error", exception, tags, message)
    }

    override fun warn(vararg tags: Pair<String, String>, message: () -> String) {
        log("warn", null, tags, message)
    }

    override fun warn(exception: Exception, vararg tags: Pair<String, String>, message: () -> String) {
        log("warn", exception, tags, message)
    }

    override fun debug(vararg tags: Pair<String, String>, message: () -> String) {
        log("debug", null, tags, message)
    }

    override fun trace(vararg tags: Pair<String, String>, message: () -> String) {
        log("trace", null, tags, message)
    }

    override fun audit(vararg tags: Pair<String, String>, message: () -> String) {
        log("audit", null, tags, message)
    }

    override fun audit(exception: Exception, vararg tags: Pair<String, String>, message: () -> String) {
        log("audit", exception, tags, message)
    }

    private fun log(level: String, exception: Exception?, tags: Array<out Pair<String, String>>, message: () -> String) {
        val map = MDC.getMap()
        val allTags = map.map { it.key to it.value }.toList() + tags + listOf("level" to level)
        val groupedTags = allTags.groupBy { it.first }
            .entries.map {
                it.key to it.value
                    .map { value -> value.second?.toString() ?: "" }
                    .fold("") { acc, s -> if (acc.isBlank()) s else "$acc,$s" }
            }

        val jsonMessage = jsonMessage(message, exception, *groupedTags.toTypedArray())
        when (level) {
            "debug" -> logger.debug(jsonMessage)
            "info" -> logger.info(jsonMessage)
            "warn" -> logger.warn(jsonMessage)
            "error" -> logger.error(jsonMessage)
            "audit" -> logger.info(jsonMessage)
            else -> logger.trace(jsonMessage)
        }
    }

    private fun jsonMessage(
        message: () -> String,
        exception: Exception?,
        vararg tags: Pair<String, String>,
    ): String? = objectMapper.writeValueAsString(LogData(message(), mapOf(*tags), exception?.stackTraceToString()))
}

private data class LogData(
    val message: String,
    val tags: Map<String, String>,
    val stacktrace: String?
) {
    val timestamp: String = OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
}