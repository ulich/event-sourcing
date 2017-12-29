package net.ulich.eventsourcing.core.persistence

import net.ulich.eventsourcing.api.dto.CreatePolicyRequest
import net.ulich.eventsourcing.core.DynamoDbIntegrationSpec
import net.ulich.eventsourcing.core.event.PolicyCreatedEvent
import net.ulich.eventsourcing.core.event.PolicyEvent
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Subject

import java.time.LocalDate

class PolicyEventRepositoryBeanIntegrationSpec extends DynamoDbIntegrationSpec {

    @Autowired
    @Subject PolicyEventRepositoryBean policyEventRepository


    def "adding a policy created event will create an active policy"() {

        given:
        policyEventRepository.add(new PolicyCreatedEvent("123", defaultCreatePolicyRequest() ))

        when:
        List<PolicyEvent> policyEvents = policyEventRepository.getByPolicyId("123")

        then:
        policyEvents.size() == 1
        policyEvents.get(0) instanceof PolicyCreatedEvent
    }


    private CreatePolicyRequest defaultCreatePolicyRequest() {
        new CreatePolicyRequest(LocalDate.now(), 100)
    }
}
