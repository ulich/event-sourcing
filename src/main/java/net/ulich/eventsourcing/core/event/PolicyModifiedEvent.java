package net.ulich.eventsourcing.core.event;

import net.ulich.eventsourcing.api.dto.PolicyMtaRequest;
import lombok.Getter;

@Getter
public class PolicyModifiedEvent extends PolicyEvent {

    private final int apartmentSize;

    public PolicyModifiedEvent(String policyId, PolicyMtaRequest mtaRequest) {
        super(policyId);
        this.apartmentSize = mtaRequest.getApartmentSize();
    }
}
