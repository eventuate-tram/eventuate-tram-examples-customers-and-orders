package io.eventuate.examples.tram.ordersandcustomers.customers;


import io.eventuate.common.testcontainers.ContainerTestUtil;
import io.eventuate.common.testcontainers.DatabaseContainerFactory;
import io.eventuate.common.testcontainers.EventuateDatabaseContainer;
import io.eventuate.examples.tram.ordersandcustomers.customers.domain.CustomerDomainConfiguration;
import io.eventuate.examples.tram.ordersandcustomers.customers.eventhandlers.CustomerServiceEventHandlerConfiguration;
import io.eventuate.examples.tram.ordersandcustomers.customers.web.CustomerWebConfiguration;
import io.eventuate.messaging.kafka.testcontainers.EventuateKafkaNativeCluster;
import io.eventuate.messaging.kafka.testcontainers.EventuateKafkaNativeContainer;
import io.github.springwolf.core.asyncapi.scanners.ChannelsScanner;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.lifecycle.Startables;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerServiceInProcessComponentTest {

    protected static Logger logger = LoggerFactory.getLogger(CustomerServiceInProcessComponentTest.class);

    public static EventuateKafkaNativeCluster eventuateKafkaCluster = new EventuateKafkaNativeCluster("customer-service-tests");

    public static EventuateKafkaNativeContainer kafka = eventuateKafkaCluster.kafka
        .withNetworkAliases("kafka")
        .withReuse(ContainerTestUtil.shouldReuse());

    public static EventuateDatabaseContainer<?> database =
            DatabaseContainerFactory.makeVanillaDatabaseContainer()
                    .withNetwork(eventuateKafkaCluster.network)
                    .withNetworkAliases("customer-service-db")
                    .withReuse(ContainerTestUtil.shouldReuse());

    @DynamicPropertySource
    static void registerMySqlProperties(DynamicPropertyRegistry registry) {
        Startables.deepStart(database, kafka).join();
        database.registerProperties(registry::add);
        kafka.registerProperties(registry::add);
    }

    @Configuration
    @EnableAutoConfiguration
    @Import({CustomerWebConfiguration.class, CustomerPersistenceConfiguration.class,
        CustomerDomainConfiguration.class, CustomerServiceEventHandlerConfiguration.class, })
    static public class Config {

        @Bean
        public SomeListener someListener() {
            return new SomeListener();
        }

        @Bean
        public ChannelsScanner channelsScanner() {
            return new MyChannelsScanner();
        }

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
    void shouldExposeSwaggerUI() throws IOException {
        String url = "http://%s:%s/swagger-ui/index.html".formatted("localhost", port);
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        if (connection.getResponseCode() != 200)
            Assertions.fail("%s: Expected 200 for %s, got %s".formatted("Customer Service", url, connection.getResponseCode()));
    }

    @Test
    public void shouldExposeSpringWolf() {
        var s = RestAssured.get("/springwolf/docs")
                .then()
                .statusCode(200)
                    .extract().response().prettyPrint();
        System.out.println(s);
        RestAssured.get("/springwolf/docs.yaml").then().statusCode(200);
        RestAssured.get("/springwolf/asyncapi-ui.html").then().statusCode(200);
    }
}
