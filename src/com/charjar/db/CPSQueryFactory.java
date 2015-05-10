package com.charjar.db;

import static com.charjar.util.Constants.CP_ACCOUNT_ID;
import static com.charjar.util.Constants.CP_PASSWORD;
import static com.charjar.util.Constants.CP_SERVER;
import static com.charjar.util.Constants.CP_USERNAME;

import com.clusterpoint.api.CPSConnection;

public class CPSQueryFactory {

	/* DB Names */
	public static final String DB_PROJECTS = "Projects";
	public static final String DB_DONATIONS = "Donations";
	public static final String DB_MATCHING_CAMPAIGNS = "MatchingCampaigns";
	public static final String DB_UPDATE_CARDS = "UpdateCards";
	public static final String DB_ORGANIZATIONS = "organizations";
	public static final String DB_PASSWORDS = "passwords";
	public static final String DB_USERS = "users";

	public static final String GT = "&gt;";
	public static final String LT = "&lt;";
	public static final String NOT = "~";
	
	public static CPSConnection getConnection(String project) throws Exception {
		return new CPSConnection(CP_SERVER, project, CP_USERNAME, CP_PASSWORD, 
		        CP_ACCOUNT_ID, "document", "//document/id");
	}
	
	public static String buildSearch(String field, String value) {
		return "<" + field + ">" + value + "</" + field + ">";
	}
}
