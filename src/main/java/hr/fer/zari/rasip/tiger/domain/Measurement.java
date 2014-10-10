package hr.fer.zari.rasip.tiger.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@CompoundIndex(name = "sensorId_dateMeasured_idx", def = "{'sensorFieldId' : 1, 'dateMeasured' : 1}")
public class Measurement implements Serializable {

	private static final long serialVersionUID = 90622329140849544L;

	@Id
	protected String id;

	protected String value;

	protected Long sensorFieldId;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dateCreated;

	protected Date dateMeasured;

	public Measurement() {
	}

	public Measurement(String value, Long sensorFieldId, Date dateMeasured) {
		super();
		this.value = value;
		this.sensorFieldId = sensorFieldId;
		this.dateMeasured = dateMeasured;
	}

	public String getId() {
		return id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Long getSensorFieldId() {
		return sensorFieldId;
	}
	
	public void setSensorFieldId(Long sensorFieldId) {
		this.sensorFieldId = sensorFieldId;
	}

	public Date getDateMeasured() {
		return dateMeasured;
	}

	public void setDateMeasured(Date dateMeasured) {
		this.dateMeasured = dateMeasured;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	@PrePersist
	void beforeInsert() {
		dateCreated = new Date();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dateMeasured == null) ? 0 : dateMeasured.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((sensorFieldId == null) ? 0 : sensorFieldId.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		Measurement other = (Measurement) obj;
		if (dateMeasured == null) {
			if (other.dateMeasured != null)
				return false;
		} else if (!dateMeasured.equals(other.dateMeasured))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (sensorFieldId == null) {
			if (other.sensorFieldId != null)
				return false;
		} else if (!sensorFieldId.equals(other.sensorFieldId))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Measurement [id=" + id + ", value=" + value + ", sensorId="
				+ sensorFieldId + ", dateCreated=" + dateCreated + ", dateMeasured="
				+ dateMeasured + "]";
	}

}
