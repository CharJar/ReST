package com.charjar.service;

import java.util.UUID;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import com.charjar.db.CPSFactory;
import com.charjar.util.Constants;

@Path("/donations")
public class DonationService {

	@Path("/")
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	public String donate( @FormParam("donation") String donationObj ) {
		String response = null;
		
		String url = "https://api-us.clusterpoint.com/100124/Donations.json";
		
		try {
			String uuid = UUID.randomUUID().toString();
			
			int bracket = donationObj.indexOf("{");
			String donation = "{\"id\":" + uuid +"\"," + donationObj.substring(1);
			

			HttpClient client = HttpClientBuilder.create().build();
			HttpPost post = new HttpPost(url);
			
			post.setHeader("Authorization", Constants.CP_AUTORIZATION);
			
			post.setEntity(new ByteArrayEntity(donation.getBytes("UTF-8")));
			
			HttpResponse resp = client.execute(post);
			System.out.println("Response Code : " 
	                + resp.getStatusLine().getStatusCode());
			
			if( resp.getStatusLine().getStatusCode() == 200 ) {
				response = "true";
			} else {
				response = resp.getStatusLine().toString();
			}
		} catch( Exception e ) {
			response = e.getMessage();
		}
		
		return response;
	}
}
