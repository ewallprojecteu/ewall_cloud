package eu.ewall.platform.idss.dao;

import eu.ewall.platform.idss.utils.json.JsonObject;

/**
 * This class consists of a user name and a table name. It can be used as a key
 * in a hash map.
 * 
 * @author Dennis Hofs (RRD)
 */
public class UserTable extends JsonObject {
	private String user;
	private String table;
	
	/**
	 * Constructs a new instance.
	 * 
	 * @param user the user name or null
	 * @param table the table name
	 */
	public UserTable(String user, String table) {
		this.user = user;
		this.table = table;
	}

	/**
	 * Returns the user name. This can be null.
	 * 
	 * @return the user name or null
	 */
	public String getUser() {
		return user;
	}

	/**
	 * Returns the table name.
	 * 
	 * @return the table name
	 */
	public String getTable() {
		return table;
	}

	@Override
	public int hashCode() {
		return user == null ? 0 : user.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserTable other = (UserTable) obj;
		if ((user == null) != (other.user == null))
			return false;
		if (user != null && !user.equals(other.user))
			return false;
		if (!table.equals(other.table))
			return false;
		return true;
	}
}
