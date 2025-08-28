package com.company1.servlet;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
<<<<<<< HEAD
import jakarta.servlet.annotation.*;
=======
>>>>>>> cddda14998e5a164e841ccb98ce4bf191064d936
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.company1.dao.EmployeeDAO;
import com.company1.dto.EmployeeDTO;

<<<<<<< HEAD
/**
 * 직원(Employee) 관련 모든 요청을 처리하는 프론트 컨트롤러 서블릿입니다.
 * URL 매핑: /EmployeeServlet
 */



@WebServlet("/employee")

public class EmployeeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
=======
@WebServlet("/employee")
public class EmployeeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

>>>>>>> cddda14998e5a164e841ccb98ce4bf191064d936
    public EmployeeServlet() {
        super();
    }

<<<<<<< HEAD
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
	            // 1. 삭제할 직원의 ID를 요청(request)에서 받아옵니다.
	            empId = request.getParameter("empId"); 
	            
	            // (선택사항) empId가 제대로 넘어왔는지 확인하는 방어 코드
	            if (empId != null && !empId.trim().isEmpty()) {
	                
	                // 2. DAO 객체에게 받아온 ID를 전달하며 삭제를 명령합니다! (가장 중요!)
	                employeeDAO.deleteEmployee(empId); 
	                
	                
	                System.out.println(empId + " 직원 정보가 삭제되었습니다."); // 확인용 로그
	            } else {
	                System.out.println("삭제할 직원 ID가 없습니다."); // 오류 로그
	            }

	            // 3. 처리가 끝난 후, 최신 목록 페이지로 이동합니다.
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
=======
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        actionDo(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        actionDo(request, response);
    }

    private void actionDo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        EmployeeDAO employeeDAO = new EmployeeDAO();
        String forwardPath = null;

        // 변수 스코프 문제를 해결하기 위해 각 case를 { } 로 감싸줍니다.
        switch (action) {
            case "list": {
                List<EmployeeDTO> employeeList = employeeDAO.selectAllEmployees();
                request.setAttribute("employeeList", employeeList);
                forwardPath = "/employee_list.jsp";
                break;
            }

            case "detail": {
                String empId = request.getParameter("empId");
                EmployeeDTO employee = employeeDAO.selectEmployeeById(empId);
                request.setAttribute("employee", employee);
                forwardPath = "/employee_detail.jsp";
                break;
            }
            
            // ================== ✨ 1. '수정 폼 보여주기' 로직을 여기에 새로 추가합니다! ✨ ==================
            case "edit": {
                // 1. 수정할 직원의 ID를 받습니다.
                String empId = request.getParameter("empId");
                // 2. DAO를 통해 해당 직원의 모든 정보를 가져옵니다.
                EmployeeDTO employee = employeeDAO.selectEmployeeById(empId);
                // 3. 가져온 정보를 request 바구니에 담습니다.
                request.setAttribute("employee", employee);
                // 4. 정보를 미리 채워넣을 수정 폼 페이지로 이동시킵니다.
                forwardPath = "/employee_update_form.jsp";
                break;
            }
            // =====================================================================================

            case "insert": {
                String empId = request.getParameter("empId");
                String empPw = request.getParameter("empPw");
                String empName = request.getParameter("empName");
                String position = request.getParameter("position");
                String auth = request.getParameter("auth");

                if (empId == null || empId.trim().isEmpty() || empPw == null || empPw.trim().isEmpty()) {
                    response.sendRedirect(request.getContextPath() + "/employee_form.jsp?error=empty_data");
                    return;
                }

                EmployeeDTO empDTO = new EmployeeDTO();
                empDTO.setEmpId(empId);
                empDTO.setEmpPw(empPw);
                empDTO.setEmpName(empName);
                empDTO.setPosition(position);
                empDTO.setAuth(auth);

                employeeDAO.insertEmployee(empDTO);
                response.sendRedirect(request.getContextPath() + "/EmployeeServlet?action=list");
                return;
            }

            // ================== ✨ 2. 기존의 비어있던 'update' 부분을 아래 코드로 완전히 교체합니다! ✨ ==================
            case "update": {
                // 1. 수정 폼(employee_update_form.jsp)에서 보낸 정보들을 받습니다.
                String empId = request.getParameter("empId");
                String empPw = request.getParameter("empPw");
                String empName = request.getParameter("empName");
                String position = request.getParameter("position");
                String auth = request.getParameter("auth");
                
                // 2. 받은 정보들을 DTO 바구니에 새로 담습니다.
                EmployeeDTO empDTO = new EmployeeDTO();
                empDTO.setEmpId(empId);
                empDTO.setEmpPw(empPw);
                empDTO.setEmpName(empName);
                empDTO.setPosition(position);
                empDTO.setAuth(auth);
                
                // 3. DAO에게 정보가 담긴 바구니를 주면서 "DB 업데이트 해줘!" 라고 명령합니다.
                employeeDAO.updateEmployee(empDTO);
                
                // 4. 처리가 끝나면, 최신 정보가 반영된 목록 페이지로 이동시킵니다.
                response.sendRedirect(request.getContextPath() + "/EmployeeServlet?action=list");
                return; // redirect 후에는 반드시 return!
            }
            // =================================================================================================

            case "delete": {
                String empId = request.getParameter("empId");

                if (empId != null && !empId.trim().isEmpty()) {
                    employeeDAO.deleteEmployee(empId);
                    System.out.println(empId + " 직원 정보가 삭제되었습니다.");
                } else {
                    System.out.println("삭제할 직원 ID가 없습니다.");
                }
                response.sendRedirect(request.getContextPath() + "/EmployeeServlet?action=list");
                return;
            }
        }

        if (forwardPath != null) {
            RequestDispatcher dispatcher = request.getRequestDispatcher(forwardPath);
            dispatcher.forward(request, response);
        }
    }
}
>>>>>>> cddda14998e5a164e841ccb98ce4bf191064d936
