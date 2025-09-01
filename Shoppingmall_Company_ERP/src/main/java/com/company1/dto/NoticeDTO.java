package com.company1.dto;

import java.sql.Date;

public class NoticeDTO {
    private int noticeId;
    private String title;
    private String content;
    private String authorId;
    private String authorName;
    private Date createDate;
    private Date updateDate;
    private int viewCount;
    private String isActive;
    
    // 기본 생성자
    public NoticeDTO() {}
    
    // 모든 필드를 포함한 생성자
    public NoticeDTO(int noticeId, String title, String content, String authorId, 
                    String authorName, Date createDate, Date updateDate, 
                    int viewCount, String isActive) {
        this.noticeId = noticeId;
        this.title = title;
        this.content = content;
        this.authorId = authorId;
        this.authorName = authorName;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.viewCount = viewCount;
        this.isActive = isActive;
    }
    
    // Getter and Setter methods
    public int getNoticeId() {
        return noticeId;
    }
    
    public void setNoticeId(int noticeId) {
        this.noticeId = noticeId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public String getAuthorId() {
        return authorId;
    }
    
    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }
    
    public String getAuthorName() {
        return authorName;
    }
    
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
    
    public Date getCreateDate() {
        return createDate;
    }
    
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    
    public Date getUpdateDate() {
        return updateDate;
    }
    
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    
    public int getViewCount() {
        return viewCount;
    }
    
    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }
    
    public String getIsActive() {
        return isActive;
    }
    
    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }
    
    @Override
    public String toString() {
        return "NoticeDTO{" +
                "noticeId=" + noticeId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", authorId='" + authorId + '\'' +
                ", authorName='" + authorName + '\'' +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                ", viewCount=" + viewCount +
                ", isActive='" + isActive + '\'' +
                '}';
    }
}