package com.genfare.gds.model;

public class AutoloadSectionHeaders {

	private	int SectionType;
	private	int size;
	private int count;
	private int offset;
	
	public AutoloadSectionHeaders(int sectionType, int size, int count, int offset) {
		super();
		SectionType = sectionType;
		this.size = size;
		this.count = count;
		this.offset = offset;
	}

	public AutoloadSectionHeaders() {
		// TODO Auto-generated constructor stub
	}

	public int getSectionType() {
		return SectionType;
	}

	public void setSectionType(int sectionType) {
		SectionType = sectionType;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}
	
	
}
