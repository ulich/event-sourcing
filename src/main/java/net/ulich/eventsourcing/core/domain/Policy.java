package net.ulich.eventsourcing.core.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import net.ulich.eventsourcing.core.event.PolicyEvent;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
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
            applyEvent(event);
        }
    }

    public void applyEvent(PolicyEvent event) {
        event.applyTo(this);
    }


    public boolean isActive() {
        return state.equals(PolicyState.ACTIVE);
    }
}
