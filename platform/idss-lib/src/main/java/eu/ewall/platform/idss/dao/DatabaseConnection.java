package eu.ewall.platform.idss.dao;

import java.util.ArrayList;
import java.util.List;

/**
 * This class models a connection to a database server. It provides access to
 * databases. You can obtain an instance of this class from {@link
 * DatabaseFactory DatabaseFactory}. When you no longer need the connection,
 * you should call {@link #close() close()}.
 *
 * <p>This class can configure returned databases so that they log database
 * actions for audit logging or for synchronisation with a remote database. You
 * can configure logging before calling {@link
 * #initDatabase(String, List, boolean) initDatabase()} or {@link
 * #getDatabase(String) getDatabase()}. For more information see {@link Database
 * Database}.</p>
 * 
 * @author Dennis Hofs (RRD)
 */
public abstract class DatabaseConnection {
	private boolean auditLogEnabled = false;
	private String auditLogUser = null;
	private boolean auditLogData = false;
	private boolean syncEnabled = false;
	private boolean saveSyncedRemoteActions = true;

	/**
	 * Returns whether audit logging is enabled.
	 *
	 * @return true if audit logging is enabled, false otherwise
	 */
	public boolean isAuditLogEnabled() {
		return auditLogEnabled;
	}

	/**
	 * Returns the user name for audit logging.
	 *
	 * @return the user name for audit logging
	 */
	public String getAuditLogUser() {
		return auditLogUser;
	}

	/**
	 * Returns whether data is stored with insert and update actions at audit
	 * logging. If synchronisation logging is enabled, the data is always
	 * stored regardless of this parameter.
	 *
	 * @return true if data is stored at audit logging, false otherwise
	 */
	public boolean isAuditLogData() {
		return auditLogData;
	}

	/**
	 * Enables audit logging. The parameter "logData" determines whether data is
	 * stored with insert and update actions at audit logging. If
	 * synchronisation logging is enabled, the data is always stored regardless
	 * of this parameter.
	 *
	 * @param user the user name that should be saved with the database actions
	 * (the user who performs the actions)
	 * @param logData true if data should be stored
	 */
	public void enableAuditLog(String user, boolean logData) {
		this.auditLogEnabled = true;
		this.auditLogUser = user;
		this.auditLogData = logData;
	}

	/**
	 * Disables audit logging.
	 */
	public void disableAuditLog() {
		this.auditLogEnabled = false;
		this.auditLogUser = null;
		this.auditLogData = false;
	}

	/**
	 * Returns whether action logging is enabled for synchronization with
	 * a remote database.
	 *
	 * @return true if synchronization logging is enabled, false otherwise
	 */
	public boolean isSyncEnabled() {
		return syncEnabled;
	}

	/**
	 * Returns whether action logging should be enabled for synchronization
	 * with a remote database.
	 *
	 * @param syncEnabled true if synchronization logging is enabled, false
	 * otherwise
	 */
	public void setSyncEnabled(boolean syncEnabled) {
		this.syncEnabled = syncEnabled;
	}

	/**
	 * If synchronization logging is enabled (see {@link #isSyncEnabled()
	 * isSyncEnabled()}), this method returns whether database actions received
	 * from the remote database should be logged as well. If this is false,
	 * then only local database actions are logged. The default is true.
	 * 
	 * @return true if remote database actions are logged, false if only local
	 * database actions are logged
	 */
	public boolean isSaveSyncedRemoteActions() {
		return saveSyncedRemoteActions;
	}

	/**
	 * If synchronization logging is enabled (see {@link
	 * #setSyncEnabled(boolean) setSyncEnabled()}), this method determines
	 * whether database actions received from the remote database should be
	 * logged as well. If this is false, then only local database actions are
	 * logged. The default is true.
	 * 
	 * @param saveSyncedRemoteActions true if remote database actions are
	 * logged, false if only local database actions are logged
	 */
	public void setSaveSyncedRemoteActions(boolean saveSyncedRemoteActions) {
		this.saveSyncedRemoteActions = saveSyncedRemoteActions;
	}
	
