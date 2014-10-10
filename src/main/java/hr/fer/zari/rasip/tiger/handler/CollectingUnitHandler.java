package hr.fer.zari.rasip.tiger.handler;

import hr.fer.zari.rasip.tiger.domain.CollectingUnit;
import hr.fer.zari.rasip.tiger.domain.Sensor;
import hr.fer.zari.rasip.tiger.domain.SensorField;

import java.util.List;
import java.util.Map;

public interface CollectingUnitHandler {

	List<Sensor> fetchSensors(CollectingUnit collectingUnit);

	Map<String, String> headersForRequestToCollectingUnit(CollectingUnit collectingUnit);
	
	String convertMeasuredValue(SensorField sensorField, Object value);
}
