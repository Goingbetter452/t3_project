package com.company1.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.company1.DBManager;
import com.company1.dto.CustomerDTO;

public class CustomerDAO {
	
	
	   
    // 고객 목록 조회
    public List<CustomerDTO> getAllCustomers() {
        List<CustomerDTO> list = new ArrayList<>();
        String sql = "SELECT cid, cname, email FROM customers ORDER BY cid";

        try (Connection conn = DBManager.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                CustomerDTO dto = new CustomerDTO();
                dto.setCid(rs.getInt("cid"));
                dto.setCname(rs.getString("cname"));
                dto.setEmail(rs.getString("email"));
                list.add(dto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 고객 등록
    public void insertCustomer(String cname, String email) {
        String sql = "INSERT INTO customers (cname, email) VALUES (?, ?)";

        try (Connection conn = DBManager.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, cname);
            pstmt.setString(2, email);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 고객 삭제
    public void deleteCustomer(int cid) {
        String sql = "DELETE FROM customers WHERE cid=?";

        try (Connection conn = DBManager.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, cid);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 고객 상세 조회
    public CustomerDTO getCustomerById(int cid) {
        String sql = "SELECT cid, cname, email FROM customers WHERE cid=?";
        CustomerDTO dto = null;

        try (Connection conn = DBManager.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, cid);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                dto = new CustomerDTO();
                dto.setCid(rs.getInt("cid"));
                dto.setCname(rs.getString("cname"));
                dto.setEmail(rs.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dto;
    }

    // 고객 수정
    public void updateCustomer(CustomerDTO dto) {
        String sql = "UPDATE customers SET cname=?, email=? WHERE cid=?";

        try (Connection conn = DBManager.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, dto.getCname());
            pstmt.setString(2, dto.getEmail());
            pstmt.setInt(3, dto.getCid());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
