package com.niek125.updateserver.socket;

import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.niek125.updateserver.dispatcher.Dispatcher;
import com.niek125.updateserver.handlers.Handler;
import com.niek125.updateserver.models.Role;
import com.niek125.updateserver.models.SessionWrapper;
import com.niek125.updateserver.models.SocketHeader;
import com.niek125.updateserver.models.SocketMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SocketHandler extends TextWebSocketHandler {
    private final SessionList sessionList;
    private final List<Dispatcher> dispatchers;
    private final List<Handler> handlers;
    private final ObjectMapper mapper;

    @Autowired
    public SocketHandler(SessionList sessionList, List<Dispatcher> dispatchers, List<Handler> handlers, ObjectMapper mapper) {
        this.sessionList = sessionList;
        this.dispatchers = dispatchers;
        this.handlers = handlers;
        this.mapper = mapper;
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println(message.getPayload());
        final SessionWrapper sessionWrapper = sessionList.getSession(session);
        final String[] pay = message.getPayload().split("\n");
        final SocketMessage socketMessage = new SocketMessage(mapper.readValue(pay[0], SocketHeader.class), pay[1], sessionWrapper);
        final Handler handler = handlers.stream().filter(x -> x.getProcessType().equals(socketMessage.getHeader().getPayload())).collect(Collectors.toList()).get(0);
        if (!sessionWrapper.isComplete() && handler.getProcessType() == "token") {
            handler.validate(socketMessage);
        } else if (handler.validate(socketMessage)) {
            handler.construct(socketMessage);
            for (Dispatcher dispatcher :
                    dispatchers) {
                dispatcher.dispatch(socketMessage);
            }
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
