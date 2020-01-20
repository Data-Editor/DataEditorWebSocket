package com.niek125.updateserver.converters.dataeditorevent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.niek125.updateserver.converters.Converter;
import com.niek125.updateserver.events.DataEditorEvent;
import com.niek125.updateserver.events.MessagePostedEvent;
import com.niek125.updateserver.models.Action;
import com.niek125.updateserver.models.SocketHeader;
import com.niek125.updateserver.models.SocketMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MessagePostedEventConverter extends Converter<MessagePostedEvent, DataEditorEvent, SocketMessage> {
    private final Logger logger = LoggerFactory.getLogger(MessagePostedEventConverter.class);
    private final ObjectMapper mapper;

    public MessagePostedEventConverter(ObjectMapper mapper){
        super(MessagePostedEvent.class);
        this.mapper = mapper;
    }

    @Override
    public SocketMessage convert(DataEditorEvent object) {
        try {
            final MessagePostedEvent event = (MessagePostedEvent) object;
            return new SocketMessage(
                    new SocketHeader(Action.CREATE, "message"),
                    mapper.writeValueAsString(event.getMessage()),
                    object.getInterest());
        } catch (JsonProcessingException e) {
            logger.error("could not parse message to json: {}", e.getMessage());
        }
        return new SocketMessage(null, null);
    }
}
