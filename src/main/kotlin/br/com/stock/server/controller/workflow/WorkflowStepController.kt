package br.com.stock.server.controller.workflow

import br.com.stock.server.domain.workflow.WorkflowStep
import br.com.stock.server.lib.observability.Observable
import br.com.stock.server.lib.security.UserAuthentication
import br.com.stock.server.service.workflow.WorkflowStepService
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/workflows/steps"])
class WorkflowStepController(
    meter: MeterRegistry,
    private val workflowStepServices: List<WorkflowStepService>
): Observable(meter) {

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_USER')")
    fun nextSteps(
        userAuthentication: UserAuthentication,
    ): ResponseEntity<List<WorkflowStep>> = loggable(journey("nextSteps")) { logger ->
        workflowStepServices.mapNotNull { it.getStep(userAuthentication) }
            .takeIf { it.isNotEmpty() }
            ?.sortedBy { it.ordinal }
            ?.also { logger.info { "Success to get next steps to employeeId=${userAuthentication.id} on restaurantId=${userAuthentication.restaurantId}" } }
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.noContent().build<List<WorkflowStep>>()
                .also { logger.info { "Success to get empty next steps to employeeId=${userAuthentication.id} on restaurantId=${userAuthentication.restaurantId}" } }
    }
}