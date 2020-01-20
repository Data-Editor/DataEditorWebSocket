package com.niek125.updateserver.converters.socketmessage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.niek125.updateserver.converters.Converter;
import com.niek125.updateserver.events.DataEditorEvent;
import com.niek125.updateserver.events.UserRoleChangedEvent;
import com.niek125.updateserver.models.Role;
import com.niek125.updateserver.models.SocketMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UserRoleChangedMessageConverter extends Converter<UserRoleChangedEvent, SocketMessage, DataEditorEvent> {
    private final Logger logger = LoggerFactory.getLogger(UserRoleChangedMessageConverter.class);
    private final ObjectMapper objectMapper;

    public UserRoleChangedMessageConverter(ObjectMapper objectMapper) {
        super(UserRoleChangedEvent.class);
        this.objectMapper = objectMapper;
    }

    @Override
    public DataEditorEvent convert(SocketMessage object) {
        try {
            return new UserRoleChangedEvent(object.getInterest(), objectMapper.readValue(object.getPayload(), Role.class));
        } catch (
                JsonProcessingException e) {
            logger.error("could not parse json to message: {}", e.getMessage());
        }
        return new DataEditorEvent(null);
    }
}
