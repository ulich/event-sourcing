package net.ulich.eventsourcing.core.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.ulich.eventsourcing.api.dto.CancelPolicyRequest;
import net.ulich.eventsourcing.core.domain.Policy;
import net.ulich.eventsourcing.core.domain.PolicyState;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PolicyCanceledEvent extends PolicyEvent {

    private LocalDate coverEndDate;

    public PolicyCanceledEvent(String policyId, CancelPolicyRequest cancelRequest) {
        this.policyId = policyId;
        this.coverEndDate = cancelRequest.getCoverEndDate();
    }

    @Override
    public Policy applyTo(Policy policy) {
        if (policy.getId() == null) {
            throw new IllegalStateException("No policy found");
        }

        if (policy.getState() != PolicyState.ACTIVE) {
            throw new IllegalStateException("Only active policies can be canceled");
        }

        if (coverEndDate.isBefore(policy.getCoverStartDate())) {
            throw new IllegalArgumentException("Cover end date may not before start date");
        }

        policy.setState(PolicyState.CANCELED);
        policy.setCoverEndDate(coverEndDate);

        return policy;
    }
}
