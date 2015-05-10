package com.charjar.service;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.TextUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.charjar.db.CPSFactory;
import com.charjar.util.Constants;

import static com.charjar.db.CPSFactory.*;


@Path("/users")
public class UserService {

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/")
	public String login(
			@QueryParam("email") String email,
			@QueryParam("password") String password ) {
		String response = null;
		
		String url = "https://api-us.clusterpoint.com/100124/passwords/_search.json";
		
		try {
			HttpClient client = HttpClientBuilder.create().build();
			HttpPost post = new HttpPost(url);
			
			post.setHeader("Authorization", Constants.CP_AUTORIZATION);
			
			String query = CPSFactory.buildSearch("email", email)
					+ CPSFactory.buildSearch("password", password);
			
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
		} catch( Exception e) {
			return e.getMessage();
		} finally {
			
		}
		
		return response;
	}
	
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/")
	public String create(
			@FormParam("user") String userObj,
			@FormParam("password") String password) {
		String response = null;
		
		try {
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
		}
		
		
		return response;
	}
}
