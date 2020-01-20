package com.niek125.updateserver.events;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAddedToProjectEvent extends DataEditorEvent {
    private String userid;
    private String projectid;

    public UserAddedToProjectEvent(String interest, String userid, String projectid){
        super(interest);
        this.userid = userid;
        this.projectid = projectid;
    }
}
