package io.eventuate.examples.tram.sagas.ordersandcustomers.customers;


import io.eventuate.examples.tram.ordersandcustomers.orderhistory.backend.OrderHistoryEventConsumer;
import io.eventuate.examples.tram.ordersandcustomers.orderhistory.backend.OrderHistoryViewBackendConfiguration;
import io.eventuate.examples.tram.ordersandcustomers.orderhistory.backend.OrderHistoryViewMongoConfiguration;
import io.eventuate.examples.tram.ordersandcustomers.orderhistory.web.OrderHistoryViewWebConfiguration;
import io.eventuate.examples.tram.ordersandcustomers.orders.domain.OrderCreatedEvent;
import io.eventuate.tram.spring.inmemory.TramInMemoryCommonConfiguration;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderHistoryServiceInProcessComponentTest {

    protected static Logger logger = LoggerFactory.getLogger(OrderHistoryServiceInProcessComponentTest.class);

    @Configuration
    @EnableAutoConfiguration(exclude = FlywayAutoConfiguration.class)
    @Import({OrderHistoryViewWebConfiguration.class,
        OrderHistoryViewBackendConfiguration.class,
        OrderHistoryViewMongoConfiguration.class,
        TramInMemoryCommonConfiguration.class})
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

    private static final String ORDER_CHANNEL = "io.eventuate.examples.tram.ordersandcustomers.orders.domain.Order";
    private static final String ORDER_CREATED_EVENT = OrderCreatedEvent.class.getName();
    private static final String ORDER_EVENT_SUBSCRIBER = OrderHistoryEventConsumer.class.getName() + ".orderCreatedEventHandler";

    @Test
    public void shouldExposeSpringWolf() {
        AsyncApiDocument doc = AsyncApiDocument.getSpringWolfDoc();

        doc.assertReceivesMessage(ORDER_EVENT_SUBSCRIBER, ORDER_CHANNEL, ORDER_CREATED_EVENT);

    }

}
