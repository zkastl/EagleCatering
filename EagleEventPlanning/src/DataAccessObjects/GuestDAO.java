package DataAccessObjects;

import java.util.ArrayList;
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
		List<Guest> allGuests = getAllGuests();
		List<Guest> guestsForEvent = new ArrayList<Guest>();
		int i = 0;

		for (Guest guest : allGuests) {
			if (guest.event.getEventId() == eventId) {
				if (i >= start)
					guestsForEvent.add(guest);
				i++;
				if (guestsForEvent.size() >= perPage)
					return guestsForEvent;
			}
		}
		return guestsForEvent;
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
