package com.niek125.updateserver.events;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class RowDeletedEvent extends DataEditorEvent {
    private String projectid;
    private int rownumber;

    public RowDeletedEvent(String interest, String projectid, int rownumber){
        super(interest);
        this.projectid = projectid;
        this.rownumber = rownumber;
    }
}
