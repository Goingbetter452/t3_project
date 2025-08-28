<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

	<%
    String pageTitle = (String) request.getAttribute("pageTitle");
    if (pageTitle == null || pageTitle.isEmpty()) {
        pageTitle = "B2B Shoppingmall Company ERP"; // 기본 제목
    }
	%>
	<title><%= pageTitle %></title>
	
		