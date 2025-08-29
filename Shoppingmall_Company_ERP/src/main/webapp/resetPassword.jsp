<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="css/login.css">
</head>
<body>
	<div class="login-container">

        <div class="logo">
            <h1 class="title">비밀번호 재설정</h1>
        </div>

	<form action="ResetPasswordServlet" method="post">
    <input type="hidden" name="empId" value="${requestScope.empId}">
    <div class="input-group">
        <input type="password" name="newPw" placeholder="새 비밀번호" required>
    </div>
    <div class="input-group">
        <input type="password" name="confirmPw" placeholder="새 비밀번호 확인" required>
    </div>
    <button type="submit" class="login-btn">비밀번호 변경</button>
	</form>
	</div>
</body>
</html>
