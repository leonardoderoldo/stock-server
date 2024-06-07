package br.com.stock.server.domain.entity.partner.restaurant

import br.com.stock.server.domain.entity.partner.employee.Employee
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
@Table(name = "RESTAURANT")
class Restaurant(

    @Column(name = "COMPANY_NAME")
    val companyName: String,

    @Column(name = "BUSINESS_NAME")
    var businessName: String? = null,

    @Column(name = "CNPJ")
    val cnpj: String? = null,

    @Column(name = "DDD")
    val ddd: String,

    @Column(name = "PHONE_NUMBER")
    val phoneNumber: String,

) {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @Column(name = "EXTERNAL_ID")
    val externalId: String = ULID().nextULID()

    @OneToOne(mappedBy = "restaurant")
    val address: RestaurantAddress? = null

    @OneToOne(mappedBy = "restaurant")
    val accountBanking: RestaurantAccountBanking? = null

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    var status: RestaurantStatus = RestaurantStatus.PENDING

    @Column(name = "IMAGE_PATH")
    var imagePath: String? = null

    @Column(name = "TERM_VERSION")
    var termVersion: String? = null

    @OneToOne
    @JoinColumn(name = "TERM_ACCEPTED_BY_EMPLOYEE_ID")
    var termAcceptedByEmployee: Employee? = null

    @CreationTimestamp
    @Column(name = "CREATED_AT")
    val createdAt: OffsetDateTime = OffsetDateTime.now()

    @UpdateTimestamp
    @Column(name = "UPDATED_AT")
    val updatedAt: OffsetDateTime = OffsetDateTime.now()

}

enum class RestaurantStatus {
    ACTIVE,
    INACTIVE,
    PENDING
}