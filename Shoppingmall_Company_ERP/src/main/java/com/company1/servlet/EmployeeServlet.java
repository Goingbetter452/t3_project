//package com.company1.controller;
//
//import java.io.IOException;
//import java.util.List;
//
//import javax.servlet.RequestDispatcher;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//// DTO와 DAO의 패키지 경로를 보내주신 코드에 맞게 수정했습니다.
//// (나중에 com.company1.* 로 변경하시는 것을 추천합니다!)
//import java.com.company1.dao.EmployeeDAO;
//import java.com.company1.dto.EmployeeDTO;
//
///**
// * 직원(Employee) 관련 모든 요청을 처리하는 프론트 컨트롤러 서블릿입니다.
// * URL 매핑: /employee
// */
//@WebServlet("/employee")
//public class EmployeeServlet extends HttpServlet {
//	private static final long serialVersionUID = 1L;
//       
//    public EmployeeServlet() {
//        super();
//    }
//
//	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		actionDo(request, response);
//	}
//
//	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		request.setCharacterEncoding("UTF-8");
//		actionDo(request, response);
//	}
//	
//	private void actionDo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		
//		String command = request.getParameter("command");
//		
//		// [수정된 부분 1] new 키워드를 사용해서 DAO 객체를 직접 생성합니다.
//		EmployeeDAO employeeDAO = new EmployeeDAO();
//		
//		String forwardPath = "";
//		
//		
//		if (command == null || command.equals("list")) {
//			
//			// [수정된 부분 2] DAO에 작성된 메소드 이름(selectAllEmployees)과 동일하게 변경했습니다.
//			List<EmployeeDTO> employeeList = employeeDAO.selectAllEmployees();
//			
//			request.setAttribute("employeeList", employeeList);
//			
//			forwardPath = "/employee/employee_list.jsp";
//			
//		} else if (command.equals("add")) {
//			// (이 부분은 나중에 employee_form.jsp와 함께 구현할 예정입니다.)
//			// DAO의 insertEmployee() 메소드를 호출하여 DB에 저장합니다.
//			
//			System.out.println("직원 등록 요청 처리");
//			response.sendRedirect("employee?command=list");
//			return; 
//			
//		} else if (command.equals("editForm")) {
//			// (이 부분도 나중에 구현할 예정입니다.)
//			
//			System.out.println("직원 수정 폼 요청 처리");
//			forwardPath = "/employee/employee_form.jsp";
//			
//		} else if (command.equals("delete")) {
//			// (이 부분도 나중에 구현할 예정입니다.)
//			
//			System.out.println("직원 삭제 요청 처리");
//			response.sendRedirect("employee?command=list");
//			return;
//		}
//		
//		
//		RequestDispatcher dispatcher = request.getRequestDispatcher(forwardPath);
//		dispatcher.forward(request, response);
//	}
//
//}
