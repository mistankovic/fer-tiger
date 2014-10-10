package hr.fer.zari.rasip.tiger.service;

import hr.fer.zari.rasip.tiger.domain.Measurement;
import hr.fer.zari.rasip.tiger.domain.Sensor;
import hr.fer.zari.rasip.tiger.rest.model.MeasurementGraphModel;
import hr.fer.zari.rasip.tiger.rest.model.MeasurementRestModel;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface MeasurementService {

	Measurement findById(String id);

	Measurement save(Measurement measurement);

	List<Measurement> save(Iterable<Measurement> measurements);

	List<Measurement> findNumberOfLastMeasured(int recentNumber);
	
	void delete(Measurement measurement);
	
	void deleteAllSensorMeasurements(Sensor sensor);
	
	void deleteAllSensorsMeasurements(Iterable<Sensor> sensors);

	List<MeasurementRestModel> convert(Iterable<Measurement> measurements);

	MeasurementRestModel convert(Measurement measurement);

	List<Measurement> findAllWithSensorIdBetweenDates(Long sensorFieldId, Date from, Date to);
	
	List<MeasurementGraphModel> convertToGraphModel(Collection<Measurement> measurements);
	
	void orderBySensorFileAndDateMeasured(List<Measurement> measurements);

}
