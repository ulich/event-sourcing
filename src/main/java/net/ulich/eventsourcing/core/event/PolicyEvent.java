package net.ulich.eventsourcing.core.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.ulich.eventsourcing.core.domain.Policy;

@AllArgsConstructor
@Getter
public abstract class PolicyEvent {

    protected final String policyId;

    public abstract Policy applyTo(Policy policy);
}
