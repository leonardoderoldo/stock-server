package br.com.stock.server.domain.entity.customer

import br.com.stock.server.domain.entity.customer.Customer
import br.com.stock.server.domain.phone.PhoneType
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
@Table(name = "CUSTOMER_PHONE")
class CustomerPhone(

    @ManyToOne
    @JoinColumn(name = "CUSTOMER_ID")
    val customer: Customer,

    @Column(name = "DDD")
    var ddd: String,

    @Column(name = "NUMBER")
    var number: String,

    @Column(name = "PHONE_TYPE")
    @Enumerated(EnumType.STRING)
    var type: PhoneType,

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
    var updatedAt: OffsetDateTime = OffsetDateTime.now()

    @Column(name = "DELETED_AT")
    var deletedAt: OffsetDateTime? = null

}