<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <link rel="stylesheet" type="text/css" href="css/header.css">

<!DOCTYPE html>
<html>

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

        </nav>
            <div class="user-info">
                <%-- Session을 사용해서 로그인 상태 확인하기! ( 한울 추가 )--%>
                <%
                    // "userId"라는 이름으로 세션에 저장된 값이 있는지 확인합니다.
                    if (session.getAttribute("loginUser") == null) {
                        // 세션에 loginUser가 없으면(로그인되지 않은 상태) 로그인 버튼을 표시합니다.
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

    </header>


</body>
</html>