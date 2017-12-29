package net.ulich.eventsourcing.core;

import lombok.AllArgsConstructor;
import net.ulich.eventsourcing.api.dto.CancelPolicyRequest;
import net.ulich.eventsourcing.api.dto.CreatePolicyRequest;
import net.ulich.eventsourcing.api.dto.PolicyMtaRequest;
import net.ulich.eventsourcing.core.domain.Policy;
import net.ulich.eventsourcing.core.event.PolicyCanceledEvent;
import net.ulich.eventsourcing.core.event.PolicyCreatedEvent;
import net.ulich.eventsourcing.core.event.PolicyEvent;
import net.ulich.eventsourcing.core.event.PolicyModifiedEvent;
import net.ulich.eventsourcing.core.persistence.PolicyEventRepositoryBean;
import org.springframework.stereotype.Service;
import org.springframework.util.JdkIdGenerator;

import java.util.List;

@Service
@AllArgsConstructor
public class PolicyServiceBean implements PolicyService {

    private final PolicyEventRepositoryBean policyEventRepository;
    private final JdkIdGenerator idGenerator = new JdkIdGenerator();

    @Override
    public Policy createPolicy(CreatePolicyRequest createRequest) {
        String id = idGenerator.generateId().toString();

        PolicyCreatedEvent event = new PolicyCreatedEvent(id, createRequest);
        Policy policy = applyEvent(id, event);

        // now it is safe to do any kind of things that modify other systems.
        // e.g. send an event to a message broker that the policy was created

        return policy;
    }

    @Override
    public Policy modifyPolicy(String policyId, PolicyMtaRequest mtaRequest) {
        PolicyModifiedEvent event = new PolicyModifiedEvent(policyId, mtaRequest);
        Policy policy = applyEvent(policyId, event);

        // now it is safe to do any kind of things that modify other systems.
        // e.g. send an event to a message broker that the policy was modified

        return policy;
    }

    @Override
    public Policy cancelPolicy(String policyId, CancelPolicyRequest cancelRequest) {
        PolicyCanceledEvent event = new PolicyCanceledEvent(policyId, cancelRequest);
        Policy policy = applyEvent(policyId, event);

        // now it is safe to do any kind of things that modify other systems.
        // e.g. send an event to a message broker that the policy was canceled

        return policy;
    }



    @Override
    public Policy getPolicy(String policyId, Integer version) {
        final List<PolicyEvent> events;
        if (version != null) {
            events = policyEventRepository.getByPolicyIdAndVersionLessOrEqualThan(policyId, version);
        } else {
            events = policyEventRepository.getByPolicyId(policyId);
        }

        return new Policy(events);
    }

    @Override
    public Policy getPolicy(String policyId) {
        return getPolicy(policyId, null);
    }



    private Policy applyEvent(String id, PolicyEvent event) {
        List<PolicyEvent> events = policyEventRepository.getByPolicyId(id);
        Policy policy = new Policy(events);

        policy.applyEvent(event);

        policyEventRepository.add(event);
        return policy;
    }
}
