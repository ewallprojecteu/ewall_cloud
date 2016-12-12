package eu.ewall.platform.idss.dao.memdb;

import java.util.Map;

/**
 * A record in a {@link MemoryDatabase MemoryDatabase}.
 * 
 * @author Dennis Hofs (RRD)
 */
public class MemoryDatabaseRecord {
	private String id;
	private Map<String,Object> fields; // includes "id"
	
	/**
	 * Constructs a new record.
	 * 
	 * @param id the record ID
	 * @param fields the record fields (including "id")
	 */
	public MemoryDatabaseRecord(String id, Map<String,Object> fields) {
		this.id = id;
		this.fields = fields;
	}
	
	/**
	 * Returns the record ID.
	 * 
	 * @return the record ID
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Returns the record fields. This includes "id".
	 * 
	 * @return the record fields
	 */
	public Map<String,Object> getFields() {
		return fields;
	}
	
	/**
	 * Returns the value of the field with the specified name.
	 * 
	 * @param name the field name (can be "id")
	 * @return the field value
	 */
	public Object getField(String name) {
		return fields.get(name);
	}
	
	/**
	 * Sets the value of the field with the specified name.
	 * 
	 * @param name the field name (can be "id")
	 * @param value the field value
	 */
	public void setField(String name, Object value) {
		if (name.equals("id"))
			id = (String)value;
		fields.put(name, value);
	}
}
