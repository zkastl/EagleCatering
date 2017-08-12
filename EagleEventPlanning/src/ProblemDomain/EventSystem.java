package ProblemDomain;

import java.util.Collection;
import java.util.List;

import DataAccessObjects.EventPlannerDAO;
import DataAccessObjects.TokenDAO;

public class System {

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

	public static Boolean removeUser(EventPlanner planner) {
		EventPlannerDAO.removeEventPlanner(planner);
		return true;
	}

	public static List<EventPlanner> findAllEventPlanners(int page, int pageSize) {
		List<EventPlanner> list = EventPlannerDAO.getAllEventPlanners(page, pageSize);
		return list;
	}

}