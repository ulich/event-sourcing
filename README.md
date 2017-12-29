# Showcase application with an event sourcing architecture

This example application provides an API to create, update and cancel insurance policies.

The event sourcing pattern is used to have an audit log and the ability to see the state of a policy from previous versions.

There is no need for CQRS in this case, because a policy will only have very few versions and replaying them is
a simple and cheap in-memory task.

When creating, modifying or canceling a policy, other systems may need to be notified of the change. If this notification
had been implemented in the [Policy](src/main/java/net/ulich/eventsourcing/core/domain/Policy.java) class where the business
logic is located (which would make most sense), other systems would have been notified every time the events are replayed.
Therefore the notification should be put into the service methods.

## start locally

To start the application, first start the dynamo db docker container:

```
docker run -it --rm -p 8000:8000 cnadiminti/dynamodb-local:latest
```

Then you can start the app through the IDE or via the corresponding gradle task.


## Create policy
```
curl -XPOST -d '{"coverStartDate": "2018-03-01", "apartmentSize": 60}' -H"Content-Type:application/json" localhost:8080/policies
```
Take note of the id in the response and provide it in the other requests.

## Modify policy
```
curl -XPUT -d '{"apartmentSize": 100}' -H"Content-Type:application/json" localhost:8080/policies/{policyId}
```

## Cancel policy
```
curl -XPOST -d '{"coverEndDate": "2018-06-01"}' -H"Content-Type:application/json" localhost:8080/policies/{policyId}/cancellation
```

## GetPolicy

latest:
```
curl 'localhost:8080/policies/{policyId}'
```

specific version:
```
curl 'localhost:8080/policies/2dde682f-6958-4f0e-9e01-aadf5e7f4ab5?version=1'
```
