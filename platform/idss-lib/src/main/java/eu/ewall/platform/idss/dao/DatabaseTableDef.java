package eu.ewall.platform.idss.dao;

import java.util.ArrayList;
import java.util.List;

/**
 * This class models the definition of a database table. It consists of a table
 * name, data class and current version number. Subclasses should implement
 * {@link #upgradeTable(int, Database) upgradeTable()} so that a database can
 * be automatically upgraded from any previous version to the latest version.
 * Table names that start with an underscore are reserved.
 * 
 * @author Dennis Hofs (RRD)
 */
public abstract class DatabaseTableDef<T extends DatabaseObject> {
	private String name;
	private Class<T> dataClass;
	private List<DatabaseIndex> compoundIndices;
	private int currentVersion;

	/**
	 * Constructs a new database table definition.
	 * 
	 * @param name the table name (lower case). Names that start with an
	 * underscore are reserved.
	 * @param dataClass the class of database objects that are stored in the
	 * table
	 * @param currentVersion the current version of this table definition
	 */
	public DatabaseTableDef(String name, Class<T> dataClass,
			int currentVersion) {
		this.name = name;
		this.dataClass = dataClass;
		this.currentVersion = currentVersion;
		this.compoundIndices = new ArrayList<DatabaseIndex>();
	}
	
	/**
	 * Returns the table name (lower case). Names that start with an underscore
	 * are reserved.
	 * 
	 * @return the table name (lower case)
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the class of database objects that are stored in the table.
	 * 
	 * @return the data class
	 */
	public Class<T> getDataClass() {
		return dataClass;
	}

	/**
	 * Returns the current version of this table definition.
	 * 
	 * @return the current version of this table definition
	 */
	public int getCurrentVersion() {
		return currentVersion;
	}
	
	/**
	 * Adds a compound index to this table. Single-field indices can be defined
	 * with {@link DatabaseField DatabaseField}.
	 * 
	 * @param index the compound index
	 */
	public void addCompoundIndex(DatabaseIndex index) {
		compoundIndices.add(index);
	}
	
	/**
	 * Returns the compound indices for this table.
	 * 
	 * @return the compound indices
	 */
	public List<DatabaseIndex> getCompoundIndices() {
		return compoundIndices;
	}
	
	/**
	 * Upgrades the database table from the specified version to the next
	 * version. This method should support all previous versions, so the table
	 * can be upgraded to the current version, possibly through multiple calls
	 * of this method with increasing versions.
	 * 
	 * @param version the version of this table in the specified database
	 * @param db the database
	 * @return the version after the upgrade
	 * @throws DatabaseException if a database error occurs
	 */
	public abstract int upgradeTable(int version, Database db)
			throws DatabaseException;

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DatabaseTableDef<?> other = (DatabaseTableDef<?>)obj;
		if (currentVersion != other.currentVersion)
			return false;
		if (dataClass != other.dataClass)
			return false;
		if (!name.equals(other.name))
			return false;
		return true;
	}
}
