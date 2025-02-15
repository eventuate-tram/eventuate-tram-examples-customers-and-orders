package io.eventuate.tram.spring.springwolf;

import io.eventuate.examples.tram.ordersandcustomers.customers.domain.EventPublisher;
import io.github.springwolf.asyncapi.v3.model.channel.ChannelObject;
import io.github.springwolf.asyncapi.v3.model.channel.message.MessageObject;
import io.github.springwolf.asyncapi.v3.model.channel.message.MessageReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChannelsFromEventPublishersScanner {

  @Autowired
  private ApplicationContext ctx;

  @Autowired
  private SpringWolfMessageFactory springWolfMessageFactory;

  Map<String, ChannelObject> findPublishingChannels() {
    List<EventPublisher> eventPublishers = ctx.getBeansOfType(EventPublisher.class).values().stream().toList();
    return eventPublishers.stream()
        .collect(Collectors.toMap(
            ep -> ep.getAggregateClass().getName(),
            ep -> makeChannelObjectFromEventPublisher(ep.getAggregateClass().getName(), ep.getEventBaseClass())
        ));
  }

  public ChannelObject makeChannelObjectFromEventPublisher(String aggregateType, Class eventBaseClass) {
    return ChannelObject.builder()
        .channelId(aggregateType)
        .messages(MessageClassScanner.findConcreteImplementorsOf(eventBaseClass).stream()
            .collect(Collectors.toMap(
                Class::getName, // key mapper
                this::makeMessageReference // value mapper
            )))
        .build();
  }

  private MessageReference makeMessageReference(Class eventClass) {
    MessageObject message = springWolfMessageFactory.makeMessageFromClass(eventClass);
    return MessageReference.toComponentMessage(message);
  }

}
