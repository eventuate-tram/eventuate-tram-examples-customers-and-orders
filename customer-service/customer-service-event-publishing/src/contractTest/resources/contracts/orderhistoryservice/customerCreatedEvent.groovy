package contracts.orderhistoryservice

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    label("customerCreatedEvent")
    input {
        triggeredBy('customerCreatedEvent()')
    }
    outputMessage {
        sentTo("io.eventuate.examples.tram.ordersandcustomers.customers.domain.Customer")
        body([
            name: $(consumer(regex('.+')), producer('John Doe')),
            creditLimit: [
                amount: $(consumer(regex('[0-9]+')), producer(1000))
            ]
        ])
        headers {
            header('event-type', 'io.eventuate.examples.tram.ordersandcustomers.customers.domain.CustomerCreatedEvent')
            header('event-aggregate-type', 'io.eventuate.examples.tram.ordersandcustomers.customers.domain.Customer')
            header('event-aggregate-id', $(consumer(regex('[0-9]+')), producer('123')))
        }
    }
}