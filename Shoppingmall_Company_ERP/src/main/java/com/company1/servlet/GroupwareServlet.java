package com.company1.servlet;

import java.io.IOException;
import java.io.PrintWriter;
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
<<<<<<< HEAD
=======
            case "updateNotice":
                updateNotice(request, response);
                break;
            case "deleteNotice":
                deleteNotice(request, response);
                break;
>>>>>>> cb00c5fcb904cfc4347a707877c00f9821a0116c
            case "checkIn":
                checkIn(request, response);
                break;
            case "checkOut":
                checkOut(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid command");
                break;
        }
    }
    
    /**
<<<<<<< HEAD
     * 모든 공지사항을 JSON 형태로 반환합니다.
     */
    private void getNotices(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json");
=======
     * 모든 공지사항을 텍스트 형태로 반환합니다.
     */
    private void getNotices(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/plain");
>>>>>>> cb00c5fcb904cfc4347a707877c00f9821a0116c
        response.setCharacterEncoding("UTF-8");
        
        List<NoticeDTO> notices = groupwareDAO.getAllNotices();
        
        PrintWriter out = response.getWriter();
<<<<<<< HEAD
        out.print("[");
        for (int i = 0; i < notices.size(); i++) {
            NoticeDTO notice = notices.get(i);
            if (i > 0) out.print(",");
            out.print("{");
            out.print("\"noticeId\":" + notice.getNoticeId() + ",");
            out.print("\"title\":\"" + escapeJson(notice.getTitle()) + "\",");
            out.print("\"content\":\"" + escapeJson(notice.getContent()) + "\",");
            out.print("\"authorId\":\"" + escapeJson(notice.getAuthorId()) + "\",");
            out.print("\"authorName\":\"" + escapeJson(notice.getAuthorName()) + "\",");
            out.print("\"createDate\":\"" + notice.getCreateDate() + "\",");
            out.print("\"updateDate\":\"" + notice.getUpdateDate() + "\",");
            out.print("\"viewCount\":" + notice.getViewCount() + ",");
            out.print("\"isActive\":\"" + escapeJson(notice.getIsActive()) + "\"");
            out.print("}");
        }
        out.print("]");
=======
        for (int i = 0; i < notices.size(); i++) {
            NoticeDTO notice = notices.get(i);
            if (i > 0) out.print(",");
            out.print(notice.getTitle() + "|" + 
                     notice.getContent() + "|" + 
                     notice.getAuthorName() + "|" + 
                     notice.getCreateDate());
        }
>>>>>>> cb00c5fcb904cfc4347a707877c00f9821a0116c
        out.flush();
    }
    
    /**
     * 새로운 공지사항을 등록합니다.
     */
    private void addNotice(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
<<<<<<< HEAD
        response.setContentType("application/json");
=======
        response.setContentType("text/plain");
>>>>>>> cb00c5fcb904cfc4347a707877c00f9821a0116c
        response.setCharacterEncoding("UTF-8");
        
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        
        HttpSession session = request.getSession();
        String authorId = (String) session.getAttribute("userId");
        String authorName = (String) session.getAttribute("userName");
        
        // 세션에 사용자 정보가 없는 경우 기본값 설정
        if (authorId == null) {
            authorId = "guest";
            authorName = "게스트";
        }
        
        if (title == null || title.trim().isEmpty() || 
            content == null || content.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter out = response.getWriter();
<<<<<<< HEAD
            out.print("{\"success\": false, \"message\": \"제목과 내용을 모두 입력해주세요.\"}");
=======
            out.print("error");
>>>>>>> cb00c5fcb904cfc4347a707877c00f9821a0116c
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
<<<<<<< HEAD
            out.print("{\"success\": true, \"message\": \"공지사항이 등록되었습니다.\"}");
        } else {
            out.print("{\"success\": false, \"message\": \"공지사항 등록에 실패했습니다.\"}");
=======
            out.print("success");
        } else {
            out.print("error");
>>>>>>> cb00c5fcb904cfc4347a707877c00f9821a0116c
        }
        out.flush();
    }
    
    /**
<<<<<<< HEAD
=======
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
>>>>>>> cb00c5fcb904cfc4347a707877c00f9821a0116c
     * 출근 처리를 합니다.
     */
    private void checkIn(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
<<<<<<< HEAD
        response.setContentType("application/json");
=======
        response.setContentType("text/plain");
>>>>>>> cb00c5fcb904cfc4347a707877c00f9821a0116c
        response.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userId");
        
        if (userId == null) {
            userId = "guest"; // 테스트용 기본값
        }
        
        PrintWriter out = response.getWriter();
        
        // 이미 출근했는지 확인
        if (groupwareDAO.isAlreadyCheckedIn(userId)) {
<<<<<<< HEAD
            out.print("{\"success\": false, \"message\": \"오늘 이미 출근 처리되었습니다.\"}");
=======
            out.print("error");
>>>>>>> cb00c5fcb904cfc4347a707877c00f9821a0116c
            return;
        }
        
        boolean success = groupwareDAO.checkIn(userId);
        
        if (success) {
<<<<<<< HEAD
            out.print("{\"success\": true, \"message\": \"출근 처리되었습니다.\"}");
        } else {
            out.print("{\"success\": false, \"message\": \"출근 처리에 실패했습니다.\"}");
=======
            out.print("success");
        } else {
            out.print("error");
>>>>>>> cb00c5fcb904cfc4347a707877c00f9821a0116c
        }
        out.flush();
    }
    
    /**
     * 퇴근 처리를 합니다.
     */
    private void checkOut(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
<<<<<<< HEAD
        response.setContentType("application/json");
=======
        response.setContentType("text/plain");
>>>>>>> cb00c5fcb904cfc4347a707877c00f9821a0116c
        response.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userId");
        
        if (userId == null) {
            userId = "guest"; // 테스트용 기본값
        }
        
        PrintWriter out = response.getWriter();
        
        // 출근 기록이 있는지 확인
        if (!groupwareDAO.isAlreadyCheckedIn(userId)) {
<<<<<<< HEAD
            out.print("{\"success\": false, \"message\": \"출근 기록이 없습니다.\"}");
=======
            out.print("error");
>>>>>>> cb00c5fcb904cfc4347a707877c00f9821a0116c
            return;
        }
        
        // 이미 퇴근했는지 확인
        if (groupwareDAO.isAlreadyCheckedOut(userId)) {
<<<<<<< HEAD
            out.print("{\"success\": false, \"message\": \"오늘 이미 퇴근 처리되었습니다.\"}");
=======
            out.print("error");
>>>>>>> cb00c5fcb904cfc4347a707877c00f9821a0116c
            return;
        }
        
        boolean success = groupwareDAO.checkOut(userId);
        
        if (success) {
<<<<<<< HEAD
            out.print("{\"success\": true, \"message\": \"퇴근 처리되었습니다.\"}");
        } else {
            out.print("{\"success\": false, \"message\": \"퇴근 처리에 실패했습니다.\"}");
=======
            out.print("success");
        } else {
            out.print("error");
>>>>>>> cb00c5fcb904cfc4347a707877c00f9821a0116c
        }
        out.flush();
    }
    
    /**
<<<<<<< HEAD
     * 오늘의 출퇴근 정보를 JSON 형태로 반환합니다.
     */
    private void getTodayAttendance(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json");
=======
     * 오늘의 출퇴근 정보를 텍스트 형태로 반환합니다.
     */
    private void getTodayAttendance(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/plain");
>>>>>>> cb00c5fcb904cfc4347a707877c00f9821a0116c
        response.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userId");
        
        if (userId == null) {
            userId = "guest"; // 테스트용 기본값
        }
        
        AttendanceDTO attendance = groupwareDAO.getTodayAttendance(userId);
        
        PrintWriter out = response.getWriter();
        if (attendance != null) {
<<<<<<< HEAD
            out.print("{");
            out.print("\"attendanceId\":" + attendance.getAttendanceId() + ",");
            out.print("\"userId\":\"" + escapeJson(attendance.getUserId()) + "\",");
            out.print("\"workDate\":\"" + attendance.getWorkDate() + "\",");
            out.print("\"checkInTime\":" + (attendance.getCheckInTime() != null ? "\"" + attendance.getCheckInTime() + "\"" : "null") + ",");
            out.print("\"checkOutTime\":" + (attendance.getCheckOutTime() != null ? "\"" + attendance.getCheckOutTime() + "\"" : "null") + ",");
            out.print("\"totalWorkMinutes\":" + attendance.getTotalWorkMinutes() + ",");
            out.print("\"breakMinutes\":" + attendance.getBreakMinutes() + ",");
            out.print("\"overtimeMinutes\":" + attendance.getOvertimeMinutes() + ",");
            out.print("\"status\":\"" + escapeJson(attendance.getStatus()) + "\",");
            out.print("\"notes\":" + (attendance.getNotes() != null ? "\"" + escapeJson(attendance.getNotes()) + "\"" : "null") + ",");
            out.print("\"createDate\":\"" + attendance.getCreateDate() + "\",");
            out.print("\"updateDate\":\"" + attendance.getUpdateDate() + "\"");
            out.print("}");
        } else {
            out.print("null");
=======
            out.print("checkInTime:" + attendance.getCheckInTime() + ",");
            out.print("checkOutTime:" + attendance.getCheckOutTime() + ",");
            out.print("totalWorkMinutes:" + attendance.getTotalWorkMinutes() + ",");
            out.print("status:" + attendance.getStatus());
        } else {
            out.print("");
>>>>>>> cb00c5fcb904cfc4347a707877c00f9821a0116c
        }
        out.flush();
    }
    
    /**
<<<<<<< HEAD
     * 월별 출퇴근 정보를 JSON 형태로 반환합니다.
     */
    private void getMonthlyAttendance(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json");
=======
     * 월별 출퇴근 정보를 텍스트 형태로 반환합니다.
     */
    private void getMonthlyAttendance(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/plain");
>>>>>>> cb00c5fcb904cfc4347a707877c00f9821a0116c
        response.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userId");
        String yearMonth = request.getParameter("yearMonth");
        
        if (userId == null) {
            userId = "guest"; // 테스트용 기본값
        }
        
        if (yearMonth == null) {
            // 현재 년월을 기본값으로 설정
            java.time.LocalDate now = java.time.LocalDate.now();
            yearMonth = now.getYear() + "-" + String.format("%02d", now.getMonthValue());
        }
        
        List<AttendanceDTO> attendanceList = groupwareDAO.getMonthlyAttendance(userId, yearMonth);
        
        PrintWriter out = response.getWriter();
<<<<<<< HEAD
        out.print("[");
        for (int i = 0; i < attendanceList.size(); i++) {
            AttendanceDTO attendance = attendanceList.get(i);
            if (i > 0) out.print(",");
            out.print("{");
            out.print("\"attendanceId\":" + attendance.getAttendanceId() + ",");
            out.print("\"userId\":\"" + escapeJson(attendance.getUserId()) + "\",");
            out.print("\"workDate\":\"" + attendance.getWorkDate() + "\",");
            out.print("\"checkInTime\":" + (attendance.getCheckInTime() != null ? "\"" + attendance.getCheckInTime() + "\"" : "null") + ",");
            out.print("\"checkOutTime\":" + (attendance.getCheckOutTime() != null ? "\"" + attendance.getCheckOutTime() + "\"" : "null") + ",");
            out.print("\"totalWorkMinutes\":" + attendance.getTotalWorkMinutes() + ",");
            out.print("\"breakMinutes\":" + attendance.getBreakMinutes() + ",");
            out.print("\"overtimeMinutes\":" + attendance.getOvertimeMinutes() + ",");
            out.print("\"status\":\"" + escapeJson(attendance.getStatus()) + "\",");
            out.print("\"notes\":" + (attendance.getNotes() != null ? "\"" + escapeJson(attendance.getNotes()) + "\"" : "null") + ",");
            out.print("\"createDate\":\"" + attendance.getCreateDate() + "\",");
            out.print("\"updateDate\":\"" + attendance.getUpdateDate() + "\"");
            out.print("}");
        }
        out.print("]");
        out.flush();
    }
    
    /**
     * JSON 문자열에서 특수문자를 이스케이프 처리합니다.
     */
    private String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\b", "\\b")
                  .replace("\f", "\\f")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }
=======
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
}
>>>>>>> cb00c5fcb904cfc4347a707877c00f9821a0116c
}