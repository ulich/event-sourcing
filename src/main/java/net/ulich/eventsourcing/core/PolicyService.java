package net.ulich.eventsourcing.core;

import net.ulich.eventsourcing.api.dto.CancelPolicyRequest;
import net.ulich.eventsourcing.api.dto.CreatePolicyRequest;
import net.ulich.eventsourcing.api.dto.PolicyMtaRequest;
import net.ulich.eventsourcing.core.domain.Policy;


/**
 * High level API for interacting with a Policy through a CRUD interface
 */
public interface PolicyService {

    Policy createPolicy(CreatePolicyRequest createRequest);

    Policy modifyPolicy(String policyId, PolicyMtaRequest mtaRequest);

    Policy cancelPolicy(String policyId, CancelPolicyRequest cancelRequest);

    Policy getPolicy(String policyId, Integer version);

    Policy getPolicy(String policyId);
}
