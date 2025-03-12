package io.eventuate.examples.tram.customersandorders.customers;



import io.eventuate.examples.tram.ordersandcustomers.customers.domain.CustomerCreditReservedEvent;
import io.eventuate.examples.tram.ordersandcustomers.orders.domain.Order;
import io.eventuate.examples.tram.ordersandcustomers.orders.domain.OrderCreatedEvent;
import io.eventuate.examples.tram.ordersandcustomers.orders.domain.OrderDomainConfiguration;
import io.eventuate.examples.tram.ordersandcustomers.orders.eventhandlers.CustomerEventConsumer;
import io.eventuate.examples.tram.ordersandcustomers.orders.eventhandlers.OrderEventHandlersConfiguration;
import io.eventuate.examples.tram.ordersandcustomers.orders.eventpublishing.OrderEventPublisherImpl;
import io.eventuate.examples.tram.ordersandcustomers.orders.eventpublishing.OrderEventPublishingConfiguration;
import io.eventuate.examples.tram.ordersandcustomers.orders.persistence.OrderPersistenceConfiguration;
import io.eventuate.examples.tram.ordersandcustomers.orders.restapi.OrderRestApiConfiguration;
import io.eventuate.tram.spring.inmemory.TramInMemoryConfiguration;
import io.eventuate.tram.spring.springwolf.testing.AsyncApiDocument;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderServiceInProcessComponentTest {

    protected static Logger logger = LoggerFactory.getLogger(OrderServiceInProcessComponentTest.class);

    @Configuration
    @EnableAutoConfiguration(exclude = FlywayAutoConfiguration.class)
    @Import({OrderRestApiConfiguration.class, OrderPersistenceConfiguration.class,
        OrderDomainConfiguration.class,
        OrderEventHandlersConfiguration.class,
        OrderEventPublishingConfiguration.class,
        TramInMemoryConfiguration.class})
    static public class Config {

    }

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setup() {
        RestAssured.port = port;
    }

    @Test
    public void shouldStart() {

    }

    @Test
    void shouldExposeSwaggerUI() {
        RestAssured.given()
            .get("/swagger-ui/index.html")
            .then()
            .statusCode(200);
    }

    private static final String CUSTOMER_CHANNEL = "io.eventuate.examples.tram.ordersandcustomers.customers.domain.Customer";
    private static final String CUSTOMER_CREDIT_RESERVED_EVENT = CustomerCreditReservedEvent.class.getName();
    private static final String CUSTOMER_EVENT_SUBSCRIBER = CustomerEventConsumer.class.getName() + ".handleCustomerCreditReservedEvent";

    private static final String ORDER_CHANNEL = Order.class.getName();
    private static final String ORDER_CREATED_EVENT = OrderCreatedEvent.class.getName();
    private static final String ORDER_EVENT_PUBLISHER = OrderEventPublisherImpl.class.getName();

    @Test
    public void shouldExposeSpringWolf() {
        AsyncApiDocument doc = AsyncApiDocument.getSpringWolfDoc();

        assertThat(doc.getVersion())
            .as("AsyncAPI version should be 3.0.0")
            .isEqualTo("3.0.0");

        doc.assertReceivesMessage(CUSTOMER_EVENT_SUBSCRIBER, CUSTOMER_CHANNEL, CUSTOMER_CREDIT_RESERVED_EVENT);
        doc.assertSendsMessage(ORDER_EVENT_PUBLISHER, ORDER_CHANNEL, ORDER_CREATED_EVENT);
    }

}
