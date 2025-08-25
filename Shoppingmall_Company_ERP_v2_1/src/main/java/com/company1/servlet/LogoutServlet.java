package com.company1.servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * 로그아웃 기능을 처리하는 서블릿입니다.
 * URL 매핑: /logout
 */

// "/LogoutServlet" 이라는 URL로 이 서블릿을 호출할 수 있도록 설정
@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response) throws ServletException, IOException {
		// 현재 사용자의 세션을 가져옵니다.
		HttpSession session = request.getSession(false);
		
		// 세션이 존재하면, 세션을 무효화(invalidate)합니다.
		if(session != null) {
		   session.invalidate();
		}
		
		// 로그아웃 후 로그인 페이지로 이동시킵니다.
		response.sendRedirect("login.jsp");
	}
}
