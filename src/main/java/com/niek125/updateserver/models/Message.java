package com.niek125.updateserver.models;

public class Message {
    private String messageid;
    private String senderid;
    private String content;
    private String sendtime;

    public Message(){
    }

    public String getMessageid() {
        return messageid;
    }

    public String getSenderid() {
        return senderid;
    }

    public String getContent() {
        return content;
    }

    public String getSendtime() {
        return sendtime;
    }

    public void setMessageid(String messageid) {
        this.messageid = messageid;
    }

    public void setSenderid(String senderid) {
        this.senderid = senderid;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setSendtime(String sendtime) {
        this.sendtime = sendtime;
    }
}
