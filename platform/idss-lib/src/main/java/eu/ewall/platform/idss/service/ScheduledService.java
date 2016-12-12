package eu.ewall.platform.idss.service;

import eu.ewall.platform.idss.dao.DatabaseConnection;
import eu.ewall.platform.idss.dao.DatabaseFactory;

import eu.ewall.platform.idss.utils.AppComponents;

import eu.ewall.platform.idss.utils.datetime.VirtualClock;

import eu.ewall.platform.idss.utils.exception.FatalException;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A scheduled service is a service or module that runs a task repeatedly at a
 * certain frequency. The frequency is defined as a task interval by {@link
 * #getTaskInterval() getTaskInterval()}. Its default implementation returns
 * the task interval from {@link ScheduledServiceProperties
 * ScheduledServiceProperties}. An implementation may override that method if
 * it wants to define its own frequency, for example defined in its own
 * configuration.
 * 
 * <p>Implementations should at least override {@link #runTask() runTask()}.
 * You may also override {@link #close() close()}.</p>
 * 
 * <p>This class contains some additional functionality that may be used by
 * implementations. First the constructor sets up a SLF4J logger and assigns it
 * to {@link #logger logger}. It will first try to get an {@link ILoggerFactory
 * ILoggerFactory} from the {@link AppComponents AppComponents}. If this fails,
 * it will just use the SLF4J {@link LoggerFactory LoggerFactory}. For the name
 * of the logger, it will try the static string field LOGTAG from the current
 * (implementation) class. If that fails, it will use the simple class
 * name.</p>
 * 
 * <p>Another function is setting up a database connection. The constructor
 * will try to get a {@link DatabaseFactory DatabaseFactory} from the {@link
 * AppComponents AppComponents}. Then you can open and close a connection with
 * {@link #openDatabaseConnection() openDatabaseConnection()} and {@link
 * #closeDatabaseConnection(DatabaseConnection) closeDatabaseConnection()}.
 * You should keep a connection only for the duration of a task, not for the
 * lifetime of the service. Any open connections will be closed when the
 * service is closed.</p>
 * 
 * @author Dennis Hofs (RRD)
 */
public abstract class ScheduledService {
	protected Logger logger;

	private DatabaseFactory dbFactory;
	private List<DatabaseConnection> openDbConns =
			new ArrayList<DatabaseConnection>();
	private boolean closed = false;
	private final Object lock = new Object();
	
	/**
	 * Constructs a new scheduled service.
	 */
	public ScheduledService() {
		AppComponents components = AppComponents.getInstance();
		ILoggerFactory logFactory = components.getComponent(
				ILoggerFactory.class);
		String logtag = null;
		try {
			logtag = (String)getClass().getField("LOGTAG").get(null);
		} catch (Exception ex) {}
		if (logtag == null)
			logtag = getClass().getSimpleName();
		if (logFactory != null)
			logger = logFactory.getLogger(logtag);
		else
			logger = LoggerFactory.getLogger(logtag);
		dbFactory = components.getComponent(DatabaseFactory.class);
	}
	
	/**
	 * Returns the interval in milliseconds to wait between running the task.
	 * If a {@link VirtualClock VirtualClock} is used, the task interval is
	 * interpreted as system time, not virtual time. This way the virtual
	 * clock can run at high speed, without causing high CPU load through
	 * very frequent task runs.
	 * 
	 * @return the task interval
	 */
	public int getTaskInterval() {
		ScheduledServiceProperties props = AppComponents.getInstance()
				.getComponent(ScheduledServiceProperties.class);
		return props.getTaskInterval();
	}

	/**
	 * Runs the task. If a fatal error occurs, this method can throw a
	 * {@link FatalException FatalException}, which will cause the service to
	 * be closed. If another exception is thrown, the error will be logged,
	 * but the task will be run again at the next occasion.
	 * 
	 * @throws FatalException if a fatal error occurs
	 * @throws Exception if a non-fatal error occurs
	 */
	public abstract void runTask() throws FatalException, Exception;

	/**
	 * Opens a new database connection. You should only call this if you have
	 * configured a {@link DatabaseFactory DatabaseFactory} in the {@link
	 * AppComponents AppComponents}. You should keep a connection only for the
	 * duration of a task, not for the lifetime of the service. The connection
	 * should be closed with {@link
	 * #closeDatabaseConnection(DatabaseConnection) closeDatabaseConnection()}.
	 * 
	 * @return the database connection
	 * @throws IOException if the connection can't be established
	 */
	public DatabaseConnection openDatabaseConnection() throws IOException {
		DatabaseConnection dbConn = dbFactory.connect();
		synchronized (lock) {
			if (closed) {
				dbConn.close();
				throw new IOException("Service closed");
			}
			openDbConns.add(dbConn);
		}
		return dbConn;
	}

	/**
	 * Closes the specified database connection. The connection should have
	 * been opened with {@link #openDatabaseConnection()
	 * openDatabaseConnection()}.
	 * 
	 * @param dbConn the database connection
	 */
	public void closeDatabaseConnection(DatabaseConnection dbConn) {
		synchronized (lock) {
			if (closed)
				return;
			dbConn.close();
			openDbConns.remove(dbConn);
		}
	}
	
	/**
	 * Closes this service. It closes any open database connections.
	 */
	public void close() {
		synchronized (lock) {
			if (!closed) {
				closed = true;
				for (DatabaseConnection dbConn : openDbConns) {
					dbConn.close();
				}
				openDbConns.clear();
			}
		}
	}
}
