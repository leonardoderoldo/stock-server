package br.com.stock.server.domain.entity.customer

import br.com.stock.server.domain.entity.customer.Wallet
import de.huxhorn.sulky.ulid.ULID
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDate
import java.time.OffsetDateTime

@Entity
@Table(name = "CUSTOMER")
class Customer(

    @Column(name = "FIRST_NAME")
    val firstName: String,

    @Column(name = "LAST_NAME")
    val lastName: String,

    @Column(name = "CPF")
    val cpf: String,

    @Column(name = "EMAIL")
    val email: String,

    @Column(name = "BIRTH_DATE")
    val birthDate: LocalDate,

)  {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @Column(name = "EXTERNAL_ID")
    val externalId: String = ULID().nextULID()

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    var status: CustomerStatus = CustomerStatus.TO_ACTIVATE

    @Column(name = "IMAGE_URL")
    var imageUrl: String? = null

    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER)
    val devices: List<CustomerDevice> = emptyList()

    @OneToMany(mappedBy = "customer")
    val wallets: List<Wallet> = emptyList()

    @CreationTimestamp
    @Column(name = "CREATED_AT")
    val createdAt: OffsetDateTime = OffsetDateTime.now()

    @UpdateTimestamp
    @Column(name = "UPDATED_AT")
    val updatedAt: OffsetDateTime = OffsetDateTime.now()
}

enum class CustomerStatus {
    ACTIVE,
    TO_ACTIVATE,
    INACTIVE,
}