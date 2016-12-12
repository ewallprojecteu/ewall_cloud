package eu.ewall.common.initialization;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;

/**
 * The Class MongoConfig.
 */
@Configuration
public class MongoConfig extends AbstractMongoConfiguration {

	/** The env. */
	@Autowired 
	private Environment env;
	
	/* (non-Javadoc)
	 * @see org.springframework.data.mongodb.config.AbstractMongoConfiguration#getDatabaseName()
	 */
	@Override
	protected String getDatabaseName() {
		String database = env.getProperty("mongo.dbname");
    	if (database == null) {
    		throw new BeanCreationException("mongo.dbname property not set");
    	}
    	return database;
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.data.mongodb.config.AbstractMongoConfiguration#mongo()
	 */
	@Override
	public Mongo mongo() throws Exception {
		String host = env.getProperty("mongo.host");
    	if (host == null) {
    		throw new BeanCreationException("mongo.host property not set");
    	}
    	int port = Integer.parseInt(env.getProperty("mongo.port", "27017"));
    	return new MongoClient(host, port);
	}
	
}
