package com.genfare.gds.model;

public class AutoloadParam {

	private String name;
	private int value;
	private int offset;
	
	public AutoloadParam(String name, int value, int offset) {
		super();
		this.name = name;
		this.value = value;
		this.offset = offset;
	}

	public AutoloadParam() {
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}
	
	
}
