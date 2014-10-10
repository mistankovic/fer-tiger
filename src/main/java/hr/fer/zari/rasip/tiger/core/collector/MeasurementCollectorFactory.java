package hr.fer.zari.rasip.tiger.core.collector;

import hr.fer.zari.rasip.tiger.domain.CollectingUnit;
import hr.fer.zari.rasip.tiger.handler.CollectingUnitHandlerFactory;

public class MeasurementCollectorFactory {

	private static CollectingPool collectingPool;

	static {
		collectingPool = CollectingPoolImpl.instance();
	}

	public static MeasurementCollector collector(CollectingUnit collectingUnit) {
		String type = collectingUnit.getType().getName();
		switch (type) {
		case "GSN":
			return new GSNMeasurementCollector(collectingPool, collectingUnit,
					CollectingUnitHandlerFactory.handler(collectingUnit));
		default:
			return null;
		}
	}

}
