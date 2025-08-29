package com.company1.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private int pid;
    private String pname;
    private double price;
    private int stock;



    @Override
    public String toString() {
        return "Product{" +
                "pid=" + pid +
                ", pname='" + pname + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                '}';
    }
}