package io.eventuate.tram.spring.springwolf;

import io.github.springwolf.asyncapi.v3.model.channel.message.MessageReference;

public class SpringWolfUtils {
  public static MessageReference makeMessageReferenceFromEventClass(Class<?> eventClass) {
    return MessageReference.toComponentMessage(eventClass.getName());
  }
}
