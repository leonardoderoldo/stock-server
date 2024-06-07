package br.com.stock.server.service.customer

import br.com.stock.server.lib.observability.Observable
import br.com.stock.server.lib.security.UserAuthentication
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.stereotype.Service

@Service
class CustomerService(
    meter: MeterRegistry,
): Observable(meter) {

}