package ProblemDomain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import DataAccessObjects.EventDAO;
import DataAccessObjects.GuestDAO;

@XmlRootElement(name = "event")
@Entity(name = "event")
public class Event implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id // signifies the primary key
	@Column(name = "event_id", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int eventId;

	@Column(name = "name", nullable = false, length = 40)
	private String name;

	@Column(name = "dateTime", nullable = false)
	private LocalDateTime dateTime;

	@Column(name = "location", nullable = false, length = 40)
	private String location;

	@ManyToOne(optional = false)
	@JoinColumn(name = "eventPlanner", referencedColumnName = "name")
	public EventPlanner eventPlanner;

	@ManyToOne(optional = false)
	@JoinColumn(name = "client", referencedColumnName = "name")
	public Client client;

	@OneToMany(mappedBy = "event", targetEntity = Guest.class, fetch = FetchType.EAGER)
	private HashMap<String, Guest> guestList;

	@Column(name = "tableSize", nullable = false)
	private Integer tableSize;

	@Column(name = "emptySeatsPerTable", nullable = false)
	private Integer emptySeatsPerTable;

	@Transient
	private SeatingArrangement seatingArrangement;

	@Column(name = "minFitness", nullable = false)
	private Integer minFitness;

	@Column(name = "iterations", nullable = false)
	private Integer iterations;

	@Column(name = "status", nullable = false, length = 20)
	private String status;

	public Event() {

	}

	public Event(String name) {
		this();
		this.name = name;
	}

	public Event(String name, LocalDateTime dateTime, String location, EventPlanner eventPlanner, Client client,
			HashMap<String, Guest> guestList, Integer tableSize, Integer emptySeatsPerTable, Integer minFitness,
			Integer iterations, String status) {
		this();
		this.name = name;
		this.dateTime = dateTime;
		this.location = location;
		this.eventPlanner = eventPlanner;
		this.client = client;
		this.guestList = guestList;
		this.tableSize = tableSize;
		this.emptySeatsPerTable = emptySeatsPerTable;
		this.minFitness = minFitness;
		this.iterations = iterations;
		this.status = status;
	}

	public int getEventId() {
		return eventId;
	}

	public void setId(int eventId) {
		this.eventId = eventId;
	}

	public String getName() {
		return name;
	}

	@XmlElement
	public void setName(String name) {

		this.name = name;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	@XmlElement
	public void setDateTime(LocalDateTime dateTime) {

		this.dateTime = dateTime;
	}

	public String getLocation() {
		return location;
	}

	@XmlElement
	public void setLocation(String location) {

		this.location = location;
	}

	public EventPlanner getEventPlanner() {
		return eventPlanner;
	}

	@XmlElement
	public void setEventPlanner(EventPlanner eventPlanner) {

		this.eventPlanner = eventPlanner;
	}

	public Client getClient() {
		return client;
	}

	@XmlElement
	public void setClient(Client client) {

		this.client = client;
	}

	public HashMap<String, Guest> getGuestList() {
		return guestList;
	}

	public Boolean addGuest(Guest guest) {
		guest.eventID = this.eventId;
		guestList.put(guest.getName(), guest);
		GuestDAO.addGuest(guest);
		return true;
	}

	public Boolean removeGuest(Guest guest) {
		GuestDAO.removeGuest(guest);
		return true;
	}

	public List<Guest> getAllGuests(int page, int perPage) {
		List<Guest> tempGuestList = GuestDAO.getAllGuestsForEvent(this, page, perPage);
		return tempGuestList;
	}

	public Integer getTableSize() {
		return tableSize;
	}

	@XmlElement
	public void setTableSize(Integer tableSize) {

		this.tableSize = tableSize;
	}

	public Integer getEmptySeatsPerTable() {
		return emptySeatsPerTable;
	}

	@XmlElement
	public void setEmptySeatsPerTable(Integer emptySeatsPerTable) {

		this.emptySeatsPerTable = emptySeatsPerTable;
	}

	public SeatingArrangement getSeatingArrangement() {
		return seatingArrangement;
	}

	@XmlElement
	public void setSeatingArrangement(SeatingArrangement seatingArrangement) {

		this.seatingArrangement = seatingArrangement;
	}

	public Integer getMinFitness() {
		return minFitness;
	}

	@XmlElement
	public void setMinFitness(Integer minFitness) {

		this.minFitness = minFitness;
	}

	public Integer getIterations() {
		return iterations;
	}

	@XmlElement
	public void setIterations(Integer iterations) {

		this.iterations = iterations;
	}

	public String getStatus() {
		return status;
	}

	@XmlElement
	public void setStatus(String status) {

		this.status = status;
	}

	public Boolean save() {

		EventDAO.saveEvent(this);
		return true;

	}

	public Integer getNumberTables() {
		return seatingArrangement.getTables().size();
	}

	public void calculateSeatingArrangement() {
		// TODO
		// insert genetic algorithm here
	}

	public void printGuestList(String sortingMethod) {
		// TODO
	}

	public void printPlaceCards() {
		// TODO
	}
}
