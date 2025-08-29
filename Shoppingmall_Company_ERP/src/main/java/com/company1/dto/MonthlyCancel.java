package com.company1.dto;
public class MonthlyCancel{
	private String month;
	private int cancelCount;
	public MonthlyCancel(String month, int cancelCount) {
		this.month=month;
		this.cancelCount=cancelCount;
	}
	public String getMonth() {
		return month;
	}
	public int getCancelCount() {
		return cancelCount;
	}
}