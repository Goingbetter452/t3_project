package com.company1.servlet;

import java.io.IOException;
import java.util.List;

import com.company1.dao.CustomerDAO;
import com.company1.dto.CustomerDTO;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomerServlet extends HttpServlet {
	
    private static final long serialVersionUID = 1L;
    
    // DAO 객체를 서블릿의 멤버 변수로 생성 (한 번만 생성해서 계속 사용)
    private CustomerDAO customerDAO = new CustomerDAO();

    // CustomerServlet의 doGet 메서드 시작 부분에 추가
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String command = request.getParameter("command"); // "action"을 "command"로 변경
        if (command == null) {
            command = "list"; // 기본 동작을 목록 보기로 설정
        }

        switch (command) { // "action"을 "command"로 변경
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
        
        String command = request.getParameter("command"); // "action"을 "command"로 변경

        if ("insert".equals(command)) { // "action"을 "command"로 변경
            insertCustomer(request, response);
        } else if ("update".equals(command)) { // "action"을 "command"로 변경
            updateCustomer(request, response);
        }
    }

    // Updated listCustomers method to align with modified CustomerDTO
    // Added debugging to log customerList size
    private void listCustomers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("=== CustomerServlet.listCustomers() 시작 ===");
        
        try {
            List<CustomerDTO> customerList = customerDAO.getAllCustomers();
            System.out.println("Customer list size: " + (customerList != null ? customerList.size() : "null"));
            
            if (customerList != null) {
                for (CustomerDTO customer : customerList) {
                    System.out.println("고객: CID=" + customer.getCid() + ", 이름=" + customer.getCname() + ", 이메일=" + customer.getEmail());
                }
            }
            
            request.setAttribute("customerList", customerList);
            System.out.println("customerList를 request에 설정 완료");
            
            RequestDispatcher dispatcher = request.getRequestDispatcher("customer_list.jsp");
            System.out.println("customer_list.jsp로 forward 시작");
            dispatcher.forward(request, response);
            System.out.println("forward 완료");
            
        } catch (Exception e) {
            System.err.println("listCustomers에서 오류 발생: " + e.getMessage());
            e.printStackTrace();
            throw new ServletException("고객 목록 조회 중 오류가 발생했습니다.", e);
        }
    }

    // Removed handling of id and phone in other methods
    private void insertCustomer(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        CustomerDTO customer = new CustomerDTO();
        customer.setCname(request.getParameter("cname"));
        customer.setEmail(request.getParameter("email"));
        customerDAO.addCustomer(customer);
        response.sendRedirect(request.getContextPath() + "/CustomerServlet?command=list"); // "action"을 "command"로 변경
    }

    private void deleteCustomer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int cid = Integer.parseInt(request.getParameter("cid"));
        customerDAO.deleteCustomer(cid);
        response.sendRedirect(request.getContextPath() + "/CustomerServlet?command=list"); // "action"을 "command"로 변경
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int cid = Integer.parseInt(request.getParameter("cid"));
        CustomerDTO customer = customerDAO.getCustomerById(cid);
        request.setAttribute("customer", customer);
        // customer_edit.jsp는 별도로 만들어야 합니다.
        RequestDispatcher dispatcher = request.getRequestDispatcher("customer_edit.jsp"); 
        dispatcher.forward(request, response);
    }

    private void updateCustomer(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        CustomerDTO customer = new CustomerDTO();
        customer.setCid(Integer.parseInt(request.getParameter("cid")));
        customer.setCname(request.getParameter("cname"));
        customer.setEmail(request.getParameter("email"));

        customerDAO.updateCustomer(customer);
        response.sendRedirect(request.getContextPath() + "/CustomerServlet?command=list");
    }
}