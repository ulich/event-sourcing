package net.ulich.eventsourcing.core.event;

import net.ulich.eventsourcing.api.dto.CreatePolicyRequest;
import lombok.Getter;
import net.ulich.eventsourcing.core.domain.Policy;
import net.ulich.eventsourcing.core.domain.PolicyState;

import java.time.LocalDate;

@Getter
public class PolicyCreatedEvent extends PolicyEvent {

    private final LocalDate coverStartDate;
    private final int apartmentSize;

    public PolicyCreatedEvent(String id, CreatePolicyRequest createRequest) {
        super(id);
        this.coverStartDate = createRequest.getCoverStartDate();
        this.apartmentSize = createRequest.getApartmentSize();
    }

    @Override
    public Policy applyTo(Policy policy) {

        if (policy.getId() != null) {
            throw new IllegalStateException("A policy with this ID already exists");
        }

        policy.setId(policyId);
        policy.setState(PolicyState.ACTIVE);
        policy.setCoverStartDate(coverStartDate);
        policy.setCoverEndDate(coverStartDate.plusYears(1l));
        policy.setApartmentSize(apartmentSize);

        return policy;
    }
}
