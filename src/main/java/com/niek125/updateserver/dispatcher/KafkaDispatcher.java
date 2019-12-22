package com.niek125.updateserver.dispatcher;

import com.niek125.updateserver.models.SocketMessage;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;

@AllArgsConstructor
public class KafkaDispatcher implements Dispatcher {
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void dispatch(SocketMessage message) {
        kafkaTemplate.send(message.getHeader().getPayload(), message.getPayload());
    }
}
