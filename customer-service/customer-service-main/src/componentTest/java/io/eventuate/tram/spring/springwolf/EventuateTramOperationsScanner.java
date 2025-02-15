package io.eventuate.tram.spring.springwolf;

import io.github.springwolf.asyncapi.v3.model.operation.Operation;
import io.github.springwolf.core.asyncapi.scanners.OperationsScanner;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

public class EventuateTramOperationsScanner implements OperationsScanner {

  @Autowired
  private OperationsFromEventHandlersScanner operationsFromEventHandlersScanner;

  @Autowired
  private OperationsFromEventPublisherScanner operationsFromEventPublisherScanner;

  @Override
  public Map<String, Operation> scan() {
    Map<String, Operation> operations = new HashMap<>();
    operations.putAll(operationsFromEventHandlersScanner.scan());
    operations.putAll(operationsFromEventPublisherScanner.scan());
    return operations;
  }


}
