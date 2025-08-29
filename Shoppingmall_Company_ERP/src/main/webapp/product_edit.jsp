<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.company1.dto.ProductDTO" %>
<%
    ProductDTO p = (ProductDTO) request.getAttribute("product");
    if (p == null) {
        response.sendRedirect(request.getContextPath() + "/product/list");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>상품 수정</title>
<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/css/main.css">
</head>
<body>
<%@ include file="common-jsp/header.jsp" %>

<div class="container">
  <h2>상품 수정</h2>
  <form action="<%= request.getContextPath() %>/product" method="post">
    <input type="hidden" name="action" value="update">
    <input type="hidden" name="pid" value="<%= p.getPid() %>">

    <div class="form-group">
      <label for="pname">상품명</label>
      <input id="pname" name="pname" value="<%= p.getPname() %>" required>
    </div>

    <div class="form-group">
      <label for="price">가격</label>
      <input id="price" name="price" type="number" min="0" step="1" value="<%= (int)p.getPrice() %>" required>
    </div>

    <div class="form-group">
      <label for="stock">재고</label>
      <input id="stock" name="stock" type="number" min="0" value="<%= p.getStock() %>" required>
    </div>

    <button type="submit" class="btn">수정하기</button>
    <a href="<%= request.getContextPath() %>/product/list" class="btn btn-secondary">취소</a>
  </form>
</div>
</body>
</html>
