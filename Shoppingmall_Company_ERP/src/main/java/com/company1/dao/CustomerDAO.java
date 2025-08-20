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
	public List<CustomerDTO> getAllCustomers() {
		List<CustomerDTO> customerList = new ArrayList<>();
		String sql = "SELECT CID, CNAME, EMAIL FROM CUSTOMERS ORDER BY CID";
		
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
		return customerList;
	}
	
}
