package com.niek125.updateserver.converters.dataeditorevent;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.niek125.updateserver.converters.Converter;
import com.niek125.updateserver.events.DataEditorEvent;
import com.niek125.updateserver.events.RowDeletedEvent;
import com.niek125.updateserver.models.Action;
import com.niek125.updateserver.models.SocketHeader;
import com.niek125.updateserver.models.SocketMessage;
import org.springframework.stereotype.Component;

@Component
public class RowDeletedEventConverter extends Converter<RowDeletedEvent, DataEditorEvent, SocketMessage> {
    public RowDeletedEventConverter() {
        super(RowDeletedEvent.class);
    }

    @Override
    public SocketMessage convert(DataEditorEvent object) {
        final RowDeletedEvent event = (RowDeletedEvent) object;
        DocumentContext payload = JsonPath.parse("{}");
        payload = payload.put("$", "projectid", event.getProjectid());
        payload = payload.put("$", "rownumber", event.getRownumber());
        return new SocketMessage(new SocketHeader(Action.DELETE, "data"),
                payload.jsonString(),
                object.getInterest());
    }
}
