/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.middleware.datamanager.dao.config.factory;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;

import eu.ewall.platform.middleware.datamanager.dao.config.dbtype.EWallDBType;

/**
 * A simple factory for returning a MongoDB.
 */
public class MongoDBFactory {

	/** The log. */
	private static final Logger LOG = LoggerFactory
			.getLogger(MongoDBFactory.class);

	/** The mongo. */
	private static Mongo mongo;

	/** The props. */
	private static Properties props;

	/** The db system name. */
	private static String dbSystemName;

	/** The db data name. */
	private static String dbDataName;

	/**
	 * Instantiates a new mongo db factory.
	 */
	private MongoDBFactory() {
	}

	static {
		initProps();
		dbSystemName = props.getProperty("mongo.dbname.system");
		dbDataName = props.getProperty("mongo.dbname.data");
	}

	/**
	 * Inits the props.
	 */
	private static void initProps() {
		String env = System.getProperty("ewall.env");
		String projectName = "data-manager";
		LOG.debug("Loading config for environment: " + env);
		Resource resource = null;

		if (env == null || "".equals(env)) {
			LOG.debug("Loading config: /" + projectName + "-local.properties");
			resource = new ClassPathResource("/" + projectName
					+ "-local.properties");
			LOG.debug("Loaded config: /" + projectName + "-local.properties");
		} else {
			LOG.debug("Loading config: /" + projectName + "-" + env
					+ ".properties " + env);
			resource = new ClassPathResource("/" + projectName + "-" + env
					+ ".properties");
			LOG.debug("Loaded config: /" + projectName + "-" + env
					+ ".properties " + env);
		}

		try {
			props = PropertiesLoaderUtils.loadProperties(resource);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
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
				mongo = new MongoClient(props.getProperty("mongo.host"),
						Integer.parseInt(props.getProperty("mongo.port")));

			} catch (UnknownHostException e) {
				LOG.error(e.toString());
			} catch (MongoException e) {
				LOG.error(e.toString());
			}
		}

		return mongo;
	}

	/**
	 * Gets the DB name from type.
	 *
	 * @param dbType
	 *            the db type
	 * @return the DB name from type
	 */
	private static String getDBNameFromType(EWallDBType dbType) {
		switch (dbType) {
		case SYSTEM:
			return dbSystemName;
		case DATA:
			return dbDataName;
		default:
			return null;
		}
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
	 * Gets the DB for db type.
	 *
	 * @param dbType
	 *            the db type
	 * @return the DB for db type
	 */
	public static DB getDBForDBType(EWallDBType dbType) {
		String dbName = getDBNameFromType(dbType);
		LOG.debug("Retrieving db: " + dbName);
		return getMongo().getDB(dbName);
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
	 * Gets the mongo operations for db type.
	 *
	 * @param dbType
	 *            the db type
	 * @return the mongo operations for db type
	 */
	public static MongoOperations getMongoOperationsForDBType(EWallDBType dbType) {
		String dbName = getDBNameFromType(dbType);
		LOG.debug("Retrieving MongoOperations for db: " + dbName);
		MongoOperations mongoOps = new MongoTemplate(getMongo(), dbName);
		return mongoOps;
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
		props = null;
	}
}
