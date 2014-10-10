package hr.fer.zari.rasip.tiger.rest.model;

import java.util.Date;

public class AppUserRestModel {

	protected Long id;

	protected String email;

	protected String firstName;

	protected String lastName;

	protected Date dateCreated;

	public AppUserRestModel() {
	}

	public AppUserRestModel(Long id, String email, String firstName,
			String lastName, Date dateCreated) {
		super();
		this.id = id;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateCreated = dateCreated;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

}
