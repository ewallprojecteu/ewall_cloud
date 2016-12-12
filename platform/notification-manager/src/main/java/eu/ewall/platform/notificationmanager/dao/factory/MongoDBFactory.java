/****************************************************************
 * Copyright 2014 Ss Cyril and Methodius University in Skopje
***************************************************************/

package eu.ewall.platform.notificationmanager.dao.factory;

import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
/*import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;*/



import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;

//import eu.ewall.platform.notificationmanager.Application;

/**
 * A simple factory for returning a MongoDB. *
 * 
 */
@Component
public class MongoDBFactory {

	@Value("${mongo.host}")
	private String mongoHost;
	
	@Value("${mongo.port}")
	private int mongoPort;
	
	@Value("${mongo.dbname}")
	private String mongoDBName;
	
	/** The log. */
	private static Logger LOG = LoggerFactory.getLogger(MongoDBFactory.class);

	/** The mongo. */
	private Mongo mongo;

	/** The Constant DB_NAME. */
	public String DB_NAME;

	//private MongoDBFactory() {
	//}
	
	


	/**
	 * Gets the mongo instance.
	 *
	 * @return the mongo
	 */
	public Mongo getMongo() {
		//LOG.debug("Retrieving MongoDB");
		//System.out.println("Retrieving MongoDB");
		if (mongo == null) {
			try {
				mongo = new MongoClient(mongoHost, mongoPort);
				//
				//System.out.println("MongoDB name "+ mongoDBName);
			    DB_NAME = mongoDBName;

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
	public DB getDB(String dbname) {
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
	public DBCollection getCollection(String dbname, String collection) {
		LOG.debug("Retrieving collection: " + collection);
		return getDB(dbname).getCollection(collection);
	}

	/**
	 * Gets the mongo operations.
	 *
	 * @return the mongo operations
	 */
	public  MongoOperations getMongoOperations() {
		LOG.debug("Retrieving MongoOperations for db: " + DB_NAME);
		return new MongoTemplate(getMongo(), DB_NAME);

	}

	/**
	 * Used for gracefull shutdow of mongo connection
	 * 
	 * @throws Exception
	 */
	public void close() throws Exception {
		if (mongo != null) {
			mongo.close();
		}
	}

}
