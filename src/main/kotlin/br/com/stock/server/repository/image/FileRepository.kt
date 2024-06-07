package br.com.stock.server.repository.image

import br.com.stock.server.domain.image.FileData

interface FileRepository {

    val bucketName: String

    fun save(externalIdType: String, externalId: String, image: FileData): String

    fun remove(fileUrl: String)
}