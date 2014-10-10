package hr.fer.zari.rasip.tiger.rest.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class CollectingUnitRegistrationRestModel {

	private String name;

	private String username;

	private String password;

	private String locationUrl;

	private Long collectingDelay;

	private String type;

	private Collection<SensorRestModel> sensors = new ArrayList<>();

	private Date collectStartDate;

	public CollectingUnitRegistrationRestModel() {
	}

	public CollectingUnitRegistrationRestModel(String name, String username,
			String password, String locationUrl, String type, Long collectingDelay) {

		this.name = name;
		this.username = username;
		this.password = password;
		this.locationUrl = locationUrl;
		this.type = type;
		this.collectingDelay = collectingDelay;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getLocationUrl() {
		return locationUrl;
	}

	public void setLocationUrl(String locationUrl) {
		this.locationUrl = locationUrl;
	}

	public Long getCollectingDelay() {
		return collectingDelay;
	}

	public void setCollectingDelay(Long collectingDelay) {
		this.collectingDelay = collectingDelay;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Collection<SensorRestModel> getSensors() {
		return sensors;
	}

	public void setSensors(Collection<SensorRestModel> sensors) {
		this.sensors = sensors;
	}

	public Date getCollectStartDate() {
		return collectStartDate;
	}

	public void setCollectStartDate(Date collectStartDate) {
		this.collectStartDate = collectStartDate;
	}

}
