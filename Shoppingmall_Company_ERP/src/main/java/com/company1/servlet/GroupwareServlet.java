package com.company1.servlet;

import java.io.IOException;
import java.io.PrintWriter;
<<<<<<< HEAD
=======
import java.text.SimpleDateFormat;
>>>>>>> cddda14998e5a164e841ccb98ce4bf191064d936
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
<<<<<<< HEAD
=======
import com.company1.dto.CalendarDTO;
import com.company1.dto.MessageDTO;
import com.company1.dto.TodoDTO;
>>>>>>> cddda14998e5a164e841ccb98ce4bf191064d936

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
<<<<<<< HEAD
=======
            case "getMonthlyEvents":  // 추가: 월별 일정 조회
                getMonthlyEvents(request, response);
                break;
            case "getEvent":  // 추가: 단일 일정 조회
                getEvent(request, response);
                break;
            case "getTodos":  // 추가: 할일 목록 조회
                getTodos(request, response);
                break;
            case "getMessages":
                getMessages(request, response);
                break;
>>>>>>> cddda14998e5a164e841ccb98ce4bf191064d936
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
<<<<<<< HEAD
=======
            case "addEvent":      // 추가: 일정 추가
                addEvent(request, response);
                break;
            case "updateEvent":   // 추가: 일정 수정
                updateEvent(request, response);
                break;
            case "deleteEvent":   // 추가: 일정 삭제
                deleteEvent(request, response);
                break;
            case "addTodo":       // 추가: 할일 추가
                addTodo(request, response);
                break;
            case "updateTodo":    // 추가: 할일 수정
                updateTodo(request, response);
                break;
            case "deleteTodo":    // 추가: 할일 삭제
                deleteTodo(request, response);
                break;
            case "toggleTodo":    // 추가: 할일 완료/미완료 토글
                toggleTodo(request, response);
                break;
            case "sendMessage":
                sendMessage(request, response);
                break;
            case "deleteMessage":
                deleteMessage(request, response);
                break;
            case "markMessageRead":
                markMessageRead(request, response);
                break;
>>>>>>> cddda14998e5a164e841ccb98ce4bf191064d936
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid command");
                break;
        }
    }
    
