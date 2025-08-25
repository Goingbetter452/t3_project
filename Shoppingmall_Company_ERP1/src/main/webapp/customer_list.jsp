<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.company1.dto.CustomerDTO" %>

<%
    // 서블릿에서 전달한 customerList 속성을 List<CustomerDTO> 타입으로 받습니다.
    List<CustomerDTO> customerList = (List<CustomerDTO>) request.getAttribute("customerList");
%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>고객 관리 시스템</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/customer.css">
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
        <div class="stats customer-stats">
            <div class="stat-item">
                <div class="stat-number"><%= customerList != null ? customerList.size() : 0 %></div>
                <div class="stat-label">전체 고객 수</div>
            </div>
            <div class="stat-item">
                <div class="stat-number">0</div>
                <div class="stat-label">신규 고객 (이번 달)</div>
            </div>
            <div class="stat-item">
                <div class="stat-number">0</div>
                <div class="stat-label">활성 고객</div>
            </div>
        </div>

        <!-- 고객 등록 폼 -->
        <div class="form-section customer-form">
            <h2>📝 신규 고객 등록</h2>
            <form action="CustomerServlet" method="post">
                <input type="hidden" name="command" value="insert">
                <div class="form-group">
                    <label for="cname">고객명:</label>
                    <input type="text" id="cname" name="cname" required placeholder="고객명을 입력하세요">
                </div>
                <div class="form-group">
                    <label for="email">이메일:</label>
                    <input type="email" id="email" name="email" placeholder="이메일을 입력하세요">
                </div>
                <div class="form-actions">
                    <input type="submit" value="✅ 고객 등록">
                </div>
            </form>
        </div>

        <!-- 고객 목록 -->
        <div class="list-section">
            <h2>👥 고객 목록</h2>
            <table class="customer-table">
                <thead>
                    <tr>
                        <th>고객 ID</th>
                        <th>고객명</th>
                        <th>이메일</th>
                        <th>등록일</th>
                        <th>관리</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        // 리스트가 null이 아니고 비어있지 않은 경우에만 내용을 표시합니다.
                        if (customerList != null && !customerList.isEmpty()) {
                            // for-each 반복문으로 리스트를 순회합니다.
                            for (CustomerDTO customer : customerList) {
                    %>
                                <tr>
                                    <%-- DTO의 getter 메소드를 사용하여 값을 가져옵니다. --%>
                                    <td class="customer-id"><strong>#<%= customer.getCid() %></strong></td>
                                    <td class="customer-name"><strong><%= customer.getCname() %></strong></td>
                                    <td class="customer-email"><%= customer.getEmail() != null ? customer.getEmail() : "-" %></td>
                                    <td class="customer-date">2025-08-22</td>
                                    <td class="actions">
                                        <a href="CustomerServlet?command=edit&cid=<%= customer.getCid() %>" class="btn-edit">✏️ 수정</a>
                                        <a href="CustomerServlet?command=delete&cid=<%= customer.getCid() %>" class="btn-delete" onclick="return confirm('정말로 삭제하시겠습니까?');">🗑️ 삭제</a>
                                    </td>
                                </tr>
                    <%
                            }
                        } else {
                            // 리스트가 비어있는 경우 메시지를 표시합니다.
                    %>
                        <tr>
                            <td colspan="5" class="no-data">📭 등록된 고객이 없습니다. 첫 번째 고객을 등록해보세요!</td>
                        </tr>
                    <%
                        }
                    %>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>