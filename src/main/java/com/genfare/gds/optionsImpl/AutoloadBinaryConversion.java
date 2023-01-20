package com.genfare.gds.optionsImpl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.genfare.cloud.device.common.DateType;
import com.genfare.gds.model.Autoload;
import com.genfare.gds.model.AutoloadFare;
import com.genfare.gds.model.AutoloadHeader;
import com.genfare.gds.model.AutoloadIndividual;
import com.genfare.gds.model.AutoloadParam;
import com.genfare.gds.model.AutoloadRange;
import com.genfare.gds.model.AutoloadSection;
import com.genfare.gds.model.AutoloadSectionHeaders;
import com.genfare.gds.model.AutoloadUserType;

public class AutoloadBinaryConversion {
	static int startposition=0;
	String autoload_filename="";
	
	public Autoload binConversion(byte[] bin, String filename) {
		startposition=0;
		autoload_filename=filename;
		
		Autoload autoload = new Autoload();
		autoload.setAutoloadHeader(getAutoloadHeaderInfo(bin));
		
		autoload.setAutoloadSection(getAutoloadSectionInfo(bin,autoload.getAutoloadHeader().getSectionCount()));
		return autoload;
		
	}
	
	public AutoloadHeader getAutoloadHeaderInfo(byte[] bin)
	{
		AutoloadHeader header = new AutoloadHeader();
		String crc="",dateTime="",platformid="",seq="";
		for(int i=0;i<24;i++)
		{
			switch (i) {
				case 0:
				case 1:
				case 2:
				case 3:
					crc+=String.format("%02X", bin[i]);
					break;
				case 4:
//					System.out.println("cat :"+bin[i]);
//					System.out.println("cat :"+String.format("%02X", bin[i]));
					header.setCategory((char) bin[i]);
					break;
				case 5:
					header.setFileType(Integer.parseInt(String.format("%02X", bin[i])));
					break;
				case 6:
					header.setVersion(Integer.parseInt(String.format("%02X", bin[i])));
					break;
				case 7:
					header.setFlag(Integer.parseInt(String.format("%02X", bin[i])));
					break;
				case 8:
					header.setPlatformSrc((char) bin[i]);
					break;
				case 9:
					header.setPlatformTarget((char) bin[i]);
					break;
				case 10:
				case 11:
				case 12:
				case 13:
//					header.setPlatformId(Integer.parseInt(String.format("%02X", bin[i])));
					platformid+=String.format("%02X", bin[i]);
					break;
				case 14:
				case 15:
				case 16:
				case 17:
					seq+=String.format("%02X", bin[i]);
					break;
				case 18:
				case 19:
				case 20:
				case 21:
					dateTime+=String.format("%02X", bin[i]);
					break;
				case 22:
					header.setSectionCount(Integer.parseInt(String.format("%02X", bin[i])));
					break;
				case 23:
					header.setSectionFlag(Integer.parseInt(String.format("%02X", bin[i])));
					break;
			}
			
		}
		System.out.println("crc :"+crc.substring(6, 8)+crc.substring(4, 6)+crc.substring(2, 4)+crc.substring(0, 2));
		crc=crc.substring(6, 8)+crc.substring(4, 6)+crc.substring(2, 4)+crc.substring(0, 2);
	
		DateType date;
		if(autoload_filename.equals("gacf"))
		{
			date=getDateTime(Integer.parseInt(dateTime,16));
		}
		else {
			date=getDateTime(reverse(Integer.parseInt(dateTime,16)));
		}
		System.out.println("seq:"+seq);
		header.setPlatformId(Integer.parseInt(platformid,16));
		header.setCreationDate(date);
		header.setCrc(crc);
		startposition+=startposition+24;
		return header;
	}
	
