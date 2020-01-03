package com.niek125.updateserver.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.niek125.updateserver.models.Message;
import com.niek125.updateserver.models.SocketMessage;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

@AllArgsConstructor
public class MessageHandler implements Handler {
    private final ObjectMapper mapper;
    private final DateTimeFormatter formatter;

    @Override
    public String getProcessType() {
        return "message";
    }

    @Override
    public boolean validate(SocketMessage message) {//old does not support delete and edit
        final Message msg;
        try {
            msg = mapper.readValue(message.getPayload(), Message.class);
        } catch (JsonProcessingException e) {
            return false;
        }
        if (
                !((Pattern.compile(".{8}-.{4}-.{4}-.{4}-.{12}").matcher(msg.getMessageid()).matches()) &&
                        (msg.getContent().length() > 0) &&
                        (msg.getContent().length() < 257) &&
                        (msg.getSendtime() == null) &&
                        (msg.getSenderid() == null))) {
            return false;
        }
        return true;
    }

    @Override
    public void construct(SocketMessage message) {
        try {
            final Message msg = mapper.readValue(message.getPayload(), Message.class);
            msg.setSendtime(LocalDateTime.now().format(formatter));
            msg.setSenderid(message.getSender().getToken().getClaim("uid").asString());
            message.setPayload(mapper.writeValueAsString(msg));
        } catch (JsonProcessingException e) {
            message.setPayload("");
        }
    }
}
