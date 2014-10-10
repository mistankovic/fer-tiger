package hr.fer.zari.rasip.tiger.core.collector;

import hr.fer.zari.rasip.tiger.domain.Measurement;

import java.util.List;

public interface MeasurementCollector {

	List<Measurement> collect();
}
