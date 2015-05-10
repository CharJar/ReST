package com.charjar.service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.charjar.db.CPSFactory;
import com.charjar.util.CharJarException;
import com.charjar.util.Constants;
import com.charjar.db.DBConnection;
import com.charjar.util.JsonTransformer;

import static com.charjar.db.CPSFactory.*;


@Path("projects")
public class ProjectService {

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/")
	public String getNextProject(
			@QueryParam("userUUID") String userUUID,
			@QueryParam("quantity") String qty
//			@QueryParam("latitude") String latString,
//			@QueryParam("longitude") String longString
			) {
		String response = null;
		Connection con = null;
		CallableStatement cs = null;
		
		try {
			con = DBConnection.getDBConnection();
			
			cs = con.prepareCall("{call sp_GetNextProject(?,?,?,?)}");
			cs.setString("UserUUID", userUUID);
			cs.setInt("Quantity", Integer.parseInt(qty));
//			cs.setDouble("latitudeRad", Math.toRadians(Double.parseDouble(latString)));
//			cs.setDouble("longitudeRad", Math.toRadians(Double.parseDouble(longString)));
//			cs.setInt("Radius", 50); // default to 50 km
			cs.registerOutParameter("Success", java.sql.Types.BIT);
			cs.registerOutParameter("ErrorID", java.sql.Types.SMALLINT);
			
			Map<String,String> extras = new HashMap<String, String>();
			String ids = "[";
			boolean first = true;
			
			if( cs.execute() ) {
				ResultSet rs = cs.getResultSet();
				while(rs.next()) {
					String projectUUID = rs.getString("ProjectUUID");
					int viewedTimes = rs.getInt("ViewedTimes");
					boolean hasDonated = rs.getBoolean("hasDonated");
					
					extras.put(projectUUID, "\"viewedTimes\":" + viewedTimes + ",\"hasDonated\":\"" + hasDonated + "\",");

					if( !first ) {
						ids +=",";
					} 
					
					ids += "{\"id\":\"" + projectUUID + "\"}";
					
					first = false;
				}
				ids +="]";
				
				String url = "https://api-us.clusterpoint.com/100124/Projects/_retrieve.json";

				HttpClient client = HttpClientBuilder.create().build();
				HttpPost post = new HttpPost(url);
				
				post.setHeader("Authorization", Constants.CP_AUTORIZATION);

				post.setEntity(new ByteArrayEntity(ids.getBytes("UTF-8")));

				HttpResponse resp = client.execute(post);
				System.out.println("Response Code : " 
		                + resp.getStatusLine().getStatusCode());
		 
				response = CPSFactory.getResponse(resp);
				
				JSONParser parser = new JSONParser();
				JSONObject obj = (JSONObject) parser.parse(response);
				JSONArray docs = (JSONArray) obj.get("documents");
				String projects = docs.toString();
				
				int offset = "\"id\":\"".length();
				
				Iterator<Map.Entry<String, String>> it = extras.entrySet().iterator();
			    while (it.hasNext()) { // find the specific project
			        Map.Entry<String,String> pair = (Map.Entry<String,String>) it.next();
			        int index = projects.indexOf(pair.getKey()) - offset;
			        projects = projects.substring(0,index) + pair.getValue() + projects.substring(index, projects.length());
			        it.remove(); // avoids a ConcurrentModificationException
			    }
				response = projects;				
			} else {
				if( cs.getBoolean("Success") ) {
					return "[]";
				} else {
					throw new CharJarException(cs.getInt("ErrorID"));
				}
			}
			
			
		} catch (CharJarException e) {
			response = JsonTransformer.toJson(e);
		} catch (Exception e) {
			response = JsonTransformer.toJson((new CharJarException(e)));
		} finally {
			DBConnection.closeCallableStatement(cs);
			DBConnection.closeConnection(con);
		}
		
		return response;
	}
	
	@PUT
	@Path("/{userID}/{projectID}")
	@Produces(MediaType.TEXT_PLAIN)
	public String makeProjectViewed(
			@PathParam("userID") String userID,
			@PathParam("projectID") String projectID ) {
		String response = null;
		
		
		return response;
	}
	
}
