package contracts.orderservice;

org.springframework.cloud.contract.spec.Contract.make {
    label 'customerValidationFailedEvent'
    input {
        triggeredBy('customerValidationFailed()')
    }

    outputMessage {
        sentTo('io.eventuate.examples.tram.ordersandcustomers.customers.domain.Customer')
        body([
                customerId: 101,
                orderId: 102
        ])
        headers {
            header('event-aggregate-type', 'io.eventuate.examples.tram.ordersandcustomers.customers.domain.Customer')
            header('event-type', 'io.eventuate.examples.tram.ordersandcustomers.customers.domain.CustomerValidationFailedEvent')
            header('event-aggregate-id', '101')
        }
    }
}