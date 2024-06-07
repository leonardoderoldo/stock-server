package br.com.stock.server.service.workflow

import br.com.stock.server.domain.workflow.WorkflowStep
import br.com.stock.server.lib.observability.Observable
import br.com.stock.server.lib.security.UserAuthentication
import io.micrometer.core.instrument.MeterRegistry

interface WorkflowStepService {

    fun getStep(userAuthentication: UserAuthentication): WorkflowStep?
}

abstract class AbstractWorkflowStepService(
    meter: MeterRegistry
): Observable(meter), WorkflowStepService {
    fun onExistingRestaurantId(userAuthentication: UserAuthentication, block: (restaurantId: String) -> WorkflowStep?): WorkflowStep? {
        return if (userAuthentication.restaurantId != null) {
            block(userAuthentication.restaurantId!!)
        } else {
            null
        }
    }
}