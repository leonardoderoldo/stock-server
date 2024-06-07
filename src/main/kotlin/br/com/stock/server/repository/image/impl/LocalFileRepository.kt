package br.com.stock.server.repository.image.impl

import br.com.stock.server.domain.exception.ApiException
import br.com.stock.server.domain.image.FileData
import br.com.stock.server.lib.crypto.extensions.decodeBase64
import br.com.stock.server.repository.image.FileRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Repository
import java.io.File
import java.io.FileOutputStream

@Repository
@ConditionalOnProperty(prefix = "app.images", name = ["repository"], havingValue = "local")
class LocalFileRepository(
    @Value("\${app.images.repository.path:/var/tmp/escolanamao}") private val directoryPath: String
): FileRepository {

    override val bucketName: String = ""

    override fun save(externalIdType: String, externalId: String, image: FileData): String {
        val basePath = "$directoryPath/$externalIdType/$externalId/categories/${image.category}/resources"
        val filePath = "$basePath/${image.fileName}"
        return runCatching {
            File(basePath).takeIf { !it.exists() }?.also { it.mkdirs() }
            FileOutputStream(filePath).use { stream -> stream.write(image.base64.decodeBase64()) }
        }.getOrElse {
            throw ApiException(
                "Error when try to save image to $filePath [message=${it.message}]",
                "Não foi possível salvar a imagem",
                HttpStatus.BAD_GATEWAY,
                it
            )
        }.let { filePath }
    }

    override fun remove(fileUrl: String) {
        File(fileUrl).deleteOnExit()
    }
}