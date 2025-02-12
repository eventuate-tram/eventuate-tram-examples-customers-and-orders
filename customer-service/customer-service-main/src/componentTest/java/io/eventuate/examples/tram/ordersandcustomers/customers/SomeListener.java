package io.eventuate.examples.tram.ordersandcustomers.customers;

import io.github.springwolf.core.asyncapi.annotations.AsyncListener;
import io.github.springwolf.core.asyncapi.annotations.AsyncOperation;

public class SomeListener {

  @AsyncListener(
      operation = @AsyncOperation(
          channelName = "MyTopic",

          description = "More details for the incoming topic"
      )
  )
  public void someMethod(SomePayload somePayload) {

  }
}
