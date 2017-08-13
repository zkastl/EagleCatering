package REST;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.sql.Date;
import java.time.LocalDateTime;

import javax.persistence.EntityTransaction;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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
import ProblemDomain.Token;

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
	
	@Context ServletContext context;
	
	@POST
	@Path("/hello/{id}/uploadfile")
	@Produces(MediaType.MULTIPART_FORM_DATA)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response helloPost(@PathParam("id") String id,
			@FormDataParam("file") InputStream uploadInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail) {
		
		// Save the file to the server
		try {
			File tempFile = File.createTempFile("upload" + "001", ".tmp");
			String lastUploadedFile =  tempFile.getAbsolutePath();
			OutputStream out = new FileOutputStream(tempFile);
			
			// Leaving this code cause I might want it later.
			/*BufferedReader buf = new BufferedReader(new InputStreamReader(uploadInputStream));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = buf.readLine()) != null) {
				sb.append(line).append("\n");				
			}
			System.out.println(sb.toString());*/
			
			int read = 0;
			byte[] bytes = new byte[1024];			
			while ((read = uploadInputStream.read(bytes)) != -1) {
				out.write(bytes,  0,  read);			
			}
			
			Event e = new Event();
			e.importGuests(lastUploadedFile);
			out.close();
		}
		catch(IOException ioex) {
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