package net.ulich.eventsourcing.core.event;

import net.ulich.eventsourcing.api.dto.CreatePolicyRequest;
import lombok.Getter;

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
}
