package br.com.stock.server.service.partner.device

import br.com.stock.server.domain.entity.partner.employee.Employee
import br.com.stock.server.domain.entity.partner.employee.EmployeeDevice
import br.com.stock.server.lib.observability.Observable
import br.com.stock.server.repository.partner.employee.EmployeeDeviceRepository
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.stereotype.Service


@Service
class EmployeeDeviceService(
    meter: MeterRegistry,
    private val employeeDeviceRepository: EmployeeDeviceRepository
): Observable(meter) {

    fun save(employee: Employee, gcmToken: String): EmployeeDevice
        = observe("save") { logger ->
        employeeDeviceRepository.findByEmployeeAndPushCode(employee, gcmToken)
            ?.let { device ->
                logger.info { "Device employee registered by [gcmToken=${gcmToken}]" }
                employeeDeviceRepository.save(device)
            } ?: employeeDeviceRepository.save(EmployeeDevice(employee, gcmToken))
    }

}