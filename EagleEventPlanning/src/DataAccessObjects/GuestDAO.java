package DataAccessObjects;

import java.util.List;

import javax.persistence.TypedQuery;

import ProblemDomain.Guest;

public class GuestDAO {

	public static void addGuest(Guest guest) {
		EM.getEM().persist(guest);
	}

	public static void removeGuest(Guest guest) {
		EM.getEM().remove(guest);
	}

	public static List<Guest> getAllGuests() {
		TypedQuery<Guest> query = EM.getEM().createQuery("SELECT guest FROM guest guest", Guest.class);
		return query.getResultList();
	}

	public static List<Guest> getAllGuestsForEvent(long eventId, int start, int perPage) {
		TypedQuery<Guest> query = EM.getEM()
				.createQuery("SELECT guest FROM guest guest WHERE guest.eventID = '" + eventId + "'", Guest.class);
		return query.setFirstResult(start).setMaxResults(perPage).getResultList();
	}

	public static void saveGuest(Guest guest) {
		EM.getEM().persist(guest);
	}

	public static Guest findGuestById(long id) {
		Guest guest = EM.getEM().find(Guest.class, id);
		return guest;
	}

	public static void removeEventPlanner(Guest guest) {
		EM.getEM().remove(guest);
	}

}
