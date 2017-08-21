package ProblemDomain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "guest")
@Entity(name = "guest")
public class Guest implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id // signifies the primary key
	@Column(name = "guest_id", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int guestId;

	@Column(name = "firstName", nullable = false, length = 40)
	public String firstName;

	@Column(name = "lastName", nullable = false, length = 40)
	public String lastName;

	@Column(name = "guestNumber", nullable = false)
	public int guestNumber;

	@Column(name = "sameTable", nullable = false)
	public List<Integer> sameTable;

	@Column(name = "notSameTable", nullable = false)
	public List<Integer> notSameTable;

	@JoinColumn(name = "assignedTable", referencedColumnName = "table_id")
	@ManyToOne(optional = false)
	public Table assignedTable;

	@Transient
	public int tableNumber;

	@Transient
	public int eventID;

	@JoinColumn(name = "event", referencedColumnName = "event_id")
	@ManyToOne(optional = false)
	public Event event;

	@Column(name = "comments", nullable = false, length = 40)
	public String comments;

	// default constructor for JPA
	public Guest() {

	}

	public Guest(int guestNumber, String firstName, String lastName, List<Integer> sameTable,
			List<Integer> notSameTable, int tableNumber, int eventID, String comments) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.guestNumber = guestNumber;
		this.sameTable = sameTable;
		this.notSameTable = notSameTable;
		this.tableNumber = tableNumber;
		this.eventID = eventID;
		this.comments = comments;
	}

	public Guest(int guestNumber, String firstName, String lastName, List<Integer> sameTable,
			List<Integer> notSameTable, int tableNumber, int eventID) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.guestNumber = guestNumber;
		this.sameTable = sameTable;
		this.notSameTable = notSameTable;
		this.tableNumber = tableNumber;
		this.eventID = eventID;
		this.comments = "";
	}

	public Guest(int guestNumber, String firstName, String lastName, List<Integer> sameTable,
			List<Integer> notSameTable, int tableNumber) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.guestNumber = guestNumber;
		this.sameTable = sameTable;
		this.notSameTable = notSameTable;
		this.tableNumber = tableNumber;
		this.eventID = -1;
		this.comments = "";
	}

	public void assignTable(int tableNumber) {
		this.tableNumber = tableNumber;
	}

	public String getName() {
		return this.firstName + " " + this.lastName;
	}

	public ArrayList<Message> validate() {
		// TODO Auto-generated method stub
		return null;
	}

	public Boolean update(Guest guest) {
		// TODO Auto-generated method stub
		return null;
	}

}
