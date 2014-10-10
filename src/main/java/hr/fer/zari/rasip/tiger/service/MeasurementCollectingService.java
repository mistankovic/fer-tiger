package hr.fer.zari.rasip.tiger.service;

import java.util.Date;
import java.util.List;

import hr.fer.zari.rasip.tiger.domain.CollectingUnit;

public interface MeasurementCollectingService {

	int collect(CollectingUnit collectingUnit, Date collectDate);

	int collect(List<CollectingUnit> collectingUnits, Date collectDate);
}
