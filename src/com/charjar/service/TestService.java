package com.charjar.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.TextUtils;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.charjar.db.CPSFactory;
import com.charjar.util.Constants;

@Path("test")
public class TestService {

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/")
	public String connect() throws ClientProtocolException, IOException {
		String response = null;
		
		String url = "https://api-us.clusterpoint.com/100124/passwords/_search.json";
		
		try {
			HttpClient client = HttpClientBuilder.create().build();
			HttpPost post = new HttpPost(url);
			
			post.setHeader("Authorization", Constants.CP_AUTORIZATION);
			
			String query = CPSFactory.buildSearch("email", "andrew")
					+ CPSFactory.buildSearch("password", "test");
			
			query = "{\"query\":\"" + query + "\",\"docs\":\"1\",\"offset\":\"0\"}";
			
			post.setEntity(new ByteArrayEntity(query.getBytes("UTF-8")));
	
			HttpResponse resp = client.execute(post);
			System.out.println("Response Code : " 
	                + resp.getStatusLine().getStatusCode());
	 
			response = CPSFactory.getResponse(resp);
			
			JSONParser parser = new JSONParser();
			JSONObject obj = (JSONObject) parser.parse(response);
			JSONArray docs = (JSONArray) obj.get("documents");
			JSONObject  doc = (JSONObject) docs.get(0);
			String user_id = (String) doc.get("user_id");
			
			if( !TextUtils.isEmpty(user_id) ) {
				response = CPSFactory.getUser(user_id);
			} else {
				response = "[]";
			}
		} catch( ParseException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} finally {
			
		}
		
		return response;
	}
}
