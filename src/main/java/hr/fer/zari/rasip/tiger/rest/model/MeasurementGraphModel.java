package hr.fer.zari.rasip.tiger.rest.model;

import java.util.List;

public class MeasurementGraphModel {

	protected String key;
	protected List<List<Object>> values;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public List<List<Object>> getValues() {
		return values;
	}

	public void setValues(List<List<Object>> values) {
		this.values = values;
	}

}
