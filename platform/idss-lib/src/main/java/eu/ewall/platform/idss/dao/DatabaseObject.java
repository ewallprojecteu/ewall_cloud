package eu.ewall.platform.idss.dao;

/**
 * A database object can be read from or written to a {@link Database
 * Database}. It defines at least one field: id. Other fields should be
 * annotated with {@link DatabaseField DatabaseField}. The "id" field should
 * not be annotated. See {@link DatabaseObjectMapper DatabaseObjectMapper} for
 * information about the mapping between Java types and database types.
 *
 * <p>If you add a "user" field, it will have a special meaning when database
 * actions are saved for audit logging or synchronisation with a remote
 * database. It means that the object belongs to the specified user and can be
 * used if only the data for one user should be synchronised.</p>
 * 
 * @author Dennis Hofs (RRD)
 */
public interface DatabaseObject {
	
	/**
	 * Returns the ID.
	 * 
	 * @return the ID
	 */
	String getId();
	
	/**
	 * Sets the ID.
	 * 
	 * @param id the ID
	 */
	void setId(String id);
}
