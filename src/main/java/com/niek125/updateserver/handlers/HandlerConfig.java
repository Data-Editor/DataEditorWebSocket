package com.niek125.updateserver.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.format.DateTimeFormatter;

@Configuration
public class HandlerConfig {

    @Bean
    public MessageHandler messageHandler(){
        return new MessageHandler(new ObjectMapper(), DateTimeFormatter.ofPattern("dd-MM-yyyy hh-mm"));
    }

}
