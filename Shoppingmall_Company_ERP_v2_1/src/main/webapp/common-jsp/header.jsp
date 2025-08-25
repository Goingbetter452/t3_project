<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%-- header.jsp : 상단 네비게이션 공통 헤더 --%>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">

<div class="header">
    <h1>🏢 B2B Company ERP</h1>

    <nav class="header-nav">
        <a href="${pageContext.request.contextPath}/index.jsp">🏠 메인 페이지</a>

        <div class="dropdown-container">
            <a href="#">👨‍💼 사용자 관리</a>
            <div class="dropdown-menu">
                <a href="${pageContext.request.contextPath}/employee_form.jsp">직원 등록</a>
                <a href="${pageContext.request.contextPath}/EmployeeServlet?command=list">직원 관리</a>
            </div>
        </div>

        <div class="dropdown-container">
            <a href="#">📦 상품 관리</a>
            <div class="dropdown-menu">
                <a href="${pageContext.request.contextPath}/product_list.jsp">상품 목록</a>
                <a href="${pageContext.request.contextPath}/product_add.jsp">상품 추가</a>
            </div>
        </div>

        <a href="${pageContext.request.contextPath}/OrderServlet?command=list">🧾 주문 관리</a>
        <a href="${pageContext.request.contextPath}/CustomerServlet?command=list">🧑‍🤝‍🧑 고객 관리</a>
        <a href="${pageContext.request.contextPath}/groupware.jsp">🏢 그룹웨어</a>
    </nav>

    <div class="user-info">
        <%-- 세션으로 로그인 상태 표시 --%>
        <%
            Object uid = session.getAttribute("userId");
            if (uid == null) {
        %>
            <a href="${pageContext.request.contextPath}/login.jsp">로그인</a>
        <%
            } else {
        %>
            <a href="${pageContext.request.contextPath}/LogoutServlet">로그아웃</a>
        <%
            }
        %>
    </div>
</div>
