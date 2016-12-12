package eu.ewall.platform.idss.dao;

/**
 * This object maps a user/table combination to a key string. This is used in
 * table names when separate database tables are used for each user/table
 * combination.
 * 
 * @author Dennis Hofs (RRD)
 */
public class UserTableKey extends AbstractDatabaseObject {
	@DatabaseField(value=DatabaseType.STRING, index=true)
	private String user = null;

	@DatabaseField(value=DatabaseType.STRING)
	private String table;

	@DatabaseField(value=DatabaseType.STRING)
	private String key;

	/**
	 * Returns the user. This can be null for tables that don't apply to a
	 * user.
	 * 
	 * @return the user or null
	 */
	public String getUser() {
		return user;
	}

	/**
	 * Sets the user. This can be null for tables that don't apply to a user.
	 * 
	 * @param user the user or null
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * Returns the table name.
	 * 
	 * @return the table name
	 */
	public String getTable() {
		return table;
	}

	/**
	 * Sets the table name.
	 * 
	 * @param table the table name
	 */
	public void setTable(String table) {
		this.table = table;
	}

	/**
	 * Returns the key string.
	 * 
	 * @return the key string
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Sets the key string.
	 * 
	 * @param key the key string
	 */
	public void setKey(String key) {
		this.key = key;
	}
}
