package com.sk.pp.kdb.feedhandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.sk.pp.core.FeedListener;
import com.sk.pp.kdb.feedhandler.c.KException;

public class FeedHandler implements FeedListener {
	
	private static Logger log = Logger.getLogger(FeedHandler.class);
	
	public static FeedHandler feedHandler;
	private static final String[] COL_NAMES = new String[] { "date", "alias", "rate" };

	private c conn;

	public FeedHandler() throws KException, IOException {
		conn = KDBConnUtil.getConnection();
	}
	
	public static FeedHandler getInstance()
	{
		try
		{
		if(feedHandler==null)
		{
			feedHandler = new FeedHandler();
		}
		}
		catch(Exception ex)
		{
			log.error("Failed FeedHander init ", ex);
		}
		return feedHandler;
		
	}

	@Override
	public void rateUpdate(Map<String, Map<String, Double>> rateEvents) {

		

		// loop through filling the columns with data
		
			for (String key : rateEvents.keySet()) {
				try {
					
					Map<String, Double> termRates = rateEvents.get(key);
					
					int numRecords = termRates.size();
					System.out.print("Recieved " + numRecords + " records from ratefeed. ");

					// create the vectors for each column
					String[] date = new String[numRecords];
					String[] alias = new String[numRecords];
					double[] rate = new double[numRecords];
					
					List keySet = new ArrayList(termRates.keySet());
					for(int i=0;i<keySet.size();i++)
					{
						alias[i] = (String) keySet.get(i);
						rate[i] = termRates.get((String) keySet.get(i));
						date[i] =key.substring(0, 10);
					}
					
					Object[] data = new Object[] { date, alias, rate };
					c.Flip tab = new c.Flip(new c.Dict(COL_NAMES, data));
					Object[] updStatement = new Object[] { ".u.upd", "historical_rates", tab };
					try {
						conn.ks(updStatement); // send asynchronously
						System.out.println("Sent " + numRecords + " records to kdb server");
					} catch (Exception e) {
						System.err.println("error sending feed to server.");
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		

		
		// create the table itself from the separate columns

	}

}