	public AutoloadSection getAutoloadSectionInfo(byte[] bin,int SectionCount)
	{
		AutoloadSection autoloadSection= new AutoloadSection();
		autoloadSection.setAutoloadSectionHeaders(getSectionHeaders(startposition,bin,SectionCount));	
		
		int headersCount=autoloadSection.getAutoloadSectionHeaders().size();
		System.out.println("headersCount :::"+headersCount);
		
		autoloadSection.setAutoloadUserType(getUserTypeSection(bin,startposition,autoloadSection.getAutoloadSectionHeaders().get(0).getSize(),autoloadSection.getAutoloadSectionHeaders().get(0).getCount()));
		autoloadSection.setAutoloadFare(getFareSection(bin,startposition,autoloadSection.getAutoloadSectionHeaders().get(1).getSize(),autoloadSection.getAutoloadSectionHeaders().get(1).getCount()));
		autoloadSection.setAutoloadParam(getParamSection(bin,startposition,autoloadSection.getAutoloadSectionHeaders().get(2).getSize(),autoloadSection.getAutoloadSectionHeaders().get(2).getCount()));
		
		
		int params_size=autoloadSection.getAutoloadSectionHeaders().get(2).getSize();
		int params_count=autoloadSection.getAutoloadSectionHeaders().get(2).getCount();
		int params_total_size=(params_size*params_count);
		int params_extra_size=0;
		if(headersCount==4)
		{
			params_extra_size=autoloadSection.getAutoloadSectionHeaders().get(3).getOffset()-params_total_size;
			startposition=startposition+(params_extra_size-autoloadSection.getAutoloadSectionHeaders().get(2).getOffset());
			autoloadSection.setAutoloadIndividual(getIndividualSection(bin,startposition,autoloadSection.getAutoloadSectionHeaders().get(3).getSize(),autoloadSection.getAutoloadSectionHeaders().get(3).getCount()));
		}
		if(headersCount==5) {
			params_extra_size=autoloadSection.getAutoloadSectionHeaders().get(3).getOffset()-params_total_size;
			startposition=startposition+(params_extra_size-autoloadSection.getAutoloadSectionHeaders().get(2).getOffset());
			
			autoloadSection.setAutoloadRange(getRangeSection(bin,startposition,autoloadSection.getAutoloadSectionHeaders().get(3).getSize(),autoloadSection.getAutoloadSectionHeaders().get(3).getCount()));
			autoloadSection.setAutoloadIndividual(getIndividualSection(bin,startposition,autoloadSection.getAutoloadSectionHeaders().get(4).getSize(),autoloadSection.getAutoloadSectionHeaders().get(4).getCount()));
		}
		return autoloadSection;
	}
	
	private List<AutoloadSectionHeaders> getSectionHeaders(int index,byte[] bin,int secCount)
	{
		List<AutoloadSectionHeaders> sectionHeaders= new ArrayList<>();
		for(int i=0;i<secCount;i++)
		{
			AutoloadSectionHeaders autoloadSectionHeaders=new AutoloadSectionHeaders();
			int k=0;String offset="",count="";
			for(int j=index;j<(index+10);j++)
			{
				switch(k)
				{
					case 0:
						autoloadSectionHeaders.setSectionType(Integer.parseInt(String.format("%02X", bin[j]),16));
						break;
					case 1:
						autoloadSectionHeaders.setSize(bin[j]);
						break;
					case 2:
					case 3:
					case 4:
					case 5:
						count+=String.format("%02X", bin[j]);
						break;
					case 6:
					case 7:
					case 8:
					case 9:
						offset+=String.format("%02X", bin[j]);
						break;
				}
				k++;
				
			}
			if(autoload_filename.equals("gacf"))
			{
				autoloadSectionHeaders.setCount(Integer.parseInt(count,16));
				autoloadSectionHeaders.setOffset(Integer.parseInt(offset,16));
			}
			else {
				autoloadSectionHeaders.setCount(reverse(Long.parseLong(count,16)));
				autoloadSectionHeaders.setOffset(reverse(Long.parseLong(offset,16)));
			}
			sectionHeaders.add(autoloadSectionHeaders);
			index=index+10;
			startposition=startposition+10;
		}
		return sectionHeaders;
	}
	
