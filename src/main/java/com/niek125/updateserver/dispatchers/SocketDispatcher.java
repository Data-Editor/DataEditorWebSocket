package com.niek125.updateserver.dispatchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.niek125.updateserver.models.SocketMessage;
import com.niek125.updateserver.socket.SessionList;
import com.niek125.updateserver.models.SessionWrapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
public class SocketDispatcher implements Dispatcher<SocketMessage> {
    private final Logger logger = LoggerFactory.getLogger(SocketDispatcher.class);
    private final SessionList sessions;
    private final ObjectMapper mapper;

    @Override
    public void dispatch(SocketMessage message) {
        final List<SessionWrapper> sws = sessions.getSessions();
        final String interest = message.getInterest();
        final TextMessage textMessage;
        try {
            textMessage = new TextMessage(mapper.writeValueAsString(message.getHeader()) + "\n" + message.getPayload());
        } catch (JsonProcessingException e) {
            logger.error("could not parse socket message to json: {}", e.getMessage());
            return;
        }
        for (SessionWrapper webSocketSession : sws) {
            if (webSocketSession.getInterest().equals(interest)) {
                try {
                    webSocketSession.getSession().sendMessage(textMessage);
                } catch (IOException e) {
                    logger.debug("session closed: {}", e.getMessage());
                    sessions.removeSession(webSocketSession.getSession());
                }
            }
        }
    }
}
