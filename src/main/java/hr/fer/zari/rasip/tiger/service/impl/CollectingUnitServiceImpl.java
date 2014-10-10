package hr.fer.zari.rasip.tiger.service.impl;

import hr.fer.zari.rasip.tiger.dao.jpa.CollectingUnitRepository;
import hr.fer.zari.rasip.tiger.domain.CollectingUnit;
import hr.fer.zari.rasip.tiger.domain.Sensor;
import hr.fer.zari.rasip.tiger.rest.model.CollectingUnitRegistrationRestModel;
import hr.fer.zari.rasip.tiger.rest.model.CollectingUnitRestModel;
import hr.fer.zari.rasip.tiger.rest.model.SensorRestModel;
import hr.fer.zari.rasip.tiger.service.CollectingUnitService;
import hr.fer.zari.rasip.tiger.service.CollectingUnitTypeService;
import hr.fer.zari.rasip.tiger.service.MeasurementService;
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
public class CollectingUnitServiceImpl implements CollectingUnitService {

	private CollectingUnitRepository collectingUnitRepository;
	private CollectingUnitTypeService collectingUnitTypeService;
	private SensorService sensorService;
	private MeasurementService measurementService;

	@Autowired
	public CollectingUnitServiceImpl(
			CollectingUnitRepository collectingUnitRepository,
			CollectingUnitTypeService collectingUnitTypeService,
			SensorService sensorService, MeasurementService measurementService) {
		this.collectingUnitRepository = collectingUnitRepository;
		this.collectingUnitTypeService = collectingUnitTypeService;
		this.sensorService = sensorService;
		this.measurementService = measurementService;
	}

	@Override
	public List<CollectingUnit> findAllCollectingUnits() {
		Iterable<CollectingUnit> all = collectingUnitRepository.findAll();
		if (all instanceof List) {
			return (List<CollectingUnit>) all;
		}

		List<CollectingUnit> list = new ArrayList<>();
		for (CollectingUnit collectingUnit : all) {
			list.add(collectingUnit);
		}

		return list;
	}

	@Override
	public CollectingUnit save(CollectingUnit collectingUnit) {
		CollectingUnit saved = null;
		if (collectingUnit != null) {
			saved = collectingUnitRepository.save(collectingUnit);
		}

		return saved;
	}

	@Override
	public CollectingUnit findById(Long id) {
		CollectingUnit cu = null;
		if (id != null) {
			cu = collectingUnitRepository.findOne(id);
		}
		return cu;
	}

	@Override
	public CollectingUnit convert(CollectingUnitRegistrationRestModel restModel) {
		CollectingUnit collectingUnit = new CollectingUnit();

		collectingUnit.setName(restModel.getName());
		collectingUnit.setUsername(restModel.getUsername());
		collectingUnit.setPassword(restModel.getPassword());
		collectingUnit.setLocationUrl(restModel.getLocationUrl());
		collectingUnit.setLastCollected(restModel.getCollectStartDate());
		collectingUnit.setType(collectingUnitTypeService.findByName(restModel.getType()));
		collectingUnit.setCollectDelay(restModel.getCollectingDelay());
		
		Collection<SensorRestModel> selected = restModel.getSensors();
		Set<Sensor> sensorsSet = new HashSet<>();
		sensorsSet.addAll(sensorService.convertAll(selected));

		for (Sensor sensor : sensorsSet) {
			sensor.setCollectingUnit(collectingUnit);
		}
		
		collectingUnit.setSensors(sensorsSet);

		return collectingUnit;
	}

	@Override
	public CollectingUnitRestModel convert(CollectingUnit cu) {
		if (cu != null) {
			return convertInternal(cu);
		}
		return null;
	}

	@Override
	public List<CollectingUnitRestModel> convert(
			List<CollectingUnit> collectingUnits) {
		List<CollectingUnitRestModel> list = null;
		if (collectingUnits != null) {
			list = new ArrayList<>(collectingUnits.size());
			for (CollectingUnit cu : collectingUnits) {
				if (cu != null) {
					list.add(convertInternal(cu));
				}
			}
		}
		return list;
	}

