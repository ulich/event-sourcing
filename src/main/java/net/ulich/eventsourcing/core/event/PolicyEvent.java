package net.ulich.eventsourcing.core.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public abstract class PolicyEvent {

    private final String policyId;
}
