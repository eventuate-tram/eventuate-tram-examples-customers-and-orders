package io.eventuate.examples.tram.sagas.ordersandcustomers.customers;


import io.eventuate.common.testcontainers.DatabaseContainerFactory;
import io.eventuate.common.testcontainers.EventuateDatabaseContainer;
import io.eventuate.common.testcontainers.EventuateZookeeperContainer;
import io.eventuate.messaging.kafka.testcontainers.EventuateKafkaCluster;
import io.eventuate.messaging.kafka.testcontainers.EventuateKafkaContainer;
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

public class CustomerServiceComponentTest {

    protected static Logger logger = LoggerFactory.getLogger(CustomerServiceComponentTest.class);

    public static EventuateKafkaCluster eventuateKafkaCluster = new EventuateKafkaCluster();

    public static EventuateZookeeperContainer zookeeper = eventuateKafkaCluster.zookeeper;

    public static EventuateKafkaContainer kafka = eventuateKafkaCluster.kafka.dependsOn(zookeeper);

    public static EventuateDatabaseContainer<?> database =
            DatabaseContainerFactory.makeVanillaDatabaseContainer()
                    .withNetwork(eventuateKafkaCluster.network)
                    .withNetworkAliases("customer-service-db")
                    .withReuse(true);


    public static ServiceContainer service =
            ServiceContainer.makeFromDockerfileOnClasspath()
                    .withNetwork(eventuateKafkaCluster.network)
                    .withDatabase(database)
                    .withKafka(kafka)
                    .dependsOn(kafka, database)
                    .withLogConsumer(new Slf4jLogConsumer(logger).withPrefix("SVC customer-service:"))
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
