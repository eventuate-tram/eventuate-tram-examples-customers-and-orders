package io.eventuate.tram.spring.springwolf;

import io.eventuate.examples.tram.ordersandcustomers.customers.domain.EventPublisher;
import io.github.springwolf.asyncapi.v3.model.channel.ChannelReference;
import io.github.springwolf.asyncapi.v3.model.operation.Operation;
import io.github.springwolf.asyncapi.v3.model.operation.OperationAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.Map;
import java.util.stream.Collectors;

import static io.eventuate.tram.spring.springwolf.MessageClassScanner.findConcreteImplementorsOf;

public class OperationsFromEventPublisherScanner {

  @Autowired
  private ApplicationContext ctx;

  Map<String, ? extends Operation> scan() {
    return ctx.getBeansOfType(EventPublisher.class).values().stream()
        .collect(Collectors.toMap(
            ep -> ep.getClass().getName(),
            this::makeOperationFromEventPublisher
        ));
  }

  private Operation makeOperationFromEventPublisher(EventPublisher ep) {
    return Operation.builder()
        .channel(ChannelReference.builder()
            .ref("#/channels/" + ep.getAggregateClass().getName())
            .build())
        .operationId(ep.getClass().getName())
        .description("my event sender")
        .action(OperationAction.SEND)
        .messages(findConcreteImplementorsOf(ep.getEventBaseClass()).stream()
            .map(SpringWolfUtils::makeMessageReferenceFromEventClass)
            .toList())
        .build();
  }

}
