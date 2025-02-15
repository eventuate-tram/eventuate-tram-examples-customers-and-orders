package io.eventuate.tram.spring.springwolf;

import io.eventuate.tram.events.subscriber.DomainEventDispatcher;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public class DomainEventDispatcherHacks {
  static @NotNull List<DomainEventHandlers> getDomainEventHandlers(ApplicationContext ctx) {
    List<DomainEventDispatcher> dispatchers = ctx.getBeansOfType(DomainEventDispatcher.class).values().stream().toList();

    System.out.println(dispatchers);
    List<DomainEventHandlers> domainEventHandlers = dispatchers.stream()
        .map(dispatcher -> getDomainEventHandlers(dispatcher))
        .collect(Collectors.toList());
    return domainEventHandlers;
  }

  private static DomainEventHandlers getDomainEventHandlers(DomainEventDispatcher dispatcher) {
    try {
        Field field = DomainEventDispatcher.class.getDeclaredField("domainEventHandlers");
        field.setAccessible(true);
        return (DomainEventHandlers) field.get(dispatcher);
    } catch (NoSuchFieldException | IllegalAccessException e) {
        throw new RuntimeException("Failed to access domainEventHandlers field", e);
    }
}
}
