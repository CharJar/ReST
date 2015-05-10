package com.charjar.service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.charjar.db.CPSFactory;
import com.charjar.util.CharJarException;
import com.charjar.util.DBConnection;
import com.charjar.util.JsonTransformer;

import static com.charjar.db.CPSFactory.*;


@Path("projects")
public class ProjectService {

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/")
	public String getNextProject(
			@QueryParam("userUUID") String userUUID,
			@QueryParam("latitude") String latString,
			@QueryParam("longitude") String longString) {
		String response = null;
		Connection con = null;
		CallableStatement cs = null;
		
		try {
			con = DBConnection.getDBConnection();
			
			cs = con.prepareCall("{call sp_GetNextProject(?,?,?,?,?)}");
			cs.setString("UserUUID", userUUID);
			cs.setDouble("latitudeRad", Math.toRadians(Double.parseDouble(latString)));
			cs.setDouble("longitudeRad", Math.toRadians(Double.parseDouble(longString)));
			cs.setInt("Radius", 50); // default to 50 km
			cs.registerOutParameter("Success", java.sql.Types.BIT);
			cs.registerOutParameter("ErrorID", java.sql.Types.SMALLINT);
			
			if( cs.execute() ) {
				ResultSet rs = cs.getResultSet();
				rs.next();
				
				String projectUUID = rs.getString("ProjectUUID");
				int viewedTimes = rs.getInt("ViewedTimes");
				boolean hasDonated = rs.getBoolean("hasDonated");
				
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
