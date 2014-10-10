package hr.fer.zari.rasip.tiger.dao.mongo;

import hr.fer.zari.rasip.tiger.domain.Measurement;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeasurementRepository extends MongoRepository<Measurement, String>{

	Set<Measurement> findAllBySensorFieldId(Long id);

	List<Measurement> findAllOrderByDateMeasured(Pageable pageable);

	List<Measurement> findAllBySensorFieldIdAndDateMeasuredBetweenOrderByDateMeasuredAsc(Long sensorFieldId, Date from, Date to);
}
