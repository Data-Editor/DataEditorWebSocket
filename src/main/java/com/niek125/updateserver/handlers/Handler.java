package com.niek125.updateserver.handlers;

import com.niek125.updateserver.models.SocketMessage;

public interface Handler {
    String getProcessType();
    boolean validate(SocketMessage message);
    void construct(SocketMessage message);
}
