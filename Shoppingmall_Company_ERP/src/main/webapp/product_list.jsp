<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<<<<<<< HEAD
<%@ page import="java.sql.*" %>
<%@ page import="com.company1.DBManager" %>

<link rel="stylesheet" type="text/css" href="css/common.css">

<%
// 페이징 파라미터 처리
int pageSize = 10; // 페이지당 상품 수
int currentPage = 1; // 현재 페이지
String pageParam = request.getParameter("page");
if (pageParam != null && !pageParam.trim().isEmpty()) {
    try {
        currentPage = Integer.parseInt(pageParam);
        if (currentPage < 1) currentPage = 1;
    } catch (NumberFormatException e) {
        currentPage = 1;
    }
}

// 상품 목록 조회 (페이징 적용)
Connection conn = com.company1.DBManager.getDBConnection();

// 전체 상품 수 조회
PreparedStatement countStmt = conn.prepareStatement("SELECT COUNT(*) FROM products");
ResultSet countRs = countStmt.executeQuery();
int totalProducts = 0;
if (countRs.next()) {
    totalProducts = countRs.getInt(1);
}
countRs.close();
countStmt.close();

// 전체 페이지 수 계산
int totalPages = (int) Math.ceil((double) totalProducts / pageSize);
if (totalPages < 1) totalPages = 1;

// 현재 페이지 범위 조정
if (currentPage > totalPages) {
    currentPage = totalPages;
}

// 페이징된 상품 목록 조회
int offset = (currentPage - 1) * pageSize;
PreparedStatement pstmt = conn.prepareStatement(
    "SELECT pid, pname, price, stock FROM products ORDER BY pname ASC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY"
);
pstmt.setInt(1, offset);
pstmt.setInt(2, pageSize);
ResultSet rs = pstmt.executeQuery();

// 통계 계산
double totalValue = 0;
int lowStockCount = 0;
=======
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
>>>>>>> cddda14998e5a164e841ccb98ce4bf191064d936
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
<<<<<<< HEAD

=======
   
>>>>>>> cddda14998e5a164e841ccb98ce4bf191064d936
    <div class="container">
        <!-- 통계 섹션 -->
        <div class="stats product-stats">
            <div class="stat-item">
<<<<<<< HEAD
                <div class="stat-number">0</div>
                <div class="stat-label">전체 상품 수</div>
            </div>
            <div class="stat-item">
                <div class="stat-number">0</div>
                <div class="stat-label">재고 부족 상품</div>
            </div>
            <div class="stat-item">
                <div class="stat-number">₩0</div>
=======
                <div class="stat-number"><%= totalProducts %></div>
                <div class="stat-label">전체 상품 수</div>
            </div>
            <div class="stat-item">
                <div class="stat-number"><%= lowStockCount %></div>
                <div class="stat-label">재고 부족 상품</div>
            </div>
            <div class="stat-item">
                <div class="stat-number">₩<%= formatter.format(totalValue) %></div>
>>>>>>> cddda14998e5a164e841ccb98ce4bf191064d936
                <div class="stat-label">총 상품 가치</div>
            </div>
        </div>

        <!-- 상품 등록 폼 -->
        <div class="product-form">
            <h2>📦 신규 상품 등록</h2>
<<<<<<< HEAD
            <form action="ProductServlet" method="post">
                <input type="hidden" name="action" value="insert">
                <input type="hidden" name="returnPage" value="<%= currentPage %>">
=======
            <form action="${pageContext.request.contextPath}/product" method="post">
                <input type="hidden" name="action" value="insert">
>>>>>>> cddda14998e5a164e841ccb98ce4bf191064d936
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
<<<<<<< HEAD
=======
        
        <!-- 검색창 섹션 -->
        <div class="search-box">
            <form method="get" action="${pageContext.request.contextPath}/product">
                <input type="hidden" name="action" value="search">
                <input type="text" name="search" value="<%= searchValue %>" class="search-input" placeholder="상품명을 입력하세요">
                <button type="submit" class="search-button">검색</button>
                <a href="${pageContext.request.contextPath}/product?action=list" class="reset-button">초기화</a>
            </form>
        </div>
>>>>>>> cddda14998e5a164e841ccb98ce4bf191064d936

        <!-- 상품 목록 -->
        <div class="list-section">
            <h2>📦 상품 목록</h2>
            <table class="product-table">
                <thead>
                    <tr>
<<<<<<< HEAD
                        <th>상품 ID</th>
=======
                        <th>No.</th>
>>>>>>> cddda14998e5a164e841ccb98ce4bf191064d936
                        <th>상품명</th>
                        <th>가격</th>
                        <th>재고</th>
                        <th>관리</th>
                    </tr>
                </thead>
                <tbody>
                    <%
