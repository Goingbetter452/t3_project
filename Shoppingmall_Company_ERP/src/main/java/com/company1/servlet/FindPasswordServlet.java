package com.company1.servlet;

import java.io.IOException;

import com.company1.dao.EmployeeDAO;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 비밀번호 찾기/재설정을 처리하는 서블릿
 */
@WebServlet("/FindPasswordServlet")
public class FindPasswordServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public FindPasswordServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. 한글 데이터 인코딩 설정
        request.setCharacterEncoding("UTF-8");

        // 2. 폼에서 전송된 데이터 받기
        String empId = request.getParameter("empId");
        String email = request.getParameter("empEmail");

        System.out.println("비밀번호 찾기 - 아이디: " + empId);
        System.out.println("비밀번호 찾기 - 이메일: " + email);

        // 3. DAO를 통해 아이디와 이메일이 일치하는지 확인
        EmployeeDAO dao = new EmployeeDAO();
        boolean isValidUser = dao.checkUserByIdAndEmail(empId, email);

        // 4. 결과를 request에 설정
        request.setAttribute("empId", empId);
        request.setAttribute("email", email);
        request.setAttribute("isValidUser", isValidUser);

        // 5. 결과 페이지로 이동
        RequestDispatcher dispatcher = request.getRequestDispatcher("resetPassword.jsp");
        dispatcher.forward(request, response);
    }
}