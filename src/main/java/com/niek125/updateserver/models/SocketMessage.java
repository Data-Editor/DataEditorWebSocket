package com.niek125.updateserver.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class SocketMessage {
    private SocketHeader header;
    @Setter
    private String payload;
    @Setter
    private String interest;

    public SocketMessage(SocketHeader header, String payload) {
        this.header = header;
        this.payload = payload;
    }
}
