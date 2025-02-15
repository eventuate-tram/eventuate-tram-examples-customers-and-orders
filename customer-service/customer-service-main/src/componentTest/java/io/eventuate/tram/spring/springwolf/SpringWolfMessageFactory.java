package io.eventuate.tram.spring.springwolf;

import io.github.springwolf.asyncapi.v3.model.channel.message.MessageObject;
import io.github.springwolf.asyncapi.v3.model.channel.message.MessagePayload;
import io.github.springwolf.asyncapi.v3.model.schema.MultiFormatSchema;
import io.github.springwolf.core.asyncapi.components.ComponentsService;
import io.github.springwolf.core.asyncapi.scanners.common.payload.PayloadSchemaObject;
import io.github.springwolf.core.asyncapi.scanners.common.payload.internal.PayloadService;
import org.springframework.beans.factory.annotation.Autowired;

public class SpringWolfMessageFactory {

  @Autowired
  private PayloadService payloadService;

  @Autowired
  private ComponentsService componentsService;

  MessageObject makeMessageFromClass(Class messageClass) {
    PayloadSchemaObject payloadSchemaObject = payloadService.buildSchema("application/json", messageClass);
    MessageObject message = MessageObject.builder()
        .messageId(messageClass.getName())
        .description("my event handler")
        .payload(MessagePayload.of(
            MultiFormatSchema.builder().schema(payloadSchemaObject.payload()).build()))
        .build();
    componentsService.registerMessage(message);
    return message;
  }
}
