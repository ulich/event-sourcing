package net.ulich.eventsourcing.core.persistence;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.ulich.eventsourcing.core.event.PolicyEvent;
import net.ulich.eventsourcing.core.persistence.PolicyPersistenceEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Repository
public class PolicyEventRepositoryBean implements PolicyEventRepository {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private DynamoDBMapper mapper;

    @Override
    public void add(PolicyEvent event) {
        PolicyPersistenceEvent persistenceEvent = createPersistentEvent(event);
        mapper.save(persistenceEvent);
    }

    @NotNull
    private PolicyPersistenceEvent createPersistentEvent(PolicyEvent event) {
        try {
            PolicyPersistenceEvent persistenceEvent = new PolicyPersistenceEvent();
            persistenceEvent.setPolicyId(event.getPolicyId());
            persistenceEvent.setContent(objectMapper.writeValueAsString(event));
            return persistenceEvent;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<PolicyEvent> getByPolicyId(String policyId) {
        return eventsOfPolicy(policyId);
    }

    private List<PolicyEvent> eventsOfPolicy(String policyId) {

        PaginatedQueryList<PolicyPersistenceEvent> persistentEvents = mapper.query(PolicyPersistenceEvent.class, createQueryByPolicyId(policyId));

        return mapToPolicyEvents(persistentEvents);
    }

    private List<PolicyEvent> mapToPolicyEvents(PaginatedQueryList<PolicyPersistenceEvent> persistentEvents) {
        return persistentEvents.stream()
                    .sorted(Comparator.comparing(PolicyPersistenceEvent::getTimestamp))
                    .map(this::mapToPolicyEvent)
                    .collect(toList());
    }

    private DynamoDBQueryExpression<PolicyPersistenceEvent> createQueryByPolicyId(String policyId) {
        PolicyPersistenceEvent policyEventHashKey = PolicyPersistenceEvent.builder().policyId(policyId).build();
        return new DynamoDBQueryExpression<PolicyPersistenceEvent>()
                .withHashKeyValues(policyEventHashKey);
    }

    @Override
    public List<PolicyEvent> getByPolicyIdAndVersionLessOrEqualThan(String id, Integer version) {
        return getByPolicyId(id).stream()
                .limit(version)
                .collect(toList());
    }

    private PolicyEvent mapToPolicyEvent(PolicyPersistenceEvent persistentEvent) {
        try {
            return objectMapper.readValue(persistentEvent.getContent(), PolicyEvent.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
