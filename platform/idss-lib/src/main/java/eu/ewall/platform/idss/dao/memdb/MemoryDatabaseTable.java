package eu.ewall.platform.idss.dao.memdb;

import eu.ewall.platform.idss.dao.DatabaseCriteria;
import eu.ewall.platform.idss.dao.DatabaseException;
import eu.ewall.platform.idss.dao.DatabaseSort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Table in a {@link MemoryDatabase MemoryDatabase}.
 * 
 * @author Dennis Hofs (RRD)
 */
public class MemoryDatabaseTable {
	private String name;
	private List<MemoryDatabaseRecord> records =
			new ArrayList<MemoryDatabaseRecord>();
	private int nextId = 1;
	private Object lock = new Object();
	
	/**
	 * Constructs a new table.
	 * 
	 * @param name the table name
	 */
	public MemoryDatabaseTable(String name) {
		this.name = name;
	}
	
	/**
	 * Returns the table name.
	 * 
	 * @return the table name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Inserts records into this table. A record may include an "id" field. If
	 * not, this method will generate the ID.
	 * 
	 * @param values the records
	 * @throws DatabaseException if a record has an ID that already exists
	 */
	public void insert(List<Map<String, Object>> values)
			throws DatabaseException {
		synchronized (lock) {
			for (Map<String,Object> record : values) {
				String id = (String)record.get("id");
				Integer idNum = null;
				try {
					idNum = new Integer(id);
				} catch (NumberFormatException ex) {}
				if (id == null) {
					id = Integer.toString(nextId++);
					record.put("id", id);
				} else if (idNum != null && idNum >= nextId) {
					nextId = idNum + 1;
				} else {
					for (MemoryDatabaseRecord dbRec : records) {
						if (dbRec.getId().equals(id)) {
							throw new DatabaseException("ID \"" + id +
									"\" already exists");
						}
					}
				}
				records.add(new MemoryDatabaseRecord(id, record));
			}
		}
	}
	
	/**
	 * Selects records for the specified parameters. Each returned record has
	 * an "id" field.
	 * 
	 * @param criteria the criteria that each record should match
	 * @param limit the maximum number of items to return (0 or less means no
	 * limit)
	 * @param sort the sort properties
	 * @return the records
	 */
	public List<Map<String,?>> select(DatabaseCriteria criteria, int limit,
			DatabaseSort[] sort) {
		synchronized (lock) {
			CriteriaMatcher matcher = new CriteriaMatcher();
			List<Map<String,?>> result = new ArrayList<Map<String,?>>();
			for (MemoryDatabaseRecord record : records) {
				if (criteria == null || matcher.matches(record, criteria))
					result.add(record.getFields());
			}
			if (sort != null && sort.length > 0)
				Collections.sort(result, new SortRecordComparator(sort));
			if (limit <= 0)
				return result;
			else {
				if (limit > result.size())
					limit = result.size();
				return result.subList(0, limit);
			}
		}
	}
	
	/**
	 * Counts the number of records that match the specified criteria.
	 * 
	 * @param criteria the criteria
	 * @return the number of records
	 */
	public int count(DatabaseCriteria criteria) {
		synchronized (lock) {
			CriteriaMatcher matcher = new CriteriaMatcher();
			int count = 0;
			for (MemoryDatabaseRecord record : records) {
				if (criteria == null || matcher.matches(record, criteria))
					count++;
			}
			return count;
		}
	}
	
	/**
	 * Updates records that match the specified criteria.
	 * 
	 * @param criteria the criteria
	 * @param values values of columns that should be changed
	 * @throws DatabaseException if the ID is changed to null or an existing ID
	 */
	public void update(DatabaseCriteria criteria, Map<String,?> values)
			throws DatabaseException {
		synchronized (lock) {
			CriteriaMatcher matcher = new CriteriaMatcher();
			for (MemoryDatabaseRecord record : records) {
				if (criteria == null || matcher.matches(record, criteria))
					updateRecord(record, values);
			}
		}
	}
	
	/**
	 * Updates the specified record.
	 * 
	 * @param record the record
	 * @param values values of columns that should be changed
	 * @throws DatabaseException if the ID is changed to null or an existing ID
	 */
	private void updateRecord(MemoryDatabaseRecord record, Map<String,?> values)
			throws DatabaseException {
		String newId = null;
		if (values.containsKey("id")) {
			Object idObj = values.get("id");
			if (idObj == null)
				throw new DatabaseException("ID cannot be null");
			newId = idObj.toString();
		}
		if (newId != null) {
			for (MemoryDatabaseRecord other : records) {
				if (other == record)
					continue;
				if (other.getId().equals(newId)) {
					throw new DatabaseException("ID \"" + newId +
							"\" already exists");
				}
			}
		}
		for (String key : values.keySet()) {
			record.setField(key, values.get(key));
		}
	}
	
	/**
	 * Deletes all records that match the specified criteria.
	 * 
	 * @param criteria the criteria
	 */
	public void delete(DatabaseCriteria criteria) {
		synchronized (lock) {
			CriteriaMatcher matcher = new CriteriaMatcher();
			Iterator<MemoryDatabaseRecord> it = records.iterator();
			while (it.hasNext()) {
				MemoryDatabaseRecord record = it.next();
				if (criteria == null || matcher.matches(record, criteria))
					it.remove();
			}
		}
	}
}
