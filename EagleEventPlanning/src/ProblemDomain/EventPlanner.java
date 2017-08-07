package ProblemDomain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import DataAccessObjects.EventPlannerDAO;

@XmlRootElement(name = "eventPlanner")
@Entity(name = "eventPlanner")
public class EventPlanner implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id // signifies the primary key
	@Column(name = "eventPlanner_id", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long eventPlannerId;

	@Column(name = "name", nullable = false, length = 50)
	private String name;

	@Column(name = "phoneNumber", nullable = false, length = 10)
	private String phoneNumber;

	@Column(name = "email", nullable = false, length = 50)
	private String email;

	@Column(name = "userName", nullable = false, length = 50)
	private String userName;

	@Column(name = "password", nullable = false, length = 50)
	private String password;

	@Column(name = "role", nullable = false, length = 50)
	private String role;

	public EventPlanner() {
	}

	public EventPlanner(String name, String phoneNumber, String email, String userName, String password, String role) {

		this.name = name;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.userName = userName;
		this.password = password;
		this.role = role;

	}

	public long getEventPlannerId() {
		return eventPlannerId;
	}

	public void setId(int eventPlannerId) {
		this.eventPlannerId = eventPlannerId;
	}

	public String getName() {
		return name;
	}

	@XmlElement
	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	@XmlElement
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	@XmlElement
	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserName() {
		return userName;
	}

	@XmlElement
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	@XmlElement
	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	@XmlElement
	public void setRole(String role) {
		this.role = role;
	}

	public Boolean save() {

		EventPlannerDAO.saveEventPlanner(this);
		return true;

	}

	public Boolean delete() {

		EventPlannerDAO.removeEventPlanner(this);

		return true;

	}

	public Boolean update(EventPlanner eventPlanner) {

		setName(eventPlanner.getName());
		setPhoneNumber(eventPlanner.getPhoneNumber());
		setEmail(eventPlanner.getEmail());
		setUserName(eventPlanner.getUserName());
		setPassword(eventPlanner.getPassword());
		setRole(eventPlanner.getRole());

		return true;
	}

	public boolean authenticate(String password) {
		return this.password.equals(password);
	}

}
