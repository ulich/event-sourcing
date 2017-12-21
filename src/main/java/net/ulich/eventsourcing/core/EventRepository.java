package net.ulich.eventsourcing.core;

import net.ulich.eventsourcing.core.event.PolicyEvent;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Repository
class EventRepository {

    private Map<String, List<PolicyEvent>> events = new HashMap<>();

    void add(PolicyEvent event) {
        eventsOfPolicy(event.getPolicyId()).add(event);
    }

    List<PolicyEvent> getByPolicyId(String policyId) {
        return eventsOfPolicy(policyId);
    }

    private List<PolicyEvent> eventsOfPolicy(String policyId) {
        return events.computeIfAbsent(policyId, k -> new ArrayList<>());
    }

    List<PolicyEvent> getByPolicyIdAndVersionLessOrEqualThan(String id, Integer version) {
        return getByPolicyId(id).stream()
                .limit(version)
                .collect(toList());
    }
}
