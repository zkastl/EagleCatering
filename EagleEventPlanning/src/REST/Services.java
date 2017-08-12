package REST;

import java.util.ArrayList;

import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import DataAccessObjects.EM;

import ProblemDomain.*;
import ProblemDomain.System;

@Path("/services")
public class Services {

	@Context
	SecurityContext securityContext;
	static int numberTimesCalled = 0;
	// ArrayList<Message> messages = new ArrayList<Message>();
	
	// Test services
	@GET
	@Path("/hello")
	@Produces(MediaType.TEXT_PLAIN)
	public String hello() {
		numberTimesCalled++;
		return "Hello GET-REST World!\n You've called this method: " + numberTimesCalled + " times.";
	}
	

	@POST
	@Path("/hello")
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_JSON)
	public String helloPost(HttpServletRequest request) {
		return "Hello POST-REST World!";
	}

	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response authenticateUser(EventPlanner user) {

		try {
			// Authenticate the user using the credentials provided
			authenticate(user.getUserName(), user.getPassword());

			// Issue a token for the user
			String token = issueToken(user.getUserName());

			// Return the token on the response
			return Response.ok(token).build();

		} catch (Exception e) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}

	private void authenticate(String username, String password) throws Exception {
		// Authenticate against a database, LDAP, file or whatever
		// Throw an Exception if the credentials are invalid
		EventPlanner planner = System.findEventPlannerByUserName(username);
		if (planner == null)
			throw new Exception();
		if (!planner.authenticate(password))
			throw new Exception();
	}

	private String issueToken(String username) {
		// Issue a token (can be a random String persisted to a database or a
		// JWT token)
		// The issued token must be associated to a user
		// Return the issued token
		EntityTransaction userTransaction = EM.getEM().getTransaction();
		userTransaction.begin();
		Token token = new Token(System.findEventPlannerByUserName(username));
		token.save();
		userTransaction.commit();
		return token.getToken();
	}
}