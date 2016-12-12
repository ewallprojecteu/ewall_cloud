package eu.ewall.platform.idss.dao;

import eu.ewall.platform.idss.dao.memdb.MemoryDatabaseFactory;

import eu.ewall.platform.idss.utils.AppComponent;
import eu.ewall.platform.idss.utils.AppComponents;

import java.io.IOException;

/**
 * This factory can create {@link DatabaseConnection DatabaseConnection}s. The
 * factory can be configured as an {@link AppComponent AppComponent}. Its
 * default implementation is {@link MemoryDatabaseFactory
 * MemoryDatabaseFactory}.
 *
 * <p>The factory can configure database connections so that the databases log
 * actions for audit logging or for synchronisation with a remote database. For
 * more information see {@link Database Database}.</p>
 *
 * @author Dennis Hofs (RRD)
 */
@AppComponent
public abstract class DatabaseFactory {
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
	 * Sets whether audit logging should be enabled. If you set this to true,
	 * you must also call {@link #setAuditLogUser(String) setAuditLogUser()}
	 * and you may call {@link #setAuditLogData(boolean)}  setAuditLogData()}.
	 *
	 * @param auditLogEnabled true if audit logging is enabled, false otherwise
	 */
	public void setAuditLogEnabled(boolean auditLogEnabled) {
		this.auditLogEnabled = auditLogEnabled;
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
	 * Sets the user name for audit logging. This must be set if you enable
	 * audit logging. If you don't enable audit logging, it will be ignored.
	 *
	 * @param auditLogUser the user name for audit logging
	 */
	public void setAuditLogUser(String auditLogUser) {
		this.auditLogUser = auditLogUser;
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
	 * Sets whether data is stored with insert and update actions at audit
	 * logging. If you don't enable audit logging, it will be ignored. If
	 * synchronisation logging is enabled, the data is always stored regardless
	 * of this parameter.
	 *
	 * @param auditLogData true if data is stored at audit logging, false
	 * otherwise
	 */
	public void setAuditLogData(boolean auditLogData) {
		this.auditLogData = auditLogData;
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
	 * Returns whether action logging is enabled for synchronisation with
	 * a remote database.
	 *
	 * @return true if synchronisation logging is enabled, false otherwise
	 */
	public boolean isSyncEnabled() {
		return syncEnabled;
	}

	/**
	 * Returns whether action logging should be enabled for synchronisation
	 * with a remote database.
	 *
	 * @param syncEnabled true if synchronisation logging is enabled, false
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
	 * Returns a new instance of {@link MemoryDatabaseFactory
	 * MemoryDatabaseFactory}. This method is called as a default when you
	 * get an instance from {@link AppComponents AppComponents} and you haven't
	 * configured a specific subclass.
	 * 
	 * @return a new memory database factory
	 */
	public static DatabaseFactory getInstance() {
		return new MemoryDatabaseFactory();
	}

	/**
	 * Connects to the database server and returns a {@link DatabaseConnection
	 * DatabaseConnection}. When you no longer need the connection, you should
	 * call {@link DatabaseConnection#close() close()}.
	 *
	 * <p>The returned connection will be configured for action logging as you
	 * have configured this factory.</p>
	 *
	 * @return the database connection
	 * @throws IOException if the connection could not be established
	 */
	public DatabaseConnection connect() throws IOException {
		DatabaseConnection conn = doConnect();
		if (auditLogEnabled)
			conn.enableAuditLog(auditLogUser, auditLogData);
		conn.setSyncEnabled(syncEnabled);
		conn.setSaveSyncedRemoteActions(saveSyncedRemoteActions);
		return conn;
	}
	
	/**
	 * Connects to the database server and returns a {@link DatabaseConnection
	 * DatabaseConnection}. When you no longer need the connection, you should
	 * call {@link DatabaseConnection#close() close()}.
	 * 
	 * @return the database connection
	 * @throws IOException if the connection could not be established
	 */
	protected abstract DatabaseConnection doConnect() throws IOException;
}
