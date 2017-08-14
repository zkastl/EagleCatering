package REST;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityTransaction;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import DataAccessObjects.EM;
import ProblemDomain.Event;
import ProblemDomain.EventPlanner;
import ProblemDomain.EventSystem;
import ProblemDomain.Message;
import ProblemDomain.Role;
import ProblemDomain.RoleAssignment;
import ProblemDomain.Token;

@Path("/services")
public class Services {

	@Context
	SecurityContext securityContext;
	static int numberTimesCalled = 0;
	ArrayList<Message> messages = new ArrayList<Message>();

	// Test services
	@GET
	@Path("/hello")
	@Produces(MediaType.TEXT_PLAIN)
	public String hello() {
		numberTimesCalled++;
		return "Hello GET-REST World!\n You've called this method: " + numberTimesCalled + " times.";
	}

	@Context
	ServletContext context;

	//@Secured({Role.EventPlanner})
	@POST
	@Path("/import/{id}/uploadfile")
	@Produces(MediaType.MULTIPART_FORM_DATA)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response importGuests(@PathParam("id") String id, @FormDataParam("file") InputStream uploadInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail) {

		// Save the file to the server
		try {
			File tempFile = File.createTempFile("upload" + "001", ".tmp");
			String lastUploadedFile = tempFile.getAbsolutePath();
			OutputStream out = new FileOutputStream(tempFile);

			int read = 0;
			byte[] bytes = new byte[1024];
			while ((read = uploadInputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}

			Event e = new Event();
			e.importGuests(lastUploadedFile);
			out.close();
		} catch (IOException ioex) {
			ioex.printStackTrace();
		}
		return Response.status(200).entity("File uploaded succsssfully!").build();
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

	@Secured({ Role.Admin })
	@GET
	@Path("/employees")
	@Produces(MediaType.APPLICATION_JSON)
	public List<EventPlanner> getEventPlanner(@DefaultValue("0") @QueryParam("page") String page,
			@DefaultValue("15") @QueryParam("per_page") String perPage) {
		return EventSystem.findAllEventPlanners(Integer.parseInt(page), Integer.parseInt(perPage));
	}

	@Secured
	@GET
	@Path("/employees/current")
	@Produces(MediaType.APPLICATION_JSON)
	public EventPlanner getEventPlanner() {
		String username = securityContext.getUserPrincipal().getName();
		EventPlanner planner = EventSystem.findEventPlannerByUserName(username);
		return planner;
	}

	@Secured({ Role.Admin })
	@GET
	@Path("/employees/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public EventPlanner getEventPlanner(@PathParam("id") String id) {
		EventPlanner planner = EventSystem.findEventPlannerById(id);
		EM.getEM().refresh(planner);
		return planner;
	}

	@Secured({ Role.Admin })
	@POST
	@Path("/employees")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<Message> addEventPlanner(EventPlanner planner, @Context final HttpServletResponse response)
			throws IOException {

		if (planner == null) {

			response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
			try {
				response.flushBuffer();
			} catch (Exception e) {
			}
			messages.add(new Message("op002", "Fail Operation", ""));
			return messages;
		} else {

			ArrayList<Message> errMessages = planner.validate();
			if (errMessages != null) {

				response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
				try {
					response.flushBuffer();
				} catch (Exception e) {
				}
				return errMessages;
			}
			EntityTransaction userTransaction = EM.getEM().getTransaction();
			userTransaction.begin();
			Boolean result = EventSystem.addEventPlanner(planner);
			if (planner.getRoleAssignments() != null) {
				for (RoleAssignment ra : planner.getRoleAssignments()) {
					planner.addRoleAssignment(ra);
				}
			}
			userTransaction.commit();
			if (result) {
				messages.add(new Message("op001", "Success Operation", ""));
				return messages;
			}
			response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
			try {
				response.flushBuffer();
			} catch (Exception e) {
			}
			messages.add(new Message("op002", "Fail Operation", ""));
			return messages;
		}
	}

	@Secured({ Role.Admin })
	@PUT
	@Path("/employees/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ArrayList<Message> udpateEventPlanner(EventPlanner planner, @PathParam("id") String id,
			@Context final HttpServletResponse response) throws IOException {
		EventPlanner oldPlanner = EventSystem.findEventPlannerById(id);
		if (oldPlanner == null) {
			response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
			try {
				response.flushBuffer();
			} catch (Exception e) {
			}
			messages.add(new Message("op002", "Fail Operation", ""));
			return messages;
		} else {
			ArrayList<Message> errMessages = planner.validate();
			if (errMessages != null) {
				response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
				try {
					response.flushBuffer();
				} catch (Exception e) {
				}
				return errMessages;
			}
		}
		EntityTransaction plannerTransaction = EM.getEM().getTransaction();
		plannerTransaction.begin();
		Boolean result = oldPlanner.update(planner);
		plannerTransaction.commit();
		if (result) {
			messages.add(new Message("op001", "Success Operation", ""));
			return messages;
		}
		response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
		try {
			response.flushBuffer();
		} catch (Exception e) {
		}
		messages.add(new Message("op002", "Fail Operation", ""));
		return messages;
	}

	@Secured({ Role.Admin })
	@DELETE
	@Path("/employees/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Message> deleteEventPlanner(@PathParam("id") String id,
			@Context final HttpServletResponse response) {
		EventPlanner planner = EventSystem.findEventPlannerById(id);
		if (planner == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			try {
				response.flushBuffer();
			} catch (Exception e) {
			}
			messages.add(new Message("op002", "Fail Operation", ""));
			return messages;
		}
		EntityTransaction plannerTransaction = EM.getEM().getTransaction();
		plannerTransaction.begin();
		if (planner.getRoleAssignments() != null) {
			for (RoleAssignment ra : planner.getRoleAssignments()) {
				planner.removeRoleAssignment(ra);
			}
		}

		Boolean result = EventSystem.removeEventPlanner(planner);
		plannerTransaction.commit();
		if (result) {
			messages.add(new Message("op001", "Success Operation", ""));
			return messages;
		} else {
			messages.add(new Message("op002", "Fail Operation", ""));
			return messages;
		}
	}

	private void authenticate(String username, String password) throws Exception {
		// Authenticate against a database, LDAP, file or whatever
		// Throw an Exception if the credentials are invalid
		EventPlanner planner = EventSystem.findEventPlannerByUserName(username);
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
		Token token = new Token(EventSystem.findEventPlannerByUserName(username));
		token.save();
		userTransaction.commit();
		return token.getToken();
	}
}