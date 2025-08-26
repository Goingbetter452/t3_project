package com.company1.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.company1.DBManager;
import com.company1.dto.NoticeDTO;
import com.company1.dto.AttendanceDTO;
import com.company1.dto.CalendarDTO;

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
            
            // 실행 전에 값들 로깅
            System.out.println("Notice Insert 시도: authorId=" + notice.getAuthorId() + 
                             ", authorName=" + notice.getAuthorName() +
                             ", title=" + notice.getTitle());
            
            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            // FK 위반 에러인 경우 (ORA-02291)
            if (e.getErrorCode() == 2291) {
                System.err.println("FK 제약조건 위반: AUTHOR_ID(" + notice.getAuthorId() + 
                                 ")가 EMPLOYEES 테이블에 존재하지 않습니다.");
            }
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
        // 1) 아직 퇴근전인 오늘 레코드에서 출근시간 가져오기
        final String selectSql =
            "SELECT CHECK_IN_TIME FROM ATTENDANCE " +
            "WHERE USER_ID = ? AND WORK_DATE = TRUNC(SYSDATE) AND CHECK_OUT_TIME IS NULL";

        // 2) 근무시간/상태 포함해 갱신
        final String updateSql =
            "UPDATE ATTENDANCE SET " +
            "  CHECK_OUT_TIME = CURRENT_TIMESTAMP, " +
            "  TOTAL_WORK_MINUTES = ?, " +
            "  STATUS = CASE " +
            "    WHEN ? < 480 THEN 'EARLY_LEAVE' " + // 8시간 미만
            "    WHEN ? > 600 THEN 'OVERTIME' " +    // 10시간 초과
            "    ELSE 'PRESENT' " +                  // 정상 근무
            "  END " +
            "WHERE USER_ID = ? AND WORK_DATE = TRUNC(SYSDATE) AND CHECK_OUT_TIME IS NULL";

        try (Connection conn = DBManager.getDBConnection()) {
            // (선택) 자동 커밋 해제하고 트랜잭션으로 묶고 싶으면 주석 해제
            // conn.setAutoCommit(false);

            Timestamp checkInTime = null;

            try (PreparedStatement ps = conn.prepareStatement(selectSql)) {
                ps.setString(1, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        checkInTime = rs.getTimestamp("CHECK_IN_TIME");
                    } else {
                        // 오늘 출근 기록이 없거나 이미 퇴근 처리됨
                        return false;
                    }
                }
            }

            if (checkInTime == null) {
                return false;
            }

            // 근무시간(분) 계산
            long nowMillis = System.currentTimeMillis();
            long inMillis  = checkInTime.getTime();
            int workMinutes = (int) ((nowMillis - inMillis) / (1000 * 60));

            try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
                ps.setInt(1, workMinutes);
                ps.setInt(2, workMinutes);
                ps.setInt(3, workMinutes);
                ps.setString(4, userId);
                int updated = ps.executeUpdate();

                // (선택) 트랜잭션 사용할 때 커밋
                // conn.commit();

                return updated > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // (선택) 트랜잭션 사용 시 롤백
            // try { conn.rollback(); } catch (SQLException ignore) {}
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

    
    /**
     * 공지사항을 수정합니다.
     */
    public boolean updateNotice(NoticeDTO notice) {
        String sql = "UPDATE NOTICES SET TITLE = ?, CONTENT = ?, UPDATE_DATE = SYSDATE " +
                    "WHERE NOTICE_ID = ?";
        
        try (Connection conn = DBManager.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, notice.getTitle());
            pstmt.setString(2, notice.getContent());
            pstmt.setInt(3, notice.getNoticeId());
            
            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 공지사항을 삭제합니다 (논리적 삭제).
     */
    public boolean deleteNotice(int noticeId) {
        String sql = "UPDATE NOTICES SET IS_ACTIVE = 'N' WHERE NOTICE_ID = ?";
        
        try (Connection conn = DBManager.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, noticeId);
            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 특정 공지사항을 조회합니다.
     */
    public NoticeDTO getNoticeById(int noticeId) {
        String sql = "SELECT * FROM NOTICES WHERE NOTICE_ID = ? AND IS_ACTIVE = 'Y'";
        
        try (Connection conn = DBManager.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, noticeId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
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
                return notice;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // 캘린더 관련 메서드
    
    /**
     * 특정 월의 일정을 조회합니다.
     */
    public List<CalendarDTO> getMonthlyEvents(String userId, String yearMonth) {
        List<CalendarDTO> events = new ArrayList<>();
        String sql = "SELECT * FROM CALENDAR_EVENTS WHERE USER_ID = ? " +
                    "AND TO_CHAR(START_DATE, 'YYYY-MM') = ? ORDER BY START_DATE";
        
        try (Connection conn = DBManager.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, userId);
            pstmt.setString(2, yearMonth);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                CalendarDTO event = new CalendarDTO();
                event.setEventId(rs.getInt("EVENT_ID"));
                event.setUserId(rs.getString("USER_ID"));
                event.setTitle(rs.getString("TITLE"));
                event.setDescription(rs.getString("DESCRIPTION"));
                event.setEventType(rs.getString("EVENT_TYPE"));
                event.setStartDate(rs.getTimestamp("START_DATE"));
                event.setEndDate(rs.getTimestamp("END_DATE"));
                event.setIsAllDay(rs.getString("IS_ALL_DAY"));
                event.setLocation(rs.getString("LOCATION"));
                event.setReminderMinutes(rs.getInt("REMINDER_MINUTES"));
                event.setIsRecurring(rs.getString("IS_RECURRING"));
                event.setRecurrencePattern(rs.getString("RECURRENCE_PATTERN"));
                events.add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }
    
    /**
     * 새로운 일정을 추가합니다.
     */
    public boolean addEvent(CalendarDTO event) {
        String sql = "INSERT INTO CALENDAR_EVENTS (EVENT_ID, USER_ID, TITLE, DESCRIPTION, " +
                    "EVENT_TYPE, START_DATE, END_DATE, IS_ALL_DAY, LOCATION) " +
                    "VALUES (SEQ_EVENT_ID.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBManager.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, event.getUserId());
            pstmt.setString(2, event.getTitle());
            pstmt.setString(3, event.getDescription());
            pstmt.setString(4, event.getEventType());
            pstmt.setTimestamp(5, event.getStartDate());
            pstmt.setTimestamp(6, event.getEndDate());
            pstmt.setString(7, event.getIsAllDay());
            pstmt.setString(8, event.getLocation());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 일정을 수정합니다.
     */
    public boolean updateEvent(CalendarDTO event) {
        String sql = "UPDATE CALENDAR_EVENTS SET TITLE = ?, DESCRIPTION = ?, " +
                    "EVENT_TYPE = ?, START_DATE = ?, END_DATE = ?, " +
                    "IS_ALL_DAY = ?, LOCATION = ? " +
                    "WHERE EVENT_ID = ? AND USER_ID = ?";
        
        try (Connection conn = DBManager.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, event.getTitle());
            pstmt.setString(2, event.getDescription());
            pstmt.setString(3, event.getEventType());
            pstmt.setTimestamp(4, event.getStartDate());
            pstmt.setTimestamp(5, event.getEndDate());
            pstmt.setString(6, event.getIsAllDay());
            pstmt.setString(7, event.getLocation());
            pstmt.setInt(8, event.getEventId());
            pstmt.setString(9, event.getUserId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 일정을 삭제합니다.
     */
    public boolean deleteEvent(int eventId, String userId) {
        String sql = "DELETE FROM CALENDAR_EVENTS WHERE EVENT_ID = ? AND USER_ID = ?";
        
        try (Connection conn = DBManager.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, eventId);
            pstmt.setString(2, userId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}