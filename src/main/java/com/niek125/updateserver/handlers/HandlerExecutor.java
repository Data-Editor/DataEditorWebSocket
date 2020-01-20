package com.niek125.updateserver.handlers;

import com.niek125.updateserver.events.DataEditorEvent;
import com.niek125.updateserver.flowprocessor.FlowProcessor;
import com.niek125.updateserver.models.SessionWrapper;
import com.niek125.updateserver.models.SocketMessage;
import com.niek125.updateserver.parsers.MessageParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class HandlerExecutor {
    private final Logger logger = LoggerFactory.getLogger(HandlerExecutor.class);
    private final FlowProcessor<SocketMessage, DataEditorEvent> flowProcessor;
    private final MessageParser parser;
    private final List<Handler> handlers;

    public HandlerExecutor(FlowProcessor<SocketMessage, DataEditorEvent> flowProcessor, MessageParser parser, List<Handler> handlers) {
        this.flowProcessor = flowProcessor;
        this.parser = parser;
        this.handlers = handlers;
    }

    public boolean handle(String message, SessionWrapper sessionWrapper){
        logger.info("parsing message");
        final SocketMessage socketMessage = parser.parse(message);
        socketMessage.setInterest(sessionWrapper.getInterest());
        logger.info("getting target class");
        final Class claz = parser.getEventTargetClass(socketMessage);
        logger.info("executing handler");
        final Handler handler = handlers.stream()
                .filter(x -> x.getProcessType().equals(socketMessage.getHeader().getPayload()))
                .collect(Collectors.toList()).get(0);
        if (handler.handle(socketMessage, sessionWrapper)) {
            logger.info("executing flowprocess");
            flowProcessor.process(socketMessage, claz);
            return true;
        }
        return false;
    }
}
