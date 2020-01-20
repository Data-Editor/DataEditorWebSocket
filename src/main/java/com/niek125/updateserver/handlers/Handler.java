package com.niek125.updateserver.handlers;

import com.niek125.updateserver.models.SessionWrapper;
import com.niek125.updateserver.models.SocketMessage;

//deprecation when front-end event based
public abstract class Handler {
    public abstract String getProcessType();
    protected abstract boolean hasPermission(SessionWrapper sessionWrapper);
    protected abstract boolean validate(SocketMessage message);
    protected abstract void construct(SocketMessage message, SessionWrapper sessionWrapper);

    public boolean handle(SocketMessage socketMessage, SessionWrapper sessionWrapper){
        if(!hasPermission(sessionWrapper)
                && !validate(socketMessage)){
            return false;
        }
        construct(socketMessage, sessionWrapper);
        return true;
    }
}
