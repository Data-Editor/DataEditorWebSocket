package com.niek125.updateserver.events;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class RowCreatedEvent extends DataEditorEvent {
    private String projectid;
    private Map row;

    public RowCreatedEvent(String interest, String projectid, Map row){
        super(interest);
        this.projectid = projectid;
        this.row = row;
    }
}
