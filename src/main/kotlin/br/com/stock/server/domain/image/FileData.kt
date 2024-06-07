package br.com.stock.server.domain.image

import br.com.stock.server.controller.dto.AttachDto
import br.com.stock.server.controller.dto.ImageDto
import org.springframework.http.MediaType

data class FileData(
    val externalId: String,
    val category: FileCategory,
    val base64: String,
    private val mediaType: MediaType,
) {
    val fileName = "$externalId.${mediaType.subtype}"
    val contentType = "${mediaType.type}/${mediaType.subtype}"
}

enum class FileCategory {

    ANNOUNCEMENT,
    ANNOUNCEMENT_ATTACH,
    ALBUM,
    ALBUM_IMAGE,
    CHAT,
    PRODUCT,
    OPTION,
    EMPLOYEE_PROFILE,
    RESTAURANT_PROFILE,
    CUSTOMER_PROFILE;

    fun toFileData(externalId: String, dto: ImageDto) = FileData(
        externalId,
        this,
        dto.base64,
        MediaType.parseMediaType(dto.contentType)
    )

    fun toFileData(externalId: String, dto: AttachDto) = FileData(
        externalId,
        this,
        dto.base64,
        MediaType.parseMediaType(dto.contentType)
    )
}
