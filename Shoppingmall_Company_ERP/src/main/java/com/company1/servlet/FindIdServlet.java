package com.company1.servlet;

import java.io.IOException;

import com.company1.dao.EmployeeDAO; // DAO를 사용하기 위해 import

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet("/FindIdServlet")
public class FindIdServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public FindIdServlet() {
        super();
    }
    
    /**
     * ★★★ 요리사 출근카드! ★★★
     * 이 메시지가 콘솔에 보이면, 서블릿이 서버에 성공적으로 등록된 것입니다!
     * 이게 안 보이면 404 에러의 원인입니다.
     */
    @Override
    public void init() throws ServletException {
        System.out.println("<<<<< FindIdServlet 요리사, 주방에 정상 출근 완료! (초기화 성공) >>>>>");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        // jsp에서 보낸 name="empName", name="empEmail" 값을 정확히 받습니다.
        String empName = request.getParameter("empName");
        String email = request.getParameter("empEmail");

        System.out.println("주문 접수 완료! 이름: " + empName + ", 이메일: " + email);

        // DAO에게 DB 조회를 시킵니다.
        EmployeeDAO dao = new EmployeeDAO();
        String foundId = dao.findIdByNameAndEmail(empName, email);

        // 찾은 아이디를 request에 담아 결과 페이지로 보냅니다.
        request.setAttribute("foundId", foundId);

        RequestDispatcher dispatcher = request.getRequestDispatcher("findIdResult.jsp");
        dispatcher.forward(request, response);
    }
}

