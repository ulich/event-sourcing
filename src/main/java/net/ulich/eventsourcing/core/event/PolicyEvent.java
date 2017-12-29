package net.ulich.eventsourcing.core.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.ulich.eventsourcing.core.domain.Policy;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Data
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = PolicyCreatedEvent.class, name = "PolicyCreatedEvent"),
        @JsonSubTypes.Type(value = PolicyCanceledEvent.class, name = "PolicyCanceledEvent"),
        @JsonSubTypes.Type(value = PolicyModifiedEvent.class, name = "PolicyModifiedEvent"),
})
public class PolicyEvent {

    protected String policyId;

    public Policy applyTo(Policy policy) {
        return policy;
    };
}

