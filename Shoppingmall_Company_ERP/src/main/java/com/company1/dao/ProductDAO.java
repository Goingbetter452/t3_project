//package com.company1.dao;
//
//import com.erp.model.Product;
//import com.company1.DBManager;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class ProductDAO {
//
//    // 상품 추가
//    public void addProduct(Product product) throws SQLException {
//        String sql = "INSERT INTO PRODUCTS (PNAME, PRICE, STOCK) VALUES (?, ?, ?)";
//        try (Connection conn = DBManager.getDBConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//
//            pstmt.setString(1, product.getPname());
//            pstmt.setDouble(2, product.getPrice());
//            pstmt.setInt(3, product.getStock());
//            pstmt.executeUpdate();
//        }
//    }
//
//    // 모든 상품 조회
//    public List<Product> getAllProducts() throws SQLException {
//        List<Product> products = new ArrayList<>();
//        String sql = "SELECT * FROM PRODUCTS ORDER BY PID";
//        
//        try (Connection conn = DBManager.getDBConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql);
//             ResultSet rs = pstmt.executeQuery()) {
//
//            while (rs.next()) {
//                Product product = new Product();
//                product.setPid(rs.getInt("PID"));
//                product.setPname(rs.getString("PNAME"));
//                product.setPrice(rs.getDouble("PRICE"));
//                product.setStock(rs.getInt("STOCK"));
//                products.add(product);
//            }
//        }
//        return products;
//    }
//
//    // 상품 ID로 조회
//    public Product getProductById(int pid) throws SQLException {
//        Product product = null;
//        String sql = "SELECT * FROM PRODUCTS WHERE PID = ?";
//        
//        try (Connection conn = DBManager.getDBConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//
//            pstmt.setInt(1, pid);
//            try (ResultSet rs = pstmt.executeQuery()) {
//                if (rs.next()) {
//                    product = new Product();
//                    product.setPid(rs.getInt("PID"));
//                    product.setPname(rs.getString("PNAME"));
//                    product.setPrice(rs.getDouble("PRICE"));
//                    product.setStock(rs.getInt("STOCK"));
//                }
//            }
//        }
//        return product;
//    }
//
//    // 상품 수정
//    public void updateProduct(Product product) throws SQLException {
//        String sql = "UPDATE PRODUCTS SET PNAME = ?, PRICE = ?, STOCK = ? WHERE PID = ?";
//        try (Connection conn = DBManager.getDBConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//
//            pstmt.setString(1, product.getPname());
//            pstmt.setDouble(2, product.getPrice());
//            pstmt.setInt(3, product.getStock());
//            pstmt.setInt(4, product.getPid());
//            pstmt.executeUpdate();
//        }
//    }
//
//    // 상품 삭제
//    public void deleteProduct(int pid) throws SQLException {
//        String sql = "DELETE FROM PRODUCTS WHERE PID = ?";
//        try (Connection conn = DBManager.getDBConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//
//            pstmt.setInt(1, pid);
//            pstmt.executeUpdate();
//        }
//    }
//}