package br.com.stock.server.repository.partner.employee

import br.com.stock.server.domain.entity.partner.employee.EmployeeCredential
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface EmployeeCredentialRepository: JpaRepository<EmployeeCredential, Long> {

    @Query("select c from EmployeeCredential c where c.employee.id = :employeeId and c.deletedAt is null")
    fun findCurrentCredential(employeeId: Long): EmployeeCredential

}