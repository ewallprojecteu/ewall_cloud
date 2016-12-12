package eu.ewall.platform.idss.dao.memdb;

import eu.ewall.platform.idss.dao.Database;
import eu.ewall.platform.idss.dao.DatabaseColumnDef;
import eu.ewall.platform.idss.dao.DatabaseCriteria;
import eu.ewall.platform.idss.dao.DatabaseException;
import eu.ewall.platform.idss.dao.DatabaseIndex;
import eu.ewall.platform.idss.dao.DatabaseSort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple database implementation that keeps all data in memory. The data is
 * lost when the application exits.
 * 
 * @author Dennis Hofs (RRD)
 */
public class MemoryDatabase extends Database {
	private Map<String,MemoryDatabaseTable> tables =
			new HashMap<String,MemoryDatabaseTable>();
	private final Object lock = new Object();
	
	public MemoryDatabase(String name) {
		super(name);
	}

	@Override
	public void createTable(String table, List<DatabaseColumnDef> columns)
			throws DatabaseException {
		synchronized (lock) {
			if (tables.containsKey(table)) {
				throw new DatabaseException("Table \"" + table +
						"\" already exists");
			}
			tables.put(table, new MemoryDatabaseTable(table));
		}
	}

	@Override
	public void createIndex(String table, DatabaseIndex index)
			throws DatabaseException {
	}

	@Override
	public void dropIndex(String table, String name) throws DatabaseException {
	}

	@Override
	public void addColumn(String table, DatabaseColumnDef column)
			throws DatabaseException {
	}

	@Override
	public void dropColumn(String table, String column)
			throws DatabaseException {
	}

	@Override
	public void renameColumn(String table, String oldName, String newName)
			throws DatabaseException {
	}

	@Override
	protected List<String> selectDbTables() throws DatabaseException {
		synchronized (lock) {
			List<String> result = new ArrayList<String>(tables.keySet());
			Collections.sort(result);
			return result;
		}
	}

	@Override
	protected void dropDbTable(String table) throws DatabaseException {
		synchronized (lock) {
			tables.remove(table);
		}
	}

	@Override
	public void beginTransaction() throws DatabaseException {
	}

	@Override
	public void commitTransaction() throws DatabaseException {
	}

	@Override
	protected void doInsertMaps(String table, List<Map<String, Object>> values)
			throws DatabaseException {
		synchronized (lock) {
			MemoryDatabaseTable t = tables.get(table);
			if (t == null) {
				throw new DatabaseException("Table \"" + table +
						"\" not found");
			}
			t.insert(values);
		}
	}

	@Override
	protected List<Map<String, ?>> doSelectMaps(String table,
			DatabaseCriteria criteria, int limit, DatabaseSort[] sort)
			throws DatabaseException {
		synchronized (lock) {
			MemoryDatabaseTable t = tables.get(table);
			if (t == null) {
				throw new DatabaseException("Table \"" + table +
						"\" not found");
			}
			return t.select(criteria, limit, sort);
		}
	}

	@Override
	public int count(String table, DatabaseCriteria criteria)
			throws DatabaseException {
		synchronized (lock) {
			MemoryDatabaseTable t = tables.get(table);
			if (t == null) {
				throw new DatabaseException("Table \"" + table +
						"\" not found");
			}
			return t.count(criteria);
		}
	}

	@Override
	protected void doUpdate(String table, DatabaseCriteria criteria,
			Map<String, ?> values) throws DatabaseException {
		synchronized (lock) {
			MemoryDatabaseTable t = tables.get(table);
			if (t == null) {
				throw new DatabaseException("Table \"" + table +
						"\" not found");
			}
			t.update(criteria, values);
		}
	}

	@Override
	protected void doDelete(String table, DatabaseCriteria criteria)
			throws DatabaseException {
		synchronized (lock) {
			MemoryDatabaseTable t = tables.get(table);
			if (t == null) {
				throw new DatabaseException("Table \"" + table +
						"\" not found");
			}
			t.delete(criteria);
		}
	}

	@Override
	protected List<? extends Map<String,?>> selectLogRecords(String table,
			DatabaseCriteria criteria) throws DatabaseException {
		synchronized (lock) {
			MemoryDatabaseTable t = tables.get(table);
			if (t == null) {
				throw new DatabaseException("Table \"" + table +
						"\" not found");
			}
			return t.select(criteria, 0, null);
		}
	}
}
