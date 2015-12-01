package com.sk.pp.core;

import java.util.Map;

public interface FeedListener {

	public void rateUpdate(Map<String,Map<String, Double>> trades);
	
}
