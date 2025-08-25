package com.company1.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.company1.DBManager;
import com.company1.dto.NoticeDTO;
import com.company1.dto.AttendanceDTO;

public class GroupwareDAO {
    
    // 공지사항 관련 메서드
    
    /**
     * 모든 공지사항을 조회합니다.
     */
    public List<NoticeDTO> getAllNotices() {
        List<NoticeDTO> notices = new ArrayList<>();
        String sql = "SELECT * FROM NOTICES WHERE IS_ACTIVE = 'Y' ORDER BY CREATE_DATE DESC";
        
        try (Connection conn = DBManager.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                NoticeDTO notice = new NoticeDTO();
                notice.setNoticeId(rs.getInt("NOTICE_ID"));
                notice.setTitle(rs.getString("TITLE"));
                notice.setContent(rs.getString("CONTENT"));
                notice.setAuthorId(rs.getString("AUTHOR_ID"));
                notice.setAuthorName(rs.getString("AUTHOR_NAME"));
                notice.setCreateDate(rs.getDate("CREATE_DATE"));
                notice.setUpdateDate(rs.getDate("UPDATE_DATE"));
                notice.setViewCount(rs.getInt("VIEW_COUNT"));
                notice.setIsActive(rs.getString("IS_ACTIVE"));
                notices.add(notice);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notices;
    }
    
    /**
     * 새로운 공지사항을 등록합니다.
     */
    public boolean insertNotice(NoticeDTO notice) {
        String sql = "INSERT INTO NOTICES (NOTICE_ID, TITLE, CONTENT, AUTHOR_ID, AUTHOR_NAME) " +
                    "VALUES (SEQ_NOTICE_ID.NEXTVAL, ?, ?, ?, ?)";
        
        try (Connection conn = DBManager.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, notice.getTitle());
            pstmt.setString(2, notice.getContent());
            pstmt.setString(3, notice.getAuthorId());
            pstmt.setString(4, notice.getAuthorName());
            
            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 공지사항 조회수를 증가시킵니다.
     */
    public void increaseViewCount(int noticeId) {
        String sql = "UPDATE NOTICES SET VIEW_COUNT = VIEW_COUNT + 1 WHERE NOTICE_ID = ?";
        
        try (Connection conn = DBManager.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, noticeId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // 출퇴근 관리 관련 메서드
    
    /**
     * 출근 처리를 합니다.
     */
    public boolean checkIn(String userId) {
        String sql = "INSERT INTO ATTENDANCE (ATTENDANCE_ID, USER_ID, WORK_DATE, CHECK_IN_TIME) " +
                    "VALUES (SEQ_ATTENDANCE_ID.NEXTVAL, ?, TRUNC(SYSDATE), CURRENT_TIMESTAMP)";
        
        try (Connection conn = DBManager.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, userId);
            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 퇴근 처리를 합니다.
     */
    public boolean checkOut(String userId) {
        String sql = "UPDATE ATTENDANCE SET CHECK_OUT_TIME = CURRENT_TIMESTAMP " +
                    "WHERE USER_ID = ? AND WORK_DATE = TRUNC(SYSDATE) AND CHECK_OUT_TIME IS NULL";
        
        try (Connection conn = DBManager.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, userId);
            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 특정 사용자의 오늘 출퇴근 정보를 조회합니다.
     */
    public AttendanceDTO getTodayAttendance(String userId) {
        String sql = "SELECT * FROM ATTENDANCE WHERE USER_ID = ? AND WORK_DATE = TRUNC(SYSDATE)";
        
        try (Connection conn = DBManager.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                AttendanceDTO attendance = new AttendanceDTO();
                attendance.setAttendanceId(rs.getInt("ATTENDANCE_ID"));
                attendance.setUserId(rs.getString("USER_ID"));
                attendance.setWorkDate(rs.getDate("WORK_DATE"));
                attendance.setCheckInTime(rs.getTimestamp("CHECK_IN_TIME"));
                attendance.setCheckOutTime(rs.getTimestamp("CHECK_OUT_TIME"));
                attendance.setTotalWorkMinutes(rs.getInt("TOTAL_WORK_MINUTES"));
                attendance.setBreakMinutes(rs.getInt("BREAK_MINUTES"));
                attendance.setOvertimeMinutes(rs.getInt("OVERTIME_MINUTES"));
                attendance.setStatus(rs.getString("STATUS"));
                attendance.setNotes(rs.getString("NOTES"));
                attendance.setCreateDate(rs.getDate("CREATE_DATE"));
                attendance.setUpdateDate(rs.getDate("UPDATE_DATE"));
                return attendance;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 오늘 이미 출근했는지 확인합니다.
     */
    public boolean isAlreadyCheckedIn(String userId) {
        String sql = "SELECT COUNT(*) FROM ATTENDANCE WHERE USER_ID = ? AND WORK_DATE = TRUNC(SYSDATE) AND CHECK_IN_TIME IS NOT NULL";
        
        try (Connection conn = DBManager.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * 오늘 이미 퇴근했는지 확인합니다.
     */
    public boolean isAlreadyCheckedOut(String userId) {
        String sql = "SELECT COUNT(*) FROM ATTENDANCE WHERE USER_ID = ? AND WORK_DATE = TRUNC(SYSDATE) AND CHECK_OUT_TIME IS NOT NULL";
        
        try (Connection conn = DBManager.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * 사용자의 월별 출퇴근 통계를 조회합니다.
     */
    public List<AttendanceDTO> getMonthlyAttendance(String userId, String yearMonth) {
        List<AttendanceDTO> attendanceList = new ArrayList<>();
        String sql = "SELECT * FROM ATTENDANCE WHERE USER_ID = ? AND TO_CHAR(WORK_DATE, 'YYYY-MM') = ? ORDER BY WORK_DATE";
        
        try (Connection conn = DBManager.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, userId);
            pstmt.setString(2, yearMonth);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                AttendanceDTO attendance = new AttendanceDTO();
                attendance.setAttendanceId(rs.getInt("ATTENDANCE_ID"));
                attendance.setUserId(rs.getString("USER_ID"));
                attendance.setWorkDate(rs.getDate("WORK_DATE"));
                attendance.setCheckInTime(rs.getTimestamp("CHECK_IN_TIME"));
                attendance.setCheckOutTime(rs.getTimestamp("CHECK_OUT_TIME"));
                attendance.setTotalWorkMinutes(rs.getInt("TOTAL_WORK_MINUTES"));
                attendance.setBreakMinutes(rs.getInt("BREAK_MINUTES"));
                attendance.setOvertimeMinutes(rs.getInt("OVERTIME_MINUTES"));
                attendance.setStatus(rs.getString("STATUS"));
                attendance.setNotes(rs.getString("NOTES"));
                attendance.setCreateDate(rs.getDate("CREATE_DATE"));
                attendance.setUpdateDate(rs.getDate("UPDATE_DATE"));
                attendanceList.add(attendance);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return attendanceList;
    }
}