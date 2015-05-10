package com.charjar.db;

import static com.charjar.util.Constants.CP_ACCOUNT_ID;
import static com.charjar.util.Constants.CP_PASSWORD;
import static com.charjar.util.Constants.CP_SERVER;
import static com.charjar.util.Constants.CP_USERNAME;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.charjar.util.Constants;

public class CPSFactory {
	
	public static final String CP_URL = "https://api-us.clusterpoint.com/100124/";

	/* DB Names */
	public static final String DB_PROJECTS = "Projects/";
	public static final String DB_DONATIONS = "Donations/";
	public static final String DB_MATCHING_CAMPAIGNS = "MatchingCampaigns/";
	public static final String DB_UPDATE_CARDS = "UpdateCards/";
	public static final String DB_ORGANIZATIONS = "organizations/";
	public static final String DB_PASSWORDS = "passwords/";
	public static final String DB_USERS = "users/";

	public static final String GT = "&gt;";
	public static final String LT = "&lt;";
	public static final String NOT = "~";
	
	public static String buildSearch(String field, String value) {
		return "<" + field + ">" + value + "</" + field + ">";
	}
	
	public static String getResponse(HttpResponse resp) throws IOException {
		BufferedReader rd = new BufferedReader(
		        new InputStreamReader(resp.getEntity().getContent()));
	 
		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		return result.toString();
	}
	
	public static String getUser(String userID) throws ClientProtocolException, IOException, ParseException, JSONException {
		String url = CP_URL + DB_USERS + userID + ".json";
		String response = null;
		

		HttpClient client = HttpClientBuilder.create().build();
		HttpGet get = new HttpGet(url);
		
		get.setHeader("Authorization", Constants.CP_AUTORIZATION);
		
		HttpResponse resp = client.execute(get);
		System.out.println("Response Code : " 
                + resp.getStatusLine().getStatusCode());
 
		response = getResponse(resp);

		JSONParser parser = new JSONParser();
		JSONObject obj = (JSONObject) parser.parse(response);
		JSONArray docs = (JSONArray) obj.get("documents");
		JSONObject  doc = (JSONObject) docs.get(0);
		
		response = doc.toString();
		
		return response;
	}
}