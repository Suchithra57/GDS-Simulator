package com.genfare.gds.optionsImpl;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.io.IOUtils;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3URI;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.genfare.cloud.osgi.device.auth.response.URIResponse;
import com.genfare.cloud.osgi.device.auth.response.AwsResponse.AwsCredentials;
import com.genfare.gds.clientrequest.DeviceAuthentication;
import com.genfare.gds.model.Autoload;

public class GdsFilesImpl {

	String bucketName=null;
	AmazonS3 s3Client;
	String autoload_filename="";
	public void getGdsFiles(CommandLine line) {
		
		String[] arguments = line.getOptionValues("gds");
		
		switch (arguments[0]) {
			case "list":
				getCredentials();
				getConfig();
				break;
			case "get":
				switch (arguments[1]) {
					case "file":
						getFile(arguments[2]);
						break;
					case "autoload":
						autoload_filename=arguments[2];
						getFile("autoload");
						break;
				}
				break;
		}
		
	}
	
	private void getCredentials()
	{
		AwsCredentials awsCredentials = DeviceAuthentication.deviceAuthResponse.getAws().getCredentials();
		bucketName=DeviceAuthentication.deviceAuthResponse.getAws().getBucket();
		AWSCredentials credentials = new BasicSessionCredentials(awsCredentials.getAccessKey(),awsCredentials.getSecretKey(),awsCredentials.getSessionId());
//		System.out.println("acceskey :"+awsCredentials.getAccessKey());
//		System.out.println("secretkey :"+awsCredentials.getSecretKey());
//		System.out.println("sessionId:"+awsCredentials.getSessionId());
		s3Client = new AmazonS3Client(credentials);
	}
	
	private void getConfig()
	{
		HashMap<String, URIResponse> data = DeviceAuthentication.deviceAuthResponse.getConfigurations();
		
		for(Map.Entry<String, URIResponse> map:data.entrySet())
		{
			listFiles(s3Client,map.getValue().getS3key());
		}
	}
	
	private void listFiles(AmazonS3 s3Client,String fileName)
	{
		// need to check bucketName
		
		ListObjectsRequest listObjectsRequest = new ListObjectsRequest().withBucketName(bucketName).withPrefix(fileName);
		ObjectListing objects = s3Client.listObjects(listObjectsRequest);
			
		objects.getObjectSummaries()
         .stream()
         .forEach(s3ObjectSummary -> {
        	 System.out.println(s3ObjectSummary.getKey());
        });
		
	}
	
	private void getFile(String fileName)
	{
		HashMap<String, URIResponse> data = DeviceAuthentication.deviceAuthResponse.getConfigurations();
		for (Map.Entry<String, URIResponse> set : data.entrySet()) {
			if (set.getKey().equalsIgnoreCase(fileName)) {
				getCredentials();
				getObjectFile(s3Client,set.getValue().getUri());
				break;
			}
		}
	}
	
	private void getObjectFile(AmazonS3 s3Client,String uri)
	{	
		AmazonS3URI s3url = new AmazonS3URI(uri);
		String bucket = s3url.getBucket().toString();
		String fileKey = s3url.getKey().toString();
		S3Object s3Object =null;
	
		if(fileKey.contains("faretable"))
		{
			String key = s3url.getKey().replace("faretable.xml", "dllv5i.bin");
			System.out.println(key);

			s3Object = s3Client.getObject(new GetObjectRequest(bucket, key));
			InputStream inputStream = new BufferedInputStream(s3Object.getObjectContent());
			try {
				byte[] binstream = IOUtils.toByteArray(inputStream);
				PrintFareStructure printFS = new PrintFareStructure();
				printFS.printFaretableBin(binstream);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(fileKey.contains("autoload"))
		{
			String key = s3url.getKey().replace("autoload.xml", autoload_filename+".bin");
			System.out.println(key);
			s3Object = s3Client.getObject(new GetObjectRequest(bucket, key));
			InputStream inputStream = new BufferedInputStream(s3Object.getObjectContent());
			try {
				byte[] binstream = IOUtils.toByteArray(inputStream);
				Autoload autoload=new Autoload();
				AutoloadBinaryConversion autoloadBinConversion = new AutoloadBinaryConversion();
				if(autoload_filename.equals("gacf"))
				{
					autoload= autoloadBinConversion.binConversion(binstream,"gacf");
				}
				else {
					autoload= autoloadBinConversion.binConversion(binstream,"gaci");
				}
				PrintAutoloadBinary print = new PrintAutoloadBinary();
				print.printAutoloadBin(autoload);
				
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		else {
			String[] exe= fileKey.split("\\.");
			if(exe[1].equals("bin"))
			{
				if(fileKey.contains("TripList"))
				{
					if(fileKey.contains("TripListi"))
					{
						fileKey=fileKey.replace("TripListi","TripsListi");
					}
					else {
						fileKey=fileKey.replace("TripListm","TripsListm");
					}
				}
				s3Object = s3Client.getObject(new GetObjectRequest(bucket, fileKey));
				InputStream inputStream = new BufferedInputStream(s3Object.getObjectContent());
				try {
					byte[] binstream = IOUtils.toByteArray(inputStream);
					List<Integer> fileData= convertBinaryFile(binstream,exe[0].substring(exe[0].length()-1));
					int count=0;
					for(int i=0;i<fileData.size();i++)
					{
						if(i==(fileData.size()-1)) {
							System.out.print(fileData.get(i)+".");
						}
						else {
							System.out.print(fileData.get(i)+", ");
						}
						count++;
						if(count==10)
						{
							count=0;
							System.out.println();
						}
					}
					
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private List<Integer> convertBinaryFile(byte[] binstream,String type)
	{
		List<Integer> binReverse = new ArrayList<>();
		int size = binstream.length-2;
		int startPosition = 0;
		for(int i=0;i<(size/4);i++)
		{
			int j = 0;
			String record = "", data = "";
			for (int k = startPosition; k < (startPosition + 4); k++) {
				switch (j) {
				case 0:
				case 1:
				case 2:
				case 3:
					record += String.format("%02X", binstream[k]);
					break;
				case 4:
					j = 0;
					break;
				}
				j++;

			}
			if (type.equals("m")) {
				// converting hex to decimal by passing base 16
				binReverse.add(Integer.parseInt(record, 16));
			} else {
				data = record.substring(6, 8) + record.substring(4, 6) + record.substring(2, 4)
						+ record.substring(0, 2);
				binReverse.add(Integer.parseInt(data, 16));
			}
			startPosition += 4;
		}
		return binReverse;
	}
	
	
}
