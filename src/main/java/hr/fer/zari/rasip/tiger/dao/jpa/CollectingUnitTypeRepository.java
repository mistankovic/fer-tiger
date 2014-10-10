package hr.fer.zari.rasip.tiger.dao.jpa;

import hr.fer.zari.rasip.tiger.domain.CollectingUnitType;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectingUnitTypeRepository extends CrudRepository<CollectingUnitType, Long>{

	CollectingUnitType findByName(String name);
}
