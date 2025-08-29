<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    // JSP 직접 접근 시 서블릿으로 리다이렉트 (리다이렉션 루프 방지)
    if (request.getAttribute("products") == null && "GET".equalsIgnoreCase(request.getMethod())) {
        response.sendRedirect(request.getContextPath() + "/product?action=list");
        return;
    }
%>
<%@ page import="java.util.List, com.company1.dto.ProductDTO, java.text.NumberFormat" %>

<%
    // 서블릿에서 전달된 값
    List<ProductDTO> products = (List<ProductDTO>) request.getAttribute("products");
    String searchValue = (String) request.getAttribute("search");
    if (searchValue == null) searchValue = "";

    Integer currentPageObj = (Integer) request.getAttribute("currentPage");
    int currentPage = (currentPageObj != null) ? currentPageObj : 1;

    Integer recordsPerPageObj = (Integer) request.getAttribute("recordsPerPage");
    int recordsPerPage = (recordsPerPageObj != null) ? recordsPerPageObj : 10;

    Integer noOfRecordsObj = (Integer) request.getAttribute("noOfRecords");
    int noOfRecords = (noOfRecordsObj != null) ? noOfRecordsObj : 0;

    Integer noOfPagesObj = (Integer) request.getAttribute("noOfPages");
    int noOfPages = (noOfPagesObj != null) ? noOfPagesObj : 1;

    // 통계 계산
    int totalProducts = 0;
    double totalValue = 0;
    int lowStockCount = 0;
    if (products != null) {
        totalProducts = noOfRecords;
        for (ProductDTO product : products) {
            totalValue += (product.getPrice() * product.getStock());
            if (product.getStock() < 10) lowStockCount++;
        }
    }

    NumberFormat formatter = NumberFormat.getNumberInstance();
    String ctx = request.getContextPath();
