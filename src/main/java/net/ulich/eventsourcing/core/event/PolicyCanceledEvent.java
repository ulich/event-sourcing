package net.ulich.eventsourcing.core.event;

import net.ulich.eventsourcing.api.dto.CancelPolicyRequest;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class PolicyCanceledEvent extends PolicyEvent {

    private final LocalDate coverEndDate;

    public PolicyCanceledEvent(String policyId, CancelPolicyRequest cancelRequest) {
        super(policyId);
        this.coverEndDate = cancelRequest.getCoverEndDate();
    }
}
