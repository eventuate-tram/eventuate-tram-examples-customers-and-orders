package io.eventuate.tram.spring.springwolf;

import io.github.springwolf.core.asyncapi.scanners.ChannelsScanner;
import io.github.springwolf.core.asyncapi.scanners.OperationsScanner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventuateSpringWolfConfiguration {

  @Bean
  public ChannelsScanner eventuateTramChannelsScanner() {
    return new EventuateTramChannelsScanner();
  }

  @Bean
  OperationsScanner eventuateTramOperationsScanner() {
    return new EventuateTramOperationsScanner();
  }

  @Bean
  OperationsFromEventHandlersScanner operationsFromEventHandlersScanner() {
    return new OperationsFromEventHandlersScanner();
  }

  @Bean
  OperationsFromEventPublisherScanner operationsFromEventPublisherScanner() {
    return new OperationsFromEventPublisherScanner();
  }

  @Bean
  ChannelsFromEventHandlersScanner channelsFromEventHandlersScanner() {
    return new ChannelsFromEventHandlersScanner();
  }

  @Bean
  ChannelsFromEventPublishersScanner channelsFromEventPublishersScanner() {
    return new ChannelsFromEventPublishersScanner();
  }

  @Bean
  EventuateSpringWolfConfiguration eventuateSpringWolfConfiguration() {
    return new EventuateSpringWolfConfiguration();
  }

  @Bean
  SpringWolfMessageFactory springWolfMessageFactory() {
    return new SpringWolfMessageFactory();
  }

}
