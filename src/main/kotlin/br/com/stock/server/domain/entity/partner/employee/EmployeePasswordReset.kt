package br.com.stock.server.domain.entity.partner.employee

import br.com.stock.server.domain.entity.partner.employee.Employee
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.OffsetDateTime

@Entity
@Table(name = "EMPLOYEE_PASSWORD_RESET")
class EmployeePasswordReset(

    @ManyToOne
    @JoinColumn(name = "EMPLOYEE_ID")
    val employee: Employee,

    @Column(name = "CODE")
    var code: String

) {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    var status: EmployeePasswordResetStatus = EmployeePasswordResetStatus.PENDING

    @CreationTimestamp
    @Column(name = "CREATED_AT")
    val createdAt: OffsetDateTime = OffsetDateTime.now()

    @UpdateTimestamp
    @Column(name = "UPDATED_AT")
    var updatedAt: OffsetDateTime = OffsetDateTime.now()
}

enum class EmployeePasswordResetStatus {
    PENDING,
    VALIDATED,
}