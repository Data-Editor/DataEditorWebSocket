package com.niek125.updateserver.converters.socketmessage;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.niek125.updateserver.converters.Converter;
import com.niek125.updateserver.events.DataEditorEvent;
import com.niek125.updateserver.events.RowDeletedEvent;
import com.niek125.updateserver.models.SocketMessage;
import org.springframework.stereotype.Component;

@Component
public class RowDeletedMessageConverter extends Converter<RowDeletedEvent, SocketMessage, DataEditorEvent> {

    public RowDeletedMessageConverter(){
        super(RowDeletedEvent.class);
    }

    @Override
    public DataEditorEvent convert(SocketMessage object) {
        final DocumentContext json = JsonPath.parse(object.getPayload());
        return new RowDeletedEvent(object.getInterest(),json.read("$.projectid"), json.read("$.rownumber"));
    }
}
