package io.eventuate.examples.tram.customersandorders.customers;


import io.eventuate.common.testcontainers.ContainerTestUtil;
import io.eventuate.common.testcontainers.DatabaseContainerFactory;
import io.eventuate.common.testcontainers.EventuateDatabaseContainer;
import io.eventuate.messaging.kafka.testcontainers.EventuateKafkaNativeCluster;
import io.eventuate.messaging.kafka.testcontainers.EventuateKafkaNativeContainer;
import io.eventuate.testcontainers.service.ServiceContainer;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.lifecycle.Startables;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class OrderServiceComponentTest {

    protected static Logger logger = LoggerFactory.getLogger(OrderServiceComponentTest.class);

    public static EventuateKafkaNativeCluster eventuateKafkaCluster = new EventuateKafkaNativeCluster("order-service-tests");


    public static EventuateKafkaNativeContainer kafka = eventuateKafkaCluster.kafka
        .withNetworkAliases("kafka")
        .withReuse(ContainerTestUtil.shouldReuse());

    public static EventuateDatabaseContainer<?> database =
            DatabaseContainerFactory.makeVanillaDatabaseContainer()
                    .withNetwork(eventuateKafkaCluster.network)
                    .withNetworkAliases("order-service-db")
                    .withReuse(ContainerTestUtil.shouldReuse());


    public static ServiceContainer service =
            ServiceContainer.makeFromDockerfileOnClasspath()
                    .withNetwork(eventuateKafkaCluster.network)
                    .withDatabase(database)
                    .withKafka(kafka)
                    .withLogConsumer(new Slf4jLogConsumer(logger).withPrefix("SVC order-service:"))
                    .withReuse(false) // should rebuild
            ;

    @BeforeAll
    public static void startContainers() {
        Startables.deepStart(service).join();
        RestAssured.port = service.getFirstMappedPort();
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
        String url = "http://%s:%s/swagger-ui/index.html".formatted("localhost", service.getFirstMappedPort());
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        if (connection.getResponseCode() != 200)
            Assertions.fail("%s: Expected 200 for %s, got %s".formatted("Customer Service", url, connection.getResponseCode()));
    }
}
