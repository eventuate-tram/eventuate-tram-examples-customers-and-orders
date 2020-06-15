package net.chrisrichardson.eventstore.examples.customersandorders.views.orderhistory;

import io.eventuate.examples.tram.ordersandcustomers.orderhistory.backend.OrderHistoryViewBackendConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableAutoConfiguration(exclude = io.eventuate.util.spring.swagger.CommonSwaggerConfiguration.class)
@Import(OrderHistoryViewBackendConfiguration.class)
public class OrderHistoryViewServiceIntegrationTestConfiguration {
}
