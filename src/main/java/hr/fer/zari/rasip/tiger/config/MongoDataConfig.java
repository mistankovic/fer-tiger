package hr.fer.zari.rasip.tiger.config;

import java.net.UnknownHostException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;

@Configuration
@EnableMongoRepositories("hr.fer.zari.rasip.tiger.dao.mongo")
public class MongoDataConfig {

	@Bean
	public MongoTemplate mongoTemplate(Mongo mongo) throws UnknownHostException {
		return new MongoTemplate(mongo, "tiger");
	}

	@Bean
	public Mongo mongo() throws UnknownHostException {
		return new MongoClient("localhost");
	}
}
