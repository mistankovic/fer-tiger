package hr.fer.zari.rasip.tiger.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints=@UniqueConstraint(columnNames={"collecting_unit", "name"}), 
	indexes = { @Index(name="sensor_name_collecting_unit_id_idx", columnList="collecting_unit, name") }
)
public class Sensor implements Serializable {

	private static final long serialVersionUID = -5200164086693117748L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	protected String name;

	@ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, optional=false)
	protected CollectingUnit collectingUnit;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "sensor")
	protected Set<SensorField> sensorFields = new HashSet<SensorField>();

	public Sensor() {
	}

	public Sensor(String name, CollectingUnit collectingUnit) {
		super();
		this.name = name;
		this.collectingUnit = collectingUnit;
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

	public CollectingUnit getCollectingUnit() {
		return collectingUnit;
	}

	public void setCollectingUnit(CollectingUnit collectingUnit) {
		this.collectingUnit = collectingUnit;
	}

	public Set<SensorField> getSensorFields() {
		return sensorFields;
	}

	public void setSensorFields(Set<SensorField> sensorFields) {
		this.sensorFields = sensorFields;
	}

	public void addSensorField(SensorField sensorField) {
		if(sensorField != null){
			sensorFields.add(sensorField);
			sensorField.setSensor(this);
		}
	}
	
	@Override
	public String toString() {
		return "Sensor [id=" + id + ", name=" + name + ", collectingUnit="
				+ collectingUnit.getName() + ", sensorFields number="
				+ sensorFields.size() + "]";
	}


}
