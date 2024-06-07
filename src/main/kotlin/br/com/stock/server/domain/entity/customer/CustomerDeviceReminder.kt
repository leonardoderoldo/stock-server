package br.com.stock.server.domain.entity.customer

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDate
import java.time.OffsetDateTime

@Entity
@Table(name = "CUSTOMER_DEVICE_REMINDER")
class CustomerDeviceReminder(

    @ManyToOne
    @JoinColumn(name = "CUSTOMER_DEVICE_ID")
    val customerDevice: CustomerDevice,

    @CreationTimestamp
    @Column(name = "SHIPPING_AT")
    val shippingAt: LocalDate = LocalDate.now()

) {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @CreationTimestamp
    @Column(name = "CREATED_AT")
    val createdAt: OffsetDateTime = OffsetDateTime.now()

}