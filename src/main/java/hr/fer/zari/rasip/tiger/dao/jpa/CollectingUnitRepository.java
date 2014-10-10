package hr.fer.zari.rasip.tiger.dao.jpa;

import java.util.List;

import hr.fer.zari.rasip.tiger.domain.CollectingUnit;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectingUnitRepository extends CrudRepository<CollectingUnit, Long>{

	List<CollectingUnit> findAllByDateArchivedIsNull();

	List<CollectingUnit> findAllByDateArchivedIsNotNull();

}
