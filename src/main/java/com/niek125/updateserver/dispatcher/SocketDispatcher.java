package com.niek125.updateserver.dispatcher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.niek125.updateserver.models.SocketMessage;
import com.niek125.updateserver.socket.SessionList;
import com.niek125.updateserver.models.SessionWrapper;
import lombok.AllArgsConstructor;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
public class SocketDispatcher implements Dispatcher {
    private final SessionList sessions;
    private final ObjectMapper mapper;

    @Override
    public void dispatch(SocketMessage message) {
        final List<SessionWrapper> sws = sessions.getSessions();
        final String interest = message.getSender().getInterest();
        final TextMessage textMessage;
        try {
            textMessage = new TextMessage(mapper.writeValueAsString(message.getPayload()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return;
        }
        for (SessionWrapper webSocketSession : sws) {
            if (webSocketSession.getInterest().equals(interest)) {
                try {
                    webSocketSession.getSession().sendMessage(textMessage);
                } catch (IOException e) {
                    sessions.removeSession(webSocketSession.getSession());
                }
            }
        }
    }
}
