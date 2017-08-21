package ProblemDomain;

import java.util.Collection;
import java.util.List;

import DataAccessObjects.ClientDAO;
import DataAccessObjects.EventDAO;
import DataAccessObjects.EventPlannerDAO;
import DataAccessObjects.TokenDAO;

public class EventSystem {

	private Collection<Token> tokens;
	private Collection<EventPlanner> planners;

	public Collection<Token> getTokens() {
		return this.tokens;
	}

	public void setTokens(Collection<Token> tokens) {
		this.tokens = tokens;
	}

	public Collection<EventPlanner> getEventPlanners() {
		return this.planners;
	}

	/**
	 * 
	 * @param token
	 */
	public static Token findToken(String token) {
		return TokenDAO.findTokenByToken(token);
	}

	/**
	 * 
	 * @param userName
	 */
	public static EventPlanner findEventPlannerByUserName(String userName) {
		return EventPlannerDAO.findEventPlannerByUserName(userName);
	}

	public static EventPlanner findEventPlannerById(String id) {
		return EventPlannerDAO.findEventPlannerById(Long.parseLong(id));
	}

	public static Boolean addEventPlanner(EventPlanner planner) {
		EventPlannerDAO.saveEventPlanner(planner);
		return true;
	}

	public static Boolean removeEventPlanner(EventPlanner planner) {
		EventPlannerDAO.removeEventPlanner(planner);
		return true;
	}

	public static List<EventPlanner> findAllEventPlanners(int page, int pageSize) {
		List<EventPlanner> list = EventPlannerDAO.getAllEventPlanners(page, pageSize);
		return list;
	}

	public static Client findClientById(String id) {
		return ClientDAO.findClientById(Long.parseLong(id));
	}

	public static Boolean addClient(Client client) {
		ClientDAO.saveClient(client);
		return true;
	}

	public static Boolean removeClient(Client client) {
		ClientDAO.removeClient(client);
		return true;
	}

	public static List<Client> findAllClients(int page, int pageSize) {
		List<Client> list = ClientDAO.getAllClients(page, pageSize);
		return list;
	}

	public static List<Event> findAllEvents(int page, int pageSize) {
		List<Event> list = EventDAO.getAllEvents(page, pageSize);
		return list;
	}

	public static Event findEventById(String id) {
		return EventDAO.findEventById(Long.parseLong(id));
	}

	public static Boolean addEvent(Event event) {
		EventDAO.saveEvent(event);
		return true;
	}

	public static Boolean removeEvent(Event event) {
		EventDAO.removeEvent(event);
		return true;
	}

	public static List<Guest> findGuestsByEventId(String eventId, int parseInt, int parseInt2) {
		// TODO Auto-generated method stub
		return null;
	}

	public static Boolean addGuest(Guest guest) {
		// TODO Auto-generated method stub
		return null;
	}

	public static Guest findGuestById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	public static Boolean removeGuest(Guest guest) {
		// TODO Auto-generated method stub
		return null;
	}

}