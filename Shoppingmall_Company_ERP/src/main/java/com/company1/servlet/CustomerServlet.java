package com.company1.servlet;

import java.io.IOException;
import java.util.List;

import com.company1.dao.CustomerDAO;
import com.company1.dto.CustomerDTO;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// @WebServlet 어노테이션으로 서블릿을 등록하는 것이 편리합니다.
@WebServlet("/CustomerServlet")
public class CustomerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    // DAO 객체를 서블릿의 멤버 변수로 생성 (한 번만 생성해서 계속 사용)
    private CustomerDAO customerDAO = new CustomerDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "list"; // 기본 동작을 목록 보기로 설정
        }

        switch (action) {
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteCustomer(request, response);
                break;
            case "list":
            default:
                listCustomers(request, response);
                break;
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // POST 요청 시 한글 깨짐 방지
        request.setCharacterEncoding("UTF-8");
        
        String action = request.getParameter("action");

        if ("insert".equals(action)) {
            insertCustomer(request, response);
        } else if ("update".equals(action)) {
            updateCustomer(request, response);
        }
    }

    private void listCustomers(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // DAO를 통해 고객 목록을 가져옴 (이제 List<CustomerDTO> 형태)
        List<CustomerDTO> customerList = customerDAO.getAllCustomers();
        request.setAttribute("customerList", customerList);
        RequestDispatcher dispatcher = request.getRequestDispatcher("customer_list.jsp");
        dispatcher.forward(request, response);
    }

    private void insertCustomer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // JSP 폼에서 넘어온 데이터로 DTO 객체 생성
        CustomerDTO customer = new CustomerDTO();
        // ID는 예시로 1을 사용. 실제로는 로그인된 사용자 ID를 사용해야 함.
        customer.setId(1); 
        customer.setCname(request.getParameter("cname"));
        customer.setEmail(request.getParameter("email"));
        customer.setPhone(request.getParameter("phone"));

        // DAO에 DTO를 전달하여 DB에 추가
        customerDAO.addCustomer(customer);
        response.sendRedirect("CustomerServlet?action=list");
    }

    private void deleteCustomer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int cid = Integer.parseInt(request.getParameter("cid"));
        customerDAO.deleteCustomer(cid);
        response.sendRedirect("CustomerServlet?action=list");
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int cid = Integer.parseInt(request.getParameter("cid"));
        CustomerDTO customer = customerDAO.getCustomerById(cid);
        request.setAttribute("customer", customer);
        // customer_edit.jsp는 별도로 만들어야 합니다.
        RequestDispatcher dispatcher = request.getRequestDispatcher("customer_edit.jsp"); 
        dispatcher.forward(request, response);
    }

    private void updateCustomer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        CustomerDTO customer = new CustomerDTO();
        customer.setCid(Integer.parseInt(request.getParameter("cid")));
        customer.setCname(request.getParameter("cname"));
        customer.setEmail(request.getParameter("email"));
        customer.setPhone(request.getParameter("phone"));

        customerDAO.updateCustomer(customer);
        response.sendRedirect("CustomerServlet?action=list");
    }
}