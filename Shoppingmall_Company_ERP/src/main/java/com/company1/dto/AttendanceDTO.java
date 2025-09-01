package com.company1.dto;

import java.sql.Date;
import java.sql.Timestamp;

public class AttendanceDTO {
    private int attendanceId;
    private String userId;
    private Date workDate;
    private Timestamp checkInTime;
    private Timestamp checkOutTime;
    private int totalWorkMinutes;
    private int breakMinutes;
    private int overtimeMinutes;
    private String status;
    private String notes;
    private Date createDate;
    private Date updateDate;
    
    // 기본 생성자
    public AttendanceDTO() {}
    
    // 생성자
    public AttendanceDTO(int attendanceId, String userId, Date workDate, 
                        Timestamp checkInTime, Timestamp checkOutTime,
                        int totalWorkMinutes, int breakMinutes, int overtimeMinutes,
                        String status, String notes, Date createDate, Date updateDate) {
        this.attendanceId = attendanceId;
        this.userId = userId;
        this.workDate = workDate;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.totalWorkMinutes = totalWorkMinutes;
        this.breakMinutes = breakMinutes;
        this.overtimeMinutes = overtimeMinutes;
        this.status = status;
        this.notes = notes;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }
    
    // Getter and Setter methods
    public int getAttendanceId() { return attendanceId; }
    public void setAttendanceId(int attendanceId) { this.attendanceId = attendanceId; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public Date getWorkDate() { return workDate; }
    public void setWorkDate(Date workDate) { this.workDate = workDate; }
    
    public Timestamp getCheckInTime() { return checkInTime; }
    public void setCheckInTime(Timestamp checkInTime) { this.checkInTime = checkInTime; }
    
    public Timestamp getCheckOutTime() { return checkOutTime; }
    public void setCheckOutTime(Timestamp checkOutTime) { this.checkOutTime = checkOutTime; }
    
    public int getTotalWorkMinutes() { return totalWorkMinutes; }
    public void setTotalWorkMinutes(int totalWorkMinutes) { this.totalWorkMinutes = totalWorkMinutes; }
    
    public int getBreakMinutes() { return breakMinutes; }
    public void setBreakMinutes(int breakMinutes) { this.breakMinutes = breakMinutes; }
    
    public int getOvertimeMinutes() { return overtimeMinutes; }
    public void setOvertimeMinutes(int overtimeMinutes) { this.overtimeMinutes = overtimeMinutes; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public Date getCreateDate() { return createDate; }
    public void setCreateDate(Date createDate) { this.createDate = createDate; }
    
    public Date getUpdateDate() { return updateDate; }
    public void setUpdateDate(Date updateDate) { this.updateDate = updateDate; }
}