package com.company1.dto;

import lombok.*;

@Data
public class CustomerDTO {
	private int cid;       // 고객 ID (PK)
    private String cname;  // 고객 이름
    private String email;  // 고객 이메일

}