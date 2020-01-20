package com.niek125.updateserver.parsers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.niek125.updateserver.events.DataEditorEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EventParser implements Parser<DataEditorEvent> {
    private final Logger logger = LoggerFactory.getLogger(EventParser.class);
    private final ObjectMapper objectMapper;
    @Value("${com.niek125.eventfolder}")
    private String eventfolder;

    @Autowired
    public EventParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public DataEditorEvent parse(String json) {
        try {
            final String eventName = JsonPath.read(json, "$.class");
            final Class claz = Class.forName(eventfolder + eventName);
            return (DataEditorEvent) objectMapper.readValue((String) JsonPath.read(json, "$.event"), claz);
        } catch (JsonProcessingException e) {
            logger.info("event does not have the right structure");
        } catch (ClassNotFoundException e) {
            logger.debug("event not found");
        }
        return new DataEditorEvent(null);
    }

    @Override
    public Class getEventTargetClass(DataEditorEvent object) {
        return object.getClass();
    }
}
