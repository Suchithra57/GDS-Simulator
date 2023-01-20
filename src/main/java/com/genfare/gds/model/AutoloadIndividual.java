package com.genfare.gds.model;

public class AutoloadIndividual {

	 private Long cardId;
	 private int cardType;
	 private int prodId;
	 private int prodType;
	 private int desig;
	 private int loadSeq;
	 private int loadType;
	 private int tbpc;
	 private int fareId;
	 private int value;
	
	 public AutoloadIndividual(Long cardId, int cardType, int prodId, int prodType, int desig, int loadSeq,
			int loadType, int tbpc, int fareId, int value) {
		super();
		this.cardId = cardId;
		this.cardType = cardType;
		this.prodId = prodId;
		this.prodType = prodType;
		this.desig = desig;
		this.loadSeq = loadSeq;
		this.loadType = loadType;
		this.tbpc = tbpc;
		this.fareId = fareId;
		this.value = value;
	}

	public AutoloadIndividual() {
		// TODO Auto-generated constructor stub
	}

	public Long getCardId() {
		return cardId;
	}

	public void setCardId(Long cardId) {
		this.cardId = cardId;
	}

	public int getCardType() {
		return cardType;
	}

	public void setCardType(int cardType) {
		this.cardType = cardType;
	}

	public int getProdId() {
		return prodId;
	}

	public void setProdId(int prodId) {
		this.prodId = prodId;
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

	public int getLoadSeq() {
		return loadSeq;
	}

	public void setLoadSeq(int loadSeq) {
		this.loadSeq = loadSeq;
	}

	public int getLoadType() {
		return loadType;
	}

	public void setLoadType(int loadType) {
		this.loadType = loadType;
	}

	public int getTbpc() {
		return tbpc;
	}

	public void setTbpc(int tbpc) {
		this.tbpc = tbpc;
	}

	public int getFareId() {
		return fareId;
	}

	public void setFareId(int fareId) {
		this.fareId = fareId;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	 
}
