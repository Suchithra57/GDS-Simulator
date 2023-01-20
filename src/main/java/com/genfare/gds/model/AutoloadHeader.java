package com.genfare.gds.model;

import com.genfare.cloud.device.common.DateType;

public class AutoloadHeader {

	private String crc;
	private char category;
	private int fileType;
	private int version;
	private int flag;
	private char platformSrc;
	private char platformTarget;
	private int platformId;
	private int seq; //Autoload package ID
	private DateType creationDate;
	private int sectionCount;
	private int sectionFlag;
	
	public AutoloadHeader(String crc, char category, int fileType, int version, int flag, char platformSrc,
			char platformTarget, int platformId, int seq, DateType creationDate, int sectionCount, int sectionFlag) {
		super();
		this.crc = crc;
		this.category = category;
		this.fileType = fileType;
		this.version = version;
		this.flag = flag;
		this.platformSrc = platformSrc;
		this.platformTarget = platformTarget;
		this.platformId = platformId;
		this.seq = seq;
		this.creationDate = creationDate;
		this.sectionCount = sectionCount;
		this.sectionFlag = sectionFlag;
	}

	public AutoloadHeader() {
		// TODO Auto-generated constructor stub
	}

	public String getCrc() {
		return crc;
	}

	public void setCrc(String crc2) {
		this.crc = crc2;
	}

	public char getCategory() {
		return category;
	}

	public void setCategory(char category) {
		this.category = category;
	}

	public int getFileType() {
		return fileType;
	}

	public void setFileType(int fileType) {
		this.fileType = fileType;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public char getPlatformSrc() {
		return platformSrc;
	}

	public void setPlatformSrc(char platformSrc) {
		this.platformSrc = platformSrc;
	}

	public char getPlatformTarget() {
		return platformTarget;
	}

	public void setPlatformTarget(char platformTarget) {
		this.platformTarget = platformTarget;
	}

	public int getPlatformId() {
		return platformId;
	}

	public void setPlatformId(int platformId) {
		this.platformId = platformId;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public DateType getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(DateType creationDate) {
		this.creationDate = creationDate;
	}

	public int getSectionCount() {
		return sectionCount;
	}

	public void setSectionCount(int sectionCount) {
		this.sectionCount = sectionCount;
	}

	public int getSectionFlag() {
		return sectionFlag;
	}

	public void setSectionFlag(int sectionFlag) {
		this.sectionFlag = sectionFlag;
	}
	
}
