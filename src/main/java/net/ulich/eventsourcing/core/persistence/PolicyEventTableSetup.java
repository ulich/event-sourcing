package net.ulich.eventsourcing.core.persistence;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class PolicyEventTableSetup implements ApplicationListener<ContextRefreshedEvent> {

    @NonNull
    private final DynamoDBUtils dynamoDBUtils;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        dynamoDBUtils.createTable(PolicyPersistenceEvent.class);
    }
}