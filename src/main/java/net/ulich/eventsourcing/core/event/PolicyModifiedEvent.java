package net.ulich.eventsourcing.core.event;

import net.ulich.eventsourcing.api.dto.PolicyMtaRequest;
import lombok.Getter;
import net.ulich.eventsourcing.core.domain.Policy;
import net.ulich.eventsourcing.core.domain.PolicyState;

@Getter
public class PolicyModifiedEvent extends PolicyEvent {

    private final int apartmentSize;

    public PolicyModifiedEvent(String policyId, PolicyMtaRequest mtaRequest) {
        super(policyId);
        this.apartmentSize = mtaRequest.getApartmentSize();
    }

    @Override
    public Policy applyTo(Policy policy) {
        if (policy.getId() == null) {
            throw new IllegalStateException("No policy found");
        }

        if (policy.getState() != PolicyState.ACTIVE) {
            throw new IllegalStateException("Only active policies can be modified");
        }

        policy.setApartmentSize(apartmentSize);

        return policy;
    }
}
