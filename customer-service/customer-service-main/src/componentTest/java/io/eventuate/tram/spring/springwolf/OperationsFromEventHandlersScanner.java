package io.eventuate.tram.spring.springwolf;

import io.eventuate.tram.events.subscriber.DomainEventHandler;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.github.springwolf.asyncapi.v3.model.channel.ChannelReference;
import io.github.springwolf.asyncapi.v3.model.operation.Operation;
import io.github.springwolf.asyncapi.v3.model.operation.OperationAction;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OperationsFromEventHandlersScanner {

  @Autowired
  private ApplicationContext ctx;


  @NotNull
  public Map<String, Operation> scan() {
    List<DomainEventHandlers> domainEventHandlers = DomainEventDispatcherHacks.getDomainEventHandlers(ctx);

    Map<String, List<DomainEventHandler>> aggregateTypeToEvents = domainEventHandlers.stream()
        .flatMap(dehs -> dehs.getHandlers().stream())
        .collect(Collectors.groupingBy(DomainEventHandler::getAggregateType));


    return aggregateTypeToEvents.entrySet().stream()
        .collect(Collectors.toMap(
            Map.Entry::getKey, // key mapper
            entry -> makeOperationForDomainEventHandlers(entry.getKey(), entry.getValue()) // value mapper
        ));
  }

  public static @NotNull Operation makeOperationForDomainEventHandlers(String aggregateType, List<DomainEventHandler> eventHandlers) {
    return Operation.builder()
        .channel(ChannelReference.builder()
            .ref("#/channels/" + aggregateType)
            .build())
        .operationId("operationId")
        .description("my event handler")
        .action(OperationAction.RECEIVE)
        .messages(eventHandlers.stream()
            .map(deh -> SpringWolfUtils.makeMessageReferenceFromEventClass(deh.getEventClass()))
            .toList())
        .build();
  }

}
