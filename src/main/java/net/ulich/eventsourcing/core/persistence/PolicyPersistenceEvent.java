package net.ulich.eventsourcing.core.persistence;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.*;

import java.util.Date;


/**
 * Persistence (DynamoDB) repesentation of a domain event.
 *
 * It will store some metadata along with the domain event as JSON
 */
@AllArgsConstructor
@Getter
@Builder
@DynamoDBTable(tableName = "policy-events")
@NoArgsConstructor
@Data
public class PolicyPersistenceEvent {

    @DynamoDBRangeKey
    @DynamoDBAutoGeneratedKey
    private String eventId;

    @DynamoDBAutoGeneratedTimestamp(strategy=DynamoDBAutoGenerateStrategy.CREATE)
    private Date timestamp;

    @DynamoDBHashKey
    protected String policyId;

    protected String content;
}

