package com.genfare.gds.model;

import com.genfare.cloud.device.common.DateType;

public class AutoloadFare {
	private int fareId;
	private int fareType;
	private int prodType;
	private int active;
	private int desig;
	private int price;
	private int value;
	private	int flag;
	private	int expType;
//	private	DateType startDate;
//	private	DateType endDate;
//	private	DateType addTime;
	
	private String startDate;
	private String endDate;
	private String addTime;

	
	public AutoloadFare(int fareId, int fareType, int prodType, int active, int desig, int price, int value,
			int flag, int expType, String startDate, String endDate, String addTime) {
		super();
		this.fareId = fareId;
		this.fareType = fareType;
		this.prodType = prodType;
		this.active = active;
		this.desig = desig;
		this.price = price;
		this.value = value;
		this.flag = flag;
		this.expType = expType;
		this.startDate = startDate;
		this.endDate = endDate;
		this.addTime = addTime;
	}


	public AutoloadFare() {

	}


	public int getFareId() {
		return fareId;
	}


	public void setFareId(int fareId) {
		this.fareId = fareId;
	}


	public int getFareType() {
		return fareType;
	}


	public void setFareType(int fareType) {
		this.fareType = fareType;
	}


	public int getProdType() {
		return prodType;
	}


	public void setProdType(int prodType) {
		this.prodType = prodType;
	}

	public int getActive() {
		return active;
	}


	public void setActive(int active) {
		this.active = active;
	}

	public int getDesig() {
		return desig;
	}


	public void setDesig(int desig) {
		this.desig = desig;
	}


	public int getPrice() {
		return price;
	}


	public void setPrice(int price) {
		this.price = price;
	}


	public int getValue() {
		return value;
	}


	public void setValue(int value) {
		this.value = value;
	}


	public int getFlag() {
		return flag;
	}


	public void setFlag(int flag) {
		this.flag = flag;
	}


	public int getExpType() {
		return expType;
	}


	public void setExpType(int expType) {
		this.expType = expType;
	}


	public String getStartDate() {
		return startDate;
	}


	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}


	public String getEndDate() {
		return endDate;
	}


	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}


	public String getAddTime() {
		return addTime;
	}


	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}
	
}
