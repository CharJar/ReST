package com.charjar.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.w3c.dom.Element;

import com.charjar.db.CPSQueryFactory;
import com.charjar.util.CharJarException;
import com.charjar.util.JsonTransformer;
import com.clusterpoint.api.CPSConnection;
import com.clusterpoint.api.request.CPSLookupRequest;
import com.clusterpoint.api.request.CPSRetrieveRequest;
import com.clusterpoint.api.request.CPSSearchRequest;
import com.clusterpoint.api.response.CPSListLastRetrieveFirstResponse;
import com.clusterpoint.api.response.CPSSearchResponse;

import static com.charjar.db.CPSQueryFactory.*;


@Path("/users")
public class UserService {

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/")
	public String getUser(
			@QueryParam("email") String email,
			@QueryParam("password") String password ) {
		String response = null;
		CPSConnection cps = null;
		
		try {
 		  cps = CPSQueryFactory.getConnection(DB_PASSWORDS);
			
		  String query = CPSQueryFactory.buildSearch("email", email) 
				  + CPSQueryFactory.buildSearch("password", password);
		  
			// return these fields from the documents
			Map<String, String> list = new HashMap<String, String>();
			list.put("user_id", "yes");
			 
			CPSSearchRequest search_req = new  CPSSearchRequest(query, 0, 1, list);
			CPSSearchResponse search_resp = (CPSSearchResponse) cps.sendRequest(search_req);
			
			if (search_resp.getHits() > 0) {
			    System.out.println("Found:" + search_resp.getHits());
			    //get list of found documents as DOM Element
			    List<Element> results = search_resp.getDocuments();    
			    Iterator<Element> it = results.iterator();    
			    while (it.hasNext()) {
			      Element el = it.next();  //here comes code that extracts data from DOM Element    
			    }
			  }
			
			cps.close();
			
			cps = CPSQueryFactory.getConnection(DB_PROJECTS);
			CPSRetrieveRequest retr_req = new CPSRetrieveRequest("docid_1");
			 CPSListLastRetrieveFirstResponse retr_resp = (CPSListLastRetrieveFirstResponse) cps.sendRequest(retr_req);
			
			 cps.close();
		} catch (Exception e) {
			response = JsonTransformer.toJson(new CharJarException(e));
		} finally {
			
		}
		
		return response;
	}
}
