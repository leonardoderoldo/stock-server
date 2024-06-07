package br.com.stock.server.service.workflow.impl

import br.com.stock.server.domain.workflow.CREATE_RESTAURANT_ACCOUNT_BANKING_QUALIFIER
import br.com.stock.server.domain.workflow.WorkflowStep
import br.com.stock.server.lib.security.UserAuthentication
import br.com.stock.server.service.partner.restaurant.RestaurantService
import br.com.stock.server.service.workflow.AbstractWorkflowStepService
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.stereotype.Service

@Service(CREATE_RESTAURANT_ACCOUNT_BANKING_QUALIFIER)
class WorkflowStepRestaurantAccountBankingService(
    meter: MeterRegistry,
    private val restaurantService: RestaurantService
): AbstractWorkflowStepService(meter) {

    override fun getStep(userAuthentication: UserAuthentication): WorkflowStep? = observe("getStep") { _ ->
        val notNecessaryStep = userAuthentication.authorities.none { authority -> WorkflowStep.CREATE_RESTAURANT_ACCOUNT_BANKING.roles.contains(authority.authority) }
        if (notNecessaryStep) return@observe null

        onExistingRestaurantId(userAuthentication) { restaurantId ->
            if (restaurantService.getAccountBanking(restaurantId) == null) {
                WorkflowStep.CREATE_RESTAURANT_ACCOUNT_BANKING
            } else {
                null
            }
        }
    }

}