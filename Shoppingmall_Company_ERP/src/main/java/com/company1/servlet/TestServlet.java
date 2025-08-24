package com.company1.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class TestServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>테스트 서블릿</title>");
        out.println("<meta charset='UTF-8'>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>TestServlet이 정상 작동합니다!</h1>");
        out.println("<p>현재 시간: " + new java.util.Date() + "</p>");
        out.println("<p>컨텍스트 경로: " + request.getContextPath() + "</p>");
        out.println("<p>요청 URI: " + request.getRequestURI() + "</p>");
        out.println("<p>서블릿 경로: " + request.getServletPath() + "</p>");
        out.println("<a href='" + request.getContextPath() + "/'>홈으로</a>");
        out.println("</body>");
        out.println("</html>");
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}
