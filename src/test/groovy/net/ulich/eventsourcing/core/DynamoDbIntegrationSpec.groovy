package net.ulich.eventsourcing.core

import com.groovycoder.spockdockerextension.Testcontainers
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.util.EnvironmentTestUtils
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.containers.GenericContainer
import spock.lang.Shared
import spock.lang.Specification

@Testcontainers
@ContextConfiguration(initializers = Initializer.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class DynamoDbIntegrationSpec extends Specification {


    @Shared
    static GenericContainer dynamoDb = new GenericContainer("cnadiminti/dynamodb-local:latest")
            .withExposedPorts(8000)


    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            dynamoDb.start()
            EnvironmentTestUtils.addEnvironment("integrationtest", configurableApplicationContext.getEnvironment(),
                    "aws.dynamodb.endpoint=http://localhost:" + dynamoDb.getMappedPort(8000),
            )
        }
    }

}