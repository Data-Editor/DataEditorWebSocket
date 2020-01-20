package com.niek125.updateserver.kafka;

import com.niek125.updateserver.events.DataEditorEvent;
import com.niek125.updateserver.flowprocessor.FlowProcessor;
import com.niek125.updateserver.models.SocketMessage;
import com.niek125.updateserver.parsers.EventParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaSocketConsumer {
    private final Logger logger = LoggerFactory.getLogger(KafkaSocketConsumer.class);
    private final FlowProcessor<DataEditorEvent, SocketMessage> flowProcessor;
    private final EventParser parser;

    public KafkaSocketConsumer(FlowProcessor<DataEditorEvent, SocketMessage> flowProcessor, EventParser parser) {
        this.flowProcessor = flowProcessor;
        this.parser = parser;
    }

    @KafkaListener(topics = {"message", "data"}, groupId = "socket-consumer")
    public void consume(String message) {
        logger.info("received message: {}", message);
        logger.info("parsing message");
        final DataEditorEvent event = parser.parse(message);
        logger.info("getting target class");
        final Class eventTargetClass = parser.getEventTargetClass(event);
        flowProcessor.process(event, eventTargetClass);
    }
}
