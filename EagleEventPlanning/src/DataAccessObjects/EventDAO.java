package DataAccessObjects;

import java.util.List;

import javax.persistence.TypedQuery;

import ProblemDomain.Event;

public class EventDAO {

	public static void saveEvent(Event event) {
		EM.getEM().persist(event);
	}

	public static List<Event> listEvent() {
		TypedQuery<Event> query = EM.getEM().createQuery("SELECT event FROM event event", Event.class);
		return query.getResultList();
	}

}
