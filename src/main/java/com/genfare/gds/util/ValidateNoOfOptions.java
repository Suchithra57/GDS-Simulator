package com.genfare.gds.util;

public class ValidateNoOfOptions {
	
	public boolean isValidate3(String[] arguments) {
		if (arguments.length < 3) {
			return false;
		}
		return true;
	}

	
	
	public boolean isValidate2(String[] arguments) {
		if (arguments.length < 2) {
			return false;
		}
		return true;
	}
	
	
	
	public boolean isValidate4(String[] arguments) {
		if (arguments.length < 4) {
			return false;
		}
		return true;
	}
}
