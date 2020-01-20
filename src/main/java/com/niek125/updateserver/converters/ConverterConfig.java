package com.niek125.updateserver.converters;

import com.niek125.updateserver.converters.dataeditorevent.MessagePostedEventConverter;
import com.niek125.updateserver.converters.dataeditorevent.RowCreatedEventConverter;
import com.niek125.updateserver.converters.dataeditorevent.RowDeletedEventConverter;
import com.niek125.updateserver.converters.dataeditorevent.RowEditedEventConverter;
import com.niek125.updateserver.converters.socketmessage.*;
import com.niek125.updateserver.events.DataEditorEvent;
import com.niek125.updateserver.models.SocketMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class ConverterConfig {
    @Bean
    @Autowired
    public List<IConverter<SocketMessage, DataEditorEvent>> socketEventConverters(
            MessagePostedMessageConverter messagePostedMessageConverter,
            RowCreatedMessageConverter rowCreatedMessageConverter,
            RowEditedMessageConverter rowEditedMessageConverter,
            RowDeletedMessageConverter rowDeletedMessageConverter,
            UserAddedToProjectMessageConverter userAddedToProjectMessageConverter,
            UserRoleChangedMessageConverter userRoleChangedMessageConverter
    ) {
        final List<IConverter<SocketMessage, DataEditorEvent>> converters = new ArrayList<>();
        converters.add(messagePostedMessageConverter);
        converters.add(rowCreatedMessageConverter);
        converters.add(rowEditedMessageConverter);
        converters.add(rowDeletedMessageConverter);
        converters.add(userAddedToProjectMessageConverter);
        converters.add(userRoleChangedMessageConverter);
        return converters;
    }

    @Bean
    public List<IConverter<DataEditorEvent, SocketMessage>> eventSocketConverters(
            MessagePostedEventConverter messagePostedEventConverter,
            RowCreatedEventConverter rowCreatedEventConverter,
            RowDeletedEventConverter rowDeletedEventConverter,
            RowEditedEventConverter rowEditedEventConverter
    ) {
        final List<IConverter<DataEditorEvent, SocketMessage>> converters = new ArrayList<>();
        converters.add(messagePostedEventConverter);
        converters.add(rowCreatedEventConverter);
        converters.add(rowDeletedEventConverter);
        converters.add(rowEditedEventConverter);
        return converters;
    }
}
