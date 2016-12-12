package eu.ewall.platform.idss.dao.mongodb;

import java.io.IOException;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;

import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseConnection;
import eu.ewall.platform.idss.dao.DatabaseException;

/**
 * Connection to a MongoDB server.
 * 
 * @author Dennis Hofs (RRD)
 */
public class MongoDatabaseConnection extends DatabaseConnection {
	private Mongo mongo;
	
	/**
	 * Connects to the specified MongoDB server.
	 * 
	 * @param host the host name
	 * @param port the port number
	 * @throws IOException if a connection error occurs
	 */
	public MongoDatabaseConnection(String host, int port) throws IOException {
		mongo = new MongoClient(host, port);
	}

	@Override
	protected boolean databaseExists(String name) {
		return mongo.getDatabaseNames().contains(name);
	}

	@Override
	protected Database createDatabase(String name) throws DatabaseException {
		return new MongoDatabase(name, mongo.getDB(name));
	}

	@Override
	protected Database doGetDatabase(String name) throws DatabaseException {
		return new MongoDatabase(name, mongo.getDB(name));
	}

	@Override
	protected void doDropDatabase(String name) throws DatabaseException {
		try {
			mongo.dropDatabase(name);
		} catch (MongoException ex) {
			throw new DatabaseException("Can't drop database \"" + name +
					"\": " + ex.getMessage(), ex);
		}
	}

	@Override
	public void close() {
		mongo.close();
	}
}
