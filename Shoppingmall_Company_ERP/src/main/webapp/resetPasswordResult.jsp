<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>비밀번호 재설정 결과</title>
<link rel="stylesheet" type="text/css" href="css/common.css">
<link rel="stylesheet" type="text/css" href="css/login.css">
</head>
<body>

<!-- 헤더 포함 -->
<%@ include file="common-jsp/header.jsp" %>

<div class="page-content">
    <div class="login-container">
        <div class="logo">
            <h1>비밀번호 재설정 결과</h1>
        </div>

        <% 
            Boolean success = (Boolean) request.getAttribute("success");
            String message = (String) request.getAttribute("message");
            
            if (success != null && success) {
        %>
            <p style="margin: 20px 0; color: green;"><%= message %></p>
            <a href="login.jsp" class="login-btn" style="text-decoration: none; display: inline-block; text-align: center;">로그인 하러 가기</a>
        <% 
            } else {
        %>
            <p style="margin: 20px 0; color: red;"><%= message %></p>
            <a href="findPassword.jsp" class="login-btn" style="text-decoration: none; display: inline-block; text-align: center;">다시 시도</a>
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