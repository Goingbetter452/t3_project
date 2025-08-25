<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>아이디 찾기</title>
<link rel="stylesheet" type="text/css" href="css/login.css"> 
</head>
<body>
    <div class="login-container">
        <div class="logo"><h1>아이디 찾기</h1></div>
        <form action="FindIdServlet" method="post">
            <div class="input-group">
                <input type="text" name="empName" placeholder="이름" required>
            </div>
            <div class="input-group">
                <input type="email" name="empEmail" placeholder="이메일" required>
            </div>
            <button type="submit" class="login-btn">아이디 찾기</button>
        </form>
    </div>
</body>
</html>