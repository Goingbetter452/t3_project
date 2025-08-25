package com.company1.dto;

public class CustomerDTO {
    // CUSTOMERS 테이블의 컬럼과 1:1로 매칭
    private int cid;      // 고객 고유번호
    private String cname;   // 고객 이름
    private String email;   // 고객 이메일

    // 기본 생성자
    public CustomerDTO() {}
    
    // Getter 메서드들
    public int getCid() { return cid; }
    public String getCname() { return cname; }
    public String getEmail() { return email; }
    
    // Setter 메서드들
    public void setCid(int cid) { this.cid = cid; }
    public void setCname(String cname) { this.cname = cname; }
    public void setEmail(String email) { this.email = email; }
}