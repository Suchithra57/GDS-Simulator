package com.genfare.gds.optionsImpl;

import java.util.Properties;

import org.apache.commons.cli.CommandLine;

import com.genfare.gds.main.EnvironmentSetting;
import com.genfare.gds.util.PropertiesRetrieve;
import com.genfare.gds.util.ValidateNoOfOptions;

public class SetOptionsImpl{

	public void settingEnv(CommandLine line) {
		String[] arguments = line.getOptionValues("set");
		ValidateNoOfOptions validateNoOfOptions = new ValidateNoOfOptions();
		if (validateNoOfOptions.isValidate3(arguments)) {
			switch (arguments[0]) {
			case "env": PropertiesRetrieve propertiesRetrieve = new PropertiesRetrieve();
			 Properties property = propertiesRetrieve.getProperties(); 
				String environment = property.toString();
				if (environment.contains(arguments[1] + "." + arguments[2])) {
					DeviceAuthOptImpl deviceAuthOptImpl = new DeviceAuthOptImpl();
					EnvironmentSetting.setTenant(arguments[1]);
					EnvironmentSetting.setEnv(arguments[2]);
					deviceAuthOptImpl.authenticate();
					
				} else {
					System.out.println("environment doesn't exist");
				}
			break;
			default:System.out.println("not a valid option: "+arguments[0]);
			}
		} else {
			System.out.println("must have option(env) and two arguments <tenant> <environment>");

		}
	}

}
