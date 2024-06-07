package br.com.stock.server.domain.entity.partner.employee

import br.com.stock.server.lib.crypto.HashedData
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import java.time.OffsetDateTime

@Entity
@Table(name = "EMPLOYEE_CREDENTIAL")
class EmployeeCredential(

    @ManyToOne
    @JoinColumn(name = "EMPLOYEE_ID")
    val employee: Employee,

    data: HashedData

) {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @Column(name = "HASH")
    private val hash: String = data.hash

    @Column(name = "SALT")
    private val salt: String = data.iv

    @CreationTimestamp
    @Column(name = "CREATED_AT")
    val createdAt: OffsetDateTime = OffsetDateTime.now()

    @Column(name = "DELETED_AT")
    var deletedAt: OffsetDateTime? = null

    fun getData() = HashedData(hash, salt)

}