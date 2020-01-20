package com.niek125.updateserver.converters.socketmessage;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.niek125.updateserver.converters.Converter;
import com.niek125.updateserver.events.DataEditorEvent;
import com.niek125.updateserver.events.RowEditedEvent;
import com.niek125.updateserver.models.SocketMessage;
import org.springframework.stereotype.Component;

@Component
public class RowEditedMessageConverter extends Converter<RowEditedEvent, SocketMessage, DataEditorEvent> {

    public RowEditedMessageConverter(){
        super(RowEditedEvent.class);
    }

    @Override
    public DataEditorEvent convert(SocketMessage object) {
        final DocumentContext json = JsonPath.parse(object.getPayload());
        return new RowEditedEvent(object.getInterest(),json.read("$.projectid"),
                json.read("$.rownumber"), json.read("$.row"));
    }
}
