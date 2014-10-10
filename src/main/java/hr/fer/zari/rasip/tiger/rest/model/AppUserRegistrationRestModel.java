package hr.fer.zari.rasip.tiger.rest.model;

import java.util.Collection;
import java.util.HashSet;

public class AppUserRegistrationRestModel {

	public AppUserRegistrationRestModel(String email, String firstName,	String lastName) {
		
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public AppUserRegistrationRestModel() {
	}

	protected String email;

	protected String firstName;

	protected String lastName;

	protected String password;

	protected Collection<UserRoleRestModel> roles = new HashSet<>();

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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Collection<UserRoleRestModel> getRoles() {
		return roles;
	}

	public void setRoles(Collection<UserRoleRestModel> roles) {
		this.roles = roles;
	}

}
