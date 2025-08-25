<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>비밀번호 찾기</title>
<link rel="stylesheet" type="text/css" href="css/login.css">
</head>
<body>

	<div class="login-container">

        <div class="logo">
            <h1>비밀번호 찾기</h1>
        </div>
        
	<form action="FindPasswordServlet" method="post">
    <div class="input-group">
        <input type="text" name="empId" placeholder="아이디" required>
    </div>
    <div class="input-group">
        <input type="email" name="empEmail" placeholder="이메일" required>
    </div>
    <button type="submit" class="login-btn">비밀번호 재설정</button>
	</form>
	</div>
</body>
</html>