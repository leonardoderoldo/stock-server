package br.com.stock.server.service.workflow.impl

import br.com.stock.server.domain.workflow.ACCEPT_RESTAURANT_TERMS_QUALIFIER
import br.com.stock.server.domain.workflow.WorkflowStep
import br.com.stock.server.extentions.toTermVersion
import br.com.stock.server.lib.security.UserAuthentication
import br.com.stock.server.service.partner.restaurant.RestaurantService
import br.com.stock.server.service.workflow.AbstractWorkflowStepService
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service(ACCEPT_RESTAURANT_TERMS_QUALIFIER)
class WorkflowStepAcceptRestaurantTermService(
    meter: MeterRegistry,
    private val restaurantService: RestaurantService,
    @Value("\${app.terms.restaurant.version}") private val termVersion: String
) : AbstractWorkflowStepService(meter) {

    override fun getStep(userAuthentication: UserAuthentication): WorkflowStep? = observe("getStep") { _ ->
        val notNecessaryStep = userAuthentication.authorities.none { authority -> WorkflowStep.ACCPECT_RESTAURANT_TERMS.roles.contains(authority.authority) }
        if (notNecessaryStep) return@observe null

        onExistingRestaurantId(userAuthentication) { restaurantId ->
            if (needAcceptTerm(termVersion, restaurantService.getByExternalId(restaurantId).termVersion)) {
                WorkflowStep.ACCPECT_RESTAURANT_TERMS
            } else {
                null
            }
        }
    }

    private fun needAcceptTerm(currentTermVersion: String, restaurantAcceptedTermVersion: String?): Boolean = observe("needAcceptTerm") { _ ->
        restaurantAcceptedTermVersion?.let {
            currentTermVersion.toTermVersion() > it.toTermVersion()
        } ?: true
    }
}