	/**
	 * Initialises a database and returns the database object. If the database
	 * does not exist on the server, it will be created. If it already exists,
	 * it will be upgraded if necessary. If the database contains tables that
	 * are not listed in "tableDefs", you can delete those tables by setting
	 * "dropOldTables" to true.
	 *
	 * <p>The returned database will be configured for action logging as you
	 * have configured this connection. This method will call {@link
	 * Database#setDatabaseInitialised(boolean) setDatabaseInitialised()} on
	 * the database. This means that you should not change the database
	 * structure after this.</p>
	 * 
	 * @param name the database name
	 * @param tableDefs the table definitions
	 * @param dropOldTables true if old tables should be dropped, false if they
	 * should be kept
	 * @return the database
	 * @throws DatabaseException if a database error occurs
	 */
	public Database initDatabase(String name,
			List<? extends DatabaseTableDef<?>> tableDefs,
			boolean dropOldTables) throws DatabaseException {
		Database db;
		if (databaseExists(name)) {
			db = doGetDatabase(name);
		} else {
			DatabaseCache.getInstance().removeDatabase(name);
			db = createDatabase(name);
		}
		List<String> newTableNames = new ArrayList<String>();
		for (DatabaseTableDef<?> tableDef : tableDefs) {
			db.initTable(tableDef);
			newTableNames.add(tableDef.getName());
		}
		if (dropOldTables) {
			List<String> currTableNames = db.selectTables();
			for (String tableName : currTableNames) {
				if (!tableName.startsWith("_") &&
						!newTableNames.contains(tableName)) {
					db.dropTable(tableName);
				}
			}
		}
		if (auditLogEnabled)
			db.enableAuditLog(auditLogUser, auditLogData);
		db.setSyncEnabled(syncEnabled);
		db.setSaveSyncedRemoteActions(saveSyncedRemoteActions);
		db.setDatabaseInitialised(true);
		return db;
	}
	
	/**
	 * Returns whether the specified database exists. This is used in {@link
	 * #initDatabase(String, List, boolean) initDatabase()} to decide whether
	 * {@link #createDatabase(String) createDatabase()} should be called.
	 *
	 * <p>Some databases may not be able to check whether a database exists.
	 * For those databases, {@link #createDatabase(String) createDatabase()}
	 * and {@link #doGetDatabase(String) doGetDatabase()} should be the
	 * same.</p>
	 *
	 * @param name the database name
	 * @return true if the database exists, false otherwise
	 * @throws DatabaseException if a database error occurs
	 */
	protected abstract boolean databaseExists(String name)
			throws DatabaseException;
	
	/**
	 * Creates a new database with the specified name and returns the database
	 * object. This method is called from {@link
	 * #initDatabase(String, List, boolean) initDatabase()} if the database does
	 * not exist.
	 *
	 * <p>Some databases may not be able to check whether a database exists.
	 * For those databases, this method should be the same as {@link
	 * #doGetDatabase(String) doGetDatabase()}.</p>
	 *
	 * @param name the database name
	 * @return the database
	 * @throws DatabaseException if a database error occurs
	 */
	protected abstract Database createDatabase(String name)
			throws DatabaseException;

	/**
	 * Returns the database with the specified name. You should only call this
	 * method if you know that the database exists and it is up-to-date.
	 * Otherwise you should call {@link #initDatabase(String, List, boolean)
	 * initDatabase()}.
	 *
	 * <p>The returned database will be configured for action logging as you
	 * have configured this connection. This method will call {@link
	 * Database#setDatabaseInitialised(boolean) setDatabaseInitialised()} on
	 * the database. This means that you should not change the database
	 * structure after this.</p>
	 *
	 * @param name the database name
	 * @return the database
	 * @throws DatabaseException if a database error occurs
	 */
	public Database getDatabase(String name) throws DatabaseException {
		Database db = doGetDatabase(name);
		if (auditLogEnabled)
			db.enableAuditLog(auditLogUser, auditLogData);
		db.setSyncEnabled(syncEnabled);
		db.setSaveSyncedRemoteActions(saveSyncedRemoteActions);
		db.setDatabaseInitialised(true);
		return db;
	}
	
	/**
	 * Returns the database with the specified name. This method is called from
	 * {@link #initDatabase(String, List, boolean) initDatabase()} if the
	 * database exists. Some databases may not be able to check whether a
	 * database exists. For those databases, this method should be the same as
	 * {@link #createDatabase(String) createDatabase()}.</p>
	 *
	 * @param name the database name
	 * @return the database
	 * @throws DatabaseException if a database error occurs
	 */
	protected abstract Database doGetDatabase(String name)
			throws DatabaseException;
	
	/**
	 * Deletes the database with the specified name. It also clears any cached
	 * database details from memory.
	 * 
	 * @param name the database name
	 * @throws DatabaseException if a database error occurs
	 */
	public void dropDatabase(String name) throws DatabaseException {
		doDropDatabase(name);
		DatabaseCache.getInstance().removeDatabase(name);
	}

	/**
	 * Deletes the database with the specified name.
	 *
	 * @param name the database name
	 * @throws DatabaseException if a database error occurs
	 */
	protected abstract void doDropDatabase(String name)
			throws DatabaseException;
	
	/**
	 * Closes the connection with the database.
	 */
	public abstract void close();
}
