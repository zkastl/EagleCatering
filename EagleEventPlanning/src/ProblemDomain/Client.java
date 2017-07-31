package ProblemDomain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import DataAccessObjects.ClientDAO;

@XmlRootElement(name = "client")
@Entity(name = "client")
public class Client implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id // signifies the primary key
	@Column(name = "client_id", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long clientId;

	@Column(name = "name", nullable = false, length = 20)
	private String name;

	@Column(name = "phoneNumber", nullable = false, length = 10)
	private String phoneNumber;

	@Column(name = "email", nullable = false, length = 50)
	private String email;

	public Client() {
	}

	public Client(String name, String phoneNumber, String email) {

		this.name = name;
		this.phoneNumber = phoneNumber;
		this.email = email;

	}

	public long getClientId() {
		return clientId;
	}

	public void setId(int clientId) {
		this.clientId = clientId;
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

	public Boolean save() {

		ClientDAO.saveClient(this);
		return true;

	}

	public Boolean delete() {

		ClientDAO.removeClient(this);

		return true;

	}

	public Boolean update(Client client) {

		setName(client.getName());
		setPhoneNumber(client.getPhoneNumber());
		setEmail(client.getEmail());

		return true;
	}

}
