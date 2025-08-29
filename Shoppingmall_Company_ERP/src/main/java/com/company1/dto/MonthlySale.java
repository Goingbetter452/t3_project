package com.company1.dto;
public class MonthlySale{
	private String month;
	private int totalQuantity;
	private double totalSales;
	public MonthlySale(String month,int totalQuantity,double totalSales) {
			this.month=month;
			this.totalQuantity=totalQuantity;
			this.totalSales=totalSales;
		}
	
	
	public String getMonth() {
		return month;
	}
	
	public int getTotalQuantity() {
		return totalQuantity;
	}
	
	public double getTotalSales() {
		return totalSales;
	}
	
	
}