package br.com.stock.server.domain.entity.partner.restaurant

import de.huxhorn.sulky.ulid.ULID
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.OffsetDateTime

@Entity
@Table(name = "RESTAURANT_ADDRESS")
class RestaurantAddress(

    @OneToOne
    @JoinColumn(name = "RESTAURANT_ID")
    val restaurant: Restaurant,

    @Column(name = "ALIAS_NAME")
    var name: String,

    @Column(name = "STREET")
    var street: String,

    @Column(name = "NUMBER")
    var number: String?,

    @Column(name = "COMPLEMENT")
    var complement: String?,

    @Column(name = "CEP")
    var cep: String,

    @Column(name = "NEIBORHOOD")
    var neiborhood: String,

    @Column(name = "STATE")
    var state: String,

    @Column(name = "CITY")
    var city: String,

) {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @Column(name = "EXTERNAL_ID")
    val externalId: String = ULID().nextULID()

    @CreationTimestamp
    @Column(name = "CREATED_AT")
    val createdAt: OffsetDateTime = OffsetDateTime.now()

    @UpdateTimestamp
    @Column(name = "UPDATED_AT")
    val updatedAt: OffsetDateTime = OffsetDateTime.now()

}