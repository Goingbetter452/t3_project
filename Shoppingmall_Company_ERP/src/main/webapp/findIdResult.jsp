<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>아이디 찾기 결과</title>
<link rel="stylesheet" type="text/css" href="css/login.css">
	
	<div class="login-container">
        <div class="logo"><h1>아이디 찾기 결과</h1></div>
        <p style="margin: 20px 0;">회원님의 아이디는 <strong>${requestScope.foundId}</strong> 입니다.</p>
        <a href="login.jsp" class="login-btn" style="text-decoration: none;">로그인 하러 가기</a>
    </div>

</head>
<body>
	<div class="login-container">
    <h1>아이디 찾기 결과</h1>
    <p>회원님의 아이디는 <strong>${requestScope.foundId}</strong> 입니다.</p>
    <a href="login.jsp">로그인 하러 가기</a>
</div>
</body>
</html>