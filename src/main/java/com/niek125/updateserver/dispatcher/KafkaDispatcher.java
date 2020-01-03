package com.niek125.updateserver.dispatcher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.niek125.updateserver.models.SocketMessage;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;

@AllArgsConstructor
public class KafkaDispatcher implements Dispatcher {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper mapper;

    @Override
    public void dispatch(SocketMessage message) {
        try {
            DocumentContext json = JsonPath.parse(message.getPayload());
            json = json.put("$", "projectid", message.getSender().getInterest());
            message.setPayload(json.jsonString());
            kafkaTemplate.send(message.getHeader().getPayload(), mapper.writeValueAsString(message.getHeader()) + "\n" + message.getPayload());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
