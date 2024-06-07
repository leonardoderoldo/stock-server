package br.com.stock.server.domain.entity.customer

import br.com.stock.server.domain.entity.customer.Customer
import jakarta.persistence.Column
import jakarta.persistence.Entity
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
@Table(name = "CUSTOMER_DEVICE")
class CustomerDevice(

    @ManyToOne
    @JoinColumn(name = "CUSTOMER_ID")
    val customer: Customer,

    @Column(name = "PUSH_CODE")
    val pushCode: String,

) {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @CreationTimestamp
    @Column(name = "CREATED_AT")
    val createdAt: OffsetDateTime = OffsetDateTime.now()

    @UpdateTimestamp
    @Column(name = "UPDATED_AT")
    val updatedAt: OffsetDateTime = createdAt

}