	private CollectingUnitRestModel convertInternal(CollectingUnit cu) {
		CollectingUnitRestModel model = new CollectingUnitRestModel();

		model.setId(cu.getId());
		model.setName(cu.getName());
		model.setUsername(cu.getUsername());
		model.setPassword(cu.getPassword());
		model.setDateCreated(cu.getDateCreated());
		model.setLastCollected(cu.getLastCollected());
		model.setLocationUrl(cu.getLocationUrl());
		model.setDateArchived(cu.getDateArchived());
		model.setCollectDelay(cu.getCollectDelay());
		
		return model;
	}

	@Override
	public void delete(Long id) {
		CollectingUnit collectingUnit = collectingUnitRepository.findOne(id);
		Set<Sensor> sensors = collectingUnit.getSensors();
		measurementService.deleteAllSensorsMeasurements(sensors);

		collectingUnitRepository.delete(collectingUnit);
	}

	@Override
	public List<CollectingUnit> findAllUnarchivedCollectingUnits() {
		return collectingUnitRepository.findAllByDateArchivedIsNull();
	}

	@Override
	public List<CollectingUnit> findAllArchivedCollectingUnits() {
		return collectingUnitRepository.findAllByDateArchivedIsNotNull();
	}

	@Override
	public CollectingUnitRegistrationRestModel convertToUpdateableModel(CollectingUnit cu) {
		CollectingUnitRegistrationRestModel restModel = new CollectingUnitRegistrationRestModel(
				cu.getName(), cu.getUsername(), cu.getPassword(),
				cu.getLocationUrl(), cu.getType().getName(), cu.getCollectDelay());
		
		restModel.setSensors(sensorService.convert(cu.getSensors()));
		
		return restModel;
	}

	@Override
	public CollectingUnit updateCollectingUnitWithId(Long id, CollectingUnitRegistrationRestModel restModel) {
		CollectingUnit collectingUnit = collectingUnitRepository.findOne(id);
		if(collectingUnit == null){
			throw new RuntimeException(String.format("User with given id: %s not found.", id));
		}
		
		updateCollectingUnitFromModel(collectingUnit, restModel);
		
		return collectingUnitRepository.save(collectingUnit);
	}
	
	private void updateCollectingUnitFromModel(CollectingUnit collectingUnit, CollectingUnitRegistrationRestModel restModel) {
		collectingUnit.setName(restModel.getName());
		collectingUnit.setUsername(restModel.getUsername());
		collectingUnit.setPassword(restModel.getPassword());
		collectingUnit.setLocationUrl(restModel.getLocationUrl());
		collectingUnit.setType(collectingUnitTypeService.findByName(restModel.getType()));
		collectingUnit.setCollectDelay(restModel.getCollectingDelay());

		Collection<SensorRestModel> selected = restModel.getSensors();
		Set<Sensor> sensorsSet = new HashSet<>();
		List<SensorRestModel> newSensors = new ArrayList<>();
		
		for (SensorRestModel sensorRestModel : selected) {
			Sensor sensor = sensorService.findByNameAndCollectingUnit(sensorRestModel.getName(), collectingUnit);
			if(sensor == null){
				newSensors.add(sensorRestModel);
			}
			else{
				sensorsSet.add(sensor);
			}
		}
		
		Set<Sensor> forDelete = new HashSet<Sensor>(collectingUnit.getSensors());
		forDelete.removeAll(sensorsSet);
		
		sensorService.deleteAll(forDelete);
		
		sensorsSet.addAll(sensorService.convertAll(newSensors));

		for (Sensor sensor : sensorsSet) {
			sensor.setCollectingUnit(collectingUnit);
		}

		collectingUnit.setSensors(sensorsSet);

	}
}