<<<<<<< HEAD
	/**
=======
    /**
>>>>>>> cddda14998e5a164e841ccb98ce4bf191064d936
     * 모든 공지사항을 텍스트 형태로 반환합니다.
     */
    private void getNotices(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        
        List<NoticeDTO> notices = groupwareDAO.getAllNotices();
        
        PrintWriter out = response.getWriter();
<<<<<<< HEAD

        for (int i = 0; i < notices.size(); i++) {
            NoticeDTO notice = notices.get(i);
            if (i > 0) out.print(",");
            out.print(notice.getTitle() + "|" + 
                     notice.getContent() + "|" + 
                     notice.getAuthorName() + "|" + 
                     notice.getCreateDate());
        }

=======
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
>>>>>>> cddda14998e5a164e841ccb98ce4bf191064d936
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
<<<<<<< HEAD
        String authorId = (String) session.getAttribute("userId");
        String authorName = (String) session.getAttribute("userName");
        
        // 세션에 사용자 정보가 없는 경우 기본값 설정
        if (authorId == null) {
            authorId = "guest";
            authorName = "게스트";
=======
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
>>>>>>> cddda14998e5a164e841ccb98ce4bf191064d936
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
<<<<<<< HEAD
>>>>>>> cb00c5fcb904cfc4347a707877c00f9821a0116c
=======
>>>>>>> cddda14998e5a164e841ccb98ce4bf191064d936
     * 출근 처리를 합니다.
     */
    private void checkIn(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession();
<<<<<<< HEAD
        String userId = (String) session.getAttribute("userId");
        
        if (userId == null) {
            userId = "guest"; // 테스트용 기본값
=======
        String userId = (String) session.getAttribute("loginUser");  // userId -> loginUser로 변경
        
        // 사용자 정보가 없으면 FK 제약조건 위반 가능성 -> 에러 반환
        if (userId == null || userId.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            PrintWriter out = response.getWriter();
            out.print("error");
            out.flush();
            return;
>>>>>>> cddda14998e5a164e841ccb98ce4bf191064d936
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
<<<<<<< HEAD
        String userId = (String) session.getAttribute("userId");
        
        if (userId == null) {
            userId = "guest"; // 테스트용 기본값
=======
        String userId = (String) session.getAttribute("loginUser");  // userId -> loginUser로 변경
        
        // 사용자 정보가 없으면 FK 제약조건 위반 가능성 -> 에러 반환
        if (userId == null || userId.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            PrintWriter out = response.getWriter();
            out.print("error");
            out.flush();
            return;
>>>>>>> cddda14998e5a164e841ccb98ce4bf191064d936
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
<<<<<<< HEAD
        String userId = (String) session.getAttribute("userId");
        
        if (userId == null) {
            userId = "guest"; // 테스트용 기본값
=======
        String userId = (String) session.getAttribute("loginUser");  // userId -> loginUser로 변경
        
        // 로그인 정보가 없으면 빈 응답 반환
        if (userId == null || userId.trim().isEmpty()) {
            PrintWriter out = response.getWriter();
            out.print("");
            out.flush();
            return;
>>>>>>> cddda14998e5a164e841ccb98ce4bf191064d936
        }
        
        AttendanceDTO attendance = groupwareDAO.getTodayAttendance(userId);
        
        PrintWriter out = response.getWriter();
        if (attendance != null) {
<<<<<<< HEAD
            out.print("checkInTime:" + attendance.getCheckInTime() + ",");
            out.print("checkOutTime:" + attendance.getCheckOutTime() + ",");
            out.print("totalWorkMinutes:" + attendance.getTotalWorkMinutes() + ",");
            out.print("status:" + attendance.getStatus());
=======
            // null 값을 빈 문자열로 변환하여 전송
            String checkInTime = attendance.getCheckInTime() != null ? attendance.getCheckInTime().toString() : "";
            String checkOutTime = attendance.getCheckOutTime() != null ? attendance.getCheckOutTime().toString() : "";
            String status = attendance.getStatus() != null ? attendance.getStatus() : "";
            
            out.print("checkInTime:" + checkInTime + ",");
            out.print("checkOutTime:" + checkOutTime + ",");
            out.print("totalWorkMinutes:" + attendance.getTotalWorkMinutes() + ",");
            out.print("status:" + status);
>>>>>>> cddda14998e5a164e841ccb98ce4bf191064d936
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
<<<<<<< HEAD
        String userId = (String) session.getAttribute("userId");
        String yearMonth = request.getParameter("yearMonth");
        
        if (userId == null) {
            userId = "guest"; // 테스트용 기본값
=======
        String userId = (String) session.getAttribute("loginUser");  // userId -> loginUser로 변경
        String yearMonth = request.getParameter("yearMonth");
        
        // 로그인 정보가 없으면 빈 응답 반환
        if (userId == null || userId.trim().isEmpty()) {
            PrintWriter out = response.getWriter();
            out.print("");
            out.flush();
            return;
>>>>>>> cddda14998e5a164e841ccb98ce4bf191064d936
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
<<<<<<< HEAD
}
=======
    
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
    
    // 단일 일정 조회
    private void getEvent(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("loginUser");
        String eventIdStr = request.getParameter("eventId");
        
        if (userId == null || userId.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            PrintWriter out = response.getWriter();
            out.print("error");
            out.flush();
            return;
        }
        
        if (eventIdStr == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter out = response.getWriter();
            out.print("error");
            out.flush();
            return;
        }
        
        try {
            int eventId = Integer.parseInt(eventIdStr);
            CalendarDTO event = groupwareDAO.getEventById(eventId, userId);
            
            PrintWriter out = response.getWriter();
            if (event != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                
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
    
    // ===== 할일 관련 메서드 =====
    
    /**
     * 사용자의 할일 목록을 조회합니다.
     */
    private void getTodos(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("loginUser");
        
        if (userId == null || userId.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            PrintWriter out = response.getWriter();
            out.print("");
            out.flush();
            return;
        }
        
        List<TodoDTO> todos = groupwareDAO.getUserTodos(userId);
        
        PrintWriter out = response.getWriter();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        for (int i = 0; i < todos.size(); i++) {
            TodoDTO todo = todos.get(i);
            if (i > 0) out.print("%%%");
            
            // todoId|title|description|isCompleted|priority|dueDate|createDate
            String dueDateStr = (todo.getDueDate() != null) ? sdf.format(todo.getDueDate()) : "";
            String createDateStr = (todo.getCreateDate() != null) ? sdf.format(todo.getCreateDate()) : "";
            
            out.print(
                todo.getTodoId() + "|" +
                todo.getTitle().replace("|", " ").replace("%", " ") + "|" +
                (todo.getDescription() != null ? todo.getDescription().replace("|", " ").replace("%", " ") : "") + "|" +
                todo.getIsCompleted() + "|" +
                todo.getPriority() + "|" +
                dueDateStr + "|" +
                createDateStr
            );
        }
        out.flush();
    }
    
    /**
     * 새로운 할일을 추가합니다.
     */
    private void addTodo(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("loginUser");
        
        if (userId == null || userId.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            PrintWriter out = response.getWriter();
            out.print("error");
            out.flush();
            return;
        }
        
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String priorityStr = request.getParameter("priority");
        String dueDateStr = request.getParameter("dueDate");
        
        if (title == null || title.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter out = response.getWriter();
            out.print("error");
            out.flush();
            return;
        }
        
        try {
            TodoDTO todo = new TodoDTO();
            todo.setUserId(userId);
            todo.setTitle(title.trim());
            todo.setDescription(description != null ? description.trim() : "");
            todo.setPriority(priorityStr != null ? Integer.parseInt(priorityStr) : 3);
            
            if (dueDateStr != null && !dueDateStr.trim().isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                todo.setDueDate(new java.sql.Date(sdf.parse(dueDateStr).getTime()));
            }
            
            boolean success = groupwareDAO.addTodo(todo);
            
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
    
    /**
     * 할일을 수정합니다.
     */
    private void updateTodo(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("loginUser");
        
        if (userId == null || userId.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            PrintWriter out = response.getWriter();
            out.print("error");
            out.flush();
            return;
        }
        
        String todoIdStr = request.getParameter("todoId");
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String priorityStr = request.getParameter("priority");
        String dueDateStr = request.getParameter("dueDate");
        
        if (todoIdStr == null || title == null || title.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter out = response.getWriter();
            out.print("error");
            out.flush();
            return;
        }
        
        try {
            TodoDTO todo = new TodoDTO();
            todo.setTodoId(Integer.parseInt(todoIdStr));
            todo.setUserId(userId);
            todo.setTitle(title.trim());
            todo.setDescription(description != null ? description.trim() : "");
            todo.setPriority(priorityStr != null ? Integer.parseInt(priorityStr) : 3);
            
            if (dueDateStr != null && !dueDateStr.trim().isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                todo.setDueDate(new java.sql.Date(sdf.parse(dueDateStr).getTime()));
            }
            
            boolean success = groupwareDAO.updateTodo(todo);
            
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
    
    /**
     * 할일을 삭제합니다.
     */
    private void deleteTodo(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("loginUser");
        
        if (userId == null || userId.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            PrintWriter out = response.getWriter();
            out.print("error");
            out.flush();
            return;
        }
        
        String todoIdStr = request.getParameter("todoId");
        
        if (todoIdStr == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter out = response.getWriter();
            out.print("error");
            out.flush();
            return;
        }
        
        try {
            int todoId = Integer.parseInt(todoIdStr);
            boolean success = groupwareDAO.deleteTodo(todoId, userId);
            
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
    
    /**
     * 할일의 완료/미완료 상태를 토글합니다.
     */
    private void toggleTodo(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("loginUser");
        
        if (userId == null || userId.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            PrintWriter out = response.getWriter();
            out.print("error");
            out.flush();
            return;
        }
        
        String todoIdStr = request.getParameter("todoId");
        
        if (todoIdStr == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter out = response.getWriter();
            out.print("error");
            out.flush();
            return;
        }
        
        try {
            int todoId = Integer.parseInt(todoIdStr);
            boolean success = groupwareDAO.toggleTodoCompletion(todoId, userId);
            
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
    
    /**
     * 메시지 목록을 조회합니다.
     */
    private void getMessages(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("loginUser");
        
        if (userId == null || userId.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        
        List<MessageDTO> messages = groupwareDAO.getUserMessages(userId);
        PrintWriter out = response.getWriter();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        for (int i = 0; i < messages.size(); i++) {
            MessageDTO msg = messages.get(i);
            if (i > 0) out.print("%%%");
            
            // messageId|type|senderId|senderName|receiverId|receiverName|content|isRead|sendDate|readDate
            out.print(
                msg.getMessageId() + "|" +
                msg.getMessageType() + "|" +
                msg.getSenderId() + "|" +
                msg.getSenderName() + "|" +
                (msg.getReceiverId() != null ? msg.getReceiverId() : "") + "|" +
                (msg.getReceiverName() != null ? msg.getReceiverName() : "") + "|" +
                msg.getContent().replace("|", " ").replace("%", " ") + "|" +
                msg.getIsRead() + "|" +
                sdf.format(msg.getSendDate()) + "|" +
                (msg.getReadDate() != null ? sdf.format(msg.getReadDate()) : "")
            );
        }
        out.flush();
    }
    
    /**
     * 새 메시지를 전송합니다.
     */
    private void sendMessage(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession();
        String senderId = (String) session.getAttribute("loginUser");
        String senderName = (String) session.getAttribute("userName");
        
        if (senderId == null || senderId.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            PrintWriter out = response.getWriter();
            out.print("error");
            out.flush();
            return;
        }
        
        String receiverId = request.getParameter("receiverId");
        String content = request.getParameter("content");
        String messageType = request.getParameter("messageType");
        
        if (content == null || content.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter out = response.getWriter();
            out.print("error");
            out.flush();
            return;
        }
        
        MessageDTO message = new MessageDTO();
        message.setSenderId(senderId);
        message.setSenderName(senderName != null ? senderName : senderId);
        message.setReceiverId(receiverId);
        message.setContent(content.trim());
        message.setMessageType(messageType != null ? messageType : "PERSONAL");
        
        boolean success = groupwareDAO.sendMessage(message);
        
        PrintWriter out = response.getWriter();
        out.print(success ? "success" : "error");
        out.flush();
    }
    
    /**
     * 메시지를 삭제합니다.
     */
    private void deleteMessage(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("loginUser");
        
        if (userId == null || userId.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            PrintWriter out = response.getWriter();
            out.print("error");
            out.flush();
            return;
        }
        
        String messageIdStr = request.getParameter("messageId");
        if (messageIdStr == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter out = response.getWriter();
            out.print("error");
            out.flush();
            return;
        }
        
        try {
            int messageId = Integer.parseInt(messageIdStr);
            boolean success = groupwareDAO.deleteMessage(messageId, userId);
            
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
    
    /**
     * 메시지를 읽음 표시합니다.
     */
    private void markMessageRead(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("loginUser");
        
        if (userId == null || userId.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            PrintWriter out = response.getWriter();
            out.print("error");
            out.flush();
            return;
        }
        
        String messageIdStr = request.getParameter("messageId");
        if (messageIdStr == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter out = response.getWriter();
            out.print("error");
            out.flush();
            return;
        }
        
        try {
            int messageId = Integer.parseInt(messageIdStr);
            boolean success = groupwareDAO.markMessageAsRead(messageId, userId);
            
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
>>>>>>> cddda14998e5a164e841ccb98ce4bf191064d936
