package com.company1.dto;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {
	private int cid;
	private String cname;
	private String email;
}
