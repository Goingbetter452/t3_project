package com.company1.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import com.company1.dao.GroupwareDAO;
import com.company1.dto.NoticeDTO;
import com.company1.dto.AttendanceDTO;
import com.company1.dto.CalendarDTO;

@WebServlet("/GroupwareServlet")
public class GroupwareServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private GroupwareDAO groupwareDAO;
    
    @Override
    public void init() throws ServletException {
        groupwareDAO = new GroupwareDAO();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String command = request.getParameter("command");
        
        if (command == null) {
            command = "main";
        }
        
        switch (command) {
            case "getNotices":
                getNotices(request, response);
                break;
            case "getTodayAttendance":
                getTodayAttendance(request, response);
                break;
            case "getMonthlyAttendance":
                getMonthlyAttendance(request, response);
                break;
            case "getMonthlyEvents":  // 추가: 월별 일정 조회
                getMonthlyEvents(request, response);
                break;
            default:
                response.sendRedirect("groupware.jsp");
                break;
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String command = request.getParameter("command");
        
        if (command == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Command parameter is required");
            return;
        }
        
        switch (command) {
            case "addNotice":
                addNotice(request, response);
                break;
            case "updateNotice":
                updateNotice(request, response);
                break;
            case "deleteNotice":
                deleteNotice(request, response);
                break;
            case "checkIn":
                checkIn(request, response);
                break;
            case "checkOut":
                checkOut(request, response);
                break;
            case "addEvent":      // 추가: 일정 추가
                addEvent(request, response);
                break;
            case "updateEvent":   // 추가: 일정 수정
                updateEvent(request, response);
                break;
            case "deleteEvent":   // 추가: 일정 삭제
                deleteEvent(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid command");
                break;
        }
    }
    
    /**
     * 모든 공지사항을 텍스트 형태로 반환합니다.
     */
    private void getNotices(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        
        List<NoticeDTO> notices = groupwareDAO.getAllNotices();
        
        PrintWriter out = response.getWriter();
        // 날짜를 'yyyy-MM-dd HH:mm:ss' 형식으로 변환하기 위한 포맷터
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (int i = 0; i < notices.size(); i++) {
            NoticeDTO notice = notices.get(i);
            if (i > 0) out.print("%%%"); // 레코드 구분자를 '%%%'로 변경

            // 날짜가 null이 아닐 경우에만 포맷팅
            String createDateStr = (notice.getCreateDate() != null) ? sdf.format(notice.getCreateDate()) : "";

            // 필드 순서: noticeId|title|content|authorName|createDate|viewCount
            out.print(
                notice.getNoticeId() + "|" +
                notice.getTitle().replace("|", " ").replace("%", " ") + "|" + 
                notice.getContent().replace("|", " ").replace("%", " ") + "|" + 
                notice.getAuthorName() + "|" + 
                createDateStr + "|" +
                notice.getViewCount()
            );
        }
        out.flush();
    }
    
    /**
     * 새로운 공지사항을 등록합니다.
     */
    private void addNotice(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        
        HttpSession session = request.getSession();
        String authorId = (String) session.getAttribute("loginUser");  // userId -> loginUser로 변경
        String authorName = (String) session.getAttribute("userName");
        
        // 세션에 사용자 정보가 없으면 FK 제약조건을 위반하므로 에러 반환
        if (authorId == null || authorId.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            PrintWriter out = response.getWriter();
            out.print("error");
            out.flush();
            return;
        }
        // authorName 이 없으면 대체값 사용 (NOT NULL 컬럼)
        if (authorName == null || authorName.trim().isEmpty()) {
            authorName = authorId;
        }
        
        if (title == null || title.trim().isEmpty() || 
            content == null || content.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter out = response.getWriter();
            out.print("error");

            return;
        }
        
        NoticeDTO notice = new NoticeDTO();
        notice.setTitle(title.trim());
        notice.setContent(content.trim());
        notice.setAuthorId(authorId);
        notice.setAuthorName(authorName);
        
        boolean success = groupwareDAO.insertNotice(notice);
        
        PrintWriter out = response.getWriter();
        if (success) {
            out.print("success");
        } else {
            out.print("error");
        }
        out.flush();
    }
    
    /**
     * 공지사항을 수정합니다.
     */
    private void updateNotice(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        
        String noticeIdStr = request.getParameter("noticeId");
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        
        if (noticeIdStr == null || title == null || title.trim().isEmpty() || 
            content == null || content.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter out = response.getWriter();
            out.print("error");
            return;
        }
        
        try {
            int noticeId = Integer.parseInt(noticeIdStr);
            NoticeDTO notice = new NoticeDTO();
            notice.setNoticeId(noticeId);
            notice.setTitle(title.trim());
            notice.setContent(content.trim());
            
            boolean success = groupwareDAO.updateNotice(notice);
            
            PrintWriter out = response.getWriter();
            if (success) {
                out.print("success");
            } else {
                out.print("error");
            }
            out.flush();
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter out = response.getWriter();
            out.print("error");
            out.flush();
        }
    }
    
    /**
     * 공지사항을 삭제합니다.
     */
    private void deleteNotice(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        
        String noticeIdStr = request.getParameter("noticeId");
        
        if (noticeIdStr == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter out = response.getWriter();
            out.print("error");
            return;
        }
        
        try {
            int noticeId = Integer.parseInt(noticeIdStr);
            boolean success = groupwareDAO.deleteNotice(noticeId);
            
            PrintWriter out = response.getWriter();
            if (success) {
                out.print("success");
            } else {
                out.print("error");
            }
            out.flush();
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter out = response.getWriter();
            out.print("error");
            out.flush();
        }
    }
    
    /**
     * 출근 처리를 합니다.
     */
    private void checkIn(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("loginUser");  // userId -> loginUser로 변경
        
        // 사용자 정보가 없으면 FK 제약조건 위반 가능성 -> 에러 반환
        if (userId == null || userId.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            PrintWriter out = response.getWriter();
            out.print("error");
            out.flush();
            return;
        }
        
        PrintWriter out = response.getWriter();
        
        // 이미 출근했는지 확인
        if (groupwareDAO.isAlreadyCheckedIn(userId)) {
            out.print("error");
            return;
        }
        
        boolean success = groupwareDAO.checkIn(userId);
        
        if (success) {
            out.print("success");
        } else {
            out.print("error");
        }
        out.flush();
    }
    
    /**
     * 퇴근 처리를 합니다.
     */
    private void checkOut(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("loginUser");  // userId -> loginUser로 변경
        
        // 사용자 정보가 없으면 FK 제약조건 위반 가능성 -> 에러 반환
        if (userId == null || userId.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            PrintWriter out = response.getWriter();
            out.print("error");
            out.flush();
            return;
        }
        
        PrintWriter out = response.getWriter();
        
        // 출근 기록이 있는지 확인
        if (!groupwareDAO.isAlreadyCheckedIn(userId)) {
            out.print("error");
            return;
        }
        
        // 이미 퇴근했는지 확인
        if (groupwareDAO.isAlreadyCheckedOut(userId)) {
            out.print("error");
            return;
        }
        
        boolean success = groupwareDAO.checkOut(userId);
        
        if (success) {
            out.print("success");
        } else {
            out.print("error");
        }
        out.flush();
    }
    
    private void getTodayAttendance(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("loginUser");  // userId -> loginUser로 변경
        
        // 로그인 정보가 없으면 빈 응답 반환
        if (userId == null || userId.trim().isEmpty()) {
            PrintWriter out = response.getWriter();
            out.print("");
            out.flush();
            return;
        }
        
        AttendanceDTO attendance = groupwareDAO.getTodayAttendance(userId);
        
        PrintWriter out = response.getWriter();
        if (attendance != null) {
            out.print("checkInTime:" + attendance.getCheckInTime() + ",");
            out.print("checkOutTime:" + attendance.getCheckOutTime() + ",");
            out.print("totalWorkMinutes:" + attendance.getTotalWorkMinutes() + ",");
            out.print("status:" + attendance.getStatus());
        } else {
            out.print("");
        }
        out.flush();
    }
    /**
     * 월별 출퇴근 정보를 텍스트 형태로 반환합니다.
     */
    private void getMonthlyAttendance(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("loginUser");  // userId -> loginUser로 변경
        String yearMonth = request.getParameter("yearMonth");
        
        // 로그인 정보가 없으면 빈 응답 반환
        if (userId == null || userId.trim().isEmpty()) {
            PrintWriter out = response.getWriter();
            out.print("");
            out.flush();
            return;
        }
        
        if (yearMonth == null) {
            // 현재 년월을 기본값으로 설정
            java.time.LocalDate now = java.time.LocalDate.now();
            yearMonth = now.getYear() + "-" + String.format("%02d", now.getMonthValue());
        }
        
        List<AttendanceDTO> attendanceList = groupwareDAO.getMonthlyAttendance(userId, yearMonth);
        
        PrintWriter out = response.getWriter();
        for (int i = 0; i < attendanceList.size(); i++) {
            AttendanceDTO attendance = attendanceList.get(i);
            if (i > 0) out.print(",");
            out.print(attendance.getWorkDate() + "|" + 
                     attendance.getCheckInTime() + "|" + 
                     attendance.getCheckOutTime() + "|" + 
                     attendance.getTotalWorkMinutes());
        }
        out.flush();
    }
    
    // 월별 일정 조회
    private void getMonthlyEvents(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("loginUser");
        String yearMonth = request.getParameter("yearMonth");
        
        if (userId == null || userId.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        
        if (yearMonth == null) {
            java.time.LocalDate now = java.time.LocalDate.now();
            yearMonth = now.getYear() + "-" + String.format("%02d", now.getMonthValue());
        }
        
        List<CalendarDTO> events = groupwareDAO.getMonthlyEvents(userId, yearMonth);
        
        PrintWriter out = response.getWriter();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        for (int i = 0; i < events.size(); i++) {
            CalendarDTO event = events.get(i);
            if (i > 0) out.print("%%%");
            
            // eventId|title|description|type|startDate|endDate|isAllDay|location
            out.print(
                event.getEventId() + "|" +
                event.getTitle().replace("|", " ").replace("%", " ") + "|" +
                event.getDescription().replace("|", " ").replace("%", " ") + "|" +
                event.getEventType() + "|" +
                sdf.format(event.getStartDate()) + "|" +
                sdf.format(event.getEndDate()) + "|" +
                event.getIsAllDay() + "|" +
                (event.getLocation() != null ? event.getLocation().replace("|", " ") : "")
            );
        }
        out.flush();
    }
    
    // 일정 추가
    private void addEvent(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("loginUser");
        
        if (userId == null || userId.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            PrintWriter out = response.getWriter();
            out.print("로그인이 필요합니다.");
            out.flush();
            return;
        }
        
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            
            CalendarDTO event = new CalendarDTO();
            event.setUserId(userId);
            event.setTitle(request.getParameter("title"));
            event.setDescription(request.getParameter("description"));
            event.setEventType(request.getParameter("eventType"));
            event.setStartDate(new java.sql.Timestamp(sdf.parse(request.getParameter("startDate")).getTime()));
            event.setEndDate(new java.sql.Timestamp(sdf.parse(request.getParameter("endDate")).getTime()));
            event.setIsAllDay(request.getParameter("isAllDay"));
            event.setLocation(request.getParameter("location"));
            
            boolean success = groupwareDAO.addEvent(event);
            
            PrintWriter out = response.getWriter();
            out.print(success ? "success" : "error");
            out.flush();
            
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter out = response.getWriter();
            out.print("error");
            out.flush();
        }
    }
    
    // 일정 수정
    private void updateEvent(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("loginUser");
        
        if (userId == null || userId.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            PrintWriter out = response.getWriter();
            out.print("로그인이 필요합니다.");
            out.flush();
            return;
        }
        
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            
            CalendarDTO event = new CalendarDTO();
            event.setEventId(Integer.parseInt(request.getParameter("eventId")));
            event.setUserId(userId);
            event.setTitle(request.getParameter("title"));
            event.setDescription(request.getParameter("description"));
            event.setEventType(request.getParameter("eventType"));
            event.setStartDate(new java.sql.Timestamp(sdf.parse(request.getParameter("startDate")).getTime()));
            event.setEndDate(new java.sql.Timestamp(sdf.parse(request.getParameter("endDate")).getTime()));
            event.setIsAllDay(request.getParameter("isAllDay"));
            event.setLocation(request.getParameter("location"));
            
            boolean success = groupwareDAO.updateEvent(event);
            
            PrintWriter out = response.getWriter();
            out.print(success ? "success" : "error");
            out.flush();
            
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter out = response.getWriter();
            out.print("error");
            out.flush();
        }
    }
    
    // 일정 삭제
    private void deleteEvent(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("loginUser");
        
        if (userId == null || userId.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            PrintWriter out = response.getWriter();
            out.print("로그인이 필요합니다.");
            out.flush();
            return;
        }
        
        try {
            int eventId = Integer.parseInt(request.getParameter("eventId"));
            boolean success = groupwareDAO.deleteEvent(eventId, userId);
            
            PrintWriter out = response.getWriter();
            out.print(success ? "success" : "error");
            out.flush();
            
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter out = response.getWriter();
            out.print("error");
            out.flush();
        }
    }
}