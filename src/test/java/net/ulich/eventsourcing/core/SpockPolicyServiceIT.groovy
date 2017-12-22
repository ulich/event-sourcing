package net.ulich.eventsourcing.core

import net.ulich.eventsourcing.api.dto.CancelPolicyRequest
import net.ulich.eventsourcing.api.dto.CreatePolicyRequest
import net.ulich.eventsourcing.core.domain.Policy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDate

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpockPolicyServiceIT extends Specification {


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
        CancelPolicyRequest cancelPolicyRequest = new CancelPolicyRequest(expectedCancelDate)

        when:
        Policy cancelledPolicy = sut.cancelPolicy(policy.getId(), cancelPolicyRequest)

        then:
        cancelledPolicy.coverEndDate == expectedCancelDate

    }


    private CreatePolicyRequest defaultCreatePolicyRequest() {
        new CreatePolicyRequest(expectedCoverStartDate, expectedApartmentSize)
    }

}