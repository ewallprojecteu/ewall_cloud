package eu.ewall.platform.idss.dao;

import eu.ewall.platform.idss.utils.beans.PropertySpec;

/**
 * The specification of a database field in a {@link DatabaseObject
 * DatabaseObject} class. Instances of this class can be obtained from {@link
 * DatabaseFieldScanner DatabaseFieldScanner}.
 * 
 * @author Dennis Hofs (RRD)
 */
public class DatabaseFieldSpec {
	private PropertySpec propSpec = null;
	private DatabaseField dbField = null;
	
	/**
	 * Constructs a new database field specification.
	 * 
	 * @param propSpec the property specification
	 * @param dbField the database field
	 */
	public DatabaseFieldSpec(PropertySpec propSpec, DatabaseField dbField) {
		this.propSpec = propSpec;
		this.dbField = dbField;
	}
	
	/**
	 * Returns the property specification.
	 * 
	 * @return the property specification
	 */
	public PropertySpec getPropSpec() {
		return propSpec;
	}

	/**
	 * Returns the database field.
	 * 
	 * @return the database field
	 */
	public DatabaseField getDbField() {
		return dbField;
	}

	@Override
	public String toString() {
		return "DatabaseFieldSpec [name=" + propSpec.getName() + "]";
	}
}
