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
import com.sk.pp.kdb.feedhandler.KDBConnUtil;

public class OTRPriceListener implements PriceListener{
	Logger log = Logger.getLogger(OTRPriceListener.class);

	private double[] initialPrices_t1 = {101.99,99.334,102.334}; 
	private double[] initialPrices_t0 = {101.99,99.334,102.334}; 
	
	private RealVector currentPrices = new ArrayRealVector(initialPrices_t1);
	private RealVector previousPrices = new ArrayRealVector(initialPrices_t0);
	
	private static List<String> predictorVariables = Arrays.asList("5YR_MID","10YR_MID", "30YR_MID");	
	private String[] responseVariables =  {"7RY_MID"};
	
	@Override
	public void OnPriceUpdate(PriceUpdate price) {
		String key=price.getAlias();
		if(predictorVariables.contains(key))
		{ 
			int index = predictorVariables.indexOf(key);
			previousPrices.setEntry(index, currentPrices.getEntry(index));
			currentPrices.setEntry(index,price.getPrice());
		   recaliberate();
		}
		
		else
		{
			log.info("Recieved update on an unsupported security");
			
		}
	}

	private void recaliberate() {
		try
		{
		for(String responseVar : responseVariables)
		{
		double newPrice = Double.NaN;
		double[] weights = KDBConnUtil.getWeights(responseVar);
		if(weights!=null)
		{
			RealVector weightVector = new ArrayRealVector(weights);
			newPrice=weightVector.getSubVector(1, 3).dotProduct(currentPrices);
			newPrice=newPrice + weights[0];
		}
		log.info("Predicted Price for alias : " + responseVar + " is " + newPrice) ;
		}
		}
		catch(Exception ex)
		{
			log.error("Error recaliberating",ex);
		}
	}

	

	public List<String> getPredictorVariables() {
		return predictorVariables;
	}

	public static void setPredictorVariables(List<String> predictorVariables) {
		OTRPriceListener.predictorVariables = predictorVariables;
	}

	public String[] getResponseVariables() {
		return responseVariables;
	}

	public void setResponseVariables(String[] responseVariables) {
		this.responseVariables = responseVariables;
	}

}
