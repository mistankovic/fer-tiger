package hr.fer.zari.rasip.tiger.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hr.fer.zari.rasip.tiger.dao.jpa.CollectingUnitTypeRepository;
import hr.fer.zari.rasip.tiger.domain.CollectingUnitType;
import hr.fer.zari.rasip.tiger.service.CollectingUnitTypeService;

@Service
@Transactional
public class CollectingUnitTypeServiceImpl implements CollectingUnitTypeService {

	private CollectingUnitTypeRepository collectingUnitTypeRepository;

	@Autowired
	public CollectingUnitTypeServiceImpl(CollectingUnitTypeRepository collectingUnitTypeRepository) {
		this.collectingUnitTypeRepository = collectingUnitTypeRepository;
	}

	@Override
	public Iterable<CollectingUnitType> findAll() {
		return collectingUnitTypeRepository.findAll();
	}

	@Override
	public CollectingUnitType findByName(String name) {
		CollectingUnitType cut = null;
		if(name != null){
			cut = collectingUnitTypeRepository.findByName(name);
		}
		return cut;
	}

	@Override
	public CollectingUnitType save(CollectingUnitType collectingUnitType) {
		CollectingUnitType cut = null;
		if(collectingUnitType != null){
			cut = collectingUnitTypeRepository.save(collectingUnitType);
		}
		return cut;
	}

	@Override
	public Iterable<CollectingUnitType> saveAll(List<CollectingUnitType> collectingUnitType) {
		Iterable<CollectingUnitType> cut = null;
		if(collectingUnitType != null){
			cut = collectingUnitTypeRepository.save(collectingUnitType);
		}
		return cut;
	}

	@Override
	public long count() {
		return collectingUnitTypeRepository.count();
	}

}
