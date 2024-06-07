package br.com.stock.server.lib.metrics

import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Tag
import java.time.Instant
import java.util.concurrent.TimeUnit

fun <T> MeterRegistry.internalOps(operation: String, block: () -> T): T = recordTimerBlock("internal_ops", operation, block)

private fun <T> MeterRegistry.recordTimerBlock(name: String, operation: String, block: () -> T): T {
    val start = Instant.now().nano.toLong()
    var success = false
    return try {
        block().also { success = true }
    } finally {
        recordTimer(name, operation, start, success)
    }
}

private fun MeterRegistry.recordTimer(name: String, operation: String, start: Long, success: Boolean) {
    val timing = Instant.now().nano.toLong() - start
    val status = if (success) "success" else "failure"
    timer(name, listOf(Tag.of("operation", operation), Tag.of("status", status))).record(timing, TimeUnit.NANOSECONDS)
}