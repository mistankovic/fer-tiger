package hr.fer.zari.rasip.tiger.service;

import hr.fer.zari.rasip.tiger.domain.CollectingUnit;
import hr.fer.zari.rasip.tiger.rest.model.CollectingUnitRegistrationRestModel;
import hr.fer.zari.rasip.tiger.rest.model.CollectingUnitRestModel;

import java.util.List;

public interface CollectingUnitService {

	List<CollectingUnit> findAllCollectingUnits();
	
	CollectingUnit save(CollectingUnit collectingUnit);

	CollectingUnit findById(Long id);
	
	CollectingUnit convert(CollectingUnitRegistrationRestModel model);
	
	CollectingUnitRestModel convert(CollectingUnit cu);
	
	CollectingUnitRegistrationRestModel convertToUpdateableModel(CollectingUnit cu);
	
	CollectingUnit updateCollectingUnitWithId(Long id, CollectingUnitRegistrationRestModel restModel);
	
	List<CollectingUnitRestModel> convert(List<CollectingUnit> collectingUnits);

	void delete(Long id);

	List<CollectingUnit> findAllUnarchivedCollectingUnits();

	List<CollectingUnit> findAllArchivedCollectingUnits();

}