%>

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>상품 관리 시스템</title>
<link rel="stylesheet" href="<%= ctx %>/css/common.css">
<link rel="stylesheet" href="<%= ctx %>/css/product.css">
</head>
<body>
    <%@ include file="common-jsp/header.jsp" %>

    <div class="container">
        <!-- 통계 -->
        <div class="stats product-stats">
            <div class="stat-item">
                <div class="stat-number"><%= totalProducts %></div>
                <div class="stat-label">전체 상품 수</div>
            </div>
            <div class="stat-item">
                <div class="stat-number"><%= lowStockCount %></div>
                <div class="stat-label">재고 부족 상품</div>
            </div>
            <div class="stat-item">
                <div class="stat-number">₩<%= formatter.format(totalValue) %></div>
                <div class="stat-label">총 상품 가치</div>
            </div>
        </div>

        <!-- 등록 폼 -->
        <div class="product-form">
            <h2>📦 신규 상품 등록</h2>
            <form action="<%= ctx %>/product" method="post">
                <input type="hidden" name="action" value="insert">
                <div class="product-form-row">
                    <div class="form-group">
                        <label for="pname">상품명:</label>
                        <input type="text" id="pname" name="pname" required placeholder="상품명을 입력하세요">
                    </div>
                    <div class="form-group">
                        <label for="price">가격:</label>
                        <input type="number" id="price" name="price" min="0" step="100" required placeholder="가격(원)">
                    </div>
                    <div class="form-group">
                        <label for="stock">재고:</label>
                        <input type="number" id="stock" name="stock" min="0" required placeholder="재고 수량">
                    </div>
                    <div class="form-actions">
                        <input type="submit" value="✅ 상품 등록">
                    </div>
                </div>
            </form>
        </div>

        <!-- 검색 -->
        <div class="search-box">
            <form method="get" action="<%= ctx %>/product">
                <input type="hidden" name="action" value="search">
                <input type="text" name="search" value="<%= searchValue %>" class="search-input" placeholder="상품명을 입력하세요">
                <button type="submit" class="search-button">검색</button>
                <a href="<%= ctx %>/product?action=list" class="reset-button">초기화</a>
            </form>
        </div>

        <!-- 목록 -->
        <div class="list-section">
            <h2>📦 상품 목록</h2>
            <table class="product-table">
                <thead>
                    <tr>
                        <th>No.</th>
                        <th>상품명</th>
                        <th>가격</th>
                        <th>재고</th>
                        <th>관리</th>
                    </tr>
                </thead>
                <tbody>
                <%
                    if (products != null && !products.isEmpty()) {
                        int startNo = (currentPage - 1) * recordsPerPage + 1;
                        for (ProductDTO product : products) {
                            String stockClass = product.getStock() < 10 ? "low" :
                                                (product.getStock() < 50 ? "medium" : "high");
                %>
                    <tr>
                        <td class="row-number"><%= startNo++ %></td>
                        <td class="product-name"><strong><%= product.getPname() %></strong></td>
                        <td class="product-price">₩<%= formatter.format(product.getPrice()) %></td>
                        <td class="product-stock <%= stockClass %>"><%= product.getStock() %>개</td>
                        <td class="product-actions">
                            <a href="<%= ctx %>/product?action=edit&pid=<%= product.getPid() %>" class="btn-edit">✏️ 수정</a>
                            <a href="<%= ctx %>/product?action=delete&pid=<%= product.getPid() %>" class="btn-delete" onclick="return confirm('정말로 삭제하시겠습니까?');">🗑️ 삭제</a>
                            <!-- 방법 B: 팝업 오픈 -->
                            <a href="javascript:void(0)" onclick="openProductDetails(<%= product.getPid() %>)">🔎 상세</a>
                        </td>
                    </tr>
                <%
                        }
                    } else {
                %>
                    <tr>
                        <td colspan="5" class="no-data">📭 검색 결과가 없거나 등록된 상품이 없습니다.</td>
                    </tr>
                <%
                    }
                %>
                </tbody>
            </table>

            <%
                int pageGroup = 5;
                int startPage = ((currentPage - 1) / pageGroup) * pageGroup + 1;
                int endPage = Math.min(startPage + pageGroup - 1, noOfPages);
                if (noOfPages > 0) {
            %>
            <div class="pagination">
                <% if (startPage > pageGroup) { %>
                <a class="page-link"
                   href="<%= ctx %>/product?action=<%= searchValue.isEmpty() ? "list" : "search" %>&page=<%= startPage - pageGroup %><%= searchValue.isEmpty() ? "" : "&search=" + java.net.URLEncoder.encode(searchValue, "UTF-8") %>">&laquo;</a>
                <% } %>

                <% for (int i = startPage; i <= endPage; i++) { %>
                <a class="page-link <%= (i == currentPage) ? "active" : "" %>"
                   href="<%= ctx %>/product?action=<%= searchValue.isEmpty() ? "list" : "search" %>&page=<%= i %><%= searchValue.isEmpty() ? "" : "&search=" + java.net.URLEncoder.encode(searchValue, "UTF-8") %>"><%= i %></a>
                <% } %>

                <% if (endPage < noOfPages) { %>
                <a class="page-link"
                   href="<%= ctx %>/product?action=<%= searchValue.isEmpty() ? "list" : "search" %>&page=<%= startPage + pageGroup %><%= searchValue.isEmpty() ? "" : "&search=" + java.net.URLEncoder.encode(searchValue, "UTF-8") %>">&raquo;</a>
                <% } %>
            </div>

            <div class="page-info">
                전체 <%= noOfRecords %>개 항목 중
                <%= (currentPage-1)*recordsPerPage + 1 %> -
                <%= Math.min(currentPage*recordsPerPage, noOfRecords) %>
            </div>
            <% } %>
        </div>
    </div>

    <!-- 팝업 스크립트 (EL 없이) -->
    <script>
      var ctx = '<%= ctx %>';
      function openProductDetails(pid) {
        if (!pid || isNaN(pid)) { alert('유효한 상품 ID가 없습니다.'); return; }
        var url = ctx + '/product_detail.jsp?pid=' + encodeURIComponent(pid);
        var popup = window.open(url, 'ProductDetails', 'width=600,height=400,scrollbars=yes,resizable=yes');
        if (popup) popup.focus(); else alert('팝업이 차단되었습니다. 팝업 차단을 해제해주세요.');
      }
    </script>

    <% // 디버깅 로그
       if (products != null) for (ProductDTO product : products) {
           System.out.println("[Debug] Product ID: " + product.getPid());
       }
    %>
</body>
</html>
