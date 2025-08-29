<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>

<%
com.company1.dto.CustomerDTO customer =
    (com.company1.dto.CustomerDTO) request.getAttribute("customer");

if (customer == null) {
    out.println("<h3>고객 정보를 찾을 수 없습니다.</h3>");
    return;
}
String ctx = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>고객 수정</title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/common.css">

</head>
<body>
<%@ include file="common-jsp/header.jsp" %>
<div class="list-section" style="width:50%; margin:20px auto; ">
<h2>고객 수정</h2>

<form action="<%= ctx %>/CustomerServlet" method="post">
 <div class="form-group">
 <input type="hidden" name="command" value="update">
  <input type="hidden" name="cid" value="<%= customer.getCid() %>">

  이름:  <input type="text"  name="cname" value="<%= customer.getCname() %>" required><br>
  이메일:<input type="email" name="email" value="<%= customer.getEmail() %>"><br>
	
  <input type="submit" value="수정 완료">
 </div>
  
</form>
<a href="<%= ctx %>/CustomerServlet?command=list">고객 목록으로</a>
</div>

</body>
</html>
