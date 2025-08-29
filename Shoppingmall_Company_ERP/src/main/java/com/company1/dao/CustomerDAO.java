package com.company1.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.company1.DBManager;
import com.company1.dto.CustomerDTO;

public class CustomerDAO {

    // 모든 고객 정보를 가져오는 메서드
    public List<CustomerDTO> getAllCustomers() {
        List<CustomerDTO> customerList = new ArrayList<>();
        String sql = "SELECT CID, CNAME, EMAIL FROM CUSTOMERS ORDER BY CID DESC";

        try (Connection conn = DBManager.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                CustomerDTO customer = new CustomerDTO();
                customer.setCid(rs.getInt("CID"));
                customer.setCname(rs.getString("CNAME"));
                customer.setEmail(rs.getString("EMAIL"));
                customerList.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerList; // ResultSet 대신 List<CustomerDTO>를 반환
    }

    // 특정 고객 정보를 가져오는 메서드
    public CustomerDTO getCustomerById(int cid) {
        CustomerDTO customer = null;
        String sql = "SELECT CID, CNAME, EMAIL FROM CUSTOMERS WHERE CID = ?";

        try (Connection conn = DBManager.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, cid);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    customer = new CustomerDTO();
                    customer.setCid(rs.getInt("CID"));
                    customer.setCname(rs.getString("CNAME"));
                    customer.setEmail(rs.getString("EMAIL"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customer;
    }

    // 고객 정보를 추가하는 메서드
    public void addCustomer(CustomerDTO customer) {
        String sql = "INSERT INTO CUSTOMERS(CNAME, EMAIL) VALUES (?, ?)";
        try (Connection conn = DBManager.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, customer.getCname());
            pstmt.setString(2, customer.getEmail());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 고객 정보를 수정하는 메서드
    public void updateCustomer(CustomerDTO customer) {
        String sql = "UPDATE CUSTOMERS SET CNAME = ?, EMAIL = ? WHERE CID = ?";
        try (Connection conn = DBManager.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, customer.getCname());
            pstmt.setString(2, customer.getEmail());
            pstmt.setInt(3, customer.getCid());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 고객 정보를 삭제하는 메서드
    public void deleteCustomer(int cid) {
        String sql = "DELETE FROM CUSTOMERS WHERE CID = ?";
        try (Connection conn = DBManager.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, cid);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}