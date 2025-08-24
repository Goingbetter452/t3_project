package com.company1.servlet;

import com.company1.DBManager;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductServlet extends HttpServlet {

    // Servlet이 처음 로드될 때 초기화합니다.
    public void init() {
        // ProductDAO 의존성 제거
    }
    
    // 메모리에 Servlet 객체가 생성되면 init() 메소드가 호출됩니다.
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action"); // action 파라미터를 통해 어떤 작업을 수행할지 결정합니다.
        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "list":
                listProducts(request, response);
                break;
            case "delete":
                deleteProduct(request, response);
                break;
            case "edit":
                editProductForm(request, response);
                break;
            default:
                listProducts(request, response);
                break;
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if ("insert".equals(action)) {
                insertProduct(request, response);
            } else if ("update".equals(action)) {
                updateProduct(request, response);
            } else {
                doGet(request, response);
            }
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }

    // Updated database handling to align with OrderServlet
    private void listProducts(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBManager.getDBConnection();
            String sql = "SELECT pid, pname, price, stock FROM products ORDER BY pname ASC";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            request.setAttribute("productList", rs);
            RequestDispatcher dispatcher = request.getRequestDispatcher("product_list.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager.close(rs, pstmt, conn);
        }
    }

    private void insertProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            String pname = request.getParameter("pname");
            double price = Double.parseDouble(request.getParameter("price"));
            int stock = Integer.parseInt(request.getParameter("stock"));

            conn = DBManager.getDBConnection();
            String sql = "INSERT INTO products(pid, pname, price, stock) VALUES(products_seq.NEXTVAL, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, pname);
            pstmt.setDouble(2, price);
            pstmt.setInt(3, stock);
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager.close(null, pstmt, conn);
        }

        listProducts(request, response);
    }

    private void deleteProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            int pid = Integer.parseInt(request.getParameter("pid"));
            conn = DBManager.getDBConnection();
            String sql = "DELETE FROM products WHERE pid=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, pid);
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager.close(null, pstmt, conn);
        }

        listProducts(request, response);
    }

    // 상품 정보를 수정하는 메소드
    private void updateProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            int pid = Integer.parseInt(request.getParameter("pid"));
            String pname = request.getParameter("pname");
            double price = Double.parseDouble(request.getParameter("price"));
            int stock = Integer.parseInt(request.getParameter("stock"));

            conn = DBManager.getDBConnection();
            String sql = "UPDATE products SET pname=?, price=?, stock=? WHERE pid=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, pname);
            pstmt.setDouble(2, price);
            pstmt.setInt(3, stock);
            pstmt.setInt(4, pid);
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager.close(null, pstmt, conn);
        }

        listProducts(request, response);
    }

    // 상품 수정 폼을 보여주는 메소드
    private void editProductForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: 상품 수정 폼 구현
        response.getWriter().println("<h2>상품 수정 폼</h2>");
        response.getWriter().println("<p>아직 구현되지 않았습니다.</p>");
    }
}