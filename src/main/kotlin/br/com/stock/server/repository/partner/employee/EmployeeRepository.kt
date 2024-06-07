package br.com.stock.server.repository.partner.employee

import br.com.stock.server.domain.entity.partner.employee.Employee
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface EmployeeRepository: JpaRepository<Employee, Long> {

    @Query("select e from Employee e where e.email = :email and e.status = 'ACTIVE' and e.restaurant.externalId = :restaurantId")
    fun findActiveEmployee(email: String, restaurantId: String): Employee?

    @Query("select e from Employee e where e.email = :email and e.status = 'ACTIVE'")
    fun findActiveEmployee(email: String): Employee?

    fun getByExternalId(externalId: String): Employee

    fun findByCpf(cpf: String): Employee?

}