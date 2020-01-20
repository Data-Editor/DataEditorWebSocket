package com.niek125.updateserver.converters.socketmessage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.niek125.updateserver.converters.Converter;
import com.niek125.updateserver.events.DataEditorEvent;
import com.niek125.updateserver.events.MessagePostedEvent;
import com.niek125.updateserver.models.Message;
import com.niek125.updateserver.models.SocketMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MessagePostedMessageConverter extends Converter<MessagePostedEvent, SocketMessage, DataEditorEvent> {
    private final Logger logger = LoggerFactory.getLogger(MessagePostedMessageConverter.class);
    private final ObjectMapper objectMapper;

    public MessagePostedMessageConverter(ObjectMapper objectMapper){
        super(MessagePostedEvent.class);
        this.objectMapper = objectMapper;
    }

    @Override
    public DataEditorEvent convert(SocketMessage object) {
        try {
            return new MessagePostedEvent(object.getInterest(), objectMapper.readValue(object.getPayload(), Message.class));
        } catch (JsonProcessingException e) {
            logger.error("could not parse json to message: {}", e.getMessage());
        }
        return new DataEditorEvent(null);
    }
}
