package eu.ewall.platform.userinteractionlogger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseConnection;
import eu.ewall.platform.idss.dao.DatabaseCriteria;
import eu.ewall.platform.idss.dao.DatabaseException;
import eu.ewall.platform.idss.dao.DatabaseTableDef;
import eu.ewall.platform.idss.dao.mongodb.MongoDatabaseFactory;
import eu.ewall.platform.idss.utils.AppComponent;
import eu.ewall.platform.userinteractionlogger.model.DailyInteractionLogTable;
import eu.ewall.platform.userinteractionlogger.model.UserInteractionLogTable;

@AppComponent
public class DatabaseAccessPoint {
	private final Object lock = new Object();
	private final Object userKeyLock = new Object();
	private final Object initLock = new Object();
	private UILConfiguration config;

	private Random random = new Random();
	private List<DatabaseConnection> openDbConns =
			new ArrayList<DatabaseConnection>();
	private boolean closed = false;
	
	public DatabaseAccessPoint(UILConfiguration config) {
		this.config = config;
	}
	
	public void close() {
		synchronized (lock) {
			if (closed)
				return;
			closed = true;
			while (!openDbConns.isEmpty()) {
				DatabaseConnection dbConn = openDbConns.remove(0);
				dbConn.close();
			}
		}
	}
	
	public DatabaseConnection openDatabase() throws IOException {
		MongoDatabaseFactory factory = new MongoDatabaseFactory(config.getMongoDBHost(),
				config.getMongoDBPort());
		DatabaseConnection dbConn = factory.connect();
		synchronized (lock) {
			if (closed) {
				dbConn.close();
				throw new IOException("Service closed");
			}
			openDbConns.add(dbConn);
		}
		return dbConn;
	}
	
	public void closeDatabase(DatabaseConnection dbConn) {
		synchronized (lock) {
			if (closed)
				return;
			openDbConns.remove(dbConn);
		}
		dbConn.close();
	}

	public Database getDatabase(DatabaseConnection dbConn)
			throws DatabaseException {
		synchronized (initLock) {
			List<DatabaseTableDef<?>> tableDefs =
					new ArrayList<DatabaseTableDef<?>>();
			tableDefs.add(new UserKeyTable());
			tableDefs.add(new DailyInteractionLogTable());
			Database db = dbConn.initDatabase(config.getMongoDBName(),
					tableDefs, false);
			if (db.selectTables().contains("userinteractionlog"))
				db.dropTable("userinteractionlog");
			return db;
		}
	}
	
	public UserInteractionLogTable initLogTable(Database db, String user)
			throws DatabaseException {
		String key = getUserKey(db, user);
		UserInteractionLogTable table = new UserInteractionLogTable(key);
		db.initTable(table);
		return table;
	}
	
	private String getUserKey(Database db, String user)
			throws DatabaseException {
		synchronized (userKeyLock) {
			DatabaseCriteria criteria = new DatabaseCriteria.Equal("user", user);
			UserKeyTable table = new UserKeyTable();
			UserKey userKey = db.selectOne(table, criteria, null);
			if (userKey != null)
				return userKey.getKey();
			boolean foundAvailableKey = false;
			String key = null;
			int n = random.nextInt();
			while (!foundAvailableKey) {
				key = String.format("%08x", n);
				criteria = new DatabaseCriteria.Equal("key", key);
				UserKey other = db.selectOne(table, criteria, null);
				if (other == null)
					foundAvailableKey = true;
				else
					n++;
			}
			userKey = new UserKey();
			userKey.setUser(user);
			userKey.setKey(key);
			db.insert(table.getName(), userKey);
			return key;
		}
	}
}
