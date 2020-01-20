package com.niek125.updateserver.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.niek125.updateserver.models.Message;
import com.niek125.updateserver.models.SessionWrapper;
import com.niek125.updateserver.models.SocketMessage;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

@AllArgsConstructor
public class MessageHandler extends Handler {
    private final ObjectMapper mapper;
    private final DateTimeFormatter formatter;

    @Override
    public String getProcessType() {
        return "message";
    }

    @Override
    protected boolean hasPermission(SessionWrapper sessionWrapper) {
        return true;
    }

    @Override
    protected boolean validate(SocketMessage message) {//old does not support delete and edit
        final Message msg;
        try {
            msg = mapper.readValue(message.getPayload(), Message.class);
        } catch (JsonProcessingException e) {
            return false;
        }
        return Pattern.compile(".{8}-.{4}-.{4}-.{4}-.{12}").matcher(msg.getMessageid()).matches() &&
                        (msg.getContent().length() > 0) &&
                        (msg.getContent().length() < 257) &&
                        (msg.getSendtime() == null) &&
                        (msg.getSenderid() == null) &&
                        (msg.getProjectid() == null);
    }

    @Override
    protected void construct(SocketMessage message, SessionWrapper sessionWrapper) {
        try {
            final Message msg = mapper.readValue(message.getPayload(), Message.class);
            msg.setSendtime(LocalDateTime.now().format(formatter));
            msg.setSenderid(sessionWrapper.getToken().getClaim("uid").asString());
            msg.setProjectid(sessionWrapper.getInterest());
            message.setPayload(mapper.writeValueAsString(msg));
        } catch (JsonProcessingException e) {
            message.setPayload("");
        }
    }
}
