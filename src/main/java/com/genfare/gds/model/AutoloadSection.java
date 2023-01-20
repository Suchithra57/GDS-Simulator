package com.genfare.gds.model;

import java.util.List;


public class AutoloadSection {
	
	private List<AutoloadSectionHeaders> autoloadSectionHeaders;
	private List<AutoloadUserType> autoloadUserType;
	private List<AutoloadFare> autoloadFare;
	private List<AutoloadParam> autoloadParam;
	private List<AutoloadRange> autoloadRange;
	private List<AutoloadIndividual> autoloadIndividual;
	
	
	
	public AutoloadSection(List<AutoloadSectionHeaders> autoloadSectionHeaders, List<AutoloadUserType> autoloadUserType,
			List<AutoloadFare> autoloadFare, List<AutoloadParam> autoloadParam, List<AutoloadRange> autoloadRange,
			List<AutoloadIndividual> autoloadIndividual) {
		super();
		this.autoloadSectionHeaders = autoloadSectionHeaders;
		this.autoloadUserType = autoloadUserType;
		this.autoloadFare = autoloadFare;
		this.autoloadParam = autoloadParam;
		this.autoloadRange = autoloadRange;
		this.autoloadIndividual = autoloadIndividual;
	}
	
	public AutoloadSection() {
		// TODO Auto-generated constructor stub
	}

	public List<AutoloadSectionHeaders> getAutoloadSectionHeaders() {
		return autoloadSectionHeaders;
	}
	public void setAutoloadSectionHeaders(List<AutoloadSectionHeaders> autoloadSectionHeaders) {
		this.autoloadSectionHeaders = autoloadSectionHeaders;
	}
	public List<AutoloadUserType> getAutoloadUserType() {
		return autoloadUserType;
	}
	public void setAutoloadUserType(List<AutoloadUserType> autoloadUserType) {
		this.autoloadUserType = autoloadUserType;
	}
	public List<AutoloadFare> getAutoloadFare() {
		return autoloadFare;
	}
	public void setAutoloadFare(List<AutoloadFare> autoloadFare) {
		this.autoloadFare = autoloadFare;
	}
	public List<AutoloadParam> getAutoloadParam() {
		return autoloadParam;
	}
	public void setAutoloadParam(List<AutoloadParam> autoloadParam) {
		this.autoloadParam = autoloadParam;
	}
	public List<AutoloadRange> getAutoloadRange() {
		return autoloadRange;
	}
	public void setAutoloadRange(List<AutoloadRange> autoloadRange) {
		this.autoloadRange = autoloadRange;
	}
	public List<AutoloadIndividual> getAutoloadIndividual() {
		return autoloadIndividual;
	}
	public void setAutoloadIndividual(List<AutoloadIndividual> autoloadIndividual) {
		this.autoloadIndividual = autoloadIndividual;
	}
	
	
}
