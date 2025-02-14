package contracts.orderservice;

org.springframework.cloud.contract.spec.Contract.make {
    label 'order-history-service-orderCreatedEvent'
    input {
        triggeredBy('orderCreated()')
    }

    outputMessage {
        sentTo('io.eventuate.examples.tram.ordersandcustomers.orders.domain.Order')
        body([
                orderDetails: [
                        customerId : 101,
                        orderTotal: [amount: 123]
                ]
        ])
        headers {
            header('event-aggregate-type', 'io.eventuate.examples.tram.ordersandcustomers.orders.domain.Order')
            header('event-type', 'io.eventuate.examples.tram.ordersandcustomers.orders.domain.OrderCreatedEvent')
            header('event-aggregate-id', '99')
        }
    }
}