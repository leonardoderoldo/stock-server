package br.com.stock.server.service.image

import br.com.stock.server.domain.entity.customer.Customer
import br.com.stock.server.domain.entity.partner.employee.Employee
import br.com.stock.server.domain.entity.partner.restaurant.Restaurant
import br.com.stock.server.domain.image.FileData
import br.com.stock.server.lib.observability.Observable
import br.com.stock.server.repository.image.FileRepository
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.stereotype.Service

@Service
class FileService(
    meter: MeterRegistry,
    private val fileRepository: FileRepository
): Observable(meter) {

    private final val bucketDns = if (fileRepository.bucketName.isNotBlank()) {
        "https://${fileRepository.bucketName}"
    } else {
        ""
    }

    fun getAddressToFileUrl(fileUrl: String?): String? = observe("getAddressToFileUrl") { _ ->
        fileUrl?.let { "${bucketDns}$it" }
    }

    fun remove(fileUrl: String?) {
        fileUrl?.also { fileRepository.remove(it) }
    }

    fun saveToRestaurant(restaurant: Restaurant, image: FileData): String = observe("saveToRestaurant") { _ ->
        fileRepository.save("restaurants", restaurant.externalId, image)
    }

    fun saveToEmployee(employee: Employee, image: FileData): String = observe("saveToEmployee") { _ ->
        fileRepository.save("employees", employee.externalId, image)
    }

    fun saveToCustomer(customer: Customer, image: FileData): String = observe("saveToCustomer") { _ ->
        fileRepository.save("customers", customer.externalId, image)
    }

}