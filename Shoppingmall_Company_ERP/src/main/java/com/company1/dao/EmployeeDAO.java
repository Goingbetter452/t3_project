package com.company1.dao;

import com.company1.DBManager; // 우리가 만든 DBManager
import com.company1.dto.EmployeeDTO; // 직원 바구니
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// 데이터베이스에 접속해 직원관련 SQL(추가, 조회, 수정, 삭제)을 실행하는 역할 담당 [ 쉽게 표현하면 해결사 ]
public class EmployeeDAO {
    
    /**
     * 로그인 처리를 위해 아이디와 비밀번호가 일치하는 사용자가 있는지 확인하는 메서드
     * @param empId 사용자 아이디
     * @param empPw 사용자 비밀번호
     * @return 로그인 성공 시 EmployeeDTO 객체, 실패 시 null
     */
    public EmployeeDTO checkLogin(String empId, String empPw) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        EmployeeDTO emp = null;
        
        // 실제 DB테이블과 컬럼명에 맞게 수정해야하는 구역
        String sql = "SELECT emp_id, emp_name, auth FROM employees WHERE emp_id = ? AND emp_pw = ?";
    
        try {
            conn = DBManager.getDBConnection(); // DBManager로부터 커넥션 획득
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, empId);
            pstmt.setString(2, empPw);
            rs = pstmt.executeQuery();
            
