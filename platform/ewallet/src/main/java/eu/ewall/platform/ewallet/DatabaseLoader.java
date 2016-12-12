package eu.ewall.platform.ewallet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseConnection;
import eu.ewall.platform.idss.dao.DatabaseException;
import eu.ewall.platform.idss.dao.DatabaseFactory;
import eu.ewall.platform.idss.dao.DatabaseTableDef;
import eu.ewall.platform.idss.dao.mongodb.MongoDatabaseFactory;
import eu.ewall.platform.idss.utils.AppComponents;

/**
 * This class can connect to the MongoDB server and load the database for the
 * ewallet service.
 * 
 * @author Dennis Hofs (RRD)
 */
public class DatabaseLoader {
	private String mongoHost;
	private int mongoPort;
	private String mongoDbName;

	/**
	 * Constructs a new database loader.
	 * 
	 * @param mongoHost the host name of MongoDB
	 * @param mongoPort the port number of MongoDB
	 * @param mongoDbName the database name for ewallet
	 */
	public DatabaseLoader(String mongoHost, int mongoPort,
			String mongoDbName) {
		this.mongoHost = mongoHost;
		this.mongoPort = mongoPort;
		this.mongoDbName = mongoDbName;
	}
	
	/**
	 * Connects to the MongoDB server. When you no longer need the connection,
	 * you should call {@link DatabaseConnection#close() close()}.
	 * 
	 * @return the database connection
	 * @throws IOException if the connection can't be established
	 */
	public DatabaseConnection connect() throws IOException {
		AppComponents components = AppComponents.getInstance();
		DatabaseFactory dbFactory;
		if (components.hasComponent(DatabaseFactory.class)) {
			dbFactory = components.getComponent(DatabaseFactory.class);
		} else {
			dbFactory = new MongoDatabaseFactory(mongoHost, mongoPort);
			components.addComponent(dbFactory);
		}
		return dbFactory.connect();
	}

	/**
	 * Loads the database for ewallet. It creates the database collections
	 * and runs upgrades where needed.
	 * 
	 * @param dbConn the database connection
	 * @return the database
	 * @throws DatabaseException if the database can't be loaded
	 */
	public Database initDatabase(DatabaseConnection dbConn) throws DatabaseException {
		List<DatabaseTableDef<?>> tableDefs = new ArrayList<DatabaseTableDef<?>>();
		tableDefs.add(new EWalletDataTable());
		tableDefs.add(new ActivityDataTable());
		tableDefs.add(new TransactionDataTable());
		tableDefs.add(new SleepTimeDataTable());
		return dbConn.initDatabase(mongoDbName, tableDefs, false);
	}
}
