package com.niek125.updateserver.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SocketHeader {
    private final Action action;
    private final String payload;
}
