package io.eventuate.examples.tram.ordersandcustomers.customers.eventpublishing.contracttests;

import io.eventuate.examples.tram.ordersandcustomers.customers.domain.Customer;
import io.eventuate.examples.tram.ordersandcustomers.customers.domain.CustomerCreditReservationFailedEvent;
import io.eventuate.examples.tram.ordersandcustomers.customers.domain.CustomerCreditReservedEvent;
import io.eventuate.examples.tram.ordersandcustomers.customers.domain.CustomerValidationFailedEvent;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.spring.testing.cloudcontract.EnableEventuateTramContractVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,
    classes = OrderserviceBase.TestConfig.class)
public abstract class OrderserviceBase {

    @Autowired
    private DomainEventPublisher domainEventPublisher;

    public void customerCreditReserved() {
        domainEventPublisher.publish(Customer.class, "101", List.of(new CustomerCreditReservedEvent(101L, 102L)));
    }

    public void customerValidationFailed() {
        domainEventPublisher.publish(Customer.class, "101", List.of(new CustomerValidationFailedEvent(101L, 102L)));
    }

    public void customerCreditReservationFailed() {
        domainEventPublisher.publish(Customer.class, "101", List.of(new CustomerCreditReservationFailedEvent(101L, 102L)));
    }

    @Configuration
    @EnableAutoConfiguration
    @EnableEventuateTramContractVerifier
    public static class TestConfig {

    }


}
