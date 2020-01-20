package com.niek125.updateserver.converters.dataeditorevent;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.niek125.updateserver.converters.Converter;
import com.niek125.updateserver.events.DataEditorEvent;
import com.niek125.updateserver.events.RowCreatedEvent;
import com.niek125.updateserver.models.Action;
import com.niek125.updateserver.models.SocketHeader;
import com.niek125.updateserver.models.SocketMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RowCreatedEventConverter extends Converter<RowCreatedEvent, DataEditorEvent, SocketMessage> {
    @Autowired
    public RowCreatedEventConverter() {
        super(RowCreatedEvent.class);
    }

    @Override
    public SocketMessage convert(DataEditorEvent object) {
        final RowCreatedEvent rowCreatedEvent = (RowCreatedEvent) object;
        DocumentContext payload = JsonPath.parse("{}");
        payload = payload.put("$", "projectid", rowCreatedEvent.getProjectid());
        payload = payload.put("$", "row", rowCreatedEvent.getRow());
        return new SocketMessage(
                new SocketHeader(Action.CREATE, "data"),
                payload.jsonString(),
                object.getInterest()
        );
    }
}
