package br.com.stock.server.domain.entity.customer

import br.com.stock.server.domain.entity.customer.Customer
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.OffsetDateTime

@Entity
@Table(name = "CUSTOMER_PASSWORD_RESET")
class CustomerPasswordReset(
    @ManyToOne
    @JoinColumn(name = "CUSTOMER_ID")
    val customer: Customer,

    @Column(name = "CODE")
    var code: String,
) {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    var status: CustomerPasswordResetStatus = CustomerPasswordResetStatus.PENDING

    @CreationTimestamp
    @Column(name = "CREATED_AT")
    val createdAt: OffsetDateTime = OffsetDateTime.now()

    @UpdateTimestamp
    @Column(name = "UPDATED_AT")
    var updatedAt: OffsetDateTime = OffsetDateTime.now()
}

enum class CustomerPasswordResetStatus {
    PENDING,
    VALIDATED,
}