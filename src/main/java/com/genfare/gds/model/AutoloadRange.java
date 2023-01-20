package com.genfare.gds.model;

public class AutoloadRange {

	private Long startPrintedId;
	private Long endPrintedId;
	private int prodId;
	private int prodType;
	private int desig;
	private int loadSeq;
	private int autoloadType;
	private int thirdPartyNo;
	private int fareId;
	private int value;
	
	public AutoloadRange(Long startPrintedId, Long endPrintedId, int prodId, int prodType, int desig, int loadSeq,
			int autoloadType, int thirdPartyNo, int fareId, int value) {
		super();
		this.startPrintedId = startPrintedId;
		this.endPrintedId = endPrintedId;
		this.prodId = prodId;
		this.prodType = prodType;
		this.desig = desig;
		this.loadSeq = loadSeq;
		this.autoloadType = autoloadType;
		this.thirdPartyNo = thirdPartyNo;
		this.fareId = fareId;
		this.value = value;
	}

	public AutoloadRange() {
		// TODO Auto-generated constructor stub
	}

	public Long getStartPrintedId() {
		return startPrintedId;
	}

	public void setStartPrintedId(Long startPrintedId) {
		this.startPrintedId = startPrintedId;
	}

	public Long getEndPrintedId() {
		return endPrintedId;
	}

	public void setEndPrintedId(Long endPrintedId) {
		this.endPrintedId = endPrintedId;
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

	public int getAutoloadType() {
		return autoloadType;
	}

	public void setAutoloadType(int autoloadType) {
		this.autoloadType = autoloadType;
	}

	public int getThirdPartyNo() {
		return thirdPartyNo;
	}

	public void setThirdPartyNo(int thirdPartyNo) {
		this.thirdPartyNo = thirdPartyNo;
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
