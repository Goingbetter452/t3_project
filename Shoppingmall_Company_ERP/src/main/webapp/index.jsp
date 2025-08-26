<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>B2B Company ERP 시스템</title>
<link rel="stylesheet" type="text/css" href="css/common.css">
<link rel="stylesheet" type="text/css" href="css/main.css">
</head>
<body>

<!-- 헤더 포함 -->
  <!-- 헤더 섹션 -->
    <div class="header">
        <h1>🏢 Shoppingmall Company ERP</h1>
        <div class="header-nav">
            <a href="${pageContext.request.contextPath}/">🏠 대시보드</a>
            <a href="${pageContext.request.contextPath}/CustomerServlet?command=list">👥 고객 관리</a>
            <a href="${pageContext.request.contextPath}/EmployeeServlet?action=list">👨‍💼 직원 관리</a>
            <a href="${pageContext.request.contextPath}/ProductServlet?action=list">📦 상품 관리</a>
            <a href="${pageContext.request.contextPath}/OrderServlet?action=list">🛒 주문 관리</a>
<<<<<<< HEAD
            <a href="${pageContext.request.contextPath}/LoginServlet?action=list">🛒 로그인</a>
=======
>>>>>>> cb00c5fcb904cfc4347a707877c00f9821a0116c
        </div>
    </div>

<div class="container">
    <div class="hero-section">
        <h1>B2B 회사 ERP 시스템</h1>
        <p>효율적인 상품 관리와 주문 처리를 위한 통합 시스템</p>
        <a href="product_main.jsp" class="btn btn-large btn-success">시작하기</a>
    </div>
    
    <div class="features">
        <div class="feature-card">
            <h3>📦 상품 관리</h3>
            <p>상품 등록, 수정, 삭제 및 재고 관리</p>
            <a href="product_list.jsp" class="btn">상품 목록 보기</a>
        </div>
        
        <div class="feature-card">
            <h3>📋 주문 관리</h3>
            <p>고객 주문 처리 및 배송 상태 관리</p>
            <a href="order_list.jsp" class="btn">주문 목록 보기</a>
        </div>
        
        <div class="feature-card">
            <h3>👥 사용자 관리</h3>
            <p>고객 정보 및 관리자 계정 관리</p>
<<<<<<< HEAD
            <a href="employeeServlet?action=list" class="btn">사용자 관리</a>
=======
            <a href="employee_list.jsp" class="btn">사용자 관리</a>
>>>>>>> cb00c5fcb904cfc4347a707877c00f9821a0116c
        </div>
        
        <div class="feature-card">
            <h3>📊 통계 및 분석</h3>
            <p>매출 분석 및 상품 판매 통계</p>
            <a href="#" class="btn">통계 보기</a>
        </div>
        
        <div class="feature-card">
            <h3> 그룹 웨어 </h3>
            <p>직원 공지 게시판</p>
            <a href="groupware.jsp" class="btn">공지 보기</a>
        </div>
    </div>
    
    <div style="text-align: center; margin-top: 40px;">
        <h2>빠른 시작</h2>
        <p>새로운 상품을 등록하거나 기존 상품을 관리해보세요.</p>
        <a href="product_add.jsp" class="btn btn-large">상품 추가</a>
        <a href="product_list.jsp" class="btn btn-large">상품 목록</a>
    </div>
    
    
</div>
</body>
</html>