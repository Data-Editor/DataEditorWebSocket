package com.niek125.updateserver.parsers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.niek125.updateserver.models.Action;
import com.niek125.updateserver.models.SocketHeader;
import com.niek125.updateserver.models.SocketMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MessageParser implements Parser<SocketMessage> {
    private final Logger logger = LoggerFactory.getLogger(MessageParser.class);
    private final ObjectMapper objectMapper;
    private final Map<String, Map<Action,Class>> classMap;

    @Autowired
    public MessageParser(ObjectMapper objectMapper, Map<String, Map<Action, Class>> classMap) {
        this.objectMapper = objectMapper;
        this.classMap = classMap;
    }

    @Override
    public SocketMessage parse(String message) {
        try {
            final String[] pay = message.split("\n");
            return new SocketMessage(
                    objectMapper.readValue(pay[0], SocketHeader.class),
                    pay[1]);
        } catch (JsonProcessingException e) {
            logger.error("could not parse header: {}", e.getMessage());
        }
        return new SocketMessage(null, null);
    }

    @Override
    public Class getEventTargetClass(SocketMessage object) {
        return classMap
                .get(object.getHeader().getPayload())
                .get(object.getHeader().getAction());
    }
}
