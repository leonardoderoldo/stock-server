package br.com.stock.server.extentions

fun String?.toTermVersion(): Long = kotlin.runCatching {
    this?.split(".")
        ?.map { it.padStart(5, '0') }
        ?.reduce { acc, s -> "$acc$s" }
        ?.toLong()
        ?: -1
}.getOrElse { -1 }

fun String.emailMask(): String {
    val prefix = this.take(1)
    val sufix = this.drop(this.indexOf("@") - 1)
    return "$prefix***$sufix"
}
