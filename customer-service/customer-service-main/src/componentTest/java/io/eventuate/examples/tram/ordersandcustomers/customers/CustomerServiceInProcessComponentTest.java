package io.eventuate.examples.tram.ordersandcustomers.customers;


import io.eventuate.examples.tram.ordersandcustomers.customers.domain.Customer;
import io.eventuate.examples.tram.ordersandcustomers.customers.domain.CustomerCreatedEvent;
import io.eventuate.examples.tram.ordersandcustomers.customers.domain.CustomerDomainConfiguration;
import io.eventuate.examples.tram.ordersandcustomers.customers.eventhandlers.CustomerServiceEventHandlerConfiguration;
import io.eventuate.examples.tram.ordersandcustomers.customers.eventhandlers.OrderEventConsumer;
import io.eventuate.examples.tram.ordersandcustomers.customers.eventpublishing.CustomerEventPublisherImpl;
import io.eventuate.examples.tram.ordersandcustomers.customers.eventpublishing.CustomerEventPublishingConfiguration;
import io.eventuate.examples.tram.ordersandcustomers.customers.restapi.CustomerRestApiConfiguration;
import io.eventuate.examples.tram.ordersandcustomers.orders.domain.OrderCreatedEvent;
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
public class CustomerServiceInProcessComponentTest {

    protected static Logger logger = LoggerFactory.getLogger(CustomerServiceInProcessComponentTest.class);

    @Configuration
    @EnableAutoConfiguration(exclude = FlywayAutoConfiguration.class)
    @Import({CustomerRestApiConfiguration.class, CustomerPersistenceConfiguration.class,
        CustomerDomainConfiguration.class,
        CustomerServiceEventHandlerConfiguration.class,
        CustomerEventPublishingConfiguration.class,
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

//        RestAssured
//            .given()
//            .contentType(ContentType.JSON)
//            .body(new CreateCustomerRequest("Fred", new Money("15.00")))
//            .post("/customers")
//            .then()
//            .statusCode(200);

    }

    @Test
    void shouldExposeSwaggerUI() {
        RestAssured.given()
            .get("/swagger-ui/index.html")
            .then()
            .statusCode(200);
    }

    private static final String CUSTOMER_CHANNEL = Customer.class.getName();
    private static final String CUSTOMER_CREATED_EVENT = CustomerCreatedEvent.class.getName();
    private static final String CUSTOMER_EVENT_PUBLISHER = CustomerEventPublisherImpl.class.getName();
    private static final String CUSTOMER_EVENT_SUBSCRIBER = "operationId";

    private static final String ORDER_CHANNEL = "io.eventuate.examples.tram.ordersandcustomers.orders.domain.Order";
    private static final String ORDER_CREATED_EVENT = OrderCreatedEvent.class.getName();
    private static final String ORDER_EVENT_SUBSCRIBER = OrderEventConsumer.class.getName() + ".handleOrderCreatedEvent";

    @Test
    public void shouldExposeSpringWolf() {
        AsyncApiDocument doc = AsyncApiDocument.getSpringWolfDoc();

        assertThat(doc.getVersion())
            .as("AsyncAPI version should be 3.0.0")
            .isEqualTo("3.0.0");

        doc.assertSendsMessage(CUSTOMER_EVENT_PUBLISHER, CUSTOMER_CHANNEL, CUSTOMER_CREATED_EVENT);
        doc.assertReceivesMessage(ORDER_EVENT_SUBSCRIBER, ORDER_CHANNEL, ORDER_CREATED_EVENT);
    }

}
