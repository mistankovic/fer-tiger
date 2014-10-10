package hr.fer.zari.rasip.tiger.service.impl;

import hr.fer.zari.rasip.tiger.dao.jpa.SensorFieldRepository;
import hr.fer.zari.rasip.tiger.dao.mongo.MeasurementRepository;
import hr.fer.zari.rasip.tiger.domain.Measurement;
import hr.fer.zari.rasip.tiger.domain.Sensor;
import hr.fer.zari.rasip.tiger.domain.SensorField;
import hr.fer.zari.rasip.tiger.rest.model.MeasurementGraphModel;
import hr.fer.zari.rasip.tiger.rest.model.MeasurementRestModel;
import hr.fer.zari.rasip.tiger.service.MeasurementService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MeasurementServiceImpl implements MeasurementService {

	private MeasurementRepository measurementRepository;
	private SensorFieldRepository sensorFieldRepository;

	@Autowired
	public MeasurementServiceImpl(MeasurementRepository measurementRepository,
			SensorFieldRepository sensorFieldRepository) {

		this.measurementRepository = measurementRepository;
		this.sensorFieldRepository = sensorFieldRepository;
	}

	@Override
	public Measurement findById(String id) {
		return measurementRepository.findOne(id);
	}

	@Override
	public Measurement save(Measurement measurement) {
		if (measurement == null) {
			throw new IllegalArgumentException(
					"Provided measurement is invalid: " + measurement);
		}
		return measurementRepository.save(measurement);
	}

	@Override
	public List<Measurement> save(Iterable<Measurement> measurements) {
		return measurementRepository.save(measurements);
	}

	@Override
	public List<Measurement> findNumberOfLastMeasured(int recentNumber) {
		if (recentNumber <= 0) {
			return new ArrayList<>();
		}

		Pageable pageable = new PageRequest(0, recentNumber);
		List<Measurement> results = measurementRepository
				.findAllOrderByDateMeasured(pageable);
		return results;
	}

	@Override
	public List<MeasurementRestModel> convert(Iterable<Measurement> measurements) {
		if (measurements != null) {
			return convertInternal(measurements);
		}
		return null;
	}

	@Override
	public MeasurementRestModel convert(Measurement measurement) {
		if (measurement != null) {
			return convertInternal(measurement);
		}
		return null;
	}

	private List<MeasurementRestModel> convertInternal(
			Iterable<Measurement> measurements) {
		List<MeasurementRestModel> dtos = new ArrayList<>();
		for (Measurement measurement : measurements) {
			dtos.add(convertInternal(measurement));
		}
		return dtos;
	}

	private MeasurementRestModel convertInternal(Measurement measurement) {
		SensorField sensorField = sensorFieldRepository.findOne(measurement
				.getSensorFieldId());
		if (sensorField == null) {
			throw new RuntimeException("Measurement with unknown sensor field.");
		}

		return new MeasurementRestModel(measurement, sensorField);

	}

	@Override
	public void delete(Measurement measurement) {
		deleteMeasurementInternal(measurement);
	}

	@Override
	public void deleteAllSensorMeasurements(Sensor sensor) {
		if (sensor != null && sensor.getId() != null) {
			deleteAllSensorMeasurementsInternal(sensor);
		}
	}

	@Override
	public void deleteAllSensorsMeasurements(Iterable<Sensor> sensors) {
		for (Sensor sensor : sensors) {
			if (sensor != null && sensor.getId() != null) {
				deleteAllSensorMeasurementsInternal(sensor);
			}
		}
	}

	private void deleteAllSensorMeasurementsInternal(Sensor sensor) {
		Set<SensorField> fields = sensor.getSensorFields();
		Set<Measurement> measurements = new HashSet<>();
		for (SensorField sensorField : fields) {
			Set<Measurement> fieldMeasurements = measurementRepository.findAllBySensorFieldId(sensorField.getId());
			measurements.addAll(fieldMeasurements);
		}
		measurementRepository.delete(measurements);
	}

	private void deleteMeasurementInternal(Measurement measurement) {
		if (measurement != null && measurement.getId() != null) {
			measurementRepository.delete(measurement);
		}
	}

	@Override
	public List<Measurement> findAllWithSensorIdBetweenDates(
			Long sensorFieldId, Date from, Date to) {
		if (sensorFieldId != null && from != null && to != null) {
			return measurementRepository.findAllBySensorFieldIdAndDateMeasuredBetweenOrderByDateMeasuredAsc(sensorFieldId, from, to);
		}

		throw new IllegalArgumentException(String.format("Bad parameters given. Sensor field id: %s, date from: %s, date to: %s",
						sensorFieldId, from, to));
	}

	@Override
	public List<MeasurementGraphModel> convertToGraphModel(
			Collection<Measurement> measurements) {
		if (measurements == null) {
			throw new RuntimeException("Measurements to convert are null.");
		}
		Map<Long, List<List<Object>>> series = new HashMap<>();
		for (Measurement measurement : measurements) {
			if (measurement != null) {
				addMeasurementToGraphModelInternal(series, measurement);
			}
		}

		List<MeasurementGraphModel> graphModels = new ArrayList<>(
				measurements.size());
		for (Long sensorFieldId : series.keySet()) {
			SensorField sensorField = sensorFieldRepository.findOne(sensorFieldId);

			if (sensorField != null) {
				String key = sensorField.getName();
				MeasurementGraphModel graphModel = new MeasurementGraphModel();
				graphModel.setKey(key);
				graphModel.setValues(series.get(sensorFieldId));

				graphModels.add(graphModel);
			}
		}

		return graphModels;
	}

	private void addMeasurementToGraphModelInternal(
			Map<Long, List<List<Object>>> series, Measurement measurement) {
		Long sensorFieldId = measurement.getSensorFieldId();
		List<List<Object>> values;
		if (!series.containsKey(sensorFieldId)) {
			values = new ArrayList<>();
			series.put(sensorFieldId, values);
		} else {
			values = series.get(sensorFieldId);
		}

		Date dateMeasured = measurement.getDateMeasured();
		Object value = measurement.getValue();
		if (dateMeasured != null && value != null) {
			List<Object> list = new ArrayList<>();

			list.add(dateMeasured.getTime());
			list.add(value);
			
			values.add(list);
		}
	}

	@Override
	public void orderBySensorFileAndDateMeasured(List<Measurement> measurements) {
		Collections.sort(measurements, new Comparator<Measurement>() {

			@Override
			public int compare(Measurement o1, Measurement o2) {
				int sensorFieldsCompare = Long.compare(o1.getSensorFieldId(), o2.getSensorFieldId());
				if(sensorFieldsCompare != 0){
					return sensorFieldsCompare;
				}
				
				Date measured1 = o1.getDateMeasured();
				Date measured2 = o2.getDateMeasured();
				
				return measured1.compareTo(measured2);
			}
			
		});
		
	}
}
