package hr.fer.zari.rasip.tiger.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class CollectingUnit implements Serializable {

	private static final long serialVersionUID = 8889838521884804684L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable=false)
	protected String name;

	protected String username;
	
	protected String password;

	@Column(unique = true, nullable=false)
	protected String locationUrl;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateCreated;

	protected Date dateArchived;

	protected Date lastCollected;
	
	@Column(nullable=false)
	protected Long collectDelay;

	@ManyToOne(optional=false)
	protected CollectingUnitType type;
	
	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="collectingUnit")
	protected Set<Sensor> sensors = new HashSet<>();

	@PrePersist
	void prePersist() {
		dateCreated = new Date();
	}

	public Long getId() {
		return id;
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

	public Date getDateArchived() {
		return dateArchived;
	}

	public void setDateArchived(Date dateArchived) {
		this.dateArchived = dateArchived;
	}

	public Date getLastCollected() {
		return lastCollected;
	}

	public void setLastCollected(Date lastCollected) {
		this.lastCollected = lastCollected;
	}

	public Long getCollectDelay() {
		return collectDelay;
	}

	public void setCollectDelay(Long collectDelay) {
		this.collectDelay = collectDelay;
	}

	public Set<Sensor> getSensors() {
		return sensors;
	}

	public void setSensors(Set<Sensor> sensors) {
		this.sensors = sensors;
	}

	public void addSensor(Sensor sensor){
		if(sensor != null){
			sensors.add(sensor);
			sensor.setCollectingUnit(this);
		}
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public CollectingUnitType getType() {
		return type;
	}
	
	public void setType(CollectingUnitType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "CollectingUnit [id=" + id + ", name=" + name + ", username="
				+ username + ", password=" + password + ", locationUrl="
				+ locationUrl + ", dateCreated=" + dateCreated
				+ ", dateArchived=" + dateArchived + ", lastCollected="
				+ lastCollected + ", collectDelay=" + collectDelay + ", type="
				+ type + ", sensors number=" + sensors.size() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((collectDelay == null) ? 0 : collectDelay.hashCode());
		result = prime * result
				+ ((dateArchived == null) ? 0 : dateArchived.hashCode());
		result = prime * result
				+ ((dateCreated == null) ? 0 : dateCreated.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((lastCollected == null) ? 0 : lastCollected.hashCode());
		result = prime * result
				+ ((locationUrl == null) ? 0 : locationUrl.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((sensors == null) ? 0 : sensors.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CollectingUnit other = (CollectingUnit) obj;
		if (collectDelay == null) {
			if (other.collectDelay != null)
				return false;
		} else if (!collectDelay.equals(other.collectDelay))
			return false;
		if (dateArchived == null) {
			if (other.dateArchived != null)
				return false;
		} else if (!dateArchived.equals(other.dateArchived))
			return false;
		if (dateCreated == null) {
			if (other.dateCreated != null)
				return false;
		} else if (!dateCreated.equals(other.dateCreated))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (lastCollected == null) {
			if (other.lastCollected != null)
				return false;
		} else if (!lastCollected.equals(other.lastCollected))
			return false;
		if (locationUrl == null) {
			if (other.locationUrl != null)
				return false;
		} else if (!locationUrl.equals(other.locationUrl))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (sensors == null) {
			if (other.sensors != null)
				return false;
		} else if (!sensors.equals(other.sensors))
			return false;
		if (type != other.type)
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}
	
}
