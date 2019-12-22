package com.niek125.updateserver.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class SocketMessage {
    private final SocketHeader header;
    @Setter
    private String payload;
    private final SessionWrapper sender;
}
