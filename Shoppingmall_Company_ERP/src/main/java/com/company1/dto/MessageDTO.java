package com.company1.dto;

import java.sql.Timestamp;

public class MessageDTO {
    private int messageId;
    private String senderId;
    private String senderName;
    private String receiverId;
    private String receiverName;
    private String messageType;
    private String content;
    private String isRead;
    private Timestamp sendDate;
    private Timestamp readDate;
    
    // 기본 생성자
    public MessageDTO() {}
    
    // 매개변수 생성자
    public MessageDTO(String senderId, String senderName, String receiverId, 
                     String receiverName, String messageType, String content) {
        this.senderId = senderId;
        this.senderName = senderName;
        this.receiverId = receiverId;
        this.receiverName = receiverName;
        this.messageType = messageType;
        this.content = content;
        this.isRead = "N";
    }
    
    // Getter와 Setter 메서드들
    public int getMessageId() {
        return messageId;
    }
    
    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }
    
    public String getSenderId() {
        return senderId;
    }
    
    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
    
    public String getSenderName() {
        return senderName;
    }
    
    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
    
    public String getReceiverId() {
        return receiverId;
    }
    
    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }
    
    public String getReceiverName() {
        return receiverName;
    }
    
    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }
    
    public String getMessageType() {
        return messageType;
    }
    
    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public String getIsRead() {
        return isRead;
    }
    
    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }
    
    public Timestamp getSendDate() {
        return sendDate;
    }
    
    public void setSendDate(Timestamp sendDate) {
        this.sendDate = sendDate;
    }
    
    public Timestamp getReadDate() {
        return readDate;
    }
    
    public void setReadDate(Timestamp readDate) {
        this.readDate = readDate;
    }
    
    @Override
    public String toString() {
        return "MessageDTO{" +
                "messageId=" + messageId +
                ", senderId='" + senderId + '\'' +
                ", senderName='" + senderName + '\'' +
                ", receiverId='" + receiverId + '\'' +
                ", receiverName='" + receiverName + '\'' +
                ", messageType='" + messageType + '\'' +
                ", content='" + content + '\'' +
                ", isRead='" + isRead + '\'' +
                ", sendDate=" + sendDate +
                ", readDate=" + readDate +
                '}';
    }
}