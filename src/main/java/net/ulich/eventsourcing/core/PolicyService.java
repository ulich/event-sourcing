package net.ulich.eventsourcing.core;

import net.ulich.eventsourcing.api.dto.CancelPolicyRequest;
import net.ulich.eventsourcing.api.dto.CreatePolicyRequest;
import net.ulich.eventsourcing.api.dto.PolicyMtaRequest;
import net.ulich.eventsourcing.core.domain.Policy;
import net.ulich.eventsourcing.core.event.PolicyCanceledEvent;
import net.ulich.eventsourcing.core.event.PolicyCreatedEvent;
import net.ulich.eventsourcing.core.event.PolicyEvent;
import net.ulich.eventsourcing.core.event.PolicyModifiedEvent;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.JdkIdGenerator;

import java.util.List;

@Service
@AllArgsConstructor
public class PolicyService {

    private final EventRepository eventRepository;
    private final JdkIdGenerator idGenerator = new JdkIdGenerator();

    public Policy createPolicy(CreatePolicyRequest createRequest) {
        String id = idGenerator.generateId().toString();

        PolicyCreatedEvent event = new PolicyCreatedEvent(id, createRequest);
        Policy policy = apply(id, event);

        // now it is safe to do any kind of things that modify other systems.
        // e.g. send an event to a message broker that the policy was created

        return policy;
    }

    public Policy getPolicy(String id, Integer version) {
        final List<PolicyEvent> events;
        if (version != null) {
            events = eventRepository.getByPolicyIdAndVersionLessOrEqualThan(id, version);
        } else {
            events = eventRepository.getByPolicyId(id);
        }

        return new Policy(events);
    }

    public Policy modifyPolicy(String id, PolicyMtaRequest mtaRequest) {
        PolicyModifiedEvent event = new PolicyModifiedEvent(id, mtaRequest);
        Policy policy = apply(id, event);

        // now it is safe to do any kind of things that modify other systems.
        // e.g. send an event to a message broker that the policy was modified

        return policy;
    }

    public Policy cancelPolicy(String id, CancelPolicyRequest cancelRequest) {
        PolicyCanceledEvent event = new PolicyCanceledEvent(id, cancelRequest);
        Policy policy = apply(id, event);

        // now it is safe to do any kind of things that modify other systems.
        // e.g. send an event to a message broker that the policy was canceled

        return policy;
    }

    private Policy apply(String id, PolicyEvent event) {
        List<PolicyEvent> events = eventRepository.getByPolicyId(id);
        Policy policy = new Policy(events);

        policy.apply(event);

        eventRepository.add(event);
        return policy;
    }
}
