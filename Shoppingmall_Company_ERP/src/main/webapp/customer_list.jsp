<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.sql.ResultSet" %>

<% 
ResultSet rs = (ResultSet) request.getAttribute("customerList"); // 고객 목록을 가져옵니다.
%>
<html>
<head>
<meta charset="UTF-8">
<head><title>고객 관리</title></head>
</head>
<body>
	<h2>고객 관리</h2>
	<a href = "index.jsp">대시보드</a><br><br>
	
	<h3>고객 등록</h3>
	<form action="CustomerServlet" method="post">
	<input type="hidden" name="action" value="insert" />
	이름 : <input type="text" name="name" required />
	이메일 : <input type="email" name="email" />
	<input type= "submit" value="등록" />
	</form>
	
	<%-- 고객 목록을 표시하는 테이블 --%>
	<h3>고객 목록</h3>
	<table border="1">
        <tr>
            <th>고객 ID</th>
            <th>이름</th>
            <th>이메일</th>
            <th>수정</th>
            <th>삭제</th>
        </tr>
        
        <%-- 고객 목록이 비어있지 않은 경우에만 반복문 실행 --%>
        <%
        try {
        	while(rs != null && rs.next()) {
        		%>
        		<tr>
        		<td><%= rs.getInt("cid") %></td>
        		<td><%= rs.getString("cname") %></td>
        		<td><%= rs.getString("email") %></td>
        		<td><a href="CustomerServlet?action=edit&cid=<%= rs.getInt("cid") %>">수정</a></td>
        		<td><a href="CustomerServlet?action=delete&cid=<%= rs.getInt("cid") %>" onclick="return confirm('삭제하시겠습니까?');">삭제</a></td>
        		</tr>
        		<% 
        	}
        } catch (Exception e) {
            e.printStackTrace(); }
        %>
            </table>
</body>
</html>