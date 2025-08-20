package com.company1.servlet;
import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.company1.dao.EmployeeDAO;
import com.company1.dto.EmployeeDTO;

@WebServlet("/LoginServlet") 	// Login.jsp의 데이터를 받을라고 있는 주소
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	 protected void doPost(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response) 
	            throws ServletException, IOException {
		 
		 
		 // 1. login.jsp에서 사용자가 입력한 데이터 받기
		 request.setCharacterEncoding("UTF-8");
		 String id = request.getParameter("username"); // <input name="username">
		 String pw = request.getParameter("password"); // <input name="password">
		 
		 
		 // 2. DAO 객체 생성 (DB에게 일을 시킬 준비)
		 EmployeeDAO dao = new EmployeeDAO();
		 
		 
		 // 3. DAO에게 id와 pw를 주고 로그인 체크를 치킴 -> 결과를 DTO로 받음
		 EmployeeDTO empDTO = dao.checkLogin(id,pw);
		 
		 
		 // 4. 로그인 결과 처리
		 if(empDTO != null) { // 로그인 성공! (null이 아니면 회원정보가 존재한다는 뜻임)
		    System.out.println("로그인 성공: " + empDTO.getEmpName() + "님 환영합니다.");
		    
		 // 세션(Session)이라는 서버의 개인 사물함에 로그인 정보를 저장   
		 jakarta.servlet.http.HttpSession session = request.getSession();
		 session.setAttribute("loginUser", empDTO); 	// "loginUser"라는 이름표로 DTO 통째로 저장
		 
		 
		 // 메인 페이지로 이동하기(메인페이지 이름 index.jsp로 만들었는지 이름 잘 확인해보기!)
		 response.sendRedirect("index.jsp");
		 
		 } else {	// 로그인 실패! (null이면 회원정보가 없다는 뜻)
			 System.out.println("로그인 실패: 아이디 또는 비밀버호를 다시 확인부탁드립니다.");
			 
			 // 다시 로그인 페이지로 돌아가기
			 response.sendRedirect("login.jsp");
		 }
	 }

}