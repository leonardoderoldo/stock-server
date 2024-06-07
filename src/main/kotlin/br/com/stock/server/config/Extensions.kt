package br.com.stock.server.config

import org.joda.time.MutableDateTime.ROUND_HALF_EVEN
import java.math.BigDecimal

fun BigDecimal.toCents(): Int {
    return this.toString().replace(".", "").replace(",", "").toInt()
}

fun BigDecimal.getPercent(percent: Float): BigDecimal {
    return if (percent.compareTo(0) < 0) BigDecimal.ZERO
    else (this.divide(BigDecimal("100"))).multiply(percent.toBigDecimal()).setScale(2)
}