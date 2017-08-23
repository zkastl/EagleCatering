package ProblemDomain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Table implements Serializable, Comparable<Table> {

	private static final long serialVersionUID = 1L;
	public int tableNumber;
	public List<Guest> seatedGuests;
	public int capacity;
	public int numberOfEmptySeats;
	public int eventID;

	public Table(int tableNumber, int capacity, int emptySeats, int eventID) {
		this.tableNumber = tableNumber;
		this.capacity = capacity;
		this.numberOfEmptySeats = emptySeats;
		seatedGuests = new ArrayList<Guest>();
		this.eventID = eventID;
	}

	public Table(int tableNumber, int capacity, int emptySeats) {
		this.tableNumber = tableNumber;
		this.capacity = capacity;
		this.numberOfEmptySeats = emptySeats;
		seatedGuests = new ArrayList<Guest>();
		this.eventID = -1;
	}

	public boolean addGuest(Guest guest) {
		if (seatedGuests.size() < capacity - numberOfEmptySeats) {
			seatedGuests.add(guest);
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

	@Override
	public int compareTo(Table o) {
		if (this.tableNumber < o.tableNumber)
			return -1;
		else if (this.tableNumber == o.tableNumber)
			return 0;
		else
			return 1;

	}

	public boolean isFull() {
		return this.seatedGuests.size() >= (this.capacity - this.numberOfEmptySeats);
	}
}
