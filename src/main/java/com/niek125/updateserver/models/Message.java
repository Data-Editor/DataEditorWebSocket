package com.niek125.updateserver.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Message {
    private String messageid;
    private String senderid;
    private String content;
    private String sendtime;
    private String projectid;
}
