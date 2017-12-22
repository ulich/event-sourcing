package net.ulich.eventsourcing.core

import net.ulich.eventsourcing.api.dto.CancelPolicyRequest
import net.ulich.eventsourcing.api.dto.CreatePolicyRequest
import net.ulich.eventsourcing.api.dto.PolicyMtaRequest
import net.ulich.eventsourcing.core.domain.Policy
import net.ulich.eventsourcing.core.domain.PolicyState
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDate

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PolicyServiceIT extends Specification {


    @Autowired
    @Subject
    PolicyService sut
    private int expectedApartmentSize
    private LocalDate expectedCoverStartDate


    void setup() {
        expectedApartmentSize = 450
        expectedCoverStartDate = LocalDate.now()
    }

    void 'create policy will take attributes from create event'() {

        given:
        CreatePolicyRequest createRequest = defaultCreatePolicyRequest()

        when:
        Policy policy = sut.createPolicy(createRequest)

        then:
        policy.apartmentSize == expectedApartmentSize
        policy.coverStartDate == expectedCoverStartDate

    }

    void 'create policy will calculate cover end date'() {

        given:
        CreatePolicyRequest createRequest = defaultCreatePolicyRequest()

        when:
        Policy policy = sut.createPolicy(createRequest)

        then:
        policy.coverEndDate == expectedCoverStartDate.plusYears(1l)

    }


    void 'cancel policy will override the cover end date from the policy'() {

        given:
        Policy policy = sut.createPolicy(defaultCreatePolicyRequest())

        and:
        LocalDate expectedCancelDate = LocalDate.now().plusMonths(1l)
        CancelPolicyRequest cancelPolicyRequest = defaultCancelRequest(expectedCancelDate)

        when:
        Policy cancelledPolicy = sut.cancelPolicy(policy.getId(), cancelPolicyRequest)

        then:
        cancelledPolicy.coverEndDate == expectedCancelDate

    }


    void 'get policy loads the policy for a given version with the correct state'() {

        given: 'version 1 is the creation'
        Policy policy = sut.createPolicy(defaultCreatePolicyRequest())

        and: 'version 2 is the modification'
        sut.modifyPolicy(policy.id, defaultModifyPolicyRequest())

        and: 'version 3 is the cancellation'
        sut.cancelPolicy(policy.id, defaultCancelRequest())

        when:
        Policy receivedPolicy = sut.getPolicy(policy.id, 2)

        then:
        receivedPolicy.state == PolicyState.ACTIVE

    }


    void 'get policy loads the latest policy when no version is given'() {

        given: 'version 1 is the creation'
        Policy policy = sut.createPolicy(defaultCreatePolicyRequest())

        and: 'version 2 is the modification'
        sut.modifyPolicy(policy.id, defaultModifyPolicyRequest())

        and: 'version 3 is the cancellation'
        Policy cancelledPolicy = sut.cancelPolicy(policy.id, defaultCancelRequest())

        when:
        Policy receivedPolicy = sut.getPolicy(policy.id)

        then:
        receivedPolicy.state == cancelledPolicy.state

    }


    private CreatePolicyRequest defaultCreatePolicyRequest() {
        new CreatePolicyRequest(expectedCoverStartDate, expectedApartmentSize)
    }
    private PolicyMtaRequest defaultModifyPolicyRequest() {
        new PolicyMtaRequest(150)
    }

    private CancelPolicyRequest defaultCancelRequest(LocalDate expectedCancelDate = null) {
        new CancelPolicyRequest(expectedCancelDate ?: LocalDate.now())
    }


}