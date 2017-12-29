package net.ulich.eventsourcing.api;

import lombok.AllArgsConstructor;
import net.ulich.eventsourcing.api.dto.CancelPolicyRequest;
import net.ulich.eventsourcing.api.dto.CreatePolicyRequest;
import net.ulich.eventsourcing.api.dto.PolicyMtaRequest;
import net.ulich.eventsourcing.core.PolicyService;
import net.ulich.eventsourcing.core.PolicyServiceBean;
import net.ulich.eventsourcing.core.domain.Policy;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
public class PolicyController {

    private final PolicyService policyService;

    @PostMapping("/policies")
    public Policy createPolicy(@RequestBody CreatePolicyRequest createRequest) {
        return policyService.createPolicy(createRequest);
    }

    @GetMapping("/policies/{policyId}")
    public Policy getPolicy(@PathVariable String policyId, @RequestParam(required = false) Integer version) {
        return policyService.getPolicy(policyId, version);
    }

    @PutMapping("/policies/{policyId}")
    public Policy modifyPolicy(@PathVariable String policyId, @RequestBody PolicyMtaRequest mtaRequest) {
        return policyService.modifyPolicy(policyId, mtaRequest);
    }

    @PostMapping("/policies/{policyId}/cancellation")
    public Policy cancelPolicy(@PathVariable String policyId, @RequestBody CancelPolicyRequest cancelRequest) {
        return policyService.cancelPolicy(policyId, cancelRequest);
    }
}