	public List<AutoloadUserType> getUserTypeSection(byte[] bin, int index, int size, int count) {
		
		List<AutoloadUserType> userTypes = new ArrayList<>();
		for(int i=0;i<count;i++)
		{
			AutoloadUserType usertype=new AutoloadUserType();
			int c=0;
			String fareid="";
			for(int j=index;j<(index+size);j++)
			{
				switch(c)
				{
					case 0:
						usertype.setUserType(Integer.parseInt(String.format("%02X", bin[j])));
						break;
					case 1:
					case 2:
						fareid+=String.format("%02X", bin[j]);
						break;
					case 3:
						usertype.setCardType(Integer.parseInt(String.format("%02X", bin[j])));
						break;
					case 4:
						usertype.setProdType(Integer.parseInt(String.format("%02X", bin[j])));
						break;
					case 5:
						usertype.setDesig(Integer.parseInt(String.format("%02X", bin[j])));
						break;
				}
				c++;
			}
			usertype.setFareId(Integer.parseInt(fareid,16));
			
			index=index+size;
			startposition=startposition+size;
			userTypes.add(usertype);
		}
		
		return userTypes;
	}
	
	
	private List<AutoloadFare> getFareSection(byte[] bin, int index, int size, int count) {
		
		List<AutoloadFare> fares = new ArrayList<>();
		for(int t=0;t<count;t++)
		{
			AutoloadFare autoloadfare =new AutoloadFare();
			int f=0;
			String fareid="",price="",value="",startTime="",endTime="",addTime="";
			for(int i=index;i<(index+size);i++)
			{
				switch(f)
				{
					case 0:
					case 1:
						fareid+=String.format("%02X", bin[i]);
						break;
					case 2:
						autoloadfare.setFareType(Integer.parseInt(String.format("%02X", bin[i]),16)%16);
						autoloadfare.setProdType(((Integer.parseInt(String.format("%02X", bin[i]),16)/16)%4));
						autoloadfare.setActive((Integer.parseInt(String.format("%02X", bin[i]),16)/16)/4);
						break;
					case 3:
						autoloadfare.setDesig(bin[i]);
						break;
					case 4:
					case 5:
					case 6:
					case 7:
						price+=String.format("%02X", bin[i]);
						break;
					case 8:
					case 9:
						value+=String.format("%02X", bin[i]);
						break;
					case 10:
						autoloadfare.setFlag(Integer.parseInt(String.format("%02X", bin[i]),16));
						break;
					case 11:
						autoloadfare.setExpType(Integer.parseInt(String.format("%02X", bin[i])));
						break;
					case 12:
					case 13:
						startTime+=String.format("%02X", bin[i]);
						break;
					case 14:
					case 15:
						endTime+=String.format("%02X", bin[i]);
						break;
					case 16:
					case 17:
						addTime+=String.format("%02X", bin[i]);
						break;
				}
				f++;
			}
			if(autoload_filename.equals("gacf"))
			{
				autoloadfare.setFareId(Integer.parseInt(fareid,16));
				autoloadfare.setPrice(Integer.parseInt(price,16));
				autoloadfare.setValue(Integer.parseInt(value,16));
			}
			else {
				autoloadfare.setFareId(swap2bytes(Integer.parseInt(fareid,16)));
				autoloadfare.setPrice(reverse(Long.parseLong(price,16)));
				autoloadfare.setValue(swap2bytes(Integer.parseInt(value,16)));
			}
			
			autoloadfare.setStartDate(startTime);
			autoloadfare.setEndDate(endTime);
			autoloadfare.setAddTime(addTime);
			index=index+18;
			startposition=startposition+18;
			fares.add(autoloadfare);
		}
		
		return fares;
	}
	
	private List<AutoloadParam> getParamSection(byte[] bin, int index, int size, int count) {
		
		List<AutoloadParam> params = new ArrayList<>();
		for(int p=0;p<count;p++)
		{
			AutoloadParam autoloadParam = new AutoloadParam();
			int a=0;
			String paramName="",value="";
			int offset=0;
			for(int b=index;b<(index+size);b++) {
				switch(a)
				{
					case 0:
					case 1:
					case 2:
					case 3:
					case 4:
					case 5:
					case 6:
					case 7:
					case 8:
					case 9:
					case 10:
					case 11:
					case 12:
					case 13:
					case 14:
					case 15:
					case 16:
					case 17:
					case 18:
					case 19:
					case 20:
					case 21:
					case 22:
					case 23:
					case 24:
					case 25:
					case 26:
					case 27:
					case 28:
					case 29:
					case 30:
					case 31:
						paramName+=(char) bin[b];
						break;
					case 32:
					case 33:
					case 34:
					case 35:
						value+= String.format("%02X", bin[b]);
						break;
					case 36:
					case 37:
					case 38:
					case 39:
						offset+=Integer.parseInt(String.format("%02X", bin[b],16));
						break;
				}
				a++;
			}
			autoloadParam.setName(paramName);
			if(autoload_filename.equals("gacf"))
			{
				autoloadParam.setValue(Integer.parseInt(value,16));
				autoloadParam.setOffset(offset);
			}
			else {
				autoloadParam.setValue(reverse(Long.parseLong(value,16)));
				autoloadParam.setOffset(offset);
			}
				
			
			index=index+size;
			startposition=startposition+size;
			params.add(autoloadParam);
		}
//		startposition=startposition+13;
		return params;
	}
	
