package eu.ewall.platform.idss.dao.listener;

import java.util.List;
import java.util.Map;

import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseCriteria;

/**
 * A database listener can be notified when an action is performed on a
 * {@link Database Database}. Listeners can be registered in the {@link
 * DatabaseListenerRepository DatabaseListenerRepository}.
 * 
 * @author Dennis Hofs (RRD)
 */
public interface DatabaseListener {
	
	/**
	 * Called when an insert action is performed on the database.
	 * 
	 * @param database the database
	 * @param table the table
	 * @param values the inserted values
	 */
	void onInsert(String database, String table,
			List<Map<String,Object>> values);

	/**
	 * Called when an update action is performed on the database.
	 * 
	 * @param database the database
	 * @param table the table
	 * @param criteria the selection criteria
	 * @param values the new values
	 */
	void onUpdate(String database, String table, DatabaseCriteria criteria,
			Map<String,?> values);
	
	/**
	 * Called when a delete action is performed on the database.
	 * 
	 * @param database the database
	 * @param table the table
	 * @param criteria the selection criteria
	 */
	void onDelete(String database, String table, DatabaseCriteria criteria);
}
