package com.niek125.updateserver.socket;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.niek125.updateserver.handlers.Handler;
import com.niek125.updateserver.kafka.Producer;
import com.niek125.updateserver.models.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import static com.niek125.updateserver.utils.PemUtils.readPublicKeyFromFile;

@Component
public class SocketHandler extends TextWebSocketHandler {
    private List<SessionWrapper> sessions;
    private Handler handler;
    private Producer producer;
    private ObjectMapper objectMapper;

    @Autowired
    public SocketHandler(Producer producer) {
        sessions = new CopyOnWriteArrayList<>();
        handler = new Handler();
        this.producer = new Producer();
        this.objectMapper = new ObjectMapper();
    }

    private void closeConnection(WebSocketSession session) throws IOException {
        System.out.println("closed");
        session.close();
        afterConnectionClosed(session, CloseStatus.NOT_ACCEPTABLE);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        System.out.println(message.getPayload());
        SessionWrapper sessionWrapper = sessions.stream().filter(x -> x.getSession() == session).collect(Collectors.toList()).get(0);
        if (sessionWrapper.getInterst() == "") {
            sessionWrapper.setInterst(message.getPayload().split("\n")[0]);
            DecodedJWT jwt = null;
            try {
                Algorithm algorithm = Algorithm.RSA512((RSAPublicKey) readPublicKeyFromFile("src/main/resources/PublicKey.pem", "RSA"), null);
                JWTVerifier verifier = JWT.require(algorithm)
                        .withIssuer("data-editor-token-service")
                        .build();
                sessionWrapper.setUserData(verifier.verify(message.getPayload().split("\n")[1]));
                Role[] perms = objectMapper.readValue(sessionWrapper.getUserData().getClaims().get("pms").asString(), Role[].class);
                if(!Arrays.stream(perms).filter(x -> x.getProjectid().equals(sessionWrapper.getInterst())).findFirst().isPresent()){
                    closeConnection(session);
                }
            } catch (JWTVerificationException | IOException exception) {
                exception.printStackTrace();
                closeConnection(session);
            }
        } else if (handler.isValid(message)) {
            String interest = sessionWrapper.getInterst();
            String content = handler.makeMessage(message, sessionWrapper.getUserData());
            producer.sendMessage(message.getPayload().split("\n")[0] + "\n" + content);
            for (SessionWrapper webSocketSession : sessions) {
                if (webSocketSession.getInterst().equals(interest)) {
                    webSocketSession.getSession().sendMessage(new TextMessage(content));
                }
            }
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(new SessionWrapper(session, "", null));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.removeIf(x -> x.getSession() == session);
    }
}
