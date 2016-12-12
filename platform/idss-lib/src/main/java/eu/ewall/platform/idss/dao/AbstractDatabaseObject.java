package eu.ewall.platform.idss.dao;

import eu.ewall.platform.idss.utils.DataFormatter;

import java.util.Map;

/**
 * Base implementation of {@link DatabaseObject DatabaseObject}. It implements
 * getId() and setId() and provides a toString() that returns the values of all
 * database fields and a hashCode() and equals() that work on the map that is
 * generated from this object using a {@link DatabaseObjectMapper
 * DatabaseObjectMapper}.
 *
 * <p>If you add a "user" field, it will have a special meaning when database
 * actions are saved for audit logging or synchronisation with a remote
 * database. It means that the object belongs to the specified user and can be
 * used if only the data for one user should be synchronised.</p>
 *
 * @author Dennis Hofs (RRD)
 */
public abstract class AbstractDatabaseObject implements DatabaseObject,
		Cloneable {
	private String id;
	
	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return toString(false);
	}
	
	/**
	 * Returns a string representation of this object. If "human" is true, the
	 * returned string will have a friendly formatting, possibly spanning
	 * multiple lines.
	 * 
	 * @param human true for friendly formatting, false for single-line
	 * formatting
	 * @return the string
	 */
	public String toString(boolean human) {
		DatabaseObjectMapper mapper = new DatabaseObjectMapper();
		Map<String,Object> map = mapper.objectToMap(this);
		DataFormatter formatter = new DataFormatter();
		return getClass().getSimpleName() + " " + formatter.format(map, human);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		DatabaseObjectMapper mapper = new DatabaseObjectMapper();
		Map<String,Object> map = mapper.objectToMap(this);
		result = prime * result + map.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractDatabaseObject other = (AbstractDatabaseObject)obj;
		DatabaseObjectMapper mapper = new DatabaseObjectMapper();
		Map<String,Object> map = mapper.objectToMap(this);
		Map<String,Object> otherMap = mapper.objectToMap(other);
		if (!map.equals(otherMap))
			return false;
		return true;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		DatabaseObjectMapper mapper = new DatabaseObjectMapper();
		return mapper.mapToObject(mapper.objectToMap(this), getClass());
	}
}
