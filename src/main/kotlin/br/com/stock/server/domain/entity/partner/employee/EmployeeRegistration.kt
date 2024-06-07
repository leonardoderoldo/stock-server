package br.com.stock.server.domain.entity.partner.employee

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
@Table(name = "EMPLOYEE_REGISTRATION")
class EmployeeRegistration(

    @ManyToOne
    @JoinColumn(name = "EMPLOYEE_ID")
    val employee: Employee,

    @Column(name = "CODE")
    val code: String

) {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    var status: EmployeeRegistrationStatus = EmployeeRegistrationStatus.PENDING

    @CreationTimestamp
    @Column(name = "CREATED_AT")
    val createdAt: OffsetDateTime = OffsetDateTime.now()

    @UpdateTimestamp
    @Column(name = "UPDATED_AT")
    var updatedAt: OffsetDateTime = OffsetDateTime.now()

}

enum class EmployeeRegistrationStatus {
    PENDING,
    VALIDATED,
}