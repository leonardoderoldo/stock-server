package br.com.stock.server.repository.partner.restaurant

import br.com.stock.server.domain.entity.partner.restaurant.RestaurantAddress
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface RestaurantAddressRepository: JpaRepository<RestaurantAddress, Long> {

    @Query("select a from RestaurantAddress a where a.restaurant.externalId = :restaurantId order by a.name asc")
    fun findByRestaurant(restaurantId: String): RestaurantAddress?

}