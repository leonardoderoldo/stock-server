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
import jakarta.persistence.Table
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Digits
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.math.BigDecimal
import java.time.OffsetDateTime

@Entity
@Table(name = "PRODUCT")
class Product(

    @Column(name = "NAME")
    var name: String,

    @Column(name = "DESCRIPTION")
    var description: String,

    @Column(name = "IMAGE_PATH")
    val imagePath: String? = null,

    @DecimalMin(value = "0.1", inclusive = false)
    @Digits(integer=5, fraction=2)
    @Column(name = "PRICE")
    val price: BigDecimal,

    @Column(name = "SERVING")
    @Enumerated(EnumType.STRING)
    var serving: ServingType = ServingType.NOT_APPLICABLE,

    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID")
    val category: Category,

//    val dietaryRestrictions: List<DietaryRestrictionsType> = emptyList()

) {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @Column(name = "EXTERNAL_ID")
    val externalId: String = ULID().nextULID()

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    var status: ProductStatus = ProductStatus.AVAILABLE

    @CreationTimestamp
    @Column(name = "CREATED_AT")
    val createdAt: OffsetDateTime = OffsetDateTime.now()

    @UpdateTimestamp
    @Column(name = "UPDATED_AT")
    val updatedAt: OffsetDateTime = createdAt

}

enum class ProductStatus {
    AVAILABLE,
    PAUSED,
    DELETED,
}

enum class DietaryRestrictionsType {
    ALCOHOLIC_DRINK,
}

enum class ServingType {
    NOT_APPLICABLE,
    SERVES_1,
    SERVES_2,
    SERVES_3,
    SERVES_4,
    SERVES_5,
    SERVES_6,
}