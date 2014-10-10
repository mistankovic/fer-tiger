package hr.fer.zari.rasip.tiger.dao.jpa;

import hr.fer.zari.rasip.tiger.domain.SensorField;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorFieldRepository extends CrudRepository<SensorField, Long> {

	SensorField findByName(String name);
}
