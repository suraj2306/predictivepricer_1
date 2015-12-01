package com.sk.pp.kdb.feedhandler;

import java.io.IOException;

import com.sk.pp.kdb.feedhandler.c.KException;

public class KDBConnUtil {

	public static String host = "localhost";
	public static int port = 2306;
	private static c conn;

	public static c getConnection() throws KException, IOException
	{
		if(conn==null)
		{
			conn = new c(host, port);
		}
		return conn;
	}
	
}
