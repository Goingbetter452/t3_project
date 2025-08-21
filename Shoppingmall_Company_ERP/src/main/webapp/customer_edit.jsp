
<%@ page import="java.sql.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
ResultSet rs = (ResultSet) request.getAttribute("customer");
rs.next();
%>
<html>
<head><title>고객 수정</title></head>
<body>
<h2>고객 수정</h2>
<form action="CustomerServlet" method="post">
<input type="hidden" name="action" value="update" />
<input type="hidden" name="cid" value="<%= rs.getInt("cid") %>" />
이름: <input type="text" name="cname" value="<%= rs.getString("cname") %>" required /><br>
이메일: <input type="email" name="email" value="<%= rs.getString("email") %>" /><br>
<input type="submit" value="수정 완료" />
</form>
<a href="CustomerServlet?action=list">고객 목록으로</a>
</body>
</html>