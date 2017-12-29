package net.ulich.eventsourcing.core.persistence;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DynamoDBUtils {
    private final DynamoDBMapper dbMapper;
    private final AmazonDynamoDB dynamoDB;

    @Autowired
    public DynamoDBUtils(DynamoDBMapper dbMapper, AmazonDynamoDB dynamoDB) {
        this.dbMapper = dbMapper;
        this.dynamoDB = dynamoDB;
    }

    public void createTable(Class clazz) {
        CreateTableRequest request = dbMapper
                .generateCreateTableRequest(clazz)
                .withProvisionedThroughput(new ProvisionedThroughput(1L, 1L));

        try {
            DescribeTableResult result = dynamoDB.describeTable(request.getTableName());
            log.info("Table {} status, {}", request.getTableName(), result.getTable().getTableStatus());
        } catch (ResourceNotFoundException expectedException) {
            CreateTableResult result = dynamoDB.createTable(request);
            log.info("Table {} creation triggered, {}", request.getTableName(), result.getTableDescription().getTableStatus());
        }
    }
}
