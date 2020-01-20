package com.niek125.updateserver.socket;

import com.niek125.updateserver.handlers.HandlerExecutor;
import com.niek125.updateserver.handlers.TokenHandler;
import com.niek125.updateserver.models.SessionWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class SocketHandler extends TextWebSocketHandler {
    private final Logger logger = LoggerFactory.getLogger(SocketHandler.class);
    private final SessionList sessionList;
    private final TokenHandler tokenHandler;
    private final HandlerExecutor handlerExecutor;

    @Autowired
    public SocketHandler(SessionList sessionList, TokenHandler tokenHandler, HandlerExecutor handlerExecutor) {
        this.sessionList = sessionList;
        this.tokenHandler = tokenHandler;
        this.handlerExecutor = handlerExecutor;
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        logger.info("received message: {}", message.getPayload());
        final SessionWrapper sessionWrapper = sessionList.getSession(session);
        if (!sessionWrapper.isComplete()) {
            if (!tokenHandler.setToken(message.getPayload(), sessionWrapper)) {
                sessionList.removeSession(session);
            }
        } else if (!handlerExecutor.handle(message.getPayload(), sessionWrapper)) {
            sessionList.removeSession(session);
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessionList.addSession(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessionList.removeSession(session);
    }
}
