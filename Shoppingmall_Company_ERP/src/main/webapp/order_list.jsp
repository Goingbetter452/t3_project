<%@ page language="java" contentType="text/html; charset=UTF-8"
<<<<<<< HEAD
    pageEncoding="UTF-8" %>
<%@ page import="java.util.*, com.company1.dto.OrderDTO" %>
<%@ page import="java.sql.*" %>
=======
    pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="java.io.*" %>
<%@ page import="com.company1.DBManager" %>

<%
//서블릿이 조회한 데이터를 orderList에 담았던 것을 가져옴
ResultSet rs = (ResultSet) request.getAttribute("orderList");

// 고객 목록 조회
Connection conn = com.company1.DBManager.getDBConnection();
//1. 전체 주문 수
PreparedStatement totalOrdersStmt = conn.prepareStatement("SELECT COUNT(*) AS cnt FROM orders");
ResultSet totalOrdersRS = totalOrdersStmt.executeQuery();
int totalOrders = 0;
if (totalOrdersRS.next()) totalOrders = totalOrdersRS.getInt("cnt");
//2. 이번 달 주문 수
PreparedStatement monthOrdersStmt = conn.prepareStatement(
    "SELECT COUNT(*) AS cnt FROM orders WHERE TO_CHAR(order_date, 'YYYYMM') = TO_CHAR(SYSDATE, 'YYYYMM')"
);
ResultSet monthOrdersRS = monthOrdersStmt.executeQuery();
int monthOrders = 0;
if (monthOrdersRS.next()) monthOrders = monthOrdersRS.getInt("cnt");

//3. 총 매출
PreparedStatement salesStmt = conn.prepareStatement(
    "SELECT NVL(SUM(quantity * unit_price), 0) AS total FROM order_items"
);
ResultSet salesRS = salesStmt.executeQuery();
double totalSales = 0;
if (salesRS.next()) totalSales = salesRS.getDouble("total");

PreparedStatement customerStmt = conn.prepareStatement("SELECT cid, cname FROM customers");
ResultSet customers = customerStmt.executeQuery();

// 상품 목록 조회
PreparedStatement productStmt = conn.prepareStatement("SELECT pid, pname FROM products");
ResultSet products = productStmt.executeQuery();
%>
>>>>>>> cddda14998e5a164e841ccb98ce4bf191064d936

<!DOCTYPE html>
<html lang="ko">
<head>
<<<<<<< HEAD
    <meta charset="UTF-8">
    <title>주문 관리</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/common.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/order.css">
</head>
<body>
<%@ include file="common-jsp/header.jsp" %>

