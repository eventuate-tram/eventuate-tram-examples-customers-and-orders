package io.eventuate.examples.tram.ordersandcustomers.customers;

import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.github.springwolf.asyncapi.v3.model.channel.ChannelObject;
import io.github.springwolf.core.asyncapi.scanners.ChannelsScanner;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MyChannelsScanner implements ChannelsScanner {

  @Autowired
  private ApplicationContext ctx;

  @Override
  public Map<String, ChannelObject> scan() {
    List<DomainEventHandlers> domainEventHandlers = ctx.getBeansOfType(DomainEventHandlers.class).values().stream()
        .collect(Collectors.toList());

    Map<String, ChannelObject> channels = domainEventHandlers.stream()
        .flatMap(deh -> deh.getAggregateTypesAndEvents().stream())
        .collect(Collectors.toMap(
            str -> str, // key mapper
            str -> makeChannelObject(str)));

    return channels; // value mapper
  }

  private static @NotNull ChannelObject makeChannelObject(String str) {
    return ChannelObject.builder()
        .channelId(str)
        .build();
  }
}
