package hr.fer.zari.rasip.tiger.rest.model;

import hr.fer.zari.rasip.tiger.domain.Measurement;
import hr.fer.zari.rasip.tiger.domain.Sensor;
import hr.fer.zari.rasip.tiger.domain.SensorField;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class MeasurementRestModel {

	protected String id;

	protected String value;

	protected String sensorFieldName;
	
	protected Long sensorFieldId;
	
	protected Long sensorId;
	
	protected String sensorName;
	
	protected String collectingUnitName;
	
	protected Date dateMeasured;
	
	public MeasurementRestModel() {}

	public MeasurementRestModel(Measurement measurement, SensorField sensorField) {
		this.id = measurement.getId();
		this.value = measurement.getValue();
		this.sensorFieldId = sensorField.getId();
		this.sensorFieldName = sensorField.getName();
		this.dateMeasured = measurement.getDateMeasured();
		
		Sensor sensor = sensorField.getSensor();
		this.sensorId = sensor.getId();
		this.sensorName = sensor.getName();
		this.collectingUnitName = sensor.getCollectingUnit().getName();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getSensorFieldName() {
		return sensorFieldName;
	}

	public void setSensorFieldName(String sensorFieldName) {
		this.sensorFieldName = sensorFieldName;
	}

	public Long getSensorFieldId() {
		return sensorFieldId;
	}

	public void setSensorFieldId(Long sensorFieldId) {
		this.sensorFieldId = sensorFieldId;
	}

	public Long getSensorId() {
		return sensorId;
	}

	public void setSensorId(Long sensorId) {
		this.sensorId = sensorId;
	}

	public String getSensorName() {
		return sensorName;
	}

	public void setSensorName(String sensorName) {
		this.sensorName = sensorName;
	}

	public String getCollectingUnitName() {
		return collectingUnitName;
	}

	public void setCollectingUnitName(String collectingUnitName) {
		this.collectingUnitName = collectingUnitName;
	}

	public Date getDateMeasured() {
		return dateMeasured;
	}

	public void setDateMeasured(Date dateMeasured) {
		this.dateMeasured = dateMeasured;
	}

}
