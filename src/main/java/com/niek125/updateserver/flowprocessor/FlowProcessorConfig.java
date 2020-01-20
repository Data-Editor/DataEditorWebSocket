package com.niek125.updateserver.flowprocessor;

import com.niek125.updateserver.converters.IConverter;
import com.niek125.updateserver.dispatchers.KafkaDispatcher;
import com.niek125.updateserver.dispatchers.SocketDispatcher;
import com.niek125.updateserver.events.DataEditorEvent;
import com.niek125.updateserver.models.SocketMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class FlowProcessorConfig {
    @Bean
    @Autowired
    public FlowProcessor<DataEditorEvent, SocketMessage> dataEditorEventSocketMessageFlowProcessor(
            SocketDispatcher socketDispatcher,
            List<IConverter<DataEditorEvent, SocketMessage>> converters
    ) {
        return new FlowProcessor<>(socketDispatcher, converters);
    }

    @Bean
    @Autowired
    public FlowProcessor<SocketMessage, DataEditorEvent> dataEditorSocketMessageEventFlowProcessor(
            KafkaDispatcher kafkaDispatcher,
            List<IConverter<SocketMessage, DataEditorEvent>> converters
    ) {
        return new FlowProcessor<>(kafkaDispatcher, converters);
    }
}