            if (rs.next()) { // 일치하는 사용자가 있다면
                emp = new EmployeeDTO();
                emp.setEmpId(rs.getString("emp_id"));
                emp.setEmpName(rs.getString("emp_name"));
                emp.setAuth(rs.getString("auth"));
                // 로그인 시 필요한 다른 정보들도 DTO에 담아줍니다.
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            DBManager.close(rs, pstmt, conn);	// DB 닫는 코드
        }
        return emp;    
    }
     
     
    /**
     * 모든 직원 목록을 조회하는 메서드 (기존 selectAllEmployees 유지)
     * @return 직원 목록 (List<EmployeeDTO>)
     */
    public List<EmployeeDTO> selectAllEmployees() {
        List<EmployeeDTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM employees ORDER BY emp_id ASC";
         
        try {
            conn = DBManager.getDBConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                EmployeeDTO emp = new EmployeeDTO();
                emp.setEmpId(rs.getString("emp_id"));
                emp.setEmpName(rs.getString("emp_name"));
                emp.setPosition(rs.getString("position"));
                emp.setAuth(rs.getString("auth"));
                
                list.add(emp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager.close(rs, pstmt, conn);	// DB 닫는 코드
        }
        return list;							// 리스트로 돌아가기
    }
    
    /**
     * 그룹웨어 수신자 목록용: 모든 직원의 기본 정보를 조회 (emp_id, emp_name, position, auth)
     */
    public List<EmployeeDTO> getAllEmployees() {
        List<EmployeeDTO> employees = new ArrayList<>();
        String sql = "SELECT emp_id, emp_name, position, auth FROM employees ORDER BY emp_name";
        try (Connection conn = DBManager.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                EmployeeDTO emp = new EmployeeDTO();
                emp.setEmpId(rs.getString("emp_id"));
                emp.setEmpName(rs.getString("emp_name"));
                emp.setPosition(rs.getString("position"));
                emp.setAuth(rs.getString("auth"));
                employees.add(emp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }
     
     
    /**
     * 직원 아이디(empId)를 이용해 특정 직원 한 명의 모든 정보를 조회하는 메소드
     * @param empId 조회할 직원의 아이디
     * @return 직원 정보가 담긴 EmployeeDTO 객체, 해당 직원이 없으면 null
     */
    public EmployeeDTO selectEmployeeById(String empId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        EmployeeDTO emp = null;
        
        String sql = "SELECT * FROM employees WHERE emp_id = ?";
        
        try {
            conn = DBManager.getDBConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, empId);
            rs = pstmt.executeQuery();
            
            if (rs.next()) { // 해당 아이디의 직원이 존재한다면
                emp = new EmployeeDTO();
                emp.setEmpId(rs.getString("emp_id"));
                emp.setEmpPw(rs.getString("emp_pw"));
                emp.setEmpName(rs.getString("emp_name"));
                emp.setPosition(rs.getString("position"));
                emp.setAuth(rs.getString("auth"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager.close(rs, pstmt, conn);
        }
        return emp; // 찾았으면 emp 객체를, 못 찾았으면 null을 반환
    }
     
     
    /**
     * 새로운 직원을 등록하는 메서드
     * @param empDto 등록할 직원의 정보가 담긴 DTO
     */
    public void insertEmployee(EmployeeDTO empDTO) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        String sql = "INSERT INTO employees (emp_id, emp_pw, emp_name, position, auth) VALUES (?, ?, ?, ?, ?)";
        
        try {
            conn = DBManager.getDBConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, empDTO.getEmpId());
            pstmt.setString(2, empDTO.getEmpPw());
            pstmt.setString(3, empDTO.getEmpName());
            pstmt.setString(4, empDTO.getPosition());
            pstmt.setString(5, empDTO.getAuth());
            pstmt.executeUpdate(); // INSERT, UPDATE, DELETE는 executeUpdate() 사용
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager.close(null, pstmt, conn); // ResultSet이 없으므로 첫 인자는 null
        }
    }
     
    // ================== ✨ 여기에 코드를 추가/수정했습니다! ✨ ==================
    
    /**
     * 직원의 정보를 수정하는 메소드
     * @param empDTO 수정할 정보가 담긴 DTO 객체
     * @return 수정 성공 시 1 이상의 값, 실패 시 0
     */
    public int updateEmployee(EmployeeDTO empDTO) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int result = 0;

        // 비밀번호 필드가 비어있는지 여부에 따라 다른 SQL을 사용합니다.
        // 비밀번호를 변경하지 않을 경우, 기존 비밀번호를 유지해야 합니다.
        String sql;
        boolean isPasswordChanged = empDTO.getEmpPw() != null && !empDTO.getEmpPw().isEmpty();
        
        if (isPasswordChanged) {
            // 비밀번호까지 모두 변경하는 경우
            sql = "UPDATE employees SET emp_pw=?, emp_name=?, position=?, auth=? WHERE emp_id=?";
        } else {
            // 비밀번호를 제외한 나머지 정보만 변경하는 경우
            sql = "UPDATE employees SET emp_name=?, position=?, auth=? WHERE emp_id=?";
        }

        try {
            conn = DBManager.getDBConnection();
            pstmt = conn.prepareStatement(sql);

            if (isPasswordChanged) {
                pstmt.setString(1, empDTO.getEmpPw());
                pstmt.setString(2, empDTO.getEmpName());
                pstmt.setString(3, empDTO.getPosition());
                pstmt.setString(4, empDTO.getAuth());
                pstmt.setString(5, empDTO.getEmpId());
            } else {
                pstmt.setString(1, empDTO.getEmpName());
                pstmt.setString(2, empDTO.getPosition());
                pstmt.setString(3, empDTO.getAuth());
                pstmt.setString(4, empDTO.getEmpId());
            }
            
            result = pstmt.executeUpdate(); // UPDATE 쿼리 실행!

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager.close(null, pstmt, conn);
        }
        return result;
    }

    /**
     * 직원 아이디(empId)를 이용해 특정 직원을 삭제하는 메서드
     * @param empId 삭제할 직원의 아이디
     * @return 삭제 성공 시 1 이상의 값, 실패 시 0
     */
    public int deleteEmployee(String empId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int result = 0;
        String sql = "DELETE FROM employees WHERE emp_id = ?";
        
        try {
            conn = DBManager.getDBConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, empId);
            result = pstmt.executeUpdate();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            DBManager.close(null, pstmt, conn);
        }
        return result;
    }

    /**
     * 이름과 이메일로 아이디 찾기 (미사용 시에도 컴파일 에러 방지용 기본 구현)
     */
    public String findIdByNameAndEmail(String name, String email) {
        String foundId = null;
        String sql = "SELECT emp_id FROM employees WHERE emp_name = ? AND emp_email = ?";
        try (Connection conn = DBManager.getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    foundId = rs.getString("emp_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return foundId;
    }
}