	private List<AutoloadRange> getRangeSection(byte[] bin, int index, int size, int count) {

		List<AutoloadRange> ranges = new ArrayList<>();
		for (int i=0;i<count;i++)
		{
			AutoloadRange range = new AutoloadRange();
			int c=0;
			String startPrintId="",endPrintId="",loadseq="",autolaodTypeAndTpbc="",fareid="",value="";
			for(int j=index;j<(index+size);j++)
			{
				switch(c)
				{
					case 0:
					case 1:
					case 2:
					case 3:
					case 4:
					case 5:
					case 6:
					case 7:
						startPrintId+=String.format("%02X", bin[j]);
						break;
					case 8:
					case 9:
					case 10:
					case 11:
					case 12:
					case 13:
					case 14:
					case 15:
						endPrintId+=String.format("%02X", bin[j]);
						break;
					case 16:
						range.setProdId(Integer.parseInt(String.format("%02X", bin[j]),16)%16);
						range.setProdType(Integer.parseInt(String.format("%02X", bin[j]),16));
						break;
					case 17:
						range.setDesig(bin[j]);
						break;
					case 18:
					case 19:
					case 20:
					case 21:
						loadseq+=String.format("%02X", bin[j]);
						break;
					case 22:
					case 23:
//						need to split
						autolaodTypeAndTpbc+=String.format("%02X", bin[j]);
						break;
					case 24:
					case 25:
						fareid+=String.format("%02X", bin[j]);
						break;
					case 26:
					case 27:
						value+=String.format("%02X", bin[j]);
						break;
				}
				c++;
			}
			if(autoload_filename.equals("gacf"))
			{
				range.setStartPrintedId(Long.parseLong(startPrintId,16));
				range.setEndPrintedId(Long.parseLong(endPrintId,16));
				range.setLoadSeq(Integer.parseInt(loadseq,16));
				range.setAutoloadType(Integer.parseInt(autolaodTypeAndTpbc,16)/4096);
				range.setThirdPartyNo(Integer.parseInt(autolaodTypeAndTpbc,16)%4096);
				range.setFareId(Integer.parseInt(fareid,16));
				range.setValue(Integer.parseInt(value,16));
			}
			else {
				range.setStartPrintedId(reverseLong(Long.parseLong(startPrintId,16)));
				range.setEndPrintedId(reverseLong(Long.parseLong(endPrintId,16)));
				range.setLoadSeq(reverse(Integer.parseInt(loadseq,16)));
				int typeAndTpbc=swap2bytes(Integer.parseInt(autolaodTypeAndTpbc,16));
				range.setAutoloadType(typeAndTpbc/4096);
				range.setThirdPartyNo(typeAndTpbc%4096);
				range.setFareId(swap2bytes(Integer.parseInt(fareid,16)));
				range.setValue(swap2bytes(Integer.parseInt(value,16)));
			}
			
			
			index=index+size;
			startposition=startposition+size;
			ranges.add(range);
		}
		
		return ranges;
	}
	
