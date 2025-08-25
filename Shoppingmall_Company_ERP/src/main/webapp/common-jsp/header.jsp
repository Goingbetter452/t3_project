<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%-- ↑ JSP가 UTF-8로 읽히고, 응답도 UTF-8로 나가도록 지정 --%>

<meta charset="UTF-8">
<link rel="stylesheet" type="text/css" href="css/common.css">

<div class="header">
    <h1>🏢 B2B Company ERP</h1>
    <div class="header-nav">
        <a href="index.jsp">🏠 메인 페이지</a>
        
        <div class="dropdown-container">
            <a href="#">👨‍💼 사용자 관리</a>
            <div class="dropdown-menu">
                <a href="employee_form.jsp">직원 등록</a>
                <a href="EmployeeServlet?command=list">직원 관리</a>
            </div>
        </div>
        
        <div class="dropdown-container">
            <a href="#">📦 상품 관리</a>
            <div class="dropdown-menu">
                <a href="product_list.jsp">상품 목록</a>
                <a href="product_add.jsp">상품 추가</a>
            </div>
        </div>
        
        <a href="OrderServlet?command=list">🛒 주문 관리</a>
        <a href="CustomerServlet?command=list">👥 고객 관리</a>
        <a href="groupware.jsp">🏢 그룹웨어</a>
    </div>
    
    <div class="user-info">
        <%-- Session을 사용해서 로그인 상태 확인하기! ( 한울 추가 )--%>
        <%
            // "userId"라는 이름으로 세션에 저장된 값이 있는지 확인합니다.
            if (session.getAttribute("userId") == null) {
                // 세션에 userId가 없으면(로그인되지 않은 상태) 로그인 버튼을 표시합니다.
        %>
                <a href="login.jsp">로그인</a>
        <%
            } else {
                // 세션에 userId가 있으면(로그인된 상태) 로그아웃 버튼을 표시합니다.
        %>
                <a href="LogoutServlet">로그아웃</a>
        <%
            }
        %>
    </div>
</div>
