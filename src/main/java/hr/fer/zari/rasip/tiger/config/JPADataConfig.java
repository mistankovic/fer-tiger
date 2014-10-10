package hr.fer.zari.rasip.tiger.config;

import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories("hr.fer.zari.rasip.tiger.dao.jpa")
@EntityScan("hr.fer.zari.rasip.tiger.domain")
@EnableTransactionManagement
public class JPADataConfig {
	
}
