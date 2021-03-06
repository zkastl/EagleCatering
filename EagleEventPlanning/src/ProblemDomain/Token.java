package ProblemDomain;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Random;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import DataAccessObjects.TokenDAO;

@XmlRootElement(name = "token")
@Entity(name = "token")
public class Token implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id // signifies the primary key
	@Column(name = "token_id", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long tokenID;
	@Column(name = "token", nullable = false, length = 40)
	private String token;
	@Column(name = "expire_date")
	private LocalDate expireDate;
	@ManyToOne(optional = false)
	@JoinColumn(name = "eventPlanner_id", referencedColumnName = "eventPlanner_id")
	private EventPlanner planner;

	public Token() {
	};

	public Token(EventPlanner planner) {
		setEventPlanner(planner);
		Random random = new SecureRandom();
		String token = new BigInteger(130, random).toString(32);
		setToken(token);
		setExpireDate(LocalDate.now());

	}

	public long getTokenID() {
		return this.tokenID;
	}

	public void setTokenID(long tokenID) {
		this.tokenID = tokenID;
	}

	public String getToken() {
		return this.token;
	}

	@XmlElement
	public void setToken(String token) {
		this.token = token;
	}

	public LocalDate getExpireDate() {
		return this.expireDate;
	}

	@XmlElement
	public void setExpireDate(LocalDate expireDate) {
		this.expireDate = expireDate;
	}

	public EventPlanner getEventPlanner() {
		return this.planner;
	}

	@XmlElement
	public void setEventPlanner(EventPlanner planner) {
		this.planner = planner;
	}

	public Boolean validate() {
		return LocalDate.now().equals(getExpireDate());
	}

	public void save() {
		TokenDAO.saveToken(this);
	}

}