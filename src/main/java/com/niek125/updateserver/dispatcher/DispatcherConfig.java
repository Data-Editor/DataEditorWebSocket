package com.niek125.updateserver.dispatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.niek125.updateserver.socket.SessionList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class DispatcherConfig {

    @Bean
    @Autowired
    public KafkaDispatcher kafkaDispatcher(KafkaTemplate<String, String> template) {
        return new KafkaDispatcher(template, new ObjectMapper());
    }

    @Bean
    @Autowired
    public SocketDispatcher socketDispatcher(SessionList sessionList) {
        return new SocketDispatcher(sessionList, new ObjectMapper());
    }

}
