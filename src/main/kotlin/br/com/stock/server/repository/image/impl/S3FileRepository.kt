package br.com.stock.server.repository.image.impl

import br.com.stock.server.domain.exception.ApiException
import br.com.stock.server.domain.image.FileData
import br.com.stock.server.lib.crypto.extensions.decodeBase64
import br.com.stock.server.lib.observability.Observable
import br.com.stock.server.repository.image.FileRepository
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.Headers
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Repository

@Repository
@ConditionalOnProperty(prefix = "app.images", name = ["repository"], havingValue = "s3")
class S3FileRepository(
    meter: MeterRegistry,
    private val amazonS3Client: AmazonS3,
    @Value("\${app.environment}") private val environmentName: String,
    @Value("\${app.domain.name}") private val domainName: String,
): Observable(meter), FileRepository {

    override val bucketName: String = "image-bucket.$environmentName.$domainName"

    override fun save(externalIdType: String, externalId: String, image: FileData): String = observe("save") { _ ->
        val bucketRelativePath = "/$externalIdType/$externalId/categories/${image.category}/resources"
        val bucketPath = "${bucketName}$bucketRelativePath"
        runCatching {
            val imageBytes = image.base64.decodeBase64()
            val putObjectRequest = PutObjectRequest(
                bucketPath,
                image.fileName,
                imageBytes.inputStream(),
                ObjectMetadata().apply {
                    contentType = image.contentType
                    contentLength = imageBytes.size.toLong()
                    userMetadata[Headers.CONTENT_TYPE] = image.contentType
                    userMetadata[Headers.CONTENT_LENGTH] = imageBytes.size.toString()
                }
            ).withCannedAcl(CannedAccessControlList.PublicRead)
            amazonS3Client.putObject(putObjectRequest)
        }.getOrElse {
            throw ApiException(
                "Error when try to save image to $bucketPath/${image.fileName} [message=${it.message}]",
                "Não foi possível salvar a imagem",
                HttpStatus.BAD_GATEWAY,
                it
            )
        }.let { "$bucketRelativePath/${image.fileName}" }
    }

    override fun remove(fileUrl: String) {
        amazonS3Client.deleteObject(bucketName, fileUrl.replace("${bucketName}/", ""))
    }
}