package br.com.stock.server.domain.entity.customer

import br.com.stock.server.domain.entity.customer.Customer
import br.com.stock.server.domain.wallet.WalletCardBrand
import de.huxhorn.sulky.ulid.ULID
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.OffsetDateTime

@Entity
@Table(name = "WALLETS")
class Wallet(

    @ManyToOne
    @JoinColumn(name = "CUSTOMER_ID")
    var customer: Customer,

    @Column(name = "CARD_NUMBER")
    val number: String,

    @Column(name = "CARD_NUMBER_SUFFIX")
    val numberSufix: String,

    @Column(name = "CARD_HOLDER")
    val holder: String,

    @Column(name = "CARD_BRAND")
    @Enumerated(EnumType.STRING)
    val brand: WalletCardBrand,

    @Column(name = "CARD_HOLDER_CPF")
    val holderCpf: String,

    @Column(name = "CARD_EXPIRE_MONTH")
    val month: Int,

    @Column(name = "CARD_EXPIRE_YEAR")
    val year: Int,

    @Column(name = "CARD_CVC")
    val cvc: String,

    @Column(name = "MAIN")
    var main: Boolean = false

) {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @Column(name = "EXTERNAL_ID")
    val externalId: String = ULID().nextULID()

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    var status: WalletStatus = WalletStatus.ACTIVE

    @CreationTimestamp
    @Column(name = "CREATED_AT")
    val createdAt: OffsetDateTime = OffsetDateTime.now()

    @UpdateTimestamp
    @Column(name = "UPDATED_AT")
    val updatedAt: OffsetDateTime = createdAt

}

enum class WalletStatus {
    ACTIVE,
    INACTIVE,
}