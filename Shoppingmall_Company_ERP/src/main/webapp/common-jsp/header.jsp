<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Header</title>
<link rel="stylesheet" type="text/css" href="css/main.css">
</head>
<body>

    <header>
        <nav>
            <ul class="main-menu">
                <li><a href="index.jsp">메인 페이지</a></li>
                <li><a href="#">사용자 관리</a></li>
                <li>
                    <a href="#">상품 관리</a>
                    <ul class="dropdown-menu">
                        <li><a href="product_list.jsp">상품 목록</a></li>
                        <li><a href="product_add.jsp">상품 추가</a></li>
                    </ul>
                </li>
                <li><a href="order_list.jsp">주문 관리</a></li>
                <li><a href="#">그룹웨어</a></li>
            </ul>
            
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
        </nav>
        
    </header>

</body>
</html>