package com.sk.pp.regression;

import java.io.File;
import java.util.*;

import javax.xml.bind.*;

import org.apache.log4j.Logger;

import com.sk.pp.generated.beans.*;
import com.sk.pp.generated.beans.Feed.Entry;
import com.sk.pp.kdb.feedhandler.FeedHandler;


public class OneTimeDataLoadToKDB 
{
	Logger log = Logger.getLogger(OneTimeDataLoadToKDB.class);
	public OneTimeDataLoadToKDB(List<String> fileNames)
	{
		init(fileNames);
	}
	
	public static void init(List<String> fileNames) {
		try {
			for (String fileName : fileNames) {
				File file = new File(fileName);
				JAXBContext jaxbContext = JAXBContext.newInstance("com.sk.pp.generated.beans");
				Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
				Feed prop = (Feed) jaxbUnmarshaller.unmarshal(file);
				Map<String, Map<String, Double>> rateMap = new HashMap<String, Map<String, Double>>();
				for(int i=0; i< prop.getEntry().size(); i++)
				{
					Map<String, Double> termMap = new HashMap<String, Double>();
					Entry entry = prop.getEntry().get(i);
					termMap.put("2Y", entry.getContent().getProperties().getBC2YEAR().getValue().doubleValue());
					termMap.put("3Y", entry.getContent().getProperties().getBC3YEAR().getValue().doubleValue());
					termMap.put("5Y", entry.getContent().getProperties().getBC5YEAR().getValue().doubleValue());
					termMap.put("7Y", entry.getContent().getProperties().getBC7YEAR().getValue().doubleValue());
					termMap.put("10Y", entry.getContent().getProperties().getBC10YEAR().getValue().doubleValue());
					termMap.put("30Y", entry.getContent().getProperties().getBC30YEAR().getValue().doubleValue());
					rateMap.put(entry.getContent().getProperties().getNEWDATE().getValue().toString(), termMap);
					
				}
				FeedHandler handler = FeedHandler.getInstance();
				handler.rateUpdate(rateMap);
				
			}

		} catch (Exception e) {
			
		}

	}
   }
