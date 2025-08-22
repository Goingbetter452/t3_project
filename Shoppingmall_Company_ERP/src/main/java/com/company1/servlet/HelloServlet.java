package com.company1.servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/hello")
public class HelloServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println("<!DOCTYPE html>");
        response.getWriter().println("<html><body>");
        response.getWriter().println("<h1>Hello World!</h1>");
        response.getWriter().println("<p>서블릿 매핑이 정상 작동합니다!</p>");
        response.getWriter().println("<a href='CustomerServlet?action=list'>CustomerServlet 테스트</a>");
        response.getWriter().println("</body></html>");
    }
}