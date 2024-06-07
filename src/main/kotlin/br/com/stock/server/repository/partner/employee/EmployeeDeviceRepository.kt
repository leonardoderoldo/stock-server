package br.com.stock.server.repository.partner.employee

import br.com.stock.server.domain.entity.partner.employee.Employee
import br.com.stock.server.domain.entity.partner.employee.EmployeeDevice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EmployeeDeviceRepository: JpaRepository<EmployeeDevice, Long> {

    fun findByEmployeeAndPushCode(employee: Employee, gcmToken: String): EmployeeDevice?

}