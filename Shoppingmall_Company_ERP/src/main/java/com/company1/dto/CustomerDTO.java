package com.company1.dto;

import lombok.*;

@Data
@NoArgsConstructor
public class CustomerDTO {
    // CUSTOMERS 테이블의 컬럼과 1:1로 매칭
    private int cid;      // 고객 고유번호
    private int id;       // 사용자 고유번호 (FK)
    private String cname;   // 고객 이름
    private String email;   // 고객 이메일
    private String phone;   // 고객 전화번호

    
}