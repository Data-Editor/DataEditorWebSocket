package com.niek125.updateserver.socket;

import com.niek125.updateserver.models.SessionWrapper;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class SessionList {
    private final List<SessionWrapper> sessions;

    public SessionList() {
        sessions = new CopyOnWriteArrayList<>();
    }

    public List<SessionWrapper> getSessions() {
        return new ArrayList<>(sessions);
    }

    public SessionWrapper getSession(WebSocketSession session) throws Exception {
        List<SessionWrapper> sws = sessions.stream().filter(x -> x.getSession() == session).collect(Collectors.toList());
        if (sws.size() == 0) {
            throw new Exception(String.format("Session %s not found", session.getId()));
        }
        if (sws.size() > 1) {
            throw new Exception("Multiple session %s not found");
        }
        return sws.get(0);
    }

    public void addSession(WebSocketSession session) {
        sessions.add(new SessionWrapper(session));
    }

    public void removeSession(WebSocketSession session) {
        try {
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        sessions.removeIf(x -> x.getSession() == session);
    }
}
