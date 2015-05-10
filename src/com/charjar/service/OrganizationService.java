package com.charjar.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.Produces;

@Path("/organizations")
public class OrganizationService {

	@GET
	@Path("/{org_id}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getOrganization(@PathParam("org_id") String org_id) {
		String response = null;
		
		return response;
	}
}
