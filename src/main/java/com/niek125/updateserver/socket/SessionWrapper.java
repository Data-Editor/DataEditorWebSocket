package com.niek125.updateserver.socket;

import org.springframework.web.socket.WebSocketSession;

public class SessionWrapper {
    private WebSocketSession session;
    private String interst;

    public SessionWrapper(WebSocketSession session) {
        this.session = session;
        this.interst = "";
    }

    public WebSocketSession getSession() {
        return session;
    }

    public String getInterst() {
        return interst;
    }

    public void setInterst(String interst) {
        this.interst = interst;
    }
}
