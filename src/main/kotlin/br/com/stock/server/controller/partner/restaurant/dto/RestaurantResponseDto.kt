package br.com.stock.server.controller.partner.restaurant.dto

import br.com.stock.server.domain.entity.partner.restaurant.Restaurant
import br.com.stock.server.service.image.FileService
import java.time.OffsetDateTime

data class RestaurantResponseDto(
    val externalId: String,
    val cnpj: String?,
    val companyName: String,
    val businessName: String?,
    val status: RestaurantStatusDto,
    val updatedAt: OffsetDateTime,
    val imageUrl: String?,
) {
    companion object {
        fun from(restaurant: Restaurant, fileService: FileService) = RestaurantResponseDto(
            externalId = restaurant.externalId,
            cnpj = restaurant.cnpj,
            companyName = restaurant.companyName,
            businessName = restaurant.businessName,
            status = RestaurantStatusDto.valueOf(restaurant.status.name),
            updatedAt = restaurant.updatedAt,
            imageUrl = fileService.getAddressToFileUrl(restaurant.imagePath)
        )
    }
}

enum class RestaurantStatusDto {
    ACTIVE,
    INACTIVE,
    PENDING,
}
