package net.ulich.eventsourcing.core.persistence;

import net.ulich.eventsourcing.core.event.PolicyEvent;

import java.util.List;




/**
 * Persistence repository for policies.
 */
public interface PolicyEventRepository {


    /**
     * Adds a policy event to the repository
     * @param event
     */
    void add(PolicyEvent event);

    /**
     * finds all policy events by a given policyId
     * @param policyId
     * @return
     */
    List<PolicyEvent> getByPolicyId(String policyId);

    /**
     * finds all policy events by a given policyId for a specific version
     * @param id
     * @param version
     * @return
     */
    List<PolicyEvent> getByPolicyIdAndVersionLessOrEqualThan(String id, Integer version);
}
