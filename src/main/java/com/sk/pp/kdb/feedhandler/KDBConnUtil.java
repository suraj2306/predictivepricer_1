package com.sk.pp.kdb.feedhandler;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.sk.pp.kdb.feedhandler.c.KException;

public class KDBConnUtil {

	static Logger log = Logger.getLogger(KDBConnUtil.class);

	public static String host = "localhost";
	public static int port = 2306;
	private static c conn;
	private static Connection jdbcConn = null;

	static final String jdbc_conn = "jdbc";
	static final String db_url = "jdbc:q:localhost:2306";
	static final String user = "";
	static final String pass = "";

	public static c getConnection() throws KException, IOException {
		if (conn == null) {
			conn = new c(host, port);
		}
		return conn;
	}

	public static Connection getJdbcConnection() {
		try {
			Class.forName(jdbc_conn);
			log.info("Connecting to database...");
			jdbcConn = DriverManager.getConnection(db_url, user, pass);
		} catch (Exception ex) {
			log.error("Failed JDBC connection", ex);
		}
		finally{
		      try {
				jdbcConn.close();
			} catch (SQLException e) {
				log.error("Cannot close connection ",e);
			}
		}
		return jdbcConn;
	}

	@SuppressWarnings("null")
	public static double[] getWeights(String alias) {
		double[] weights = null;

		try {
			Statement stmt = null;
			stmt = jdbcConn.createStatement();

			ResultSet rs = stmt.executeQuery("SELECT c, w1, w2, w3 FROM weights where alias=`"+alias);

			while (rs.next()) {
				Double c = rs.getDouble("c");
				weights[0] = c;
				Double w1 = rs.getDouble("w1");
				weights[1] = w1;
				Double w2 =rs.getDouble("w2");
				weights[2] = w2;
				Double w3 =rs.getDouble("w3");
				weights[3] = w3;
				
			}
			rs.close();
		      stmt.close();
		     
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return weights;
	}

}
