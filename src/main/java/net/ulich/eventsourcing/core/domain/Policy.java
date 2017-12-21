package net.ulich.eventsourcing.core.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import net.ulich.eventsourcing.core.event.PolicyCanceledEvent;
import net.ulich.eventsourcing.core.event.PolicyCreatedEvent;
import net.ulich.eventsourcing.core.event.PolicyEvent;
import net.ulich.eventsourcing.core.event.PolicyModifiedEvent;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class Policy {

    private String id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate coverStartDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate coverEndDate = null;

    private PolicyState state;

    private int apartmentSize;


    public Policy(List<PolicyEvent> events) {
        for (PolicyEvent event : events) {
            apply(event);
        }
    }

    public void apply(PolicyEvent event) {
        if (event instanceof PolicyCreatedEvent) {
            apply((PolicyCreatedEvent) event);
        } else if (event instanceof PolicyModifiedEvent) {
            apply((PolicyModifiedEvent) event);
        } else if (event instanceof PolicyCanceledEvent) {
            apply((PolicyCanceledEvent) event);
        }
    }

    private void apply(PolicyCreatedEvent event) {
        if (id != null) {
            throw new IllegalStateException("A policy with this ID already exists");
        }

        id = event.getPolicyId();
        state = PolicyState.ACTIVE;
        coverStartDate = event.getCoverStartDate();
        apartmentSize = event.getApartmentSize();
    }

    private void apply(PolicyModifiedEvent event) {
        if (id == null) {
            throw new IllegalStateException("No policy found");
        }

        if (state != PolicyState.ACTIVE) {
            throw new IllegalStateException("Only active policies can be modified");
        }

        apartmentSize = event.getApartmentSize();
    }

    private void apply(PolicyCanceledEvent event) {
        if (id == null) {
            throw new IllegalStateException("No policy found");
        }

        if (state != PolicyState.ACTIVE) {
            throw new IllegalStateException("Only active policies can be canceled");
        }

        if (event.getCoverEndDate().isBefore(coverStartDate)) {
            throw new IllegalArgumentException("Cover end date may not before start date");
        }

        state = PolicyState.CANCELED;
        coverEndDate = event.getCoverEndDate();
    }
}
