package DataAccessObjects;

import java.util.List;

import javax.persistence.TypedQuery;

import ProblemDomain.Client;
import ProblemDomain.Event;

public class EventDAO {

	public static void saveEvent(Event event) {
		EM.getEM().persist(event);
	}

	public static List<Event> listEvent() {
		TypedQuery<Event> query = EM.getEM().createQuery("SELECT event FROM event event", Event.class);
		return query.getResultList();
	}

	public static List<Event> getAllEvents(int page, int pageSize) {
		TypedQuery<Event> query = EM.getEM().createQuery("SELECT event FROM event event", Event.class);
		return query.setFirstResult(page).setMaxResults(pageSize).getResultList();
	}

	public static Event findEventById(long id) {
		Event event = EM.getEM().find(Event.class, new Long(id));
		return event;
	}

	public static void removeEvent(Event event) {
		EM.getEM().remove(event);
	}

}
