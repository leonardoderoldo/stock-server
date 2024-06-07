package br.com.stock.server.service.workflow.impl

import br.com.stock.server.domain.workflow.CUSTOMER_ADD_PHONE_QUALIFIER
import br.com.stock.server.domain.workflow.WorkflowStep
import br.com.stock.server.lib.security.UserAuthentication
import br.com.stock.server.service.customer.CustomerService
import br.com.stock.server.service.workflow.AbstractWorkflowStepService
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.stereotype.Service

@Service(CUSTOMER_ADD_PHONE_QUALIFIER)
class WorkflowStepCustomerAddPhoneService(
    meter: MeterRegistry,
    private val customerService: CustomerService
) : AbstractWorkflowStepService(meter) {

    override fun getStep(userAuthentication: UserAuthentication): WorkflowStep? = observe("getStep") {
//        val notNecessaryStep = userAuthentication.authorities.none { authority -> WorkflowStep.CUSTOMER_ADD_PHONE.roles.contains(authority.authority) }
//        if (notNecessaryStep) return@observe null
//
//        if (customerService.getPhones(userAuthentication).isEmpty()) {
//            WorkflowStep.CUSTOMER_ADD_PHONE
//        } else {
            null
//        }
    }
}