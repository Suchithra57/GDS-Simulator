package com.genfare.gds.model;

public class Autoload {

	private AutoloadHeader autoloadHeader; 
	private AutoloadSection autoloadSection;
	
	public Autoload(AutoloadHeader autoloadHeader, AutoloadSection autoloadSection) {
		super();
		this.autoloadHeader = autoloadHeader;
		this.autoloadSection = autoloadSection;
	}

	public Autoload() {
		// TODO Auto-generated constructor stub
	}

	public AutoloadHeader getAutoloadHeader() {
		return autoloadHeader;
	}

	public void setAutoloadHeader(AutoloadHeader autoloadHeader) {
		this.autoloadHeader = autoloadHeader;
	}

	public AutoloadSection getAutoloadSection() {
		return autoloadSection;
	}

	public void setAutoloadSection(AutoloadSection autoloadSection) {
		this.autoloadSection = autoloadSection;
	}
	
	
	
}
