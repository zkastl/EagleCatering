package DataAccessObjects;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import ProblemDomain.EventPlanner;

public class EventPlannerDAO {

	public static void saveEventPlanner(EventPlanner planner) {
		EM.getEM().persist(planner);
	}

	public static List<EventPlanner> getAllEventPlanners() {
		TypedQuery<EventPlanner> query = EM.getEM().createQuery("SELECT eventplanner FROM eventplanner eventplanner",
				EventPlanner.class);
		return query.getResultList();
	}

	public static EventPlanner findEventPlannerById(long id) {
		EventPlanner planner = EM.getEM().find(EventPlanner.class, id);
		return planner;
	}

	public static EventPlanner findEventPlannerByUserName(String username) {
		String qString = "SELECT eventplanner FROM eventplanner eventplanner  WHERE eventplanner.userName ='" + username
				+ "'";
		Query query = EM.getEM().createQuery(qString);
		EventPlanner planner = (EventPlanner) query.getSingleResult();
		return planner;
	}

	public static List<EventPlanner> getAllEventPlanners(int page, int pageSize) {
		TypedQuery<EventPlanner> query = EM.getEM().createQuery("SELECT eventplanner FROM eventplanner eventplanner",
				EventPlanner.class);
		return query.setFirstResult(page).setMaxResults(pageSize).getResultList();

	}

	public static void removeEventPlanner(EventPlanner eventPlanner) {
		EM.getEM().remove(eventPlanner);
	}
}
