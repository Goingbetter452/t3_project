<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="java.io.*" %>
<%@ page import="com.company1.DBManager" %>
    
<!DOCTYPE html>
<html>
<head>
<!-- 밑의 링크는 css 연결을 위한 코드 -->
<Link rel="stylesheet" type="text/css" href="css/login.css">  
<meta charset="UTF-8">
<title>로그인2026</title>
</head>
<body>
       <div class="login-container">
        
        <div class="logo">
            <h1>로그인</h1>
        </div>

   <form action="LoginServlet" method="post" class="login-form">
            <div class="input-group">
                <input type="text" name="username" placeholder="아이디">
            </div>
            <div class="input-group">
                <input type="password" name="password" placeholder="비밀번호">
            </div>
    		
    		<button type="submit" class="login-btn">로그인</button>
    		
    		<div class="find-links">
    			<a href="#">비밀번호 찾기</a>
    			<span>|</span>
    			<a href="#">아이디 찾기</a>
    			<span>|</span>
    			<a href="#">회원가입</a>
    		</div>  
	</form>
   
    </form>
</div> <div class="ad-banner">
    <a href="#"> <img src="images/loginpage.jpg" alt="자연 풍경 이미지">
    </a>
</div>
   
</body>
</html>