package com.company1.servlet;

import java.io.IOException;
import java.util.List;

import com.company1.dao.CustomerDAO;
import com.company1.dto.CustomerDTO;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/CustomerServlet")
public class CustomerServlet extends HttpServlet {

    private CustomerDAO customerDAO = new CustomerDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "list":
                listCustomers(request, response);
                break;
            case "delete":
                deleteCustomer(request, response);
                break;
            case "edit":
                editCustomerForm(request, response);
                break;
            default:
                listCustomers(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("insert".equals(action)) {
            insertCustomer(request, response);
        } else if ("update".equals(action)) {
            updateCustomer(request, response);
        } else {
            doGet(request, response);
        }
    }

    private void listCustomers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<CustomerDTO> list = customerDAO.getAllCustomers();
        request.setAttribute("customerList", list);

        RequestDispatcher dispatcher = request.getRequestDispatcher("customer_list.jsp");
        dispatcher.forward(request, response);
    }

    private void insertCustomer(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String cname = request.getParameter("cname");
        String email = request.getParameter("email");

        customerDAO.insertCustomer(cname, email);
        response.sendRedirect("CustomerServlet?action=list");
    }

    private void deleteCustomer(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int cid = Integer.parseInt(request.getParameter("cid"));
        customerDAO.deleteCustomer(cid);
        response.sendRedirect("CustomerServlet?action=list");
    }

    private void editCustomerForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int cid = Integer.parseInt(request.getParameter("cid"));
        CustomerDTO customer = customerDAO.getCustomerById(cid);

        request.setAttribute("customer", customer);
        RequestDispatcher dispatcher = request.getRequestDispatcher("customer_edit.jsp");
        dispatcher.forward(request, response);
    }

    private void updateCustomer(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        int cid = Integer.parseInt(request.getParameter("cid"));
        String cname = request.getParameter("cname");
        String email = request.getParameter("email");

        CustomerDTO dto = new CustomerDTO();
        dto.setCid(cid);
        dto.setCname(cname);
        dto.setEmail(email);

        customerDAO.updateCustomer(dto);
        response.sendRedirect("CustomerServlet?action=list");
    }
}
