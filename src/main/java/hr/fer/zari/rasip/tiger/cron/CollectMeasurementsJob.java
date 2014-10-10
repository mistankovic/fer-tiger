package hr.fer.zari.rasip.tiger.cron;

import hr.fer.zari.rasip.tiger.domain.CollectingUnit;
import hr.fer.zari.rasip.tiger.service.CollectingUnitService;
import hr.fer.zari.rasip.tiger.service.MeasurementCollectingService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Transactional
public class CollectMeasurementsJob {

	private static final long ONE_SECOND = 1000L;
	private static final long ONE_MINUTE = 60 * ONE_SECOND;
	private static final long ONE_HOUR = 60 * ONE_MINUTE;

	private static final Logger logger = LoggerFactory.getLogger(CollectMeasurementsJob.class);

	@Autowired
	MeasurementCollectingService measurementCollectingService;
	@Autowired
	CollectingUnitService collectingUnitService;

	@Scheduled(cron = "0 0 * * * ?")
	public void collectMeasurements() {
		Date collectDate = new Date();
		Date referentDate = new Date(collectDate.getTime() + ONE_SECOND);

		logger.info("Started collecting measurements job with collect date {} and referent date {}.",
					collectDate, referentDate);

		List<CollectingUnit> all = collectingUnitService.findAllUnarchivedCollectingUnits();

		List<CollectingUnit> forCollect = new ArrayList<>();
		for (CollectingUnit cu : all) {
			if (isTimeToCollect(cu, referentDate)) {
				logger.info("Collecting measurements for collecting unit {}.",cu);
				forCollect.add(cu);
			}
		}

		int collected = measurementCollectingService.collect(forCollect, collectDate);

		logger.info(
				"Finished collecting measurements job with collect date {}. Totally collected {} measurements",
				collectDate, collected);
	}

	private boolean isTimeToCollect(CollectingUnit cu, Date referentDate) {
		Date lastCollected = cu.getLastCollected();
		if (lastCollected == null) {
			return true;
		}

		long collectDelay = cu.getCollectDelay() * ONE_HOUR;
		Date nextCollect = new Date(lastCollected.getTime() + collectDelay);
		if (nextCollect.before(referentDate)) {
			return true;
		}

		return false;
	}
}
