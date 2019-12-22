package com.niek125.updateserver.dispatcher;

import com.niek125.updateserver.models.SocketMessage;

public interface Dispatcher {
    void dispatch(SocketMessage message);
}
