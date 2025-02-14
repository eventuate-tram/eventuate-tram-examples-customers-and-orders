package io.eventuate.examples.tram.ordersandcustomers.customers.eventpublishing.contracttests;

import io.eventuate.examples.common.money.Money;
import io.eventuate.examples.tram.ordersandcustomers.customers.domain.CustomerCreatedEvent;
import io.eventuate.tram.events.common.DomainEvent;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.spring.testing.cloudcontract.EnableEventuateTramContractVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@SpringBootTest(classes = OrderhistoryserviceBase.TestConfig.class)
@AutoConfigureMessageVerifier
public abstract class OrderhistoryserviceBase {

    @Configuration
    @EnableAutoConfiguration
    @EnableEventuateTramContractVerifier
    public static class TestConfig {

    }

    @Autowired
    private DomainEventPublisher domainEventPublisher;

    protected void customerCreatedEvent() {
        CustomerCreatedEvent event = new CustomerCreatedEvent(123L, "John Doe", new Money(1000));
        domainEventPublisher.publish("io.eventuate.examples.tram.ordersandcustomers.customers.domain.Customer",
                "123",
                Collections.singletonList((DomainEvent) event));
    }
}
