package br.com.stock.server.domain.entity.partner.catalog

import br.com.stock.server.domain.entity.partner.restaurant.Restaurant
import de.huxhorn.sulky.ulid.ULID
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.OffsetDateTime

@Entity
@Table(name = "CATALOG")
class Catalog(

    @Column(name = "NAME")
    var name: String = "Default",

    @OneToOne
    @JoinColumn(name = "RESTAURANT_ID")
    val restaurant: Restaurant,

) {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @Column(name = "EXTERNAL_ID")
    val externalId: String = ULID().nextULID()

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    var status: CatalogStatus = CatalogStatus.ACTIVE

    @CreationTimestamp
    @Column(name = "CREATED_AT")
    val createdAt: OffsetDateTime = OffsetDateTime.now()

    @UpdateTimestamp
    @Column(name = "UPDATED_AT")
    val updatedAt: OffsetDateTime = createdAt

}


enum class CatalogStatus {
    ACTIVE,
    DELETED,
}