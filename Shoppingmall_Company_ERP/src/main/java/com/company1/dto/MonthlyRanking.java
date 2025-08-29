package com.company1.dto;

public class MonthlyRanking {
private String month;
private String productName;
private double totalSales;

public MonthlyRanking(String month, String productName, double totalSales) {
	this.month=month;
	this.productName=productName;
	this.totalSales=totalSales;
}
public String getMonth() {
	return month;
}
public String getProductName() {
	return productName;
}
public double getTotalSales() {
	return totalSales;
}



}
