package com.company1.dto;

public class EmployeeDTO {
    private String empId;      // 직원 로그인 ID
    private String empPw;      // 비밀번호
    private String empName;    // 직원명
    private String position;   // 직책
    private String auth;       // 권한 ('admin' 또는 'user')
    
    // 기본 생성자
    public EmployeeDTO() {}
    
    // 매개변수 생성자
    public EmployeeDTO(String empId, String empName, String position, String auth) {
        this.empId = empId;
        this.empName = empName;
        this.position = position;
        this.auth = auth;
    }
    
    // Getters and Setters
    public String getEmpId() {
        return empId;
    }
    
    public void setEmpId(String empId) {
        this.empId = empId;
    }
    
    public String getEmpPw() {
        return empPw;
    }
    
    public void setEmpPw(String empPw) {
        this.empPw = empPw;
    }
    
    public String getEmpName() {
        return empName;
    }
    
    public void setEmpName(String empName) {
        this.empName = empName;
    }
    
    public String getPosition() {
        return position;
    }
    
    public void setPosition(String position) {
        this.position = position;
    }
    
    public String getAuth() {
        return auth;
    }
    
    public void setAuth(String auth) {
        this.auth = auth;
    }
}