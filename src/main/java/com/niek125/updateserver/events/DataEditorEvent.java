package com.niek125.updateserver.events;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataEditorEvent {
    private String creator;
    private String interest;

    public DataEditorEvent(String interest) {
        this.creator = "socket-server";
        this.interest = interest;
    }
}
