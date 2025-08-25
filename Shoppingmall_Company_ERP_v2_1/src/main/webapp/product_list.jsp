<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="com.company1.DBManager" %>

<%@ include file="common-jsp/header.jsp" %>
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
   

    <div class="container">
        <!-- 통계 섹션 -->
        <div class="stats product-stats">
            <div class="stat-item">
                <div class="stat-number">0</div>
                <div class="stat-label">전체 상품 수</div>
            </div>
            <div class="stat-item">
                <div class="stat-number">0</div>
                <div class="stat-label">재고 부족 상품</div>
            </div>
            <div class="stat-item">
                <div class="stat-number">₩0</div>
                <div class="stat-label">총 상품 가치</div>
            </div>
        </div>

        <!-- 상품 등록 폼 -->
        <div class="form-section product-form">
            <h2>📦 신규 상품 등록</h2>
            <form action="ProductServlet" method="post">
                <input type="hidden" name="action" value="insert">
                <input type="hidden" name="returnPage" value="<%= currentPage %>">
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

        <!-- 상품 목록 -->
        <div class="list-section">
            <h2>📦 상품 목록</h2>
            <table class="product-table">
                <thead>
                    <tr>
                        <th>상품 ID</th>
                        <th>상품명</th>
                        <th>가격</th>
                        <th>재고</th>
                        <th>관리</th>
                    </tr>
                </thead>
                <tbody>
                    <%
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
                                stockClass = "medium";
                            } else {
                                stockClass = "high";
                            }
                    %>
                        <tr>
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
                    }
                    %>
                </tbody>
            </table>
            
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
