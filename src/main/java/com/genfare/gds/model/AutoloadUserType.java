package com.genfare.gds.model;

public class AutoloadUserType {

	private int userType;
	private int fareId;
	private int cardType;
	private int prodType;
	private int desig;
	
	public AutoloadUserType(int userType, int fareId, int cardType, int prodType, int desig) {
		super();
		this.userType = userType;
		this.fareId = fareId;
		this.cardType = cardType;
		this.prodType = prodType;
		this.desig = desig;
	}

	public AutoloadUserType() {
		// TODO Auto-generated constructor stub
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public int getFareId() {
		return fareId;
	}

	public void setFareId(int fareId) {
		this.fareId = fareId;
	}

	public int getCardType() {
		return cardType;
	}

	public void setCardType(int cardType) {
		this.cardType = cardType;
	}

	public int getProdType() {
		return prodType;
	}

	public void setProdType(int prodType) {
		this.prodType = prodType;
	}

	public int getDesig() {
		return desig;
	}

	public void setDesig(int desig) {
		this.desig = desig;
	}
	
	
}
