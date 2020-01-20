package com.niek125.updateserver.dispatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.niek125.updateserver.events.*;
import com.niek125.updateserver.socket.SessionList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class DispatcherConfig {

    @Bean
    @Autowired
    public KafkaDispatcher kafkaDispatcher(KafkaTemplate<String, String> template) {
        final Map<Class, String> topicMappings = new HashMap<>();

        topicMappings.put(MessagePostedEvent.class, "message");

        topicMappings.put(RowEditedEvent.class, "data");
        topicMappings.put(RowDeletedEvent.class, "data");
        topicMappings.put(RowCreatedEvent.class, "data");

        topicMappings.put(UserRoleChangedEvent.class,"role");
        topicMappings.put(UserAddedToProjectEvent.class, "role");

        return new KafkaDispatcher(template, new ObjectMapper(), topicMappings);
    }

    @Bean
    @Autowired
    public SocketDispatcher socketDispatcher(SessionList sessionList) {
        return new SocketDispatcher(sessionList, new ObjectMapper());
    }

}
