package hr.fer.zari.rasip.tiger.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(indexes = { @Index(name = "collecting_unit_type_name_idx", columnList = "name") })
public class CollectingUnitType implements Serializable {

	private static final long serialVersionUID = -791878192999603616L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable=false)
	protected String name;

	@ElementCollection(fetch = FetchType.LAZY)
	protected Set<String> supportedMeasurementUnits = new HashSet<>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "type", fetch = FetchType.LAZY)
	protected Set<CollectingUnit> collectingUnits = new HashSet<>();

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		String nameUpperCased = name;
		if (name != null) {
			nameUpperCased = nameUpperCased.toUpperCase();
		}
		this.name = nameUpperCased;
	}

	public Set<String> getSupportedMeasurementUnits() {
		return supportedMeasurementUnits;
	}

	public void setSupportedMeasurementUnits(
			Set<String> supportedMeasurementUnits) {
		this.supportedMeasurementUnits = supportedMeasurementUnits;
	}

	public void addSupportedMeasurementUnit(String mesurementUnit) {
		if (mesurementUnit != null) {
			supportedMeasurementUnits.add(mesurementUnit);
		}
	}


	public Set<CollectingUnit> getCollectingUnits() {
		return collectingUnits;
	}

	public void setCollectingUnits(Set<CollectingUnit> collectingUnits) {
		this.collectingUnits = collectingUnits;
	}

	public void addCollectingUnit(CollectingUnit collectingUnit) {
		if (collectingUnit != null) {
			collectingUnits.add(collectingUnit);
			collectingUnit.setType(this);
		}
	}
}
