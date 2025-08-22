<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.company1.servlet.SimpleCustomerServlet" %>
<%@ page import="java.io.*" %>

<%
    SimpleCustomerServlet servlet = new SimpleCustomerServlet();
    String method = request.getMethod();
    String queryString = request.getQueryString();
    
    // PrintWriter를 사용하여 HTML 출력
    PrintWriter out = response.getWriter();
    servlet.handleRequest(method, queryString, out);
%>
