package hr.fer.zari.rasip.tiger.controller;

import hr.fer.zari.rasip.tiger.domain.CollectingUnitType;
import hr.fer.zari.rasip.tiger.service.CollectingUnitTypeService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/collecting-unit-type")
public class CollectingUnitTypeController {

	private CollectingUnitTypeService collectingUnitTypeService;

	@Autowired
	public CollectingUnitTypeController(CollectingUnitTypeService collectingUnitTypeService) {
		this.collectingUnitTypeService = collectingUnitTypeService;
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<String> list() {
		Iterable<CollectingUnitType> all = collectingUnitTypeService.findAll();
		List<String> names = new ArrayList<>();
		for (CollectingUnitType collectingUnitType : all) {
			names.add(collectingUnitType.getName());
		}

		return names;
	}
}
