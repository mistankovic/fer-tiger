package hr.fer.zari.rasip.tiger.service;

import hr.fer.zari.rasip.tiger.domain.CollectingUnitType;

import java.util.List;

public interface CollectingUnitTypeService {

	Iterable<CollectingUnitType> findAll();
	
	CollectingUnitType findByName(String name);
	
	CollectingUnitType save(CollectingUnitType collectingUnitType);
	
	Iterable<CollectingUnitType> saveAll(List<CollectingUnitType> collectingUnitType);

	long count();

}
