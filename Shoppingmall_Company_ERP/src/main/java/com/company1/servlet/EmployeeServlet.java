package com.company1.servlet;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.company1.dao.EmployeeDAO;
import com.company1.dto.EmployeeDTO;

/**
 * 직원(Employee) 관련 모든 요청을 처리하는 프론트 컨트롤러 서블릿입니다.
 * URL 매핑: /EmployeeServlet
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
	    
	    // 'action' 파라미터를 기준으로 로직을 통일합니다.
	    String action = request.getParameter("action");
	    if (action == null) {
	        action = "list"; // 파라미터가 없으면 기본값은 'list'
	    }

	    EmployeeDAO employeeDAO = new EmployeeDAO();
	    String forwardPath = null; // forward할 경로가 있을 때만 사용하도록 null로 초기화

	    switch (action) {
	        case "list":
	            // 직원 목록 조회
	            List<EmployeeDTO> employeeList = employeeDAO.selectAllEmployees();
	            request.setAttribute("employeeList", employeeList);
	            forwardPath = "/employee_list.jsp"; // 결과를 보여줄 JSP 경로를 정확히 지정
	            break;

	        case "form":
	            // 직원 등록/수정 폼으로 이동
	            // TODO: 수정 로직 구현
	            forwardPath = "/employee_form.jsp";
	            break;

	        case "insert":
	            // 직원 등록 처리
	            String empId = request.getParameter("empId");
	            String empPw = request.getParameter("empPw");
	            String empName = request.getParameter("empName");
	            String position = request.getParameter("position");
	            String auth = request.getParameter("auth");
	            
	            // 유효성 검사... (기존 코드와 동일)
	            if (empId == null || empId.trim().isEmpty() || empPw == null || empPw.trim().isEmpty()) {
	                response.sendRedirect(request.getContextPath() + "/employee_form.jsp?error=empty_data");
	                return;
	            }
	            // ... 나머지 유효성 검사 ...

	            EmployeeDTO empDTO = new EmployeeDTO();
	            empDTO.setEmpId(empId);
	            empDTO.setEmpPw(empPw);
	            empDTO.setEmpName(empName);
	            empDTO.setPosition(position);
	            empDTO.setAuth(auth);
	            
	            employeeDAO.insertEmployee(empDTO);
	            
	            // sendRedirect 시에는 contextPath를 포함하는 것이 안전합니다.
	            response.sendRedirect(request.getContextPath() + "/EmployeeServlet?action=list");
	            return; // redirect 후에는 메소드 종료

	        case "update":
	            // 직원 수정 처리
	            System.out.println("직원 수정 처리");
	            response.sendRedirect(request.getContextPath() + "/EmployeeServlet?action=list");
	            return;

	        case "delete":
	            // 직원 삭제 처리
	            System.out.println("직원 삭제 요청 처리");
	            response.sendRedirect(request.getContextPath() + "/EmployeeServlet?action=list");
	            return;
	    }
	    
	    // forwardPath가 설정된 경우에만 forward를 실행합니다.
	    if (forwardPath != null) {
	        RequestDispatcher dispatcher = request.getRequestDispatcher(forwardPath);
	        dispatcher.forward(request, response);
	    }
	}
}