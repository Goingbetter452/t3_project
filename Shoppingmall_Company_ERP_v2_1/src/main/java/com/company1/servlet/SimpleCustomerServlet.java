package com.company1.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.company1.dao.CustomerDAO;
import com.company1.dto.CustomerDTO;

public class SimpleCustomerServlet {
    
    private CustomerDAO customerDAO = new CustomerDAO();
    
    public void handleRequest(String method, String queryString, PrintWriter out) throws IOException {
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>고객 관리 시스템</title>");
        out.println("<meta charset='UTF-8'>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; margin: 20px; }");
        out.println("table { border-collapse: collapse; width: 100%; }");
        out.println("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
        out.println("th { background-color: #f2f2f2; }");
        out.println("form { margin: 20px 0; }");
        out.println("input[type=text], input[type=email] { padding: 5px; margin: 5px; }");
        out.println("input[type=submit] { padding: 8px 16px; background-color: #4CAF50; color: white; border: none; cursor: pointer; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        
        if ("GET".equals(method)) {
            handleGetRequest(queryString, out);
        } else if ("POST".equals(method)) {
            handlePostRequest(queryString, out);
        }
        
        out.println("</body>");
        out.println("</html>");
    }
    
    private void handleGetRequest(String queryString, PrintWriter out) {
        String command = extractParameter(queryString, "command");
        if (command == null) command = "list";
        
        switch (command) {
            case "list":
                showCustomerList(out);
                break;
            case "form":
                showCustomerForm(out);
                break;
            default:
                showCustomerList(out);
                break;
        }
    }
    
    private void handlePostRequest(String queryString, PrintWriter out) {
        String command = extractParameter(queryString, "command");
        
        if ("insert".equals(command)) {
            // 실제로는 POST 데이터를 파싱해야 하지만, 여기서는 간단하게 처리
            out.println("<h2>고객 등록 처리</h2>");
            out.println("<p>고객 등록이 완료되었습니다.</p>");
            out.println("<a href='?command=list'>목록으로 돌아가기</a>");
        }
    }
    
    private void showCustomerList(PrintWriter out) {
        out.println("<h1>고객 관리 시스템</h1>");
        out.println("<a href='?command=form'>새 고객 등록</a>");
        
        try {
            List<CustomerDTO> customerList = customerDAO.getAllCustomers();
            
            out.println("<h2>고객 목록</h2>");
            out.println("<table>");
            out.println("<tr><th>고객 ID</th><th>이름</th><th>이메일</th></tr>");
            
            if (customerList != null && !customerList.isEmpty()) {
                for (CustomerDTO customer : customerList) {
                    out.println("<tr>");
                    out.println("<td>" + customer.getCid() + "</td>");
                    out.println("<td>" + customer.getCname() + "</td>");
                    out.println("<td>" + customer.getEmail() + "</td>");
                    out.println("</tr>");
                }
            } else {
                out.println("<tr><td colspan='3'>등록된 고객이 없습니다.</td></tr>");
            }
            
            out.println("</table>");
            
        } catch (Exception e) {
            out.println("<p style='color: red;'>오류 발생: " + e.getMessage() + "</p>");
            e.printStackTrace();
        }
    }
    
    private void showCustomerForm(PrintWriter out) {
        out.println("<h1>고객 등록</h1>");
        out.println("<form method='post' action='?command=insert'>");
        out.println("<div>");
        out.println("<label for='cname'>이름:</label>");
        out.println("<input type='text' id='cname' name='cname' required>");
        out.println("</div>");
        out.println("<div>");
        out.println("<label for='email'>이메일:</label>");
        out.println("<input type='email' id='email' name='email'>");
        out.println("</div>");
        out.println("<input type='submit' value='등록하기'>");
        out.println("</form>");
        out.println("<a href='?command=list'>목록으로 돌아가기</a>");
    }
    
    private String extractParameter(String queryString, String paramName) {
        if (queryString == null) return null;
        
        String[] params = queryString.split("&");
        for (String param : params) {
            String[] keyValue = param.split("=");
            if (keyValue.length == 2 && paramName.equals(keyValue[0])) {
                return keyValue[1];
            }
        }
        return null;
    }
}
