package DataAccessObjects;

import java.util.List;

import ProblemDomain.Event;
import ProblemDomain.Guest;

public class GuestDAO {

	public static void addGuest(Guest guest) {
		EM.getEM().persist(guest);
	}

	public static void removeGuest(Guest guest) {
		EM.getEM().remove(guest);
	}

	public static List<Guest> getAllGuestsForEvent(Event event, int page, int perPage) {
		// TODO Auto-generated method stub
		return null;
	}

}
