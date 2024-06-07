package br.com.stock.server.service.workflow.impl

import br.com.stock.server.domain.workflow.ANSWER_INVITE_QUALIFIER
import br.com.stock.server.domain.workflow.WorkflowStep
import br.com.stock.server.lib.security.UserAuthentication
import br.com.stock.server.service.workflow.AbstractWorkflowStepService
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.stereotype.Service

@Service(ANSWER_INVITE_QUALIFIER)
class WorkflowStepCustomerAnswerInviteService(
    meter: MeterRegistry,
//    private val customerService: CustomerService
) : AbstractWorkflowStepService(meter) {

    override fun getStep(userAuthentication: UserAuthentication): WorkflowStep? = observe("getStep") {
//        val notNecessaryStep = userAuthentication.authorities.none { authority -> WorkflowStep.ANSWER_INVITE.roles.contains(authority.authority) }
//        if (notNecessaryStep) return@observe null
//
//        if (customerService.getInvites(userAuthentication).isNotEmpty()) {
//            WorkflowStep.ANSWER_INVITE
//        } else {
            null
//        }
    }
}