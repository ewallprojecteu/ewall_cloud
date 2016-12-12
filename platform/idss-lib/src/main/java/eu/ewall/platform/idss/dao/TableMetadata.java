package eu.ewall.platform.idss.dao;

/**
 * This class defines metadata about a database table. See also {@link
 * TableMetadataTableDef TableMetadataTableDef}.
 * 
 * @author Dennis Hofs (RRD)
 */
public class TableMetadata extends AbstractDatabaseObject {

	/**
	 * The value is the current version of the specified table.
	 */
	public static final String KEY_VERSION = "version";

	/**
	 * The value is a JSON array with the names of all database fields in the
	 * specified table, excluding "id".
	 */
	public static final String KEY_FIELDS = "fields";
	
	@DatabaseField(value=DatabaseType.STRING)
	private String table;

	@DatabaseField(value=DatabaseType.STRING)
	private String key;

	@DatabaseField(value=DatabaseType.TEXT)
	private String value;

	/**
	 * Returns the table name (lower case).
	 * 
	 * @return the table name (lower case)
	 */
	public String getTable() {
		return table;
	}

	/**
	 * Sets the table name (lower case).
	 * 
	 * @param table the table name (lower case)
	 */
	public void setTable(String table) {
		this.table = table;
	}

	/**
	 * Returns the metadata key. This should be one of the KEY_* constants
	 * defined in this class.
	 * 
	 * @return the metadata key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Sets the metadata key. This should be one of the KEY_* constants defined
	 * in this class.
	 * 
	 * @param key the metadata key
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * Returns the metadata value.
	 * 
	 * @return the metadata value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the metadata value.
	 * 
	 * @param value the metadata value
	 */
	public void setValue(String value) {
		this.value = value;
	}
}
