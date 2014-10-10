package hr.fer.zari.rasip.tiger.service;

import hr.fer.zari.rasip.tiger.domain.CollectingUnit;
import hr.fer.zari.rasip.tiger.domain.Sensor;
import hr.fer.zari.rasip.tiger.domain.SensorField;
import hr.fer.zari.rasip.tiger.rest.model.SensorFieldRestModel;
import hr.fer.zari.rasip.tiger.rest.model.SensorRestModel;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface SensorService {

	List<Sensor> fetchSensors(CollectingUnit cu);

	List<SensorRestModel> convert(Collection<Sensor> sensors);

	SensorRestModel convert(Sensor sensor);

	List<Sensor> convertAll(Collection<SensorRestModel> sensors);

	Sensor convert(SensorRestModel sensorRestModel);

	Set<SensorField> convertSensorFields(List<SensorFieldRestModel> fields);

	void deleteAll(Set<Sensor> sensors);

	Sensor findByNameAndCollectingUnit(String name, CollectingUnit collectingUnit);
}