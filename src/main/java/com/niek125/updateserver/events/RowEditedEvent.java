package com.niek125.updateserver.events;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class RowEditedEvent extends DataEditorEvent {
    private String projectid;
    private int rownumber;
    private Map row;

    public RowEditedEvent(String interest, String projectid, int rownumber, Map row){
        super(interest);
        this.projectid = projectid;
        this.rownumber = rownumber;
        this.row = row;
    }
}
