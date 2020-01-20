package com.niek125.updateserver.converters.socketmessage;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.niek125.updateserver.converters.Converter;
import com.niek125.updateserver.events.DataEditorEvent;
import com.niek125.updateserver.events.RowCreatedEvent;
import com.niek125.updateserver.models.SocketMessage;
import org.springframework.stereotype.Component;

@Component
public class RowCreatedMessageConverter extends Converter<RowCreatedEvent, SocketMessage, DataEditorEvent> {
    public RowCreatedMessageConverter(){
        super(RowCreatedEvent.class);
    }

    @Override
    public RowCreatedEvent convert(SocketMessage object) {
        final DocumentContext json = JsonPath.parse(object.getPayload());
        return new RowCreatedEvent(object.getInterest(),json.read("$.projectid"), json.read("$.row"));
    }
}
