/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.middleware.cloudgateway;

import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;

/**
 * A simple factory for returning a MongoDB. *
 * 
 * @author eandgrg
 */
public class MongoDBFactoryForTesting {

	/** The log. */
	private static Logger LOG = LoggerFactory
			.getLogger(MongoDBFactoryForTesting.class);

	/** The mongo. */
	private static Mongo mongo;

	private static String MONGO_HOST = "localhost";
	private static int MONGO_PORT = 27017;
	private static String MONGO_DBNAME = "ewalldb";

	private MongoDBFactoryForTesting() {
	}

	/**
	 * Gets the mongo instance.
	 *
	 * @return the mongo
	 */
	public static Mongo getMongo() {

		LOG.debug("Retrieving MongoDB");
		if (mongo == null) {
			try {
				mongo = new MongoClient(MONGO_HOST,MONGO_PORT);
			} catch (UnknownHostException e) {
				LOG.error(e.toString());
			} catch (MongoException e) {
				LOG.error(e.toString());
			}
		}

		return mongo;
	}


//	/**
//	 * Gets the collection.
//	 *
//	 * @param dbname
//	 *            the dbname
//	 * @param collection
//	 *            the collection
//	 * @return the collection
//	 */
//	public static DBCollection getCollection(String dbname, String collection) {
//		LOG.debug("Retrieving collection: " + collection);
//		return getDB(dbname).getCollection(collection);
//	}

	/**
	 * Gets the mongo operations.
	 *
	 * @return the mongo operations
	 */
	public static MongoOperations getMongoOperations() {
		LOG.debug("Retrieving MongoOperations for db: " + MONGO_DBNAME);
		return new MongoTemplate(getMongo(), MONGO_DBNAME);

	}

	/**
	 * Used for gracefull shutdown of mongo connection
	 * 
	 * @throws Exception
	 */
	public static void close() throws Exception {
		if (mongo != null) {
			mongo.close();
		}
	}

}
