package com.niek125.updateserver.converters.dataeditorevent;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.niek125.updateserver.converters.Converter;
import com.niek125.updateserver.events.DataEditorEvent;
import com.niek125.updateserver.events.RowEditedEvent;
import com.niek125.updateserver.models.Action;
import com.niek125.updateserver.models.SocketHeader;
import com.niek125.updateserver.models.SocketMessage;
import org.springframework.stereotype.Component;

@Component
public class RowEditedEventConverter extends Converter<RowEditedEvent, DataEditorEvent, SocketMessage> {

    public RowEditedEventConverter(){
        super(RowEditedEvent.class);
    }

    @Override
    public SocketMessage convert(DataEditorEvent object) {
        final RowEditedEvent event = (RowEditedEvent) object;
        DocumentContext payload = JsonPath.parse("{}");
        payload = payload.put("$", "projectid", event.getProjectid());
        payload.put("$","row", event.getRow());
        payload.put("$","rownumber",event.getRownumber());
        return new SocketMessage(new SocketHeader(Action.UPDATE, "data"),
                payload.jsonString(),
                event.getInterest());
    }
}
