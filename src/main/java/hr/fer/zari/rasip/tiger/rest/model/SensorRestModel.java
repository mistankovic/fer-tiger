package hr.fer.zari.rasip.tiger.rest.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class SensorRestModel {

	protected Long id;
	protected String name;
	protected List<SensorFieldRestModel> fields = new ArrayList<>();

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

	public List<SensorFieldRestModel> getFields() {
		return fields;
	}

	public void setFields(List<SensorFieldRestModel> fields) {
		this.fields = fields;
	}

}
