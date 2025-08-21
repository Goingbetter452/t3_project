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
        String sql = "SELECT CID, ID, CNAME, EMAIL, PHONE FROM CUSTOMERS ORDER BY CID DESC";

        try (Connection conn = DBManager.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                CustomerDTO customer = new CustomerDTO();
                customer.setCid(rs.getInt("CID"));
                customer.setId(rs.getInt("ID"));
                customer.setCname(rs.getString("CNAME"));
                customer.setEmail(rs.getString("EMAIL"));
                customer.setPhone(rs.getString("PHONE"));
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
        String sql = "SELECT CID, ID, CNAME, EMAIL, PHONE FROM CUSTOMERS WHERE CID = ?";

        try (Connection conn = DBManager.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, cid);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    customer = new CustomerDTO();
                    customer.setCid(rs.getInt("CID"));
                    customer.setId(rs.getInt("ID"));
                    customer.setCname(rs.getString("CNAME"));
                    customer.setEmail(rs.getString("EMAIL"));
                    customer.setPhone(rs.getString("PHONE"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customer;
    }

    // 고객 정보를 추가하는 메서드
    public void addCustomer(CustomerDTO customer) {
        // ID는 실제로는 로그인 세션 등에서 가져와야 하지만, 예시에서는 DTO에 포함된 값을 사용
        String sql = "INSERT INTO CUSTOMERS(ID, CNAME, EMAIL, PHONE) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBManager.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, customer.getId());
            pstmt.setString(2, customer.getCname());
            pstmt.setString(3, customer.getEmail());
            pstmt.setString(4, customer.getPhone());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 고객 정보를 수정하는 메서드
    public void updateCustomer(CustomerDTO customer) {
        String sql = "UPDATE CUSTOMERS SET CNAME = ?, EMAIL = ?, PHONE = ? WHERE CID = ?";
        try (Connection conn = DBManager.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, customer.getCname());
            pstmt.setString(2, customer.getEmail());
            pstmt.setString(3, customer.getPhone());
            pstmt.setInt(4, customer.getCid());
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