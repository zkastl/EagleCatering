package ProblemDomain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "table")
@Entity(name = "table")
public class Table implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id // signifies the primary key
	@Column(name = "table_id", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int tableId;

	@Column(name = "tableNumber", nullable = false)
	public int tableNumber;
	
	@OneToMany(mappedBy = "table", targetEntity = Guest.class, fetch = FetchType.EAGER)
	public List<Guest> seatedGuests;
	
	@Column(name = "capacity", nullable = false)
	public int capacity;
	
	@Column(name = "numberOfEmptySeats", nullable = false)
	public int numberOfEmptySeats;
	
	@Column(name = "eventID", nullable = false)
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
