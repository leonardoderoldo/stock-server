package br.com.stock.server.repository.partner.restaurant

import br.com.stock.server.domain.entity.partner.restaurant.Restaurant
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RestaurantRepository: JpaRepository<Restaurant, Long> {

    fun findByCnpj(cnpj: String): Restaurant?

    fun getByExternalId(externalId: String): Restaurant

}