package br.com.stock.server.repository.partner.employee

import br.com.stock.server.domain.entity.partner.employee.EmployeeRegistration
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface EmployeeRegistrationRepository: JpaRepository<EmployeeRegistration, Long> {

    @Query("select er from EmployeeRegistration er where er.employee.email = :email and er.employee.status = 'TO_ACTIVATE'")
    fun findByEmployee(email: String): List<EmployeeRegistration>

}