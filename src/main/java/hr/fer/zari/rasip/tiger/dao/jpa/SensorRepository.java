package hr.fer.zari.rasip.tiger.dao.jpa;

import hr.fer.zari.rasip.tiger.domain.CollectingUnit;
import hr.fer.zari.rasip.tiger.domain.Sensor;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorRepository extends CrudRepository<Sensor, Long>{
	
	Sensor findByCollectingUnitAndName(CollectingUnit collectingUnit, String name);
}
