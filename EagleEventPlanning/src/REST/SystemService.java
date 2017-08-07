package REST;

import java.util.ArrayList;

import javax.persistence.EntityTransaction;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import DataAccessObjects.EM;
import ProblemDomain.Event;
//import schoolPD.Student;
//import schoolUT.Message;
import ProblemDomain.EventPlanner;
import ProblemDomain.Guest;
//import systemPD.RoleAssignment;
import ProblemDomain.System;
import ProblemDomain.Token;
import TableSorter.Layout;

@Path("/system")
public class SystemService {

	@Context
	SecurityContext securityContext;

	// ArrayList<Message> messages = new ArrayList<Message>();

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
	
	
	@Path("/events")
	@Produces(MediaType.TEXT_PLAIN)
	public Layout getLayout(String guestList) throws Exception {
		Event t = new Event();
		t.importGuests(guestList);
		return Layout.createRandomTableLayout((ArrayList<Guest>)t.getGuestList().values(), 8, 2);
	}
	//
	// @Secured({Role.Admin})
	// @GET
	// @Path("/employees")
	// @Produces(MediaType.APPLICATION_JSON)
	// public List<EventPlanner> getUser(
	// @DefaultValue("0") @QueryParam("page") String page,
	// @DefaultValue("5") @QueryParam("per_page") String perPage){
	// return
	// System.findAllEventPlanners(Integer.parseInt(page),Integer.parseInt(perPage));
	// }
	// @Secured
	// @GET
	// @Path("/users/current")
	// @Produces(MediaType.APPLICATION_JSON)
	// public EventPlanner getUser(){
	// String username = securityContext.getUserPrincipal().getName();
	// EventPlanner planner = System.findEventPlannerByUserName(username);
	// return planner;
	// }
	//
	// @Secured({Role.Admin})
	// @GET
	// @Path("/users/{id}")
	// @Produces(MediaType.APPLICATION_JSON)
	// public User getUser(@PathParam("id") String id){
	// User user = System.findUserById(id);
	// EM.getEM().refresh(user);
	// return user;
	// }
	//
	// @Secured({Role.ADMIN})
	// @POST
	// @Path("/users")
	// @Produces(MediaType.APPLICATION_JSON)
	// @Consumes(MediaType.APPLICATION_JSON)
	// public ArrayList<Message> addUser(User user,@Context final
	// HttpServletResponse response) throws IOException{
	//
	// if (user == null) {
	//
	// response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
	// try {
	// response.flushBuffer();
	// }catch(Exception e){}
	// messages.add(new Message("op002","Fail Operation",""));
	// return messages;
	// }
	// else {
	//
	// ArrayList<Message> errMessages = user.validate();
	// if (errMessages != null) {
	//
	// response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
	// try {
	// response.flushBuffer();
	// }
	// catch(Exception e){
	// }
	// return errMessages;
	// }
	// EntityTransaction userTransaction = EM.getEM().getTransaction();
	// userTransaction.begin();
	// Boolean result = System.addUser(user);
	// if (user.getRoleAssignments() != null){
	// for (RoleAssignment ra : user.getRoleAssignments()) {
	// user.addRoleAssignment(ra);
	// }
	// }
	// userTransaction.commit();
	// if(result){
	// messages.add(new Message("op001","Success Operation",""));
	// return messages;
	// }
	// response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
	// try {
	// response.flushBuffer();
	// }
	// catch(Exception e){
	// }
	// messages.add(new Message("op002","Fail Operation",""));
	// return messages;
	// }
	// }
	// @Secured({Role.ADMIN})
	// @PUT
	// @Path("/users/{id}")
	// @Produces(MediaType.APPLICATION_JSON)
	// @Consumes(MediaType.APPLICATION_JSON)
	// public ArrayList<Message> udpatedStudent(User user,@PathParam("id")
	// String id, @Context final HttpServletResponse response) throws
	// IOException{
	// User oldUser = System.findUserById(id);
	// if (oldUser == null)
	// {
	// response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
	// try {
	// response.flushBuffer();
	// }catch(Exception e){}
	// messages.add(new Message("op002","Fail Operation",""));
	// return messages;
	// }
	// else
	// {
	// ArrayList<Message> errMessages = user.validate();
	// if (errMessages != null) {
	// response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
	// try {
	// response.flushBuffer();
	// }
	// catch(Exception e){
	// }
	// return errMessages;
	// }
	// }
	// EntityTransaction userTransaction = EM.getEM().getTransaction();
	// userTransaction.begin();
	// Boolean result = oldUser.update(user);
	// userTransaction.commit();
	// if(result){
	// messages.add(new Message("op001","Success Operation",""));
	// return messages;
	// }
	// response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
	// try {
	// response.flushBuffer();
	// }catch(Exception e){}
	// messages.add(new Message("op002","Fail Operation",""));
	// return messages;
	// }
	//
	// @Secured({Role.ADMIN})
	// @DELETE
	// @Path("/users/{id}")
	// @Produces(MediaType.APPLICATION_JSON)
	// public ArrayList<Message> deleteUser(@PathParam("id") String id, @Context
	// final HttpServletResponse response){
	// User user = System.findUserById(id);
	// if (user == null) {
	// response.setStatus(HttpServletResponse.SC_NOT_FOUND);
	// try {
	// response.flushBuffer();
	// }catch(Exception e){}
	// messages.add(new Message("op002","Fail Operation",""));
	// return messages;
	// }
	// EntityTransaction userTransaction = EM.getEM().getTransaction();
	// userTransaction.begin();
	// if (user.getRoleAssignments() != null){
	// for (RoleAssignment ra : user.getRoleAssignments()) {
	// user.removeRoleAssignment(ra);
	// }
	// }
	//
	// Boolean result = System.removeUser(user);
	// userTransaction.commit();
	// if(result){
	// messages.add(new Message("op001","Success Operation",""));
	// return messages;
	// }
	// else {
	// messages.add(new Message("op002","Fail Operation",""));
	// return messages;
	// }
	// }

}