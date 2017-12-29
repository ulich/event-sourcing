package net.ulich.eventsourcing.core.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.ulich.eventsourcing.api.dto.CreatePolicyRequest;
import net.ulich.eventsourcing.core.domain.Policy;
import net.ulich.eventsourcing.core.domain.PolicyState;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PolicyCreatedEvent extends PolicyEvent {

    private LocalDate coverStartDate;

    private int apartmentSize;

    public PolicyCreatedEvent(String policyId, CreatePolicyRequest createRequest) {
        this.policyId = policyId;
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
