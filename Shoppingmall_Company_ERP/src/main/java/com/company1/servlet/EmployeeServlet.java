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

public class EmployeeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public EmployeeServlet() {
        super();
    }

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
            action = "list"; // 기본 action을 'list'로 설정
        }

        EmployeeDAO employeeDAO = new EmployeeDAO();
        String forwardPath = null;

        switch (action) {
            case "list": {
                List<EmployeeDTO> employeeList = employeeDAO.selectAllEmployees();
                request.setAttribute("employeeList", employeeList);
                // ✨ 수정된 부분: 올바른 JSP 파일명으로 경로 변경
                forwardPath = "/employee_list.jsp"; 
                break;
            }

            case "detail": {
                String empId = request.getParameter("empId");
                EmployeeDTO employee = employeeDAO.selectEmployeeById(empId);
                request.setAttribute("employee", employee);
                // (필요시 employee_detail.jsp 생성 후 사용)
                forwardPath = "/employee_detail.jsp"; 
                break;
            }
            
            case "edit": {
                String empId = request.getParameter("empId");
                // DAO를 통해 직원 정보를 가져오는 부분은 JSP가 처리하므로 서블릿에서는 경로만 지정
                // ✨ 수정된 부분: 우리가 만들었던 등록/수정 통합 폼 페이지로 경로 변경
                forwardPath = "/employee_form.jsp?empId=" + empId;
                break;
            }

            case "insert": {
                // JSP 폼에서 보낸 정보들을 받습니다.
                String empId = request.getParameter("empId");
                String empPw = request.getParameter("empPw");
                String empName = request.getParameter("empName");
                String email = request.getParameter("email"); // ✨ 추가된 부분
                String position = request.getParameter("position");
                String auth = request.getParameter("auth");
                
                // 받은 정보들을 DTO 바구니에 담습니다.
                EmployeeDTO empDTO = new EmployeeDTO();
                empDTO.setEmpId(empId);
                empDTO.setEmpPw(empPw);
                empDTO.setEmpName(empName);
                empDTO.setEmail(email); // ✨ 추가된 부분
                empDTO.setPosition(position);
                empDTO.setAuth(auth);

                // DAO에게 "DB에 저장해줘!" 라고 명령합니다.
                employeeDAO.insertEmployee(empDTO);
                
                // 처리가 끝나면 목록 페이지로 다시 이동시킵니다.
                response.sendRedirect(request.getContextPath() + "/EmployeeServlet?action=list");
                return; // sendRedirect 후에는 반드시 return!
            }

            case "update": {
                // 수정 폼에서 보낸 정보들을 받습니다.
                String empId = request.getParameter("empId");
                String empPw = request.getParameter("empPw");
                String empName = request.getParameter("empName");
                String email = request.getParameter("email"); // ✨ 추가된 부분
                String position = request.getParameter("position");
                String auth = request.getParameter("auth");
                
                // DTO 바구니에 새로 담습니다.
                EmployeeDTO empDTO = new EmployeeDTO();
                empDTO.setEmpId(empId);
                empDTO.setEmpPw(empPw);
                empDTO.setEmpName(empName);
                empDTO.setEmail(email); // ✨ 추가된 부분
                empDTO.setPosition(position);
                empDTO.setAuth(auth);
                
                // DAO에게 "DB 업데이트 해줘!" 라고 명령합니다.
                employeeDAO.updateEmployee(empDTO);
                
                // 처리가 끝나면 목록 페이지로 이동시킵니다.
                response.sendRedirect(request.getContextPath() + "/EmployeeServlet?action=list");
                return; 
            }

            case "delete": {
                String empId = request.getParameter("empId");

                if (empId != null && !empId.trim().isEmpty()) {
                    employeeDAO.deleteEmployee(empId);
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