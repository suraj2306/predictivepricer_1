package com.sk.pp.regression;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.log4j.Logger;

import com.sk.pp.core.PriceListener;
import com.sk.pp.core.PriceUpdate;

public class OTRPriceListener implements PriceListener{
	Logger log = Logger.getLogger(OTRPriceListener.class);

	private RealVector currentPrices = new ArrayRealVector(3);
	private RealVector previousPrices = new ArrayRealVector(3);
	
	private static String[] predictorVariables = {"5YR_MID","10YR_MID", "30YR_MID"};	
	private String[] responseVariables =  {"7RY_MID"};
	
	@Override
	public void OnPriceUpdate(PriceUpdate price) {
		String key=price.getAlias();
		if(Arrays.asList(predictorVariables).contains(key))
		{
		   AddBasedOnKey(key,price);
		   recaliberate();
		}
		
		else
		{
			log.info("Recieved update on an unsupported security");
			
		}
	}

	private void recaliberate() {
		// TODO Auto-generated method stub
		
	}

	private void AddBasedOnKey(String key, PriceUpdate price) {
		
		List<String> predVariables = Arrays.asList(predictorVariables);
		if(predVariables.contains(key))
		{
			int index=predVariables.indexOf(key);
			if(price.getPrice()!=null)
			{
				if(!Double.isNaN(currentPrices.getEntry(index)))
				{
					previousPrices.addToEntry(index, currentPrices.getEntry(index));
				}
				currentPrices.addToEntry(index, price.getPrice());
				
			}
		}
	}

	public static String[] getPredictorVariables() {
		return predictorVariables;
	}

	public static void setPredictorVariables(String[] predictorVariables) {
		OTRPriceListener.predictorVariables = predictorVariables;
	}

	public String[] getResponseVariables() {
		return responseVariables;
	}

	public void setResponseVariables(String[] responseVariables) {
		this.responseVariables = responseVariables;
	}

}
