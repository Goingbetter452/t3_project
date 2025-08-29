package com.company1.dto;

import java.sql.Timestamp;

public class OrderDTO {
    private int oid;
    private String customerName;
    private String productName;
    private int quantity;
    private double unitPrice;
    private Timestamp orderDate;

    public OrderDTO(int oid, String customerName, String productName,
                    int quantity, double unitPrice, Timestamp orderDate) {
        this.oid = oid;
        this.customerName = customerName;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.orderDate = orderDate;
    }

    public int getOid() { return oid; }
    public String getCustomerName() { return customerName; }
    public String getProductName() { return productName; }
    public int getQuantity() { return quantity; }
    public double getUnitPrice() { return unitPrice; }
    public Timestamp getOrderDate() { return orderDate; }
}