<%
    // 주문 목록 DTO
    List<OrderDTO> orderList = (List<OrderDTO>) request.getAttribute("orderList");

    // DB 연결
    Connection conn = com.company1.DBManager.getDBConnection();

    // 통계 데이터
    int totalOrders = 0;
    int monthOrders = 0;
    double totalSales = 0;

    // 고객 목록
    List<Map<String, Object>> customerList = new ArrayList<>();
    List<Map<String, Object>> productList = new ArrayList<>();

    try {
        PreparedStatement stmt;
        ResultSet rs;

        // 전체 주문 수
        stmt = conn.prepareStatement("SELECT COUNT(*) AS cnt FROM orders");
        rs = stmt.executeQuery();
        if (rs.next()) totalOrders = rs.getInt("cnt");
        rs.close();
        stmt.close();

        // 이번 달 주문 수
        stmt = conn.prepareStatement("SELECT COUNT(*) AS cnt FROM orders WHERE TO_CHAR(order_date, 'YYYYMM') = TO_CHAR(SYSDATE, 'YYYYMM')");
        rs = stmt.executeQuery();
        if (rs.next()) monthOrders = rs.getInt("cnt");
        rs.close();
        stmt.close();

        // 총 매출
        stmt = conn.prepareStatement("SELECT NVL(SUM(quantity * unit_price), 0) AS total FROM order_items");
        rs = stmt.executeQuery();
        if (rs.next()) totalSales = rs.getDouble("total");
        rs.close();
        stmt.close();

        // 고객 목록
        stmt = conn.prepareStatement("SELECT cid, cname FROM customers");
        rs = stmt.executeQuery();
        while (rs.next()) {
            Map<String, Object> map = new HashMap<>();
            map.put("cid", rs.getInt("cid"));
            map.put("cname", rs.getString("cname"));
            customerList.add(map);
        }
        rs.close();
        stmt.close();

        // 상품 목록
        stmt = conn.prepareStatement("SELECT pid, pname FROM products");
        rs = stmt.executeQuery();
        while (rs.next()) {
            Map<String, Object> map = new HashMap<>();
            map.put("pid", rs.getInt("pid"));
            map.put("pname", rs.getString("pname"));
            productList.add(map);
        }
        rs.close();
        stmt.close();

        conn.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
%>

<div class="container">
    <!-- 통계 -->
=======
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>주문관리</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/css/common.css">
  <link rel="stylesheet" href="<%= request.getContextPath() %>/css/order.css">
</head>
<body>
    <%@ include file="common-jsp/header.jsp" %>

<%--
  <!-- 헤더 섹션 -->
  <div class="header">
      <h1>🏢 Shoppingmall Company ERP</h1>
      <div class="header-nav">
          <a href="<%= request.getContextPath() %>/">🏠 대시보드</a>
          <a href="<%= request.getContextPath() %>/CustomerServlet?command=list">👥 고객 관리</a>
          <a href="<%= request.getContextPath() %>/EmployeeServlet?action=list">👨‍💼 직원 관리</a>
          <a href="<%= request.getContextPath() %>/ProductServlet?action=list">📦 상품 관리</a>
          <a href="<%= request.getContextPath() %>/OrderServlet?action=list">🛒 주문 관리</a>
      </div>
  </div>
 --%>
 
  <div class="container">
    <!-- 통계 섹션 -->
>>>>>>> cddda14998e5a164e841ccb98ce4bf191064d936
    <div class="stats order-stats">
        <div class="stat-item">
            <div class="stat-number"><%= totalOrders %></div>
            <div class="stat-label">전체 주문 수</div>
        </div>
        <div class="stat-item">
            <div class="stat-number"><%= monthOrders %></div>
            <div class="stat-label">이번 달 주문</div>
        </div>
        <div class="stat-item">
<<<<<<< HEAD
            <div class="stat-number">₩<%= String.format("%,.0f", totalSales) %></div>
=======
            <div class="stat-number"><%= String.format("%,.0f", totalSales) %></div>
>>>>>>> cddda14998e5a164e841ccb98ce4bf191064d936
            <div class="stat-label">총 매출</div>
        </div>
    </div>

<<<<<<< HEAD
    <!-- 주문 등록 폼 -->
    <div class="form-section order-form">
        <h3>🛒 주문 등록</h3>
        <form action="<%= request.getContextPath() %>/OrderServlet" method="post" onsubmit="return confirm('주문을 등록하시겠습니까?');">
            <input type="hidden" name="action" value="insert"/>
            <div class="order-form-row">
                <div class="form-group">
                    <label for="cid">고객:</label>
                    <select name="cid" required>
                        <% for (Map<String, Object> c : customerList) { %>
                            <option value="<%= c.get("cid") %>"><%= c.get("cname") %></option>
                        <% } %>
                    </select>
                </div>
                <div class="form-group">
                    <label for="pid">상품:</label>
                    <select name="pid" required>
                        <% for (Map<String, Object> p : productList) { %>
                            <option value="<%= p.get("pid") %>"><%= p.get("pname") %></option>
                        <% } %>
                    </select>
                </div>
                <div class="form-group">
                    <label for="quantity">수량:</label>
                    <input type="number" name="quantity" min="1" required />
                </div>
                <div class="form-actions">
                    <input type="submit" value="✅ 주문 등록" />
                </div>
            </div>
        </form>
    </div>

    <!-- 주문 목록 -->
    <div class="list-section">
        <h3>📋 주문 목록</h3>
        <table class="order-table">
            <tr>
                <th>주문번호</th>
                <th>고객명</th>
                <th>상품명</th>
                <th>수량</th>
                <th>단가</th>
                <th>합계금액</th>
                <th>주문일</th>
                <th>삭제</th>
            </tr>
            <%
                if (orderList != null && !orderList.isEmpty()) {
                    for (OrderDTO order : orderList) {
            %>
            <tr>
                <td class="order-id">#<%= order.getOid() %></td>
                <td class="order-customer"><%= order.getCustomerName() %></td>
                <td class="order-product"><%= order.getProductName() %></td>
                <td class="order-quantity"><%= order.getQuantity() %></td>
                <td class="order-price">₩<%= String.format("%,.0f", order.getUnitPrice()) %></td>
                <td class="order-total-price">₩<%= String.format("%,.0f", order.getQuantity() * order.getUnitPrice()) %></td>
                <td class="order-date"><%= order.getOrderDate() %></td>
                <td class="order-actions">
				    <a href="<%= request.getContextPath() %>/OrderServlet?action=delete&oid=<%= order.getOid() %>"
				       class="delete-link" onclick="return confirm('삭제하시겠습니까?');">🗑️ 삭제</a>
				</td>

            </tr>
            <%
                    }
                } else {
            %>
            <tr>
                <td colspan="8" style="text-align:center;">주문 내역이 없습니다.</td>
            </tr>
            <% } %>
        </table>
    </div>
</div>
</body>
</html>
=======
    <div class="form-section order-form">
      <h3>🛒 주문등록</h3>
      <form action="<%= request.getContextPath() %>/OrderServlet" method="post" onsubmit="return confirm('주문을 등록하시겠습니까?');" >
        <input type="hidden" name="action" value="insert"/>
        
        <div class="order-form-row">
          <div class="form-group">
            <label for="cid">고객:</label>
            <select name="cid" required>
              <% while (customers.next()) { %>
              <option value="<%= customers.getInt("cid") %>"><%= customers.getString("cname")%></option>
              <% } %>
            </select>
          </div>
        
          <div class="form-group">
            <label for="pid">상품:</label>
            <select name="pid" required>
                <% while (products.next()) { %>
                    <option value="<%= products.getInt("pid") %>"><%= products.getString("pname")%></option>
                <% } %>
            </select>
          </div>

          <div class="form-group">
            <label for="quantity">수량:</label>
            <input type="number" name="quantity" min="1" required />
          </div>
          
          <div class="form-actions">
            <input type="submit" value="✅ 주문 등록" />
          </div>
        </div>
      </form>
    </div>
    
    <div class="list-section">
      <h3>📋 주문 목록</h3>
      <table class="order-table">
        <tr>
          <th>주문번호</th>
          <th>고객명</th>
          <th>상품명</th>
          <th>수량</th>
          <th>단가</th>
          <th>합계금액</th>
          <th>주문일</th>
          <th>삭제</th>
        </tr>
        <% 
        try{
          while (rs != null && rs.next()) {
            %>
        <tr>
          <td class="order-id"><strong>#<%= rs.getInt("oid") %></strong></td>
          <td class="order-customer"><%= rs.getString("cname") %></td>
          <td class="order-product"><%= rs.getString("pname") %></td>
          <td class="order-quantity"><%= rs.getInt("quantity") %></td>
          <td class="order-price">₩<%= String.format("%,.0f", rs.getDouble("unit_price")) %></td>
          <td class="order-total-price">₩<%= String.format("%,.0f",rs.getInt("quantity")*rs.getDouble("unit_price")) %></td>
          <td class="order-date"><%= rs.getTimestamp("order_date") %></td>
          <td class="order-actions"><a href="<%= request.getContextPath() %>/OrderServlet?action=delete&oid=<%= rs.getInt("oid") %>" 
                 class="delete-link" onclick="return confirm('삭제하시겠습니까?');">🗑️ 삭제</a></td>
        </tr>
        <%
          }
        } catch (Exception e) {
          e.printStackTrace();
        } finally {
           if (rs != null) rs.close();
           if (totalOrdersRS != null) totalOrdersRS.close();
           if (monthOrdersRS != null) monthOrdersRS.close();
           if (salesRS != null) salesRS.close();
           if (totalOrdersStmt != null) totalOrdersStmt.close();
           if (monthOrdersStmt != null) monthOrdersStmt.close();
           if (salesStmt != null) salesStmt.close();
            if (customers != null) customers.close();
            if (products != null) products.close();
            if (customerStmt != null) customerStmt.close();
            if (productStmt != null) productStmt.close();
            if (conn != null) conn.close();
        }
        %>
      </table>
    </div>
  </div>
</body>
</html>
>>>>>>> cddda14998e5a164e841ccb98ce4bf191064d936
