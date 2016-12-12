package eu.ewall.platform.idss.dao.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseAction;
import eu.ewall.platform.idss.dao.DatabaseCriteria;

/**
 * This repository contains all database listeners within the current process.
 * Clients can add and remove listeners per database with {@link
 * #addDatabaseListener(String, DatabaseListener) addDatabaseListener()} and
 * {@link #removeDatabaseListener(String, DatabaseListener)
 * removeDatabaseListener()}.
 * 
 * <p>The class {@link Database Database} calls the notify methods. They should
 * not be called by clients.</p>
 * 
 * @author Dennis Hofs (RRD)
 */
public class DatabaseListenerRepository {
	private static final Object LOCK = new Object();
	private static DatabaseListenerRepository instance = null;
	
	private Map<String,List<DatabaseListener>> listeners =
			new HashMap<String,List<DatabaseListener>>();
	private Map<String,List<DatabaseActionListener>> actionListeners =
			new HashMap<String,List<DatabaseActionListener>>();
	
	/**
	 * This private constructor is used in {@link #getInstance()
	 * getInstance()}.
	 */
	private DatabaseListenerRepository() {
	}
	
	/**
	 * Returns the database listener repository.
	 * 
	 * @return the database listener repository
	 */
	public static DatabaseListenerRepository getInstance() {
		synchronized (LOCK) {
			if (instance == null)
				instance = new DatabaseListenerRepository();
			return instance;
		}
	}
	
	/**
	 * Adds a database listener.
	 * 
	 * @param database the database
	 * @param listener the listener
	 */
	public void addDatabaseListener(String database,
			DatabaseListener listener) {
		synchronized (LOCK) {
			List<DatabaseListener> ls = listeners.get(database);
			if (ls == null) {
				ls = new ArrayList<DatabaseListener>();
				listeners.put(database, ls);
			}
			ls.add(listener);
		}
	}
	
	/**
	 * Removes a database listener.
	 * 
	 * @param database the database
	 * @param listener the listener
	 */
	public void removeDatabaseListener(String database,
			DatabaseListener listener) {
		synchronized (LOCK) {
			List<DatabaseListener> ls = listeners.get(database);
			if (ls == null)
				return;
			ls.remove(listener);
			if (ls.isEmpty())
				listeners.remove(database);
		}
	}
	
	/**
	 * Adds a database action listener.
	 * 
	 * @param database the database
	 * @param listener the listener
	 */
	public void addDatabaseActionListener(String database,
			DatabaseActionListener listener) {
		synchronized (LOCK) {
			List<DatabaseActionListener> ls = actionListeners.get(database);
			if (ls == null) {
				ls = new ArrayList<DatabaseActionListener>();
				actionListeners.put(database, ls);
			}
			ls.add(listener);
		}
	}
	
	/**
	 * Removes a database action listener.
	 * 
	 * @param database the database
	 * @param listener the listener
	 */
	public void removeDatabaseActionListener(String database,
			DatabaseActionListener listener) {
		synchronized (LOCK) {
			List<DatabaseActionListener> ls = actionListeners.get(database);
			if (ls == null)
				return;
			ls.remove(listener);
			if (ls.isEmpty())
				actionListeners.remove(database);
		}
	}
	
	/**
	 * Called when an insert action is performed on the database. Registered
	 * listeners will be notified.
	 * 
	 * @param database the database
	 * @param table the table
	 * @param values the inserted values
	 */
	public void notifyInsert(String database, String table,
			List<Map<String,Object>> values) {
		List<DatabaseListener> ls = getDatabaseListeners(database);
		if (ls == null)
			return;
		for (DatabaseListener l : ls) {
			l.onInsert(database, table, values);
		}
	}
	
	/**
	 * Called when an update action is performed on the database. Registered
	 * listeners will be notified.
	 * 
	 * @param database the database
	 * @param table the table
	 * @param criteria the selection criteria
	 * @param values the new values
	 */
	public void notifyUpdate(String database, String table,
			DatabaseCriteria criteria, Map<String,?> values) {
		List<DatabaseListener> ls = getDatabaseListeners(database);
		if (ls == null)
			return;
		for (DatabaseListener l : ls) {
			l.onUpdate(database, table, criteria, values);
		}
	}
	
	/**
	 * Called when a delete action is performed on the database. Registered
	 * listeners will be notified.
	 * 
	 * @param database the database
	 * @param table the table
	 * @param criteria the selection criteria
	 */
	public void notifyDelete(String database, String table,
			DatabaseCriteria criteria) {
		List<DatabaseListener> ls = getDatabaseListeners(database);
		if (ls == null)
			return;
		for (DatabaseListener l : ls) {
			l.onDelete(database, table, criteria);
		}
	}
	
	/**
	 * Called when one or more database actions are added. All actions in the
	 * specified list should have the same type {@link
	 * nl.rrd.r2d2.dao.DatabaseAction.Action DatabaseAction.Action}.
	 * 
	 * @param database the database
	 * @param table the table
	 * @param actions the new database actions
	 */
	public void notifyAddDatabaseActions(String database, String table,
			List<DatabaseAction> actions) {
		List<DatabaseActionListener> ls = getDatabaseActionListeners(database);
		if (ls == null)
			return;
		for (DatabaseActionListener l : ls) {
			l.onAddDatabaseActions(database, table, actions);
		}
	}

	/**
	 * Returns all database listeners for the specified database. It returns
	 * a copy of the list, so it can be read outside the lock. If there are
	 * no listeners on the database, this method returns null.
	 * 
	 * @param database the database
	 * @return the database listeners or null
	 */
	private List<DatabaseListener> getDatabaseListeners(String database) {
		synchronized (LOCK) {
			List<DatabaseListener> ls = listeners.get(database);
			if (ls == null)
				return null;
			return new ArrayList<DatabaseListener>(ls);
		}
	}
	
	/**
	 * Returns all database action listeners for the specified database. It
	 * returns a copy of the list, so it can be read outside the lock. If there
	 * are no listeners on the database, this method returns null.
	 * 
	 * @param database the database
	 * @return the database action listeners or null
	 */
	private List<DatabaseActionListener> getDatabaseActionListeners(
			String database) {
		synchronized (LOCK) {
			List<DatabaseActionListener> ls = actionListeners.get(database);
			if (ls == null)
				return null;
			return new ArrayList<DatabaseActionListener>(ls);
		}
	}
}