	private List<AutoloadIndividual> getIndividualSection(byte[] bin, int index, int size, int count) {
		
		List<AutoloadIndividual> individuals=new ArrayList<>();
		for(int r=0;r<count;r++)
		{
			AutoloadIndividual autoloadIndividual = new AutoloadIndividual();
			int a=0;
			String tbpc="",fareid="",value="",card_id="";
			for(int b=index;b<(index+size);b++) {
				switch(a)
				{
					case 0:
						autoloadIndividual.setCardType(Integer.parseInt(String.format("%02X", bin[b],16)));
						break;
					case 1:
					case 2:
					case 3:
					case 4:
					case 5:
					case 6:
					case 7:
						card_id+=String.format("%02X", bin[b]);
						break;
						
					case 8:
						autoloadIndividual.setProdId(Integer.parseInt(String.format("%02X", bin[b]),16)%16);
						autoloadIndividual.setProdType(Integer.parseInt(String.format("%02X", bin[b]),16));
						
						break;
					case 9:
						autoloadIndividual.setDesig(bin[b]);
						break;
					case 10:
						autoloadIndividual.setLoadSeq(Integer.parseInt(String.format("%02X", bin[b]),16));
						break;
					case 11:
						autoloadIndividual.setLoadType(Integer.parseInt(String.format("%02X", bin[b]),16));
						break;
					case 12:
					case 13:
						tbpc+=String.format("%02X", bin[b]);
						break;
					case 14:
					case 15:
						fareid+=String.format("%02X", bin[b]);
						break;
					case 16:
					case 17:
						value+=String.format("%02X", bin[b]);
						break;
				}
				a++;
			}
			 /**
		     * add card type in the highest byte position in 8 byte word getCardIdVal() is the combination of card electronic id and
		     * Card Type ElectronicId is cardId = 1239486389691776 Hexadecimal representation of electronic id 0004674e69f62580 CardType
		     * is 1 = 01 So getCardIdVal = cardType + electronic id = 01000000 00000000 + 0004674e 69f62580 = 0104674e69f62580
		     */
			
			if(autoload_filename.equals("gacf"))
			{
				autoloadIndividual.setCardId(Long.parseLong(card_id,16));
				autoloadIndividual.setTbpc(Integer.parseInt(tbpc));
				autoloadIndividual.setFareId(Integer.parseInt(fareid,16));
				autoloadIndividual.setValue(Integer.parseInt(value,16));
			}
			else {
				autoloadIndividual.setCardId(Long.parseLong(card_id,16));
				autoloadIndividual.setTbpc(swap2bytes(Integer.parseInt(tbpc,16)));
				autoloadIndividual.setFareId(swap2bytes(Integer.parseInt(fareid,16)));
				autoloadIndividual.setValue(swap2bytes(Integer.parseInt(value,16)));
			}
			
			index=index+size;
			startposition=startposition+size;
			individuals.add(autoloadIndividual);
		}
		return individuals;
	}
	
	private  DateType getDateTime(int dateTime)
	{
		long etime = dateTime * 1000L;

		Date date = new Date();
		date.setTime(etime);

		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);

		XMLGregorianCalendar xmlGregorianCalendar = null;
		try {
			xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
		DateType dt = new DateType();
		dt.setValue(xmlGregorianCalendar);
		dt.setLocalTime(true);
		return dt;
	}
	
	public int reverse(int x) {
	    ByteBuffer bbuf = ByteBuffer.allocate(8);
	    bbuf.order(ByteOrder.LITTLE_ENDIAN);
	 
	    bbuf.putInt(x);
	    bbuf.order(ByteOrder.BIG_ENDIAN);
	    return bbuf.getInt(0);
	}
	
	public int reverse(long x) {
	    ByteBuffer bbuf = ByteBuffer.allocate(8);
	    bbuf.order(ByteOrder.LITTLE_ENDIAN);
	 
	    bbuf.putLong(x);
	    bbuf.order(ByteOrder.BIG_ENDIAN);
	    return bbuf.getInt(0);
	}
	public long reverseLong(long x) {
	    ByteBuffer bbuf = ByteBuffer.allocate(8);
	    bbuf.order(ByteOrder.LITTLE_ENDIAN);
	 
	    bbuf.putLong(x);
	    bbuf.order(ByteOrder.BIG_ENDIAN);
	    return bbuf.getLong(0);
	}
	public int swap2bytes(int x) {
	    return (((x << 8) & 0xff00) | ((x >> 8) & 0x00ff));
	}

}
