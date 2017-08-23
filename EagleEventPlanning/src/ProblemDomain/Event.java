package ProblemDomain;

import java.io.FileReader;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Hashtable;
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

// I used the jar from the zip at https://examples.javacodegeeks.com/core-java/sql/import-csv-file-to-mysql-table-java-example/
// seems like it's the current one from https://sourceforge.net/projects/opencsv/ even though the article was 3 years ago and opencsv was updated two days ago
import com.opencsv.CSVReader;

import DataAccessObjects.EventDAO;
import DataAccessObjects.GuestDAO;
import TableSorter.Layout;

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
	@JoinColumn(name = "eventPlanner", referencedColumnName = "eventPlanner_id")
	public EventPlanner eventPlanner;

	@Column(name = "eventPlannerID", nullable = false)
	public long eventPlannerID;

	@ManyToOne(optional = false)
	@JoinColumn(name = "client", referencedColumnName = "client_id")
	public Client client;

	@Column(name = "clientID", nullable = false)
	public long clientID;

	@OneToMany(mappedBy = "event", targetEntity = Guest.class, fetch = FetchType.EAGER)
	private Hashtable<String, Guest> guestList = new Hashtable<String, Guest>();

	@Column(name = "tableSize", nullable = false)
	private Integer tableSize;

	@Column(name = "emptySeatsPerTable", nullable = false)
	private Integer emptySeatsPerTable;

	@Transient
	private Layout seatingArrangement;

	@Column(name = "status", nullable = false, length = 20)
	private String status;

	public Event() {
	}

	public Event(String name) {
		this();
		this.name = name;
	}

	public Event(String name, LocalDateTime dateTime, String location, EventPlanner eventPlanner, Client client,
			Hashtable<String, Guest> guestList, Integer tableSize, Integer emptySeatsPerTable, String status) {
		this();
		this.name = name;
		this.dateTime = dateTime;
		this.location = location;
		this.eventPlanner = eventPlanner;
		this.client = client;
		this.guestList = guestList;
		this.tableSize = tableSize;
		this.emptySeatsPerTable = emptySeatsPerTable;
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
	public void setDateTime(String dateTime) {
		this.dateTime = LocalDateTime.parse(dateTime);
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

	public long getEventPlannerID() {
		return eventPlannerID;
	}

	@XmlElement
	public void setEventPlannerID(long eventPlannerID) {
		this.eventPlannerID = eventPlannerID;
	}

	public Client getClient() {
		return client;
	}

	@XmlElement
	public void setClient(Client client) {
		this.client = client;
	}

	public long getClientID() {
		return clientID;
	}

	@XmlElement
	public void setClientID(long clientID) {
		this.clientID = clientID;
	}

	public Hashtable<String, Guest> getGuestList() {
		return guestList;
	}

	public Boolean addGuest(Guest guest) {
		guest.eventID = this.eventId;
		this.guestList.put(guest.getName(), guest);
		GuestDAO.addGuest(guest);
		return true;
	}

	public Boolean removeGuest(Guest guest) {
		GuestDAO.removeGuest(guest);
		return true;
	}

	public List<Guest> getAllGuests(int page, int perPage) {
		List<Guest> tempGuestList = GuestDAO.getAllGuestsForEvent(this.eventId, page, perPage);
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

	public Layout getSeatingArrangement() {
		return seatingArrangement;
	}

	@XmlElement
	public void setSeatingArrangement(Layout seatingArrangement) {
		this.seatingArrangement = seatingArrangement;
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
		return (seatingArrangement == null) ? 0 : seatingArrangement.tableList.size();
	}

	public void calculateSeatingArrangement() throws Exception {
		if (this.tableSize == null)
			this.tableSize = 8;
		if (this.emptySeatsPerTable == null)
			this.emptySeatsPerTable = 2;

		this.seatingArrangement = TableSorter.GA.runGA(guestList.values(), this.tableSize, this.emptySeatsPerTable);
	}

	public String printGuestList() {
		return this.seatingArrangement.printLayout();
	}

	public void printPlaceCards() {
		// TODO
	}

	public void importGuests(String csvFile) {
		try (CSVReader reader = new CSVReader(new FileReader(csvFile), ',');) {
			String[] rowData = null;
			ArrayList<String> rowHeader = new ArrayList<String>();
			boolean firstLine = true;
			int i = 0;
			int guestNumber = 1;
			String firstName = "";
			String lastName = "";
			ArrayList<Integer> sameTable = new ArrayList<Integer>();
			ArrayList<Integer> notSameTable = new ArrayList<Integer>();
			int tableNumber = -1;

			while ((rowData = reader.readNext()) != null) {
				for (String data : rowData) {
					// the first line of the file is column headers
					if (firstLine) {
						rowHeader.add(data);
					} else { // set the appropriate field
						String value = rowHeader.get(i);
						switch (value) {
						case "Guest #":
							break;
						case "First Name":
							firstName = data;
							break;
						case "Last Name":
							lastName = data;
							break;
						case "Same Table":
							try {
								sameTable.add(Integer.parseInt(data));
							} catch (NumberFormatException nfex) {
								/* bad value, ignore it */ }
							break;
						case "Not Same Table":
							try {
								notSameTable.add(Integer.parseInt(data));
							} catch (NumberFormatException nfex) {
								/* bad value, ignore it */ }
							break;
						case "Table Number":
							tableNumber = Integer.parseInt(data);
							break;
						default:
							break;
						}

						// move to the next column
						i++;
						// when you reach the end of a row add the guest, return
						// to the first header index, and sanitize the fields
						// There's a major issue with trying to parse the first
						// header in the csv file. Because it's the guest
						// number,
						// there are other ways to track it - zak
						if (i == rowHeader.size()) {
							Guest currentGuest = new Guest(guestNumber, firstName, lastName,
									new ArrayList<Integer>(sameTable), new ArrayList<Integer>(notSameTable),
									tableNumber);
							addGuest(currentGuest);

							i = 0;
							guestNumber++;
							firstName = "";
							lastName = "";
							sameTable.clear();
							notSameTable.clear();
							tableNumber = -1;
						}
					}
				}

				if (firstLine) {
					firstLine = false;
				}
			}
			calculateSeatingArrangement();
			java.lang.System.out.println("Data Successfully Uploaded");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Message> validate() {
		// TODO Auto-generated method stub
		return null;
	}

	public Boolean update(Event event) {
		setClient(event.client);
		setClientID(event.getClientID());
		setDateTime(event.dateTime.toString());
		setEmptySeatsPerTable(event.emptySeatsPerTable);
		setEventPlanner(event.eventPlanner);
		setEventPlannerID(event.eventPlannerID);
		setId(event.eventId);
		setLocation(event.location);
		setName(event.name);
		setSeatingArrangement(event.seatingArrangement);
		setStatus(event.status);
		setTableSize(event.tableSize);
		return true;
	}
}
