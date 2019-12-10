package com.niek125.updateserver.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.niek125.updateserver.models.Message;
import org.springframework.web.socket.TextMessage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class Handler {
    private ObjectMapper mapper;
    DateTimeFormatter formatter;

    public Handler() {
        mapper = new ObjectMapper();
        formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh-mm");
    }

    public boolean isValid(TextMessage message) throws JsonProcessingException {
        String[] headPay = message.getPayload().split("\n");
        Message msg = mapper.readValue(headPay[1], Message.class);
        if (
                        (Pattern.compile(".{8}-.{4}-.{4}-.{4}-.{12}").matcher(msg.getMessageid()).matches()) &&
                        (msg.getContent().length() > 0) &&
                        (msg.getContent().length() < 257) &&
                        (msg.getSendtime() == null) &&
                        (msg.getSenderid() == null)) {
            return true;
        }
        return false;
    }

    public String makeMessage(TextMessage message) throws JsonProcessingException {
        String[] headPay = message.getPayload().split("\n");
        Message msg = mapper.readValue(headPay[1], Message.class);
        msg.setSendtime(LocalDateTime.now().format(formatter));
        msg.setSenderid("m9lvWuHxJTgAOf4ugEcmea6sC0v1");
        return mapper.writeValueAsString(msg);
    }
}
