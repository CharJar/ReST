package com.charjar.util;

public class Constants {
	
	public static final long VERSION = 2;

	// Database Connection
	public static final String DATABASE = "jdbc:sqlserver://jma-db.c5wfusvdk0sq.us-east-1.rds.amazonaws.com:1433;database=CharJar";
	public static final String USERNAME = "CharJar";
	public static final String PASSWORD = "@[L[~5u>WFRf/C<>C\39";
	
	// ClusterPoint
	public static final String CP_SERVER= "tcp://api-us.clusterpoint.com:443";
	public static final String CP_USERNAME = "aschulz@gatech.edu";
	public static final String CP_PASSWORD = "charjar04!";
	public static final String CP_ACCOUNT_ID = "100124";
	
	// Google
	public static final String GOOGLE_PROJECT_ID = "625103153117";
	public static final String GOOGLE_API_KEY = "AIzaSyAqh_VXdvymTFc3MXVMfP7XmAgsFKyGyr0";
	
	// Mandrill
	public static final String MANDRILL_API_KEY = "vfQx6A5ZlGdIQ7PPgoqgVQ";
	
	public class DeviceTypes {
		public static final int ANDROID = 1;
		public static final int APPLE = 2;
		public static final int BLACKBERRY = 3;
		public static final int AMAZON = 4;
	}
}
