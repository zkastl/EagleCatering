package ProblemDomain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import DataAccessObjects.EventPlannerDAO;
import DataAccessObjects.RoleAssignmentDAO;

@XmlRootElement(name = "eventplanner")
@Entity(name = "eventplanner")
public class EventPlanner implements Serializable {

	private static final long serialVersionUID = 1L;

	@OneToMany(mappedBy = "planner", targetEntity = RoleAssignment.class, fetch = FetchType.EAGER)
	private Collection<RoleAssignment> roleAssignments;

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

	public EventPlanner() {
	}

	public EventPlanner(String name, String phoneNumber, String email, String userName, String password, String role) {

		this.name = name;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.userName = userName;
		this.password = password;
	}

	public long getEventPlannerId() {
		return eventPlannerId;
	}

	public Collection<RoleAssignment> getRoleAssignments() {
		return this.roleAssignments;
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

	@XmlElement
	public void setRoleAssignments(Collection<RoleAssignment> roleAssignments) {
		this.roleAssignments = roleAssignments;
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
		if (eventPlanner.getRoleAssignments() != null) {
			for (RoleAssignment ra : this.getRoleAssignments()) {
				removeRoleAssignment(ra);
			}
			for (RoleAssignment ra : eventPlanner.getRoleAssignments()) {
				addRoleAssignment(ra);
			}
		}

		return true;
	}

	public boolean authenticate(String password) {
		return this.password.equals(password);
	}

	public ArrayList<Message> validate() {
		// TODO Auto-generated method stub
		return null;
	}

	public void removeRoleAssignment(RoleAssignment ra) {
		RoleAssignmentDAO.removeRoleAssignment(ra);
	}

	public void addRoleAssignment(RoleAssignment ra) {
		ra.setEventPlanner(this);
		RoleAssignmentDAO.saveRoleAssignment(ra);
	}

	public boolean isAuthorized(Role role) {
		for (RoleAssignment ra : getRoleAssignments()) {
			if (ra.getRole().equals(role))
				return true;
		}
		return false;
	}

}
