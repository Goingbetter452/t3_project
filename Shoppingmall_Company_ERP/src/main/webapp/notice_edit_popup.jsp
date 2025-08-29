<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.company1.dao.*, com.company1.dto.*" %>
<%@ page import="java.util.*, java.text.*, java.time.*" %>

<%
    String noticeId = request.getParameter("noticeId");
    String title = "";
    String content = "";
    
    if (noticeId != null) {
        try {
            GroupwareDAO groupwareDAO = new GroupwareDAO();
            NoticeDTO notice = groupwareDAO.getNoticeById(Integer.parseInt(noticeId));
            if (notice != null) {
                title = notice.getTitle();
                content = notice.getContent();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>공지사항 수정</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/groupware.css">
</head>
<body class="popup-body">
    <div class="popup-container">
        <h2>공지사항 수정</h2>
        <form class="popup-form" onsubmit="return false;">
            <div class="form-group">
                <label for="editTitle">제목:</label>
                <input type="text" id="editTitle" name="editTitle" value="<%= title %>" maxlength="100">
            </div>
            <div class="form-group">
                <label for="editContent">내용:</label>
                <textarea id="editContent" name="editContent" rows="10" maxlength="500"><%= content %></textarea>
            </div>
            <div class="button-group">
                <button type="button" class="save-btn" onclick="saveNotice()">저장</button>
                <button type="button" class="cancel-btn" onclick="window.close()">취소</button>
            </div>
        </form>
    </div>

    <script>
        function saveNotice() {
            const title = document.getElementById('editTitle').value.trim();
            const content = document.getElementById('editContent').value.trim();
            const noticeId = '<%= noticeId %>';
            
            if (!title || !content) {
                alert('제목과 내용을 모두 입력해주세요.');
                return;
            }

            // 부모 창의 함수 호출
            if (window.opener && window.opener.updateNoticeFromPopup) {
                window.opener.updateNoticeFromPopup(noticeId, title, content, window);
            } else {
                alert('부모 창과의 연결이 끊어졌습니다.');
            }
        }
    </script>
</body>
</html>