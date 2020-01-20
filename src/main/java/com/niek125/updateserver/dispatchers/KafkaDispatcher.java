package com.niek125.updateserver.dispatchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.niek125.updateserver.events.DataEditorEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Map;

public class KafkaDispatcher implements Dispatcher<DataEditorEvent> {
    private final Logger logger = LoggerFactory.getLogger(KafkaDispatcher.class);
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper mapper;
    private final Map<Class, String> topicMappings;

    public KafkaDispatcher(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper mapper, Map<Class, String> topicMappings) {
        this.kafkaTemplate = kafkaTemplate;
        this.mapper = mapper;
        this.topicMappings = topicMappings;
    }

    @Override
    public void dispatch(DataEditorEvent event) {
        try {
            final Class claz = event.getClass();
            DocumentContext json = JsonPath.parse("{}");
            json = json.put("$", "class", claz.getSimpleName());
            json = json.put("$", "event", mapper.writeValueAsString(event));
            kafkaTemplate.send(topicMappings.get(claz), json.jsonString());
        } catch (JsonProcessingException e) {
            logger.error("could not map event to json: {}", e.getMessage());
        }
    }
}
