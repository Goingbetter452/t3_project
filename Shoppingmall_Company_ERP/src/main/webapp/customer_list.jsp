<%@ page import="java.util.*, com.company1.dto.CustomerDTO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%
List<CustomerDTO> customerList = (List<CustomerDTO>) request.getAttribute("customerList");
%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>고객 관리</title>
    <link rel="stylesheet" href="css/customer.css">
</head>
<body>
<div class="container">
    <header>
        <h1>고객 관리</h1>
    </header>

    <a href="index.jsp" class="btn btn-primary">대시보드</a>

    <h3>고객 등록</h3>
    <form action="CustomerServlet" method="post">
        <input type="hidden" name="action" value="insert" />
        이름 : <input type="text" name="cname" required />
        이메일 : <input type="email" name="email" />
        <button type="submit" class="btn btn-primary">등록</button>
    </form>

    <h3>고객 목록</h3>
    <div class="table-container">
        <table>
            <thead>
                <tr>
                    <th>순번</th>
                    <th>이름</th>
                    <th>이메일</th>
                    <th>수정</th>
                    <th>삭제</th>
                </tr>
            </thead>
            <tbody>
            <%
                if (customerList != null && !customerList.isEmpty()) {
                    for (CustomerDTO c : customerList) {
            %>
                <tr>
                    <td><%= c.getCid() %></td>
                    <td><%= c.getCname() %></td>
                    <td><%= c.getEmail() %></td>
                    <td><a href="CustomerServlet?action=edit&cid=<%= c.getCid() %>" class="btn btn-primary">수정</a></td>
                    <td><a href="CustomerServlet?action=delete&cid=<%= c.getCid() %>" class="btn btn-primary" onclick="return confirm('삭제하시겠습니까?');">삭제</a></td>
                </tr>
            <%
                    }
                } else {
            %>
                <tr>
                    <td colspan="5" class="no-data">등록된 고객이 없습니다.</td>
                </tr>
            <%
                }
            %>
            </tbody>
        </table>
    </div>

    <footer>
        &copy; 2025 고객 관리 시스템
    </footer>
</div>
</body>
</html>