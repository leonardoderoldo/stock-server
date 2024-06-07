package br.com.stock.server.domain.entity.partner.catalog

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
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.OffsetDateTime

@Entity
@Table(name = "OPTION_GROUP_PRODUCT")
class OptionGroupProduct(

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID")
    val product: Product,

    @ManyToOne
    @JoinColumn(name = "OPTION_GROUP_ID")
    val optionGroup: OptionGroup,

) {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @Column(name = "EXTERNAL_ID")
    val externalId: String = ULID().nextULID()

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    var status: OptionGroupProductStatus = OptionGroupProductStatus.ACTIVE

    @CreationTimestamp
    @Column(name = "CREATED_AT")
    val createdAt: OffsetDateTime = OffsetDateTime.now()

    @UpdateTimestamp
    @Column(name = "UPDATED_AT")
    val updatedAt: OffsetDateTime = OffsetDateTime.now()


    fun isActive() = status == OptionGroupProductStatus.ACTIVE && product.status == ProductStatus.AVAILABLE

}


enum class OptionGroupProductStatus {
    ACTIVE,
    INACTIVE,
}
