package com.niek125.updateserver.models;

import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

public class SessionWrapper {
    @Getter
    private final WebSocketSession session;
    @Setter
    @Getter
    private String interest;
    @Setter
    @Getter
    private DecodedJWT token;

    public SessionWrapper(WebSocketSession webSocketSession){
        this.session = webSocketSession;
    }

    public boolean isComplete(){
        return !(token == null || interest == null || session == null);
    }
}
