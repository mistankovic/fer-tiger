package hr.fer.zari.rasip.tiger.controller;

import hr.fer.zari.rasip.tiger.domain.CollectingUnit;
import hr.fer.zari.rasip.tiger.domain.Sensor;
import hr.fer.zari.rasip.tiger.rest.model.CollectingUnitRegistrationRestModel;
import hr.fer.zari.rasip.tiger.rest.model.CollectingUnitRestModel;
import hr.fer.zari.rasip.tiger.rest.model.SensorRestModel;
import hr.fer.zari.rasip.tiger.service.CollectingUnitService;
import hr.fer.zari.rasip.tiger.service.MeasurementCollectingService;
import hr.fer.zari.rasip.tiger.service.SensorService;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/collecting-unit")
public class CollectingUnitController {

	private static final String COLLECT_MESSAGE = "Totally collected %s measurements.";
	private CollectingUnitService collectingUnitService;
	private MeasurementCollectingService measurementCollectingService;
	private SensorService sensorService;

	@Autowired
	public CollectingUnitController(
			CollectingUnitService collectingUnitService,
			MeasurementCollectingService measurementCollectingService,
			SensorService sensorService) {
		super();
		this.collectingUnitService = collectingUnitService;
		this.measurementCollectingService = measurementCollectingService;
		this.sensorService = sensorService;
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<CollectingUnitRestModel> list(@RequestParam(defaultValue = "false") boolean archived) {
		List<CollectingUnit> collectingUnits;
		if (archived == true) {
			collectingUnits = collectingUnitService.findAllArchivedCollectingUnits();
		} else {
			collectingUnits = collectingUnitService.findAllUnarchivedCollectingUnits();
		}
		return collectingUnitService.convert(collectingUnits);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public CollectingUnitRegistrationRestModel show(@PathVariable Long id) {
		CollectingUnit collectingUnit = collectingUnitService.findById(id);

		CollectingUnitRegistrationRestModel restModel = null;
		if (collectingUnit != null) {
			restModel = collectingUnitService.convertToUpdateableModel(collectingUnit);
		}
		return restModel;
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.POST)
	public CollectingUnitRestModel save(@RequestBody CollectingUnitRegistrationRestModel registrationModel) {
		CollectingUnit collectingUnit = collectingUnitService
				.convert(registrationModel);
		CollectingUnit savedCollectingUnit = collectingUnitService
				.save(collectingUnit);
		return collectingUnitService.convert(savedCollectingUnit);
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/{id}/collect")
	public String collect(@PathVariable Long id) {
		CollectingUnit cu = collectingUnitService.findById(id);
		int collected = measurementCollectingService.collect(cu, new Date());
		return String.format(COLLECT_MESSAGE, collected);
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST }, value = "/collect")
	public String collectAll() {
		List<CollectingUnit> collectingUnits = collectingUnitService
				.findAllUnarchivedCollectingUnits();
		int collected = measurementCollectingService.collect(collectingUnits,
				new Date());
		return String.format(COLLECT_MESSAGE, collected);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/fetch-sensors")
	public List<SensorRestModel> fetchSensors(@RequestBody CollectingUnitRegistrationRestModel cuModel) {
		CollectingUnit cu = collectingUnitService.convert(cuModel);
		List<Sensor> fetched = sensorService.fetchSensors(cu);
		return sensorService.convert(fetched);
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public CollectingUnitRestModel update(@PathVariable Long id, @RequestBody CollectingUnitRegistrationRestModel cuModel) {
		CollectingUnit collectingUnit = collectingUnitService.updateCollectingUnitWithId(id, cuModel);
		return collectingUnitService.convert(collectingUnit);
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public String delete(@PathVariable Long id) {
		collectingUnitService.delete(id);

		return String.format("Permanently deleted collecting unit with id %s ", id);
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.POST, value ={"/{id}/archive", "/{id}/unarchive"})
	public CollectingUnitRestModel archiveAndUnarchive(@PathVariable Long id, HttpServletRequest request) {
		CollectingUnit collectingUnit = collectingUnitService.findById(id);
		
		Date archiveDate;
		if(request.getRequestURL().toString().endsWith("/unarchive")){
			archiveDate = null;
		}
		else{
			archiveDate = new Date();
		}
		collectingUnit.setDateArchived(archiveDate);

		CollectingUnit saved = collectingUnitService.save(collectingUnit);
		return collectingUnitService.convert(saved);
	}

	@RequestMapping(value = "/{id}/sensor", method = RequestMethod.GET)
	public List<SensorRestModel> listCollectingUnitSensors(@PathVariable Long id) {
		CollectingUnit collectingUnit = collectingUnitService.findById(id);
		Set<Sensor> sensors = collectingUnit.getSensors();
		return sensorService.convert(sensors);
	}
}
