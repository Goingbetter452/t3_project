<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>아이디 찾기 결과</title>
<link rel="stylesheet" type="text/css" href="css/login.css">
</head>
<body>

<!-- 헤더 포함 -->
<%@ include file="common-jsp/header.jsp" %>

<div class="page-content">
    <div class="login-container">
        <div class="logo"><h1>아이디 찾기 결과</h1></div>
        <% 
            String foundId = (String) request.getAttribute("foundId");
            if (foundId != null && !foundId.isEmpty()) {
        %>
            <p style="margin: 20px 0;">회원님의 아이디는 <strong><%= foundId %></strong> 입니다.</p>
        <% 
            } else {
        %>
            <p style="margin: 20px 0; color: red;">입력하신 정보와 일치하는 아이디를 찾을 수 없습니다.</p>
        <% 
            }
        %>
        <a href="login.jsp" class="login-btn" style="text-decoration: none; display: inline-block; text-align: center;">로그인 하러 가기</a>
    </div>
</div>
</body>
</html>