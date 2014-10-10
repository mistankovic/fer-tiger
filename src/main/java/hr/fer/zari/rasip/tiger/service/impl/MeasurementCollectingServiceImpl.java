package hr.fer.zari.rasip.tiger.service.impl;

import hr.fer.zari.rasip.tiger.core.collector.MeasurementCollector;
import hr.fer.zari.rasip.tiger.core.collector.MeasurementCollectorFactory;
import hr.fer.zari.rasip.tiger.domain.CollectingUnit;
import hr.fer.zari.rasip.tiger.domain.Measurement;
import hr.fer.zari.rasip.tiger.service.CollectingUnitService;
import hr.fer.zari.rasip.tiger.service.MeasurementCollectingService;
import hr.fer.zari.rasip.tiger.service.MeasurementService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MeasurementCollectingServiceImpl implements
		MeasurementCollectingService {

	private MeasurementService measurementService;
	private CollectingUnitService collectingUnitService;

	@Autowired
	public MeasurementCollectingServiceImpl(MeasurementService measurementService, CollectingUnitService collectingUnitService) {
		this.measurementService = measurementService;
		this.collectingUnitService = collectingUnitService;
	}

	@Override
	public int collect(CollectingUnit collectingUnit, Date collectDate) {
		if (collectingUnit != null) {
			List<CollectingUnit> list = new ArrayList<>(1);
			list.add(collectingUnit);
			return collectInternal(list, collectDate);
		}
		return 0;
	}

	@Override
	public int collect(List<CollectingUnit> collectingUnits, Date collectDate) {
		if (collectingUnits != null) {
			return collectInternal(collectingUnits, collectDate);
		}
		return 0;
	}

	private int collectInternal(List<CollectingUnit> collectingUnits, Date collectDate) {
		int savedNumber = 0;
		for (CollectingUnit collectingUnit : collectingUnits) {
			MeasurementCollector collector = MeasurementCollectorFactory.collector(collectingUnit);
			List<Measurement> list = collector.collect();
			if (list != null && !list.isEmpty()) {
				List<Measurement> saved = measurementService.save(list);
				savedNumber += saved.size();

				collectingUnit.setLastCollected(collectDate);
				collectingUnitService.save(collectingUnit);
			}
		}
		
		return savedNumber;
	}

}
