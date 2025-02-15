package io.eventuate.tram.spring.springwolf;

import io.github.springwolf.asyncapi.v3.model.channel.ChannelObject;
import io.github.springwolf.core.asyncapi.scanners.ChannelsScanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class EventuateTramChannelsScanner implements ChannelsScanner {


  @Autowired
  ChannelsFromEventPublishersScanner channelsFromEventPublishersScanner;

  @Autowired
  private ChannelsFromEventHandlersScanner channelsFromEventHandlersScanner;


  @Override
  public Map<String, ChannelObject> scan() {
    Map<String, ChannelObject> channels = new HashMap<>();

    channels.putAll(channelsFromEventHandlersScanner.makeChannelsFromEventHandlers());

    channels.putAll(channelsFromEventPublishersScanner.findPublishingChannels());

    return channels;
  }


}
