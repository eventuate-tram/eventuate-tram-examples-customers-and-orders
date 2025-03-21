package io.eventuate.examples.tram.customersandorders.endtoendtests;

import io.eventuate.cdc.testcontainers.EventuateCdcContainer;
import io.eventuate.common.testcontainers.ContainerTestUtil;
import io.eventuate.common.testcontainers.DatabaseContainerFactory;
import io.eventuate.common.testcontainers.EventuateDatabaseContainer;
import io.eventuate.common.testcontainers.EventuateGenericContainer;
import io.eventuate.messaging.kafka.testcontainers.EventuateKafkaNativeCluster;
import io.eventuate.messaging.kafka.testcontainers.EventuateKafkaNativeContainer;
import io.eventuate.testcontainers.service.ServiceContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.lifecycle.Startables;

public class ApplicationUnderTestUsingTestContainers extends ApplicationUnderTest {
  private final ServiceContainer customerService
          // should rebuild
          ;
  private final ServiceContainer orderService
          // should rebuild
          ;
  private final ServiceContainer apiGatewayService;
  private final EventuateCdcContainer cdc
          // State for deleted databases is persisted in Kafka
          ;
  private final MongoDBContainer mongoDBContainer;
  private final ServiceContainer orderHistoryService;

  public ApplicationUnderTestUsingTestContainers() {
    EventuateKafkaNativeCluster eventuateKafkaCluster = new EventuateKafkaNativeCluster("CustomersAndOrdersEndToEndTest");

    EventuateKafkaNativeContainer kafka = eventuateKafkaCluster.kafka
        .withReuse(ContainerTestUtil.shouldReuse())
        .withNetworkAliases("kafka");

    EventuateDatabaseContainer<?> customerServiceDatabase = DatabaseContainerFactory.makeVanillaDatabaseContainer()
        .withNetwork(eventuateKafkaCluster.network)
        .withNetworkAliases("customer-service-db")
        .withReuse(false);
    // This results in only one DB!
    EventuateDatabaseContainer<?> orderServiceDatabase = DatabaseContainerFactory.makeVanillaDatabaseContainer()
        .withNetwork(eventuateKafkaCluster.network)
        .withNetworkAliases("order-service-db")
        .withReuse(false);
    customerService = ServiceContainer.makeFromDockerfileInFileSystem("../customer-service/customer-service-main/Dockerfile")
            .withNetwork(eventuateKafkaCluster.network)
            .withNetworkAliases("customer-service")
            .withDatabase(customerServiceDatabase)
            .withKafka(kafka)
            .withLogConsumer(new Slf4jLogConsumer(logger).withPrefix("SVC customer-service:"))
            .withReuse(false)
            .withStartupTimeout(ContainerTestUtil.startupTimeout());
            ;
    orderService = ServiceContainer.makeFromDockerfileInFileSystem("../order-service/order-service-main/Dockerfile")
            .withNetwork(eventuateKafkaCluster.network)
            .withNetworkAliases("order-service")
            .withDatabase(orderServiceDatabase)
            .withKafka(kafka)
            .withLogConsumer(new Slf4jLogConsumer(logger).withPrefix("SVC order-service:"))
            .withReuse(false)
            .withStartupTimeout(ContainerTestUtil.startupTimeout());
    mongoDBContainer = new MongoDBContainer("mongo:8.0.4")
        .withNetwork(eventuateKafkaCluster.network)
        .withNetworkAliases("order-history-service-db")
        .withReuse(ContainerTestUtil.shouldReuse());
    orderHistoryService = ServiceContainer.makeFromDockerfileInFileSystem("../order-history-service/order-history-service-main/Dockerfile")
            .withExposedPorts(8080)
            .withNetwork(eventuateKafkaCluster.network)
            .withNetworkAliases("order-history-service")
            .withKafka(kafka)
            .withEnv("SPRING_DATA_MONGODB_URI", "mongodb://order-history-service-db/customers_and_orders")
            .dependsOn(mongoDBContainer)
            .withLogConsumer(new Slf4jLogConsumer(logger).withPrefix("SVC order-history-service:"))
            .withReuse(false)
            .withStartupTimeout(ContainerTestUtil.startupTimeout());
    apiGatewayService = ServiceContainer.makeFromDockerfileInFileSystem("../api-gateway-service/api-gateway-service-main/Dockerfile")
            .withNetwork(eventuateKafkaCluster.network)
            .withExposedPorts(8080)
            .withEnv("CUSTOMER_DESTINATIONS_CUSTOMERSERVICEURL", "http://customer-service:8080")
            .withEnv("ORDER_DESTINATIONS_ORDERSERVICEURL", "http://order-service:8080")
            .withEnv("ORDERHISTORY_DESTINATIONS_ORDERHISTORYSERVICEURL", "http://order-history-service:8080")
            .withEnv("JAVA_OPTS", "-Ddebug")
            .withEnv("APIGATEWAY_TIMEOUT_MILLIS", "1000")
            .withLogConsumer(new Slf4jLogConsumer(logger).withPrefix("SVC api-gateway-service:"))
            .withLabel("io.eventuate.name", "api-gateway-service")
            .withReuse(false);
    cdc = new EventuateCdcContainer()
            .withKafka(kafka)
            .withKafkaLeadership()
            .withTramPipeline(customerServiceDatabase)
            .withTramPipeline(orderServiceDatabase)
            .dependsOn(customerService, orderService)
            .withReuse(false);
  }

  @Override
  public void start() {
    Startables.deepStart(cdc, apiGatewayService, orderHistoryService).join();
//      for (EventuateGenericContainer<? extends EventuateGenericContainer<?>> container : List.of(zookeeper, kafka, customerServiceDatabase, orderServiceDatabase, customerService, orderService, apiGatewayService, cdc)) {
//          System.out.println("Starting " + container.getClass().getSimpleName() + "," + container);
//          startContainer(container);
//      }
  }

  private void startContainer(EventuateGenericContainer<?> container) {
    String name = container.getFirstNetworkAlias();

    Slf4jLogConsumer logConsumer2 = new Slf4jLogConsumer(logger).withPrefix("SVC " + name + ":");
    System.out.println("============ Starting " + container.getClass().getSimpleName() + "," + container);
    container.start();
    System.out.println("============ Started " + container.getClass().getSimpleName() + "," + container);
    container.followOutput(logConsumer2);

  }

  @Override
  public int getCustomerServicePort() {
      return customerService.getFirstMappedPort();
  }

  @Override
  public int getApigatewayPort() {
      return apiGatewayService.getFirstMappedPort();
  }

  @Override
  public int getOrderServicePort() {
      return orderService.getFirstMappedPort();
  }

  public int getOrderHistoryServicePort() {
      return orderHistoryService.getFirstMappedPort();
  }

  @Override
  boolean exposesSwaggerUiForBackendServices() {
    return true;
  }


}
