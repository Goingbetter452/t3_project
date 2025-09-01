<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>비밀번호 재설정</title>
<link rel="stylesheet" type="text/css" href="css/common.css">
<link rel="stylesheet" type="text/css" href="css/login.css">
</head>
<body>

<!-- 헤더 포함 -->
<%@ include file="common-jsp/header.jsp" %>

<div class="page-content">
    <div class="login-container">
        <div class="logo">
            <h1 class="title">비밀번호 재설정</h1>
        </div>

        <% 
            Boolean isValidUser = (Boolean) request.getAttribute("isValidUser");
            String empId = (String) request.getAttribute("empId");
            String error = (String) request.getAttribute("error");
            
            // 에러 메시지가 있으면 표시
            if (error != null) {
        %>
            <p style="margin: 20px 0; color: red;"><%= error %></p>
        <%
            }
            
            if (isValidUser != null && isValidUser) {
        %>
            <% if (error == null) { %>
                <p style="margin: 20px 0; color: green;">사용자 확인이 완료되었습니다.</p>
            <% } %>
            <form action="ResetPasswordServlet" method="post">
                <input type="hidden" name="empId" value="<%= empId %>">
                <div class="input-group">
                    <input type="password" name="newPw" placeholder="새 비밀번호" required>
                </div>
                <div class="input-group">
                    <input type="password" name="confirmPw" placeholder="새 비밀번호 확인" required>
                </div>
                <button type="submit" class="login-btn">비밀번호 변경</button>
            </form>
        <% 
            } else if (isValidUser != null && !isValidUser) {
        %>
            <p style="margin: 20px 0; color: red;">입력하신 아이디와 이메일 정보가 일치하지 않습니다.</p>
            <a href="findPassword.jsp" class="login-btn" style="text-decoration: none; display: inline-block; text-align: center;">다시 시도</a>
        <% 
            } else {
        %>
            <p style="margin: 20px 0;">비밀번호를 재설정하시려면 먼저 아이디와 이메일을 확인해주세요.</p>
            <a href="findPassword.jsp" class="login-btn" style="text-decoration: none; display: inline-block; text-align: center;">비밀번호 찾기</a>
        <% 
            }
        %>
        
        <div style="margin-top: 20px;">
            <a href="login.jsp" style="color: #666; text-decoration: none;">로그인 화면으로 돌아가기</a>
        </div>
    </div>
</div>
</body>
</html>