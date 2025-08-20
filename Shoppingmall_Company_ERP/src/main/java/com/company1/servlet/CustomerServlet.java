package com.company1.servlet;

import java.io.IOException;
import java.sql.*;

import com.company1.DBManager;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomerServlet extends HttpServlet {

protected void doGet(HttpServletRequest request, HttpServletResponse response)
throws ServletException, IOException {
String action = request.getParameter("action");
if (action == null) action = "list";

switch(action) {
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
String sql = "SELECT * FROM customers";
try (Connection conn = DBManager.getDBConnection();
PreparedStatement pstmt = conn.prepareStatement(sql);
ResultSet rs = pstmt.executeQuery()) {

request.setAttribute("customerList", rs);
RequestDispatcher dispatcher = request.getRequestDispatcher("customer_list.jsp");
dispatcher.forward(request, response);
} catch(SQLException e) {
e.printStackTrace();
}
}

private void insertCustomer(HttpServletRequest request, HttpServletResponse response)
throws IOException {
String cname = request.getParameter("cname");
String email = request.getParameter("email");

String sql = "INSERT INTO customers(cname, email) VALUES (?, ?)";
try (Connection conn = DBManager.getDBConnection();
PreparedStatement pstmt = conn.prepareStatement(sql)) {

pstmt.setString(1, cname);
pstmt.setString(2, email);
pstmt.executeUpdate();
} catch(SQLException e) {
e.printStackTrace();
}
response.sendRedirect("CustomerServlet?action=list");
}

private void deleteCustomer(HttpServletRequest request, HttpServletResponse response)
throws IOException {
int cid = Integer.parseInt(request.getParameter("cid"));
String sql = "DELETE FROM customers WHERE cid=?";
try (Connection conn = DBManager.getDBConnection();
PreparedStatement pstmt = conn.prepareStatement(sql)) {

pstmt.setInt(1, cid);
pstmt.executeUpdate();
} catch(SQLException e) {
e.printStackTrace();
}
response.sendRedirect("CustomerServlet?action=list");
}

private void editCustomerForm(HttpServletRequest request, HttpServletResponse response)
throws ServletException, IOException {
int cid = Integer.parseInt(request.getParameter("cid"));
String sql = "SELECT * FROM customers WHERE cid=?";
try (Connection conn = DBManager.getDBConnection();
PreparedStatement pstmt = conn.prepareStatement(sql)) {

pstmt.setInt(1, cid);
ResultSet rs = pstmt.executeQuery();
if(rs.next()) {
request.setAttribute("customer", rs);
RequestDispatcher dispatcher = request.getRequestDispatcher("customer_edit.jsp");
dispatcher.forward(request, response);
}
} catch(SQLException e) {
e.printStackTrace();
}
}

private void updateCustomer(HttpServletRequest request, HttpServletResponse response)
throws IOException {
int cid = Integer.parseInt(request.getParameter("cid"));
String cname = request.getParameter("cname");
String email = request.getParameter("email");

String sql = "UPDATE customers SET cname=?, email=? WHERE cid=?";
try (Connection conn = DBManager.getDBConnection();
PreparedStatement pstmt = conn.prepareStatement(sql)) {

pstmt.setString(1, cname);
pstmt.setString(2, email);
pstmt.setInt(3, cid);
pstmt.executeUpdate();
} catch(SQLException e) {
e.printStackTrace();
}
response.sendRedirect("CustomerServlet?action=list");
}
}