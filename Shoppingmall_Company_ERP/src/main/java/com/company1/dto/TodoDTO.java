package com.company1.dto;

import java.sql.Date;

public class TodoDTO {
    private int todoId;
    private String userId;
    private String title;
    private String description;
    private String isCompleted;
    private int priority;
    private Date dueDate;
    private Date createDate;
    private Date completeDate;
    
    // 기본 생성자
    public TodoDTO() {}
    
    // 매개변수 생성자
    public TodoDTO(String userId, String title, String description, int priority) {
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.isCompleted = "N";
    }
    
    // Getter와 Setter 메서드들
    public int getTodoId() {
        return todoId;
    }
    
    public void setTodoId(int todoId) {
        this.todoId = todoId;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getIsCompleted() {
        return isCompleted;
    }
    
    public void setIsCompleted(String isCompleted) {
        this.isCompleted = isCompleted;
    }
    
    public int getPriority() {
        return priority;
    }
    
    public void setPriority(int priority) {
        this.priority = priority;
    }
    
    public Date getDueDate() {
        return dueDate;
    }
    
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
    
    public Date getCreateDate() {
        return createDate;
    }
    
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    
    public Date getCompleteDate() {
        return completeDate;
    }
    
    public void setCompleteDate(Date completeDate) {
        this.completeDate = completeDate;
    }
    
    @Override
    public String toString() {
        return "TodoDTO{" +
                "todoId=" + todoId +
                ", userId='" + userId + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", isCompleted='" + isCompleted + '\'' +
                ", priority=" + priority +
                ", dueDate=" + dueDate +
                ", createDate=" + createDate +
                ", completeDate=" + completeDate +
                '}';
    }
}