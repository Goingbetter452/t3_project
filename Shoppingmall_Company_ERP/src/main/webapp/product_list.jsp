<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    // JSP 직접 접근 시 서블릿으로 리다이렉트 (리다이렉션 루프 방지)
    if (request.getAttribute("products") == null && 
        "GET".equalsIgnoreCase(request.getMethod())) {
        response.sendRedirect(request.getContextPath() + "/product?action=list");
        return;
    }
%>
<%@ page import="java.util.List, com.company1.dto.ProductDTO, java.text.NumberFormat" %>

<%
    // 서블릿에서 전달된 상품 목록과 검색어를 가져옵니다.
    List<ProductDTO> products = (List<ProductDTO>) request.getAttribute("products");
    String searchValue = (String) request.getAttribute("search");
    if (searchValue == null) {
        searchValue = "";
    }

    // 페이징 관련 변수 초기화
    Integer currentPageObj = (Integer) request.getAttribute("currentPage");
    int currentPage = (currentPageObj != null) ? currentPageObj : 1;
    
    Integer recordsPerPageObj = (Integer) request.getAttribute("recordsPerPage");
    int recordsPerPage = (recordsPerPageObj != null) ? recordsPerPageObj : 10;
    
    Integer noOfRecordsObj = (Integer) request.getAttribute("noOfRecords");
    int noOfRecords = (noOfRecordsObj != null) ? noOfRecordsObj : 0;
    
    Integer noOfPagesObj = (Integer) request.getAttribute("noOfPages");
    int noOfPages = (noOfPagesObj != null) ? noOfPagesObj : 1;

    // 통계 계산을 위한 변수 초기화
    int totalProducts = 0;
    double totalValue = 0;
    int lowStockCount = 0;

    if (products != null) {
        totalProducts = noOfRecords;  // 전체 레코드 수로 변경
        for (ProductDTO product : products) {
            totalValue += (product.getPrice() * product.getStock());
            if (product.getStock() < 10) {
                lowStockCount++;
            }
        }
    }
    // 숫자 포맷팅을 위한 NumberFormat 객체
    NumberFormat formatter = NumberFormat.getNumberInstance();
