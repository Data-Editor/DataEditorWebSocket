package com.niek125.updateserver.socket;

import com.niek125.updateserver.handlers.Handler;
import com.niek125.updateserver.kafka.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Component
public class SocketHandler extends TextWebSocketHandler {
    private List<SessionWrapper> sessions;
    private Handler handler;
    private Producer producer;

    @Autowired
    public SocketHandler(Producer producer) {
        sessions = new CopyOnWriteArrayList<>();
        handler = new Handler();
        this.producer = new Producer();
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws InterruptedException, IOException {
        SessionWrapper sessionWrapper = sessions.stream().filter(x -> x.getSession() == session).collect(Collectors.toList()).get(0);
        if (sessionWrapper.getInterst() == "") {
            sessionWrapper.setInterst(message.getPayload());
        } else if (handler.isValid(message)) {
            String interest = sessionWrapper.getInterst();
            String content = handler.makeMessage(message);
            producer.sendMessage(message.getPayload());
            for (SessionWrapper webSocketSession : sessions) {
                if (webSocketSession.getInterst().equals(interest)) {
                    webSocketSession.getSession().sendMessage(new TextMessage(content));
                }
            }
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(new SessionWrapper(session));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.removeIf(x -> x.getSession() == session);
    }
}
