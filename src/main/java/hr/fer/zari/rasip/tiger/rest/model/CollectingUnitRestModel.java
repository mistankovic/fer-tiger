package hr.fer.zari.rasip.tiger.rest.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CollectingUnitRestModel {

	protected Long id;

	protected String name;

	protected String locationUrl;

	protected Long collectDelay;

	protected Date dateCreated;

	protected Date lastCollected;

	protected Date dateArchived;

	protected String username;

	protected String password;

	public CollectingUnitRestModel() {
	}

	public CollectingUnitRestModel(Long id, String name, String locationUrl,
			Date dateCreated, Date lastCollected, Date dateArchived) {

		this();
		this.id = id;
		this.name = name;
		this.locationUrl = locationUrl;
		this.dateCreated = dateCreated;
		this.lastCollected = lastCollected;
		this.dateArchived = dateArchived;
	}

	public CollectingUnitRestModel(Long id, String name, String username,
			String password, String locationUrl, Date dateCreated,
			Date lastCollected, Date dateArchived) {

		this(id, name, locationUrl, dateCreated, lastCollected, dateArchived);
		this.username = username;
		this.password = password;

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocationUrl() {
		return locationUrl;
	}

	public void setLocationUrl(String location) {
		this.locationUrl = location;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getLastCollected() {
		return lastCollected;
	}

	public void setLastCollected(Date lastCollected) {
		this.lastCollected = lastCollected;
	}

	public Date getDateArchived() {
		return dateArchived;
	}

	public void setDateArchived(Date dateArchived) {
		this.dateArchived = dateArchived;
	}

	public Long getCollectDelay() {
		return collectDelay;
	}

	public void setCollectDelay(Long collectDelay) {
		this.collectDelay = collectDelay;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
