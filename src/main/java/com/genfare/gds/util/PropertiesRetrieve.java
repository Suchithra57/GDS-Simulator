package com.genfare.gds.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.genfare.gds.main.EnvironmentSetting;



public class PropertiesRetrieve {
	//private static final Logger log = Logger.getLogger(PropertiesRetrieve.class.getName());
	static Properties prop = new Properties();

	public Properties getProperties() {
		try {
			prop.load(new FileInputStream(EnvironmentSetting.getPropertiesFilePath()));
							
		} catch (IOException ex) {

			ex.printStackTrace();
		}
	
		return prop;
	}
}