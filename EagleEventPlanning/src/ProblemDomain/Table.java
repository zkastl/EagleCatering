package ProblemDomain;

import java.util.ArrayList;
import java.util.List;

public class Table {

	public int tableNumber;
	public List<Guest> seatedGuests;
	int capacity;
	int numberOfEmptySeats;

	public Table(int tableNumber, int capacity, int emptySeats) {
		this.tableNumber = tableNumber;
		this.capacity = capacity;
		this.numberOfEmptySeats = emptySeats;
		seatedGuests = new ArrayList<Guest>();
	}

	public boolean addGuest(Guest guest) {
		if (seatedGuests.size() < capacity) {
			seatedGuests.add(guest);
			seatedGuests.get(seatedGuests.size() - 1).tableNumber = tableNumber;
			return true;
		}
		return false;
	}

	public boolean removeGuest(Guest guest) {
		if (seatedGuests.contains(guest)) {
			seatedGuests.remove(guest);
			return true;
		}
		return false;
	}
}
