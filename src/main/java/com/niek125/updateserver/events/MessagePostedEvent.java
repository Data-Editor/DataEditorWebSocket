package com.niek125.updateserver.events;

import com.niek125.updateserver.models.Message;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessagePostedEvent extends DataEditorEvent {
    private Message message;

    public MessagePostedEvent(String interest, Message message){
        super(interest);
        this.message = message;
    }
}
