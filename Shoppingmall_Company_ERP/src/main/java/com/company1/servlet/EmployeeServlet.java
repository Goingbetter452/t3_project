package com.company1.servlet;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.company1.dao.EmployeeDAO;
import com.company1.dto.EmployeeDTO;

/**
 * 직원(Employee) 관련 모든 요청을 처리하는 프론트 컨트롤러 서블릿입니다.
 * URL 매핑: /employee
 */
@WebServlet("/employee")
public class EmployeeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public EmployeeServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		actionDo(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");		// 한글 깨짐 방지
		actionDo(request, response);
	}
	
	private void actionDo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String command = request.getParameter("command");
		String action = request.getParameter("action");
		
		EmployeeDAO employeeDAO = new EmployeeDAO();
		
		String forwardPath = "";
		
		
		if (command == null || command.equals("list")) {
			// 직원 목록 조회
			List<EmployeeDTO> employeeList = employeeDAO.selectAllEmployees();
			request.setAttribute("employeeList", employeeList);
			forwardPath = "/employee/employee_list.jsp";
			
		} else if (command.equals("form")) {
			// 직원 등록/수정 폼으로 이동
			String empId = request.getParameter("empId");
			if (empId != null && !empId.isEmpty()) {
				// 수정 모드: 해당 직원 정보를 가져와서 폼에 전달
				// TODO: selectEmployeeById 메서드 구현 후 사용
				// EmployeeDTO emp = employeeDAO.selectEmployeeById(empId);
				// request.setAttribute("employee", emp);
			}
			forwardPath = "/employee/employee_form.jsp";
			
		} else if (action != null && action.equals("insert")) {
			// 직원 등록 처리
			String empId = request.getParameter("empId");
			String empPw = request.getParameter("empPw");
			String empName = request.getParameter("empName");
			String position = request.getParameter("position");
			String auth = request.getParameter("auth");
			
			
			 // 여기에 유효성 검사 코드가 시작 
		    /* =======================================================
		     * 서버 측 유효성 검사 (Server-side Validation)
		     * ======================================================= */
		    // 검사 1: 주요 데이터가 비어있는지 확인
		    if (empId == null || empId.trim().isEmpty() || 
		        empPw == null || empPw.trim().isEmpty() || 
		        empName == null || empName.trim().isEmpty() ||
		        position == null || position.trim().isEmpty()) {
		        
		        // 문제가 있으면 다시 폼으로 돌려보낸다.
		        response.sendRedirect("employee/employee_form.jsp?error=empty_data");
		        return; // 여기서 메소드 실행을 중단시킨다.
		    }

		    // 검사 2: 비밀번호가 8자리 이상인지 확인
		    if (empPw.trim().length() < 8) {
		        response.sendRedirect("employee/employee_form.jsp?error=password_length");
		        return;
		    }

		    // 검사 3: 아이디가 영문/숫자로만 되어있는지 확인 (정규표현식 사용)
		    // ^[a-zA-Z0-9]*$  -> 시작(^)부터 끝($)까지 영문(a-zA-Z)과 숫자(0-9)만 있는지 검사
		    if (!empId.matches("^[a-zA-Z0-9]*$")) {
		        response.sendRedirect("employee/employee_form.jsp?error=invalid_id");
		        return;
		    }
		    // 여기까지 유효성 검사 코드

			
		    
			EmployeeDTO empDTO = new EmployeeDTO();
			empDTO.setEmpId(empId);
			empDTO.setEmpPw(empPw);
			empDTO.setEmpName(empName);
			empDTO.setPosition(position);
			empDTO.setAuth(auth);
			
			employeeDAO.insertEmployee(empDTO);
			
			response.sendRedirect("index.jsp");
			
		} else if (action != null && action.equals("update")) {
			// 직원 수정 처리 (나중에 구현)
			System.out.println("직원 수정 처리");
			response.sendRedirect("employee?command=list");
			return;
			
		} else if (command.equals("delete")) {
			// 직원 삭제 처리 (나중에 구현)
			System.out.println("직원 삭제 요청 처리");
			response.sendRedirect("employee?command=list");
			return;
		}
		
		
		RequestDispatcher dispatcher = request.getRequestDispatcher(forwardPath);
		dispatcher.forward(request, response);
	}

}