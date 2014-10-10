package hr.fer.zari.rasip.tiger.service.impl;

import hr.fer.zari.rasip.tiger.dao.jpa.SensorRepository;
import hr.fer.zari.rasip.tiger.domain.CollectingUnit;
import hr.fer.zari.rasip.tiger.domain.Sensor;
import hr.fer.zari.rasip.tiger.domain.SensorField;
import hr.fer.zari.rasip.tiger.handler.CollectingUnitHandler;
import hr.fer.zari.rasip.tiger.handler.CollectingUnitHandlerFactory;
import hr.fer.zari.rasip.tiger.rest.model.SensorFieldRestModel;
import hr.fer.zari.rasip.tiger.rest.model.SensorRestModel;
import hr.fer.zari.rasip.tiger.service.SensorService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SensorServiceImpl implements SensorService {

	private SensorRepository sensorRepository;

	@Autowired
	public SensorServiceImpl(SensorRepository sensorRepository) {
		this.sensorRepository = sensorRepository;
	}

	@Override
	public List<Sensor> fetchSensors(CollectingUnit cu) {
		List<Sensor> result = new ArrayList<>(0);
		if(cu != null){
			CollectingUnitHandler handler = CollectingUnitHandlerFactory.handler(cu);
			result = handler.fetchSensors(cu);
		}
		return result;
	}

	@Override
	public List<SensorRestModel> convert(Collection<Sensor> sensors) {
		List<SensorRestModel> result = null;
		if(sensors != null){
			result = new ArrayList<>(sensors.size());
			for (Sensor sensor : sensors) {
				if(sensor != null){
					result.add(convertInternal(sensor));
				}
			}
		}
		
		return result;
	}

	@Override
	public SensorRestModel convert(Sensor sensor) {
		if(sensor != null){
			return convertInternal(sensor);
		}
		return null;
	}
	
	private SensorRestModel convertInternal(Sensor sensor){
		SensorRestModel srm = new SensorRestModel();
		srm.setName(sensor.getName());
		srm.setId(sensor.getId());
		List<SensorFieldRestModel> fields = new ArrayList<>();
		Set<SensorField> set = sensor.getSensorFields();
		for (SensorField sf : set) {
			fields.add(convertInternal(sf));
		}
		
		srm.setFields(fields);
		
		return srm;
	}

	private SensorFieldRestModel convertInternal(SensorField sf) {
		SensorFieldRestModel sfrm = new SensorFieldRestModel();
		
		sfrm.setId(sf.getId());
		sfrm.setName(sf.getName());
		sfrm.setType(sf.getUnit());
		
		return sfrm;
	}

	@Override
	public List<Sensor> convertAll(Collection<SensorRestModel> models) {
		List<Sensor> result = null;
		if(models != null){
			result = new ArrayList<>(models.size());
			for (SensorRestModel model : models) {
				if(model != null){
					result.add(convertInternal(model));
				}
			}
		}
		
		return result;
	}
	
	@Override
	public Sensor convert(SensorRestModel model){
		if(model != null){
			return convertInternal(model);
		}
		return null;
	}

	private Sensor convertInternal(SensorRestModel model) {
		Sensor sensor = new Sensor();
		sensor.setName(model.getName());
		Set<SensorField> sensorFields = convertSensorFields(model.getFields());
		for (SensorField sensorField : sensorFields) {
			sensor.addSensorField(sensorField);
		}
		return sensor;
	}

	@Override
	public Set<SensorField> convertSensorFields(List<SensorFieldRestModel> fields) {
		Set<SensorField> result = null;
		if(fields != null){
			result = new HashSet<>();
			for (SensorFieldRestModel model : fields) {
				if(model != null){
					result.add(convertInternal(model));
				}
			}
		}
		
		return result;
		
	}

	private SensorField convertInternal(SensorFieldRestModel model) {
		SensorField sf = new SensorField();
		sf.setName(model.getName());
		sf.setUnit(model.getType());
		
		return sf;
	}

	@Override
	public void deleteAll(Set<Sensor> sensors) {
		if(sensors == null){
			throw new IllegalArgumentException("Null reference for sensors to delete.");
		}
		sensorRepository.delete(sensors);
		
	}

	@Override
	public Sensor findByNameAndCollectingUnit(String name,CollectingUnit collectingUnit) {
		return sensorRepository.findByCollectingUnitAndName(collectingUnit, name);
	}

}