<<<<<<< HEAD
                    try {
                        int rowNumber = (currentPage - 1) * pageSize + 1; // 행 번호 계산
                        while (rs.next()) {
                            double price = rs.getDouble("price");
                            int stock = rs.getInt("stock");
                            totalValue += (price * stock);
                            
                            if (stock < 10) lowStockCount++;
                            
                            // CSS 클래스 결정
                            String stockClass = "";
                            if (stock < 10) {
                                stockClass = "low";
                            } else if (stock < 50) {
=======
                    if (products != null && !products.isEmpty()) {
                        int startNo = (currentPage - 1) * recordsPerPage + 1;
                        for (ProductDTO product : products) {
                            // 재고량에 따른 CSS 클래스 설정
                            String stockClass = "";
                            if (product.getStock() < 10) {
                                stockClass = "low";
                            } else if (product.getStock() < 50) {
>>>>>>> cddda14998e5a164e841ccb98ce4bf191064d936
                                stockClass = "medium";
                            } else {
                                stockClass = "high";
                            }
                    %>
                        <tr>
<<<<<<< HEAD
                            <td class="product-id"><strong>#<%= rowNumber %></strong></td>
                            <td class="product-name"><strong><%= rs.getString("pname") %></strong></td>
                            <td class="product-price">₩<%= String.format("%,d", (int)price) %></td>
                            <td class="product-stock <%= stockClass %>">
                                <%= stock %>
                            </td>
                            <td class="product-actions">
                                <a href="ProductServlet?action=edit&pid=<%= rs.getInt("pid") %>" class="btn-edit">✏️ 수정</a>
                                <a href="ProductServlet?action=delete&pid=<%= rs.getInt("pid") %>" class="btn-delete" onclick="return confirm('정말로 삭제하시겠습니까?');">🗑️ 삭제</a>
                            </td>
                        </tr>
                    <%
                            rowNumber++; // 행 번호 증가
                        }
                        
                        // 통계 업데이트를 위한 스크립트
                    %>
                        <script>
                            document.querySelector('.product-stats .stat-item:nth-child(1) .stat-number').textContent = '<%= totalProducts %>';
                            document.querySelector('.product-stats .stat-item:nth-child(2) .stat-number').textContent = '<%= lowStockCount %>';
                            document.querySelector('.product-stats .stat-item:nth-child(3) .stat-number').textContent = '₩<%= String.format("%,d", (int)totalValue) %>';
                        </script>
                    <%
                        
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (rs != null) rs.close();
                        if (pstmt != null) pstmt.close();
                        if (conn != null) conn.close();
=======
                            <td class="row-number"><%= startNo++ %></td>
                            <td class="product-name"><strong><%= product.getPname() %></strong></td>
                            <td class="product-price">₩<%= formatter.format(product.getPrice()) %></td>
                            <td class="product-stock <%= stockClass %>"><%= product.getStock() %>개</td>
                            <td class="product-actions">
                                <a href="${pageContext.request.contextPath}/product?action=edit&pid=<%= product.getPid() %>" class="btn-edit">✏️ 수정</a>
                                <a href="${pageContext.request.contextPath}/product?action=delete&pid=<%= product.getPid() %>" class="btn-delete" onclick="return confirm('정말로 삭제하시겠습니까?');">🗑️ 삭제</a>
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
>>>>>>> cddda14998e5a164e841ccb98ce4bf191064d936
                    }
                    %>
                </tbody>
            </table>
            
<<<<<<< HEAD
            <%
            if (totalProducts == 0) {
            %>
                <div class="no-data">📭 등록된 상품이 없습니다. 첫 번째 상품을 등록해보세요!</div>
            <%
            } else {
                // 페이징 네비게이션 표시
            %>
                <div class="pagination">
                    <%
                    // 이전 페이지 버튼
                    if (currentPage > 1) {
                    %>
                        <a href="?page=<%= currentPage - 1 %>" class="page-btn prev">◀ 이전</a>
                    <%
                    }
                    
                    // 페이지 번호들
                    int startPage = Math.max(1, currentPage - 2);
                    int endPage = Math.min(totalPages, currentPage + 2);
                    
                    if (startPage > 1) {
                    %>
                        <a href="?page=1" class="page-btn">1</a>
                        <% if (startPage > 2) { %>
                            <span class="page-ellipsis">...</span>
                        <% } %>
                    <%
                    }
                    
                    for (int i = startPage; i <= endPage; i++) {
                        if (i == currentPage) {
                    %>
                            <span class="page-btn current"><%= i %></span>
                    <%
                        } else {
                    %>
                            <a href="?page=<%= i %>" class="page-btn"><%= i %></a>
                    <%
                        }
                    }
                    
                    if (endPage < totalPages) {
                        if (endPage < totalPages - 1) {
                    %>
                            <span class="page-ellipsis">...</span>
                    <%
                        }
                    %>
                        <a href="?page=<%= totalPages %>" class="page-btn"><%= totalPages %></a>
                    <%
                    }
                    
                    // 다음 페이지 버튼
                    if (currentPage < totalPages) {
                    %>
                        <a href="?page=<%= currentPage + 1 %>" class="page-btn next">다음 ▶</a>
                    <%
                    }
                    %>
                </div>
                
                <div class="page-info">
                    <span>총 <%= totalProducts %>개 상품 중 <%= (currentPage - 1) * pageSize + 1 %>-<%= Math.min(currentPage * pageSize, totalProducts) %>번째 상품</span>
                    <span>페이지 <%= currentPage %> / <%= totalPages %></span>
                </div>
            <%
            }
            %>
        </div>
    </div>
 
</body>
</html>
=======
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
</body>
</html>
>>>>>>> cddda14998e5a164e841ccb98ce4bf191064d936
