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
 * 비밀번호 재설정을 실제로 처리하는 서블릿
 */
@WebServlet("/ResetPasswordServlet")
public class ResetPasswordServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ResetPasswordServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. 한글 데이터 인코딩 설정
        request.setCharacterEncoding("UTF-8");

        // 2. 폼에서 전송된 데이터 받기
        String empId = request.getParameter("empId");
        String newPw = request.getParameter("newPw");
        String confirmPw = request.getParameter("confirmPw");

        System.out.println("비밀번호 재설정 - 아이디: " + empId);
        System.out.println("새 비밀번호 길이: " + (newPw != null ? newPw.length() : 0));

        // 3. 비밀번호 확인 검증
        if (newPw == null || confirmPw == null || !newPw.equals(confirmPw)) {
            request.setAttribute("error", "비밀번호가 일치하지 않습니다.");
            request.setAttribute("empId", empId);
            request.setAttribute("isValidUser", true); // 다시 폼을 보여주기 위해
            RequestDispatcher dispatcher = request.getRequestDispatcher("resetPassword.jsp");
            dispatcher.forward(request, response);
            return;
        }

        // 4. 비밀번호 길이 검증 (최소 4자 이상)
        if (newPw.length() < 4) {
            request.setAttribute("error", "비밀번호는 최소 4자 이상이어야 합니다.");
            request.setAttribute("empId", empId);
            request.setAttribute("isValidUser", true); // 다시 폼을 보여주기 위해
            RequestDispatcher dispatcher = request.getRequestDispatcher("resetPassword.jsp");
            dispatcher.forward(request, response);
            return;
        }

        // 5. DAO를 통해 비밀번호 업데이트
        EmployeeDAO dao = new EmployeeDAO();
        boolean isSuccess = dao.resetPassword(empId, newPw);

        // 6. 결과에 따라 적절한 페이지로 이동
        if (isSuccess) {
            request.setAttribute("success", true);
            request.setAttribute("message", "비밀번호가 성공적으로 변경되었습니다.");
        } else {
            request.setAttribute("success", false);
            request.setAttribute("message", "비밀번호 변경에 실패했습니다. 다시 시도해주세요.");
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("resetPasswordResult.jsp");
        dispatcher.forward(request, response);
    }
}