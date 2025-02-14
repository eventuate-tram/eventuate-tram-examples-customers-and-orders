package io.eventuate.examples.tram.ordersandcustomers.orders.eventpublishing.contracttests;

import io.eventuate.examples.common.money.Money;
import io.eventuate.examples.tram.ordersandcustomers.orders.domain.Order;
import io.eventuate.examples.tram.ordersandcustomers.orders.domain.OrderCreatedEvent;
import io.eventuate.examples.tram.ordersandcustomers.orders.domain.OrderDetails;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.spring.testing.cloudcontract.EnableEventuateTramContractVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,
    classes = OrderhistoryserviceBase.TestConfig.class)
public abstract class OrderhistoryserviceBase {

    @Autowired
    private DomainEventPublisher domainEventPublisher;

    public void orderCreated() {
        domainEventPublisher.publish(Order.class, "99", List.of(new OrderCreatedEvent(new OrderDetails(101L, new Money(123)))));
    }

    @Configuration
    @EnableAutoConfiguration
    @EnableEventuateTramContractVerifier
    public static class TestConfig {

    }


}
