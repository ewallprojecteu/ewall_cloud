/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.fusioner.twitter.dao.mongo.factory;

import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;

import eu.ewall.fusioner.twitter.config.EnvironmentUtils;

/**
 * A simple factory for returning a MongoDB. *
 */
public class MongoDBFactory {

	/** The log. */
	private static Logger LOG = LoggerFactory.getLogger(MongoDBFactory.class);

	/** The mongo. */
	private static Mongo mongo;

	/** The Constant DB_NAME. */
	public static String DB_NAME;
	
	/**
	 * Instantiates a new mongo db factory.
	 */
	private MongoDBFactory() {
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
				mongo = new MongoClient(EnvironmentUtils.getProperty("mongo.host"),
						Integer.parseInt(EnvironmentUtils.getProperty("mongo.port")));
				DB_NAME = EnvironmentUtils.getProperty("mongo.dbname");

			} catch (UnknownHostException e) {
				LOG.error(e.toString());
			} catch (MongoException e) {
				LOG.error(e.toString());
			}
		}

		return mongo;
	}

	/**
	 * Gets the db.
	 *
	 * @param dbname
	 *            the dbname
	 * @return the db
	 */
	public static DB getDB(String dbname) {
		LOG.debug("Retrieving db: " + dbname);
		return getMongo().getDB(dbname);
	}

	/**
	 * Gets the collection.
	 *
	 * @param dbname
	 *            the dbname
	 * @param collection
	 *            the collection
	 * @return the collection
	 */
	public static DBCollection getCollection(String dbname, String collection) {
		LOG.debug("Retrieving collection: " + collection);
		return getDB(dbname).getCollection(collection);
	}

	/**
	 * Gets the mongo operations.
	 *
	 * @return the mongo operations
	 */
	public static MongoOperations getMongoOperations() {
		LOG.debug("Retrieving MongoOperations for db: " + DB_NAME);
		return new MongoTemplate(getMongo(), DB_NAME);

	}

	/**
	 * Used for gracefull shutdow of mongo connection.
	 *
	 * @throws Exception
	 *             the exception
	 */
	public static void close() throws Exception {
		if (mongo != null) {
			mongo.close();
		}
	}
}
