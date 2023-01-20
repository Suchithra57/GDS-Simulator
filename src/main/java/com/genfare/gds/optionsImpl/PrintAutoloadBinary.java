package com.genfare.gds.optionsImpl;

import java.util.Formatter;
import java.util.List;

import com.genfare.gds.model.Autoload;
import com.genfare.gds.model.AutoloadFare;
import com.genfare.gds.model.AutoloadHeader;
import com.genfare.gds.model.AutoloadIndividual;
import com.genfare.gds.model.AutoloadParam;
import com.genfare.gds.model.AutoloadRange;
import com.genfare.gds.model.AutoloadSectionHeaders;

public class PrintAutoloadBinary {

	public void printAutoloadBin(Autoload autoload)
	{
	
	//	Main Header Info
		AutoloadHeader header= autoload.getAutoloadHeader();
		Formatter formatter = new Formatter();
	    System.out.println(formatter.format("%10s    |  %10s  |   %10s  |   %10s  |   %10s  |  %10s  |  %10s|  %30s|  %10s|  %10s", 
	    		"File Category","File Type", "Version", "Flag", "Platform Src","Platform Target","Platform Id","Creation Date & Time","Sections Count","Section Flag"));
	    
	    System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
	    formatter = new Formatter();
	    System.out.println(formatter.format("%13s    |  %10s  |   %10s  |   %10s  |   %12s  |  %15s  |  %11s|  %30s|  %14s|  %10s",header.getCategory(),header.getFileType(),header.getVersion(),header.getFlag(),
				header.getPlatformSrc(),header.getPlatformTarget(),header.getPlatformId(),header.getCreationDate().getValue(),header.getSectionCount(),header.getSectionFlag()));
	  
	    System.out.println("\n");
	  
	    List<AutoloadSectionHeaders> headersList =autoload.getAutoloadSection().getAutoloadSectionHeaders();
	    int headersListSize=headersList.size();
	  
	    // Section Header1 Info
	    AutoloadSectionHeaders sectionHeaders = autoload.getAutoloadSection().getAutoloadSectionHeaders().get(0);
	    Formatter formatter1 = new Formatter();
	    System.out.println(formatter1.format("%10s    :  %5s  |   %10s  :   %5s  |   %10s    :  %5s  |   %10s  :   %5s  |   %10s  :   %5s", 
	    		"Section Index",1,"Section Type",sectionHeaders.getSectionType(), "Size",sectionHeaders.getSize(),"Count",sectionHeaders.getCount(), "Offset",sectionHeaders.getOffset()));
	    
	    System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------");
	    System.out.println();
		  
	    if(sectionHeaders.getCount()>0)
	    {
	//		  AutoloadUserType userTypes=autoload
	    }
	    else {
	    	System.out.println("No data in UserType Section");
	    }
	   
	    System.out.println("\n");
		  
	    // Section Header2 Info
	    sectionHeaders = autoload.getAutoloadSection().getAutoloadSectionHeaders().get(1);
	    Formatter formatter2 = new Formatter();
	    System.out.println(formatter2.format("%10s    :  %5s  |   %10s  :   %5s  |   %10s    :  %5s  |   %10s  :   %5s  |   %10s  :   %5s", 
	    		"Section Index",2,"Section Type",sectionHeaders.getSectionType(), "Size",sectionHeaders.getSize(),"Count",sectionHeaders.getCount(), "Offset",sectionHeaders.getOffset()));
	    
	    System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------");
	    System.out.println();
		  
	    if(sectionHeaders.getCount()>0)
	    {
	    	Formatter formatter21 = new Formatter();
	    	System.out.println(formatter21.format("%10s    |  %10s  |   %10s  |   %10s  |   %10s  |  %10s  |   %10s  |   %10s  |   %10s  |  %10s  |   %10s  |   %10s",
	    			"Fare Id","Fare Type","Prod Type","Active","Designator","Price","Value","Flag","Exp Type","Start Date","End Date","Add Time"));
	
	    	List<AutoloadFare> autoloadFares = autoload.getAutoloadSection().getAutoloadFare();
	    	
	    	for(int i=0;i<autoloadFares.size();i++)
	    	{
	    		formatter21 = new Formatter();
	 	    	System.out.println(formatter21.format("%10s    |  %10s  |   %10s  |   %10s  |   %10s  |  %10s  |   %10s  |   %10s  |   %10s  |  %10s  |   %10s  |   %10s",
	 	    			autoloadFares.get(i).getFareId(),autoloadFares.get(i).getFareType(),autoloadFares.get(i).getProdType(),autoloadFares.get(i).getActive(),autoloadFares.get(i).getDesig(),
	 	    			autoloadFares.get(i).getPrice(),autoloadFares.get(i).getValue(),autoloadFares.get(i).getFlag(),autoloadFares.get(i).getExpType(),autoloadFares.get(i).getStartDate()
	 	    			,autoloadFares.get(i).getEndDate(),autoloadFares.get(i).getAddTime()));
	    	}
	    	
	    }
	    else {
	    	System.out.println("No data in Fares Section");
	    }
	   
	    System.out.println("\n");
	    
	    // Section Header3 Info
	    sectionHeaders = autoload.getAutoloadSection().getAutoloadSectionHeaders().get(2);
	    Formatter formatter3 = new Formatter();
	    System.out.println(formatter3.format("%10s    :  %5s  |   %10s  :   %5s  |   %10s    :  %5s  |   %10s  :   %5s  |   %10s  :   %5s", 
	    		"Section Index",3,"Section Type",sectionHeaders.getSectionType(), "Size",sectionHeaders.getSize(),"Count",sectionHeaders.getCount(), "Offset",sectionHeaders.getOffset()));
	    
	    System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------");
	    System.out.println();
		  
	    if(sectionHeaders.getCount()>0)
	    {
	    	Formatter formatter31 = new Formatter();
	    	System.out.println(formatter31.format("%32s    |  %10s  |   %10s","Name","Value","Offset"));
	
	    	List<AutoloadParam> autoloadParams = autoload.getAutoloadSection().getAutoloadParam();
	    	
	    	for(int i=0;i<autoloadParams.size();i++)
	    	{
	    		formatter31 = new Formatter();
	 	    	System.out.println(formatter31.format("%10s    |  %10s  |   %10s",
	 	    			autoloadParams.get(i).getName(),autoloadParams.get(i).getValue(),autoloadParams.get(i).getOffset()));
	    	}
	    	
	    }
	    else {
	    	System.out.println("No data in Params Section");
	    }
	
	
	    System.out.println("\n");
	    
	    // Section Header4 Info
	    int index = 4;
	    sectionHeaders = autoload.getAutoloadSection().getAutoloadSectionHeaders().get(3);
	    if(sectionHeaders.getSectionType()==3)
	    {
		    Formatter formatter4 = new Formatter();
		    System.out.println(formatter4.format("%10s    :  %5s  |   %10s  :   %5s  |   %10s    :  %5s  |   %10s  :   %5s  |   %10s  :   %5s", 
		    		"Section Index",index,"Section Type",sectionHeaders.getSectionType(), "Size",sectionHeaders.getSize(),"Count",sectionHeaders.getCount(), "Offset",sectionHeaders.getOffset()));
		    
		    System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------");
		    System.out.println();
			  
		    if(sectionHeaders.getCount()>0)
		    {
		    	Formatter formatter41 = new Formatter();
		    	System.out.println(formatter41.format("%15s    |  %15s  |   %10s  |   %10s  |   %10s  |   %10s  |   %10s  |   %10s  |   %10s  |   %10s","StartPrintID","EndPrintID","Prod ID","Prod Type","Designator","Load Seq","Autoload Type","TPBC","Fare Id","Value"));
		
		    	List<AutoloadRange> autoloadRanges = autoload.getAutoloadSection().getAutoloadRange();
		    	
		    	for(int i=0;i<autoloadRanges.size();i++)
		    	{
		    		formatter41 = new Formatter();
		 	    	System.out.println(formatter41.format("%15s    |  %15s  |   %10s  |   %10s  |   %10s  |   %10s  |   %13s  |   %10s  |   %10s  |   %10s",
		 	    			autoloadRanges.get(i).getStartPrintedId(),autoloadRanges.get(i).getEndPrintedId(),autoloadRanges.get(i).getProdId(),autoloadRanges.get(i).getProdType(),
		 	    			autoloadRanges.get(i).getDesig(),autoloadRanges.get(i).getLoadSeq(),autoloadRanges.get(i).getAutoloadType(),autoloadRanges.get(i).getThirdPartyNo(),autoloadRanges.get(i).getFareId(),autoloadRanges.get(i).getValue()));
		    	}
		    	
		    }
		    else {
		    	System.out.println("No data in Ranges Section");
		    }
	    }
	    
	    if(headersListSize==5)
	    {
	    	 sectionHeaders = autoload.getAutoloadSection().getAutoloadSectionHeaders().get(4);
	    	 System.out.println("\n");
	    	 index=index+1;
	    }
	    
	    if(sectionHeaders.getSectionType()==2)
	    {
	    	 Formatter formatter5 = new Formatter();
			 System.out.println(formatter5.format("%10s    :  %5s  |   %10s  :   %5s  |   %10s    :  %5s  |   %10s  :   %5s  |   %10s  :   %5s", 
			    		"Section Index",index,"Section Type",sectionHeaders.getSectionType(), "Size",sectionHeaders.getSize(),"Count",sectionHeaders.getCount(), "Offset",sectionHeaders.getOffset()));
			    
			 System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------");
			 System.out.println();
				  
			 if(sectionHeaders.getCount()>0)
			 {
			   	Formatter formatter51 = new Formatter();
			   	System.out.println(formatter51.format("%20s    |  %10s  |   %10s    |  %10s  |   %10s    |  %10s  |   %10s    |  %10s  |   %10s  |   %10s","Card Id","Card Type","Prod Id","Prod Type","Designator","Load Sequence","Load Type","TBPC","Fare Id","Value"));
			   	List<AutoloadIndividual> autoloadIndividuals = autoload.getAutoloadSection().getAutoloadIndividual();
			    	
			   	for(int i=0;i<autoloadIndividuals.size();i++)
			   	{
			   		formatter51 = new Formatter();
			    	System.out.println(formatter51.format("%20s    |  %10s  |   %10s    |  %10s  |   %10s    |  %13s  |   %10s    |  %10s  |   %10s  |   %10s",
				    			autoloadIndividuals.get(i).getCardId(),autoloadIndividuals.get(i).getCardType(),autoloadIndividuals.get(i).getProdId(),autoloadIndividuals.get(i).getProdType(),autoloadIndividuals.get(i).getDesig(),autoloadIndividuals.get(i).getLoadSeq(),autoloadIndividuals.get(i).getLoadType(),
				    			autoloadIndividuals.get(i).getTbpc(),autoloadIndividuals.get(i).getFareId(),autoloadIndividuals.get(i).getValue()));
			   	}
			    	
			  }
			  else {
			   	System.out.println("No data in Ranges Section");
			  }
	    }
	}
}
