<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%-- header.jsp : 상단 네비게이션 공통 헤더 --%>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">

<<<<<<< HEAD
    <header>
        <nav>
            <ul class="main-menu">
                <li><a href="index.jsp">메인 페이지</a></li>
                <li>
                    <a href="#">사용자 관리</a>
                    <ul class="dropdown-menu">
                        <li><a href="employee_form.jsp">직원 등록</a></li>
                        <li><a href="EmployeeServlet?command=list">직원 관리</a></li>
                    </ul>        
                </li>
                
                <li>
                    <a href="#">상품 관리</a>
                    <ul class="dropdown-menu">
                        <li><a href="product_list.jsp">상품 목록</a></li>
                        <li><a href="product_add.jsp">상품 추가</a></li>
                    </ul>
                </li>
                <li><a href="OrderServlet?command=list">주문 관리</a></li>
                 <li><a href="CustomerServlet?command=list">고객 관리</a></li>
                  <li><a href="groupware.jsp">그룹웨어</a></li>
            </ul>

			<%-- Session을 사용해서 로그인 상태 확인하기! ( 한울 추가 )--%>
    <div class="user-info">
        <%
            // "userId"라는 이름으로 세션에 저장된 값이 있는지 확인합니다.
            if (session.getAttribute("loginUser") == null) {
                // 세션에 userId가 없으면(로그인되지 않은 상태) 로그인 버튼을 표시합니다.
        %>
                <a href="login.jsp">로그인</a>
        <%
            } else {
                // 세션에 userId가 있으면(로그인된 상태) 로그아웃 버튼을 표시합니다.
        %>
                <a href="LogoutServlet">로그아웃</a>
=======
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
>>>>>>> cb00c5fcb904cfc4347a707877c00f9821a0116c
        <%
            }
        %>
    </div>
<<<<<<< HEAD
        </nav>
    </header>
</body>
</html>
=======
</div>
>>>>>>> cb00c5fcb904cfc4347a707877c00f9821a0116c
