<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.company1.dto.ProductDTO" %>
<%
    ProductDTO product = (ProductDTO) request.getAttribute("product");
    if (product == null) {
        out.println("<h3>상품 정보를 찾을 수 없습니다.</h3>");
        return;
    }
    String ctx = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>상품 수정</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/product.css">
</head>
<body>
    <%@ include file="common-jsp/header.jsp" %>
    
    <div class="container">
        <div class="product-form">
            <h2>🛠️ 상품 정보 수정</h2>
            <form action="<%= ctx %>/product" method="post">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="pid" value="<%= product.getPid() %>">
                
                <div class="product-form-row">
                    <div class="form-group">
                        <label for="pname">상품명:</label>
                        <input type="text" id="pname" name="pname" value="<%= product.getPname() %>" required placeholder="상품명을 입력하세요">
                    </div>
                    
                    <div class="form-group">
                        <label for="price">가격:</label>
                        <input type="number" id="price" name="price" value="<%= (int)product.getPrice() %>" min="0" step="100" required placeholder="가격을 입력하세요 (원)">
                    </div>
                    
                    <div class="form-group">
                        <label for="stock">재고:</label>
                        <input type="number" id="stock" name="stock" value="<%= product.getStock() %>" min="0" required placeholder="재고 수량을 입력하세요">
                    </div>
                    
                    <div class="form-actions">
                        <input type="submit" value="✅ 수정 완료" class="btn btn-primary">
                        <a href="<%= ctx %>/product?action=list" class="btn btn-secondary">❌ 취소</a>
                    </div>
                </div>
            </form>
        </div>
    </div>
</body>
</html>