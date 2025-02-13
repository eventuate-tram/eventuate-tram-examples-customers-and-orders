package contracts.orderservice;

org.springframework.cloud.contract.spec.Contract.make {
    label 'customerCreditReservedEvent'
    input {
        triggeredBy('customerCreditReserved()')
    }

    outputMessage {
        sentTo('io.eventuate.examples.tram.ordersandcustomers.customers.domain.Customer')
        body([
                customerId: 101,
                orderId: 102
        ])
        headers {
            header('event-aggregate-type', 'io.eventuate.examples.tram.ordersandcustomers.customers.domain.Customer')
            header('event-type', 'io.eventuate.examples.tram.ordersandcustomers.customers.domain.CustomerCreditReservedEvent')
            header('event-aggregate-id', '101')
        }
    }
}