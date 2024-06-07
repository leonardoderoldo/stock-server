package br.com.stock.server.domain.entity.partner.restaurant

import de.huxhorn.sulky.ulid.ULID
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
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
@Table(name = "RESTAURANT_ACCOUNT_BANKING")
class RestaurantAccountBanking(

    @OneToOne
    @JoinColumn(name = "RESTAURANT_ID")
    val restaurant: Restaurant,

    @Column(name = "BANK_CODE")
    var bankCode: String,

    @Column(name = "AGENCY")
    var agency: String,

    @Column(name = "ACCOUNT_NUMBER")
    var accountNumber: String,

    @Column(name = "VERIFYING_DIGIT")
    var verifyingDigit: String,

) {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @Column(name = "EXTERNAL_ID")
    val externalId: String = ULID().nextULID()

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    var status: RestaurantAccountBankingStatus = RestaurantAccountBankingStatus.ACTIVE

    @CreationTimestamp
    @Column(name = "CREATED_AT")
    val createdAt: OffsetDateTime = OffsetDateTime.now()

    @UpdateTimestamp
    @Column(name = "UPDATED_AT")
    val updatedAt: OffsetDateTime = OffsetDateTime.now()

}

enum class RestaurantAccountBankingStatus {
    ACTIVE,
    DELETED
}