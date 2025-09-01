<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>비밀번호 찾기 결과</title>
<link rel="stylesheet" type="text/css" href="css/login.css"> <%-- 기존 CSS 재활용 --%>
</head>
<body>
    <div class="login-container">
        <div class="logo">
            <h1>비밀번호 찾기 결과</h1>
        </div>

        <%-- JSTL의 choose, when, otherwise를 사용하여 조건에 따라 다른 내용을 보여줍니다. --%>
        <c:choose>
            <%-- 서블릿이 "password"라는 이름으로 정보를 담아 보냈을 때 (성공) --%>
            <c:when test="${not empty requestScope.password}">
                <div class="result-message">
                    <p><strong>${requestScope.empId}</strong> 님의 비밀번호는</p>
                    <p class="password-box">${requestScope.password}</p>
                    <p>입니다.</p>
                </div>
                <a href="login.jsp" class="login-btn" style="text-decoration: none; text-align: center; line-height: 50px;">로그인 하러 가기</a>
            </c:when>
            
            <%-- 그 외의 경우, 즉 서블릿이 "error" 정보를 보냈을 때 (실패) --%>
            <c:otherwise>
                <div class="result-message">
                    <p>${requestScope.error}</p>
                </div>
                <a href="javascript:history.back()" class="login-btn" style="text-decoration: none; text-align: center; line-height: 50px;">다시 시도하기</a>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>