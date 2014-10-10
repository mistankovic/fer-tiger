package hr.fer.zari.rasip.tiger.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRoleRestModel {

	protected String name;

	protected String description;

	public UserRoleRestModel(String name, String description) {
		super();
		this.name = name;
		this.description = description;
	}

	public UserRoleRestModel() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
