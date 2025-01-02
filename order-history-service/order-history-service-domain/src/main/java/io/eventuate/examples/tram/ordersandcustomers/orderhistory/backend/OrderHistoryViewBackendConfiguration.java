package io.eventuate.examples.tram.ordersandcustomers.orderhistory.backend;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({OrderHistoryViewMongoConfiguration.class,})
public class OrderHistoryViewBackendConfiguration {
}
