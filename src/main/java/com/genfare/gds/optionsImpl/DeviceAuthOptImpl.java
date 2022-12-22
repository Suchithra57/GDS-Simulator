package com.genfare.gds.optionsImpl;

import java.util.Properties;

import com.genfare.gds.clientrequest.DeviceAuthentication;
import com.genfare.gds.main.EnvironmentSetting;
import com.genfare.gds.util.PropertiesRetrieve;



public class DeviceAuthOptImpl {

	
	public String authenticate() {
		
		PropertiesRetrieve propertiesRetrieve = new PropertiesRetrieve();
		Properties property = propertiesRetrieve.getProperties(); 
		String tenant=EnvironmentSetting.getTenant().toLowerCase();
//		String serialNumber = property.getProperty(tenant+"."+EnvironmentSetting.getEnv()+".fbxno");
//		String password = property.getProperty(tenant+"."+EnvironmentSetting.getEnv()+".pwd");
		String serialNumber =property.getProperty(tenant+"."+EnvironmentSetting.getEnv()+".gds.username");
		String password = property.getProperty(tenant+"."+EnvironmentSetting.getEnv()+".gds.password");
		DeviceAuthentication deviceAuthentication = new DeviceAuthentication();
		String deviceAuthToken = deviceAuthentication.authenticate(serialNumber,password);
		if(deviceAuthToken != null)
		{
			EnvironmentSetting.setTenant(tenant);
			EnvironmentSetting.setFbSerialNumber(serialNumber);
			EnvironmentSetting.setFbPassword(password);
			return "authentication successfull";
		}
		return "failed to authenticate";
	}
	
	
	
}
