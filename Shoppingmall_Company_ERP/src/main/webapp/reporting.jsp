<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*, com.company1.dto.MonthlySale, com.company1.dto.MonthlyCancel, com.company1.dto.MonthlyRanking " %>

<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <title>월별 판매 통계</title>
</head>
<body>
<%@ include file="common-jsp/header.jsp" %>
<div class="container">

    <!-- 월별 판매 통계 -->
    <div class="list-section">
        <h2>월별 판매 통계</h2>
        <table border="1" class="border0">
            <thead>
                <th>월</th>
                <th>판매 수량</th>
                <th>총 매출</th>
            </thead>
            <%
                List<MonthlySale> monthlySales = (List<MonthlySale>) request.getAttribute("monthlySales");
                if (monthlySales == null) {
                    monthlySales = new ArrayList<>();
                }
                for (MonthlySale sale : monthlySales) {
            %>
            <tr>
                <td><%= sale.getMonth() %></td>
                <td><%= sale.getTotalQuantity() %></td>
                <td><%= String.format("%,.0f", sale.getTotalSales()) %></td>
            </tr>
            <% } %>
        </table>
    </div>
    
    <!-- 월별 상품별 매출 랭킹 -->
    <div class="list-section">
        <h2>월간 상품별 매출 TOP5</h2>
        <table border="1" class="border0">
            <thead>
                <tr>
                    <th>월</th>
                    <th>상품명</th>
                    <th>총 매출</th>
                </tr>
            </thead>
            <tbody>
                <%
                    List<MonthlyRanking> monthlyRankings = (List<MonthlyRanking>) request.getAttribute("monthlyRankings");
                    if (monthlyRankings == null) {
                        monthlyRankings = new ArrayList<>();
                    }
                    for (MonthlyRanking ranking : monthlyRankings) {
                %>
                <tr>
                    <td><%= ranking.getMonth() %></td>
                    <td><%= ranking.getProductName() %></td>
                    <td>₩<%= String.format("%,.0f", ranking.getTotalSales()) %></td>
                </tr>
                <% } %>
            </tbody>
        </table>
    </div>
</div>

</body>
</html>
