<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="com.company1.DBManager" %>

<%
// 상품 목록 조회
Connection conn = com.company1.DBManager.getDBConnection();
PreparedStatement pstmt = conn.prepareStatement("SELECT pid, pname, price, stock FROM products ORDER BY pname ASC");
ResultSet rs = pstmt.executeQuery();

// 변수들을 try 블록 밖에서 선언
int totalProducts = 0;
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
    <!-- 헤더 섹션 -->
    <div class="header">
        <h1>🏢 Shoppingmall Company ERP</h1>
        <div class="header-nav">
            <a href="${pageContext.request.contextPath}/">🏠 대시보드</a>
            <a href="${pageContext.request.contextPath}/CustomerServlet?command=list">👥 고객 관리</a>
            <a href="${pageContext.request.contextPath}/EmployeeServlet?action=list">👨‍💼 직원 관리</a>
            <a href="${pageContext.request.contextPath}/ProductServlet?action=list">📦 상품 관리</a>
            <a href="${pageContext.request.contextPath}/OrderServlet?action=list">🛒 주문 관리</a>
        </div>
    </div>

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
                        while (rs.next()) {
                            totalProducts++;
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
                            <td class="product-id"><strong>#<%= rs.getInt("pid") %></strong></td>
                            <td class="product-name"><strong><%= rs.getString("pname") %></strong></td>
                            <td class="product-price">₩<%= String.format("%,d", (int)price) %></td>
                            <td class="product-stock <%= stockClass %>">
                                <%= stock %>개
                            </td>
                            <td class="product-actions">
                                <a href="ProductServlet?action=edit&pid=<%= rs.getInt("pid") %>" class="btn-edit">✏️ 수정</a>
                                <a href="ProductServlet?action=delete&pid=<%= rs.getInt("pid") %>" class="btn-delete" onclick="return confirm('정말로 삭제하시겠습니까?');">🗑️ 삭제</a>
                            </td>
                        </tr>
                    <%
                        }
                        
                        // 통계 업데이트를 위한 스크립트
                        if (totalProducts > 0) {
                    %>
                        <script>
                            document.querySelector('.product-stats .stat-item:nth-child(1) .stat-number').textContent = '<%= totalProducts %>';
                            document.querySelector('.product-stats .stat-item:nth-child(2) .stat-number').textContent = '<%= lowStockCount %>';
                            document.querySelector('.product-stats .stat-item:nth-child(3) .stat-number').textContent = '₩<%= String.format("%,d", (int)totalValue) %>';
                        </script>
                    <%
                        }
                        
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
            }
            %>
        </div>
    </div>
</body>
</html>
