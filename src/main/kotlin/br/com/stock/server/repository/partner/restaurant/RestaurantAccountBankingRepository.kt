package br.com.stock.server.repository.partner.restaurant

import br.com.stock.server.domain.entity.partner.restaurant.RestaurantAccountBanking
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface RestaurantAccountBankingRepository: JpaRepository<RestaurantAccountBanking, Long> {

    @Query("select a from RestaurantAccountBanking a where a.restaurant.externalId = :restaurantId")
    fun findByRestaurant(restaurantId: String): RestaurantAccountBanking?

}