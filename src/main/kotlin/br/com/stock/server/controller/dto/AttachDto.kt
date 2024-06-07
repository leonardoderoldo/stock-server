package br.com.stock.server.controller.dto

data class AttachDto(
    val base64: String,
    val contentType: String,
    val fileName: String,
)
