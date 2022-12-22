package com.genfare.gds.optionsImpl;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Formatter;
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
import com.genfare.cloud.device.common.TicketType;
import com.genfare.cloud.device.common.ValidDayType;
import com.genfare.cloud.device.fare.BadListedCardType;
import com.genfare.cloud.device.fare.FareCellType;
import com.genfare.cloud.device.fare.FareSetType;
import com.genfare.cloud.device.fare.FareTableAPIType;
import com.genfare.cloud.device.fare.HolidayType;
import com.genfare.cloud.osgi.device.auth.response.URIResponse;
import com.genfare.cloud.osgi.device.auth.response.AwsResponse.AwsCredentials;
import com.genfare.gds.clientrequest.DeviceAuthentication;
import com.genfare.gds.model.FaretableTransferDTO;

public class GdsFilesImpl {

	String bucketName=null;
	AmazonS3 s3Client;
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
				}
				break;
		}
		
	}
	
	private void getCredentials()
	{
		AwsCredentials awsCredentials = DeviceAuthentication.deviceAuthResponse.getAws().getCredentials();
		bucketName=DeviceAuthentication.deviceAuthResponse.getAws().getBucket();
		AWSCredentials credentials = new BasicSessionCredentials(awsCredentials.getAccessKey(),awsCredentials.getSecretKey(),awsCredentials.getSessionId());
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
				convertFaretableBin(binstream);
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
	
	private void convertFaretableBin(byte[] binstream)
	{
		FaretableBinConversion  faretableBin= new FaretableBinConversion();
		FareTableAPIType reverseFareTable=faretableBin.reverseConversion(binstream);
		List<FaretableTransferDTO> transferList=faretableBin.transferControl(binstream);
//		int farestructureSize=reverseFareTable.getFareStructures().getFareStructure().size();
		
		System.out.println("\n FareStructure Information \n");
		System.out.println("Fare Structure Effective Date/Time: "+reverseFareTable.getFareStructures().getFareStructure().get(0).getEffectiveDate().getValue());
	
		System.out.println("AltKey :"+reverseFareTable.getFareStructures().getFareStructure().get(0).getAltKey());
		System.out.println("PeakTime1Start :"+reverseFareTable.getFareStructures().getFareStructure().get(0).getPeakTime1Start());
		System.out.println("PeakTime1End :"+reverseFareTable.getFareStructures().getFareStructure().get(0).getPeakTime1End());
		System.out.println("PeakTime2Start :"+reverseFareTable.getFareStructures().getFareStructure().get(0).getPeakTime2Start());
		System.out.println("PeakTime2End :"+reverseFareTable.getFareStructures().getFareStructure().get(0).getPeakTime2End());
		System.out.println("LockCode :"+reverseFareTable.getFareStructures().getFareStructure().get(0).getLockCodeValue());
		System.out.println("LockCodeType :"+reverseFareTable.getFareStructures().getFareStructure().get(0).getLockCode().getId());
			
//		FareSets
		List<FareSetType> faresets=reverseFareTable.getFareStructures().getFareStructure().get(0).getFareSets().getFareSet();
		System.out.println("Faresets size :"+faresets.size());
		for(int i=0;i<faresets.size();i++)
		{
			System.out.println("\n FARE SET #"+(i+1));
			
			List<FareCellType> farecells=faresets.get(i).getFareCells().getFareCell();
			
			Formatter formatter = new Formatter();
		    System.out.println(formatter.format("%10s    |  %10s  |   %10s  |   %10s  |   %10s  |  %10s  |  %10s|  %10s", 
		    		"TTP","Value", "Sound1", "Sound2", "LLight","RLight","TrNdx","Attribute"));
		    
		    System.out.println("--------------------------------------------------------------------------------------------------------------------------");
			
			for(int j=0;j<farecells.size();j++)
			{
				formatter = new Formatter();
				FareCellType cell = farecells.get(j);
				String ttp="";
				if(j==0)
					ttp="FS";
				if(j==1)
					ttp="Preset";
				if(j>=2 && j<16)
					ttp="KEY"+(j-1);
				if(j>=16 && j<=farecells.size())
					ttp="TTP"+(j-15);
			
				System.out.println(formatter.format("%10s    |  %10s  |   %10s  |   %10s  |   %10s  |  %10s  |  %10s|  %10s",ttp,cell.getValue(),cell.getPreSound().getCode(),cell.getPostSound().getCode(),
						cell.getLed1().getCode(),cell.getLed2().getCode(),cell.getDocument().getTicketId(),cell.getAttribute1().getId()));
			}
			if(i<1)
				break;
		}
		
//		MediaList
		
		List<TicketType> tickets = reverseFareTable.getTickets().getTicket();
		System.out.println("\n MediaList");
		Formatter formatter = new Formatter();
	    System.out.println(formatter.format("%10s    |  %10s  |   %10s  |   %10s  |   %10s","ID","Group","Designator","Text","Valid Days")); 
		System.out.println("------------------------------------------------------------------------------------");
		
		for(int t=0;t<tickets.size();t++)
		{
			TicketType ticket = tickets.get(t);
			formatter = new Formatter();
			List<String> validDays = new ArrayList<String>();
			for(ValidDayType days:ticket.getValidDays().getValidDay())
			{
				validDays.add(days.getDescription());
			}
			
			 System.out.println(formatter.format("%10s    |  %10s  |   %10s  |   %10s  |   %10s",(t+1),ticket.getGroup(),ticket.getDesignator(),ticket.getFareboxConfiguration().getDisplayText(),validDays)); 
		
		}
		
		
		
//		Holiday 
		List<HolidayType> holidays = reverseFareTable.getHolidays().getHoliday();
		System.out.println("\n Holiday \n");
		
		Formatter formatter1 = new Formatter();
		 System.out.println(formatter1.format("%10s    |  %10s  |   %10s|  %10s","ID","Control","Date","Month")); 
		System.out.println("----------------------------------------------------------------------");
		for(int h=0;h<holidays.size();h++)
		{
			formatter1 = new Formatter();
			if(holidays.get(h).getHolidayDate()!=null)
			{
				System.out.println(formatter1.format("%10s    |  %10s  |   %10s|  %10s",(h+1),15,holidays.get(h).getHolidayDate().getValue().getDay(),holidays.get(h).getHolidayDate().getValue().getMonth()));
			}
			else {
				System.out.println(formatter1.format("%10s    |  %10s  |   %10s|  %10s",(h+1),15,0,0));
				
			}
		}
		
		
		
//		Transfer Control
		System.out.println("\n Transfer Control \n");
		Formatter formatter2 = new Formatter();
	    System.out.println(formatter2.format("%5s    |  %5s  |   %5s  |  %5s  |  %5s  |  %6s  |  %15s  |  %6s  |  %6s  |  %6s  |  %6s  |  %6s  |  %6s  |  %6s  |  %6s",
	    		"ID","Group","Desig","Trips","Exp Res","Exp Off","Text", "ODDR","ODSR","SDDR","SDSR","cap","hold","payreq","upgred")); 
		System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		for(int a=0;a<transferList.size();a++)
		{
			formatter2 = new Formatter();
			System.out.println(formatter2.format("%5s    |  %5s  |   %5s  |  %5s  |  %6s  |  %6s  |  %17s  |  %6s  |  %6s  |  %6s  |  %6s  |  %6s  |  %6s  |  %6s  |  %6s",
					transferList.get(a).getTransferIndex(),transferList.get(a).getGroup(),transferList.get(a).getDesignator(),transferList.get(a).getTrips()
					,transferList.get(a).getExpResolution(),transferList.get(a).getExpOffset(),transferList.get(a).getText(),transferList.get(a).getOppDirDifRoute(),
					transferList.get(a).getOppDirSmRoute(),transferList.get(a).getSameDirDifRoute(),transferList.get(a).getSameDirSmRoute(),transferList.get(a).getCapFlag()
					,transferList.get(a).getHoldFlag(),transferList.get(a).getPayreqFlag(),transferList.get(a).getUpgredFlag()));
		}
		
		
//		BADLIST RANGE
		
		System.out.println("\n BADLIST RANGE ");
		Formatter formatter3 = new Formatter();
	    System.out.println(formatter3.format("%5s    |  %5s  |   %5s  |  %5s  |  %5s  |  %5s  |  %5s  |  %10s  |  %10s",
	    		"ID","Group","Desig","Sec.Code","Agency","MID","TBPC","Start", "Stop")); 
		
		System.out.println("--------------------------------------------------------------------------------------------------------");
		List<BadListedCardType> badlists = reverseFareTable.getBadListedCards().getBadListedCard();
		for(int b=0;b<badlists.size();b++)
		{
			formatter3 = new Formatter();
			 System.out.println(formatter3.format("%5s    |  %5s  |   %5s  |  %7s  |  %7s  |  %5s  |  %5s  |  %10s  |  %10s",
					 (b+1),	badlists.get(b).getGroup(),	badlists.get(b).getDesignator(),badlists.get(b).getSecurityCode(),badlists.get(b).getAgencyID(),
					 badlists.get(b).getManufactureID(),badlists.get(b).getThirdPartyNumber(),badlists.get(b).getSequence1(),badlists.get(b).getSequence2()));
			
		}
	}
}