%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>상품 관리 시스템</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/product.css">
</head>
<body>
   <%@ include file="common-jsp/header.jsp" %>
   
    <div class="container">
        <!-- 통계 섹션 -->
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

        <!-- 상품 등록 폼 -->
        <div class="product-form">
            <h2>📦 신규 상품 등록</h2>
            <form action="${pageContext.request.contextPath}/product" method="post">
                <input type="hidden" name="action" value="insert">
                <div class="product-form-row">
                    <div class="form-group">
                        <label for="pname">상품명:</label>
                        <input type="text" id="pname" name="pname" required placeholder="상품명을 입력하세요">
                    </div>
                    <div class="form-group">
                        <label for="price">가격:</label>
                        <input type="number" id="price" name="price" min="0" step="100" required placeholder="가격을 입력하세요 (원)">
                    </div>
                    <div class="form-group">
                        <label for="stock">재고:</label>
                        <input type="number" id="stock" name="stock" min="0" required placeholder="재고 수량을 입력하세요">
                    </div>
                    <div class="form-actions">
                        <input type="submit" value="✅ 상품 등록">
                    </div>
                </div>
            </form>
        </div>
        
        <!-- 검색창 섹션 -->
        <div class="search-box">
            <form method="get" action="${pageContext.request.contextPath}/product">
                <input type="hidden" name="action" value="search">
                <input type="text" name="search" value="<%= searchValue %>" class="search-input" placeholder="상품명을 입력하세요">
                <button type="submit" class="search-button">검색</button>
                <a href="${pageContext.request.contextPath}/product?action=list" class="reset-button">초기화</a>
            </form>
        </div>

        <!-- 상품 목록 -->
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
                            // 재고량에 따른 CSS 클래스 설정
                            String stockClass = "";
                            if (product.getStock() < 10) {
                                stockClass = "low";
                            } else if (product.getStock() < 50) {
                                stockClass = "medium";
                            } else {
                                stockClass = "high";
                            }
                    %>
                        <tr>
                            <td class="row-number"><%= startNo++ %></td>
                            <td class="product-name"><strong><%= product.getPname() %></strong></td>
                            <td class="product-price">₩<%= formatter.format(product.getPrice()) %></td>
                            <td class="product-stock <%= stockClass %>"><%= product.getStock() %>개</td>
                            <td class="product-actions">
                                <a href="${pageContext.request.contextPath}/product?action=edit&pid=<%= product.getPid() %>" class="btn-edit">✏️ 수정</a>
                                <a href="${pageContext.request.contextPath}/product?action=delete&pid=<%= product.getPid() %>" class="btn-delete" onclick="return confirm('정말로 삭제하시겠습니까?');">🗑️ 삭제</a>
                                <button class="btn-view" onclick="openProductDetails(<%= product.getPid() %>)">🔍 상세보기</button>
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
            
            <!-- 페이징 UI 추가 -->
            <%
                // 페이지 그룹 계산
                int pageGroup = 5; // 한 그룹당 페이지 수
                int startPage = ((currentPage - 1) / pageGroup) * pageGroup + 1;
                int endPage = Math.min(startPage + pageGroup - 1, noOfPages);
                
                if (noOfPages > 0) {
            %>
            <div class="pagination">
                <!-- 이전 페이지 그룹으로 이동 -->
                <% if (startPage > pageGroup) { %>
                    <a href="${pageContext.request.contextPath}/product?action=<%= searchValue.isEmpty() ? "list" : "search" %>&page=<%= startPage - pageGroup %><%= searchValue.isEmpty() ? "" : "&search=" + searchValue %>" class="page-link">&laquo;</a>
                <% } %>
                
                <!-- 페이지 번호 -->
                <% for (int i = startPage; i <= endPage; i++) { %>
                    <a href="${pageContext.request.contextPath}/product?action=<%= searchValue.isEmpty() ? "list" : "search" %>&page=<%= i %><%= searchValue.isEmpty() ? "" : "&search=" + searchValue %>" 
                       class="page-link <%= (i == currentPage) ? "active" : "" %>">
                        <%= i %>
                    </a>
                <% } %>
                
                <!-- 다음 페이지 그룹으로 이동 -->
                <% if (endPage < noOfPages) { %>
                    <a href="${pageContext.request.contextPath}/product?action=<%= searchValue.isEmpty() ? "list" : "search" %>&page=<%= startPage + pageGroup %><%= searchValue.isEmpty() ? "" : "&search=" + searchValue %>" class="page-link">&raquo;</a>
                <% } %>
            </div>
            
            <!-- 페이지 정보 표시 -->
            <div class="page-info">
                전체 <%= noOfRecords %>개 항목 중 <%= (currentPage-1)*recordsPerPage + 1 %> - <%= Math.min(currentPage*recordsPerPage, noOfRecords) %>
            </div>
            <% } %>
        </div>
    </div>

    <!-- 상품 상세 정보 모달 -->
    <div id="productDetailModal" class="modal" style="display:none;">
        <div class="modal-content">
            <span class="close" onclick="closeProductDetails()">&times;</span>
            <h2>상품 상세 정보</h2>
            <p><strong>상품명:</strong> <span id="modalProductName"></span></p>
            <p><strong>가격:</strong> <span id="modalProductPrice"></span></p>
            <p><strong>재고:</strong> <span id="modalProductStock"></span></p>
        </div>
    </div>

    <script>
    function openProductDetails(pid) {
        console.log('[Debug] openProductDetails called with pid:', pid); // 디버깅용
        if (!pid || pid === '') {
            alert('상품 ID가 없습니다.');
            return;
        }
        
        const contextPath = '<%= request.getContextPath() %>';
        const url = contextPath + '/product_detail.jsp?pid=' + encodeURIComponent(pid);
        console.log('[Debug] Opening popup with URL:', url); // 디버깅용
        
        const popup = window.open(url, 'ProductDetails', 
            'width=600,height=500,scrollbars=yes,resizable=yes,location=no,menubar=no,toolbar=no');
        if (popup) {
            popup.focus();
        } else {
            alert('팝업이 차단되었습니다. 팝업 차단을 해제해주세요.');
        }
    }

    function closeProductDetails() {
        document.getElementById('productDetailModal').style.display = 'none';
    }
    </script>

    <%-- Debugging log to verify product IDs --%>
    <%
        for (ProductDTO product : products) {
            System.out.println("[Debug] Product ID: " + product.getPid());
        }
    %>
</body>
</html>