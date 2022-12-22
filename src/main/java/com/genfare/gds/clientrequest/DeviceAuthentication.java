package com.genfare.gds.clientrequest;

import java.util.Properties;
import java.util.logging.Logger;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.util.Base64;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.genfare.cloud.osgi.device.auth.response.DeviceAuthResponse;
import com.genfare.gds.main.EnvironmentSetting;
import com.genfare.gds.util.PropertiesRetrieve;
import com.genfare.cloud.osgi.device.auth.response.AwsResponse.AwsCredentials;

public class DeviceAuthentication {

	private static final Logger log = Logger.getLogger(DeviceAuthentication.class.getName());

	public static final String AUTH_HEADER_PROPERTY = "Authorization";
	public static final String AUTH_HEADER_PREFIX = "Basic";

	public static DeviceAuthResponse deviceAuthResponse = null;
	static String response = null;
	PropertiesRetrieve propertiesRetrieve = new PropertiesRetrieve();
	Properties prop = propertiesRetrieve.getProperties();

	public static String deviceType = null;
	
	public String authenticate(String fareBoxSerialNumber, String fareBoxPassword) {
		String awsAuthorizationKey = null;
			deviceType = prop.getProperty("gds.deviceType");
//			deviceType = prop.getProperty("deviceType");
		DeviceAuthResponse deviceAuthResponse = postAuthenticate(fareBoxSerialNumber, fareBoxPassword);
		if (deviceAuthResponse != null) {
			AwsCredentials awsCredentials = deviceAuthResponse.getAws().getCredentials();
			String accessKey = awsCredentials.getAccessKey();
			String secretKey = awsCredentials.getSecretKey();
			String sessionId = awsCredentials.getSessionId();
			byte[] authorizationBytes = (accessKey + " | " + secretKey + " | " + sessionId).getBytes();
			awsAuthorizationKey = new String(Base64.encodeBytes(authorizationBytes));
			EnvironmentSetting.setDeviceAuthToken(awsAuthorizationKey);
			System.out.println("AuthoriZation Token :"+awsAuthorizationKey);
		}
		
		return awsAuthorizationKey;

	}

	public static DeviceAuthResponse postAuthenticate(String fareBoxSerialNumber, String fareBoxPassword) {
		String authUrlString;
		
		
		if (EnvironmentSetting.getEnv().equals("qe")) {
			authUrlString = "http://" + "api." + EnvironmentSetting.getEnv() + ".gfcp.io"  + "/services/device/v5/auth" + "?tenant="
					+ EnvironmentSetting.getTenant() + "&type=" + deviceType;
			System.out.println("URL" + authUrlString);

		} else if(EnvironmentSetting.getEnv().equals("local"))
		{
			authUrlString = "http://" + EnvironmentSetting.getEnvironment()+":"+EnvironmentSetting.FUSE_PORT + "/services/device/v5/auth" + "?tenant="
					+ EnvironmentSetting.getTenant() + "&type=" + deviceType;	
		}
		else
		{
		 authUrlString = "https://" + EnvironmentSetting.getEnvironment() + "/services/device/v5/auth" + "?tenant="
					+ EnvironmentSetting.getTenant() + "&type=" + deviceType;
		 System.out.println("authUrlString : "+authUrlString);
		}
		
		byte[] authorizationBytes = (fareBoxSerialNumber + ":" + fareBoxPassword).getBytes();

		String authorizationHeader = AUTH_HEADER_PREFIX + " " + new String(Base64.encodeBytes(authorizationBytes));

		javax.ws.rs.client.Client client = ClientBuilder.newClient();
		try {
			MultivaluedMap<String, Object> head = new MultivaluedHashMap<String, Object>();
			head.add(AUTH_HEADER_PROPERTY, authorizationHeader);
			head.add("Content-Type", "application/json");

			Response response = client.target(authUrlString).request().headers(head).post(null);

			String responseAsString = response.readEntity(String.class);

			if (response.getStatus() != 200 && response.getStatus() != 201) {
				log.info("Failed : HTTP error code : " + response.getStatus() + responseAsString);
			} else {
				EnvironmentSetting.setFbSerialNumber(fareBoxSerialNumber);
				EnvironmentSetting.setFbPassword(fareBoxPassword);

				ObjectMapper mapper2 = new ObjectMapper();
				deviceAuthResponse = mapper2.readValue(responseAsString, DeviceAuthResponse.class);
				System.out.println("Successfully authenticated...");
			}
		}

		catch (Exception e) {
			log.info(e.getMessage());
		} finally {
			if (client != null) {
				client.close();
			}
		}
		return deviceAuthResponse;
	}

}
