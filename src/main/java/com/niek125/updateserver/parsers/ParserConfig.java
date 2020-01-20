package com.niek125.updateserver.parsers;

import com.niek125.updateserver.events.*;
import com.niek125.updateserver.models.Action;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ParserConfig {
    @Bean
    public Map<String, Map<Action,Class>> classMapping(){
        final Map<String, Map<Action,Class>> mapping = new HashMap<>();

        final Map<Action,Class> messageMap = new EnumMap<>(Action.class);
        messageMap.put(Action.CREATE, MessagePostedEvent.class);

        mapping.put("message", messageMap);

        final Map<Action, Class> dataMap = new EnumMap<>(Action.class);
        dataMap.put(Action.CREATE, RowCreatedEvent.class);
        dataMap.put(Action.UPDATE, RowEditedEvent.class);
        dataMap.put(Action.DELETE, RowDeletedEvent.class);

        mapping.put("data", dataMap);

        final Map<Action, Class> roleMap = new EnumMap<>(Action.class);
        roleMap.put(Action.CREATE, UserAddedToProjectEvent.class);
        roleMap.put(Action.UPDATE, UserRoleChangedEvent.class);

        mapping.put("role", roleMap);

        return mapping;
    }
}
