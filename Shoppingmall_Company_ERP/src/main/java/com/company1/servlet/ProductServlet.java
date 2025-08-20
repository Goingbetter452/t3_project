package com.company1.servlet;

import com.company1.DBManager;
import com.company1.dao.ProductDAO;
import com.company1.dto.ProductDTO;
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
import java.util.List;

public class ProductServlet extends HttpServlet {

    private ProductDAO productDAO;
    
    // Servlet이 처음 로드될 때 ProductDAO 객체를 초기화합니다.
    public void init() {
        productDAO = new ProductDAO();
    }
    
    // 메모리에 Servlet 객체가 생성되면 init() 메소드가 호출되어 ProductDAO 객체를 초기화합니다.
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action"); // action 파라미터를 통해 어떤 작업을 수행할지 결정합니다.
        if (action == null) {
            action = "list";
        }

        try {
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
        } catch (SQLException ex) {
            throw new ServletException(ex);
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
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void listProducts(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        List<ProductDTO> productList = productDAO.getAllProducts();
        request.setAttribute("productList", productList);
        RequestDispatcher dispatcher = request.getRequestDispatcher("product_list.jsp");
        dispatcher.forward(request, response);
    }

    private void insertProduct(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        String pname = request.getParameter("pname");
        double price = Double.parseDouble(request.getParameter("price"));
        int stock = Integer.parseInt(request.getParameter("stock"));

        ProductDTO newProduct = new ProductDTO();
        newProduct.setPname(pname);
        newProduct.setPrice(price);
        newProduct.setStock(stock);
        productDAO.addProduct(newProduct);
        response.sendRedirect("ProductServlet?action=list");
    }

    private void deleteProduct(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int pid = Integer.parseInt(request.getParameter("pid"));
        productDAO.deleteProduct(pid);
        response.sendRedirect("ProductServlet?action=list");
    }

    // 상품 수정 폼을 보여주는 메소드
    private void editProductForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int pid = Integer.parseInt(request.getParameter("pid"));
        ProductDTO existingProduct = productDAO.getProductById(pid); // 상품 ID로 상품 정보를 조회합니다.
        RequestDispatcher dispatcher = request.getRequestDispatcher("product_edit.jsp");
        request.setAttribute("product", existingProduct);
        dispatcher.forward(request, response);
    }

    // 상품 정보를 수정하는 메소드
    private void updateProduct(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int pid = Integer.parseInt(request.getParameter("pid"));
        String pname = request.getParameter("pname");
        double price = Double.parseDouble(request.getParameter("price"));
        int stock = Integer.parseInt(request.getParameter("stock"));

        ProductDTO product = new ProductDTO();
        product.setPid(pid);
        product.setPname(pname);
        product.setPrice(price);
        product.setStock(stock);
        productDAO.updateProduct(product);
        response.sendRedirect("ProductServlet?action=list");
    }
}
