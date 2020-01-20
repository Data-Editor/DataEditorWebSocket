package com.niek125.updateserver.converters.socketmessage;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.niek125.updateserver.converters.Converter;
import com.niek125.updateserver.events.DataEditorEvent;
import com.niek125.updateserver.events.UserAddedToProjectEvent;
import com.niek125.updateserver.models.SocketMessage;
import org.springframework.stereotype.Component;

@Component
public class UserAddedToProjectMessageConverter extends Converter<UserAddedToProjectEvent, SocketMessage, DataEditorEvent> {

    public UserAddedToProjectMessageConverter() {
        super(UserAddedToProjectEvent.class);
    }

    @Override
    public DataEditorEvent convert(SocketMessage object) {
        final DocumentContext json = JsonPath.parse(object.getPayload());
        return new UserAddedToProjectEvent(object.getInterest(), json.read("$.userid"), object.getInterest());
    }
}
