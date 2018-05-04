package com.lfork.a98620.lfree.imservice.message;


import com.lfork.a98620.lfree.util.JSONUtil;

public class Message {

    private int senderID;  //System的ID 为 0 , 如果是group(messageType为Group)的话， 这里的SenderID就是groupID

    private int receiverID;

    private long messageID;

    private String content;

    private MessageStatus status;

    private MessageContentType contentType;

    private MessageType type;

    private int chatType;
    public final static int SendType = 0, ReceiveType = 1;

    public Message(String content, int chatType){
        this.content = content;
        this.chatType = chatType;
    }

    public Message() {
    }


    public int getChatType(){
        return chatType;
    }

    public void setChatType(int chatType) {
        this.chatType = chatType;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public int getSenderID() {
        return senderID;
    }

    public void setSenderID(int senderID) {
        this.senderID = senderID;
    }

    public int getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(int receiverID) {
        this.receiverID = receiverID;
    }

    public long getMessageID() {
        return messageID;
    }

    public void setMessageID(long messageID) {
        this.messageID = messageID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MessageContentType getContentType() {
        return contentType;
    }

    public void setContentType(MessageContentType contentType) {
        this.contentType = contentType;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return JSONUtil.toJson(this);
    }
}
