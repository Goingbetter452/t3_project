<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
import java.sql.ResultSet;
<% // import 대신 JSTL을 사용하여 데이터베이스 연결 및 쿼리 실행 
ResultSet rs = (ResultSet) request.getAttribute("customerList"); // 고객 목록을 가져옵니다.
%>
<html>
<head>
<meta charset="UTF-8">
<head><title>고객 관리</title></head>
</head>
<body>
	<h2>고객 관리</h2>
	<a href = "dashboard.jsp">대시보드</a><br><br>
	
	
</body>
</html>