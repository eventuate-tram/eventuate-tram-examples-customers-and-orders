package io.eventuate.examples.tram.sagas.ordersandcustomers.customers;


import io.eventuate.examples.tram.sagas.ordersandcustomers.testutil.testcontainers.ContainerTestUtil;
import io.eventuate.messaging.kafka.testcontainers.EventuateKafkaNativeCluster;
import io.eventuate.messaging.kafka.testcontainers.EventuateKafkaNativeContainer;
import io.eventuate.testcontainers.service.ServiceContainer;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.lifecycle.Startables;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class OrderHistoryServiceComponentTest {

    protected static Logger logger = LoggerFactory.getLogger(OrderHistoryServiceComponentTest.class);

    public static EventuateKafkaNativeCluster eventuateKafkaCluster = new EventuateKafkaNativeCluster();

    public static EventuateKafkaNativeContainer kafka = eventuateKafkaCluster.kafka
        .withNetworkAliases("kafka")
        .withReuse(ContainerTestUtil.shouldReuse());

    // Use testcontainers module to run MongoDB

    private static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:8.0.4")
            .withNetwork(eventuateKafkaCluster.network)
            .withNetworkAliases("order-history-service-db")
            .withReuse(ContainerTestUtil.shouldReuse());

    public static ServiceContainer service =
            ServiceContainer.makeFromDockerfileOnClasspath()
                    .withNetwork(eventuateKafkaCluster.network)
                    .withKafka(kafka)
                    .withEnv("SPRING_DATA_MONGODB_URI", "mongodb://order-history-service-db/customers_and_orders")
                    .dependsOn(mongoDBContainer)
                    .withLogConsumer(new Slf4jLogConsumer(logger).withPrefix("SVC order-history-service:"))
                    .withReuse(false) // should rebuild
            ;

    @BeforeAll
    public static void startContainers() {
        Startables.deepStart(service).join();
        RestAssured.port = service.getFirstMappedPort();
    }

    @Test
    public void shouldStart() {

        RestAssured
            .given()
            .get("/customers/count")
            .then()
            .statusCode(200);

    }

    @Test
    void shouldExposeSwaggerUI() throws IOException {
        String url = "http://%s:%s/swagger-ui/index.html".formatted("localhost", service.getFirstMappedPort());
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        if (connection.getResponseCode() != 200)
            Assertions.fail("%s: Expected 200 for %s, got %s".formatted("Customer Service", url, connection.getResponseCode()));
    }
}
