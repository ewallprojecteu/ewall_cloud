package eu.ewall.platform.idss.dao.sync;

import com.fasterxml.jackson.annotation.JsonIgnore;

import eu.ewall.platform.idss.dao.AbstractDatabaseObject;
import eu.ewall.platform.idss.dao.DatabaseAction;
import eu.ewall.platform.idss.dao.DatabaseField;
import eu.ewall.platform.idss.dao.DatabaseType;

/**
 * A SyncProgress object is stored in the database to track what database
 * actions ({@link DatabaseAction DatabaseAction}) have already been
 * synchronised from a remote database to the local database. This object
 * contains the time and order number of the last action that was successfully
 * synchronised. Progress is tracked per table and optionally per user. Tracking
 * per user is only done if:
 *
 * <p><ul>
 * <li>The table has a "user" field (each record belongs to a user), and</li>
 * <li>The local database holds data for multiple users and synchronisation
 * is done separately for them. Normally this applies to the server, which has
 * data for all users. Client devices have data for one user and they
 * synchronise with the server independently.</li>
 * </ul></p>
 *
 * @author Dennis Hofs (RRD)
 */
public class SyncProgress extends AbstractDatabaseObject {
	@JsonIgnore
	private String id;
	
	@DatabaseField(value=DatabaseType.STRING)
	private String table;
	@DatabaseField(value=DatabaseType.STRING, index=true)
	private String user = null;
	// "time" is stored as LONG instead of ISOTIME so times can be compared
	// with GreaterThan / LessThan
	@DatabaseField(value=DatabaseType.LONG)
	private long time;
	@DatabaseField(value=DatabaseType.INT)
	private int order;

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Returns the table name.
	 *
	 * @return the table name (lower case)
	 */
	public String getTable() {
		return table;
	}

	/**
	 * Sets the table name.
	 *
	 * @param table the table name (lower case)
	 */
	public void setTable(String table) {
		this.table = table;
	}

	/**
	 * Returns the user name. See the top of this page for more information.
	 *
	 * @return the user name or null
	 */
	public String getUser() {
		return user;
	}

	/**
	 * Sets the user name. See the top of this page for more information.
	 *
	 * @param user the user name or null (default)
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * Returns the time of the last database action that was synchronised.
	 *
	 * @return the time of the last database action that was synchronised
	 */
	public long getTime() {
		return time;
	}

	/**
	 * Sets the time of the last database action that was synchronised.
	 *
	 * @param time the time of the last database action that was synchronised
	 */
	public void setTime(long time) {
		this.time = time;
	}

	/**
	 * Returns the order number of the last database action that was
	 * synchronised.
	 *
	 * @return the order number of the last database action that was
	 * synchronised
	 */
	public int getOrder() {
		return order;
	}

	/**
	 * Sets the order number of the last database action that was synchronised.
	 *
	 * @param order the order number of the last database action that was
	 * synchronised
	 */
	public void setOrder(int order) {
		this.order = order;
	}
}
