package hr.fer.zari.rasip.tiger.config;

import hr.fer.zari.rasip.tiger.cron.CollectMeasurementsJob;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class SchedulingConfig {

	@Bean
	public CollectMeasurementsJob collectMeasurementsJob(){
		return new CollectMeasurementsJob();
	}

}
